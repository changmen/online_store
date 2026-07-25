import os

import pytest
from git import Repo


def make_commit(repo: Repo, filename: str, content: str | bytes, message: str) -> None:
    filepath = os.path.join(repo.working_dir, filename)
    os.makedirs(os.path.dirname(filepath), exist_ok=True)
    mode = "wb" if isinstance(content, bytes) else "w"
    with open(filepath, mode) as f:
        f.write(content)
    repo.index.add([filename])
    repo.index.commit(message)


def delete_file(repo: Repo, filename: str, message: str) -> None:
    filepath = os.path.join(repo.working_dir, filename)
    os.remove(filepath)
    repo.index.remove([filename])
    repo.index.commit(message)


@pytest.fixture
def git_repo(tmp_path) -> Repo:
    repo = Repo.init(tmp_path)
    with repo.config_writer() as cw:
        cw.set_value("user", "name", "Test User")
        cw.set_value("user", "email", "test@example.com")

    make_commit(repo, "init.txt", "init", "init commit")
    make_commit(repo, "src/app.py", "print('hello')\n", "E.1.0 add app")
    make_commit(repo, "src/app.py", "print('world')\n", "fix: update app")
    return repo
