package com.example.taobaou.model.message;

public class MessageEvent {
    private int MessageCode;
    private String mkeyword;

    public MessageEvent(int messageCode, String keyword) {
        MessageCode = messageCode;
        mkeyword = keyword;
    }

    public MessageEvent(int messageCode) {
        MessageCode = messageCode;
    }

    public String getMkeyword() {
        return mkeyword;
    }

    public int getMessageCode() {
        return MessageCode;
    }

    public void setMessageCode(int messageCode) {
        MessageCode = messageCode;
    }
}
