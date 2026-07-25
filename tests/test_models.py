from git_diff_extractor.models import CommitData, FileChange, Summary


def test_file_change_defaults():
    fc = FileChange(
        path="a.py",
        old_path="a.py",
        change_type="M",
        old_mode=0o100644,
        new_mode=0o100644,
        diff_text="diff",
        new_file_content="new",
    )
    assert fc.old_file_content == ""


def test_commit_data_defaults():
    cd = CommitData(hash="abc", author="A", date="2024-01-01", message="msg", stats_total={})
    assert cd.files == []


def test_summary_to_dict():
    s = Summary(total_commits=2, total_files=5, change_type_counts={"A": 3, "M": 2})
    assert s.to_dict() == {
        "total_commits": 2,
        "total_files": 5,
        "change_type_counts": {"A": 3, "M": 2},
    }
