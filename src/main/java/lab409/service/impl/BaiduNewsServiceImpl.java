package lab409.service.impl;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lab409.dao.NewsMapperDao;
import lab409.dao.UrlMapperDao;
import lab409.mapper.UrlMapper;
import lab409.model.News;
import lab409.model.Url;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lab409.service.BaiduNewsService;
import lab409.utils.StringUtil;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xuawai on 03/11/2017.
 */
@Service
public class BaiduNewsServiceImpl implements BaiduNewsService {


    @Autowired
    UrlMapperDao urlMapperDao;

    @Autowired
    NewsMapperDao newsMapperDao;

    /*
    startDate、endDate : yyyy-mm-dd
    keys : [ "key1" , "key2" ]
    istitle : search title ? ture . search body? false .
    crawl urls from baidu news
     */
    public void CrawlBaiduNewsUrl(String startDay, String endDay, String[] keywords, boolean isTitle){


        Long start = StringUtil.parseDateStr(startDay);
        Long end = StringUtil.parseDateStr(endDay);
        Long oneDay = 1000 * 60 * 60 * 24L;
        Long time = start;
        while (time <= end) {
            Date d = new Date(time);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            int[] result = StringUtil.parseDateStrToInt(df.format(d));
            int year = result[0];
            int month = result[1];
            int day = result[2];

            for(int key_index = 0; key_index < keywords.length; key_index++) {
                String keyword = null;
                try {
                    keyword = URLEncoder.encode(keywords[key_index], "utf-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int index = 0;
                while (true) {

                    Date date = new Date(1969, 12, 30, 8, 0);
                    Date bDate = new Date(year, month, day);
                    Date eDate = new Date(year, month, day + 1);
                    String bt = Long.toString((bDate.getTime() - date.getTime()) / 1000);
                    String et = Long.toString((eDate.getTime() - date.getTime()) / 1000 - 1);

                    String url = null;
                    if (isTitle)
                        url = "http://news.baidu.com/ns?from=news&cl=2&bt=" + bt + "&y0=" + year
                                + "&m0=" + month + "&d0=" + day + "&y1=" + year + "&m1=" + month + "&d1=" + day
                                + "&et=" + et + "&q1=" + keyword + "&submit=%B0%D9%B6%C8%D2%BB%CF%C2&q3=&q4=&mt=0&lm=&s=2&begin_date="
                                + year + "-" + month + "-" + day + "&end_date=" + year + "-" + month + "-" + day
                                + "&tn=newstitledy&ct=0&rn=20&q6=&pn=" + index;
                    else
                        url = "http://news.baidu.com/ns?from=news&cl=2&bt=" + bt + "&y0=" + year
                                + "&m0=" + month + "&d0=" + day + "&y1=" + year + "&m1=" + month + "&d1=" + day
                                + "&et=" + et + "&q1=" + keyword + "&submit=%B0%D9%B6%C8%D2%BB%CF%C2&q3=&q4=&mt=0&lm=&s=2&begin_date="
                                + year + "-" + month + "-" + day + "&end_date=" + year + "-" + month + "-" + day
                                + "&tn=newstitledy&ct1=1&ct=1&rn=20&q6=&pn=" + index;
                    System.out.println("URL:" + url);

                    String title = null;
                    String publish_time = null;
                    String writer = null;
                    String source = "baidu";
                    Document doc = null;
                    //搜索结果现实搜索到的新闻数量
                    int num = 0;
                    try {
                        doc = Jsoup.connect(url).timeout(5000).get();
                        String numStr = doc.getElementsByClass("nums").text();
                        num = StringUtil.matchNumberFromStr(numStr);

                        Elements elements = doc.getElementsByClass("result");
                        for (Element element : elements) {
                            Url mUrl = new Url();
                            Element href = element.select("a[href]").get(0);
                            String raw_url = href.attr("abs:href");
                            title = href.text();
                            if (isTitle) {

                                Element writerAndTime = element.select("div.c-title-author").get(0);
                                String[] writerStr = writerAndTime.text().split(String.valueOf(Character.valueOf('\240')));
                                writer = writerStr[0];
                                publish_time = writerStr[2];
                            } else {
                                Element writerAndTime = element.select("div.c-author").get(0);
                                String[] writerStr = writerAndTime.text().split(String.valueOf(Character.valueOf('\240')));
                                writer = writerStr[0];
                                publish_time = writerStr[2];
                            }
                            mUrl.setUrl(raw_url);
                            mUrl.setKeyword(keywords[key_index]);
                            mUrl.setTitle(title);
                            mUrl.setWriter(writer);
                            mUrl.setPublish_time(publish_time);
                            mUrl.setSource(source);

                            System.out.println(mUrl);
                            urlMapperDao.insertUrl("baidu", mUrl);
                        }
                    } catch (IOException e) {
                        index += 20;
                        continue;
                    }
                    if(doc != null){
                        if(!doc.text().contains("下一页")){
                            break;
                        }
                    }
                    index += 20;
                    if (index > num)
                        break;

                    System.out.println("------------------" + df.format(d) + ":" + index + "----------------");
                }
            }



            time += oneDay;
        }


        System.out.println("-------------------spider is over!-------------------");

    }

    public void CrawlBaiduNews(){

        List<Url> urlList = urlMapperDao.getUrls("baidu");

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
                newsMapperDao.updateNews("baidu", news);
            }

        }


        System.out.println("-------------------spider is over!-------------------");

    }




}
