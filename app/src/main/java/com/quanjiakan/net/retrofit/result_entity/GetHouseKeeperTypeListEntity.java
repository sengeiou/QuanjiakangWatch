package com.quanjiakan.net.retrofit.result_entity;

import java.util.List;

/**
 * Created by Administrator on 2017/10/31.
 */

public class GetHouseKeeperTypeListEntity {

    /**
     * code : 200
     * list : [{"createtime":1479364796,"id":1,"name":"保姆","status":0},{"createtime":1479364796,"id":2,"name":"保洁","status":0},{"createtime":1479364796,"id":3,"name":"月嫂","status":0},{"createtime":1479364796,"id":4,"name":"育婴师","status":0},{"createtime":1479364796,"id":5,"name":"催乳师","status":0},{"createtime":1479364796,"id":6,"name":"早教师","status":0},{"createtime":1479364796,"id":7,"name":"老人陪护","status":0},{"createtime":1479364796,"id":8,"name":"钟点","status":0},{"createtime":1479364796,"id":9,"name":"小儿推拿","status":0}]
     * message : 返回成功
     * rows : 9
     */

    private String code;
    private String message;
    private int rows;
    private List<ListBean> list;

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

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * createtime : 1479364796
         * id : 1
         * name : 保姆
         * status : 0
         */

        private int createtime;
        private int id;
        private String name;
        private int status;

        public int getCreatetime() {
            return createtime;
        }

        public void setCreatetime(int createtime) {
            this.createtime = createtime;
        }

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

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
