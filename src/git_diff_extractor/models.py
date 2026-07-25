from dataclasses import dataclass, field


@dataclass
class FileChange:
    path: str
    old_path: str | None
    change_type: str | None
    old_mode: int | None
    new_mode: int | None
    diff_text: str
    new_file_content: str
    old_file_content: str = ""


@dataclass
class CommitData:
    hash: str
    author: str
    date: str
    message: str
    stats_total: dict
    files: list[FileChange] = field(default_factory=list)


@dataclass
class Summary:
    total_commits: int
    total_files: int
    change_type_counts: dict[str, int]

    def to_dict(self) -> dict:
        return {
            "total_commits": self.total_commits,
            "total_files": self.total_files,
            "change_type_counts": self.change_type_counts,
        }
