1、数据库导入sql，修改application.properties文件里的数据库配置，连接到本机数据库
2、启动项目
3、http://localhost:8080/url/add/{source}爬取所有url，source可以填写baidu、sina、sogou、toutiao
4、http://localhost:8080/news/add/{source}根据刚刚爬取的url，进一步获得所有详细信息，source可以填写baidu、sina、sogou、toutiao
5、搜索关键字、日期条件等，详见lab409/controller/UrlController.java，直接修改即可
6、爬虫完成时，控制台输出-------------------spider is over!-------------------。



示例：
如果我想爬取2017-11-1到2017-11-3之间所有关于"同济大学软件学院"与"华东师范大学软件学院"的百度新闻，并根据标题搜索，首先需要在
lab409/controller/UrlController.java里配置好响应参数。
接着运行http://localhost:8080/url/add/baidu,爬取url。
url爬取完成后，运行http://localhost:8080/news/add/baidu，爬取详细信息。
详细信息爬取完成后，工作结束。
数据存于数据表news_baidu中。