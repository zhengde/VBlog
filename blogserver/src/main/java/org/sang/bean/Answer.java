package org.sang.bean;

public class Answer {
    private Long id;
    private Long uid;
    private String t_id;
    private int status;
    private String type;
    private Long praise_num;
    private Long collect_num;

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

    public String getT_id() {
        return t_id;
    }

    public void setT_id(String t_id) {
        this.t_id = t_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
