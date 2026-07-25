import os

from git_diff_extractor.exceptions import RepositoryError
from git_diff_extractor.extractor import get_commit_diffs

from conftest import delete_file, make_commit


def test_extracts_commit_fields(git_repo):
    results = get_commit_diffs(repo=git_repo, max_commits=10)
    assert len(results) == 2  # initial commit skipped (no parent)

    first = results[-1]  # oldest processed commit
    assert first.message == "E.1.0 add app"
    assert first.author == "Test User <test@example.com>"
    assert len(first.hash) == 40
    assert "T" in first.date

    fc = first.files[0]
    assert fc.path == "src/app.py"
    assert fc.change_type == "A"
    assert fc.new_file_content == "print('hello')\n"
    assert fc.old_file_content == ""


def test_modified_file_has_old_content(git_repo):
    results = get_commit_diffs(repo=git_repo, max_commits=10)
    latest = results[0]
    assert latest.message == "fix: update app"
    fc = latest.files[0]
    assert fc.old_file_content == "print('hello')\n"
    assert fc.new_file_content == "print('world')\n"
    assert "-print('hello')" in fc.diff_text


def test_deleted_file(git_repo):
    delete_file(git_repo, "init.txt", "remove init")
    results = get_commit_diffs(repo=git_repo, max_commits=10)
    latest = results[0]
    fc = latest.files[0]
    assert fc.change_type == "D"
    assert fc.old_file_content == "init"


def test_binary_file_skipped(git_repo, monkeypatch):
    make_commit(git_repo, "data.bin", b"\x00\x01\x02binary", "add binary")

    from git.objects.commit import Commit

    original_diff = Commit.diff

    def fake_diff(self, other, **kwargs):
        result = original_diff(self, other, **kwargs)
        for d in result:
            d.diff = b"\x00\x01\x02" + (d.diff or b"")
        return result

    monkeypatch.setattr(Commit, "diff", fake_diff)
    results = get_commit_diffs(repo=git_repo, max_commits=10)
    for commit_data in results:
        assert commit_data.files == []


def test_author_filter(git_repo):
    results = get_commit_diffs(repo=git_repo, max_commits=10, author_filter="test user")
    assert len(results) == 2

    results = get_commit_diffs(repo=git_repo, max_commits=10, author_filter="nobody")
    assert results == []


def test_max_commits(git_repo):
    results = get_commit_diffs(repo=git_repo, max_commits=1)
    assert len(results) == 1
    assert results[0].message == "fix: update app"


def test_invalid_repo_raises_repository_error(tmp_path):
    import pytest

    with pytest.raises(RepositoryError):
        get_commit_diffs(repo_path=str(tmp_path / "nonexistent"))
