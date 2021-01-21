package com.example.facelibs.model.message;

public class MessageEvent {
    private int MessageCode;
    private String mkeyword;

    public MessageEvent(int messageCode, String keyword) {
        MessageCode = messageCode;
        mkeyword = keyword;
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
