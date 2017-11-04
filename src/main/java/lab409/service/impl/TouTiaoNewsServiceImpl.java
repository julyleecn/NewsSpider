package lab409.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lab409.dao.NewsMapperDao;
import lab409.dao.UrlMapperDao;
import lab409.mapper.UrlMapper;
import lab409.model.News;
import lab409.model.Url;
import lab409.service.TouTiaoNewsService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
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
 * Created by xuawai on 04/11/2017.
 */
@Service
public class TouTiaoNewsServiceImpl implements TouTiaoNewsService{

    @Autowired
    UrlMapperDao urlMapperDao;

    @Autowired
    NewsMapperDao newsMapperDao;

    public void CrawlTouTiaoNewsUrl(String[] keywords){
        for(String key : keywords){
            int index = 0;
            while(true){
                String url = "https://www.toutiao.com/search_content/?offset="
                        + index + "&format=json&keyword=" + key
                        + "&autoload=true&count=20&cur_tab=1";
                //获取目标
                List<Url> list = getDetailUrls(url, key);
                if(list == null){
                    break;
                }
                index += 20;
            }
        }

        System.out.println("-------------------spider is over!-------------------");

    }

    public void CrawlTouTiaoNews(){

        List<Url> urlList = urlMapperDao.getUrls("toutiao");

        for(Url url : urlList) {

            String title = null;
            String public_time = null;
            String body = null;
            News news = new News();

            //jsoup拿不到正确内容
            WebClient webClient = new WebClient(BrowserVersion.CHROME);
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setCssEnabled(false);
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
            webClient.getOptions().setTimeout(10000);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.addRequestHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3192.0 Safari/537.36");
            webClient.waitForBackgroundJavaScript(20000);

            try {
                HtmlPage page = webClient.getPage(url.getUrl());
                Document doc = Jsoup.parse(page.asXml());

                Elements article_title = doc.getElementsByClass("article-title");
                if (article_title.size() != 0) {
                    title = article_title.get(0).text();
                }
                Elements article_sub = doc.getElementsByClass("article-sub");

                if (article_sub.size() != 0) {
                    public_time = article_sub.get(0).text().substring(article_sub.get(0).text().indexOf(" ") + 1);
                }

                Elements article_content = doc.getElementsByClass("article-content");
                if (article_content.size() != 0) {
                    body = article_content.get(0).text();
                }

                news.setUrl(url.getUrl());
                news.setSource(url.getSource());
                news.setKeyword(url.getKeyword());
                news.setWriter(url.getWriter());
                news.setPublish_time(public_time);
                news.setTitle(title);
                news.setBody(body);
                newsMapperDao.updateNews("toutiao", news);

                System.out.println(news.toString());

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("-------------------spider is over!-------------------");

    }

    private List<Url> getDetailUrls(String url, String key){
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response;
        List<Url> urls = new ArrayList<Url>();
        try{
            response = httpClient.execute(httpGet);
            String temp;
            HttpEntity entity = response.getEntity();
            temp = EntityUtils.toString(entity,"UTF-8");
            JSONObject jsStr = JSONObject.parseObject(temp);
            if(jsStr.getIntValue("return_count") < 1){
                return null;
            }else{
                JSONArray data = jsStr.getJSONArray("data");
                for(Object item : data){
                    JSONObject jsonObject = JSONObject.parseObject(item.toString());
                    //System.out.println(jsonObject.getString("group_id"));
                    if(jsonObject.getString("group_id") != null){
                        String init_url = "http://www.toutiao.com/a" + jsonObject.getString("group_id") + "/";
                        String writer = jsonObject.getString("source");
                        Url mUrl = new Url();
                        mUrl.setUrl(init_url);
                        mUrl.setWriter(writer);
                        mUrl.setSource("toutiao");
                        mUrl.setKeyword(key);

                        urlMapperDao.insertUrl("toutiao", mUrl);
                        urls.add(mUrl);
                        System.out.println(init_url + "　" + key);
                    }
                }
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return urls;
    }

}
