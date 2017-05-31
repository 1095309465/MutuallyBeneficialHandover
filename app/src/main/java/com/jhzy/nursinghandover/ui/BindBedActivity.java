package com.jhzy.nursinghandover.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jhzy.nursinghandover.R;
import com.jhzy.nursinghandover.beans.BindBedsBackBean;
import com.jhzy.nursinghandover.ui.adapters.BindBedAdapter;
import com.jhzy.nursinghandover.beans.BindBedBean;
import com.jhzy.nursinghandover.utils.AppUtils;
import com.jhzy.nursinghandover.utils.CONTACT;
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

public class BindBedActivity extends AppCompatActivity {
    private List<BindBedBean.DataBean> mlist;
    private RecyclerView recycle;
    private BindBedAdapter adapter;
    private String RoomId;
    private String imei;
    private SwipeRefreshLayout layout_swipe;
    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_bed);
        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        imei = tm.getDeviceId();
        init();
        nexWork();
    }

    public void nexWork() {
        Log.e("123", "RoomId=" + RoomId);
        TreeMap<String, String> map = new TreeMap<>();
        map.put("RoomId", RoomId);
        map.put("sign", GetSign.GetSign(map, SPUtils.find(CONTACT.TOKEN)));
        HttpUtils.getInstance().getRetrofitApi().getBed("basic " + SPUtils.find(CONTACT.TOKEN), map).enqueue(new Callback<BindBedBean>() {
            @Override
            public void onResponse(Call<BindBedBean> call, Response<BindBedBean> response) {
                layout_swipe.setRefreshing(false);
                BindBedBean bean = response.body();
                Log.e("123", "bean=" + bean.toString());
                if (response.body() != null && CONTACT.DataSuccess.equals(response.body().getCode())) {

                    List<BindBedBean.DataBean> list = bean.getData();
                    mlist.clear();
                    mlist.addAll(list);
                    Log.e("123", "mlist=" + mlist.toString());
                    adapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(BindBedActivity.this, "服务异常", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<BindBedBean> call, Throwable t) {
                layout_swipe.setRefreshing(false);
                if (!AppUtils.isNetworkConnected(BindBedActivity.this)) {
                    Toast.makeText(BindBedActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(BindBedActivity.this, "服务异常", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void btn(View view) {
        switch (view.getId()) {
            case R.id.btn_bind://绑定
                PopWindowUtils.showBindDialog("确定绑定设备吗?", BindBedActivity.this, findViewById(R.id.activity_bind_bed), getWindow(), new PopWindowUtils.OnBindListener() {
                    @Override
                    public void onOkClick() {
                        bindBeds();
                    }

                    @Override
                    public void onNoClick() {

                    }
                });
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_unbind://解绑
                PopWindowUtils.showBindDialog("确定解绑设备吗?", BindBedActivity.this, findViewById(R.id.activity_bind_bed), getWindow(), new PopWindowUtils.OnBindListener() {
                    @Override
                    public void onOkClick() {
                        unBindBes();
                    }

                    @Override
                    public void onNoClick() {

                    }
                });
                break;
        }
    }


    public String getCheckedBeds() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mlist.size(); i++) {
            BindBedBean.DataBean dataBean = mlist.get(i);
            boolean ischecked = dataBean.isChecked();
            if (ischecked) {
                sb.append("," + dataBean.getID());
            }
        }
        String str = sb.toString();
        if (str.startsWith(",")) {
            str = str.substring(1);
        }
        return str;
    }

    /**
     * 绑定床位
     */
    public void bindBeds() {
        //参数：RoomId  房间ID，  BedIds  //多选床位ID，逗号分隔，IMEI
        TreeMap<String, String> map = new TreeMap<>();
        String BedIds = getCheckedBeds();
        if (TextUtils.isEmpty(BedIds)) {
            Toast.makeText(BindBedActivity.this, "没有选择床位，请选择后重试", Toast.LENGTH_SHORT).show();
            return;
        }
        map.put("RoomId", RoomId);
        map.put("BedIds", BedIds);
        map.put("IMEI", imei);
        map.put("sign", GetSign.GetSign(map, SPUtils.find(CONTACT.TOKEN)));
        Log.e("123", "绑定床位");
        HttpUtils.getInstance().getRetrofitApi().bindBeds("basic " + SPUtils.find(CONTACT.TOKEN), map).enqueue(new Callback<BindBedsBackBean>() {
            @Override
            public void onResponse(Call<BindBedsBackBean> call, Response<BindBedsBackBean> response) {
                BindBedsBackBean body = response.body();
                String msg = body.getMsg() == null ? "" : body.getMsg().toString();
                if (response.body() != null && CONTACT.DataSuccess.equals(response.body().getCode())) {
                    Toast.makeText(BindBedActivity.this, "绑定成功", Toast.LENGTH_SHORT).show();
                    nexWork();
                } else {
                    Toast.makeText(BindBedActivity.this, "绑定失败" + msg, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<BindBedsBackBean> call, Throwable t) {
                if (!AppUtils.isNetworkConnected(BindBedActivity.this)) {
                    Toast.makeText(BindBedActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(BindBedActivity.this, "服务异常", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /**
     * 解绑床位
     */
    public void unBindBes() {
        TreeMap<String, String> map = new TreeMap<>();
        String BedIds = getCheckedBeds();
        if (TextUtils.isEmpty(BedIds)) {
            Toast.makeText(BindBedActivity.this, "没有选择床位，请选择后重试", Toast.LENGTH_SHORT).show();
            return;
        }
        map.put("RoomId", RoomId);
        map.put("BedIds", BedIds);
        map.put("IMEI", imei);
        map.put("sign", GetSign.GetSign(map, SPUtils.find(CONTACT.TOKEN)));
        Log.e("123", "解绑床位");
        HttpUtils.getInstance().getRetrofitApi().unBindBeds("basic " + SPUtils.find(CONTACT.TOKEN), map).enqueue(new Callback<BindBedsBackBean>() {
            @Override
            public void onResponse(Call<BindBedsBackBean> call, Response<BindBedsBackBean> response) {
                BindBedsBackBean body = response.body();
                String msg = body.getMsg() == null ? "" : body.getMsg().toString();
                if (response.body() != null && CONTACT.DataSuccess.equals(response.body().getCode())) {
                    Toast.makeText(BindBedActivity.this, "解绑成功", Toast.LENGTH_SHORT).show();
                    nexWork();
                } else {
                    Toast.makeText(BindBedActivity.this, "解绑失败" + msg, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<BindBedsBackBean> call, Throwable t) {
                Toast.makeText(BindBedActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void init() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        layout_swipe = (SwipeRefreshLayout) findViewById(R.id.layout_swipe);
        RoomId = getIntent().getStringExtra("RoomId");
        tv_title.setText("   床位绑定\r\n本机设备号: " + imei);
        mlist = new ArrayList<>();
        recycle = (RecyclerView) findViewById(R.id.recycle);
        recycle.setLayoutManager(new GridLayoutManager(BindBedActivity.this, 2));
        adapter = new BindBedAdapter(mlist, BindBedActivity.this);
        layout_swipe.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        layout_swipe.setSize(SwipeRefreshLayout.LARGE);
        layout_swipe.setOnRefreshListener(onRefreshListener);
        adapter.setOnItemClickListener(new BindBedAdapter.OnItemClickListener() {
            @Override
            public void click(View view, int position) {
                mlist.get(position).setChecked(!mlist.get(position).isChecked());
                adapter.notifyDataSetChanged();
            }
        });
        recycle.setAdapter(adapter);
    }

    /**
     * 下拉刷新
     */
    SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            layout_swipe.setRefreshing(true);
            nexWork();
        }
    };
}
