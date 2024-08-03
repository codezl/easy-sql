package com.codezl.easy_sql.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.codezl.easy_sql.mybatis.mapper.CommonMapper;
import com.codezl.easy_sql.pojo.entiy.ColumnDict;
import com.codezl.easy_sql.service.BaseSqlExecService;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.List;

public class BaseSqlExecServiceImpl implements BaseSqlExecService {

    @Resource
    CommonMapper commonMapper;

    @PostMapping("addDict")
    @ApiOperation("添加字典信息(开发人员使用)")
    public synchronized String level(@RequestBody Dict dto) {
        ColumnDict columnDict = commonMapper.findLast(dto.getName());
        if (columnDict != null) {
            int newNum = columnDict.getNum() + 1;
            ColumnDict newOne = new ColumnDict();
            newOne.setName(dto.getName());
            newOne.setDescription(dto.getDescription());
            newOne.setNum(newNum);
            newOne.setExist(1);
            commonMapper.initData("INSERT INTO \"kc_column_dict\"(\"exist\", \"name\", \"num\", \"description\") VALUES (1, '" + dto.getName() + "', " + newOne.getNum() + ", '" + newOne.getDescription() + "')");
        }
        return "";
    }

    @PostMapping("addDict2")
    public String add2(@RequestBody Dict dto) {
        String description = dto.getDescription();
        String[] split = description.split("，");
        if (split.length > 99) {
            return "超过100";
        }
        Dict one = new Dict();
        one.setName(dto.getName());
        for (String s : split) {
            one.setDescription(s);
            level(one);
        }
        return "success";
    }

    @PostMapping("initData")
    @ApiOperation("初始化数据（开发人员使用）")
    public void initData(@RequestBody DataInit dto) {
        List<Tb> list = commonMapper.getTableColumn(dto.getTbName());

        if (list.size() > 0) {
            String sqlStr = createSqlStr(dto, list);
            for (int i = 0; i < dto.getCount(); i++) {
                commonMapper.initData(sqlStr);
            }
        }
    }

    @PostMapping("initData2")
    @ApiOperation("所有可初始化数据表名（开发人员使用）")
    public List<TbN> initData2() {
        List<TbN> list = commonMapper.getTableColumn2();
        return list;
    }

    public String createSqlStr(DataInit dto, List<Tb> list) {
        String sqlStr = "insert into " + dto.getTbName();
        StringBuilder column = new StringBuilder("(");
        StringBuilder values = new StringBuilder(" VALUES(");
        for (Tb item : list) {
            column.append(item.getTbColumn()).append(",");
            if (dto.getKv().containsKey(item.getTbColumn())) {
                Object o = dto.getKv().get(item.getTbColumn());
                values.append("'").append(o.toString()).append("'").append(",");
            } else if ("exist".equals(item.getTbColumn())) {
                values.append(1).append(",");
            } else if (!item.getTbColumn().contains("update") && item.getType().contains("time")) {
                values.append("now()").append(",");
            } else if (item.getTbName().contains("email")) {
                values.append("codezl@code.com").append(",");
            } else if ("phone".contains(item.getTbName())) {
                values.append("13131313131").append(",");
            } else if (item.getTbName().contains("img")) {
                values.append("http://120.238.111.190:25900/kchz/kchz-f90184eb-f71c-4157-b3ca-e7df73192a9a.jpg").append(",");
            } else if (item.getTbName().contains("type")) {
                values.append(dto.getNum() != null ? dto.getNum() : 1).append(",");
            } else if (item.getType().contains("int")) {
                values.append(1).append(",");
            } else if (item.getType().contains("char")) {
                values.append("'").append(item.getDescr()).append("'").append(",");
            } else {
                values.append("null").append(",");
            }
        }
        column.deleteCharAt(column.lastIndexOf(","));
        values.deleteCharAt(values.lastIndexOf(","));
        column.append(")");
        values.append(")");
        sqlStr += column + " " + values;
        return sqlStr;
    }

    @Data
    public static class Dict {
        private String name;
        private String description;
    }


    @Data
    public static class DataInit {
        @ApiModelProperty(required = true, value = "表名（根据'kc_' + 实体名反驼峰命名获得,如：supplyInfo => kc_supply_info）")
        private String tbName;
        @ApiModelProperty(required = true, value = "添加数据条数")
        private Integer count = 1;
        @ApiModelProperty("是否需指定type字典值num（有些分类型的数据可到数据库修改）")
        private Integer num;
        @ApiModelProperty("指定某个字段值{\"name\":\"这是指定名字\"}")
        private JSONObject kv;
    }


    @Data
    public static class Tb {
        @ApiModelProperty("修改的表名字")
        private String tbName;
        @ApiModelProperty("自定义修改字段名字")
        private String tbColumn;
        @ApiModelProperty("字段含义")
        private String descr;
        @ApiModelProperty("字段数据类型")
        private String type;
        @ApiModelProperty("是否必填字段")
        private Integer require;
        private Integer index;
    }

    @Data
    public static class TbN {
        @ApiModelProperty("修改的表名字")
        private String tbName;
        @ApiModelProperty("表解释-id")
        private String descr;
    }
}
