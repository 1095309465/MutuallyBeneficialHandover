package com.jhzy.nursinghandover.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jhzy.nursinghandover.R;

import com.jhzy.nursinghandover.api.OnClickListenerNoDouble;
import com.jhzy.nursinghandover.beans.RoomGridBean;
import com.jhzy.nursinghandover.ui.adapters.RoomGridAdapter;
import com.jhzy.nursinghandover.utils.AppUtils;
import com.jhzy.nursinghandover.utils.CONTACT;
import com.jhzy.nursinghandover.utils.HttpUtils;
import com.jhzy.nursinghandover.utils.SPUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomActivity extends AppCompatActivity {
    public static final String TAG = "workNo";
    public static final String TAG1 = "tiban";
    ImageView back;
    TextView roomNumber;
    GridView roomGrid;
    SwipeRefreshLayout roomRefresh;
    private List<RoomGridBean.DataBean.TasksBean> list = new ArrayList<>();
    private RoomGridAdapter adapter;
    private String roomid;

    private Runnable touchrunnable = new Runnable() {
        @Override
        public void run() {
            finish();
        }
    };
    private Handler timehandler = new Handler();
    private TextView tiban;
    private Context context;
    private String workNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        workNo = getIntent().getStringExtra(TAG);
        context = this;
        initView();
        adapter = new RoomGridAdapter(this, list);
        roomGrid.setAdapter(adapter);
        roomRefresh.setOnRefreshListener(onRefreshListener);
        roomid = SPUtils.find(CONTACT.IMEI);
        roomGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(RoomActivity.this, ElderActivity.class);
                intent.putExtra("elderid", list.get(i).getElderID());
                intent.putExtra("eldernumber", list.get(i).getCount());
                startActivity(intent);
            }
        });
        network();
    }


    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                finish();
            }
        });
        roomNumber = ((TextView) findViewById(R.id.room_number));
        roomGrid = ((GridView) findViewById(R.id.room_grid));
        roomRefresh = ((SwipeRefreshLayout) findViewById(R.id.room_refresh));
        tiban = ((TextView) findViewById(R.id.tiban));
        tiban.setOnClickListener(new OnClickListenerNoDouble() {
            @Override public void myOnClick(View view) {
                finish();
                Intent intent = new Intent(context,NextActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(NextActivity.TAG, workNo);
                bundle.putSerializable(NextActivity.TAG1,
                    (Serializable) MainActivity.eldersList);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        if(!TextUtils.isEmpty(getIntent().getStringExtra(TAG1))){
            tiban.setVisibility(View.GONE);
        }
    }


    private void network() {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("DeviceID", roomid);
        map.put("sign",SPUtils.find(CONTACT.TOKEN));
        HttpUtils.getInstance()
            .getRetrofitApi()
            .getAllElders("basic " + SPUtils.find(CONTACT.TOKEN), map)
            .enqueue(new Callback<RoomGridBean>() {
                @Override
                public void onResponse(Call<RoomGridBean> call, Response<RoomGridBean> response) {
                    roomRefresh.setRefreshing(false);
                    RoomGridBean body = response.body();
                    if (body != null && body.getCode().equals(CONTACT.DataSuccess)) {
                        list.clear();
                        list.addAll(body.getData().getTasks());
                        adapter.notifyDataSetChanged();
                        roomNumber.setText(body.getData().getCount() + "");
                    } else {
                        Toast.makeText(RoomActivity.this, "服务器数据异常！", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onFailure(Call<RoomGridBean> call, Throwable t) {
                    roomRefresh.setRefreshing(false);
                    t.printStackTrace();
                    if (!AppUtils.isNetworkConnected(RoomActivity.this)){
                        Toast.makeText(RoomActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(RoomActivity.this, "服务器数据异常", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    }


    SwipeRefreshLayout.OnRefreshListener onRefreshListener
        = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            roomRefresh.setRefreshing(true);
            network();
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        timehandler.postDelayed(touchrunnable, 20000);
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
