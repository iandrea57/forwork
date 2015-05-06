/**
 * $Id$
 * Copyright 2011-2013 Oak Pacific Interactive. All rights reserved.
 */
package com.mmz.mybatis.model;

import java.util.Date;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-5-6 下午3:02
 */
public class Agent {

    private int id;

    private String name;

    private int level;

    private String phone;

    private String renrenId;

    private int renrenUid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRenrenId() {
        return renrenId;
    }

    public void setRenrenId(String renrenId) {
        this.renrenId = renrenId;
    }

    public int getRenrenUid() {
        return renrenUid;
    }

    public void setRenrenUid(int renrenUid) {
        this.renrenUid = renrenUid;
    }
}
