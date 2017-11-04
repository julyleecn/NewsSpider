package lab409.service;

/**
 * Created by xuawai on 03/11/2017.
 */
public interface BaiduNewsService {

    void CrawlBaiduNewsUrl(String startDay, String endDay, String[] keywords, boolean isTitle);

    void CrawlBaiduNews();

}
