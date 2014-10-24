记录

该项目是从 Dev Vaadion 网上下载的一篇 AddressBook 改进版基础上修改完成的案例。
下载地址
https://vaadin.com/wiki/-/wiki/Main/Adding%20JPA%20to%20the%20Address%20Book%20Demo

说明：
1、该文章主要是在 Vaadion6 官方提供的地址簿 案例基础上，加上 JPA 访问数据库功能
2、提高了 Container 容器的实现，封装数据库查询的数据，以便于页面Table Form 组件使用
3、能够使用完整的 Theme 样式，这也是我唯一一份能够看到效果的页面样式。
4、首先需要使用 MySql 数据库，创建 ddl 目录里面的数据库   addressbook
5、修改数据库链接的配置信息  Java Resources/conf/ioc/dao.js
6、其他的不用修改，直接发布运行，可以看到结果。




修改：
1、添加了 Nutz 框架到项目中。并且在 web.xml 中配置了加载点。
2、发现问题，Nutz 加载点和 Vaadin 启动时都会过滤所有请求，只有完全被 Nutz IOC 控制的类才会被自动初始化。
3、解决方案，在 Application 启动类 init 方法中，调用代码加载 Nutz ioc 容器。

