package com.example.jhta_3team_finalproject.domain.TourPackage;

public class tripMailVO {
    private String  from ="rlatks15@naver.com";
    private String to;
    private String subject="회원가입을 축하드립니다.";
    private String content;
    private String random;

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }


}
