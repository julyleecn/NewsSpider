package lab409.model;

import com.alibaba.fastjson.JSONObject;

public class News extends Url{

    private String body;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
