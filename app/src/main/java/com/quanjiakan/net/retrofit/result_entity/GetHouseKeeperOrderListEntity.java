package com.quanjiakan.net.retrofit.result_entity;

import com.quanjiakan.entity.HouseKeeperOrderDetailEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/7.
 */

public class GetHouseKeeperOrderListEntity {

    /**
     * code : 200
     * list : [{"address":"复活币","age":46,"begindate":"2017-11-10","companyName":"广州家事无忧家庭服务股份有限公司   ","createtime":"2017-11-10 17:39:36","enddate":"2017-11-10","experience":6,"fromRegion":"湖南,null,null","hkMobile":"020-81773777","housekeeperId":38,"housekeeperName":"刘霞","id":111011,"image":"http://picture.quanjiakan.com/quanjiakan/resources/housekeeper/20161109200616_ri4lld.png","mobile":"13212345678","orderUserName":"发古板","orderid":"QJKKEEPER20171110053936135398","price":"3500-4000","serviceItem":"室内桌面,台面,厨房,卫生间,整理物品,清除垃圾\r\n","status":1},{"address":"巨贵","age":47,"begindate":"2017-11-13","companyName":"广州家事无忧家庭服务股份有限公司   ","createtime":"2017-11-13 10:55:35","enddate":"2017-11-13","experience":5,"fromRegion":"湖南,长沙,芙蓉区","hkMobile":"020-81773777","housekeeperId":91,"housekeeperName":"刘桂兰","id":111012,"image":"http://picture.quanjiakan.com/quanjiakan/resources/housekeeper/20161109200637_jmuz0k.jpg","mobile":"18011935659","orderUserName":"复活币","orderid":"QJKKEEPER20171113105535783018","price":"3500-4000","serviceItem":"室内桌面,台面,厨房,卫生间,整理物品,清除垃圾","status":1}]
     * message : 返回成功
     * rows : 10
     * total : 52
     */

    private String code;
    private String message;
    private int rows;
    private int total;
    private List<HouseKeeperOrderDetailEntity> list;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<HouseKeeperOrderDetailEntity> getList() {
        return list;
    }

    public void setList(List<HouseKeeperOrderDetailEntity> list) {
        this.list = list;
    }

}
