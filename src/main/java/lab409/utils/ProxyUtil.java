package lab409.utils;

import java.util.ArrayList;
import java.util.List;


public class ProxyUtil {
	private static List<String> proxyIpList;
	static{
		proxyIpList=new ArrayList<String>();
		proxyIpList.add("166.62.86.208:8080");
		proxyIpList.add("112.216.16.250:3128");
		proxyIpList.add("185.89.217.106:3128");
	}
	//随机获取一个请求头
    public static String getOneRandom(){
    	return proxyIpList.get(
    	(int) Math.round(Math.random()*(proxyIpList.size()-1))); 
    	
    }
    public static List<String> getList(){
    	return proxyIpList;
    }
}
