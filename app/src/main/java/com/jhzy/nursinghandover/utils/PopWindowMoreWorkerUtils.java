package com.jhzy.nursinghandover.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.jhzy.nursinghandover.R;
import com.jhzy.nursinghandover.api.OnClickListenerNoDouble;
import com.jhzy.nursinghandover.beans.NFCBean;
import com.jhzy.nursinghandover.beans.WorkerBean;
import com.jhzy.nursinghandover.beans.WorkerDataBean;
import com.jhzy.nursinghandover.ui.adapters.MoreNurseAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sxmd on 2017/3/3.
 */

public class PopWindowMoreWorkerUtils {
    private PopupWindow popupWindow;
    private Context mContext;
    private View view;
    private WorkerDataBean worker;
    private NurseSumCallback nurseSumCallback;
    //private List<Integer> mList;
    public static List<WorkerDataBean> dataList;
    private MoreNurseAdapter adapter;
    private long time = 0;
    private EditText editText;


    public PopWindowMoreWorkerUtils() {
    }


    public PopWindowMoreWorkerUtils(final Context mContext, View view, final Window window, WorkerDataBean worker, final NurseSumCallback nurseSumCallback) {
        this.mContext = mContext;
        this.view = view;
        this.worker = worker;
        this.nurseSumCallback = nurseSumCallback;
        //mList = new ArrayList<>();

        View contentView = LayoutInflater.from(mContext).inflate(R.layout.more_nurse_layout, null);
        if (dataList == null) {
            dataList = new ArrayList<>();
            initData(worker.getWorkerNo() + "", "");
        }
        initView(contentView);
        initPop(contentView, window);
    }


    //订阅事件FirstEvent
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getNFC(NFCBean nfc) {
        if (nfc != null && "MORE_NFC".equals(nfc.getFun())) {
            if (System.currentTimeMillis() - time > 3 * 1000) {
                time = System.currentTimeMillis();
                initData("", nfc.getNfc());
            }
        }
    }


    public void initPop(View contentView, final Window window) {
        popupWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setAnimationStyle(R.style.from_bottom_anim);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.alpha = 0.6f;   //这句就是设置窗口里崆件的透明度的．０.０全透明．１.０不透明．
        window.setAttributes(wl);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Log.e("rxf", "解绑");
                EventBus.getDefault().unregister(mContext);
                WindowManager.LayoutParams wl = window.getAttributes();
                wl.alpha = 1f;   //这句就是设置窗口里崆件的透明度的．０.０全透明．１.０不透明．
                window.setAttributes(wl);
            }
        });
    }


    public void initView(View contentView) {
        View iv_back = contentView.findViewById(R.id.iv_back);
        TextView tv_title = (TextView) contentView.findViewById(R.id.tv_title);
        RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.recycle);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        adapter = new MoreNurseAdapter(dataList, mContext, tv_title);
        recyclerView.setAdapter(adapter);
        Button btn_clear = (Button) contentView.findViewById(R.id.btn_clear);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        btn_clear.setOnClickListener(new OnClickListenerNoDouble() {//清除
            @Override
            public void myOnClick(View view) {
                /*mList.clear();
                for (int i = 0; i < dataList.size(); i++) {
                    int staffID = dataList.get(i).getStaffID();
                    mList.add(staffID);
                }*/
                nurseSumCallback.nurseSum(dataList);
                popupWindow.dismiss();
            }
        });
        editText = (EditText) contentView.findViewById(R.id.ed_nfc);
        editText.requestFocus();
        editText.setCursorVisible(false);
        editText.addTextChangedListener(new TextWatcher() {
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
                    if (s.endsWith(";")) {
                        Toast.makeText(mContext, "扫描", Toast.LENGTH_SHORT).show();
                        String substring = s.substring(0, s.length() - 1);
                        initData("", NFCUtils.toStr2(substring));
                        editText.setText("");
                    }
                }
            }
        });
    }


    public void show(final Context mContext, View view, final Window window, WorkerDataBean worker, final NurseSumCallback nurseSumCallback) {
        this.mContext = mContext;
        this.view = view;
        this.worker = worker;
        this.nurseSumCallback = nurseSumCallback;
        //mList = new ArrayList<>();

        View contentView = LayoutInflater.from(mContext).inflate(R.layout.more_nurse_layout, null);
        if (dataList == null) {
            dataList = new ArrayList<>();
            initData(worker.getWorkerNo() + "", "");
        }
        initView(contentView);
        initPop(contentView, window);
        if (popupWindow != null) {
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
            editText.requestFocus();
            changeWindowsBg();
        }
    }


    private void initData(String workNo, String DeviceID) {
        Log.e("123", "多人护理接口请求开始");
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("JobNo", workNo);
        treeMap.put("DeviceID", DeviceID);
        treeMap.put("sign",GetSign.GetSign(treeMap,SPUtils.find(CONTACT.TOKEN)));
        HttpUtils.getInstance()
                .getRetrofitApi()
                .getWorkerInfo("basic " + SPUtils.find(CONTACT.TOKEN), treeMap)
                .enqueue(new Callback<WorkerBean>() {
                             @Override
                             public void onResponse(Call<WorkerBean> call, Response<WorkerBean> response) {
                                 WorkerBean body = response.body();
                                 if (body != null && body.getCode().equals(CONTACT.DataSuccess)) {
                                     WorkerDataBean dataBean = body.getData();
                                     String StaffID = dataBean.getStaffID() + "";
                                     for (int i = 0; i < dataList.size(); i++) {
                                         String id = dataList.get(i).getStaffID() + "";
                                         if (StaffID.equals(id)) {
                                             Toast.makeText(mContext, "已经存在该护工", Toast.LENGTH_SHORT).show();
                                             return;
                                         }
                                     }

                                     dataList.add(dataBean);
                                     adapter.notifyDataSetChanged();

                                 } else {
                                         Toast.makeText(mContext, "服务器异常", Toast.LENGTH_SHORT).show();
                                 }
                             }


                             @Override
                             public void onFailure(Call<WorkerBean> call, Throwable t) {
                                 if (!AppUtils.isNetworkConnected(mContext)) {
                                     Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
                                 }else {
                                     Toast.makeText(mContext, "服务器异常", Toast.LENGTH_SHORT).show();
                                 }
                             }
                         }
                );
    }


    public interface NurseSumCallback {
        void nurseSum(List<WorkerDataBean> strings);
    }


    private void changeWindowsBg() {
        WindowManager.LayoutParams wl = ((Activity) mContext).getWindow().getAttributes();
        wl.alpha = 0.5f;   //这句就是设置窗口里崆件的透明度的．０.０全透明．１.０不透明．
        ((Activity) mContext).getWindow().setAttributes(wl);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                EventBus.getDefault().unregister(mContext);
                WindowManager.LayoutParams wl = ((Activity) mContext).getWindow().getAttributes();
                wl.alpha = 1f;   //这句就是设置窗口里崆件的透明度的．０.０全透明．１.０不透明．
                ((Activity) mContext).getWindow().setAttributes(wl);
            }
        });
    }

}
