package com.jhzy.nursinghandover.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jhzy.nursinghandover.R;
import com.jhzy.nursinghandover.beans.BindBedBean;

import java.util.List;

/**
 * Created by Administrator on 2017/2/26 0026.
 */

public class BindBedAdapter extends RecyclerView.Adapter<BindBedAdapter.ViewHolder> {
    private List<BindBedBean.DataBean> mList;
    private Context mContext;
//    private String mineImei;

    public BindBedAdapter(List<BindBedBean.DataBean> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
//        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(TELEPHONY_SERVICE);
//        mineImei = tm.getDeviceId();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.bind_bed_item, null);
        return new ViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        BindBedBean.DataBean bean = mList.get(position);
        holder.tv_bedTitle.setText(bean.getBedTitle());
        holder.tv_bedName.setText(bean.getBedName());
        holder.tv_zoneName.setText(bean.getZoneName());
        Object imei = bean.getIMEIs();
        if (imei != null) {
            holder.tv_iem.setText(imei.toString());
        } else {
            holder.tv_iem.setText("");
        }
        holder.tv_roomName.setText(bean.getRoomName());
        boolean flag = bean.isChecked();
        if (!flag) {
            holder.iv_mark.setVisibility(View.GONE);
        } else {
            holder.iv_mark.setVisibility(View.VISIBLE);
        }
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.click(view, position);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView tv_zoneName;
        TextView tv_roomName;
        TextView tv_bedName;
        TextView tv_bedTitle;
        TextView tv_iem;
        ImageView iv_mark;
        private RelativeLayout layoutBg;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            layoutBg = ((RelativeLayout) itemView.findViewById(R.id.layout_bg));
            tv_zoneName = (TextView) itemView.findViewById(R.id.tv_zoneName);
            tv_roomName = (TextView) itemView.findViewById(R.id.tv_roomName);
            tv_bedName = (TextView) itemView.findViewById(R.id.tv_bedName);
            tv_bedTitle = (TextView) itemView.findViewById(R.id.tv_bedTitle);
            tv_iem = (TextView) itemView.findViewById(R.id.tv_iem);
            iv_mark = (ImageView) itemView.findViewById(R.id.iv_mark);
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void click(View view, int position);
    }
}
