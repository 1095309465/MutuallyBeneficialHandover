package com.jhzy.nursinghandover.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jhzy.nursinghandover.R;
import com.jhzy.nursinghandover.beans.WorkerBean;
import com.jhzy.nursinghandover.beans.WorkerDataBean;
import com.jhzy.nursinghandover.utils.ImageLoaderUtils;

import java.util.List;

/**
 * Created by sxmd on 2017/2/23.
 */

public class MoreNurseAdapter extends RecyclerView.Adapter<MoreNurseAdapter.ViewHolder> {

    private List<WorkerDataBean> mList;
    private Context mContext;
    private String url = "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2475600856,3809306764&fm=111&gp=0.jpg";
    private TextView tv_title;

    public MoreNurseAdapter(List<WorkerDataBean> mList, Context mContext, TextView tv_title) {
        this.mList = mList;
        this.mContext = mContext;
        this.tv_title = tv_title;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.more_nurse_recycle_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        WorkerDataBean bean = mList.get(position);
        SimpleDraweeView iv = holder.iv;
        TextView tv = holder.tv;
        ImageLoaderUtils.load(iv, bean.getPhotoUrl());
        tv.setText(bean.getStaffName());
        if (position == 0) {
            holder.iv_close.setVisibility(View.GONE);
        } else {
            holder.iv_close.setVisibility(View.VISIBLE);
        }

        holder.iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "删除", Toast.LENGTH_SHORT).show();
                mList.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        check();
        int size = mList.size();
        if (tv_title != null) {
            if (size == 3) {
                tv_title.setText("人数已超过上限");
            } else if (size == 1) {
                tv_title.setText("请扫描NFC设备");
            }
        }
        return mList.size();
    }

    public void check() {
        if (mList.size() > 3) {
            mList.remove(3);
            check();
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView iv;
        TextView tv;
        ImageView iv_close;

        public ViewHolder(View itemView) {
            super(itemView);
            iv = (SimpleDraweeView) itemView.findViewById(R.id.iv_photo);
            tv = (TextView) itemView.findViewById(R.id.tv);
            iv_close = (ImageView) itemView.findViewById(R.id.iv_close);
        }
    }
}
