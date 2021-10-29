package com.school.manager.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class MarketConfirm {

    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;
    private String userid;
    private String phoneid;
    private String createtime;
    private String updatetime;
    private String isconfirm;
    private String marketsource;

    public void setId(Integer id){
        this.id = id;
    }

    public Integer getId(){
        return id;
    }

    public void setUserid(String userid){
        this.userid = userid;
    }

    public String getUserid(){
        return userid;
    }

    public void setPhoneid(String phoneid){
        this.phoneid = phoneid;
    }

    public String getPhoneid(){
        return phoneid;
    }

    public void setCreatetime(String createtime){
        this.createtime = createtime;
    }

    public String getCreatetime(){
        return createtime;
    }

    public void setUpdatetime(String updatetime){
        this.updatetime = updatetime;
    }

    public String getUpdatetime(){
        return updatetime;
    }

    public void setIsconfirm(String isconfirm){
        this.isconfirm = isconfirm;
    }

    public String getIsconfirm(){
        return isconfirm;
    }

    public void setMarketsource(String marketsource){
        this.marketsource = marketsource;
    }

    public String getMarketsource(){
        return marketsource;
    }


}
