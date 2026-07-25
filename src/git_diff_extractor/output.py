import json
import logging
import os
from collections.abc import Sequence

import jsonlines

from .models import CommitData, Summary

logger = logging.getLogger(__name__)


def filter_commits(
    commits: list[CommitData],
    prefixes: Sequence[str],
) -> tuple[list[CommitData], int]:
    prefix_tuple = tuple(prefixes)
    matched = []
    filtered = 0
    for commit in commits:
        if not commit.files:
            continue
        if not commit.message.startswith(prefix_tuple):
            filtered += 1
            continue
        matched.append(commit)
    return matched, filtered


def commit_to_output_record(commit: CommitData) -> dict:
    return {
        "message": commit.message,
        "category_label": commit.message.split(" ", 1)[0],
        "patches": [
            {
                "path": fc.path,
                "old_file_content": fc.old_file_content,
                "new_file_content": fc.new_file_content,
                "patch": fc.diff_text,
            }
            for fc in commit.files
        ],
    }


def _ensure_parent_dir(path: str) -> None:
    dirname = os.path.dirname(path)
    if dirname:
        os.makedirs(dirname, exist_ok=True)


def write_jsonl(commits: list[CommitData], output_file: str, prefixes: Sequence[str]) -> int:
    matched, filtered = filter_commits(commits, prefixes)
    records = [commit_to_output_record(c) for c in matched]

    _ensure_parent_dir(output_file)
    with jsonlines.open(output_file, "w") as f:
        f.write_all(records)

    logger.info(f"写入完成：{len(records)} 条记录，过滤 {filtered} 个提交")
    return len(records)


def compute_summary(commits: list[CommitData]) -> Summary:
    total_files = 0
    change_type_counts: dict[str, int] = {}

    for commit in commits:
        total_files += len(commit.files)
        for fc in commit.files:
            key = fc.change_type if fc.change_type is not None else "?"
            change_type_counts[key] = change_type_counts.get(key, 0) + 1

    return Summary(
        total_commits=len(commits),
        total_files=total_files,
        change_type_counts=change_type_counts,
    )


def print_summary(commits: list[CommitData], summary_file: str | None = None) -> Summary:
    summary = compute_summary(commits)

    logger.info("=" * 40)
    logger.info(f"概要：共 {summary.total_commits} 个提交，{summary.total_files} 个文件变更")
    for change_type, count in sorted(summary.change_type_counts.items()):
        logger.info(f"  变更类型 {change_type}: {count} 个")
    logger.info("=" * 40)

    if summary_file:
        _ensure_parent_dir(summary_file)
        with open(summary_file, "w", encoding="utf-8") as f:
            json.dump(summary.to_dict(), f, ensure_ascii=False, indent=2)
        logger.info(f"概要已写入：{summary_file}")

    return summary
