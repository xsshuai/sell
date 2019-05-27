package com.imooc.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CategoryForm {

    /* 类目Id*/
    private Integer categoryId;

    @NotEmpty(message = "名称必填")
    /* 类目名称*/
    private String categoryName;

}
