package com.school.manager.model;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

@Data
public class MerItems {

    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;
    private String sku;
    private String itemname;
    private Integer categoryId;
    private String t1CategoryName;
    private Integer t1CategoryId;
    private String t2CategoryName;
    private Integer t2CategoryId;
    private String t3CategoryName;
    private Integer t3CategoryId;
    private String brand;
    private String size;
    private String itemstatus;
    private String postageby;
    private String deliverymethod;
    private String deliveryaddress;
    private String deliverydate;
    private Double price;
    private String des;
    private String images;
    private String chinesemark;
    private Integer userid;
    private String createtime;
    private String updatetime;
    private Integer utimes;
    private String mark;
    private Integer waiting;
    @Transient
    private String firstImage;
    @Transient
    private String size1;
    @Transient
    private String size2;
    @Transient
    private String size3;
    @Transient
    private String file;
    @Transient
    private List<Image> list = new ArrayList<>();
}
