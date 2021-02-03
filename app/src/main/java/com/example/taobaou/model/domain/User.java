package com.example.taobaou.model.domain;

public class User {
    private String account;
    private String password;
    private String cover;


    public User(String account, String cover, String password) {
        this.account = account;
        this.cover = cover;
        this.password = password;
    }




    public User(String account, String password) {
        this.account = account;
        this.password = password;
    }

    public User(String account) {
        this.account = account;
    }



    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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


}
