package com.jhzy.nursinghandover.ui;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jhzy.nursinghandover.R;
import com.jhzy.nursinghandover.api.OnClickListenerNoDouble;
import com.jhzy.nursinghandover.beans.WorkerDataBean;
import com.jhzy.nursinghandover.beans.nextItem.CommonBean;
import com.jhzy.nursinghandover.beans.nextItem.CommonItem;
import com.jhzy.nursinghandover.beans.nextItem.DataBean;
import com.jhzy.nursinghandover.beans.nextItem.EldersBean;
import com.jhzy.nursinghandover.beans.nextItem.NextDetail;
import com.jhzy.nursinghandover.beans.nextItem.NurseBean;
import com.jhzy.nursinghandover.beans.nextItem.ScoreBean;
import com.jhzy.nursinghandover.beans.nextItem.StaffBean;
import com.jhzy.nursinghandover.beans.nextItem.SubItem;
import com.jhzy.nursinghandover.beans.nextItem.TourBean;
import com.jhzy.nursinghandover.model.lock.Elders;
import com.jhzy.nursinghandover.ui.adapters.CommonItemAdapter;
import com.jhzy.nursinghandover.ui.adapters.NextElderAdapter;
import com.jhzy.nursinghandover.ui.adapters.NextItemAdapter;
import com.jhzy.nursinghandover.utils.AppUtils;
import com.jhzy.nursinghandover.utils.CONTACT;
import com.jhzy.nursinghandover.utils.CustomDialogutils;
import com.jhzy.nursinghandover.utils.GetSign;
import com.jhzy.nursinghandover.utils.HttpUtils;
import com.jhzy.nursinghandover.utils.ImageLoaderUtils;
import com.jhzy.nursinghandover.utils.PopWindowMoreWorkerUtils;
import com.jhzy.nursinghandover.utils.PopWindowUtils;
import com.jhzy.nursinghandover.utils.SPUtils;
import com.jhzy.nursinghandover.utils.ScreenBritnessUtil;
import com.jhzy.nursinghandover.utils.ScreenUtils;
import com.jhzy.nursinghandover.utils.TimeParseUtils;
import com.jhzy.nursinghandover.widget.ImageMaskView;
import com.jhzy.nursinghandover.widget.ImageTxtView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 护理界面
 */
public class NextActivity extends BaseActivity {
    public static String TAG = "next";
    public static String TAG1 = "next1";
    private ViewHolder holder;
    /**
     * 服务项目
     */
    List<NurseBean> nurseBeanList;
    /**
     * 服务项适配器
     */
    private NextItemAdapter nextItemAdapter;
    /**
     * 老人适配器
     */
    private NextElderAdapter nextElderAdapter;
    //服务项点击index
    private int lastPosition = -1;
    //锁屏时间
    private int lockTime = 60 * 1000;
    //工号
    private String workNo;
    private DataBean data;
    private StaffBean staff;
    //服务子项
    private List<SubItem> sbList;
    //护理项目标题
    private String currentTitle;
    private NurseBean currentBean;
    //是否是护理任务
    private boolean isTask = true;
    private List<WorkerDataBean> nurseSum;
    //接收老人总量
    private ArrayList<Elders> tempList;
    //日常护理的信息
    private List<EldersBean> commonElderList;
    private CustomDialogutils customDialogutils1;

    private String dailyText = "日积分：";
    private String monthText = "月积分：";
    private int statusBarHeight;
    private PopWindowMoreWorkerUtils popWindowMoreWorkerUtils;
    private WorkerDataBean currentWorker;
    //日常护理项适配器
    private CommonItemAdapter commonItemAdapter;
    private List<CommonBean> commonData;

    @Override
    public int getContentView() {
        return R.layout.activity_next;
    }


    @Override
    public void init() {
        initUtils();
        initView();
        initListener();
        initData();
        loadItems();
        loadCommon();
    }


    /**
     * 记载日常护理
     */
    private void loadCommon() {
        holder.commonRv.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));
        commonItemAdapter = new CommonItemAdapter();
        commonItemAdapter.setClick(commonClick);
        holder.commonRv.setAdapter(commonItemAdapter);
        TreeMap<String,String> map = new TreeMap<>();
        map.put("sign",GetSign.GetSign(map,SPUtils.find(CONTACT.TOKEN)));
        HttpUtils.getInstance().getRetrofitApi().getCommon("basic " + SPUtils.find(CONTACT.TOKEN)).enqueue(
            new Callback<CommonItem>() {

                @Override
                public void onResponse(Call<CommonItem> call, Response<CommonItem> response) {
                    CommonItem body = response.body();
                    if(body != null && CONTACT.DataSuccess.equals(body.getCode())){
                        commonData = body.getData();
                        commonItemAdapter.setDatas(commonData);
                    }
                }


                @Override public void onFailure(Call<CommonItem> call, Throwable t) {
                }
            });
    }

    //
    CommonItemAdapter.CommonClick commonClick = new CommonItemAdapter.CommonClick() {
        @Override public void click(View view,int position) {
            String title = ((ImageTxtView) view).getTitle();
            currentTitle = title;
            final List<EldersBean> eldersBeen = new ArrayList<>();
            for (int i = 0; i < commonElderList.size(); i++) {
                EldersBean eldersBean = commonElderList.get(i);
                if (eldersBean.isChecked()) {
                    eldersBean.setCName(title);
                    eldersBeen.add(eldersBean);
                }
            }
            if (eldersBeen.size() == 0) {
                Toast.makeText(mContext, "请选择完成护理的老人", Toast.LENGTH_SHORT).show();
                return;
            }

            List<SubItem> subItems;
            subItems = makeGson(commonData.get(position).getSubproject());
            commonID = commonData.get(position).getID();
            PopWindowUtils.showNurseItemPop(mContext, findViewById(R.id.rl_search_main),
                getWindow(), currentTitle, subItems, new PopWindowUtils.NurseItemCallback() {
                    @Override
                    public void subItem(List<SubItem> subItems) {
                        for (int i = 0; i < eldersBeen.size(); i++) {
                            eldersBeen.get(i).setCid(commonID);
                            eldersBeen.get(i).setSubItem(getSubItemString(subItems));
                        }
                    }
                });
        }
    };


    private void initUtils() {
        CONTACT.type = CONTACT.NFCTAG.TAG_MORENURSE;
        Bundle bundle = getIntent().getExtras();
        workNo = bundle.getString(TAG);
        tempList = (ArrayList<Elders>) bundle.getSerializable(TAG1);
        holder = new ViewHolder();
        currentWorker = new WorkerDataBean();
        statusBarHeight = ScreenUtils.getStatusBarHeight(mContext);
        popWindowMoreWorkerUtils = new PopWindowMoreWorkerUtils();
    }


    private void loadItems() {
        customDialogutils1.setResfreshDialog("正在加载数据", 5 * 1000);
        TreeMap<String, String> map = new TreeMap<>();
        map.put("JobNo", workNo);
        map.put("DeviceID", SPUtils.find(CONTACT.IMEI));
        map.put("sign",GetSign.GetSign(map,SPUtils.find(CONTACT.TOKEN)));
        HttpUtils.getInstance()
                .getRetrofitApi()
                .toNext("basic " + SPUtils.find(CONTACT.TOKEN), map)
                .enqueue(new Callback<NextDetail>() {
                    @Override
                    public void onResponse(Call<NextDetail> call, Response<NextDetail> response) {
                        NextDetail body = response.body();
                        if (body != null && CONTACT.DataSuccess.equals(body.getCode())) {
                            customDialogutils1.cancelNetworkDialog("加载成功", true);
                            data = response.body().getData();
                            staff = data.getStaff();
                            nurseBeanList = data.getNurse();
                            if (!TextUtils.isEmpty(staff.getPhotoUrl())) {
                                ImageLoaderUtils.load(holder.head, staff.getPhotoUrl());
                            }
                            if (!TextUtils.isEmpty(staff.getStaffName())) {
                                holder.name.setText(staff.getStaffName());
                            }
                            holder.roomid.setText(staff.getStaffID() + "");
                            holder.dailyScore.setText(dailyText + staff.getDayPoint());
                            holder.monthScore.setText(monthText + staff.getMonthPoint());
                            nextItemAdapter.setData(nurseBeanList);
                            //加入初始护工id
                            currentWorker.setStaffID(staff.getStaffID());
                            currentWorker.setPhotoUrl(staff.getPhotoUrl());
                            currentWorker.setStaffName(staff.getStaffName());
                            currentWorker.setWorkerNo(workNo);
                            nurseSum.add(currentWorker);
                            scrollToPosition();
                        } else {
                            customDialogutils1.cancelNetworkDialog("服务器异常", false);
                        }
                    }


                    @Override
                    public void onFailure(Call<NextDetail> call, Throwable t) {
                        t.printStackTrace();
                        if (!AppUtils.isNetworkConnected(mContext)) {
                            customDialogutils1.cancelNetworkDialog("网络异常", false);
                        } else {
                            customDialogutils1.cancelNetworkDialog("服务器异常", false);
                        }
                    }
                });
    }


    private void initData() {
        customDialogutils1 = new CustomDialogutils(this);
        nurseBeanList = new ArrayList<>();
        sbList = new ArrayList<>();
        nurseSum = new ArrayList<>();
        commonElderList = new ArrayList<>();
        if (tempList != null) {
            for (int i = 0; i < tempList.size(); i++) {
                Elders elder = tempList.get(i);
                EldersBean bean = new EldersBean();
                bean.setBedTitle(elder.getBedTitle());
                bean.setElderID(elder.getElderID());
                bean.setElderName(elder.getElderName());
                bean.setPhotoUrl(elder.getPhotoUrl());
                bean.setChecked(false);
                bean.setIsCompleted(0);
                bean.setSubItem("");
                bean.setTaskId(-1);
                commonElderList.add(bean);
            }
        }
        nextItemAdapter = new NextItemAdapter(mContext, click);
        holder.leftRv.setLayoutManager(new LinearLayoutManager(mContext));
        holder.leftRv.setAdapter(nextItemAdapter);
        nextElderAdapter = new NextElderAdapter(mContext, elderClick);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 5);
        holder.rightRv.setLayoutManager(gridLayoutManager);
        holder.rightRv.setAdapter(nextElderAdapter);
    }

    /**
     * 长者点击
     */
    NextElderAdapter.ElderClick elderClick = new NextElderAdapter.ElderClick() {
        @Override
        public void onElderClick(final View view, final int position) {
            if (isTask) {//护理任务状态下
                final EldersBean info = currentBean.getElders().get(position);
                if (info.getIsCompleted() == 1) {
                    Toast.makeText(mContext, "已完成", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (info.isChecked()) {//老人已经被选中状态
                    info.setChecked(!info.isChecked());
                    info.setSubItem("");
                    nextElderAdapter.notifyDataSetChanged();
                } else {
                    if (sbList.size() > 1) {//未选中状态下假如有服务子项就弹窗
                        for (int i = 0; i < sbList.size(); i++) {
                            sbList.get(i).setSelected(false);
                            sbList.get(i).setTask(true);
                        }
                        PopWindowUtils.showNurseItemPop(mContext, findViewById(R.id.rl_search_main),
                                getWindow(), currentTitle, sbList, new PopWindowUtils.NurseItemCallback() {
                                    @Override
                                    public void subItem(List<SubItem> subItems) {
                                        if (subItems.size() > 0) {//选中服务子项返回
                                            currentBean.getElders()
                                                    .get(position)
                                                    .setSubItem(getSubItemString(subItems));
                                            info.setChecked(!info.isChecked());
                                            loadAnimation(view, info.getPhotoUrl());
                                            nextElderAdapter.notifyDataSetChanged();
                                            cal(currentBean.getElders());
                                        } else {
                                        /*Toast.makeText(mContext, "未选择服务子项", Toast.LENGTH_SHORT)
                                            .show();*/
                                        }
                                    }
                                });
                    } else {//未选中状态下没有服务子项就选中
                        info.setChecked(!info.isChecked());
                        loadAnimation(view, info.getPhotoUrl());
                        nextElderAdapter.notifyDataSetChanged();
                    }
                }
                cal(currentBean.getElders());
            } else {
                int ii = 0;
                EldersBean eldersBean = commonElderList.get(position);
                for (int i = 0; i < commonElderList.size(); i++) {
                    if (commonElderList.get(i).isChecked()) {
                        ii++;
                    }
                }
                if (commonID != 0) {//有护理任务
                    /*for (int i = 0; i < commonElderList.size(); i++) {
                        if (commonElderList.get(i).isChecked()) {
                            ii++;
                        }
                    }*/
                    if (ii == 0) {//假如长者全部被点掉，将服务项置0
                        commonID = 0;
                    }
                    if (eldersBean.isChecked()) {//被选中状态下
                        if (ii == 1) {
                            holder.commonRv.setVisibility(View.GONE);
                        }
                        eldersBean.setCid(0);
                        eldersBean.setCName("");
                        eldersBean.setSubItem("");
                        eldersBean.setChecked(!eldersBean.isChecked());
                        nextElderAdapter.notifyDataSetChanged();
                    } else {
                        if (commonID == 0) {
                            eldersBean.setChecked(!eldersBean.isChecked());
                            loadAnimation(view, commonElderList.get(position).getPhotoUrl());
                            if (!holder.commonRv.isShown()) {
                                ObjectAnimator animator = ObjectAnimator.ofFloat(holder.commonRv, "translationY", 350.0f, 0.0f, 0f);
                                animator.setDuration(500).start();
                                holder.commonRv.setVisibility(View.VISIBLE);
                            }
                            nextElderAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(mContext, "请确认完成当前选择任务再选择老人", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {//没护理任务
                    if (!eldersBean.isChecked()) {
                        loadAnimation(view, commonElderList.get(position).getPhotoUrl());
                    }
                    eldersBean.setChecked(!eldersBean.isChecked());
                    if (!eldersBean.isChecked() && ii == 1) {
                        holder.commonRv.setVisibility(View.GONE);
                    } else {
                        if (!holder.commonRv.isShown()) {
                            ObjectAnimator animator = ObjectAnimator.ofFloat(holder.commonRv, "translationY", 350.0f, 0.0f, 0f);
                            animator.setDuration(500).start();
                            holder.commonRv.setVisibility(View.VISIBLE);
                        }

                    }
                    nextElderAdapter.notifyDataSetChanged();
                }
                cal(commonElderList);
            }
        }
    };


    /**
     * 计算选中老人
     * @param bean
     */
    private void cal(List<EldersBean> bean) {
        int ii = 0;
        for (int i = 0; i < bean.size(); i++) {
            if (bean.get(i).isChecked()) {
                ii++;
            }
        }
        if (ii > 0) {
            holder.confirm.setText("确认完成(" + ii + ")");
        } else {
            holder.confirm.setText("确认完成");
        }
    }

    /**
     * 服务项点击
     */
    NextItemAdapter.ItemClick click = new NextItemAdapter.ItemClick() {
        @Override
        public void onItemClick(View view, int position) {
            isTask = true;
            commonID = 0;
            holder.commonRv.setVisibility(View.GONE);
            holder.commonView.setBackgroundResource(R.mipmap.next_unclick_3);
            holder.commonName.setTextColor(Color.BLACK);
            if (position != lastPosition) {
                currentBean = nurseBeanList.get(position);
                currentBean.setChecked(true);
                if (lastPosition > -1) {//上一项是任务项
                    nurseBeanList.get(lastPosition).setChecked(false);
                    NurseBean lastItem = nurseBeanList.get(lastPosition);
                    for (int i = 0; i < lastItem.getElders().size(); i++) {
                        EldersBean eldersBean = lastItem.getElders().get(i);
                        eldersBean.setSubItem("");
                        if (eldersBean.isChecked()) {
                            eldersBean.setChecked(false);
                        }
                    }
                }
                nextItemAdapter.notifyDataSetChanged();
                lastPosition = position;
                //设置新服务项
                ImageLoaderUtils.load(holder.icon, currentBean.getCareItemUrl());
                currentTitle = currentBean.getCareItemTitle();
                holder.itemName.setText(currentTitle);
                if (!TextUtils.isEmpty(currentBean.getSubproject())) {
                    sbList = makeGson(currentBean.getSubproject());
                } else {
                    sbList.clear();
                }
                nextElderAdapter.setData(currentBean.getElders());
                nextElderAdapter.notifyDataSetChanged();
                holder.confirm.setText("确认完成");
            }
        }
    };

    private CustomDialogutils customDialogutils = null;
    OnClickListenerNoDouble noDouble = new OnClickListenerNoDouble() {
        @Override
        public void myOnClick(View view) {
            switch (view.getId()) {
                //未完成任务
                case R.id.next_todo:
                    Intent intent1 = new Intent(mContext, RoomActivity.class);
                    intent1.putExtra(RoomActivity.TAG,workNo);
                    intent1.putExtra(RoomActivity.TAG1,"tiban");
                    startActivity(intent1);
                    break;
                case R.id.next_back://返回
                    finish();
                    break;
                case R.id.next_tour://巡视
                    tour();
                    break;
                case R.id.next_multi://多人
                    popWindowMoreWorkerUtils.show(NextActivity.this,
                            findViewById(R.id.rl_search_main), getWindow(), currentWorker,
                            new PopWindowMoreWorkerUtils.NurseSumCallback() {
                                @Override
                                public void nurseSum(List<WorkerDataBean> strings) {
                                    nurseSum = strings;
                                }
                            });
                    break;
                case R.id.next_handover://交接班
                    Intent intent = new Intent(NextActivity.this, ShiftsActivity.class);
                    intent.putExtra("workNo", workNo);
                    startActivity(intent);
                    break;
                case R.id.next_common://日常护理
                    if (lastPosition > -1) {
                        NurseBean lastItem = nurseBeanList.get(lastPosition);
                        lastItem.setChecked(false);
                        for (int i = 0; i < lastItem.getElders().size(); i++) {
                            EldersBean eldersBean = lastItem.getElders().get(i);
                            eldersBean.setSubItem("");
                            if (eldersBean.isChecked()) {
                                eldersBean.setChecked(false);
                            }
                        }
                        nextItemAdapter.notifyDataSetChanged();
                        lastPosition = -1;
                        isTask = false;
                        Uri uri = Uri.parse("res://" + mContext.getPackageName() + "/" + R.mipmap.icon_rchl);
                        holder.icon.setImageURI(uri);
                        holder.itemName.setText("常用护理");
                        holder.commonName.setTextColor(Color.WHITE);
                        //holder.commonDetail.setVisibility(View.VISIBLE);
                        holder.commonView.setBackgroundResource(R.mipmap.next_click_3);
                        for (int i = 0; i < commonElderList.size(); i++) {
                            if (commonElderList.get(i).isChecked()) {
                                commonElderList.get(i).setChecked(false);
                            }
                        }
                        nextElderAdapter.setData(commonElderList);
                        holder.confirm.setText("确认完成");
                    }
                    break;
                case R.id.next_confirm://提交
                    if (isTask) {
                        task();
                    } else {
                        common();
                    }
                    break;
            }
        }
    };

    /**
     * 巡视
     */
    private void tour() {
        customDialogutils1.setResfreshDialog("提交巡视信息");
        TreeMap<String, String> map = new TreeMap<>();
        map.put("DeviceCode", SPUtils.find(CONTACT.IMEI));
        map.put("StaffCode", workNo);
        map.put("sign", GetSign.GetSign(map, SPUtils.find(CONTACT.TOKEN)));
        HttpUtils.getInstance().getRetrofitApi().tour("basic " + SPUtils.find(CONTACT.TOKEN), map).enqueue(
                new Callback<TourBean>() {
                    @Override
                    public void onResponse(Call<TourBean> call, Response<TourBean> response) {
                        TourBean body = response.body();
                        if (body != null && CONTACT.DataSuccess.equals(body.getCode())) {
                            customDialogutils1.cancelNetworkDialog("提交成功", true);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 1000);
                        } else {
                            customDialogutils1.cancelNetworkDialog("服务器异常", false);
                        }
                    }


                    @Override
                    public void onFailure(Call<TourBean> call, Throwable t) {
                        if (!AppUtils.isNetworkConnected(mContext)){
                            customDialogutils1.cancelNetworkDialog("网络异常", false);
                        }else {
                            customDialogutils1.cancelNetworkDialog("服务异常", false);
                        }
                    }
                });
    }


    /**
     * 提交日常护理
     */
    private void common() {
        List<EldersBean> list = new ArrayList<>();
        int ii = 0;
        for (int i = 0; i < commonElderList.size(); i++) {
            EldersBean eldersBean = commonElderList.get(i);
            if (eldersBean.isChecked()) {
                list.add(eldersBean);
                ii++;
            }
        }
        if (ii == 0) {
            Toast.makeText(mContext, "未选择老人", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(list.get(0).getSubItem())) {
            commonID = 0;
        }
        if (commonID == 0 || TextUtils.isEmpty(list.get(0).getSubItem())) {
            Toast.makeText(mContext, "请选择护理任务", Toast.LENGTH_SHORT).show();
            return;
        }
        TreeMap<String, String> map = new TreeMap<>();
        map.put("Type", "2");
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            EldersBean eldersBean = list.get(i);
            JSONObject object = new JSONObject();
            try {
                object.put("CID", eldersBean.getCid());
                object.put("CName", eldersBean.getCName());
                object.put("TID", eldersBean.getTaskId());
                JSONArray jsonArray1 = new JSONArray();
                for (int j = 0; j < nurseSum.size(); j++) {
                    JSONObject o = new JSONObject();
                    o.put("id", nurseSum.get(j).getStaffID());
                    jsonArray1.put(o);
                }
                object.put("SID", jsonArray1);
                object.put("EID", eldersBean.getElderID());
                object.put("Sub", eldersBean.getSubItem());
                jsonArray.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.e("rxf", "提交的json串==" + jsonArray.toString());
        map.put("Task", jsonArray.toString());
        map.put("sign",GetSign.GetSign(map,SPUtils.find(CONTACT.TOKEN)));
        customDialogutils1.setResfreshDialog("正在提交");
        HttpUtils.getInstance()
                .getRetrofitApi()
                .submit("basic " + SPUtils.find(CONTACT.TOKEN), map)
                .enqueue(
                        new Callback<ScoreBean>() {
                            @Override
                            public void onResponse(Call<ScoreBean> call, Response<ScoreBean> response) {
                                ScoreBean body = response.body();
                                if (body != null &&
                                        CONTACT.DataSuccess.equals(body.getCode())) {
                                    for (int i = 0; i < commonElderList.size(); i++) {
                                        EldersBean eldersBean = commonElderList.get(i);
                                        eldersBean.setChecked(false);
                                        eldersBean.setSubItem("");
                                    }
                                    nextElderAdapter.notifyDataSetChanged();
                                    commonID = 0;
                                    holder.dailyScore.setText(dailyText + body.getData().getDayPoint() + "");
                                    holder.monthScore.setText(monthText + body.getData().getMonthPoint() + "");
                                    customDialogutils1.cancelNetworkDialog("提交成功", true);
                                    holder.commonRv.setVisibility(View.GONE);
                                    holder.confirm.setText("确认完成");
                                    if (popWindowMoreWorkerUtils.dataList != null) {
                                        popWindowMoreWorkerUtils.dataList.clear();
                                        popWindowMoreWorkerUtils.dataList.add(currentWorker);
                                    }
                                } else {
                                    customDialogutils1.cancelNetworkDialog("服务器异常", false);
                                }
                            }


                            @Override
                            public void onFailure(Call<ScoreBean> call, Throwable t) {
                                if (!AppUtils.isNetworkConnected(mContext)) {
                                    customDialogutils1.cancelNetworkDialog("网络异常", false);
                                }else {
                                    customDialogutils1.cancelNetworkDialog("服务器异常", false);
                                }
                            }
                        });
    }


    /**
     * 提交护理任务
     */
    private void task() {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("Type", "1");
        if (currentBean == null) {
            return;
        }
        final List<EldersBean> elders = currentBean.getElders();
        final List<EldersBean> tempList = new ArrayList<>();
        for (int i = 0; i < elders.size(); i++) {
            EldersBean eldersBean = elders.get(i);
            if (eldersBean.isChecked() && eldersBean.getIsCompleted() == 0) {
                tempList.add(eldersBean);
            }
        }
        if (tempList.size() == 0) {
            Toast.makeText(this, "请选择完成护理的老人", Toast.LENGTH_SHORT).show();
            return;
        }
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < tempList.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("CID", currentBean.getCareItemID());
                jsonObject.put("CName", currentBean.getCareItemTitle());
                jsonObject.put("TID", tempList.get(i).getTaskId());
                JSONArray jsonArray1 = new JSONArray();
                for (int j = 0; j < nurseSum.size(); j++) {
                    JSONObject o = new JSONObject();
                    o.put("id", nurseSum.get(j).getStaffID());
                    jsonArray1.put(o);
                }
                jsonObject.put("SID", jsonArray1);
                jsonObject.put("EID", tempList.get(i).getElderID());
                jsonObject.put("Sub", tempList.get(i).getSubItem());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.e("rxf", "提交的json串==" + jsonArray.toString());
        map.put("Task", jsonArray.toString());
        map.put("sign",GetSign.GetSign(map,SPUtils.find(CONTACT.TOKEN)));
        customDialogutils1.setResfreshDialog("正在提交");
        HttpUtils.getInstance()
                .getRetrofitApi()
                .submit("basic " + SPUtils.find(CONTACT.TOKEN), map)
                .enqueue(
                        new Callback<ScoreBean>() {
                            @Override
                            public void onResponse(Call<ScoreBean> call, Response<ScoreBean> response) {
                                ScoreBean body = response.body();
                                if (body != null &&
                                        CONTACT.DataSuccess.equals(body.getCode())) {
                                    customDialogutils1.cancelNetworkDialog("提交成功", true);
                                    for (int i = 0; i < currentBean.getElders().size(); i++) {
                                        EldersBean eldersBean = currentBean.getElders().get(i);
                                        if (eldersBean.isChecked()) {
                                            eldersBean.setIsCompleted(1);
                                            eldersBean.setChecked(false);
                                            //ii++;
                                        }
                                    }
                                    holder.dailyScore.setText(dailyText + body.getData().getDayPoint());
                                    holder.monthScore.setText(monthText + body.getData().getMonthPoint());
                                    nextElderAdapter.notifyDataSetChanged();
                                    /*int ii = 0;
                                    for (int i = 0; i < currentBean.getElders().size(); i++) {
                                        if (currentBean.getElders().get(i).getIsCompleted() == 0) {
                                            ii++;
                                            break;
                                        }
                                    }
                                    if (ii == 0) {
                                        nurseBeanList.remove(lastPosition);
                                        nextItemAdapter.notifyDataSetChanged();
                                        scrollToPosition();
                                    }*/
                                    holder.confirm.setText("确认完成");
                                    if (popWindowMoreWorkerUtils.dataList != null) {
                                        popWindowMoreWorkerUtils.dataList.clear();
                                        popWindowMoreWorkerUtils.dataList.add(currentWorker);
                                    }
                                } else {
                                    customDialogutils1.cancelNetworkDialog("服务器异常", false);
                                }
                            }

                            @Override
                            public void onFailure(Call<ScoreBean> call, Throwable t) {
                                if (!AppUtils.isNetworkConnected(mContext)) {
                                    customDialogutils1.cancelNetworkDialog("网络异常", false);
                                }else {
                                    customDialogutils1.cancelNetworkDialog("服务器异常", false);
                                }
                            }
                        });
    }


    private Handler handler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(mContext,MainActivity.class);
            startActivity(intent);
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        Log.e("rxf", "onresume");
        ScreenBritnessUtil.setScreenBritness(getWindow());
        if (handler != null && mRunnable != null) {
            handler.postDelayed(mRunnable, lockTime);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.e("rxf", "onPause");
        if (handler != null && mRunnable != null) {
            handler.removeCallbacks(mRunnable);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        popWindowMoreWorkerUtils.dataList = null;
    }


    //触摸监听（没操作20秒后跳转屏保）
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        ScreenBritnessUtil.setScreenBritness(getWindow());

        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                handler.postDelayed(mRunnable, lockTime);
                break;
            case MotionEvent.ACTION_DOWN:
                handler.removeCallbacks(mRunnable);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }


    /**
     * 滚动到对应时间段的任务（没对应时间段任务显示日常护理）
     */
    private void scrollToPosition() {
        try {
            //当前时间
            long time = System.currentTimeMillis();
            int ii = 0;//判断是否滚动到对应时间段或者日常
            for (int i = 0; i < nurseBeanList.size(); i++) {
                String startTime = nurseBeanList.get(i).getStartTime();
                String endTime = nurseBeanList.get(i).getEndTime();

                String tTime = TimeParseUtils.getDateDate() + " " + startTime;
                String eTime = TimeParseUtils.getDateDate() + " " + endTime;
                //护理开始时间
                long timeParseS = TimeParseUtils.getTimeMillion2(tTime);
                //护理结束时间
                long timeParseE = TimeParseUtils.getTimeMillion2(eTime);
                if (timeParseS <= time && time <= timeParseE) {
                    lastPosition = i;
                    holder.leftRv.scrollToPosition(lastPosition);
                    holder.commonView.setBackgroundResource(R.mipmap.next_unclick_3);
                    holder.commonName.setTextColor(Color.BLACK);
                    currentBean = nurseBeanList.get(lastPosition);
                    nextElderAdapter.setData(currentBean.getElders());
                    for (NurseBean bean : nurseBeanList) {
                        bean.setChecked(false);
                    }
                    currentTitle = currentBean.getCareItemTitle();
                    holder.itemName.setText(currentTitle);
                    ImageLoaderUtils.load(holder.icon, currentBean.getCareItemUrl());
                    currentBean.setChecked(true);
                    if (!TextUtils.isEmpty(currentBean.getSubproject())) {
                        sbList = makeGson(currentBean.getSubproject());
                    }
                    ii++;
                    break;
                }
            }
            if (ii == 0) {
                lastPosition = -1;
                isTask = false;
                holder.itemName.setText("常用护理");
                holder.commonName.setTextColor(Color.WHITE);
                holder.commonView.setBackgroundResource(R.mipmap.next_click_3);
                Uri uri = Uri.parse("res://" + mContext.getPackageName() + "/" + R.mipmap.icon_rchl);
                holder.icon.setImageURI(uri);
                nextElderAdapter.setData(commonElderList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 111:
                if (resultCode == RESULT_OK) {
                    loadItems();
                }
                break;
        }
    }*/


    /**
     * 将护理项目写成json格式
     * @param json
     * @return
     */
    private List<SubItem> makeGson(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<SubItem>>() {
        }.getType();
        return gson.fromJson(json, type);
    }


    private void initListener() {
        holder.back.setOnClickListener(noDouble);
        holder.tour.setOnClickListener(noDouble);
        holder.multi.setOnClickListener(noDouble);
        holder.handover.setOnClickListener(noDouble);
        holder.commonView.setOnClickListener(noDouble);
        holder.confirm.setOnClickListener(noDouble);
        holder.todo.setOnClickListener(noDouble);

        //bigyu
        /*holder.img1.setOnClickListener(quickOnClickListener);
        holder.img2.setOnClickListener(quickOnClickListener);
        holder.img3.setOnClickListener(quickOnClickListener);
        holder.img4.setOnClickListener(quickOnClickListener);
        holder.img5.setOnClickListener(quickOnClickListener);
        holder.img6.setOnClickListener(quickOnClickListener);
        holder.img7.setOnClickListener(quickOnClickListener);*/
    }


    private void initView() {
        holder.tour = ((TextView) findViewById(R.id.next_tour));
        holder.multi = ((TextView) findViewById(R.id.next_multi));
        holder.handover = (TextView) findViewById(R.id.next_handover);
        holder.back = ((ImageView) findViewById(R.id.next_back));
        holder.name = ((TextView) findViewById(R.id.next_name));
        holder.roomid = ((TextView) findViewById(R.id.next_roomid));
        holder.commonView = findViewById(R.id.next_common);
        holder.head = ((SimpleDraweeView) findViewById(R.id.next_headphoto));
        holder.leftRv = ((RecyclerView) findViewById(R.id.next_left));
        holder.icon = ((SimpleDraweeView) findViewById(R.id.next_icon));
        holder.itemName = ((TextView) findViewById(R.id.next_itemname));
        holder.confirm = ((TextView) findViewById(R.id.next_confirm));
        holder.rightRv = ((RecyclerView) findViewById(R.id.next_right));
        holder.commonDetail = findViewById(R.id.next_common_detail);
        holder.commonName = ((TextView) findViewById(R.id.next_common_name));
        holder.dailyScore = ((TextView) findViewById(R.id.next_daily_score));
        holder.monthScore = ((TextView) findViewById(R.id.next_month_score));
        ///bigyu
        holder.img1 = (ImageTxtView) findViewById(R.id.img_1);
        holder.img2 = (ImageTxtView) findViewById(R.id.img_2);
        holder.img3 = (ImageTxtView) findViewById(R.id.img_3);
        holder.img4 = (ImageTxtView) findViewById(R.id.img_4);
        holder.img5 = (ImageTxtView) findViewById(R.id.img_5);
        holder.img6 = (ImageTxtView) findViewById(R.id.img_6);
        holder.img7 = (ImageTxtView) findViewById(R.id.img_7);
        holder.parentLayout = (RelativeLayout) findViewById(R.id.rl_search_main);
        holder.commonRv = ((RecyclerView) findViewById(R.id.next_common_rv));
        holder.rightLayout = findViewById(R.id.next_right_layout);
        holder.todo = ((TextView) findViewById(R.id.next_todo));
    }


    private class ViewHolder {
        TextView tour, multi, handover,todo;//巡视，多人，交接班
        TextView name, roomid;//护工姓名，房间号,常用护理按钮
        View commonView, commonDetail;//常用护理，常用护理项目
        ImageView back;//返回
        SimpleDraweeView head;//护工头像
        RecyclerView leftRv, rightRv;//左边任务列表，右边老人列表
        SimpleDraweeView icon;//护理图标
        TextView itemName, confirm;//护理名称，确认按钮
        TextView commonName;
        TextView dailyScore, monthScore;
        //            父布局
        RelativeLayout parentLayout;
        //bigyu
        ImageTxtView img1, img2, img3, img4, img5, img6, img7; // 快捷护理的图标
        RecyclerView commonRv;
        View rightLayout;
    }


    private int commonID = 0;

    /*OnClickListenerNoDouble quickOnClickListener = new OnClickListenerNoDouble() {
        @Override
        public void myOnClick(View view) {
            String title = ((ImageTxtView) view).getTitle();
            final List<EldersBean> eldersBeen = new ArrayList<>();
            for (int i = 0; i < commonElderList.size(); i++) {
                EldersBean eldersBean = commonElderList.get(i);
                if (eldersBean.isChecked()) {
                    eldersBean.setCName(title);
                    eldersBeen.add(eldersBean);
                }
            }
            if (eldersBeen.size() == 0) {
                Toast.makeText(mContext, "请选择完成护理的老人", Toast.LENGTH_SHORT).show();
                return;
            }
            int id = view.getId();
            ArrayList<SubItem> subItems = new ArrayList<>();
            switch (id) {
                case R.id.img_1:
                    commonID = 1;
                    currentTitle = "换纸尿裤";
                    subItems.add(new SubItem("1", R.mipmap.icon_db, "大便", false));
                    subItems.add(new SubItem("1", R.mipmap.icon_xb, "小便", false));
                    break;
                case R.id.img_2:
                    commonID = 2;
                    currentTitle = "倒夜壶";
                    subItems.add(new SubItem("1", R.mipmap.icon_dnh, "倒夜壶", false));
                    break;
                case R.id.img_3:
                    commonID = 3;
                    currentTitle = "喂水";
                    subItems.add(new SubItem("1", R.mipmap.icon_ws, "喂水", false));

                    break;
                case R.id.img_4:
                    commonID = 4;
                    currentTitle = "翻身";
                    subItems.add(new SubItem("1", R.mipmap.icon_zfs, "左", false));
                    subItems.add(new SubItem("1", R.mipmap.icon_pt, "平躺", false));
                    subItems.add(new SubItem("1", R.mipmap.icon_yfs, "右", false));
                    break;
                case R.id.img_5:
                    commonID = 5;
                    currentTitle = "个人清洁";
                    subItems.add(new SubItem("1", R.mipmap.icon_jtf, "理发", false));
                    subItems.add(new SubItem("1", R.mipmap.icon_jzj, "剪指甲", false));
                    subItems.add(new SubItem("1", R.mipmap.icon_my, "沐浴", false));
                    subItems.add(new SubItem("1", R.mipmap.icon_sy, "刷牙", false));
                    break;
                case R.id.img_6:
                    commonID = 6;
                    currentTitle = "助卧床/起床";
                    subItems.add(new SubItem("1", R.mipmap.icon_xzwc, "卧床", false));
                    subItems.add(new SubItem("1", R.mipmap.icon_xzqc, "起床", false));
                    break;
                case R.id.img_7:
                    commonID = 7;
                    currentTitle = "急救";
                    subItems.add(new SubItem("1", R.mipmap.icon_xffs, "心肺复苏", false));
                    subItems.add(new SubItem("1", R.mipmap.icon_ysjj, "噎食急救", false));
                    subItems.add(new SubItem("1", R.mipmap.icon_xy, "吸氧", false));
                    subItems.add(new SubItem("1", R.mipmap.icon_bs, "鼻饲", false));
                    break;
            }
            PopWindowUtils.showNurseItemPop(mContext, findViewById(R.id.rl_search_main),
                    getWindow(), currentTitle, subItems, new PopWindowUtils.NurseItemCallback() {
                        @Override
                        public void subItem(List<SubItem> subItems) {
                            for (int i = 0; i < eldersBeen.size(); i++) {
                                eldersBeen.get(i).setCid(commonID);
                                eldersBeen.get(i).setSubItem(getSubItemString(subItems));
                            }
                        }
                    });
        }
    };*/


    /**
     * 拼接子服务项
     */
    private String getSubItemString(List<SubItem> subItems) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < subItems.size(); i++) {
            if (i == subItems.size() - 1) {
                sb.append(subItems.get(i).getSubprojectName());
            } else {
                sb.append(subItems.get(i).getSubprojectName())
                        .append(",");
            }
        }
        Log.e("rxf", sb.toString());
        return sb.toString();
    }

    private boolean isFirst = true;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && isFirst) {
            commonItemAdapter.setParentWidth(holder.rightLayout.getMeasuredWidth());
            isFirst = false;
        }
    }


    //**************************************bigyu动画***************************************************
    AnimationSet animationSet;

    private void loadAnimation(View view, String url) {
        if (view instanceof ImageMaskView) {
            //获取视图在屏幕上的位置
            int[] location = new int[2];
            view.getLocationOnScreen(location);
            ///获取 确认完成  按钮在屏幕的位置
            int[] location_end = new int[2];
            holder.confirm.getLocationOnScreen(location_end);

            //获取视图的宽度和高度
            int width = view.getWidth();
            int height = view.getHeight();

            final ImageMaskView mCopyView = new ImageMaskView(this);      //新建副本View
            mCopyView.setBackgroundColor(Color.RED);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);  //建立layout属性对象
            params.topMargin = location[1] - statusBarHeight;
            params.leftMargin = location[0];
            mCopyView.setLayoutParams(params);
            holder.parentLayout.addView(mCopyView);    //加入父View中
            mCopyView.setbg(url);

            int[] loc = new int[2];
            mCopyView.getLocationOnScreen(loc);
            //平移动画
            TranslateAnimation translateAnimation = new TranslateAnimation(
                    //X轴初始位置
                    loc[0],
                    //y轴开始位置
                    location_end[0] - location[0],
                    //X轴移动的结束位置
                    loc[1] - statusBarHeight,
                    //y轴移动后的结束位置
                    location_end[1] - location[1] - statusBarHeight + height / 2);
            //3秒完成动画
            translateAnimation.setDuration(1000);

            ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 0.5f, 1f, 0.5f);
            //3秒完成动画
            scaleAnimation.setDuration(800);

            animationSet = new AnimationSet(true);
            animationSet.addAnimation(scaleAnimation);
            animationSet.addAnimation(translateAnimation);

            mCopyView.startAnimation(animationSet);
            animationSet.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    //会导致parent边移除子视图边加子视图，形成管理混乱。所以要加上：
                    new Handler().post(new Runnable() {
                        public void run() {
                            if (mCopyView.getParent() == holder.parentLayout) {
                                holder.parentLayout.removeView(mCopyView);
                            }
                        }
                    });
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }
}
