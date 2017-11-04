package lab409.service.impl;

import lab409.model.News;
import lab409.model.Url;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by xuawai on 04/11/2017.
 */
public class BaseNewsOperation {

    public static News CrawlBodyFromTagP(Url url){
        Document doc = null;
        News news = new News();

        try {
            doc = Jsoup.connect(url.getUrl()).get();

            Elements elements = doc.getElementsByTag("p");
            StringBuffer sb = new StringBuffer();
            for (Element element : elements){
                sb.append(element.text());
            }
            String content = sb.toString();
            news.setBody(content);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return news;
    }
}
