package com.mmz.mybatis.dao;

import com.mmz.mybatis.model.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by dra on 15-5-5.
 */
public interface UserDAO {

//    @Select("select * from User")
//    public List<User> getAll();
//
//    @Select("select * from User where id = #{id}")
//    public User get(int id);

    @Select("select name from User")
    public List<String> getAllNames();
}
