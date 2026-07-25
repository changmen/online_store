import json

from git_diff_extractor.models import CommitData, FileChange
from git_diff_extractor.output import (
    commit_to_output_record,
    compute_summary,
    filter_commits,
    print_summary,
    write_jsonl,
)


def _make_commit(message: str, num_files: int = 1) -> CommitData:
    files = [
        FileChange(
            path=f"f{i}.py",
            old_path=f"f{i}.py",
            change_type="A" if i % 2 == 0 else "M",
            old_mode=None,
            new_mode=0o100644,
            diff_text=f"diff {i}",
            new_file_content=f"content {i}",
        )
        for i in range(num_files)
    ]
    return CommitData(hash="a" * 40, author="A <a@b.c>", date="2024-01-01", message=message, stats_total={}, files=files)


def test_filter_commits_by_prefix():
    commits = [_make_commit("E.1.0 fix"), _make_commit("feat: other"), _make_commit("E.2.0 add")]
    matched, filtered = filter_commits(commits, ["E."])
    assert len(matched) == 2
    assert filtered == 1


def test_filter_commits_multiple_prefixes():
    commits = [_make_commit("E.1 fix"), _make_commit("W.2 warn"), _make_commit("other")]
    matched, filtered = filter_commits(commits, ["E.", "W."])
    assert len(matched) == 2
    assert filtered == 1


def test_filter_skips_empty_files():
    empty = _make_commit("E.1 empty")
    empty.files = []
    matched, filtered = filter_commits([empty], ["E."])
    assert matched == []
    assert filtered == 0


def test_output_record_schema():
    commit = _make_commit("E.1.0.7->E.1.0.7.1 Feature Envy", num_files=2)
    record = commit_to_output_record(commit)
    assert record["message"] == "E.1.0.7->E.1.0.7.1 Feature Envy"
    assert record["category_label"] == "E.1.0.7->E.1.0.7.1"
    assert len(record["patches"]) == 2
    patch = record["patches"][0]
    assert set(patch.keys()) == {"path", "old_file_content", "new_file_content", "patch"}


def test_write_jsonl_creates_dirs(tmp_path):
    out = str(tmp_path / "nested" / "dir" / "out.jsonl")
    commits = [_make_commit("E.1 fix"), _make_commit("skip me")]
    count = write_jsonl(commits, out, ["E."])
    assert count == 1
    with open(out) as f:
        lines = f.readlines()
    assert len(lines) == 1
    assert json.loads(lines[0])["message"] == "E.1 fix"


def test_write_jsonl_bare_filename(tmp_path, monkeypatch):
    monkeypatch.chdir(tmp_path)
    count = write_jsonl([_make_commit("E.1 ok")], "bare.jsonl", ["E."])
    assert count == 1
    assert (tmp_path / "bare.jsonl").exists()


def test_compute_summary():
    commits = [_make_commit("E.1 a", num_files=2), _make_commit("E.2 b", num_files=1)]
    summary = compute_summary(commits)
    assert summary.total_commits == 2
    assert summary.total_files == 3
    assert summary.change_type_counts == {"A": 2, "M": 1}


def test_compute_summary_none_change_type():
    commit = _make_commit("E.1 x")
    commit.files[0].change_type = None
    summary = compute_summary([commit])
    assert summary.change_type_counts == {"?": 1}


def test_print_summary_writes_file(tmp_path):
    out = str(tmp_path / "summary.json")
    summary = print_summary([_make_commit("E.1 a")], summary_file=out)
    assert summary.total_commits == 1
    with open(out) as f:
        data = json.load(f)
    assert data == summary.to_dict()
