# BlockLauncher

一个使用 JavaFX 编写的 Minecraft Java 版启动器，目标是把游戏版本下载、版本管理、模组加载器安装和游戏启动整合到一个桌面应用中。

> 项目目前处于开发阶段，功能和界面仍在持续完善。使用前请备份已有的 `.minecraft` 目录和存档。

## 功能概览

- 从 Minecraft 版本清单读取版本信息
- 下载并校验版本 JSON 与客户端 `client.jar`
- 按版本管理游戏文件
- 启动 Minecraft Java 版客户端
- 检测和管理部分模组加载器
  - Fabric
  - Forge
  - Quilt
  - OptiFine
- JavaFX 桌面用户界面
- 基于 SHA-1 校验下载文件完整性，并使用临时文件避免下载中断时破坏已有文件
- 内置日志记录，便于定位下载和启动问题

## 项目结构

```text
BlockLauncher/
├── src/main/java/
│   ├── BlockLauncher.java       # 程序入口
│   └── org/bkl/
│       ├── download/             # 版本清单和游戏文件下载
│       ├── game/                 # Minecraft 路径、版本解析和启动
│       ├── modloader/             # Fabric、Forge、Quilt、OptiFine 支持
│       ├── ui/                   # JavaFX 界面
│       ├── log/                  # 日志组件
│       └── util/                 # 通用工具
├── src/main/resources/
│   ├── fonts/                    # 界面字体
│   ├── image/                    # 图标、背景和头像资源
│   └── lib/                      # 项目使用的本地依赖
├── src/test/java/                # 下载、版本和模组加载器测试
├── pom.xml
└── .gitignore
```

## 技术栈

- Java 21
- JavaFX 20
- Maven
- Gson
- JUnit 5
- JMCCC（用于 Minecraft 相关启动和版本处理）

## 环境要求

- JDK 21 或更高版本
- Maven 3.8+
- 可访问 Minecraft 官方版本清单及资源下载地址的网络环境
- Windows、Linux 或 macOS 桌面环境（具体兼容性取决于 JavaFX 和 Minecraft 运行环境）

启动器会使用当前用户的 Minecraft 目录。首次运行前请确认当前用户对该目录具有读写权限。

## 构建项目

在项目根目录执行：

```bash
mvn clean package
```

运行测试：

```bash
mvn test
```

项目入口类为：

```text
BlockLauncher
```

可以在 IntelliJ IDEA 等 IDE 中导入 Maven 项目，并直接运行该类进行开发调试。由于项目使用 JavaFX，命令行运行时需要确保 JavaFX 依赖能够被正确加载。

## 使用流程

1. 启动应用并进入主界面。
2. 选择或下载 Minecraft 游戏版本。
3. 等待版本 JSON、客户端和必要资源完成下载及校验。
4. 根据需要安装或选择 Fabric、Forge、Quilt 或 OptiFine。
5. 配置游戏目录、Java 运行时和启动参数。
6. 启动游戏。

下载失败时，优先检查网络、版本清单地址、磁盘空间和目标目录权限。日志文件通常位于项目运行目录的 `logs/` 下。

## 文件校验与安全说明

项目会根据版本清单中的 SHA-1 对客户端文件进行校验。下载过程中使用 `.part` 临时文件，校验成功后再替换目标文件；校验失败时会删除临时文件并记录错误。

请注意：

- 只从可信来源下载 Minecraft、模组加载器和第三方模组。
- 不要在日志、截图或 Issue 中发布 Microsoft/Minecraft 登录凭据、访问令牌或个人路径。
- 不要把个人存档、账号信息或本地游戏目录打包提交到仓库。
- 安装第三方模组前请检查其来源、许可证和版本兼容性。

## 开发建议

- 下载相关逻辑位于 `org.bkl.download`，修改时请保留 SHA-1 校验和临时文件替换流程。
- Minecraft 路径和版本处理逻辑位于 `org.bkl.game`。
- 新增模组加载器时，可参考 `org.bkl.modloader` 中现有 Checker/Install 实现。
- UI 代码位于 `org.bkl.ui`，耗时下载和安装任务不应阻塞 JavaFX UI 线程。
- 提交代码前运行 `mvn test`，并清理本地生成的 `target/` 和日志文件。

## 已知限制

- 项目仍在开发中，部分版本和模组加载器组合可能尚未完整验证。
- 不同操作系统的 Java 路径、Minecraft 目录和启动参数存在差异。
- 当前项目没有发布正式安装包，建议使用源码和 IDE 进行开发测试。

## 许可证

当前仓库未提供独立的许可证文件。除项目自身代码外，项目使用的 JavaFX、Gson、JMCCC、Minecraft 及第三方模组均受各自许可证和使用条款约束。公开发布或再分发前，请补充项目许可证并确认所有依赖的授权范围。
