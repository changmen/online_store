import os
from datetime import datetime
from git import Repo
from typing import List, Dict
import jsonlines

EMPTY_TREE_SHA = "4b825dc642cb6eb9a060e54bf8d69288fbee4904"


def get_commit_diffs(repo_path: str = ".", max_commits: int = 10) -> List[Dict]:
    """
    获取 Git 仓库提交历史中的差异详情
    :param repo_path: Git仓库路径 (默认当前目录)
    :param max_commits: 最大获取提交数
    :return: 结构化差异数据列表
    """
    repo = Repo(repo_path)
    diffs_data = []

    print(f"开始处理仓库 {repo_path}，最多 {max_commits} 个提交")
    commits = list(repo.iter_commits(max_count=max_commits))
    # commits.reverse()
    for commit in commits:
        try:
            # 获取当前提交的父提交（处理初始提交）
            if not commit.parents:
                continue
            parent_commit = commit.parents[0]

            print(f"[{datetime.now().strftime('%H:%M:%S')}] 提交：{commit.hexsha[:7]} - {commit.message.strip()}, parent:{parent_commit}")
            # 获取差异对象列表
            diffs = parent_commit.diff(commit,
                                       create_patch=True,  # 包含完整差异内容
                                       unified=3)  # 上下文行数

            commit_data = {
                "hash": commit.hexsha,
                "author": f"{commit.author.name} <{commit.author.email}>",
                "date": commit.authored_datetime.isoformat(),
                "message": commit.message.strip(),
                "stats": {
                    "total": {
                        "insertions": commit.stats.total["insertions"],
                        "deletions": commit.stats.total["deletions"],
                        "files": commit.stats.total["files"]
                    }
                },
                "files": []
            }

            for diff in diffs:
                # 解析差异类型
                change_type = diff.change_type
                if diff.new_file:
                    change_type = "A"  # Added
                elif diff.deleted_file:
                    change_type = "D"  # Deleted
                elif diff.renamed_file:
                    change_type = "R"  # Renamed

                # 解析差异内容
                diff_content = diff.diff.decode('utf-8', errors='replace') if diff.diff else ""
                # patches = parse_diff_patches(diff_content)

                file_data = {
                    "path": diff.b_path if diff.b_path else diff.a_path,
                    "old_path": diff.a_path,
                    "change_type": change_type,
                    "mode": {
                        "old": diff.a_mode,
                        "new": diff.b_mode
                    },
                    "diff": diff_content
                }
                if diff.b_path:
                    try:
                        file_blob = commit.tree / diff.b_path
                        file_data["new_file_content"] = file_blob.data_stream.read().decode('utf-8', errors='replace')
                    except KeyError:
                        file_data["new_file_content"] = ""
                else:
                    file_data["new_file_content"] = ""
                if not diff.new_file and diff.a_path:
                    try:
                        old_file_blob = parent_commit.tree / diff.a_path
                        file_data["old_file_content"] = old_file_blob.data_stream.read().decode('utf-8', errors='replace')
                    except KeyError:
                        file_data["old_file_content"] = ""
                commit_data["files"].append(file_data)

            diffs_data.append(commit_data)

        except Exception as e:
            print(f"Error processing commit {commit.hexsha[:7]}: {str(e)}")
            continue

    return diffs_data


def write_diff_to_file(diff_data: List[Dict], output_file="output/test.jsonl"):
    """过滤并提交数据写入 JSONL 文件"""
    review_datasets = []
    filtered = 0
    for commit in diff_data:
        if not commit["files"]:
            continue
        message = commit["message"]
        if not message.startswith("E."):
            filtered += 1
            continue
        item = {
            "message": message,
            "patches": [],
            "category_label": str.split(message, " ", 1)[0]
        }

        for file in commit["files"]:
            patch = {
                "path": file["path"],
                "old_file_content": file.get("old_file_content", ""),
                "new_file_content": file["new_file_content"],
                "patch": file["diff"]
            }

            item["patches"].append(patch)

        review_datasets.append(item)

    with jsonlines.open(output_file, "w") as f:
        f.write_all(review_datasets)

    print(f"写入完成：{len(review_datasets)} 条记录，过滤 {filtered} 个提交")


if __name__ == "__main__":
    import argparse

    parser = argparse.ArgumentParser(description="Extract git commit diffs into jsonlines")
    parser.add_argument("--max-commits", type=int, default=100)
    parser.add_argument("--output", default="output/test.jsonl")
    args = parser.parse_args()

    repo_path = os.path.abspath(os.path.dirname(__file__))

    diffs = get_commit_diffs(repo_path, max_commits=args.max_commits)
    write_diff_to_file(diffs, output_file=args.output)
