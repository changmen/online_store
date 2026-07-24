import argparse
import json
import logging
import os

import jsonlines
from git import Repo

logging.basicConfig(level=logging.INFO, format="[%(asctime)s] %(message)s", datefmt="%H:%M:%S")
logger = logging.getLogger(__name__)


def read_blob_content(tree, path: str) -> str:
    """读取指定 tree 中文件的内容，文件不存在时返回空字符串"""
    try:
        blob = tree / path
        return blob.data_stream.read().decode("utf-8", errors="replace")
    except KeyError:
        return ""


def extract_file_data(diff, commit, parent_commit) -> dict:
    """从单个 diff 对象提取结构化的文件变更数据"""
    change_type = diff.change_type
    if diff.new_file:
        change_type = "A"  # Added
    elif diff.deleted_file:
        change_type = "D"  # Deleted
    elif diff.renamed_file:
        change_type = "R"  # Renamed

    file_data = {
        "path": diff.b_path if diff.b_path else diff.a_path,
        "old_path": diff.a_path,
        "change_type": change_type,
        "mode": {"old": diff.a_mode, "new": diff.b_mode},
        "diff": diff.diff.decode("utf-8", errors="replace") if diff.diff else "",
        "new_file_content": read_blob_content(commit.tree, diff.b_path) if diff.b_path else "",
    }
    if not diff.new_file and diff.a_path:
        file_data["old_file_content"] = read_blob_content(parent_commit.tree, diff.a_path)
    return file_data


def get_commit_diffs(repo_path: str = ".", max_commits: int = 10) -> list[dict]:
    """
    获取 Git 仓库提交历史中的差异详情
    :param repo_path: Git仓库路径 (默认当前目录)
    :param max_commits: 最大获取提交数
    :return: 结构化差异数据列表
    """
    repo = Repo(repo_path)
    diffs_data = []

    logger.info(f"开始处理仓库 {repo_path}，最多 {max_commits} 个提交")
    commits = list(repo.iter_commits(max_count=max_commits))
    for commit in commits:
        try:
            # 跳过没有父提交的初始提交
            if not commit.parents:
                continue
            parent_commit = commit.parents[0]

            logger.info(f"提交：{commit.hexsha[:7]} - {commit.message.strip()}")
            diffs = parent_commit.diff(commit, create_patch=True, unified=3)

            commit_data = {
                "hash": commit.hexsha,
                "author": f"{commit.author.name} <{commit.author.email}>",
                "date": commit.authored_datetime.isoformat(),
                "message": commit.message.strip(),
                "stats": {"total": commit.stats.total},
                "files": [],
            }

            for diff in diffs:
                # 跳过二进制文件，避免乱码污染数据集
                if diff.diff and b"\x00" in diff.diff[:8000]:
                    continue
                commit_data["files"].append(extract_file_data(diff, commit, parent_commit))

            diffs_data.append(commit_data)

        except Exception as e:
            logger.error(f"处理提交 {commit.hexsha[:7]} 失败: {e}")

    return diffs_data


def write_diff_to_file(diff_data: list[dict], output_file: str, prefix: str | list[str] = "E."):
    """按提交信息前缀过滤，并写入 JSONL 文件。prefix 支持单个字符串或多个前缀列表"""
    prefixes = tuple([prefix] if isinstance(prefix, str) else prefix)
    os.makedirs(os.path.dirname(output_file), exist_ok=True)
    review_datasets = []
    filtered = 0
    for commit in diff_data:
        if not commit["files"]:
            continue
        message = commit["message"]
        if not message.startswith(prefixes):
            filtered += 1
            continue

        item = {
            "message": message,
            "category_label": message.split(" ", 1)[0],
            "patches": [
                {
                    "path": file["path"],
                    "old_file_content": file.get("old_file_content", ""),
                    "new_file_content": file["new_file_content"],
                    "patch": file["diff"],
                }
                for file in commit["files"]
            ],
        }
        review_datasets.append(item)

    with jsonlines.open(output_file, "w") as f:
        f.write_all(review_datasets)

    logger.info(f"写入完成：{len(review_datasets)} 条记录，过滤 {filtered} 个提交")


def print_summary(diff_data: list[dict], summary_file: str | None = None) -> dict:
    """统计并打印差异数据的概要信息，返回统计结果字典"""
    total_commits = len(diff_data)
    total_files = 0
    change_type_counts: dict[str, int] = {}

    for commit in diff_data:
        files = commit.get("files", [])
        total_files += len(files)
        for file in files:
            change_type = file.get("change_type", "?")
            change_type_counts[change_type] = change_type_counts.get(change_type, 0) + 1

    logger.info("=" * 40)
    logger.info(f"概要：共 {total_commits} 个提交，{total_files} 个文件变更")
    for change_type, count in sorted(change_type_counts.items()):
        logger.info(f"  变更类型 {change_type}: {count} 个")
    logger.info("=" * 40)

    summary = {
        "total_commits": total_commits,
        "total_files": total_files,
        "change_type_counts": change_type_counts,
    }

    if summary_file:
        os.makedirs(os.path.dirname(summary_file), exist_ok=True)
        with open(summary_file, "w", encoding="utf-8") as f:
            json.dump(summary, f, ensure_ascii=False, indent=2)
        logger.info(f"概要已写入：{summary_file}")

    return summary


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Extract git commit diffs into jsonlines")
    parser.add_argument("--repo", default=os.path.abspath(os.path.dirname(__file__)), help="Git 仓库路径")
    parser.add_argument("--max-commits", type=int, default=100, help="最大处理提交数")
    parser.add_argument("--output", default="output/test.jsonl", help="输出文件路径")
    parser.add_argument("--prefix", default="E.", help="提交信息过滤前缀，多个用逗号分隔")
    parser.add_argument("--summary", default=None, help="概要统计输出的 JSON 文件路径")
    args = parser.parse_args()

    prefixes = [p.strip() for p in args.prefix.split(",") if p.strip()]
    diffs = get_commit_diffs(args.repo, max_commits=args.max_commits)
    write_diff_to_file(diffs, output_file=args.output, prefix=prefixes)
    print_summary(diffs, summary_file=args.summary)
