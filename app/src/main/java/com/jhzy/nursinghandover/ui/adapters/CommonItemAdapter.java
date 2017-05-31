package com.jhzy.nursinghandover.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jhzy.nursinghandover.R;
import com.jhzy.nursinghandover.beans.nextItem.CommonBean;
import com.jhzy.nursinghandover.widget.ImageTxtView;

import java.util.List;

/**
 * Created by nakisaRen
 * on 17/3/27.
 */

public class CommonItemAdapter extends RecyclerView.Adapter<CommonItemAdapter.Holder> {

    public interface CommonClick {
        void click(View view, int position);
    }

    public void setClick(CommonClick click) {
        this.click = click;
    }

    private List<CommonBean> datas;
    private CommonClick click;

    public CommonItemAdapter() {
    }

    public void setDatas(List<CommonBean> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }


    private float parentWidth;

    public void setParentWidth(float parentWidth) {
        this.parentWidth = parentWidth;
    }

    private boolean isFirst = true;
    private float space;

    @Override
    public void onViewAttachedToWindow(final Holder holder) {
        super.onViewAttachedToWindow(holder);
        //只计算一次空间
        if (isFirst) {
            holder.layout.post(new Runnable() {
                @Override
                public void run() {
                    int width = holder.layout.getWidth();
                    float calculateOneWidth = parentWidth / 7;
                    space = (calculateOneWidth - width);
                }
            });
            isFirst = false;
        }
        holder.layout.post(new Runnable() {
            @Override
            public void run() {
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.layout.getLayoutParams();
                params.leftMargin = (int) (space / 2);
                params.rightMargin = (int) (space / 2);
                holder.layout.setLayoutParams(params);
            }
        });

    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_common, parent, false);
        return new Holder(view);
    }


    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        CommonBean commonBean = datas.get(holder.getAdapterPosition());
        holder.item.setTitle(commonBean.getCareItemTitle());
        holder.item.setUrlImage(commonBean.getCareItemUrl());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click.click(holder.item, holder.getAdapterPosition());
            }
        });
    }


    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }


    class Holder extends RecyclerView.ViewHolder {

        private ImageTxtView item;

        private View layout;


        public Holder(View itemView) {
            super(itemView);
            this.layout = itemView;
            item = ((ImageTxtView) itemView.findViewById(R.id.common_item));
        }
    }
}
