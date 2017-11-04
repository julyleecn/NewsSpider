package lab409.mapper;

import lab409.model.News;
import lab409.model.Url;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface NewsMapper {

    /**
     * 插入数据库一条数据
     * @param news
     * @return
     */


    @Update("UPDATE news_baidu SET body=#{body} WHERE url=#{url};")
    int updateBaiduNews(News news);

    @Select("select url, keyword, title, writer, publish_time, source, body from news_baidu")
    List<News> getBaiduNews();

    @Update("UPDATE news_sina SET body=#{body} WHERE url=#{url};")
    int updateSinaNews(News news);

    @Select("select url, keyword, title, writer, publish_time, source, body from news_sina")
    List<News> getSinaNews();

    @Update("UPDATE news_toutiao SET title=#{title}, publish_time=#{publish_time}, body=#{body} WHERE url=#{url};")
    int updateTouTiaoNews(News news);

    @Select("select url, keyword, title, writer, publish_time, source, body from news_toutiao")
    List<News> getTouTiaoNews();

    @Update("UPDATE news_sogou SET publish_time=#{publish_time}, body=#{body} WHERE url=#{url};")
    int updateSogouNews(News news);

    @Select("select url, keyword, title, writer, publish_time, source, body from news_sogou")
    List<News> getSogouNews();
}
