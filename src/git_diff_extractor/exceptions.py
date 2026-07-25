class DiffExtractorError(Exception):
    """Base exception for git-diff-extractor."""


class RepositoryError(DiffExtractorError):
    """Repository cannot be opened or is invalid."""


class CommitExtractionError(DiffExtractorError):
    """A specific commit failed to process."""

    def __init__(self, commit_hash: str, cause: Exception):
        self.commit_hash = commit_hash
        self.cause = cause
        super().__init__(f"处理提交 {commit_hash[:7]} 失败: {cause}")
