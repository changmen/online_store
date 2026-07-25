import logging

from git import Repo
from git.exc import GitError

from .exceptions import CommitExtractionError, RepositoryError
from .models import CommitData, FileChange

logger = logging.getLogger(__name__)


def _read_blob_content(tree, path: str) -> str:
    try:
        blob = tree / path
        return blob.data_stream.read().decode("utf-8", errors="replace")
    except KeyError:
        return ""


def _extract_file_data(diff, commit, parent_commit) -> FileChange:
    change_type = diff.change_type
    if diff.new_file:
        change_type = "A"
    elif diff.deleted_file:
        change_type = "D"
    elif diff.renamed_file:
        change_type = "R"

    old_file_content = ""
    if not diff.new_file and diff.a_path:
        old_file_content = _read_blob_content(parent_commit.tree, diff.a_path)

    return FileChange(
        path=diff.b_path if diff.b_path else diff.a_path,
        old_path=diff.a_path,
        change_type=change_type,
        old_mode=diff.a_mode,
        new_mode=diff.b_mode,
        diff_text=diff.diff.decode("utf-8", errors="replace") if diff.diff else "",
        new_file_content=_read_blob_content(commit.tree, diff.b_path) if diff.b_path else "",
        old_file_content=old_file_content,
    )


def get_commit_diffs(
    repo_path: str = ".",
    max_commits: int = 10,
    author_filter: str | None = None,
    repo: Repo | None = None,
) -> list[CommitData]:
    if repo is None:
        try:
            repo = Repo(repo_path)
        except GitError as e:
            raise RepositoryError(f"无法打开仓库 {repo_path}: {e}") from e

    diffs_data: list[CommitData] = []
    author_skipped = 0

    logger.info(f"开始处理仓库 {repo_path}，最多 {max_commits} 个提交")
    commits = list(repo.iter_commits(max_count=max_commits))
    for commit in commits:
        try:
            if not commit.parents:
                continue
            parent_commit = commit.parents[0]

            author = f"{commit.author.name} <{commit.author.email}>"
            if author_filter and author_filter.lower() not in author.lower():
                author_skipped += 1
                continue

            logger.info(f"提交：{commit.hexsha[:7]} - {commit.message.strip()}")
            diffs = parent_commit.diff(commit, create_patch=True, unified=3)

            commit_data = CommitData(
                hash=commit.hexsha,
                author=author,
                date=commit.authored_datetime.isoformat(),
                message=commit.message.strip(),
                stats_total=dict(commit.stats.total),
            )

            for diff in diffs:
                if diff.diff and b"\x00" in diff.diff[:8000]:
                    continue
                commit_data.files.append(_extract_file_data(diff, commit, parent_commit))

            diffs_data.append(commit_data)

        except Exception as e:
            error = CommitExtractionError(commit.hexsha, e)
            logger.error(str(error), exc_info=e)

    if author_filter:
        logger.info(f"作者过滤 '{author_filter}' 跳过 {author_skipped} 个提交")

    return diffs_data
