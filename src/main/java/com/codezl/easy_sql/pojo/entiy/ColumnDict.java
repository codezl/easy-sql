package com.codezl.easy_sql.pojo.entiy;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("kc_column_dict")
@Data
public class ColumnDict {

    private Integer id;
    private Integer exist;
    private String name;
    private Integer num;
    private String description;
    private String icon;
}