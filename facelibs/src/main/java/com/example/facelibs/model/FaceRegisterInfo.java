package com.example.facelibs.model;

public class FaceRegisterInfo {
    private Integer id;
    private byte[] featureData;
    private String name;

    public FaceRegisterInfo(byte[] faceFeature, String name) {
        this.featureData = faceFeature;
        this.name = name;
    }

    public FaceRegisterInfo(Integer id, byte[] featureData, String name) {
        this.id = id;
        this.featureData = featureData;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getFeatureData() {
        return featureData;
    }

    public void setFeatureData(byte[] featureData) {
        this.featureData = featureData;
    }
}
