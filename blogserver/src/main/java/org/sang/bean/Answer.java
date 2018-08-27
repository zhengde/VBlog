package org.sang.bean;

public class Answer {
    private Long id;
    private Long aid;
    private Long uid;
//    private Long cid;
    private int status;
    private String content;
    private String title;
    private Long praise_num;
    private Long collect_num;
    private Long create_time;

    public Long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Long create_time) {
        this.create_time = create_time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getAid() {
        return aid;
    }

    public void setAid(Long aid) {
        this.aid = aid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Long getPraise_num() {
        return praise_num;
    }

    public void setPraise_num(Long praise_num) {
        this.praise_num = praise_num;
    }

    public Long getCollect_num() {
        return collect_num;
    }

    public void setCollect_num(Long collect_num) {
        this.collect_num = collect_num;
    }
}
