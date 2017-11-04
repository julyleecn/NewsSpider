package lab409.service;

/**
 * Created by xuawai on 03/11/2017.
 */
public interface SinaNewsService {

    void CrawlSinaNewsUrl(String[] keywords, boolean isTitle);

    void CrawlSinaNews();
}
