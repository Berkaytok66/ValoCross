package com.valo.valorcroshhair.Claslar;

public class AdminVeriEkle {
    String Url,CrosName,CrosCode;
    private boolean expanded;




    public AdminVeriEkle(String url, String crosName, String crosCode) {
        Url = url;
        CrosName = crosName;
        CrosCode = crosCode;
        this.expanded = false;
    }
    public AdminVeriEkle() {
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getCrosName() {
        return CrosName;
    }

    public void setCrosName(String crosName) {
        CrosName = crosName;
    }

    public String getCrosCode() {
        return CrosCode;
    }

    public void setCrosCode(String crosCode) {
        CrosCode = crosCode;
    }
    public boolean isExpanded()
    {
        return expanded;
    }

    public void setExpanded(boolean expanded)
    {
        this.expanded = expanded;
    }

}
