package com.codezl.easy_sql.mybatis.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.codezl.easy_sql.pojo.entiy.ColumnDict;
import com.codezl.easy_sql.service.impl.BaseSqlExecServiceImpl;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: code-zl
 * @Date: 2023/06/09/9:46
 * @Description:
 */
@Mapper
public interface CommonMapper {

    /**
     * @Description: 查询字典字段值
     * @Param:
     * @return:
     * @Author: code-zl
     * @Date: 2023/6/9
     */
    @Select("SELECT * FROM kc_column_dict WHERE name=#{name} and exist=1 ORDER BY num")
    List<ColumnDict> findByName(String name);

    @Select("SELECT * FROM kc_column_dict WHERE name=#{name} and exist=1 ORDER BY num desc limit 1")
    ColumnDict findLast(String name);

    @Select("SELECT\n" +
            " c.relname tbName,\n" +
            " A.attname AS tbColumn,\n" +
            " split_part(col_description ( A.attrelid, A.attnum ),'：',1) AS descr,\n" +
            " format_type ( A.atttypid, A.atttypmod ) AS type,\n" +
            " CASE WHEN A.attnotnull='f' THEN 0 ELSE 1 END AS require,\n" +
            " a.attnum index\n" +
            "FROM\n" +
            " pg_class AS c,\n" +
            " pg_attribute AS a\n" +
            "WHERE\n" +
            " c.relnamespace=2200\n" +
            " AND c.relname not like 'v_%'\n" +
            " AND c.relname not like 'pk_%'\n" +
            " AND c.relname not like 'unidx%'\n" +
            " AND c.relname not like '%_index'\n" +
            " AND c.relname not like '%_seq'\n" +
            " AND c.relname not like '%_pkey'\n" +
            " AND A.attrelid = C.oid \n" +
            " AND A.attnum > 0\n" +
            " AND c.relname = #{tbName}" +
            " AND A.attname != 'id'" +
            " AND a.attname not like '%pg.dropped%'\n" +
            " ORDER BY c.relname,a.attnum;")
    List<BaseSqlExecServiceImpl.Tb> getTableColumn(String tbName);

    @Select("SELECT\n" +
            " c.relname tbName,\n" +
            " A.attname AS tbColumn,\n" +
            " split_part(col_description ( A.attrelid, A.attnum ),'：',1) AS descr,\n" +
            " format_type ( A.atttypid, A.atttypmod ) AS type,\n" +
            " CASE WHEN A.attnotnull='f' THEN 0 ELSE 1 END AS require,\n" +
            " a.attnum index\n" +
            "FROM\n" +
            " pg_class AS c,\n" +
            " pg_attribute AS a\n" +
            "WHERE\n" +
            " c.relnamespace=2200\n" +
            " AND c.relname not like 'v_%'\n" +
            " AND c.relname not like 'pk_%'\n" +
            " AND c.relname not like 'unidx%'\n" +
            " AND c.relname not like '%_index'\n" +
            " AND c.relname not like '%_seq'\n" +
            " AND c.relname not like '%_pkey'\n" +
            " AND A.attrelid = C.oid \n" +
            " AND A.attnum > 0\n" +
            " AND A.attname = 'id'" +
            " AND a.attname not like '%pg.dropped%'\n" +
            " ORDER BY c.relname,a.attnum;")
    List<BaseSqlExecServiceImpl.TbN> getTableColumn2();

    @Insert("${sqlStr}")
    void initData(String sqlStr);
}
