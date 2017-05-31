package com.jhzy.nursinghandover.ui.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jhzy.nursinghandover.R;
import com.jhzy.nursinghandover.beans.nextItem.SubItem;

import com.jhzy.nursinghandover.utils.ImageLoaderUtils;
import java.util.List;

/**
 * Created by sxmd on 2017/2/23.
 */

public class NurseItemAdapter extends RecyclerView.Adapter<NurseItemAdapter.ViewHolder> {
    private List<SubItem> mList;
    private Context mContext;

    public NurseItemAdapter(List<SubItem> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public NurseItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.nurse_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NurseItemAdapter.ViewHolder holder, final int position) {
        SubItem bean = mList.get(position);
        SimpleDraweeView iv_service = holder.iv_service;
        TextView tv_title = holder.tv_title;
        tv_title.setText(bean.getSubprojectName());
        if(bean.isTask()){
            iv_service.setImageURI(bean.getSubprojectIco());
            if(bean.isSelected()){
                //iv_service.setImageURI(bean.getSubprojectIcoClk());
                holder.rl.setBackgroundResource(R.drawable.shape_item_border);
            }else{
                holder.rl.setBackground(null);
            }

        }else{
            //iv_service.setBackgroundDrawable(mContext.getResources().getDrawable(bean.getSubprojectIcoInt()));
            ImageLoaderUtils.load(iv_service,bean.getSubprojectIco());
            if(bean.isSelected()){
                iv_service.setSelected(true);
                holder.rl.setBackgroundResource(R.drawable.shape_item_border);
            }else{
                iv_service.setSelected(false);
                holder.rl.setBackground(null);
            }
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
        return (mList.size() > 4 ? 4 : mList.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View rl;
        SimpleDraweeView iv_service;
        TextView tv_title;
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            iv_service = (SimpleDraweeView) itemView.findViewById(R.id.iv_service);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            rl = itemView.findViewById(R.id.sub_rl);
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
