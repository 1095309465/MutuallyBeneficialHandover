package com.jhzy.nursinghandover.beans.nextItem;

import java.util.List;

/**
 * Created by nakisaRen
 * on 17/3/27.
 */

public class CommonItem {

    /**
     * code : A00000
     * data : [{"ID":42,"CareItemTitle":"换纸尿裤","CareItemUrl":"http://zhihuizunyang.oss-cn-shanghai.aliyuncs.com/NurseIcon/HLJJAPP/taskIcon/icon_hnb.jpg","Subproject":"[{\"Number\":\"1\",\"SubprojectIco\":\"http://zhihuizunyang.oss-cn-shanghai.aliyuncs.com/NurseIcon/HLJJAPP/taskIcon/icon_db.jpg\",\"SubprojectName\":\"大便\"},{\"Number\":\"2\",\"SubprojectIco\":\"http://zhihuizunyang.oss-cn-shanghai.aliyuncs.com/NurseIcon/HLJJAPP/subIcon/icon_xb.jpg\",\"SubprojectName\":\"小便\"}]"},{"ID":43,"CareItemTitle":"倒夜壶","CareItemUrl":"http://zhihuizunyang.oss-cn-shanghai.aliyuncs.com/NurseIcon/HLJJAPP/taskIcon/icon_dnh.jpg","Subproject":"[{\"Number\":\"1\",\"SubprojectIco\":\"http://zhihuizunyang.oss-cn-shanghai.aliyuncs.com/NurseIcon/HLJJAPP/taskIcon/icon_dnh.jpg\",\"SubprojectName\":\"倒夜壶\"}]"},{"ID":44,"CareItemTitle":"喂水","CareItemUrl":"http://zhihuizunyang.oss-cn-shanghai.aliyuncs.com/NurseIcon/HLJJAPP/taskIcon/icon_ws.jpg","Subproject":"[{\"Number\":\"1\",\"SubprojectIco\":\"http://zhihuizunyang.oss-cn-shanghai.aliyuncs.com/NurseIcon/HLJJAPP/taskIcon/icon_ws.jpg\",\"SubprojectIcoClk\":\"http://zhihuizunyang.oss-cn-shanghai.aliyuncs.com/NurseIcon/HLJJAPP/taskIcon/icon_ws.jpg\",\"SubprojectName\":\"喂水\"}]"},{"ID":45,"CareItemTitle":"翻身","CareItemUrl":"\r\nhttp://zhihuizunyang.oss-cn-shanghai.aliyuncs.com/NurseIcon/HLJJAPP/taskIcon/icon_fs.jpg","Subproject":"[{\"Number\":\"1\",\"SubprojectIco\":\"http://zhihuizunyang.oss-cn-shanghai.aliyuncs.com/NurseIcon/HLJJAPP/taskIcon/icon_zfs.jpg\",\"SubprojectName\":\"左翻身\"},{\"Number\":\"2\",\"SubprojectIco\":\"http://zhihuizunyang.oss-cn-shanghai.aliyuncs.com/NurseIcon/HLJJAPP/taskIcon/icon_pt.jpg\",\"SubprojectName\":\"平躺\"},{\"Number\":\"3\",\"SubprojectIco\":\"http://zhihuizunyang.oss-cn-shanghai.aliyuncs.com/NurseIcon/HLJJAPP/taskIcon/icon_yfs.jpg\",\"SubprojectName\":\"右翻身\"}]"},{"ID":46,"CareItemTitle":"个人清洁","CareItemUrl":"http://zhihuizunyang.oss-cn-shanghai.aliyuncs.com/NurseIcon/HLJJAPP/taskIcon/icon_grqj.jpg","Subproject":"[{\"Number\":\"1\",\"SubprojectIco\":\"http://zhihuizunyang.oss-cn-shanghai.aliyuncs.com/NurseIcon/HLJJAPP/taskIcon/icon_lf.jpg\",\"SubprojectName\":\"理发\"},{\"Number\":\"2\",\"SubprojectIco\":\"http://zhihuizunyang.oss-cn-shanghai.aliyuncs.com/NurseIcon/HLJJAPP/taskIcon/icon_jzj.jpg\",\"SubprojectName\":\"剪指甲\"},{\"Number\":\"3\",\"SubprojectIco\":\"http://zhihuizunyang.oss-cn-shanghai.aliyuncs.com/NurseIcon/HLJJAPP/taskIcon/icon_my.jpg\",\"SubprojectName\":\"沐浴\"},{\"Number\":\"4\",\"SubprojectIco\":\"http://zhihuizunyang.oss-cn-shanghai.aliyuncs.com/NurseIcon/HLJJAPP/taskIcon/icon_sy.jpg\",\"SubprojectName\":\"口腔\"}]"},{"ID":48,"CareItemTitle":"助卧床/起床","CareItemUrl":"http://zhihuizunyang.oss-cn-shanghai.aliyuncs.com/NurseIcon/HLJJAPP/taskIcon/icon_xzqwc.jpg","Subproject":"[{\"Number\":\"1\",\"SubprojectIco\":\"http://zhihuizunyang.oss-cn-shanghai.aliyuncs.com/NurseIcon/HLJJAPP/taskIcon/icon_xzqc.jpg\",\"SubprojectName\":\"起床\"},{\"Number\":\"2\",\"SubprojectIco\":\"http://zhihuizunyang.oss-cn-shanghai.aliyuncs.com/NurseIcon/HLJJAPP/taskIcon/icon_xzwc.jpg\",\"SubprojectName\":\"卧床\"}]"},{"ID":49,"CareItemTitle":"急救","CareItemUrl":"http://zhihuizunyang.oss-cn-shanghai.aliyuncs.com/NurseIcon/HLJJAPP/taskIcon/icon_jj.jpg","Subproject":"[{\"Number\":\"1\",\"SubprojectIco\":\"http://zhihuizunyang.oss-cn-shanghai.aliyuncs.com/NurseIcon/HLJJAPP/taskIcon/icon_xffs.jpg\",\"SubprojectName\":\"心肺复苏\"},{\"Number\":\"2\",\"SubprojectIco\":\"http://zhihuizunyang.oss-cn-shanghai.aliyuncs.com/NurseIcon/HLJJAPP/taskIcon/icon_ysjj.jpg\",\"SubprojectName\":\"噎食急救\"},{\"Number\":\"3\",\"SubprojectIco\":\"http://zhihuizunyang.oss-cn-shanghai.aliyuncs.com/NurseIcon/HLJJAPP/taskIcon/icon_xy.jpg\",\"SubprojectName\":\"吸氧\"},{\"Number\":\"4\",\"SubprojectIco\":\"http://zhihuizunyang.oss-cn-shanghai.aliyuncs.com/NurseIcon/HLJJAPP/taskIcon/icon_bs.jpg\",\"SubprojectName\":\"鼻饲\"}]"}]
     * msg : null
     */

    private String code;
    private Object msg;
    private List<CommonBean> data;


    public String getCode() { return code;}


    public void setCode(String code) { this.code = code;}


    public Object getMsg() { return msg;}


    public void setMsg(Object msg) { this.msg = msg;}


    public List<CommonBean> getData() { return data;}


    public void setData(List<CommonBean> data) { this.data = data;}


}
