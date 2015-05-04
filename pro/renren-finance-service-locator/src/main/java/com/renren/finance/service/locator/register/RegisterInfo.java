/**
 * $Id$
 * Copyright 2011-2013 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.finance.service.locator.register;

import com.renren.finance.service.locator.curator.ServiceNode;
import com.renren.finance.service.locator.curator.Node;
import com.renren.finance.service.locator.util.IpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-4-28 下午5:45
 */
public class RegisterInfo {

    private Node node;

    private String interfaceName;

    private List<String> alarmPhones;

    private List<String> alarmEmails;

    private RegisterInfo() {}

    public static class Builder {

        private String host;
        private int port;

        private List<String> alarmPhones = new ArrayList<String>();
        private List<String> alarmEmails = new ArrayList<String>();

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public Builder alarmPhones(String alarmPhone) {
            if (alarmPhones != null)
                this.alarmPhones.add(alarmPhone);
            return this;
        }

        public Builder alarmEmails(String alarmEmail) {
            if (alarmEmails != null)
                this.alarmEmails.add(alarmEmail);
            return this;
        }

        public Builder alarmPhones(List<String> alarmPhones) {
            if (alarmPhones != null)
                this.alarmPhones.addAll(alarmPhones);
            return this;
        }

        public Builder alarmEmails(List<String> alarmEmails) {
            if (alarmEmails != null)
                this.alarmEmails.addAll(alarmEmails);
            return this;
        }

        public RegisterInfo build() {
            if (host == null || "".equals(host.trim()))
                host = IpUtils.getOneLocalIp();
            ServiceNode node = new ServiceNode();
            node.setHost(host);
            node.setPort(port);

            RegisterInfo info = new RegisterInfo();
            info.setNode(node);
            info.setAlarmPhones(alarmPhones);
            info.setAlarmEmails(alarmEmails);
            return info;
        }
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public List<String> getAlarmPhones() {
        return alarmPhones;
    }

    public void setAlarmPhones(List<String> alarmPhones) {
        this.alarmPhones = alarmPhones;
    }

    public List<String> getAlarmEmails() {
        return alarmEmails;
    }

    public void setAlarmEmails(List<String> alarmEmails) {
        this.alarmEmails = alarmEmails;
    }




}
