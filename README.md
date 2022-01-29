## ThrowIt-Mirai 丢人插件
简单高效的 “丢人” 插件

把你的群友丢出去吧

### 使用方法:
将插件置于plugin目录下, 并安装chat-command插件

使用插件需要权限 pers.moe.throwit-mirai:command.丢

`perm permit m* pers.moe.throwit-mirai:command.丢`

聊天环境使用命令:

/丢 [Target] 或 /throw [Target]

@Target可以是"at某人",或者一个群友的qq号

### TODO List:
#### 已完成:
* 图像处理均在内存操作
* 图像抗锯齿处理
* 使用Kotlin重构
* ~~增加数据目录清理模块~~
* 增加图片上传的检查及重试逻辑
#### 工作中:
* 增加纯文本@解析
* 支持任意QQ账号丢出
* 支持自定义头像源图质量
#### 计划中:
* RESTful API

### 引用
项目由 YJBeetle/ThrowItBot 改写 使用了该项目的图片素材与绘制参数

图像处理使用 thumbnailator 与 Java2D 库

感谢 Samarium150/mirai-console-throw-it 提供的Kotlin重构参考


