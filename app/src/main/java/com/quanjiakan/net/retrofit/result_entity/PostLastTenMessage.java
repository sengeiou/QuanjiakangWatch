package com.quanjiakan.net.retrofit.result_entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/11/15.
 */

public class PostLastTenMessage implements Serializable{

    /**
     * 实际是获取到了全部的数据，需要自己进行排序与过滤
     *
     * code : 200
     * list : [{"chunyuId":440121340,"createMillisecond":1477882941480,"createtime":"2016-11-01 10:41:02.0","creator":10833,"doctorId":"clinic_web_cdea79ab4d2e3b53","id":105,"isreply":0,"patient":"{\"age\":\"22\",\"sex\":\"男\",\"Name\":\"李勇\",\"type\":\"patient_meta\"}","price":0,"status":1,"title":"最近牙齿突然好怕凉 以前没有啊 现在一碰到凉的东西就好酸 怎么办？"},{"chunyuId":440590162,"createMillisecond":1477967249702,"createtime":"2016-11-01 10:41:02.0","creator":10833,"doctorId":"clinic_web_1ca9a01cbd7be02f","id":106,"isreply":0,"patient":"{\"age\":\"24\",\"sex\":\"男\",\"Name\":\"春\",\"type\":\"patient_meta\"}","price":0,"status":1,"title":"头疼，呼吸难受"},{"chunyuId":440590505,"createMillisecond":1477967282475,"createtime":"2017-05-11 09:15:18.0","creator":10833,"doctorId":"clinic_web_2f2d5b7473fc7352","id":107,"isreply":0,"patient":"{\"age\":\"24\",\"sex\":\"男\",\"Name\":\"春\",\"type\":\"patient_meta\"}","price":0,"status":1,"title":"头疼，流鼻涕！"},{"chunyuId":440610210,"createMillisecond":1477969782480,"createtime":"2016-11-01 15:13:09.0","creator":10833,"doctorId":"clinic_web_7fe2265c83d03d48","id":108,"isreply":0,"patient":"{\"age\":\"33\",\"sex\":\"男\",\"Name\":\"jeck\",\"type\":\"patient_meta\"}","price":0,"status":1,"title":"头晕感冒咳嗽！"},{"chunyuId":449274136,"createMillisecond":1479523572297,"createtime":"2017-05-11 09:15:18.0","creator":10833,"doctorId":"clinic_web_66a6f3e2cce1bd65","id":140,"isreply":0,"patient":"{\"age\":\"23\",\"sex\":\"男\",\"Name\":\"王勇\",\"type\":\"patient_meta\"}","price":0,"status":1,"title":"早上肚子就疼得很厉害，怎么办啊？有点拉肚子"},{"chunyuId":449274491,"createMillisecond":1479523609577,"createtime":"2017-05-11 09:15:18.0","creator":10833,"doctorId":"clinic_web_736d181df95aa4aa","id":141,"isreply":0,"patient":"{\"age\":\"23\",\"sex\":\"男\",\"Name\":\"眼镜\",\"type\":\"patient_meta\"}","price":0,"status":1,"title":"感觉身体被掏空了，怎么办，整天在办公室里面坐着"},{"chunyuId":526818882,"createMillisecond":1494407063451,"createtime":"2017-05-11 09:15:18.0","creator":10833,"doctorId":"clinic_web_43919e71a4a0393a","id":342,"isreply":0,"patient":"{\"age\":\"26\",\"sex\":\"男\",\"Name\":\"李牌\",\"type\":\"patient_meta\"}","price":0,"status":1,"title":"您好 每天早上眼睛就特别酸 还会有泪 怎么回事啊？"},{"chunyuId":527562050,"createMillisecond":1494570772573,"createtime":"2017-10-18 14:00:35.0","creator":10833,"doctorId":"clinic_web_fa57946e0cb1b82f","id":349,"isreply":0,"patient":"{\"age\":\"22\",\"sex\":\"女\",\"Name\":\"小孙\",\"type\":\"patient_meta\"}","price":0,"status":1,"title":"经常哟咳嗽！件怎么办！吃点啥药好！"},{"chunyuId":529461833,"createMillisecond":1494984076960,"createtime":"2017-10-18 14:00:35.0","creator":10833,"doctorId":"clinic_web_684f7198b5548ca6","id":358,"isreply":0,"patient":"{\"age\":\"25\",\"sex\":\"男\",\"Name\":\"王勇\",\"type\":\"patient_meta\"}","price":0,"status":1,"title":"最近总是全身无力 怎么回事啊"},{"chunyuId":589455709,"createMillisecond":1508292807032,"createtime":"2017-10-20 09:08:56.0","creator":10833,"doctorId":"clinic_web_a1373118acb2d70f","id":405,"isreply":0,"patient":"{\"type\":\"patient_meta\",\"age\":\"28\",\"sex\":\"男\",\"name\":\"罗鹏\"}","price":0,"status":1,"title":"最近喉咙感觉里面有东西,经常引起我想吐,请问该如何处理?"},{"chunyuId":589571475,"createMillisecond":1508312996781,"createtime":"2017-10-18 16:25:39.0","creator":10833,"doctorId":"clinic_web_f721d46225a8442b","id":406,"isreply":0,"patient":"{\"age\":\"26\",\"sex\":\"男\",\"Name\":\"李先生\",\"type\":\"patient_meta\"}","price":0,"status":1,"title":"眼睛不舒服 最近早上起来眼睛总是很酸"},{"chunyuId":595160846,"createMillisecond":1509422251129,"createtime":"2017-11-08 10:18:13.0","creator":10833,"doctorId":"clinic_web_0b208474089ec907","id":415,"isreply":0,"patient":"{\"age\":\"45\",\"sex\":\"男\",\"Name\":\"杨勇\",\"type\":\"patient_meta\"}","price":0,"status":1,"title":"最近腰很酸 浑身没力气"},{"chunyuId":595161093,"createMillisecond":1509422291584,"createtime":"2017-11-08 10:18:13.0","creator":10833,"doctorId":"clinic_web_65e4a3e1f743d797","id":416,"isreply":0,"patient":"{\"age\":\"45\",\"sex\":\"男\",\"Name\":\"杨勇\",\"type\":\"patient_meta\"}","price":0,"status":1,"title":"夜里多梦 有点困了也睡不着 唉"},{"chunyuId":596597433,"createMillisecond":1509689838712,"createtime":"2017-11-08 10:18:13.0","creator":10833,"doctorId":"clinic_web_f598c70e48b6ff56","id":419,"isreply":0,"patient":"{\"age\":\"23\",\"sex\":\"男\",\"Name\":\"王勇\",\"type\":\"patient_meta\"}","price":0,"status":1,"title":"最近肚子老疼 也没怎么吃凉的"},{"chunyuId":596608492,"createMillisecond":1509691537241,"createtime":"2017-11-03 14:45:37.0","creator":10833,"doctorId":"0","id":420,"isreply":0,"patient":"{\"age\":\"23\",\"sex\":\"男\",\"Name\":\"王勇\",\"type\":\"patient_meta\"}","price":0,"status":1,"title":"kkk阿里啦咯啦阿里郎"},{"chunyuId":598383550,"createMillisecond":1510035028532,"createtime":"2017-11-08 12:16:11.0","creator":10833,"doctorId":"clinic_web_a8c43093bbd629f4","id":427,"isreply":1,"patient":"{\"age\":\"36\",\"sex\":\"男\",\"Name\":\"王刚\",\"type\":\"patient_meta\"}","price":0,"status":1,"title":"您好 最近腿一受寒 就会疼 不受寒就没事 请问这种情况不去治疗会不会越来越严重"}]
     * message : 返回成功
     * rows : 16
     * total : 2
     */

    private String code;
    private String message;
    private int rows;
    private int total;
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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Serializable{
        /**
         * chunyuId : 440121340
         * createMillisecond : 1477882941480
         * createtime : 2016-11-01 10:41:02.0
         * creator : 10833
         * doctorId : clinic_web_cdea79ab4d2e3b53
         * id : 105
         * isreply : 0
         * patient : {"age":"22","sex":"男","Name":"李勇","type":"patient_meta"}
         * price : 0
         * status : 1
         * title : 最近牙齿突然好怕凉 以前没有啊 现在一碰到凉的东西就好酸 怎么办？
         */

        private int chunyuId;
        private long createMillisecond;
        private String createtime;
        private int creator;
        private String doctorId;
        private int id;
        private int isreply;//TODO 0 未回复 ；1为已回复
        private String patient;
        private int price;
        private int status;
        private String title;

        public int getChunyuId() {
            return chunyuId;
        }

        public void setChunyuId(int chunyuId) {
            this.chunyuId = chunyuId;
        }

        public long getCreateMillisecond() {
            return createMillisecond;
        }

        public void setCreateMillisecond(long createMillisecond) {
            this.createMillisecond = createMillisecond;
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

        public String getDoctorId() {
            return doctorId;
        }

        public void setDoctorId(String doctorId) {
            this.doctorId = doctorId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getIsreply() {
            return isreply;
        }

        public void setIsreply(int isreply) {
            this.isreply = isreply;
        }

        public String getPatient() {
            return patient;
        }

        public void setPatient(String patient) {
            this.patient = patient;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isReply(){
            return isreply==1;
        }
    }
}
