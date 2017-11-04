package lab409.controller;

import lab409.service.BaiduNewsService;
import lab409.service.SinaNewsService;
import lab409.service.SogouNewsService;
import lab409.service.TouTiaoNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by xuawai on 03/11/2017.
 */
@RestController
@RequestMapping({"/news"})
public class NewsController {


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
                baiduNewsService.CrawlBaiduNews();
                break;
            case "sina":
                sinaNewsService.CrawlSinaNews();
                break;
            case "toutiao":
                touTiaoNewsService.CrawlTouTiaoNews();
                break;
            case "sogou":
                sogouNewsService.CrawlSogouNews();
                break;
            default:
                break;
        }

        Long end = System.currentTimeMillis();
        Long time = (end - start) / 1000 ;
        return String.valueOf(time);
    }

}
