package com.jhzy.nursinghandover.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jhzy.nursinghandover.R;
import com.jhzy.nursinghandover.beans.BedBindBean;
import com.jhzy.nursinghandover.beans.BindBedsBackBean;
import com.jhzy.nursinghandover.beans.BindRoomBackBean;
import com.jhzy.nursinghandover.ui.adapters.BindAdapter;
import com.jhzy.nursinghandover.utils.AppUtils;
import com.jhzy.nursinghandover.utils.CONTACT;
import com.jhzy.nursinghandover.utils.CustomDialogutils;
import com.jhzy.nursinghandover.utils.GetSign;
import com.jhzy.nursinghandover.utils.HttpUtils;
import com.jhzy.nursinghandover.utils.PopWindowUtils;
import com.jhzy.nursinghandover.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BindActivity extends AppCompatActivity {

    private RecyclerView recycle;
    private List<BedBindBean.DataBean> mList;
    private BindAdapter adapter;
    private String id;
    private CustomDialogutils customDialogutils = null;
    private String imei;
    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind);
        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        imei = tm.getDeviceId();
        Log.e("123", "imei=" + imei);
        init();

    }

    @Override
    protected void onResume() {
        super.onResume();
        netWork();
    }

    private void netWork() {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("sign",GetSign.GetSign(map,SPUtils.find(CONTACT.TOKEN)));
        HttpUtils.getInstance().getRetrofitApi().getZone("basic " + SPUtils.find(CONTACT.TOKEN), map).enqueue(new Callback<BedBindBean>() {
            @Override
            public void onResponse(Call<BedBindBean> call, Response<BedBindBean> response) {

                if (customDialogutils != null) {
                    customDialogutils.dismissDialog();
                }
                if (response.body() != null && CONTACT.DataSuccess.equals(response.body().getCode())) {
                    Log.e("123", "response=" + response.body().toString());
                    BedBindBean bean = response.body();
                    List<BedBindBean.DataBean> list = bean.getData();
                    mList.clear();
                    mList.addAll(list);
                    adapter.notifyDataSetChanged();
                } else {

                    if (customDialogutils != null) {
                        customDialogutils.cancelNetworkDialog("加载失败", false);
                    }
                }
            }

            @Override
            public void onFailure(Call<BedBindBean> call, Throwable t) {
                if (customDialogutils != null) {
                    customDialogutils.cancelNetworkDialog("网络异常", false);
                }
            }
        });
    }

    /**
     * 绑定房间的接口请求
     */
    private void bindRoomsNetWork(String RoomId) {//bindRooms
        Log.e("123", "绑定房间的RoomId=" + RoomId);
        TreeMap<String, String> map = new TreeMap<>();
        map.put("RoomId", RoomId);
        map.put("IMEI", imei);
        map.put("sign", GetSign.GetSign(map, SPUtils.find(CONTACT.TOKEN)));
        HttpUtils.getInstance().getRetrofitApi().bindRooms("basic " + SPUtils.find(CONTACT.TOKEN), map).enqueue(new Callback<BindRoomBackBean>() {
            @Override
            public void onResponse(Call<BindRoomBackBean> call, Response<BindRoomBackBean> response) {
                BindRoomBackBean body = response.body();
                String msg = body.getMsg() == null ? "" : body.getMsg().toString();
                if (response.body() != null && CONTACT.DataSuccess.equals(response.body().getCode())) {
                    Toast.makeText(BindActivity.this, "绑定成功", Toast.LENGTH_SHORT).show();
                    netWork();

                } else {
                    Toast.makeText(BindActivity.this, "绑定失败" + msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BindRoomBackBean> call, Throwable t) {
                if (!AppUtils.isNetworkConnected(BindActivity.this)) {
                    Toast.makeText(BindActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(BindActivity.this, "服务异常", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * 解绑房间的接口请求
     */
    private void unBindRoomsNetWork(String RoomId) {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("RoomId", RoomId);
        map.put("IMEI", imei);
        map.put("sign", GetSign.GetSign(map, SPUtils.find(CONTACT.TOKEN)));
        HttpUtils.getInstance().getRetrofitApi().unBindRooms("basic " + SPUtils.find(CONTACT.TOKEN), map).enqueue(new Callback<BindBedsBackBean>() {
            @Override
            public void onResponse(Call<BindBedsBackBean> call, Response<BindBedsBackBean> response) {
                BindBedsBackBean body = response.body();
                String msg = body.getMsg() == null ? "" : body.getMsg().toString();
                if (response.body() != null && CONTACT.DataSuccess.equals(response.body().getCode())) {
                    Toast.makeText(BindActivity.this, "解绑成功", Toast.LENGTH_SHORT).show();
                    netWork();
                } else {
                    Toast.makeText(BindActivity.this, "解绑失败" + msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BindBedsBackBean> call, Throwable t) {
                if (!AppUtils.isNetworkConnected(BindActivity.this)) {
                    Toast.makeText(BindActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(BindActivity.this, "服务异常", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void init() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        id = getIntent().getStringExtra("id");
        recycle = (RecyclerView) findViewById(R.id.recycle);
        recycle.setLayoutManager(new GridLayoutManager(BindActivity.this, 2));
        tv_title.setText("    区域绑定\r\n本机设备号: " + imei);

        mList = new ArrayList<>();
        adapter = new BindAdapter(mList, BindActivity.this);
        adapter.setOnItemClickListener(new BindAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {//单项点击
                //RoomId
                Intent intent = new Intent(BindActivity.this, BindBedActivity.class);
                intent.putExtra("RoomId", mList.get(position).getID() + "");
                startActivity(intent);
            }
        });
        adapter.setOnBindClickListener(new BindAdapter.OnBindClickListener() {
            @Override
            public void onClick(int position) {//点击绑定按钮
                final String roomId = mList.get(position).getID() + "";
                PopWindowUtils.showBindDialog("确定绑定设备吗?", BindActivity.this, findViewById(R.id.activity_main), getWindow(), new PopWindowUtils.OnBindListener() {
                    @Override
                    public void onOkClick() {
                        bindRoomsNetWork(roomId);
                    }

                    @Override
                    public void onNoClick() {


                    }
                });
            }
        });
        adapter.setOnUnBindClickListener(new BindAdapter.OnUnBindClickListener() {
            @Override
            public void onClick(final int position) {//点击解绑
                PopWindowUtils.showBindDialog("确定解绑设备吗", BindActivity.this, findViewById(R.id.activity_main), getWindow(), new PopWindowUtils.OnBindListener() {
                    @Override
                    public void onOkClick() {
                        final String roomId = mList.get(position).getID() + "";
                        unBindRoomsNetWork(roomId);
                    }

                    @Override
                    public void onNoClick() {

                    }
                });

            }
        });
        recycle.setAdapter(adapter);
    }

    public void btn(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();

                break;
            case R.id.iv_refresh://刷新
                customDialogutils = new CustomDialogutils(BindActivity.this);
                customDialogutils.networkRefreshDiallog("正在刷新...");
                netWork();
                break;
        }
    }
}
