package com.jhzy.nursinghandover.beans;

import java.util.List;

/**
 * Created by Administrator on 2017/2/26 0026.
 */

public class BindBedBean {

    /**
     * code : A00000
     * data : [{"OrganizationID":15,"ZoneID":1,"ZoneValue":"ROOT","ID":62,"RoomName":"A001","ZoneName":"养老院","ZoneTitle":"养老院","BedName":"001","BedTitle":"001","IMEIs":null},{"OrganizationID":15,"ZoneID":1,"ZoneValue":"ROOT","ID":63,"RoomName":"A001","ZoneName":"养老院","ZoneTitle":"养老院","BedName":"002","BedTitle":"002","IMEIs":null}]
     * msg : null
     */
    private String code;
    private Object msg;
    /**
     * OrganizationID : 15
     * ZoneID : 1
     * ZoneValue : ROOT
     * ID : 62
     * RoomName : A001
     * ZoneName : 养老院
     * ZoneTitle : 养老院
     * BedName : 001
     * BedTitle : 001
     * IMEIs : null
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
        private int OrganizationID;
        private int ZoneID;
        private String ZoneValue;
        private int ID;
        private String RoomName;
        private String ZoneName;
        private String ZoneTitle;
        private String BedName;
        private String BedTitle;
        private Object IMEIs;
        private boolean isChecked;

        @Override
        public String toString() {
            return "DataBean{" +
                    "OrganizationID=" + OrganizationID +
                    ", ZoneID=" + ZoneID +
                    ", ZoneValue='" + ZoneValue + '\'' +
                    ", ID=" + ID +
                    ", RoomName='" + RoomName + '\'' +
                    ", ZoneName='" + ZoneName + '\'' +
                    ", ZoneTitle='" + ZoneTitle + '\'' +
                    ", BedName='" + BedName + '\'' +
                    ", BedTitle='" + BedTitle + '\'' +
                    ", IMEIs=" + IMEIs +
                    ", isChecked=" + isChecked +
                    '}';
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

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

        public String getBedName() {
            return BedName;
        }

        public void setBedName(String BedName) {
            this.BedName = BedName;
        }

        public String getBedTitle() {
            return BedTitle;
        }

        public void setBedTitle(String BedTitle) {
            this.BedTitle = BedTitle;
        }

        public Object getIMEIs() {
            return IMEIs;
        }

        public void setIMEIs(Object IMEIs) {
            this.IMEIs = IMEIs;
        }
    }
}
