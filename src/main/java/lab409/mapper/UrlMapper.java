package lab409.mapper;

import lab409.model.Url;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UrlMapper {

    /**
     * @param news
     * @return
     */
    @Insert("INSERT INTO news_baidu (url, keyword, title, writer, publish_time, source)"+
            " VALUES (#{url},#{keyword},#{title},#{writer},#{publish_time},#{source});")
    int insertBaiduUrl(Url url);


    @Select("select url, keyword, title, writer, publish_time, source from news_baidu")
    List<Url> getBaiduUrls();

    @Insert("INSERT INTO news_sina (url, keyword, title, writer, publish_time, source)"+
            " VALUES (#{url},#{keyword},#{title},#{writer},#{publish_time},#{source});")
    int insertSinaUrl(Url url);


    @Select("select url, keyword, title, writer, publish_time, source from news_sina")
    List<Url> getSinaUrls();

    @Insert("INSERT INTO news_toutiao (url, keyword, writer, source)"+
            " VALUES (#{url},#{keyword},#{writer},#{source});")
    int insertTouTiaoUrl(Url url);


    @Select("select url, keyword, writer, source from news_toutiao")
    List<Url> getTouTiaoUrls();

    @Insert("INSERT INTO news_sogou (url, keyword, title, writer, source)"+
            " VALUES (#{url},#{keyword},#{title},#{writer},#{source});")
    int insertSogouUrl(Url url);


    @Select("select url, keyword, title, writer, source from news_sogou")
    List<Url> getSogouUrls();


}
