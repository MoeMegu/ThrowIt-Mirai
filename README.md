## ThrowIt-Mirai 丢人插件
简单高效的 “丢人” 插件

把你的群友丢出去吧

### 使用方法:
将插件置于plugin目录下, 并安装chat-command插件

使用插件需要权限 pers.moe.throwit-mirai:command.丢

`perm permit m* pers.moe.throwit-mirai:command.丢`

聊天环境使用命令:

/丢 [@目标] 或 /throw [@目标]

使用 /clean-throw 清理数据目录

### TODO List:
#### 已完成:
* 绘制过程在内存操作
* 图像抗锯齿处理
* 增加数据目录清理模块
#### 计划中:
* RESTful API
* 支持其他参数类型

### 引用
项目由 YJBeetle/ThrowItBot 改写 使用了该项目的图片素材与绘制参数

图像处理使用 thumbnailator 与 Java2D 库


