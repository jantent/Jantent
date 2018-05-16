# Jantent
用spring boot开发的web博客系统
# 概述

- 首先要感谢两位大神，该项目的想法来源自[tale](https://github.com/otale/tale)和[MyBlog](https://github.com/JayTange/My-Blog)，本项目的想法。

- 做了一些改造，增加了一些功能和一些代码的重构，并且更换了博客主题。

- 关于项目，对于开发的练手项目，能够工程化，严谨一些。

- 关于文档，本文主要中从项目需求，项目设计的方式来阐述.

- **如何从零开始**，使用springboot开发项目。

- 记录一些在开发过程中遇到的一些问题，总结开发技巧

**接下来，会以需求和设计方式来阐述**

# 效果图
- 首页展示
![alt](http://www.janti.cn/upload/2018/05/q2v7ug8ml6isgp93lhij8pvqlt.png)

- 文章编辑
![alt](http://www.janti.cn/upload/2018/05/pj08ddivuehtto8vnna7ot1rf3.png)

- 文章管理
![alt](http://www.janti.cn/upload/2018/05/2roqtej4umhclo0rta76cs8eed.png)

# 项目需求
## 项目背景
对于刚学习springboot的同学，最好的就是拿一个项目练练手。在编码过程中遇到的问题并解决，这都是宝贵的经验。
用springboot开发的博客系统，简单而且实用，适合做练手项目。

## 功能需求
### 界面需求
#### 主页
- 博客汇总，以列表加图片的形式展示
- 能够以分类的方式查看文章
- 能够以时间列表的方式归档文章
- 个人介绍，github地址
- 搜索框，能够搜索文章

#### 后台管理
- 管理主页，记录最新文章，最新留言，最近日志等
  - 最近日志记录登录IP，地址，操作等
  - 记录一天的访问量
- 发布文章
  - 使用markdown编辑器，支持插入代码，插入图片等功能
  - 能够给文章添加缩略图。
  - 可将文章存为草稿或者发布
  - 文章可选择分类和标签，自定义url
  - 文章可控制是否允许评论
- 文章管理
  - 以列表形式展示文章信息
  - 在可选操作中增加删除，预览，编辑功能
  - 支持分页显示
  - 增加搜索功能，可根据文章名文章信息
- 分类管理 
  - 可以新增、删除、修改分类

- 文件管理
  - 支持文件上传
  - 支持删除已上传的文件

- 友情联机
  - 支持增加友情链接
  - 支持删除友情链接

- 系统设置
  - 支持修改密码
  - 支持备份数据库
  - 支持黑名单配置

### 非界面需求
- 日志记录，记录来访IP名单
- 每天定时备份数据库

### 安装部署需求
- 可以使用docker方式部署，也可支持-jar方式
- 使用springboot自带方式打包

## 非功能性需求

### 性能需求
- 首页响应的时间不超过1秒钟
- 文章页响应时间不超过1秒钟

# 项目设计
## 总体设计
- 本项目用到的技术和框架
  - 项目构建： maven
  - web框架：spring boot
  - 数据库ORM：mybatis
  - 数据库连接池：Druid
  - 分页插件：PageHelper
  - 数据库：mysql
  - 缓存NOSQL：redis
  - 前段模板：thymeleaf 
  - 文章展示：使用commonmark，将markdown转成html页面

- 本项目的关键点
  - 采用springboot开发，数据库使用连接池加orm框架的模式，对于系统的关键业务使用redis缓存，加快响应速度
  - 整体系统采用门户网站+后台管理的方式搭建，门户主要展示博客内容，后台管理主要用于编辑文章，上传附件，控制黑名单登录等。

- 环境

工具 | 名称 |
------- | -------|
开发工具 | IDEA
语言 | JDK1.8, JS, HTML
数据库 | mysql5.6
缓存NOSQL | redis
项目构建|Maven
运行环境|阿里云Centos7

 
## 结构设计
![alt](http://www.janti.cn/upload/2018/05/ungedgq714gpoqi707016alr7g.png)
熟悉spring开发的同学，相信对此结构图也并不陌生。平时的开发过程中，结构设计是重要的缓解，特别是协作开发的时候，明细的分包，模块化，可减少在git提交时的冲突。

## 业务设计
本模块主要介绍一些关键的业务流程。
- 发布文章流程：
![alt](http://www.janti.cn/upload/2018/05/36qmnl6sa6h3irsjo03o7arf1e.png)


- **修改文章的流程大致上和发布是相似的，这里不再赘述了**

- 登录流程
![alt](http://www.janti.cn/upload/2018/05/ptrqrhl9kuhj9p29kejv5i8tj8.png)

- 文件上传
  - 在写文章的时候，通常会使用到图片，可以引用一些网络上的图片，更好的是本系统支持上传文件和图片
  - 将文件区别为图片和其他，图片支持预览模式
  - 文件路径设计成绝对路径，在web系统中可直接引用
  - 文件按月份归类，文件名以uuid的重新命名存储
  - 其他文件支持下载
  - **文件上传流程图**

![alt](http://www.janti.cn/upload/2018/05/6biht8rofqivnp3jm1dv5h1he9.png)


- 首页展示
  - 首页也文章列表+图片的形式展示内容，默认最大显示12篇文章，包括发布时间和分类
  - 上部展示菜单栏，支持搜索，归档页等功能
  - 右侧显示菜单栏，展示个人github地址，个人信息，标签云等
  - 使用redis缓存首页的html页面，加速访问。

## 打包、部署和运行
 - 本项目采用springboot的maven插件进行打包，打成jar形式
 - 部署方式：使用**nohub java -jar xxx.jar &**的方式，启动项目


## 数据设计

用户表：t_users

 名称 | 类型 | 长度 | 主键 | 非空 | 描述 
------- | ------- | ------- | ------- | ------- | -------  
uid| int | 10|  true | true  |主键，自增
username | varchar | 32  |false | false | 用户名
password | varchar | 64  |false | false | 密码
email| varchar | 200  |false | false | 邮件地址
creted | int | 10  |false | false | 创建时间

用户表主要管理后台管理用户。

文章表：t_contents

 名称 | 类型 | 长度 | 主键 | 非空 | 描述 
------- | ------- | ------- | ------- | ------- | -------  
cid| int | 10|  true | true  |主键,自增
title| varchar | 200  |false | false | 文章标题
slug | varchar | 200  |false | false | url地址
creted | int | 10  |false | false | 创建时间
modified| int | 10  |false | false | 修改时间
content | text  |无限制| false | false| 文章内容
author_id| int | 10  |false | false | 作者ID
type| varchar| 16  |false | false | 文章类型
status | varchar | 16  |false | false | 文章状态
categories| varchar| 200|false | false | 分类
thumbImg| varchar| 512|false | false | 缩略图地址
hits| int | 10 |false | false | 文章点击量
comments_num|int|10 |false | false |评论数量
allow_comment|int| 1 |false | false |允许评论

主要管理文章内容，外键为cid

标签表：t_metas

 名称 | 类型 | 长度 | 主键 | 非空 | 描述 
------- | ------- | ------- | ------- | ------- | -------  
mid| int | 10|  true | true  |主键，自增
name | varchar | 200 |false | false | 名称
slug| varchar | 200 |false | false | 说明
type| varchar | 200  |false | false | 类型
description|varchar| 200|false|false|描述
sort|int| 10|false|false|排序
parent|int| 10|false|false|父标签

管理标签信息，外键为mid

文章标签关系表：t_relationships

 名称 | 类型 | 长度 | 主键 | 非空 | 描述 
------- | ------- | ------- | ------- | ------- | -------  
cid| int | 10|  false| false |组合主键，用户ID
uid | int| 10  |false| false |组合主键，标签ID

记录文章和分类的关系，多对多表

## 性能与可靠性
### 性能设计
- 将文章内容写入redis中，加快访问速度

### 可靠性设计
- 后台管理，可以系统日志，查看系统运行状态
- 定时发送邮件，发送服务端的可用内存，cpu，最新日志，硬盘情况进行监控
- 对于恶意的IP，支持黑名单设置，禁止访问

# 开发流程
## 数据库的curd
- 首先，编写sql语句，创建数据库。

- 本项目的crud操作采用mybatis的逆向功能，对于特殊操作，需要自己手写sql语句

- springboot如何使用mybatis，以及mybatis的逆向工程，请参考[springboot与mybatis](http://www.janti.cn/article/springboot-mybaits)

- 编写service层，根据需求分析和概要设计，将具体业务转成具体代码

- 关于事务的使用，使用srping中的@Transactional，还是很方便的

- 本流程的开发不是特别难，关键在于业务的实现

## 页面与展示
- 作为一个后端开发，css的功力还是有所欠缺的，所以也是用了[妹子UI主题](http://amazeui.org/)，和tale的后端页面，大大减少了页面的开发难度，特此感谢

- 页面与后端的交换主要是在controller包中，springboot的页面开发和springmvc是几乎一样的，@PostMapping和@GetMapping这两个注解也是方便了开发。

- **统一的异常处理**，使用@ControllerAdvice，定义异常页面，设置自动跳转500，404页面。

- **拦截器**，获取http请求中的ip，判断是否在黑名单（如果在，则禁止访问系统）

## 其他功能
- 该项目是在My blog基础上修改的，修复了部分bug，增加添加黑白单功能，指定文章缩略图

- 分析访问量最多的数据，主要在于文章访问部分，将文章放入redis缓存。每次编辑完文章后，更新缓存

- 每天定时发送邮件，汇报服务器运行状态和最新日志，手机即可查看。

## 系统安全
- 使用阿里云云主机，借助阿里云本身防护机制，

- 在主机中安装denyhosts，对于尝试暴力破解ssh的IP，实施封禁

- 对于评论部分，能够抵御sql注入和xss攻击

## 打包测试
- 使用springboot本身测试方式，在集成测试之前，先进行单元测试

- 打包，使用springboot的mvn插件，打成jar包

## 开发总结

- [springboot常用注解](http://www.janti.cn/article/springbootzhujie) 
- [srpingboot整合mybatis](http://www.janti.cn/article/springboot-mybaits)
- [springboot之邮件的发送](http://www.janti.cn/article/springbootmail)
- [springboot之thymeleaf的使用](http://www.janti.cn/article/springbootthymeleaf)
- [springboot之定时任务](http://www.janti.cn/article/springbootscheduletask)
- [springboot之netty的使用](http://www.janti.cn/article/springbootpackage)
- [springboot之redis的整合与使用](http://www.janti.cn/article/springbootredis)

- **以上是我学习springboot总结的一些博客，特此分享**

# 网站地址

  **[www.janti.cn](http://www.janti.cn)**

  **[项目代码](https://github.com/JayTange/Jantent)**