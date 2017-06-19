# VideoWorld_Android

本项目致力于搭建一个资源类 APP 的后端平台，提供资源展示、资源搜等服务。利用爬虫技术爬取 3 个资源类网站的内容，共数万条记录，涵盖电影、电视剧、综艺节目、动漫、游戏五大类别的资源。整合两大资源搜索引擎，连接互联网千万数量级的资源库。

## 项目框架
  - 主流 Java Web 框架 SSM：[Spring][1] + [Spring MVC][2] + [Mybatis][3]
 >Spring 是一个轻量级的 Java 开发框架，它是为了解决企业应用开发的复杂性而创建的。Spring 的用途不仅限于服务器端的开发。从简单性、可测试性和松耦合的角度而言，任何Java应用都可以从 Spring 中受益。 简单来说，Spring 是一个轻量级的控制反转（IoC）和面向切面（AOP）的容器框架。<br/>
>Spring MVC 属于Spring FrameWork的后续产品，分离了控制器、模型对象、分派器以及处理程序对象的角色，这种分离让它们更容易进行定制。  <br/>
>MyBatis 是一个基于Java的持久层框架。MyBatis 提供的持久层框架包括 SQL Maps 和 Data Access Objects（DAO）它消除了几乎所有的 JDBC 代码和参数的手工设置以及结果集的检索。MyBatis 使用简单的 XML 或注解用于配置和原始映射，将接口和 Java映射成数据库中的记录。
 - 爬虫框架：[WebMagic][4]

  >WebMagic 采用完全模块化的设计，功能覆盖整个爬虫的生命周期(链接提取、页面下载、内容抽取、持久化)，支持多线程抓取，分布式抓取，并支持自动重试、自定义 UA/cookie 等功能。
##数据库截图



  [1]: http://baike.baidu.com/item/spring/85061
  [2]: http://baike.baidu.com/item/spring%20MVC
  [3]: http://baike.baidu.com/item/MyBatis
  [4]: http://webmagic.io/
