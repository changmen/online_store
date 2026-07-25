# Git Diff Extractor

从 Git 仓库提交历史中提取 diff 信息，生成 JSONL 格式的代码审查训练数据集。

## 技术栈

- **Python 3.12+**
- **GitPython** — Git 仓库操作
- **jsonlines** — JSONL 文件输出
- **uv** — 依赖管理

## 快速开始

### 安装依赖

```bash
uv sync
```

### 运行

```bash
uv run git-diff-extractor --max-commits 100 --output output/test.jsonl
# 或
uv run python -m git_diff_extractor --max-commits 100 --output output/test.jsonl
```

### 参数说明

| 参数 | 默认值 | 说明 |
|------|--------|------|
| `--repo` | 项目根目录 | Git 仓库路径 |
| `--max-commits` | 100 | 最大处理的提交数 |
| `--output` | `output/test.jsonl` | 输出文件路径 |
| `--prefix` | `E.` | 提交信息过滤前缀，多个用逗号分隔 |
| `--summary` | 无 | 概要统计输出的 JSON 文件路径 |
| `--author` | 无 | 按作者名/邮箱子串过滤提交 |

## 输出格式

每行一条 JSON 记录，包含：

- `message` — 提交信息
- `category_label` — 分类标签（提交信息的前缀）
- `patches` — 文件变更列表，每项包含路径、旧文件内容、新文件内容和 diff

## 过滤规则

仅保留提交信息以指定前缀开头的记录（默认 `E.`，可通过 `--prefix` 修改）。

## 项目结构

```
src/git_diff_extractor/
├── cli.py          # CLI 参数解析与编排
├── extractor.py    # Git 仓库交互与 diff 提取
├── models.py       # 数据模型（FileChange, CommitData, Summary）
├── output.py       # 过滤、JSONL 写入、统计
└── exceptions.py   # 异常层次
```

## 测试

```bash
uv run pytest
```

## License

MIT
