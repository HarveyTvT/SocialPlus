#社交网络分析平台

##功能

1. 支持各大常见社交平台接入，之后可以进行相应的统计分析。
2. 统计分析包括（以微博为例），包括以下方面
	1. 微博转发数、评论数、点赞数
	2. 微博转发人群性别比例
	3. 微博以时间线为横坐标，转发数为纵坐标折线图
	4. 微博地理位置（省份）为权的地理位置统计
	5. 微博转发历史无向图（以转发者为节点）
	6. 微博关键字分析
	7. 微博热点（可选）
3. 接入方式：
	1. 统一的对外API
	2. 社交平台本身提供相应API的，直接调用
	3. 不支持API的，通过爬虫，预先导入数据库等方式手动获取
4. 暂支持社交平台
	1. 微博
5. 实现方式
	1. Play Framework
	2. 数据库MongoDB
	4. 关键词分析：LDA，Lucence


##项目结构
1. Web框架：[Play Framework](https://www.playframework.com) 2.4.x
2. 数据库：[MongoDB](http://www.mongodb.org) 3.0 +
3. 项目结构：

	1. `app` -- 所有代码文件
		1. `constroller` -- 控制器处理请求响应
		2. `edu` & `org` -- NLP分词语言处理
		3. `models` -- 请求处理，分析逻辑，数据库ORM
		4. `utils` -- 辅助类
		5. `views` -- 前端渲染模版，以`*.scala.html`为名
	2. `conf` -- 配置文件
	3. `database` -- MongoDB数据库转储路径
	4. `doc` -- 文档，展示Slides，实例视频
	5. `lib` -- 本地依赖
	6. `logs` -- 日志路径
	7. `test` -- 测试代码
	8. `resource` -- NLP分词词库
	9. 其他：`activator` & `activator.bat` -- 启动构建脚本
	`activator-launch-1.3.5.jar` -- 实际构建启动jar
	`build.sbt` -- SBT构建配置文件

##开发

1.导入数据库
首先下载MongoDB，版本为3.0+即可，对应操作系统下载对应的Server端，以下命令行以OS X为例，对应操作系统请选择对应的软件和命令。

```bash
brew install mongodb
```

使用MongoDB自带mongorestore，导入bson文件到MongoDB数据库

```bash
mongorestore --host YOUR_HOST_IP --port 27017 --username YOUR_USER_NAME --password YOUR_PASSWORD /path/to/bson/file.bson
```
包括数据库文件，路径在`database/`下

```
outcome.bson		--	对应分析结果Collection
socialmessage.bson		--	对应社交平台（如微博）的微博Collection
socialuser.bson		--	对应社交平台（如微博）的用户Collection
user.bson		--	对应本平台用户Collection
```

2.配置文件

路径：conf/application.conf

```
# MongoDB URI -- 改为自己的MongoDB URI
app.mongodb.uri = "mongodb://localhost:27017/socialplus"
# App keys
# Weibo -- 分别对应微博的AppKey，Secret和回调URL
app.weibo.appkey = "xxxxx"
app.weibo.secret = "xxxxx"
app.weibo.redirectURL = "http://YOUR_IP:9000/api/weibo/auth"
```

3.运行构建

注：Windows下构建请使用`activator.bat`批处理代替`activator`脚本

首先确保可执行权限

```bash
chmod +x ./activator
```

再执行

```bash
activator run
```
项目将自动下载依赖到~/.ivy2，包括SBT依赖和Maven依赖，若下载问题请使用代理。然后在9000端口启动，可通过`http://localhost:9000/`访问

注：为了使用微博的Oauth认证，请使用`http://本机IP:9000/`来访问

##部署

```bash
sudo activator "start -Dhttp.port=80"
```
项目将在80端口启动，通过`http://本机IP/`或者绑定域名访问