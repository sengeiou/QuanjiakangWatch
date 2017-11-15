package com.quanjiakan.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/11/15.
 */

public class HouseKeeperOrderDetailEntity implements Serializable{

    /**
     * housekeeperId : 91
     * companyName : 广州家事无忧家庭服务股份有限公司
     * housekeeperName : 刘桂兰
     * image : http://picture.quanjiakan.com/quanjiakan/resources/housekeeper/20161109200637_jmuz0k.jpg
     * price : 3500-4000
     * age : 47
     * serviceItem : 室内桌面,台面,厨房,卫生间,整理物品,清除垃圾
     * experience : 5
     * fromRegion : 湖南,长沙,芙蓉区
     * evaluation : null
     * begindate : 2017-11-15
     * enddate : 2017-11-15
     * orderUserName : 固话
     * mobile : 13212345678
     * address : 湖北
     * id : {"orderid":"QJKKEEPER20171115095332624401","paidinfo":{"partner":"2088021868486397","seller_id":"13510237554@163.com","out_trade_no":"QJKKEEPER20171115095332624401","subject":"全家康支付","body":"支付家政人员服务费用","total_fee":"200.0","notify_url":"http://pay.quanjiakan.com:7080/familycore-pay/notify_alipay.jsp","service":"mobile.securitypay.pay","payment_type":"1","_input_charset":"utf-8","it_b_pay":"30m","return_url":"m.alipay.com","sign":"aIpIfYZigxNS8/qBkvhTbghXfNf/9Hw8csjf7XSUgWdqnrYDPRUgvoMBtg1AzAJgavnhj8drQL+0P6mz0rwQTP/6dcGSQHu2lqaRVY9UdPQO0EQv1IVFeXsdfbY9Hs6XRLlpAEBb7EPunL68ffVLIUxlQSIFVV16LdMB/0JNfz4=","code":"200"}}
     * orderid : QJKKEEPER20171115095332624401
     * createtime : 2017-11-15 09:53:36
     * status : 1
     */

    private String housekeeperId;
    private String companyName;
    private String housekeeperName;
    private String image;
    private String price;
    private String age;
    private String serviceItem;
    private String experience;
    private String fromRegion;
    private Object evaluation;
    private String begindate;
    private String enddate;
    private String orderUserName;
    private String mobile;
    private String address;
    private String id;
    private String orderid;
    private String createtime;
    private String status;

    public String getHousekeeperId() {
        return housekeeperId;
    }

    public void setHousekeeperId(String housekeeperId) {
        this.housekeeperId = housekeeperId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getHousekeeperName() {
        return housekeeperName;
    }

    public void setHousekeeperName(String housekeeperName) {
        this.housekeeperName = housekeeperName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getServiceItem() {
        return serviceItem;
    }

    public void setServiceItem(String serviceItem) {
        this.serviceItem = serviceItem;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getFromRegion() {
        return fromRegion;
    }

    public void setFromRegion(String fromRegion) {
        this.fromRegion = fromRegion;
    }

    public Object getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Object evaluation) {
        this.evaluation = evaluation;
    }

    public String getBegindate() {
        return begindate;
    }

    public void setBegindate(String begindate) {
        this.begindate = begindate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getOrderUserName() {
        return orderUserName;
    }

    public void setOrderUserName(String orderUserName) {
        this.orderUserName = orderUserName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
