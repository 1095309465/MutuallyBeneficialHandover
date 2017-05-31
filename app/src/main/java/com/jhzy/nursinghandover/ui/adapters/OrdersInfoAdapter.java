package com.jhzy.nursinghandover.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jhzy.nursinghandover.R;
import com.jhzy.nursinghandover.api.EldersInfoListener;
import com.jhzy.nursinghandover.api.OnClickListenerNoDouble;
import com.jhzy.nursinghandover.model.lock.Elders;
import com.jhzy.nursinghandover.ui.MainActivity;
import com.jhzy.nursinghandover.utils.ImageLoaderUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bigyu on 2017/2/22.
 * 主页老人展示的适配器
 */
public class OrdersInfoAdapter extends RecyclerView.Adapter<OrdersInfoAdapter.ViewHolder> {

    // 布局id
    private int resId = R.layout.adapter_order_info;
    // 数据集合
    private List<Elders> list;
    //监听接口
    private EldersInfoListener eldersInfoListener;

    private boolean isFirst = true;

    private Context mContext;
    GenericDraweeHierarchy build;
    private MainActivity.ViewHolder vh;

    public OrdersInfoAdapter(Context context, MainActivity.ViewHolder vh) {
        this.mContext = context;
        list = new ArrayList<>();
        this.vh = vh;
//        GenericDraweeHierarchyBuilder hierarchyBuilder = new GenericDraweeHierarchyBuilder(mContext.getResources());
//         build = hierarchyBuilder
//                .setOverlay(mContext.getResources().getDrawable(R.mipmap.ic_launcher))
//                .build();

    }


    /**
     * @param eldersInfoListener 初始化监听
     */
    public void setEldersInfoListener(EldersInfoListener eldersInfoListener) {
        this.eldersInfoListener = eldersInfoListener;
    }

    /**
     * @param lists 初始化数据
     */
    public void setDatas(List<Elders> lists) {
        list.clear();
        list.addAll(lists);
        notifyDataSetChanged();
    }
    public  void clear(){
        list.clear();
        notifyDataSetChanged();
    }


    @Override
    public OrdersInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(resId, parent, false);
        return new ViewHolder(view);
    }

    /**
     * 动态计算 line 的长度
     *
     * @param holder
     */
    @Override
    public void onViewAttachedToWindow(final ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (isFirst) {
            holder.headIcon.post(new Runnable() {
                @Override
                public void run() {
                    int itemWidth = holder.layout.getWidth();
                    int cardWidth = holder.cardView.getWidth();
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) vh.line.getLayoutParams();
                    int marginStart = params.getMarginStart();
                    int space = marginStart + (itemWidth - cardWidth) / 2;
                    params.leftMargin = space;
                    params.rightMargin = space;
                    vh.line.setLayoutParams(params);
                    RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) vh.dutynurse.getLayoutParams();
                    params1.rightMargin = space;
                    vh.dutynurse.setLayoutParams(params1);
                }
            });
            isFirst = false;
        }

    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Elders elders = list.get(position);
        holder.name.setText(elders.getElderName());
        holder.bed.setText(elders.getBedTitle());
        //   holder.nurse.setText("责任护工:" + elders.getStaffName() == null ? "" : elders.getStaffName());
        String staffName = null;
        if (TextUtils.isEmpty(elders.getStaffName())) {
            staffName = "暂无";
        } else {
            staffName = elders.getStaffName();
        }
        holder.nurse.setText("责任护工:" + staffName);
        ImageLoaderUtils.load(holder.headIcon, elders.getPhotoUrl());
        final int currentPosition = position;
        if (eldersInfoListener != null) {
            holder.layout.setOnClickListener(new OnClickListenerNoDouble() {
                @Override
                public void myOnClick(View view) {
                    eldersInfoListener.eldersInfoListener(currentPosition);
                }
            });
        }
//        holder.headIcon.setBackgroundResource(R.mipmap.erder);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name; // 姓名
        private TextView bed; // 床位号
        private TextView nurse; // 责任护工
        private View layout;
        private SimpleDraweeView headIcon;
        private LinearLayout cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.layout = itemView;
            cardView = (LinearLayout) itemView.findViewById(R.id.cardView);
            headIcon = (SimpleDraweeView) itemView.findViewById(R.id.head_icon);
            name = ((TextView) itemView.findViewById(R.id.name));
            bed = ((TextView) itemView.findViewById(R.id.bed));
            nurse = ((TextView) itemView.findViewById(R.id.nurse));
        }
    }


}
