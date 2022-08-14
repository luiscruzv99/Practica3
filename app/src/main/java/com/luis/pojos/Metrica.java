package com.luis.pojos;

public class Metrica {

    int clicks, testId, order;
    long tiempo;
    String ifaceName;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getIfaceName() {
        return ifaceName;
    }

    public void setIfaceName(String ifaceName) {
        this.ifaceName = ifaceName;
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public long getTiempo() {
        return tiempo;
    }

    public void setTiempo(long tiempo) {
        this.tiempo = tiempo;
    }

    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    public Metrica(){}

    public Metrica(int clicks, long time, String ifaceName, int testId){
        this.clicks = clicks;
        this.tiempo = time;
        this.ifaceName = ifaceName;
        this.testId = testId;
        this.order = Repository.getORDER();
    }

}
