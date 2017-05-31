package com.jhzy.nursinghandover.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jhzy.nursinghandover.R;
import com.jhzy.nursinghandover.model.lock.Elders;
import com.jhzy.nursinghandover.utils.ImageLoaderUtils;

import java.util.List;

/**
 * Created by sxmd on 2017/2/23.
 */

public class ShiftAdapter extends RecyclerView.Adapter<ShiftAdapter.ViewHolder> {
    private Context mContext;
    private List<Elders> mList;

    public ShiftAdapter(Context mContext, List<Elders> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public ShiftAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.shift_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShiftAdapter.ViewHolder holder, final int position) {
        Log.e("123", "onBindViewHolder" + position);
        Elders bean = mList.get(position);
        boolean flag = bean.isSelected();
        if (flag) {
            holder.lin_bg.setBackgroundResource(R.mipmap.img_bg_shade);
        } else {
            holder.lin_bg.setBackgroundResource(R.drawable.shape_elder);
        }
        ImageLoaderUtils.load(holder.photo, bean.getPhotoUrl());
        holder.tvBedCode.setText(bean.getBedTitle());
        holder.tvName.setText(bean.getElderName());
        String staffName = bean.getStaffName();
        if (TextUtils.isEmpty(staffName)) {//
            holder.tvNurse.setText("责任护工:暂无");
        } else {
            holder.tvNurse.setText("责任护工:" + bean.getStaffName());
        }
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(v, position);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView photo;
        TextView tvName;
        TextView tvBedCode;
        TextView tvNurse;
        View view;
        LinearLayout lin_bg;

        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            lin_bg = (LinearLayout) itemView.findViewById(R.id.lin_bg);
            photo = (SimpleDraweeView) itemView.findViewById(R.id.iv_photo);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvBedCode = (TextView) itemView.findViewById(R.id.tv_bedcode);
            tvNurse = (TextView) itemView.findViewById(R.id.tv_nurse);
        }

    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(View view, int position);
    }
}
