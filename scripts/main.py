import os
from git import Repo, Diff
from typing import List, Dict
import re
import json
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

    commits = list(repo.iter_commits(max_count=max_commits))
    # commits.reverse()
    for commit in commits:
        try:
            # 获取当前提交的父提交（处理初始提交）
            if not commit.parents:
                continue
            parent_commit = commit.parents[0]

            print(f"提交：{commit.hexsha[:7]} - {commit.message.strip()}, parent:{parent_commit}")
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
                file_blob = commit.tree / diff.b_path
                file_data["new_file_content"] = file_blob.data_stream.read().decode('utf-8', errors='replace')
                if not diff.new_file:
                    old_file_blob = parent_commit.tree / diff.a_path
                    file_data["old_file_content"] = old_file_blob.data_stream.read().decode('utf-8', errors='replace')
                commit_data["files"].append(file_data)
            # binary_file

            diffs_data.append(commit_data)

        except Exception as e:
            print(f"Error processing commit {commit.hexsha[:7]}: {str(e)}")
            continue

    return diffs_data


def parse_diff_patches(diff_content: str) -> List[Dict]:
    """
    解析原始差异内容为结构化数据
    :param diff_content: 原始差异字符串
    :return: 结构化差异块列表
    """
    print(f"parse_diff_patches:{diff_content}")
    patches = []
    current_hunk = {}
    hunk_pattern = re.compile(r"^@@ -(\d+),?(\d*) \+(\d+),?(\d*) @@(.*)", re.MULTILINE)

    for line in diff_content.splitlines():
        # 匹配差异块头部
        hunk_match = hunk_pattern.match(line)
        if hunk_match:
            if current_hunk:
                patches.append(current_hunk)
            current_hunk = {
                "old_start": int(hunk_match.group(1)),
                "old_lines": int(hunk_match.group(2)) if hunk_match.group(2) else 1,
                "new_start": int(hunk_match.group(3)),
                "new_lines": int(hunk_match.group(4)) if hunk_match.group(4) else 1,
                "context": hunk_match.group(5).strip(),
                "changes": []
            }
        elif current_hunk:
            # 解析差异行
            line_type = "context"
            content = line

            if line.startswith("+"):
                line_type = "addition"
            elif line.startswith("-"):
                line_type = "deletion"
            elif line.startswith("\\"):
                line_type = "meta"  # 处理 \ No newline at end of file

            current_hunk["changes"].append({
                "type": line_type,
                "content": content[1:] if line_type != "meta" else content,
                "original": line
            })

    if current_hunk:
        patches.append(current_hunk)

    return patches


def write_diff_to_file(diff_data: List[Dict], output_file="output/test.jsonl"):
    review_datasets = []
    for commit in diff_data:
        if not commit["files"]:
            continue
        message = commit["message"]
        if not message.startswith("E."):
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


# 使用示例
if __name__ == "__main__":
    repo_path = local_path = os.path.abspath(os.path.dirname(os.path.dirname(__file__)))

    diffs = get_commit_diffs(repo_path, max_commits=100)
    write_diff_to_file(diffs)
