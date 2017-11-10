package com.quanjiakan.net.retrofit.result_entity;

/**
 * Created by Administrator on 2017/11/9.
 */

public class GetHouseKeeperDetailEntity {

    /**
     * code : 200
     * message : 返回成功
     * object : {"address":"广州市天河区龙口西196号伊顿商务中心3楼   ","age":46,"companyId":7,"companyName":"广州家事无忧家庭服务股份有限公司   ","createtime":"2017-05-10 13:59:47.0","creator":1,"education":1,"experience":"6","fromRegion":"湖南,null,null","gender":"f","height":160,"housekeeperType":1,"id":38,"image":"http://picture.quanjiakan.com:9080/quanjiakan/resources/housekeeper/20161109200616_ri4lld.png","language":"普通话","level":1,"marriage":1,"mobile":"020-81773777","model":1,"name":"刘霞","nation":"汉族","price":"3500-4000","serviceItem":"室内桌面,台面,厨房,卫生间,整理物品,清除垃圾\r\n","serviceRegion":"广东,广州,荔湾区","shengxiao":"狗","star":1,"status":1}
     */

    private String code;
    private String message;
    private ObjectBean object;

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

    public ObjectBean getObject() {
        return object;
    }

    public void setObject(ObjectBean object) {
        this.object = object;
    }

    public static class ObjectBean {
        /**
         * address : 广州市天河区龙口西196号伊顿商务中心3楼
         * age : 46
         * companyId : 7
         * companyName : 广州家事无忧家庭服务股份有限公司
         * createtime : 2017-05-10 13:59:47.0
         * creator : 1
         * education : 1
         * experience : 6
         * fromRegion : 湖南,null,null
         * gender : f
         * height : 160
         * housekeeperType : 1
         * id : 38
         * image : http://picture.quanjiakan.com:9080/quanjiakan/resources/housekeeper/20161109200616_ri4lld.png
         * language : 普通话
         * level : 1
         * marriage : 1
         * mobile : 020-81773777
         * model : 1
         * name : 刘霞
         * nation : 汉族
         * price : 3500-4000
         * serviceItem : 室内桌面,台面,厨房,卫生间,整理物品,清除垃圾

         * serviceRegion : 广东,广州,荔湾区
         * shengxiao : 狗
         * star : 1
         * status : 1
         */

        private String address;
        private int age;
        private int companyId;
        private String companyName;
        private String createtime;
        private int creator;
        private int education;
        private String experience;
        private String fromRegion;
        private String gender;
        private int height;
        private int housekeeperType;
        private int id;
        private String image;
        private String language;
        private int level;
        private int marriage;
        private String mobile;
        private int model;
        private String name;
        private String nation;
        private String price;
        private String serviceItem;
        private String serviceRegion;
        private String shengxiao;
        private int star;
        private int status;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public int getCompanyId() {
            return companyId;
        }

        public void setCompanyId(int companyId) {
            this.companyId = companyId;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public int getCreator() {
            return creator;
        }

        public void setCreator(int creator) {
            this.creator = creator;
        }

        public int getEducation() {
            return education;
        }

        public void setEducation(int education) {
            this.education = education;
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

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getHousekeeperType() {
            return housekeeperType;
        }

        public void setHousekeeperType(int housekeeperType) {
            this.housekeeperType = housekeeperType;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public int getMarriage() {
            return marriage;
        }

        public void setMarriage(int marriage) {
            this.marriage = marriage;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public int getModel() {
            return model;
        }

        public void setModel(int model) {
            this.model = model;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNation() {
            return nation;
        }

        public void setNation(String nation) {
            this.nation = nation;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getServiceItem() {
            return serviceItem;
        }

        public void setServiceItem(String serviceItem) {
            this.serviceItem = serviceItem;
        }

        public String getServiceRegion() {
            return serviceRegion;
        }

        public void setServiceRegion(String serviceRegion) {
            this.serviceRegion = serviceRegion;
        }

        public String getShengxiao() {
            return shengxiao;
        }

        public void setShengxiao(String shengxiao) {
            this.shengxiao = shengxiao;
        }

        public int getStar() {
            return star;
        }

        public void setStar(int star) {
            this.star = star;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
