package com.jhzy.nursinghandover.model.lock;

import java.util.List;

/**
 * Created by Administrator on 2017/2/27.
 */

public class Lock {

    private String staffs;
    /**
     * ElderID : 3
     * ElderName : 吕钲
     * PhotoUrl : https://zhihuizunyang.oss-cn-shanghai.aliyuncs.com/Org/1/Elder/20170314162943603.jpg
     * BedTitle : A012
     * StaffName : null
     */

    private List<Elders> elders;

    public String getStaffs() {
        return staffs;
    }

    public void setStaffs(String staffs) {
        this.staffs = staffs;
    }

    public List<Elders> getElders() {
        return elders;
    }

    public void setElders(List<Elders> elders) {
        this.elders = elders;
    }


}

