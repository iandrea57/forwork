/**
 * $Id$
 * Copyright 2011-2013 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.finance.service.locator.conf;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-4-30 下午5:39
 */
public class InvalidLocatorConfException extends Exception {

    private static final long serialVersionUID = -3402536374224109460L;

    public InvalidLocatorConfException() {
        super();
    }

    public InvalidLocatorConfException(String message) {
        super(message);
    }

    public InvalidLocatorConfException(String message, Throwable cause) {
        super(message, cause);
    }

}
