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
        if (!pict_url.contains("https")){

            return "https:"+pict_url;
        }
        return pict_url;
    }

    public static String getTicketUrl(String url) {
        if (url.startsWith("http")||url.startsWith("https")){
            return url;
        }else {
            return "https:"+url;
        }
    }

    public static String getSelectedPgaeContentUrl(int categoryID) {
        return "recommend/"+categoryID;
    }
    public static String getSelectedCover(String url){
        if (url.startsWith("http")||url.startsWith("https")){
            return url;
        }else {
            return "https:"+url;
        }
    }

    public static String getOnSellPageUrl(int currentPage) {
        return "onSell/"+currentPage;

    }
}
