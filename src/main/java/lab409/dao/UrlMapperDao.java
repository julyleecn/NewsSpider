package lab409.dao;

import lab409.mapper.UrlMapper;
import lab409.model.Url;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xuawai on 03/11/2017.
 */
@Repository
public class UrlMapperDao {

    @Autowired
    private UrlMapper urlMapper;



    public void insertUrl(String source, Url url) {


        try {
            switch (source) {
                case "baidu":
                    urlMapper.insertBaiduUrl(url);
                    break;
                case "sina":
                    urlMapper.insertSinaUrl(url);
                    break;
                case "toutiao":
                    urlMapper.insertTouTiaoUrl(url);
                    break;
                case "sogou":
                    urlMapper.insertSogouUrl(url);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }





    public List<Url> getUrls(String source){

        try{
             switch(source){
                 case "baidu":
                     return urlMapper.getBaiduUrls();
                 case "sina":
                     return urlMapper.getSinaUrls();
                 case "toutiao":
                     return urlMapper.getTouTiaoUrls();
                 case "sogou":
                     return urlMapper.getSogouUrls();
                 default:
                     break;
             }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

}
