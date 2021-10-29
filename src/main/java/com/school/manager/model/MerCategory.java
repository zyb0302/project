package com.school.manager.model;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
public class MerCategory {

    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;
    private Integer categoryId;
    private String categoryName;
    private Integer parentId;
    private Integer level;

}
