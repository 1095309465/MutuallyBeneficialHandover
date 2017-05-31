package com.jhzy.nursinghandover.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jhzy.nursinghandover.R;
import com.jhzy.nursinghandover.beans.BedBindBean;

import java.util.List;

/**
 * Created by Administrator on 2017/2/26 0026.
 */

public class BindAdapter extends RecyclerView.Adapter<BindAdapter.ViewHolder> {
    private List<BedBindBean.DataBean> mList;
    private Context mContext;
//    private String imei;

    public BindAdapter(List<BedBindBean.DataBean> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
//        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(TELEPHONY_SERVICE);
//        imei = tm.getDeviceId();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.bind_room_item, null);
        return new ViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        BedBindBean.DataBean bean = mList.get(position);
        holder.tv_zoneName.setText(bean.getZoneName());
        holder.tv_roomValue.setText(bean.getRoomValue());
        holder.tv_iem.setText(bean.getIMEIs().toString());
        holder.tv_zoneValue.setText(bean.getZoneValue());
        holder.tv_roomName.setText(bean.getRoomName());
        holder.btn_bind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onBindClickListener != null) {
                    onBindClickListener.onClick(position);
                }

            }
        });
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(view, position);
                }
            }
        });
        holder.btn_unbind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onUnBindClickListener != null) {
                    onUnBindClickListener.onClick(position);
                }

            }
        });
//        String imes = bean.getIMEIs().toString();
//        if (!TextUtils.isEmpty(imei) && !TextUtils.isEmpty(imes)) {
//            if (imes.contains(imei)) {
//                holder.layoutBg.setBackgroundResource(R.drawable.bind_layout_shape_blue);
//            } else {
//                holder.layoutBg.setBackgroundResource(R.drawable.bind_layout_shape);
//            }
//        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_zoneName;
        TextView tv_roomName;
        TextView tv_zoneValue;
        TextView tv_roomValue;
        TextView tv_iem;
        TextView btn_bind;
        TextView btn_unbind;
        View view;
        private LinearLayout layoutBg;

        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            layoutBg = ((LinearLayout) itemView.findViewById(R.id.layout_bg));
            tv_zoneName = (TextView) itemView.findViewById(R.id.tv_zoneName);
            tv_roomName = (TextView) itemView.findViewById(R.id.tv_roomName);
            tv_zoneValue = (TextView) itemView.findViewById(R.id.tv_zoneValue);
            tv_roomValue = (TextView) itemView.findViewById(R.id.tv_roomValue);
            tv_iem = (TextView) itemView.findViewById(R.id.tv_iem);
            btn_bind = (TextView) itemView.findViewById(R.id.btn_bind);
            btn_unbind = (TextView) itemView.findViewById(R.id.btn_unbind);
        }
    }

    public OnItemClickListener onItemClickListener;
    public OnBindClickListener onBindClickListener;
    public OnUnBindClickListener onUnBindClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnBindClickListener(OnBindClickListener onBindClickListener) {
        this.onBindClickListener = onBindClickListener;
    }

    public void setOnUnBindClickListener(OnUnBindClickListener onUnBindClickListener) {
        this.onUnBindClickListener = onUnBindClickListener;
    }

    public interface OnItemClickListener {
        void onClick(View view, int position);
    }

    public interface OnBindClickListener {
        void onClick(int position);
    }

    public interface OnUnBindClickListener {
        void onClick(int position);
    }
}
