import json

from git_diff_extractor.cli import main, parse_args


def test_parse_args_defaults():
    args = parse_args([])
    assert args.max_commits == 100
    assert args.output == "output/test.jsonl"
    assert args.prefix == "E."
    assert args.summary is None
    assert args.author is None


def test_parse_args_custom():
    args = parse_args(["--max-commits", "5", "--prefix", "A., B.", "--author", "dev"])
    assert args.max_commits == 5
    assert args.prefix == "A., B."
    assert args.author == "dev"


def test_main_end_to_end(git_repo, tmp_path):
    out = str(tmp_path / "result.jsonl")
    summary_out = str(tmp_path / "summary.json")
    code = main(["--repo", str(git_repo.working_dir), "--max-commits", "10",
                 "--output", out, "--prefix", "E.", "--summary", summary_out])
    assert code == 0

    with open(out) as f:
        records = [json.loads(line) for line in f]
    assert len(records) == 1
    assert records[0]["message"] == "E.1.0 add app"
    assert records[0]["category_label"] == "E.1.0"

    with open(summary_out) as f:
        summary = json.load(f)
    assert summary["total_commits"] == 2


def test_main_invalid_repo(tmp_path):
    code = main(["--repo", str(tmp_path / "no_repo"), "--output", str(tmp_path / "x.jsonl")])
    assert code == 1


def test_main_multi_prefix(git_repo, tmp_path):
    out = str(tmp_path / "result.jsonl")
    code = main(["--repo", str(git_repo.working_dir), "--max-commits", "10",
                 "--output", out, "--prefix", "E.,fix:"])
    assert code == 0
    with open(out) as f:
        records = [json.loads(line) for line in f]
    assert len(records) == 2
