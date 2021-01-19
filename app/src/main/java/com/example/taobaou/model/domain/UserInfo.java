package com.example.taobaou.model.domain;

public class UserInfo {
    private String account;
    private String cover;
    private boolean isLogin;

    public UserInfo(String account, String cover, boolean isLogin) {
        this.account = account;
        this.cover = cover;
        this.isLogin = isLogin;
    }


    public UserInfo(String account, String cover) {
        this.account = account;
        this.cover = cover;
    }

    public UserInfo(String account) {
        this.account = account;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "account='" + account + '\'' +
                ", cover='" + cover + '\'' +
                ", isLogin=" + isLogin +
                '}';
    }
}
