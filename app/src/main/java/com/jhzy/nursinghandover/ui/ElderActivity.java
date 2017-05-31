package com.jhzy.nursinghandover.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jhzy.nursinghandover.R;
import com.jhzy.nursinghandover.api.OnClickListenerNoDouble;
import com.jhzy.nursinghandover.beans.ElderGridBean;
import com.jhzy.nursinghandover.ui.adapters.ElderGridAdapter;
import com.jhzy.nursinghandover.utils.AppUtils;
import com.jhzy.nursinghandover.utils.CONTACT;
import com.jhzy.nursinghandover.utils.GetSign;
import com.jhzy.nursinghandover.utils.HttpUtils;
import com.jhzy.nursinghandover.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ElderActivity extends AppCompatActivity {

    //返回键
    ImageView elderBack;
    //标题
    TextView elderTitle;
    //宗惟成任务数
    TextView elderNumber;
    GridView elderGrid;
    //刷新区域空间
    SwipeRefreshLayout elderRefresh;
    private List<ElderGridBean.DataBean> list = new ArrayList<>();
    private ElderGridAdapter adapter;
    private int elderid;
    private int eldernumber;

    private Runnable touchrunnable = new Runnable() {
        @Override
        public void run() {
            finish();
        }
    };
    private Handler timehandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elder);
        initView();
        elderRefresh.setOnRefreshListener(onRefreshListener);
        elderid = getIntent().getIntExtra("elderid", 0);
        eldernumber = getIntent().getIntExtra("eldernumber", 0);
        elderNumber.setText(eldernumber + "");
        adapter = new ElderGridAdapter(this, list);
        elderGrid.setAdapter(adapter);
        network();
    }


    private void initView() {
        elderBack = ((ImageView) findViewById(R.id.elder_back));
        elderBack.setOnClickListener(new OnClickListenerNoDouble() {
            @Override public void myOnClick(View view) {
                finish();
            }
        });
        elderTitle = ((TextView) findViewById(R.id.elder_title));
        elderNumber = ((TextView) findViewById(R.id.elder_number));
        elderGrid = ((GridView) findViewById(R.id.elder_grid));
        elderRefresh = ((SwipeRefreshLayout) findViewById(R.id.elder_refresh));
    }


    /**
     * 网络请求数据
     */
    private void network() {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("ElderID", elderid + "");
        map.put("sign", GetSign.GetSign(map,SPUtils.find(CONTACT.TOKEN)));
        HttpUtils.getInstance().getRetrofitApi().getAllTask("basic " + SPUtils.find(CONTACT.TOKEN), map).enqueue(new Callback<ElderGridBean>() {
            @Override
            public void onResponse(Call<ElderGridBean> call, Response<ElderGridBean> response) {
                elderRefresh.setRefreshing(false);
                if (response.body() != null && response.body().getCode().equals(CONTACT.DataSuccess)) {
                    list.clear();
                    list.addAll(response.body().getData());
                    if (list.size() != 0)
                        adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ElderActivity.this, "服务器数据异常！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ElderGridBean> call, Throwable t) {
                elderRefresh.setRefreshing(false);
                t.printStackTrace();
                if (!AppUtils.isNetworkConnected(ElderActivity.this)){
                    Toast.makeText(ElderActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(ElderActivity.this, "服务器数据异常", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 下拉刷新监听器
     */
    SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            elderRefresh.setRefreshing(true);
            network();
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        timehandler.postDelayed(touchrunnable,20000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        timehandler.removeCallbacks(touchrunnable);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                timehandler.postDelayed(touchrunnable, 20000);
                break;
            case MotionEvent.ACTION_DOWN:
                timehandler.removeCallbacks(touchrunnable);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
