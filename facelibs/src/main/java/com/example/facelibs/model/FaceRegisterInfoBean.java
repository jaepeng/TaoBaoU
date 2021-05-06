package com.example.facelibs.model;

public class FaceRegisterInfoBean {
    private Integer id;
    private String name;

    public FaceRegisterInfoBean( String name) {
        this.name = name;
    }

    public FaceRegisterInfoBean(Integer id, byte[] featureData, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
