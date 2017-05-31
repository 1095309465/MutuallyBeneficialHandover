package com.jhzy.nursinghandover.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jhzy.nursinghandover.R;
import com.jhzy.nursinghandover.api.OnClickListenerNoDouble;
import com.jhzy.nursinghandover.widget.TaskView;

/**
 * Created by Administrator on 2017/3/27.
 */

public class BedPopwindowUtils {

    private Context mContext;
    private LayoutInflater from;
    private int screenHeight;
    private int screenWidth;

    public BedPopwindowUtils(Context context) {
        this.mContext = context;
        from = LayoutInflater.from(context);
        screenHeight = ScreenUtils.getScreenHeight(mContext);
        screenWidth = ScreenUtils.getScreenWidth(mContext);
    }


    /**
     * 展示任务
     *
     * @param v
     * @return
     */
    public ViewHolder showTaskPop(View v) {
        View inflate = from.inflate(R.layout.pop_task, null);
        final ViewHolder vh = new ViewHolder(inflate);
        vh.popupWindow = new PopupWindow(inflate, (int) (screenWidth * 0.6), (int) (screenHeight * 0.65), false);

        vh.cancel.setOnClickListener(new OnClickListenerNoDouble() {
            @Override
            public void myOnClick(View view) {
                vh.popupWindow.dismiss();
            }
        });
        vh.bedDetailSure.setEnabled(false);
        vh.popupWindow.setAnimationStyle(R.style.from_bottom_anim);
        vh.popupWindow.setBackgroundDrawable(new BitmapDrawable());
        vh.popupWindow.setFocusable(true);
        vh.popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        lp.alpha = 0.5f; //0.0-1.0
        ((Activity) mContext).getWindow().setAttributes(lp);
        setBackgroundAlpha(((Activity) mContext), 0.5f);
        vh.popupWindow.setOnDismissListener(new poponDismissListener());
        return vh;
    }

    /**
     * 注意事项
     * @param v
     * @return
     */
    public ViewHolder showAttention(View v) {
        View inflate = from.inflate(R.layout.item_bed_showattention_detail, null);
        final ViewHolder vh = new ViewHolder(inflate);
        vh.popupWindow = new PopupWindow(inflate, screenWidth * 3 / 4, screenHeight * 3 / 4, false);
        vh.attentionLayout = (LinearLayout) inflate.findViewById(R.id.bed_attention_context_group);
        vh.cancel.setOnClickListener(new OnClickListenerNoDouble() {
            @Override
            public void myOnClick(View view) {
                vh.popupWindow.dismiss();
            }
        });


        vh.popupWindow.setAnimationStyle(R.style.from_bottom_anim);
        vh.popupWindow.setBackgroundDrawable(new BitmapDrawable());
        vh.popupWindow.setFocusable(true);
        vh.popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        lp.alpha = 0.5f; //0.0-1.0
        ((Activity) mContext).getWindow().setAttributes(lp);
        setBackgroundAlpha(((Activity) mContext), 0.5f);
        vh.popupWindow.setOnDismissListener(new poponDismissListener());
        return vh;
    }

    public class ViewHolder {
        public View cancel;
        public SimpleDraweeView titleIcon;
        public TextView titleTxt;
        public TaskView txt1;
        public TaskView txt2;
        public PopupWindow popupWindow;
        public TaskView txt3;
        public TaskView txt4;
        public LinearLayout attentionLayout;
        public RelativeLayout bedDetailSure;
        public TextView sureTxt;
        public ImageView sureImage;

        public ViewHolder(View view) {
            cancel = view.findViewById(R.id.bed_detail_cancel);

            titleIcon = ((SimpleDraweeView) view.findViewById(R.id.title_icon));
            titleTxt = ((TextView) view.findViewById(R.id.title_txt));
            txt1 = ((TaskView) view.findViewById(R.id.text1));
            txt2 = ((TaskView) view.findViewById(R.id.text2));
            txt3 = ((TaskView) view.findViewById(R.id.text3));
            txt4 = ((TaskView) view.findViewById(R.id.text4));
            bedDetailSure = (RelativeLayout) view.findViewById(R.id.bed_detail_sure);
            sureTxt = ((TextView) view.findViewById(R.id.text_sure));
            sureImage = ((ImageView) view.findViewById(R.id.image_sure));
        }
    }

    /**
     * 设置页面的透明度
     *
     * @param bgAlpha 1表示不透明
     */
    private static void setBackgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        if (bgAlpha == 1) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        activity.getWindow().setAttributes(lp);
    }

    /**
     * 主要是为了将背景透明度改回来
     *
     * @author cg
     */
    private class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            setBackgroundAlpha(((Activity) mContext), 1f);
        }
    }
}
