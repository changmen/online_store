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
uv run python main.py --max-commits 100 --output output/test.jsonl
```

### 参数说明

| 参数 | 默认值 | 说明 |
|------|--------|------|
| `--max-commits` | 100 | 最大处理的提交数 |
| `--output` | `output/test.jsonl` | 输出文件路径 |

## 输出格式

每行一条 JSON 记录，包含：

- `message` — 提交信息
- `category_label` — 分类标签（提交信息的前缀）
- `patches` — 文件变更列表，每项包含路径、旧文件内容、新文件内容和 diff

## 过滤规则

仅保留提交信息以 `E.` 开头的提交记录。

## License

MIT
