package com.jhzy.nursinghandover.ui;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jhzy.nursinghandover.R;
import com.jhzy.nursinghandover.api.OnClickListenerNoDouble;
import com.jhzy.nursinghandover.beans.BedHomepageInfoDataBean;
import com.jhzy.nursinghandover.beans.CurrentSelectedTask;
import com.jhzy.nursinghandover.beans.nextItem.CommonBean;
import com.jhzy.nursinghandover.beans.nextItem.CommonItem;
import com.jhzy.nursinghandover.beans.nextItem.EldersBean;
import com.jhzy.nursinghandover.beans.nextItem.NextDetail;
import com.jhzy.nursinghandover.beans.nextItem.NurseBean;
import com.jhzy.nursinghandover.beans.nextItem.ScoreBean;
import com.jhzy.nursinghandover.beans.nextItem.SubItem;
import com.jhzy.nursinghandover.ui.adapters.CommonItemAdapter;
import com.jhzy.nursinghandover.ui.adapters.CompletedTaskAdapter;
import com.jhzy.nursinghandover.ui.adapters.GalleryAdapter;
import com.jhzy.nursinghandover.utils.AppUtils;
import com.jhzy.nursinghandover.utils.BedPopwindowUtils;
import com.jhzy.nursinghandover.utils.CONTACT;
import com.jhzy.nursinghandover.utils.CustomDialogutils;
import com.jhzy.nursinghandover.utils.GetSign;
import com.jhzy.nursinghandover.utils.HttpUtils;
import com.jhzy.nursinghandover.utils.ImageLoaderUtils;
import com.jhzy.nursinghandover.utils.NetUtils;
import com.jhzy.nursinghandover.utils.SPUtils;
import com.jhzy.nursinghandover.utils.ScreenBritnessUtil;
import com.jhzy.nursinghandover.utils.ScreenUtils;
import com.jhzy.nursinghandover.utils.TimeParseUtils;
import com.jhzy.nursinghandover.widget.Circle;
import com.jhzy.nursinghandover.widget.ImageMaskView;
import com.jhzy.nursinghandover.widget.MyGridView;
import com.jhzy.nursinghandover.widget.TaskView;

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
 * bigyu 床头界面
 */
public class BedNextActivity extends BaseActivity {


    private ViewHolder vh;
    private GalleryAdapter galleryAdapter;
    private int lastPosition = 0;
    private View lastView;
    //是否第一次进入该界面
    private boolean isFirstIn = true;
    private BedPopwindowUtils bedPopwindowUtils;
    //   常用护理项目的适配器
    private CommonItemAdapter commonItemAdapter;
    //   常用护理项目的数据源
    private List<CommonBean> commonData;
    private HttpUtils httpUtils;
    private String token;

    private String workNo;
    private String EID;
    private List<NurseBean> nurse;

    private int statusBarHeight;
    private CompletedTaskAdapter completedTaskAdapter;

    private List<String> photos;
    private CustomDialogutils customDialogutils;

    private CurrentSelectedTask currentSelectedTask;
    //左边布局的宽度
    private int leftLayoutMeasuredWidth;
    private BedHomepageInfoDataBean eldersData;

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.setClass(context, BedNextActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getContentView() {
        return (R.layout.activity_bed_next);
    }

    @Override
    public void init() {
        super.init();
        initParams();
        initUtils();
        initView();
        initListener();
        loadCommon();
        loadAttentionLayout(vh.attentionLayout, 15f);
        netWork();
        loadElderInfo();
    }

    /**
     * 加载老人的信息
     */
    private void loadElderInfo() {
        //老人的头像
        String photoUrl = eldersData.getPhotoUrl();
        //老人的名字
        String elderName = eldersData.getElderName();
        //老人的年龄
        int age = eldersData.getAge();
        //老人房间
        String bedTitle = eldersData.getBedTitle();
        //老人护理等级的文字
        String careLevelTitle = eldersData.getCareLevelTitle();
        //老人护理任务的数值
        String sicknessCategory = eldersData.getSicknessCategory();

        ImageLoaderUtils.load(vh.headIcon, photoUrl);
        vh.name.setText(elderName);
        vh.ageAndBed.setText(age + "岁 | " + bedTitle);
        vh.nurseLever.setText(careLevelTitle);

        vh.nurseLeverLayout.removeAllViews();
        if (!TextUtils.isEmpty(sicknessCategory)) {
            String[] split = sicknessCategory.split("\\|");
            for (int i = 0; i < split.length; i++) {
                String s = split[i];
                String[] strings = s.split(",");
                if (strings.length < 2) {
                    continue;
                }
                View view = LayoutInflater.from(mContext).inflate(R.layout.circle_item, null);
                Circle circle = (Circle) view.findViewById(R.id.circle);
                circle.setColor(strings[1]);
                circle.setNum(strings[0]);
                vh.nurseLeverLayout.addView(view);
            }
        }
    }

    private void initParams() {
        token = SPUtils.find(CONTACT.TOKEN);
        Bundle extras = getIntent().getExtras();
        eldersData = ((BedHomepageInfoDataBean) extras.getParcelable("data"));
        if (eldersData == null) {
            Toast.makeText(mContext, "发生未知错误", Toast.LENGTH_SHORT).show();
            finish();
        }
        EID = String.valueOf(eldersData.getElderID());
        workNo = extras.getString("workNo");
    }

    private void netWork() {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("JobNo", workNo);
        map.put("DeviceID", SPUtils.find(CONTACT.IMEI));
        map.put("sign",GetSign.GetSign(map,SPUtils.find(CONTACT.TOKEN)));
        httpUtils.getRetrofitApi().toNext("basic " + token, map).enqueue(new Callback<NextDetail>() {
            @Override
            public void onResponse(Call<NextDetail> call, Response<NextDetail> response) {
                NextDetail body = response.body();
                if (body != null && CONTACT.DataSuccess.equals(body.getCode())) {
                    nurse = body.getData().getNurse();
                    galleryAdapter.setList(nurse);
                    jumpToCurrentTimeTask();
                } else {
                    Toast.makeText(mContext, "服务器异常", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NextDetail> call, Throwable t) {
                t.printStackTrace();
                if (!AppUtils.isNetworkConnected(mContext)) {
                    Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "服务器异常", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 跳转到当前时间任务那一项
     */
    private void jumpToCurrentTimeTask(){
        if (nurse == null){
            return;
        }
        int size = nurse.size();
        for (int i = 0; i < size; i++) {
            NurseBean nurseBean = nurse.get(i);
            String startTime = nurseBean.getStartTime();
            startTime= TimeParseUtils.getDateDate() + " " + startTime;
            long startMillion = TimeParseUtils.getTimeMillion2(startTime);
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis <= startMillion){
                vh.gallery.setSelection(i , true);
                break;
            }
        }
    }


    /**
     * 加载日常护理
     */
    private void loadCommon() {
        vh.commonRv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        commonItemAdapter = new CommonItemAdapter();

        commonItemAdapter.setClick(commonClick);
        vh.commonRv.setAdapter(commonItemAdapter);
        TreeMap<String,String> map = new TreeMap<>();
        map.put("sign",GetSign.GetSign(map,SPUtils.find(CONTACT.TOKEN)));
        HttpUtils.getInstance().getRetrofitApi().getCommon("basic " + SPUtils.find(CONTACT.TOKEN)).enqueue(
                new Callback<CommonItem>() {
                    @Override
                    public void onResponse(Call<CommonItem> call, Response<CommonItem> response) {
                        CommonItem body = response.body();
                        if (body != null && CONTACT.DataSuccess.equals(body.getCode())) {
                            commonData = body.getData();
                            commonItemAdapter.setDatas(commonData);
                        }
                    }


                    @Override
                    public void onFailure(Call<CommonItem> call, Throwable t) {
                        t.printStackTrace();
                        if (NetUtils.isNetworkAvalible(mContext)) {
                            Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "服务器异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean isFirst = true;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && isFirst) {
            commonItemAdapter.setParentWidth(vh.rightLayout.getMeasuredWidth());
            leftLayoutMeasuredWidth = vh.leftLayout.getMeasuredWidth();
            isFirst = false;
        }
    }

    private void initListener() {
        vh.leftBtn.setOnClickListener(onClickListenerNoDouble);
        vh.rightBtn.setOnClickListener(onClickListenerNoDouble);
        vh.back.setOnClickListener(onClickListenerNoDouble);
        vh.gallery.setOnItemClickListener(onItemClickListener);
        vh.gallery.setOnItemSelectedListener(onItemSelectedListener);
        vh.attentionLayout.setOnClickListener(onClickListenerNoDouble);
        vh.lockScreen.setOnClickListener(onClickListenerNoDouble);
    }


    private void initView() {
        galleryAdapter = new GalleryAdapter(mContext);
        vh.gallery.setAdapter(galleryAdapter);

        completedTaskAdapter = new CompletedTaskAdapter(mContext);
        vh.myGridView.setAdapter(completedTaskAdapter);
    }

    private void initUtils() {
        statusBarHeight = ScreenUtils.getStatusBarHeight(mContext);
        vh = new ViewHolder();
        httpUtils = HttpUtils.getInstance();
        bedPopwindowUtils = new BedPopwindowUtils(mContext);
        photos = new ArrayList<>();
        customDialogutils = new CustomDialogutils(mContext);
    }


    private List<SubItem> makeGson(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<SubItem>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    //******************************************提交数据*********************************************************
    //准备提交的任务
    private String prepareTaskData(String CID, String title, String workNo, String EID, List<SubItem> subItems, TaskView[] views, String TID) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < views.length; i++) {
            if (views[i].getSelected()) {
                sb.append(subItems.get(i).getSubprojectName());
                sb.append(",");
            }
        }
        String str = sb.toString();
        if (!TextUtils.isEmpty(str) && str.endsWith(",")) {
            str = str.substring(0, str.length() - 1);
        }

        JSONObject jsonObject = new JSONObject();
        try {
            JSONArray jsonArray1 = new JSONArray();
            JSONObject o = new JSONObject();
            o.put("id", workNo);
            jsonArray1.put(o);

            jsonObject.put("CID", CID);
            jsonObject.put("CName", title);
            jsonObject.put("TID", TID);
            jsonObject.put("EID", EID);
            jsonObject.put("Sub", str);
            jsonObject.put("SID", jsonArray1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(jsonObject);
        String toString = jsonArray.toString();
        Log.e("----------", "prepareTaskData: " + toString);
        return toString;
    }

    private void submitTask(String type, String task, final NurseBean nurseBean) {
        customDialogutils.setResfreshDialog("正在提交数据...", 10 * 1000);
        TreeMap<String, String> map = new TreeMap<>();
        map.put("Type", type);
        map.put("Task", task);
        map.put("sign", GetSign.GetSign(map, token));
        httpUtils.getRetrofitApi().submit("basic " + token, map).enqueue(new Callback<ScoreBean>() {
            @Override
            public void onResponse(Call<ScoreBean> call, Response<ScoreBean> response) {
                ScoreBean body = response.body();
                if (body != null && CONTACT.DataSuccess.equals(body.getCode())) {
                    customDialogutils.cancelNetworkDialog("完成", true);
                    if (nurseBean != null) {
                        List<EldersBean> elders = nurseBean.getElders();
                        if (elders.size() == 1) {
                            elders.get(0).setIsCompleted(1);
                        }
                    }
                    if (lastSelectedView != null) {
                        lastSelectedView.isCheck(true);
                        lastSelectedView = null;
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (currentSelectedTask != null) {
                                completedTaskAdapter.setPhoto(currentSelectedTask.getUrl());
                                loadAnimation(currentSelectedTask.getView(), currentSelectedTask.getWidth(), currentSelectedTask.getHeight(), currentSelectedTask.getUrl());
                                currentSelectedTask = null;
                            }
                        }
                    }, 1500);
                } else {
                    customDialogutils.cancelNetworkDialog("服务器异常", false);
                }
            }

            @Override
            public void onFailure(Call<ScoreBean> call, Throwable t) {
                t.printStackTrace();
                if (NetUtils.isNetworkAvalible(mContext)) {
                    customDialogutils.cancelNetworkDialog("服务器异常", false);
                } else {
                    customDialogutils.cancelNetworkDialog("网络异常", false);
                }
            }
        });
    }

    //******************************************加载数据****************************************************

    /**
     * 注意事项
     */
    private void loadAttentionLayout(LinearLayout attentionLayout, float size) {
        int childCount = attentionLayout.getChildCount();
        if (childCount > 0) {
            attentionLayout.removeAllViews();
        }
        String attention = eldersData.getAttention();
        String sicknessCategory = eldersData.getSicknessCategory();
        String foodCategoryies = eldersData.getFoodCategoryies();
        String careNote = eldersData.getCareNote();
        String medicineNote = eldersData.getMedicineNote();
        String sicknessNote = eldersData.getSicknessNote();
        ArrayList<String> attentions = new ArrayList<>();
        if (!TextUtils.isEmpty(attention)) {
            attentions.add(attention);
        }
        if (!TextUtils.isEmpty(foodCategoryies)) {
            attentions.add(foodCategoryies);
        }
        if (!TextUtils.isEmpty(careNote)) {
            attentions.add(careNote);
        }
        if (!TextUtils.isEmpty(medicineNote)) {
            attentions.add(medicineNote);
        }
        if (!TextUtils.isEmpty(sicknessNote)) {
            attentions.add(sicknessNote);
        }

        for (int i = 0; i < attentions.size(); i++) {
            View oneAttention = LayoutInflater.from(mContext).inflate(R.layout.item_attention_context, attentionLayout, false);
            TextView attentionContext = (TextView) oneAttention.findViewById(R.id.attention_context);
            attentionContext.setTextSize(size);
            attentionContext.setText(attentions.get(i));
            attentionLayout.addView(oneAttention);
        }
    }

    //加载服务子项的内容
    private void loadSubItemData(BedPopwindowUtils.ViewHolder vh, List<SubItem> subItems, TaskView[] views, int size) {
        for (int i = 0; i < size; i++) {
            SubItem subItem = subItems.get(i);
            //标题
            views[i].setTitle(subItem.getSubprojectName());
            //图标
            views[i].setImg(subItem.getSubprojectIco());
        }
    }

    ////准备显示几个服务子项
    private void showSubItemView(TaskView[] views, int size) {
        for (int i = 0; i < size; i++) {
            views[i].setVisibility(View.VISIBLE);
        }
    }

    private void state(final TaskView[] views, final BedPopwindowUtils.ViewHolder viewHolder, final int size, final String CID, final String title, final List<SubItem> subItems, final String TID, final NurseBean nurseBean) {
        OnClickListenerNoDouble onClickListenerNoDouble = new OnClickListenerNoDouble() {
            @Override
            public void myOnClick(View view) {
                int id = view.getId();
                switch (id) {
                    case R.id.text1:
                        views[0].setSelected(!views[0].getSelected());
                        break;
                    case R.id.text2:
                        views[1].setSelected(!views[1].getSelected());
                        break;
                    case R.id.text3:
                        views[2].setSelected(!views[2].getSelected());
                        break;
                    case R.id.text4:
                        views[3].setSelected(!views[3].getSelected());
                        break;
                    case R.id.bed_detail_sure:
                        String prepareTaskData = prepareTaskData(CID, title, workNo, EID, subItems, views, TID);
                        if ("-1".equals(TID)) {
                            submitTask("2", prepareTaskData, null);
                        } else if (TextUtils.isEmpty(TID)) {
                            Toast.makeText(BedNextActivity.this, "服务异常，参数错误", Toast.LENGTH_SHORT).show();
                        } else {
                            submitTask("1", prepareTaskData, nurseBean);
                        }
                        viewHolder.popupWindow.dismiss();
                        break;
                }
                //改变提交按钮的点击状态
                boolean[] checkSelectedItem = checkSelectedItem(views, size);
                changeSubmitBtn(checkSelectedItem, viewHolder);
            }
        };
        viewHolder.txt1.setOnClickListener(onClickListenerNoDouble);
        viewHolder.txt2.setOnClickListener(onClickListenerNoDouble);
        viewHolder.txt3.setOnClickListener(onClickListenerNoDouble);
        viewHolder.txt4.setOnClickListener(onClickListenerNoDouble);
        viewHolder.bedDetailSure.setOnClickListener(onClickListenerNoDouble);
    }

    private int calculateSize(List<SubItem> subItems, TaskView[] views) {
        if (subItems == null){
            return 0;
        }
        int size = subItems.size();
        if (size > views.length) {
            size = views.length;
        }
        return size;
    }

    private boolean[] checkSelectedItem(TaskView[] views, int size) {
        boolean[] viewChecks = new boolean[size];
        for (int i = 0; i < size; i++) {
            boolean selected = views[i].getSelected();
            viewChecks[i] = selected;
        }
        return viewChecks;
    }

    private void changeSubmitBtn(boolean[] checkSelectedItem, BedPopwindowUtils.ViewHolder viewHolder) {
        for (int i = 0; i < checkSelectedItem.length; i++) {
            if (checkSelectedItem[i]) {
                viewHolder.sureImage.setBackgroundResource(R.mipmap.sure);
                viewHolder.sureTxt.setTextColor(mContext.getResources().getColor(R.color.white));
                viewHolder.bedDetailSure.setBackgroundColor(mContext.getResources().getColor(R.color.color_submit_can));
                viewHolder.bedDetailSure.setEnabled(true);
                return;
            }
        }
        viewHolder.bedDetailSure.setEnabled(false);
        viewHolder.sureImage.setBackgroundResource(R.mipmap.sure_ok);
        viewHolder.sureTxt.setTextColor(mContext.getResources().getColor(R.color.color_submit));
        viewHolder.bedDetailSure.setBackgroundColor(mContext.getResources().getColor(R.color.color_activity_bg));
    }
    //******************************************监听相关****************************************************
    /**
     * 监听
     */
    OnClickListenerNoDouble onClickListenerNoDouble = new OnClickListenerNoDouble() {
        @Override
        public void myOnClick(View view) {
            int id = view.getId();
            int position2 = vh.gallery.getSelectedItemPosition();
            switch (id) {
                case R.id.left_btn:
                    changeTask(position2, -1);
                    break;
                case R.id.right_btn:
                    changeTask(position2, 1);
                    break;
                case R.id.back:
                case R.id.lock_screen:
                    finish();
                    break;
                case R.id.attention_layout: //弹出注意事项的弹框
                    BedPopwindowUtils.ViewHolder viewHolder = bedPopwindowUtils.showAttention(view);
                    loadAttentionLayout(viewHolder.attentionLayout, 20f);
                    break;
            }
        }
    };

    private ImageMaskView lastSelectedView = null;
    /**
     * 点击任务子项
     */
    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (lastPosition == i) {

                NurseBean nurseBean = nurse.get(i);

                String TID = null;
                List<EldersBean> elders = nurseBean.getElders();
                for (int j = 0; j < elders.size(); j++) {
                    EldersBean eldersBean = elders.get(j);
                    int elderID = eldersBean.getElderID();
                    if (EID.equals(String.valueOf(elderID))) {
                        TID = String.valueOf(eldersBean.getTaskId());
                    }
                }
                //判断是否已完成 ， 如果已完成则不点开
                if (elders.size() == 1) {
                    int isCompleted = elders.get(0).getIsCompleted();
                    if (isCompleted != 0) {
                        return;
                    }
                }
                BedPopwindowUtils.ViewHolder viewHolder = bedPopwindowUtils.showTaskPop(view);
                TaskView[] views = new TaskView[]{viewHolder.txt1, viewHolder.txt2, viewHolder.txt3, viewHolder.txt4};
                //显示标题图标 和 标题文字
                String careItemTitle = nurseBean.getCareItemTitle();
                String CID = String.valueOf(nurseBean.getCareItemID());

                viewHolder.titleTxt.setText(careItemTitle);
                ImageLoaderUtils.load(viewHolder.titleIcon, nurseBean.getCareItemUrl());
                 //解析服务子项的集合
                String subproject = nurseBean.getSubproject();
                List<SubItem> subItems = makeGson(subproject);
                //判断子项的个数
                int size = calculateSize(subItems, views);
                //准备显示几个服务子项
                showSubItemView(views, size);
                //加载服务子项的内容
                loadSubItemData(viewHolder, subItems, views, size);


                //改变状态和监听事件
                state(views, viewHolder, size, CID, careItemTitle, subItems, TID, nurseBean);

                View pView = view.findViewById(R.id.icon);
                lastSelectedView = (ImageMaskView) pView;

                int width = pView.getMeasuredWidth() * 2 / 3;
                int height = pView.getMeasuredHeight() * 2 / 3;
                currentSelectedTask = new CurrentSelectedTask(nurseBean.getCareItemUrl(), pView, width, height);
            }
        }
    };
    AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
            View currentView = view.findViewById(R.id.layout);
            if (lastView != null) {
                if (lastView != currentView) {
                    galleryAnimation(currentView, 1f, 1f, 1.45f, 1.43f);
                    changeTaskState(currentView, true);
                }
                changeTaskState(lastView, false);
                galleryAnimation(lastView, 1.45f, 1.43f, 1f, 1f);
            } else {
                galleryAnimation(currentView, 1f, 1f, 1.45f, 1.43f);
                changeTaskState(currentView, true);
            }
            lastView = currentView;
            lastPosition = position;
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };
    /**
     * 常用护理的点击事件
     */
    CommonItemAdapter.CommonClick commonClick = new CommonItemAdapter.CommonClick() {
        @Override
        public void click(View view, int position) {
            BedPopwindowUtils.ViewHolder viewHolder = bedPopwindowUtils.showTaskPop(view);
            TaskView[] views = new TaskView[]{viewHolder.txt1, viewHolder.txt2, viewHolder.txt3, viewHolder.txt4};
            CommonBean commonBean = commonData.get(position);
            String subproject = commonBean.getSubproject();
            String CID = String.valueOf(commonBean.getID());

            //显示标题的图标 和 文字
            String careItemTitle = commonBean.getCareItemTitle();
            ImageLoaderUtils.load(viewHolder.titleIcon, commonBean.getCareItemUrl());
            viewHolder.titleTxt.setText(careItemTitle);

            //解析服务子项的集合
            List<SubItem> subItems = makeGson(subproject);
            //判断子项的个数
            int size = calculateSize(subItems, views);
            //准备显示几个服务子项
            showSubItemView(views, size);
            //加载服务子项的内容
            loadSubItemData(viewHolder, subItems, views, size);
            //改变状态和监听事件
            state(views, viewHolder, size, CID, careItemTitle, subItems, "-1", null);


            View pView = view.findViewById(R.id.img);

            currentSelectedTask = new CurrentSelectedTask(commonBean.getCareItemUrl(), pView, pView.getMeasuredWidth(), pView.getMeasuredHeight());
        }
    };

    private void loadAnimation(View view, int width, int height, String url) {
        //动画开始的位置
        int[] location = new int[2];
        view.getLocationOnScreen(location);

        int[] location_end = new int[2];
        int childCount = vh.myGridView.getChildCount();
        Log.e("--------", "loadAnimation: " + childCount);
        if (childCount == 0) {
            location_end[0] = 8;
            location_end[1] = 95;
        } else {
            View childAt = vh.myGridView.getChildAt(childCount - 1);
            int height1 = childAt.getHeight();
            int width1 = childAt.getWidth();
            childAt.getLocationOnScreen(location_end);
            if (childCount % 5 == 0) {
                location_end[0] = 8;
                location_end[1] = location_end[1] + height1;
            } else {
                location_end[0] = location_end[0] + width1 + 8;
                location_end[1] = location_end[1];
            }
        }
        //获取视图的宽度和高度

        //新建图片副本到父控件中
        final ImageMaskView mCopyView = new ImageMaskView(mContext);      //新建副本View
        mCopyView.setBackgroundColor(Color.RED);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);  //建立layout属性对象
        params.topMargin = location[1] - statusBarHeight;
        params.leftMargin = location[0];
        mCopyView.setLayoutParams(params);
        vh.parentLayout.addView(mCopyView);    //加入父View中
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

        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 0.3f, 1f, 0.3f);
        //3秒完成动画
        scaleAnimation.setDuration(800);

        animationSet = new AnimationSet(true);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(translateAnimation);

        mCopyView.startAnimation(animationSet);

        //清除动画
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //会导致parent边移除子视图边加子视图，形成管理混乱。所以要加上：
                new Handler().post(new Runnable() {
                    public void run() {
                        if (mCopyView.getParent() == vh.parentLayout) {
                            vh.parentLayout.removeView(mCopyView);
                        }
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    //******************************************任务的动画相关****************************************************
    private void changeTaskState(View view, boolean current) {
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView time = (TextView) view.findViewById(R.id.time);
        if (current) {
            title.setTextColor(mContext.getResources().getColor(R.color.black));
            time.setTextColor(mContext.getResources().getColor(R.color.color_subtitle_selector));
            galleryAnimation(title, 1f, 1f, 1.2f, 1.2f);
            galleryAnimation(time, 1f, 1f, 1.2f, 1.2f);
        } else {
            title.setTextColor(mContext.getResources().getColor(R.color.thunder));
            time.setTextColor(mContext.getResources().getColor(R.color.color_subtitle));
            galleryAnimation(title, 1.2f, 1.2f, 1f, 1f);
            galleryAnimation(time, 1.2f, 1.2f, 1f, 1f);
        }
    }

    /**
     * gallery动画, 服务项放大的效果
     *
     * @param layout
     * @param
     */
    public void galleryAnimation(View layout, float fromX, float formY, float toX, float toY) {
        PropertyValuesHolder x = PropertyValuesHolder.ofFloat("scaleX", fromX, toX);
        PropertyValuesHolder y = PropertyValuesHolder.ofFloat("scaleY", formY, toY);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(layout, x, y);
        objectAnimator.start();
    }

    /**
     * 切换护理任务 ， 产生动画效果
     *
     * @param position 当前任务的下标
     * @param isRight  滑动的方向
     */
    public void changeTask(int position, int isRight) {
        int num = position + isRight;
        int childCount = galleryAdapter.count();
        if (num < childCount && num >= 0) {
            taskAnimation(num, isRight);
        } else {
            Toast.makeText(mContext, "已滑到尽头", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * gallery 切换实现动画效果
     *
     * @param position
     * @param isRight
     */
    public void taskAnimation(int position, int isRight) {
        //实现动画效果
        if (isRight == 1) {
            if (position > 0) {
                position--; //先回退1个
                vh.gallery.setSelection(position, false); //注意这里必须是false，
                vh.gallery.onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null); //再模拟按下
            } else {
                vh.gallery.setSelection(position, false);
            }
        } else {
            if (position <= vh.gallery.getChildCount()) {
                position++; //先回退1个
                vh.gallery.setSelection(position, false); //注意这里必须是false，
                vh.gallery.onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT, null); //再模拟按下
            } else {
                vh.gallery.setSelection(position, false);
            }
        }
    }

    //******************************************注册控件****************************************************
    class ViewHolder {
        private Gallery gallery;
        private TextView name;
        private View leftBtn; //左按钮
        private View rightBtn; //右按钮
        private View back; //返回键
        private LinearLayout attentionLayout; //注意事项 布局
        private View lockScreen; // 锁屏按钮
        private RecyclerView commonRv;
        private LinearLayout rightLayout;
        private MyGridView myGridView;
        private RelativeLayout parentLayout;
        private LinearLayout leftLayout;
        private SimpleDraweeView headIcon;
        private TextView ageAndBed;
        private TextView nurseLever;
        private LinearLayout nurseLeverLayout;

        public ViewHolder() {
            back = findViewById(R.id.back);
            name = ((TextView) findViewById(R.id.name));
            leftBtn = findViewById(R.id.left_btn);
            rightBtn = findViewById(R.id.right_btn);
            gallery = ((Gallery) findViewById(R.id.gallery));
            attentionLayout = ((LinearLayout) findViewById(R.id.attention_layout));
            lockScreen = findViewById(R.id.lock_screen);
            commonRv = ((RecyclerView) findViewById(R.id.next_common_rv));
            rightLayout = ((LinearLayout) findViewById(R.id.right_layout));
            myGridView = ((MyGridView) findViewById(R.id.completed_grid_view));
            parentLayout = ((RelativeLayout) findViewById(R.id.activity_bed_next));
            leftLayout = ((LinearLayout) findViewById(R.id.left_layout));

            headIcon = ((SimpleDraweeView) findViewById(R.id.head_icon));
            ageAndBed = ((TextView) findViewById(R.id.age_and_bed));
            nurseLever = ((TextView) findViewById(R.id.nurse_lever));
            nurseLeverLayout = ((LinearLayout) findViewById(R.id.nurse_lever_layout));
        }
    }

    //锁屏时间
    private int lockTime = 60 * 1000;

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


    //**************************************bigyu动画***************************************************
    AnimationSet animationSet;

//    private void loadAnimation(View view, String url) {
//        if (view instanceof ImageMaskView) {
//            //获取视图在屏幕上的位置
//            int[] location = new int[2];
//            view.getLocationOnScreen(location);
//            ///获取 确认完成  按钮在屏幕的位置
//            int[] location_end = new int[2];
//            holder.confirm.getLocationOnScreen(location_end);
//
//            //获取视图的宽度和高度
//            int width = view.getWidth();
//            int height = view.getHeight();
//
//            final ImageMaskView mCopyView = new ImageMaskView(this);      //新建副本View
//            mCopyView.setBackgroundColor(Color.RED);
//            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);  //建立layout属性对象
//            params.topMargin = location[1] - statusBarHeight;
//            params.leftMargin = location[0];
//            mCopyView.setLayoutParams(params);
//            holder.parentLayout.addView(mCopyView);    //加入父View中
//            mCopyView.setbg(url);
//
//            int[] loc = new int[2];
//            mCopyView.getLocationOnScreen(loc);
//            //平移动画
//            TranslateAnimation translateAnimation = new TranslateAnimation(
//                    //X轴初始位置
//                    loc[0],
//                    //y轴开始位置
//                    location_end[0] - location[0],
//                    //X轴移动的结束位置
//                    loc[1] - statusBarHeight,
//                    //y轴移动后的结束位置
//                    location_end[1] - location[1] - statusBarHeight + height / 2);
//            //3秒完成动画
//            translateAnimation.setDuration(1000);
//
//            ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 0.5f, 1f, 0.5f);
//            //3秒完成动画
//            scaleAnimation.setDuration(800);
//
//            animationSet = new AnimationSet(true);
//            animationSet.addAnimation(scaleAnimation);
//            animationSet.addAnimation(translateAnimation);
//
//            mCopyView.startAnimation(animationSet);
//            animationSet.setAnimationListener(new Animation.AnimationListener() {
//                @Override
//                public void onAnimationStart(Animation animation) {
//
//                }
//
//                @Override
//                public void onAnimationEnd(Animation animation) {
//                    //会导致parent边移除子视图边加子视图，形成管理混乱。所以要加上：
//                    new Handler().post(new Runnable() {
//                        public void run() {
//                            if (mCopyView.getParent() == holder.parentLayout) {
//                                holder.parentLayout.removeView(mCopyView);
//                            }
//                        }
//                    });
//                }
//
//                @Override
//                public void onAnimationRepeat(Animation animation) {
//
//                }
//            });
//        }
//    }
}
