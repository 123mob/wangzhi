package com.example.wangzhi6.Sql;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/4 0004.
 */

public class UserData implements Serializable {
    private int id;
    private String title;
    private String remark;
    private String url_icon;
    private String url;
//userDo.addSql(title,remark,url_icon,url);
    public UserData() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUrl_icon() {
        return url_icon;
    }

    public void setUrl_icon(String url_icon) {
        this.url_icon = url_icon;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", remark='" + remark + '\'' +
                ", url_icon='" + url_icon + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public UserData(int id, String title, String remark, String url_icon, String url) {
        this.id = id;
        this.title = title;
        this.remark = remark;
        this.url_icon = url_icon;
        this.url = url;
    }




}
