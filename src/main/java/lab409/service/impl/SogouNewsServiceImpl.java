package lab409.service.impl;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import lab409.dao.NewsMapperDao;
import lab409.dao.UrlMapperDao;
import lab409.model.News;
import lab409.model.Url;
import lab409.service.SogouNewsService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by xuawai on 04/11/2017.
 */
@Service
public class SogouNewsServiceImpl implements SogouNewsService {


    @Autowired
    UrlMapperDao urlMapperDao;

    @Autowired
    NewsMapperDao newsMapperDao;

    /*
    目前只有key参数起作用
     */
    public void CrawlSogouNewsUrl(String startDay, String endDay, String[] keywords, boolean isTitle){

        List<Url> urlsList = new ArrayList<Url>();
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.getOptions().setTimeout(35000);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.addRequestHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3192.0 Safari/537.36");
        webClient.waitForBackgroundJavaScript(20000);
        //webclient开启cookie
        webClient.getCookieManager().setCookiesEnabled(true);
        Set<Cookie> cookies= new HashSet<>();
        //添加cookie
        //下面这一行为cookie格式的示例
//        cookies.add(new Cookie("weixin.sogou.com", "CXID", "A50A96F377FBDFE702F8DA83F628D740"));
        Iterator<Cookie> i;
        i = cookies.iterator();
        while (i.hasNext())
        {
            webClient.getCookieManager().addCookie(i.next());
        }


        for(int key_index = 0; key_index < keywords.length; key_index++){
            String keyword = null;
            try {
                keyword = URLEncoder.encode(keywords[key_index], "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
            int page = 1;
            while(true){

                String url ="http://weixin.sogou.com/weixin?query=" + keyword +
                        "&_sug_type_=&s_from=input&_sug_=n&type=2&page=" + page + "&ie=utf8";

//                String url = "http://weixin.sogou.com/weixin?usip=&query=" + key +"&ft="+year+"-"+month+"-"+day+"&tsn=5&et="+year+"-"+month+"-"+day+"&interation=&type=2&wxid=&page=2&ie=utf8";

                String title = null;
                String writer = null;
                String source = "sogou";
                HtmlPage mypage = null;
                try {
                    mypage = webClient.getPage(url);
                    Document doc = Jsoup.parse(mypage.asXml());
                    Elements elements = doc.getElementsByClass("txt-box");
                    for(Element element : elements){
                        Url mUrl = new Url();

                        Element href = element.select("a[href]").get(0);
                        String raw_url = href.attr("abs:href");

                        title = href.text();

                        if(element.select("a.account")!=null)
                            writer = element.select("a.account").get(0).text();

                        mUrl.setUrl(raw_url);
                        mUrl.setKeyword(keywords[key_index]);
                        mUrl.setTitle(title);
                        mUrl.setWriter(writer);
                        mUrl.setSource(source);

                        System.out.println(mUrl);
                        urlMapperDao.insertUrl("sogou", mUrl);
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
                if(mypage != null){
                    if(!mypage.asText().contains("下一页")){
                        break;
                    }
                }else{
                    break;
                }
                page += 1;
                try {
                    Thread.sleep((int) (500 + Math.random() * 2000));
                }catch (Exception e){
                    e.printStackTrace();
                }
                System.out.println("----------------------------------第" + page + "页----------------------" + keywords[key_index]);
            }
        }

        System.out.println("-------------------spider is over!-------------------");
    }

    public void CrawlSogouNews(){
        List<Url> urlList = urlMapperDao.getUrls("sogou");

        for(Url url : urlList) {
            Document doc = null;
            String public_time = null;
            String body = null;
            News news = new News();

            try {
                doc = Jsoup.connect(url.getUrl()).timeout(5000).get();

                Elements article_sub = doc.getElementsByClass("rich_media_meta");

                if(article_sub.size() != 0){
                    public_time = article_sub.get(0).text();
                    if(public_time.equals("原创"))
                        public_time = article_sub.get(1).text();
                }


                Elements elements = doc.getElementsByTag("p");
                StringBuffer sb = new StringBuffer();
                //k 从2开始，前两个是元数据
                for (int k=2; k<elements.size(); k++){

                    sb.append(elements.get(k).text());
                }
                body = sb.toString();

                news.setUrl(url.getUrl());
                news.setSource(url.getSource());
                news.setKeyword(url.getKeyword());
                news.setWriter(url.getWriter());
                news.setPublish_time(public_time);
                news.setTitle(url.getTitle());
                news.setBody(body);
                newsMapperDao.updateNews("sogou", news);

                System.out.println(news.toString());

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("-------------------spider is over!-------------------");
    }
}
