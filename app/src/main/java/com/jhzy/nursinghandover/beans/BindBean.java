package com.jhzy.nursinghandover.beans;

/**
 * Created by Administrator on 2017/2/26 0026.
 */

public class BindBean {
    private String floor;//楼层
    private String roomName;//房间名
    private String regionCode;//区域编码
    private String roomCode;//房间编码
    private String iem;//设备号

    @Override
    public String toString() {
        return "BindBean{" +
                "floor='" + floor + '\'' +
                ", roomName='" + roomName + '\'' +
                ", regionCode='" + regionCode + '\'' +
                ", roomCode='" + roomCode + '\'' +
                ", iem='" + iem + '\'' +
                '}';
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getIem() {
        return iem;
    }

    public void setIem(String iem) {
        this.iem = iem;
    }
}
