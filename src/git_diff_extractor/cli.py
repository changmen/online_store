import argparse
import logging
import os
from collections.abc import Sequence
from pathlib import Path

from .exceptions import RepositoryError
from .extractor import get_commit_diffs
from .output import print_summary, write_jsonl

logging.basicConfig(level=logging.INFO, format="[%(asctime)s] %(message)s", datefmt="%H:%M:%S")
logger = logging.getLogger(__name__)

# 保持与原 main.py 相同的默认值语义：项目根目录
_PROJECT_ROOT = str(Path(__file__).resolve().parent.parent.parent)


def parse_args(argv: Sequence[str] | None = None) -> argparse.Namespace:
    parser = argparse.ArgumentParser(description="Extract git commit diffs into jsonlines")
    parser.add_argument("--repo", default=_PROJECT_ROOT, help="Git 仓库路径")
    parser.add_argument("--max-commits", type=int, default=100, help="最大处理提交数")
    parser.add_argument("--output", default="output/test.jsonl", help="输出文件路径")
    parser.add_argument("--prefix", default="E.", help="提交信息过滤前缀，多个用逗号分隔")
    parser.add_argument("--summary", default=None, help="概要统计输出的 JSON 文件路径")
    parser.add_argument("--author", default=None, help="按作者名/邮箱子串过滤提交")
    return parser.parse_args(argv)


def main(argv: Sequence[str] | None = None) -> int:
    args = parse_args(argv)
    prefixes = [p.strip() for p in args.prefix.split(",") if p.strip()]

    try:
        diffs = get_commit_diffs(args.repo, max_commits=args.max_commits, author_filter=args.author)
    except RepositoryError as e:
        logger.error(str(e))
        return 1

    write_jsonl(diffs, output_file=args.output, prefixes=prefixes)
    print_summary(diffs, summary_file=args.summary)
    return 0
