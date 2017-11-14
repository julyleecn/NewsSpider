package lab409.service.impl;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lab409.dao.NewsMapperDao;
import lab409.dao.UrlMapperDao;
import lab409.model.News;
import lab409.model.Url;
import lab409.service.SinaNewsService;
import lab409.utils.StringUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuawai on 03/11/2017.
 */
@Service
public class SinaNewsServiceImpl implements SinaNewsService {

    @Autowired
    UrlMapperDao urlMapperDao;

    @Autowired
    NewsMapperDao newsMapperDao;

    public void CrawlSinaNewsUrl(String[] keywords, boolean isTitle){

        for(int key_index = 0; key_index < keywords.length; key_index++){
            String keyword = null;
            try {
                keyword = URLEncoder.encode(keywords[key_index], "GB2312");
            } catch (Exception e) {
                e.printStackTrace();
            }
            int page = 1;
            while(true){
                String url = null;

                if(isTitle)
                    url ="http://search.sina.com.cn/?q="+ keyword +"&range=title&c=news&sort=time&col=&source=&from=&country=&size=&time=&a=&page="+
                            page;
                else
                    url ="http://search.sina.com.cn/?q="+ keyword +"&range=all&c=news&sort=time&col=&source=&from=&country=&size=&time=&a=&page="+
                            page;

                String title = null;
                String publish_time = null;
                String writer = null;
                String source = "sina";
                Document doc = null;
                try {
                    doc = Jsoup.connect(url).timeout(5000).get();
                    Elements elements = doc.getElementsByClass("box-result");
                    for(Element element : elements){
                        Url mUrl = new Url();

                        Element href = element.select("a[href]").get(0);
                        String raw_url = href.attr("abs:href");

                        title = href.text();

                        Element writerAndTime =  element.select("span.fgray_time").get(0);
                        String[] info = writerAndTime.text().split(" ");
                        if(info.length == 2) {
                            writer = null;
                            publish_time = info[0] +" " + info[1];
                        }
                        else if(info.length == 3) {
                            writer = info[0];
                            publish_time = info[1] + " " + info[2];
                        }




                        mUrl.setUrl(raw_url);
                        mUrl.setKeyword(keywords[key_index]);
                        mUrl.setTitle(title);
                        mUrl.setWriter(writer);
                        mUrl.setPublish_time(publish_time);
                        mUrl.setSource(source);

                        System.out.println(mUrl);
                        urlMapperDao.insertUrl("sina", mUrl);
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
                if(doc != null){
                    if(!doc.text().contains("下一页")){
                        break;
                    }
                }
                page += 1;

                System.out.println("------------------page" + ":" + page + "----------------");
            }
        }

        System.out.println("-------------------spider is over!-------------------");
    }

    public void CrawlSinaNews(){
        List<Url> urlList = urlMapperDao.getUrls("sina");

        for(Url url : urlList) {
            News news = null;
            news = BaseNewsOperation.CrawlBodyFromTagP(url);

            System.out.println(news);
            if(news.getBody()!=null) {
                news.setUrl(url.getUrl());
                news.setSource(url.getSource());
                news.setKeyword(url.getKeyword());
                news.setWriter(url.getWriter());
                news.setPublish_time(url.getPublish_time());
                news.setTitle(url.getTitle());
                newsMapperDao.updateNews("sina", news);
            }

        }

        System.out.println("-------------------spider is over!-------------------");
    }



    private News CrawlBodyFromType(Url url){
        Document doc = null;
        News news = new News();

        try {
            doc = Jsoup.connect(url.getUrl()).get();

            Elements article_title = doc.getElementsByClass("lcBlk");
            if(article_title.size()!=0){

                Elements body = doc.getElementsByClass("artibody");
                if(body.size()!=0)
                    news.setBody(body.text());

                return news;
            }

            article_title = doc.getElementsByClass("page-header");
            if(article_title.size()!=0){

                Elements body = doc.getElementsByClass("article");
                if(body.size()!=0)
                    news.setBody(body.text());

                return news;
            }

            article_title = doc.getElementsByClass("f24");
            if(article_title.size()!=0){

                Elements article = doc.getElementsByAttributeValue("id", "article");
                if(article.size()!=0) {
                    Element body = article.select("tr").get(4);
                    if(body!=null)
                        news.setBody(body.text());
                    return news;
                }
//                http://news.sina.com.cn/o/2003-12-25/03421423727s.shtml

                Elements body = doc.getElementsByClass("l17");
                if(body.size()!=0) {
                    if(StringUtil.isUTF8(body.text()))
                        news.setBody(body.text());
                    else
                        news.setBody(StringUtil.getUTF8StringFromGBKString(body.text()));
                    return news;
                }

            }

            article_title = doc.getElementsByClass("main-title");
            if(article_title.size()!=0){

                Elements body = doc.getElementsByClass("article");
                if(body.size()!=0)
                    news.setBody(body.text());

                return news;
            }

            article_title = doc.getElementsByAttributeValue("id", "artibodyTitle");
            if(article_title.size()!=0){

                Elements body = doc.getElementsByClass("articontent");
                if(body.size()!=0) {
                    news.setBody(body.text());
                    return news;
                }

                body = doc.getElementsByAttributeValue("id", "articleContent");
                if(body.size()!=0) {
                    news.setBody(body.text());
                    return news;
                }

                body = doc.getElementsByClass("artibody");
                if(body.size()!=0) {
                    news.setBody(body.text());
                    return news;
                }

                body = doc.getElementsByAttributeValue("id", "artibody");
                if(body.size()!=0) {
                    news.setBody(body.text());
                    return news;
                }


                return news;
            }

            article_title = doc.getElementsByAttributeValue("color", "#3100de");
            if(article_title.size()!=0){

                Elements body = doc.getElementsByClass("l17");
                if(body.size()!=0)
                    news.setBody(body.text());

                return news;
            }

            article_title = doc.getElementsByAttributeValue("color", "#05006C");
            if(article_title.size()!=0){

                return news;
            }

            article_title = doc.getElementsByClass("swp-tit");
            if(article_title.size()!=0){

                Elements body = doc.getElementsByClass("swpt-cont");
                if(body.size()!=0)
                    news.setBody(body.text());

                return news;
            }

//            http://video.sina.com.cn/p/news/v/2013-10-26/084563066609.html
            article_title = doc.getElementsByClass("Vd_titBox");
            if(article_title.size()!=0){

                news.setBody("This is a video.");

                return news;
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
        return news;
    }



}
