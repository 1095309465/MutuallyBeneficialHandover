package com.jhzy.nursinghandover.beans;

import java.util.List;

/**
 * Created by sxmd on 2017/3/7.
 */

public class BedBindBean {
    /**
     * code : A00000
     * data : [{"OrganizationID":15,"ZoneID":4,"ZoneValue":"区域值","ID":19,"RoomName":"A001","RoomTitle":"A001","RoomValue":"房间值","ZoneName":"A","ZoneTitle":"B区","IMEIs":[]},{"OrganizationID":15,"ZoneID":5,"ZoneValue":"区域值","ID":22,"RoomName":"A002","RoomTitle":"A002","RoomValue":"房间值","ZoneName":"A","ZoneTitle":"c区","IMEIs":[]}]
     * msg : null
     */



    private String code;
    private Object msg;

    @Override
    public String toString() {
        return "BedBindBean{" +
                "code='" + code + '\'' +
                ", msg=" + msg +
                ", data=" + data +
                '}';
    }

    /**
     * OrganizationID : 15
     * ZoneID : 4
     * ZoneValue : 区域值
     * ID : 19
     * RoomName : A001
     * RoomTitle : A001
     * RoomValue : 房间值
     * ZoneName : A
     * ZoneTitle : B区
     * IMEIs : []
     */

    private List<DataBean> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {

        @Override
        public String toString() {
            return "DataBean{" +
                    "OrganizationID=" + OrganizationID +
                    ", ZoneID=" + ZoneID +
                    ", ZoneValue='" + ZoneValue + '\'' +
                    ", ID=" + ID +
                    ", RoomName='" + RoomName + '\'' +
                    ", RoomTitle='" + RoomTitle + '\'' +
                    ", RoomValue='" + RoomValue + '\'' +
                    ", ZoneName='" + ZoneName + '\'' +
                    ", ZoneTitle='" + ZoneTitle + '\'' +
                    ", IMEIs=" + IMEIs +
                    '}';
        }

        private int OrganizationID;
        private int ZoneID;
        private String ZoneValue;
        private int ID;
        private String RoomName;
        private String RoomTitle;
        private String RoomValue;
        private String ZoneName;
        private String ZoneTitle;
        private List<?> IMEIs;

        public int getOrganizationID() {
            return OrganizationID;
        }

        public void setOrganizationID(int OrganizationID) {
            this.OrganizationID = OrganizationID;
        }

        public int getZoneID() {
            return ZoneID;
        }

        public void setZoneID(int ZoneID) {
            this.ZoneID = ZoneID;
        }

        public String getZoneValue() {
            return ZoneValue;
        }

        public void setZoneValue(String ZoneValue) {
            this.ZoneValue = ZoneValue;
        }

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getRoomName() {
            return RoomName;
        }

        public void setRoomName(String RoomName) {
            this.RoomName = RoomName;
        }

        public String getRoomTitle() {
            return RoomTitle;
        }

        public void setRoomTitle(String RoomTitle) {
            this.RoomTitle = RoomTitle;
        }

        public String getRoomValue() {
            return RoomValue;
        }

        public void setRoomValue(String RoomValue) {
            this.RoomValue = RoomValue;
        }

        public String getZoneName() {
            return ZoneName;
        }

        public void setZoneName(String ZoneName) {
            this.ZoneName = ZoneName;
        }

        public String getZoneTitle() {
            return ZoneTitle;
        }

        public void setZoneTitle(String ZoneTitle) {
            this.ZoneTitle = ZoneTitle;
        }

        public List<?> getIMEIs() {
            return IMEIs;
        }

        public void setIMEIs(List<?> IMEIs) {
            this.IMEIs = IMEIs;
        }
    }
}
