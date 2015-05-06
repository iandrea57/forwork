package com.mmz.mybatis.dao;

import com.mmz.mybatis.annotation.DataSourceLbs;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-5-6 下午3:04
 */
@DataSourceLbs
public interface AgentDAO {

    final String TABLE = "agent";

    @Select("select name from " + TABLE+ " limit 10")
    public List<String> getAllNames();

}
