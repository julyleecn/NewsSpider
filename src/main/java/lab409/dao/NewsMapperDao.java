package lab409.dao;

import lab409.mapper.NewsMapper;
import lab409.model.News;
import lab409.model.Url;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xuawai on 03/11/2017.
 */
@Repository
public class NewsMapperDao {

    @Autowired
    private NewsMapper newsMapper;

    public void updateNews(String source, News news){
        try{
            switch(source){
                case "baidu":
                    newsMapper.updateBaiduNews(news);
                    break;
                case "sina":
                    newsMapper.updateSinaNews(news);
                    break;
                case "toutiao":
                    newsMapper.updateTouTiaoNews(news);
                    break;
                case "sogou":
                    newsMapper.updateSogouNews(news);
                    break;
                default:
                    break;
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

//    public List<News> getNews(String source){
//        switch(source){
//            case "baidu":
//                try{
//                    return newsMapper.getBaiduNews();
//                }catch (Exception e){
//                    System.out.println(e.getMessage());
//                }
//            default:
//                break;
//        }
//        return null;
//    }

}
