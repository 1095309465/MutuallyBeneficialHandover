package com.jhzy.nursinghandover.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jhzy.nursinghandover.R;
import com.jhzy.nursinghandover.api.OnClickListenerNoDouble;
import com.jhzy.nursinghandover.beans.nextItem.NextDetail;
import com.jhzy.nursinghandover.beans.nextItem.NurseBean;
import java.util.List;

/**
 * Created by nakisaRen
 * on 17/2/23.
 */

public class NextItemAdapter extends RecyclerView.Adapter<NextItemAdapter.Holder> {
    private Context context;
    private List<NurseBean> items;
    private ItemClick itemClick;


    public NextItemAdapter(Context context, ItemClick itemClick) {
        this.context = context;
        this.itemClick = itemClick;
    }


    public void setData(List<NurseBean> items) {
        this.items = items;
        notifyDataSetChanged();
    }


    @Override public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.next_left_item, null);
        return new Holder(view);
    }


    @Override public void onBindViewHolder(final Holder holder, final int position) {
        NurseBean item = items.get(position);
        holder.name.setText(item.getCareItemTitle());
        try {
            holder.time.setText(
                item.getStartTime().substring(0, 5) + "-" + item.getEndTime().substring(0, 5));
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.view.setOnClickListener(new OnClickListenerNoDouble() {
            @Override public void myOnClick(View view) {
                itemClick.onItemClick(view, position);
            }
        });
        if (item.isChecked()) {
            holder.name.setTextColor(Color.WHITE);
            holder.time.setTextColor(Color.WHITE);
            if (position == items.size() - 1) {
                holder.bg.setBackgroundResource(R.mipmap.next_click_end);
            } else {
                holder.bg.setBackgroundResource(R.mipmap.next_click_3);
            }
        } else {
            if(item.isAllCompleted()){
                holder.name.setTextColor(context.getResources().getColor(R.color.color_name_gray));
                holder.time.setTextColor(context.getResources().getColor(R.color.thunder_1));
            }else{
                holder.name.setTextColor(context.getResources().getColor(R.color.black));
                holder.time.setTextColor(context.getResources().getColor(R.color.black));
            }
            if (position == items.size() - 1) {
                holder.bg.setBackgroundResource(R.mipmap.next_unclick_end);
            } else {
                holder.bg.setBackgroundResource(R.mipmap.next_unclick_3);
            }
        }
    }


    @Override public int getItemCount() {
        return items == null ? 0 : items.size();
    }


    class Holder extends RecyclerView.ViewHolder {
        TextView name;
        TextView time;
        View view;
        LinearLayout bg;


        public Holder(View itemView) {
            super(itemView);
            name = ((TextView) itemView.findViewById(R.id.item_name));
            time = ((TextView) itemView.findViewById(R.id.item_time));
            bg = (LinearLayout) itemView.findViewById(R.id.bg);
            view = itemView;
        }
    }


    public interface ItemClick {
        void onItemClick(View view, int position);
    }
}
