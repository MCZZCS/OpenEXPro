# OpenEXPro

由OpenEX : JavaEditionPlus版本的分支升级版本,其目前有两种发行版

* GraalVM NativeImage Win32可执行程序(EXE)
* OpenJDK 17 Java应用程序(JAR)

<hr>

## Run Native Image

### 环境

将exe文件下载后由CMD命令行启动,如果提示缺少DLL文件请前往

[Visual C++ Redistributable for Visual Studio](https://www.microsoft.com/zh-CN/download/details.aspx?id=48145)

下载OpenEX `Win32`版本的运行环境

### 命令行

将`openex`本体拷贝至您想要的文件夹内,然后将该文件夹添加至系统环境变量`PATH`中

启动`cmd`或`WindowsPowerShell`,输入 `openex` 查看命令帮助

## Spigot Plugin

如果可能,我们将继承EXBuilder项目,重新将Pro版本移植到 SpigotPlugin 上

## How to build

将项目直接导入至IDEA或者使用`mvn clean install`构建项目

## 链接

* 详细的更多信息请前往:

## Update

### Pro v0.1.0

* 覆写了命令行提示信息,使格式更加人性化
* 修复了`Plus`版本局部变量的定义会影响全局变量的BUG
* 新增`GraalVM native-image`构建的`Win32`可执行程序

### Pro v0.1.1

* 新增`elif`结构语句,重写了条件判断执行节点的结构
* 修复了词法分析器在遇到`//`注释但后方没有内容时会报错的BUG
* 废弃`system`库的`compile_version`和`runtime_version`函数
* 新增`system`库的`sysinfo`函数
* 修改了版本号格式

### Pro v0.1.2

* 修复语句末尾不加`;`报错结果不详细的问题
* 修复`function`内定义局部变量无法识别的BUG
* 加入并发编译模式以提高编译速度,通过参数`-concur`开启
* 加入`-debug`参数,打印详细编译警告/提示信息等
* 加入调用栈结构, 运行时异常报错结果更加精准

### Pro v0.1.3

* 新增`for`语句(初步支持,不支持迭代器写法)
* 修复了`break`语句的BUG

### Pro v0.1.4

* 编译报错优化
* 修复了对`break`语句的上下文错误解析
* 新增`continue`语句
* 修复了`for`语句处理BUG

### Pro v0.1.5

* 新增运算符 `+=` `-=` `*=` `/=` `%` `%=`
* 优化了编译器与运行时的执行效率
* 修复了`for`语句的解析和运行BUG
