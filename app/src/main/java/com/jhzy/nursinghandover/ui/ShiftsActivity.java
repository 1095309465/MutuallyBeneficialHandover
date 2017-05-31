package com.jhzy.nursinghandover.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jhzy.nursinghandover.R;
import com.jhzy.nursinghandover.model.lock.Elders;
import com.jhzy.nursinghandover.model.lock.Lock;
import com.jhzy.nursinghandover.model.lock.LockCode;
import com.jhzy.nursinghandover.ui.adapters.ShiftAdapter;
import com.jhzy.nursinghandover.beans.JiaoBean;
import com.jhzy.nursinghandover.utils.AppUtils;
import com.jhzy.nursinghandover.utils.CONTACT;
import com.jhzy.nursinghandover.utils.CustomDialogutils;
import com.jhzy.nursinghandover.utils.GetSign;
import com.jhzy.nursinghandover.utils.HttpUtils;
import com.jhzy.nursinghandover.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 交接班
 */
public class ShiftsActivity extends AppCompatActivity {
    private RecyclerView recycle;
    // private List<ShiftBean> mList;
    private ShiftAdapter adapter;
    private CustomDialogutils customDialogutils = null;
    private Button btn_jieban;
    private Button btn_jiaoban;
    private HttpUtils httpUtils;
    private List<Elders> eldersList;
    private String imei = "";
    private String workNo = "";
    private SwipeRefreshLayout layout_swipe;
    private boolean isChanged = false;//标识是否交接班过

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shifts);
        initView();
        init();
        netWork();
    }

    private void initView() {
        layout_swipe = (SwipeRefreshLayout) findViewById(R.id.layout_swipe);
        btn_jieban = (Button) findViewById(R.id.btn_jieban);
        btn_jiaoban = (Button) findViewById(R.id.btn_jiaoban);
        recycle = (RecyclerView) findViewById(R.id.recycle);
        recycle.setLayoutManager(new GridLayoutManager(ShiftsActivity.this, 5));
        layout_swipe.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        layout_swipe.setSize(SwipeRefreshLayout.LARGE);
        layout_swipe.setOnRefreshListener(onRefreshListener);
    }

    /**
     * 下拉刷新
     */
    SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            layout_swipe.setRefreshing(true);
            netWork();
        }
    };


    private void init() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        imei = tm.getDeviceId();
        workNo = getIntent().getStringExtra("workNo");
        Log.e("123", "workNo=" + workNo);
        eldersList = new ArrayList<>();
        httpUtils = HttpUtils.getInstance();
        adapter = new ShiftAdapter(ShiftsActivity.this, eldersList);
        adapter.setOnItemClickListener(new ShiftAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                eldersList.get(position).setSelected(!eldersList.get(position).isSelected());
                adapter.notifyDataSetChanged();
                checkList();
            }
        });
        recycle.setAdapter(adapter);
        Log.e("123", "设置适配器" + eldersList.toString());
    }

    public void checkList() {
        for (int i = 0; i < eldersList.size(); i++) {
            boolean flag = eldersList.get(i).isSelected();
            if (flag) {
                btn_jieban.setEnabled(true);
                return;
            }

        }
        btn_jieban.setEnabled(false);

    }

    private void netWork() {
        isChanged = true;
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("DeviceID", SPUtils.find(CONTACT.IMEI));
        treeMap.put("sign", GetSign.GetSign(treeMap, SPUtils.find(CONTACT.TOKEN)));
        httpUtils.getRetrofitApi().getLockInfo("basic " + SPUtils.find(CONTACT.TOKEN), treeMap).enqueue(new Callback<LockCode>() {
            @Override
            public void onResponse(Call<LockCode> call, Response<LockCode> response) {
                layout_swipe.setRefreshing(false);
                LockCode body = response.body();
                if (body != null && body.getCode().equals(CONTACT.DataSuccess)) {
                    Lock data = body.getData();
                    //老人的数据集合
                    List<Elders> list = data.getElders();
                    if (list.size() <= 0) return;
                    eldersList.clear();
                    eldersList.addAll(list);
                    Log.e("123", "eldersList=" + eldersList.toString());
                    //加载老人头像数据
                    adapter.notifyDataSetChanged();
                    btn_jieban.setEnabled(false);

                } else {
                    Toast.makeText(getApplicationContext(), "服务器数据异常！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LockCode> call, Throwable t) {
                layout_swipe.setRefreshing(false);
            }
        });

    }


    /**
     * 交班
     */
    public void jiaoBan() {
        customDialogutils = new CustomDialogutils(ShiftsActivity.this);
        customDialogutils.networkRefreshDiallog("正在交班...");
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("JobNo", workNo);
        treeMap.put("DeviceID", SPUtils.find(CONTACT.IMEI));
        treeMap.put("sign", GetSign.GetSign(treeMap, SPUtils.find(CONTACT.TOKEN)));
        HttpUtils.getInstance().getRetrofitApi().jiaoBan("basic " + SPUtils.find(CONTACT.TOKEN), treeMap).enqueue(new Callback<JiaoBean>() {
            @Override
            public void onResponse(Call<JiaoBean> call, Response<JiaoBean> response) {
                JiaoBean body = response.body();
                if (body != null && body.getCode().equals(CONTACT.DataSuccess)) {
                    String info = body.getData();
                    if ("交班成功".equals(info)) {
                        customDialogutils.cancelNetworkDialog("交班完成", true);
                        Intent intent = new Intent(ShiftsActivity.this, MainActivity.class);
                        startActivity(intent);
                        netWork();
                    } else {
                        customDialogutils.cancelNetworkDialog("服务异常", false);
                    }
                } else {
                    customDialogutils.cancelNetworkDialog("服务异常", false);
                }
            }

            @Override
            public void onFailure(Call<JiaoBean> call, Throwable t) {
                if (!AppUtils.isNetworkConnected(ShiftsActivity.this)) {
                    customDialogutils.cancelNetworkDialog("网络异常", false);
                } else {
                    customDialogutils.cancelNetworkDialog("服务异常", false);
                }
            }
        });
    }

    /**
     * 接班
     */
    public void jieBan() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < eldersList.size(); i++) {
            boolean flag = eldersList.get(i).isSelected();
            String id = eldersList.get(i).getElderID() + "";
            if (flag) {

                sb.append("," + id);
            }
        }
        String elders = sb.toString().substring(1);
        Log.e("123", "elders=" + elders);
        customDialogutils = new CustomDialogutils(ShiftsActivity.this);
        customDialogutils.networkRefreshDiallog("正在接班...");
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("JobNo", workNo);
        treeMap.put("Elders", elders);
        treeMap.put("sign", GetSign.GetSign(treeMap, SPUtils.find(CONTACT.TOKEN)));
        HttpUtils.getInstance().getRetrofitApi().jieBan("basic " + SPUtils.find(CONTACT.TOKEN), treeMap).enqueue(new Callback<JiaoBean>() {
            @Override
            public void onResponse(Call<JiaoBean> call, Response<JiaoBean> response) {
                JiaoBean body = response.body();
                if (body != null && body.getCode().equals(CONTACT.DataSuccess)) {
                    String info = body.getData();
                    if ("接班成功".equals(info)) {
                        Log.e("123", "接班成功");
                        customDialogutils.cancelNetworkDialog("接班成功", true);
                        Intent intent = new Intent(ShiftsActivity.this, MainActivity.class);
                        startActivity(intent);
                        netWork();
                    } else {
                        Log.e("123", "接班失败");
                        customDialogutils.cancelNetworkDialog("接班失败", false);
                    }

                } else {
                    Log.e("123", "服务器数据异常");
                    customDialogutils.cancelNetworkDialog("接班失败", false);
                }
            }

            @Override
            public void onFailure(Call<JiaoBean> call, Throwable t) {
                customDialogutils.cancelNetworkDialog("接班失败", false);
            }
        });


    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_jiaoban://交班
             /*   if (customDialogutils != null) customDialogutils = null;
                customDialogutils = new CustomDialogutils(ShiftsActivity.this);
                customDialogutils.networkRefreshDiallog("正在处理交班...");
                if (mHandler.hasMessages(0)) mHandler.removeMessages(0);
                mHandler.sendEmptyMessageDelayed(0, 3000);*/
                jiaoBan();
                break;
            case R.id.btn_jieban://接班
             /*   if (customDialogutils != null) customDialogutils = null;
                customDialogutils = new CustomDialogutils(ShiftsActivity.this);
                customDialogutils.networkRefreshDiallog("正在处理接班...");
                if (mHandler.hasMessages(0)) mHandler.removeMessages(0);
                mHandler.sendEmptyMessageDelayed(0, 3000);*/
                jieBan();
                break;
            case R.id.iv_back://返回键
                backEvent();
                break;
        }
    }


    @Override
    public void onBackPressed() {
        backEvent();
    }


    /**
     * 返回改变状态
     */
    private void backEvent() {
        /*Intent intent = new Intent();
        if (isChanged) {
            setResult(RESULT_OK, intent);
        } else {
            setResult(RESULT_CANCELED, intent);
        }*/
        finish();
    }


}
