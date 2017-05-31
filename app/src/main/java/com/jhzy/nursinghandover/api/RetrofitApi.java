package com.jhzy.nursinghandover.api;

import com.jhzy.nursinghandover.beans.BedBindBean;
import com.jhzy.nursinghandover.beans.BedHomepageInfo;
import com.jhzy.nursinghandover.beans.BedOrDoor;
import com.jhzy.nursinghandover.beans.BindBedBean;
import com.jhzy.nursinghandover.beans.BindBedsBackBean;
import com.jhzy.nursinghandover.beans.BindRoomBackBean;
import com.jhzy.nursinghandover.beans.ElderGridBean;
import com.jhzy.nursinghandover.beans.JiaoBean;
import com.jhzy.nursinghandover.beans.LoginBean;
import com.jhzy.nursinghandover.beans.RoomGridBean;
import com.jhzy.nursinghandover.beans.UpdateBean;
import com.jhzy.nursinghandover.beans.WorkerBean;
import com.jhzy.nursinghandover.beans.nextItem.CommonItem;
import com.jhzy.nursinghandover.beans.nextItem.NextDetail;
import com.jhzy.nursinghandover.beans.nextItem.ScoreBean;
import com.jhzy.nursinghandover.beans.nextItem.TourBean;
import com.jhzy.nursinghandover.model.lock.LockCode;
import com.jhzy.nursinghandover.model.loginnfc.StaffLoginCode;
import com.jhzy.nursinghandover.utils.CONTACT;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by bigyu on 2017/2/15.
 */

public interface RetrofitApi {
    /**
     * 登录
     */
    @FormUrlEncoded
    @POST(CONTACT.VERSION_INTERFACE + "/Account/OrgLogin")
    Call<LoginBean> login(@FieldMap Map<String, String> map);

    //锁屏界面的数据
    @FormUrlEncoded
    @POST(CONTACT.VERSION_INTERFACE + "/OrgNurse/RoomElders_LockScn")
    Call<LockCode> getLockInfo(
            @Header("Authorization") String token, @FieldMap Map<String, String> map);

    //    刷NFC或者验证账号
    @FormUrlEncoded
    @POST(CONTACT.VERSION_INTERFACE + "/Account/StaffLogin")
    Call<StaffLoginCode> loginNFC(
            @Header("Authorization") String token, @FieldMap Map<String, String> map);

    /**
     * 护工进入护理界面
     */
    @FormUrlEncoded
    @POST(CONTACT.VERSION_INTERFACE + "/OrgNurse/RoomTasks")
    Call<NextDetail> toNext(
            @Header("Authorization") String token, @FieldMap Map<String, String> map);

    /**
     * 护工提交任务
     */
    @FormUrlEncoded
    @POST(CONTACT.VERSION_INTERFACE + "/OrgNurse/SmtTask")
    Call<ScoreBean> submit(
            @Header("Authorization") String token, @FieldMap Map<String, String> map);

    //根据员工工号获取员工信息
    @FormUrlEncoded
    @POST(CONTACT.VERSION_INTERFACE + "/OrgNurse/GetStaffInfo")
    Call<WorkerBean> getWorkerInfo(
            @Header("Authorization") String token, @FieldMap Map<String, String> map);

    //http://192.168.0.38/v1_1/OrgNurse/JiaoBan

    /**
     * 交班
     *
     * @param token
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST(CONTACT.VERSION_INTERFACE + "/OrgNurse/JiaoBan")
    Call<JiaoBean> jiaoBan(
            @Header("Authorization") String token, @FieldMap Map<String, String> map);

    /**
     * 接班
     *
     * @param token
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST(CONTACT.VERSION_INTERFACE + "/OrgNurse/JieBan")
    Call<JiaoBean> jieBan(@Header("Authorization") String token, @FieldMap Map<String, String> map);

    /**
     * 巡视
     */
    @FormUrlEncoded
    @POST(CONTACT.VERSION_INTERFACE + "/OrgNurse/TouringElder")
    Call<TourBean> tour(@Header("Authorization") String token, @FieldMap Map<String, String> map);

    //http://192.168.0.38/v1_1/OrgNurse/Rooms

    /**
     * 获取区域的接口
     *
     * @param token
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST(CONTACT.VERSION_INTERFACE + "/OrgNurse/Rooms")
    Call<BedBindBean> getZone(
            @Header("Authorization") String token, @FieldMap Map<String, String> map);

    @FormUrlEncoded//获取区域内床位的接口
    @POST(CONTACT.VERSION_INTERFACE + "/OrgNurse/RoomBeds")
    Call<BindBedBean> getBed(
            @Header("Authorization") String token, @FieldMap Map<String, String> map);

    //http://192.168.0.38/v1_1/OrgNurse/BindRooms
    @FormUrlEncoded//获取区域内床位的接口
    @POST(CONTACT.VERSION_INTERFACE + "/OrgNurse/BindRooms")
    Call<BindRoomBackBean> bindRooms(
            @Header("Authorization") String token, @FieldMap Map<String, String> map);

    //http://192.168.0.38/v1_1/OrgNurse/UnBindRooms,
    //解绑房间
    @FormUrlEncoded//解绑房间
    @POST(CONTACT.VERSION_INTERFACE + "/OrgNurse/UnBindRooms")
    Call<BindBedsBackBean> unBindRooms(
            @Header("Authorization") String token, @FieldMap Map<String, String> map);

    //http://192.168.0.38/v1_1/OrgNurse/BindBeds

    //绑定床位
    //http://192.168.0.38/v1_1/OrgNurse/BindBeds
    @FormUrlEncoded
    @POST(CONTACT.VERSION_INTERFACE + "/OrgNurse/BindBeds")
    Call<BindBedsBackBean> bindBeds(
            @Header("Authorization") String token, @FieldMap Map<String, String> map);

    //解绑床位
    //http://192.168.0.38/v1_1/OrgNurse/UnBindBeds
    @FormUrlEncoded
    @POST(CONTACT.VERSION_INTERFACE + "/OrgNurse/UnBindBeds")
    Call<BindBedsBackBean> unBindBeds(
            @Header("Authorization") String token, @FieldMap Map<String, String> map);

    //http://192.168.0.38/public/AppVersion	，版本更新
    @FormUrlEncoded
    @POST("public/AppVersion")
    Call<UpdateBean> updateApp(@FieldMap Map<String, String> map);

    @Streaming//大文件时要加不然会OOM
    @GET
    Call<ResponseBody> downloadFile(@Url String fileUrl);

    /**
     * 获得日常护理
     */
    @POST(CONTACT.VERSION_INTERFACE + "/OrgNurse/TemporaryTask")
    Call<CommonItem> getCommon(@Header("Authorization") String token);

    /**
     * 获取绑定老人的数量
     */
    @GET(CONTACT.VERSION_INTERFACE + "/OrgNurse/BindedBeds")
    Call<BedOrDoor> checkBedOrDoor(@Header("Authorization") String token, @QueryMap Map<String, String> map);

    /**
     * 获取该床位老人信息（首页）
     */
    @GET(CONTACT.VERSION_INTERFACE + "/OrgNurse/BedElderInfo")
    Call<BedHomepageInfo> getBedElderInfo(@Header("Authorization") String token, @QueryMap Map<String, String> map);

    /**
     * 查询对应房间的任务完成情况
     *
     * @param token
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST(CONTACT.VERSION_INTERFACE + "/OrgNurse/BedUnfinished")
    Call<RoomGridBean> getAllElders(@Header("Authorization") String token, @FieldMap Map<String, String> map);


    /**
     * 查询对应长者的任务完成情况
     *
     * @param token
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST(CONTACT.VERSION_INTERFACE + "/OrgNurse/ElderUnfinished")
    Call<ElderGridBean> getAllTask(@Header("Authorization") String token, @FieldMap Map<String, String> map);
}
