package lab409.controller;

import lab409.service.SinaNewsService;
import lab409.service.SogouNewsService;
import lab409.service.TouTiaoNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import lab409.service.BaiduNewsService;

/**
 * Created by xuawai on 03/11/2017.
 */

@RestController
@RequestMapping({"/url"})
public class UrlController {

    @Autowired
    BaiduNewsService baiduNewsService;

    @Autowired
    SinaNewsService sinaNewsService;

    @Autowired
    TouTiaoNewsService touTiaoNewsService;

    @Autowired
    SogouNewsService sogouNewsService;

    /**
     * 收集 ulrs
     * @return
     */
    @RequestMapping(value = "add/{source}")
    @ResponseBody
    public String addUrls(@PathVariable("source") String source){
        Long start = System.currentTimeMillis();
        switch(source){
            case "baidu":
                //起始日期、终止日期、搜索关键字、是否根据标题搜索（true为根据标题，false为根据内容）
                baiduNewsService.CrawlBaiduNewsUrl("2017-11-1", "2017-11-2", new String[]{"同济大学软件学院", "华东师范大学软件学院"}, true);
                break;
            case "sina":
                //搜索关键字、是否根据标题搜索
                sinaNewsService.CrawlSinaNewsUrl(new String[]{"同济大学软件学院", "华东师范大学软件学院"}, true);
                break;
            case "toutiao":
                //搜索关键字
                touTiaoNewsService.CrawlTouTiaoNewsUrl(new String[]{"同济大学软件学院", "华东师范大学软件学院"});
                break;
            case "sogou":
                //起始日期、终止日期、搜索关键字、是否根据标题搜索（true为根据标题，false为根据内容）
                //注意目前仅"搜索关键字"有效，其余参数皆无效
                //使用sogou搜索，自行在lab409/service/impl/SogouNewsService.java 55行左右设置cookie，否则最多读取100条数据
                //搜狗有翻爬虫机制。大约每爬二十页，需要在浏览器中进入搜狗公号的新闻搜索页面，手动输入验证码，使爬虫继续进行
                sogouNewsService.CrawlSogouNewsUrl("2017-11-1", "2017-11-2", new String[]{"同济大学软件学院", "华东师范大学软件学院"}, true);
                break;
            default:
                break;
        }

        Long end = System.currentTimeMillis();
        Long time = (end - start) / 1000 ;
        return String.valueOf(time);
    }



}
