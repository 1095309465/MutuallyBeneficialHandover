package com.jhzy.nursinghandover.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhzy.nursinghandover.R;
import com.jhzy.nursinghandover.api.OnClickListenerNoDouble;
import com.jhzy.nursinghandover.beans.ElderInfo;
import com.jhzy.nursinghandover.beans.nextItem.EldersBean;
import com.jhzy.nursinghandover.widget.ImageMaskView;

import java.util.List;

/**
 * Created by nakisaRen
 * on 17/2/23.
 */

public class NextElderAdapter extends RecyclerView.Adapter<NextElderAdapter.Holder> {
    private Context context;
    private ElderClick elderClick;
    private List<EldersBean> elderInfos;


    public void setData(List<EldersBean> elderInfos) {
        this.elderInfos = elderInfos;
        notifyDataSetChanged();
    }


    public NextElderAdapter(Context context, ElderClick elderClick) {
        this.context = context;
        this.elderClick = elderClick;
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.next_right_item, null);
        return new Holder(view);
    }


    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        EldersBean info = elderInfos.get(position);
        holder.name.setText(info.getElderName());
        holder.bed.setText(info.getBedTitle());
        holder.head.setbg(info.getPhotoUrl());
        //        ImageLoaderUtils.load(holder.head, info.getHead());
        holder.view.setOnClickListener(new OnClickListenerNoDouble() {
            @Override
            public void myOnClick(View view) {
                elderClick.onElderClick(holder.head, position);
            }
        });
        if (info.getIsCompleted() == 1) {
            holder.head.isCheck(true);
        } else {
            holder.head.isCheck(false);
        }
        if (info.isChecked()) {
            //holder.check.setImageResource(R.mipmap.next_check);
            holder.bg.setBackgroundResource(R.mipmap.img_elder_bg_blue);
        } else {
            //holder.check.setImageResource(R.mipmap.icon_search_check_white);
            holder.bg.setBackgroundResource(R.drawable.shape_elder);
        }
    }


    @Override
    public int getItemCount() {
        return elderInfos == null ? 0 : elderInfos.size();
    }


    class Holder extends RecyclerView.ViewHolder {
        private ImageView check;
        private TextView bed;
        private TextView name;
        private ImageMaskView head;
        View view, bg;


        public Holder(View itemView) {
            super(itemView);
            view = itemView;
            check = ((ImageView) itemView.findViewById(R.id.right_item_check));
            bed = ((TextView) itemView.findViewById(R.id.right_item_bed));
            name = ((TextView) itemView.findViewById(R.id.right_item_name));
            head = ((ImageMaskView) itemView.findViewById(R.id.head_icon));
            bg = itemView.findViewById(R.id.right_item_bg);
        }
    }


    public interface ElderClick {
        void onElderClick(View view, int position);
    }
}
