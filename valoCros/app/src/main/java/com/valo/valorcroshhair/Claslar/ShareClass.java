package com.valo.valorcroshhair.Claslar;

public class ShareClass {
    String crosCode,crosName,crosURL,crosProfImage,userName;

    public ShareClass() {
    }

    public ShareClass(String crosCode, String crosName, String crosURL,String crosProfImage,String userName) {
        this.crosCode = crosCode;
        this.crosName = crosName;
        this.crosURL = crosURL;
        this.crosProfImage = crosProfImage;
        this.userName = userName;
    }

    public String getCrosCode() {
        return crosCode;
    }

    public void setCrosCode(String crosCode) {
        this.crosCode = crosCode;
    }

    public String getCrosName() {
        return crosName;
    }

    public void setCrosName(String crosName) {
        this.crosName = crosName;
    }

    public String getCrosURL() {
        return crosURL;
    }

    public void setCrosURL(String crosURL) {
        this.crosURL = crosURL;
    }

    public String getCrosProfImage() {
        return crosProfImage;
    }

    public void setCrosProfImage(String crosProfImage) {
        this.crosProfImage = crosProfImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
