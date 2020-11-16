package com.example.taobaou.utils;

public class UrlUtils {
    public static String createHomePagerUrl(int materialId,int page){
        // /discovery/{materialId}/{page}
        return "discovery/"+materialId+"/"+page;

    }

    public static String getCoverPath(String pict_url,int size) {
//        return "https:"+pict_url+"_"+size+"x"+size+".jpg";
        return "https:"+pict_url;
    }
    public static String getCoverPath(String pict_url) {
        return "https:"+pict_url;
    }

    public static String getTicketUrl(String url) {
        if (url.startsWith("http")||url.startsWith("https")){
            return url;
        }else {
            return "https:"+url;
        }
    }
}
