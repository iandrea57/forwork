/**
 * $Id$
 * Copyright 2011-2013 Oak Pacific Interactive. All rights reserved.
 */
package com.mmz.frameworkuse.thrift;

import com.mmz.xt.service.api.GetInfoRequest;
import com.mmz.xt.service.api.GetInfoResponse;
import com.mmz.xt.service.api.model.Info;
import com.mmz.xt.service.api.model.Type;
import org.apache.thrift.TException;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-4-10 下午12:06
 */
public class InfoServiceImpl implements IInfoService {

    @Override
    public GetInfoResponse getInfo(GetInfoRequest request) throws TException {
        GetInfoResponse response = new GetInfoResponse();
        Info info = new Info();
        info.setId(1111);
        info.setSeq(198329323233223L);
        info.setName("王其");
        info.setMoney(133.3);
        info.setType(Type.SUCCESS);
        response.setInfo(info);
        return response;
    }
}
