package com.jhzy.nursinghandover.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jhzy.nursinghandover.R;
import com.jhzy.nursinghandover.api.EldersInfoListener;
import com.jhzy.nursinghandover.api.OnClickListenerNoDouble;
import com.jhzy.nursinghandover.beans.BedHomepageInfo;
import com.jhzy.nursinghandover.beans.BedHomepageInfoDataBean;
import com.jhzy.nursinghandover.beans.BedOrDoor;
import com.jhzy.nursinghandover.beans.NFCBean;
import com.jhzy.nursinghandover.model.lock.Elders;
import com.jhzy.nursinghandover.model.lock.Lock;
import com.jhzy.nursinghandover.model.lock.LockCode;
import com.jhzy.nursinghandover.model.loginnfc.StaffLoginCode;
import com.jhzy.nursinghandover.ui.adapters.OrdersInfoAdapter;
import com.jhzy.nursinghandover.utils.AppUtils;
import com.jhzy.nursinghandover.utils.CONTACT;
import com.jhzy.nursinghandover.utils.CustomDialogutils;
import com.jhzy.nursinghandover.utils.GetSign;
import com.jhzy.nursinghandover.utils.HttpUtils;
import com.jhzy.nursinghandover.utils.ImageLoaderUtils;
import com.jhzy.nursinghandover.utils.LoginPopWindowUtil;
import com.jhzy.nursinghandover.utils.NFCUtils;
import com.jhzy.nursinghandover.utils.NetUtils;
import com.jhzy.nursinghandover.utils.SPUtils;
import com.jhzy.nursinghandover.utils.ScreenBritnessUtil;
import com.jhzy.nursinghandover.utils.UpdateDialogUtils;
import com.jhzy.nursinghandover.widget.Circle;
import com.jhzy.nursinghandover.widget.DotTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 护理app 锁屏界面
 */
public class MainActivity extends BaseActivity {
    private Intent intent;
    private ViewHolder vh;
    //老人信息的适配器
    private OrdersInfoAdapter adapter;

    private HttpUtils httpUtils;

    public static List<Elders> eldersList;
    //  再按一次退出
    private long exitTime = 0;
    //弹出框对象
    private CustomDialogutils customDialogutils;
    //床数量
    private int bedCount = 0;
    private BedHomepageInfoDataBean data;


    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        ScreenBritnessUtil.setScreenBritness(getWindow());
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public void init() {
        initUtils();
        initView();
        initListener();
//        checkDoorOrBed();
    }


    /**
     * 是否是门口或是床头PAD
     */
    private void checkDoorOrBed() {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("deviceID", SPUtils.find(CONTACT.IMEI));
        map.put("sign", GetSign.GetSign(map, SPUtils.find(CONTACT.TOKEN)));
        HttpUtils.getInstance()
                .getRetrofitApi()
                .checkBedOrDoor("basic " + SPUtils.find(CONTACT.TOKEN), map)
                .enqueue(
                        new Callback<BedOrDoor>() {
                            @Override
                            public void onResponse(Call<BedOrDoor> call, Response<BedOrDoor> response) {
                                BedOrDoor body = response.body();
                                if (body != null && CONTACT.DataSuccess.equals(body.getCode())) {
                                    if (body.getData() == null || body.getData().getBedCount() == 0) {
                                        bedCount = 0;
                                    } else {
                                        bedCount = body.getData().getBedCount();
                                    }
                                    switchBedOrDoor();
                                }
                            }


                            @Override
                            public void onFailure(Call<BedOrDoor> call, Throwable t) {

                            }
                        });
    }


    private void switchBedOrDoor() {
        switch (bedCount) {
            case 0:
                Intent intent = new Intent(mContext, BindActivity.class);
                startActivity(intent);
                break;
            case 1:
                Log.e("rxf", "bed");
                vh.bedView.setVisibility(View.VISIBLE);
                vh.doorView.setVisibility(View.GONE);
                loadBed();
                break;
            default:
                Log.e("rxf", "door");
                vh.doorView.setVisibility(View.VISIBLE);
                vh.bedView.setVisibility(View.GONE);
                network();
        }
    }


    /**
     * 加载床头老人信息
     */
    private void loadBed() {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("deviceID", SPUtils.find(CONTACT.IMEI));
        map.put("sign", GetSign.GetSign(map, SPUtils.find(CONTACT.TOKEN)));
        HttpUtils.getInstance()
                .getRetrofitApi()
                .getBedElderInfo("basic " + SPUtils.find(CONTACT.TOKEN), map)
                .enqueue(
                        new Callback<BedHomepageInfo>() {
                            @Override
                            public void onResponse(Call<BedHomepageInfo> call, Response<BedHomepageInfo> response) {
                                vh.refreshBed.setRefreshing(false);
                                BedHomepageInfo body = response.body();
                                if (body != null && CONTACT.DataSuccess.equals(body.getCode())) {
                                    data = body.getData();
                                    String elderName = data.getElderName();
                                    ImageLoaderUtils.load(vh.bedIcon, data.getPhotoUrl());
                                    vh.bedName.setText(elderName);
                                    vh.bedAge.setText(data.getAge() + "岁");
                                    vh.bedLv.setText(data.getCareLevelTitle());
                                    vh.bedFood.setContent(data.getFoodCategoryies());
                                    vh.bedAtt.setContent(data.getAttention());
                                    vh.bedDrug.setContent(data.getMedicineNote());
                                    vh.bedMedical.setContent(data.getSicknessNote());
                                    vh.bedSpecial.setContent(data.getCareNote());
                                    vh.bedTime.setText(changeFormat(data.getCheckInDate()));
                                    vh.bedNo.setText(data.getBedTitle());

                                    String sicknessCategory = data.getSicknessCategory();
                                    vh.nursingLevelLayout.removeAllViews();
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
                                            vh.nursingLevelLayout.addView(view);
                                        }
                                    }

                                } else if (body != null) {
                                    Toast.makeText(mContext, body.getMsg(), Toast.LENGTH_SHORT).show();
                                    data = null;
                                } else {
                                    Toast.makeText(mContext, "服务器数据异常", Toast.LENGTH_SHORT).show();
                                    data = null;
                                }
                            }


                            @Override
                            public void onFailure(Call<BedHomepageInfo> call, Throwable t) {
                                vh.refreshBed.setRefreshing(false);
                                t.printStackTrace();
                                if (!NetUtils.isNetworkAvalible(mContext)) {
                                    Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(mContext, "服务器数据异常", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
    }


    private String changeFormat(String text) {
        if (text.length() < 11) {
            return "暂无入住信息";
        }
        String time = "";
        String pat1 = "yyyy-MM-dd HH:mm:ss";
        String pat2 = "yyyy年MM月dd日 HH时mm分ss秒";
        SimpleDateFormat sdf1 = new SimpleDateFormat(pat1); // 实例化模板对象
        SimpleDateFormat sdf2 = new SimpleDateFormat(pat2);
        Date d = null;
        try {
            d = sdf1.parse(text); // 将给定的字符串中的日期提取出来
            String format = sdf2.format(d);
            time = format.substring(0, 11);
        } catch (Exception e) { // 如果提供的字符串格式有错误，则进行异常处理
            e.printStackTrace(); // 打印异常信息
        }
        return time + "入住";

    }


    @Override
    protected void onResume() {
        super.onResume();
        vh.screensaverNotice.setVisibility(View.INVISIBLE);
        vh.version.setVisibility(View.INVISIBLE);
        checkDoorOrBed();
        startNFCReader();
        CONTACT.type = CONTACT.NFCTAG.TAG_MAINACTIVITY;
        ScreenBritnessUtil.setScreenBritness(getWindow());
        vh.version.setText("版本号:V"+ getVersion(mContext));
    }
    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "版本号获取失败";
        }
    }

    /**
     * 设置NFC读卡器参数，将服务修改为不休眠服务
     */
    public void setNFCData() {
        intent = new Intent("com.xishua.integration.ReaderService");
        intent.setPackage(getPackageName());
        int op = 1;
        Bundle bundle = new Bundle();
        bundle.putString("comType", "sound");
        bundle.putBoolean("powerSaveMode", false);
        // bundle.putBoolean("readUidOnly", false);
        bundle.putBoolean("readUidOnly", true);
        bundle.putInt("searchCardTimeout", 0);
        bundle.putInt("getPowerInterval", 10000);
        bundle.putInt("op", op);
        intent.putExtras(bundle);
        startService(intent);
    }


    //订阅事件FirstEvent
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(NFCBean nfc) {
        if (nfc != null && "NFC".equals(nfc.getFun())) {
            Log.e("-----------", "onEventMainThread: ");
            vh.searchEdittext.setText(nfc.getNfc());
            brushNFC(nfc.getNfc(), "");
            Log.e("---------", "onEventMainThread: " + nfc.getNfc());
            ScreenBritnessUtil.setScreenBritness(getWindow());
        }
    }


    /**
     * 开启NFC读卡器
     */
    public void startNFCReader() {
        intent = new Intent("com.xishua.integration.ReaderService");
        intent.setPackage(getPackageName());
        Bundle bundle = new Bundle();
        bundle.putInt("op", 4);
        intent.putExtras(bundle);
        startService(intent); // startService
        Log.e("123", "开启服务");
        mHandler.sendEmptyMessageDelayed(0, 3000);
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        if (intent != null) {
            stopService(intent);
        }
        super.onDestroy();
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int num = msg.what;
            switch (num) {
                case 0:
                    // TODO: 2017/5/10  耳机式NFC
                    //setNFCData();
                    Log.e("123", "设置NFC读卡器服务为不休眠模式");
                    break;
            }

        }
    };


    private void initUtils() {
        EventBus.getDefault().register(this);
        vh = new ViewHolder();
        httpUtils = HttpUtils.getInstance();
        customDialogutils = new CustomDialogutils(this);
    }


    public class ViewHolder {
        private final SimpleDraweeView bedIcon;
        private final TextView bedName;
        private final TextView bedAge;
        private final TextView bedNo;
        private final TextView bedLv;
        private final TextView bedTime;
        private final DotTextView bedFood;
        private final DotTextView bedSpecial;
        private final DotTextView bedMedical;
        private final DotTextView bedDrug;
        private final DotTextView bedAtt;
        private final View bedView;
        private final View doorView;
        private TextView search; // 查询
        private DrawerLayout mDrawerLayout;
        private EditText searchEdittext;  // 输入框
        private RecyclerView recycler; //老人信息展示 列表
        private NavigationView navigationView;
        public View line;
        public TextView dutynurse; // 当班护理员
        public ImageView tv_door_change;//菜单按钮
        private SwipeRefreshLayout swipeLayout;
        private View setting_location;
        private View setting_update;
        private View setting_loginout;
        private SwipeRefreshLayout refreshBed;
        private View screensaverNotice;
        private LinearLayout nursingLevelLayout;
        private TextView version;
        private View attentionTitle;


        ViewHolder() {
            search = ((TextView) findViewById(R.id.tv_door_top_confirm));
            searchEdittext = ((EditText) findViewById(R.id.et_door_search));
            recycler = ((RecyclerView) findViewById(R.id.rv_door_elder));
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            View headView = navigationView.getHeaderView(0);
            line = findViewById(R.id.line);
            dutynurse = ((TextView) findViewById(R.id.duty_nurse));
            tv_door_change = (ImageView) findViewById(R.id.tv_door_change);
            swipeLayout = ((SwipeRefreshLayout) findViewById(R.id.layout_swipe));
            setting_location = headView.findViewById(R.id.setting_location);
            setting_update = headView.findViewById(R.id.setting_update);
            setting_loginout = headView.findViewById(R.id.setting_loginout);
            doorView = findViewById(R.id.door_view);

            //bed控件
            bedView = findViewById(R.id.bed_view);
            bedIcon = ((SimpleDraweeView) findViewById(R.id.screensaver_icon));
            bedName = ((TextView) findViewById(R.id.screensaver_name));
            bedAge = ((TextView) findViewById(R.id.screensaver_age));
            bedNo = ((TextView) findViewById(R.id.screensaver_bedno));
            bedLv = ((TextView) findViewById(R.id.screensaver_nursing_lv));
            bedTime = ((TextView) findViewById(R.id.screensaver_time));
            bedFood = ((DotTextView) findViewById(R.id.screensaver_food_note));
            bedSpecial = ((DotTextView) findViewById(R.id.screensaver_special_note));
            bedMedical = ((DotTextView) findViewById(R.id.screensaver_medical_note));
            bedDrug = ((DotTextView) findViewById(R.id.screensaver_drug_note));
            bedAtt = ((DotTextView) findViewById(R.id.screensaver_note_att));
            refreshBed = ((SwipeRefreshLayout) findViewById(R.id.iv_refresh_bed));
            screensaverNotice = findViewById(R.id.screensaver_notice);
            nursingLevelLayout = ((LinearLayout) findViewById(R.id.nurse_lever_layout));
            version = ((TextView) findViewById(R.id.version));
            attentionTitle = findViewById(R.id.attention_title);
        }
    }


    private void network() {
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("DeviceID", SPUtils.find(CONTACT.IMEI));
        treeMap.put("sign", GetSign.GetSign(treeMap, SPUtils.find(CONTACT.TOKEN)));
        httpUtils.getRetrofitApi()
                .getLockInfo("basic " + SPUtils.find(CONTACT.TOKEN), treeMap)
                .enqueue(new Callback<LockCode>() {
                    @Override
                    public void onResponse(Call<LockCode> call, Response<LockCode> response) {
                        vh.swipeLayout.setRefreshing(false);
                        LockCode body = response.body();

                        if (body != null && body.getCode().equals(CONTACT.DataSuccess)) {
                            Lock data = body.getData();
                            //老人的数据集合
                            eldersList = data.getElders();
                            //责任护工的数据集合

                            //加载老人头像数据
                            loadElderData();
                            //加载当班护理员
                            loadStaffsData(data.getStaffs());
                        } else if (body != null) {
                            String msg = body.getMsg();
                            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                            adapter.clear();
                        } else {
                            Toast.makeText(mContext, "服务器异常", Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onFailure(Call<LockCode> call, Throwable t) {
                        vh.swipeLayout.setRefreshing(false);
                        t.printStackTrace();
                        if (!AppUtils.isNetworkConnected(mContext)) {
                            Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "服务器异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


    //加载当班护理员
    private void loadStaffsData(String staffs) {
        if (TextUtils.isEmpty(staffs)) {
            staffs = "暂无";
        }
        vh.dutynurse.setText("责任护工:" + staffs);
    }


    //加载老人头像数据
    private void loadElderData() {
        adapter.setDatas(eldersList);
    }


    /**
     * 初始化监听
     */
    private void initListener() {
        vh.refreshBed.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                vh.refreshBed.setRefreshing(true);
                loadBed();
            }
        });
        vh.attentionTitle.setOnClickListener(onClickListenerNoDouble);
        vh.bedIcon.setOnClickListener(onClickListenerNoDouble);
        vh.search.setOnClickListener(onClickListenerNoDouble);
        adapter.setEldersInfoListener(eldersInfoListener);
        vh.tv_door_change.setOnClickListener(onClickListenerNoDouble);
        vh.setting_location.setOnClickListener(onClickListenerNoDouble);
        vh.setting_update.setOnClickListener(onClickListenerNoDouble);
        vh.setting_loginout.setOnClickListener(onClickListenerNoDouble);
        vh.swipeLayout.setOnRefreshListener(onRefreshListener);
        vh.searchEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }


            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }


            @Override
            public void afterTextChanged(Editable editable) {
                if (editable != null) {
                    String s = editable.toString();
                    if (s.endsWith(";") && s.length() > 2) {
                        String substring = s.substring(0, s.length() - 1);
                        String toStr = NFCUtils.toStr2(substring);
                        brushNFC(toStr, "");
                        ScreenBritnessUtil.setScreenBritness(getWindow());
                    }
                }
            }
        });
    }


    private void initView() {
        adapter = new OrdersInfoAdapter(mContext, vh);
        GridLayoutManager manager = new GridLayoutManager(mContext, 5);
        vh.recycler.setLayoutManager(manager);
        vh.recycler.setAdapter(adapter);
        //设置卷内的颜色
        vh.swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }


    /**
     * 点击监听
     */
    OnClickListenerNoDouble onClickListenerNoDouble = new OnClickListenerNoDouble() {
        @Override
        public void myOnClick(View view) {
            switch (view.getId()) {
                case R.id.tv_door_top_confirm:   //搜索按钮
                    hideKeyBoard(view);
                    brushNFC("", vh.searchEdittext.getText().toString());    //验证NFC或者工号
                    break;

                case R.id.tv_door_change://菜单按钮
                    vh.mDrawerLayout.openDrawer(GravityCompat.START);
                    break;

                case R.id.setting_location:
                    menuType = 0;
                    LoginPopWindowUtil.showLoginPopWindow(MainActivity.this,
                            findViewById(R.id.drawer_layout), getWindow(), loginPopWindowListener);
                    break;

                case R.id.setting_update:
                    menuType = 1;
//                    LoginPopWindowUtil.showLoginPopWindow(MainActivity.this,
//                            findViewById(R.id.drawer_layout), getWindow(), loginPopWindowListener);
                    new UpdateDialogUtils(MainActivity.this).show();
                    break;

                case R.id.setting_loginout:
                    menuType = 2;
                    LoginPopWindowUtil.showLoginPopWindow(MainActivity.this,
                            findViewById(R.id.drawer_layout), getWindow(), loginPopWindowListener);
                    break;
                case R.id.screensaver_icon:// 20s把老人的病程隐藏
                    vh.screensaverNotice.setVisibility(View.VISIBLE);
                    handler.removeMessages(1);
                    Message message = handler.obtainMessage(1);     // Message
                    handler.sendMessageDelayed(message, 5 * 1000);
                    break;
                case R.id.attention_title:
                    vh.version.setVisibility(View.VISIBLE);
                    handler.removeMessages(2);
                    Message message1 = handler.obtainMessage(2);     // Message
                    handler.sendMessageDelayed(message1, 2 * 1000);
                    break;
            }
        }
    };

    Handler handler = new Handler() {

        public void handleMessage(Message msg) {         // handle message
            switch (msg.what) {
                case 1:
                    vh.screensaverNotice.setVisibility(View.INVISIBLE);
                    break;
                case 2:
                    vh.version.setVisibility(View.INVISIBLE);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    int menuType = -1;
    LoginPopWindowUtil.LoginPopWindowListener loginPopWindowListener
            = new LoginPopWindowUtil.LoginPopWindowListener() {
        @Override
        public void loginSucceed(String id) {
            vh.mDrawerLayout.closeDrawers();
            switch (menuType) {
                case 0://位置绑定
                    Intent intent = new Intent();
                    intent.setClass(mContext, BindActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                    break;
                case 1://版本更新
                    new UpdateDialogUtils(MainActivity.this).show();
                    break;
                case 2://登陆出去
                    SPUtils.delete(CONTACT.ACCOUNT);
                    SPUtils.delete(CONTACT.PASSWORD);
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                    break;
            }

        }


        @Override
        public void loginFailed() {

        }
    };


    //验证NFC或者工号
    private void brushNFC(String nfc, final String jobNo) {
        //debug模式
//        if (CONTACT.Debug) {
//            Toast.makeText(mContext, "nfc=" + nfc, Toast.LENGTH_SHORT).show();
//        }
        //判断工号是否为空
        if (TextUtils.isEmpty(jobNo) && TextUtils.isEmpty(nfc)) {
            Toast.makeText(mContext, "请输入工号", Toast.LENGTH_SHORT).show();
            return;
        }
        customDialogutils.setResfreshDialog("正在登录", 15 * 1000);
        TreeMap<String, String> map = new TreeMap<>();
        map.put("DeviceID", nfc);
        map.put("JobNo", jobNo);
        map.put("sign", GetSign.GetSign(map, SPUtils.find(CONTACT.TOKEN)));
        //验证账号
        httpUtils.getRetrofitApi()
                .loginNFC("basic " + SPUtils.find(CONTACT.TOKEN), map)
                .enqueue(new Callback<StaffLoginCode>() {
                    @Override
                    public void onResponse(Call<StaffLoginCode> call, Response<StaffLoginCode> response) {
                        vh.searchEdittext.setText("");
                        StaffLoginCode body = response.body();
                        if (body != null && CONTACT.DataSuccess.equals(body.getCode())) {
                            customDialogutils.dismissDialog();

                            if (bedCount == 0) {
                                Toast.makeText(mContext, "请先绑定床位", Toast.LENGTH_SHORT).show();
                            } else if (bedCount == 1) {
                                if (data == null || body.getData() == null || TextUtils.isEmpty(body.getData().getStaffCode())) {
                                    Toast.makeText(mContext, "首页数据异常！", Toast.LENGTH_SHORT).show();
                                } else {
                                    Bundle bundle = new Bundle();
                                    bundle.putParcelable("data", data);
                                    bundle.putString("workNo", body.getData().getStaffCode());
                                    BedNextActivity.start(mContext, bundle);
                                }
                            } else {
                                if (body.getData().getStaffLevel() == 2) {//领导查看界面
                                    Intent intent = new Intent(mContext, RoomActivity.class);
                                    intent.putExtra(RoomActivity.TAG, body.getData().getStaffCode());
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(mContext, NextActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString(NextActivity.TAG, body.getData().getStaffCode());
                                    bundle.putSerializable(NextActivity.TAG1,
                                            (Serializable) eldersList);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            }
                        } else if (body != null) {
                            customDialogutils.cancelNetworkDialog(body.getMsg(), false);
                        } else {
                            customDialogutils.cancelNetworkDialog("登录失败", false);

                        }
                    }


                    @Override
                    public void onFailure(Call<StaffLoginCode> call, Throwable t) {
                        vh.searchEdittext.setText("");
                        t.printStackTrace();
                        if (!AppUtils.isNetworkConnected(mContext)) {
                            customDialogutils.cancelNetworkDialog("登录失败，网络异常", false);
                        } else {
                            customDialogutils.cancelNetworkDialog("登录失败，服务器异常", false);
                        }
                    }
                });
    }


    /**
     * 点击老人头像的
     */
    EldersInfoListener eldersInfoListener = new EldersInfoListener() {
        @Override
        public void eldersInfoListener(int position) {
            //Toast.makeText(mContext, position + "", Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 下拉刷新
     */
    SwipeRefreshLayout.OnRefreshListener onRefreshListener
            = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            vh.swipeLayout.setRefreshing(true);
            network();
        }
    };


    /**
     * 返回键重写
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//            if ((System.currentTimeMillis() - exitTime) > 2000) {
//                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
//                exitTime = System.currentTimeMillis();
//            } else {
//                finish();
//                System.exit(0);
//            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 收起键盘
     */
    private void hideKeyBoard(View v) {
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}
