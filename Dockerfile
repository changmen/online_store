# 步骤 1: 指定基础镜像
FROM ubuntu:24.04

# 设置环境变量，避免在 apt-get 安装过程中出现交互式提示
ENV DEBIAN_FRONTEND=noninteractive

# 步骤 2: 更新包列表并安装常用工具
# 使用 RUN 指令在镜像构建过程中执行命令。
# 将 apt-get update 和 apt-get install 放在同一个 RUN 指令中，
# 这样可以利用 Docker 的层缓存机制，并确保安装的是最新的包。
# 最后清理 apt 缓存，以减小镜像体积。
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    # 网络工具
    curl \
    wget \
    net-tools \
    iputils-ping \
    dnsutils \
    # 版本控制
    git \
    # 编辑器
    vim \
    nano \
    # 系统和进程工具
    procps \
    htop \
    less \
    # 常用工具
    unzip \
    ca-certificates \
    gnupg && \
    # 清理工作
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# 步骤 3: 设置容器启动时要执行的默认命令
# 容器启动后，将睡眠一小时，以保持运行状态。
CMD ["sleep", "3600"]