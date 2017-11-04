package lab409.service;

/**
 * Created by xuawai on 04/11/2017.
 */
public interface SogouNewsService {

    void CrawlSogouNewsUrl(String startDay, String endDay, String[] keywords, boolean isTitle);

    void CrawlSogouNews();
}
