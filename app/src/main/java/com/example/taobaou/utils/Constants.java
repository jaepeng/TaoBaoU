package com.example.taobaou.utils;

public class Constants {

    //base Url
    public static final String BASE_URL ="https://api.sunofbeach.net/shop/";
    public static final String OTHER_BASE_URL ="http://192.168.1.3:8080";
    //home page fragment bundle key
    public static final String KEY_HOME_PAGE_TITLE="key_home_page_title";
    public static final String KEY_HOME_PAGE_MATERIAL_ID="key_home_page_material_id";
    public static final int SUCCESS_CODE=10000;
    public static final String KEY_SEARCH_KEYWORD="key_search_result_keyword";

    /**************************************************用户登录**************************************************/
    public static final String SP_KEY_USER_MAP="sp_key_user_map";
    public static final String SP_KEY_LOGIN_ACCOUNT_MAP="sp_key_login_account_map";
    public static final String SP_KEY_LOGIN_PASSWOARD="sp_key_login_passwoard";



    /**************************************************保存领券记录**************************************************/
    public static final String SP_KEY_HISTORY_TIECKT="sp_key_history_tieckt";
    public static final String SP_KEY_HISTORY_TIECKT_MAP="sp_key_history_tieckt_map";
    public static final String SP_KEY_HISTORY_TIECKT_URL="sp_key_history_tieckt_url";
    public static final String SP_KEY_HISTORY_TIECKT_CODE="sp_key_history_tieckt_code";

    /**************************************************从别的地方跳回ManActivity**************************************************/

    public static final String RETURN_MAIN_FROM_OTHER ="return_main_from_other";

    /**
     * 要到哪个Fragment去
     */
    public static final String GO_TO_WHAT_FRAGMENT="go_to_what_fragment";

    /**
     * 到MyInfoFragment
     */
    public static final String GO_TO_MYINFO_FRAGMENT="go_to_myinfo_fragment";

    /**
     * 传回来的数据
     */
    public static final String RETURN_MAIN_FROM_OTHER_DATA="return_main_from_other_data";


}
