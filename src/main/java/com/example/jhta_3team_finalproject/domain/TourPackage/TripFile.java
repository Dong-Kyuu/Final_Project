package com.example.jhta_3team_finalproject.domain.TourPackage;

public class TripFile {
    private String fileId;
    private String mainIMG;
    private String introIMG;
    private String routeIMG;
    private String scheduleIMG;
    private String detailIMG;

    public String getFileNo() {
        return fileId;
    }

    public void setFileNo(String fileId) {
        this.fileId = fileId;
    }

    public String getMainIMG() {
        return mainIMG;
    }

    public void setMainIMG(String mainIMG) {
        this.mainIMG = mainIMG;
    }

    public String getIntroIMG() {
        return introIMG;
    }

    public void setIntroIMG(String introIMG) {
        this.introIMG = introIMG;
    }

    public String getRouteIMG() {
        return routeIMG;
    }

    public void setRouteIMG(String routeIMG) {
        this.routeIMG = routeIMG;
    }

    public String getScheduleIMG() {
        return scheduleIMG;
    }

    public void setScheduleIMG(String scheduleIMG) {
        this.scheduleIMG = scheduleIMG;
    }

    public String getDetailIMG() {
        return detailIMG;
    }

    public void setDetailIMG(String detailIMG) {
        this.detailIMG = detailIMG;
    }
}
