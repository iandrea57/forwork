package com.mmz.mybatis.dao;

import com.mmz.mybatis.annotation.DataSourceDefault;
import com.mmz.mybatis.model.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import xce.userapicache.TABLEBORN;

import java.util.List;

/**
 * Created by dra on 15-5-5.
 */
@DataSourceDefault
public interface UserDAO {

    final String TABLE = "users";

    @Select("select * from " + TABLE + " limit 10")
    public List<User> getAll();

    @Insert({"insert into " + TABLE + " set last_name = #{last_name} "})
    @Options(useGeneratedKeys = true)
    public int insert(User user);

    @Delete("delete from " + TABLE + " where last_name = #{last_name} ")
    public void delete(User user);

    @Select("select count(1) from " + TABLE + " where last_name = #{last_name} ")
    public int getCouont(User user);

}
