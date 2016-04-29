package com.dikulous.ric.orderapp.order.monitor;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dikulous.ric.orderapp.R;

/**
 * Created by ric on 29/04/16.
 */
public class MonitorOrderStep {
    Context context;
    RelativeLayout layout;
    ImageView image;
    TextView text;
    int defaultDrawable;
    int selectedDrawable;


    public MonitorOrderStep(Context context, RelativeLayout layout, ImageView image, TextView text, int defaultDrawable, int selectedDrawable) {
        this.context = context;
        this.layout = layout;
        this.image = image;
        this.text = text;
        this.defaultDrawable = defaultDrawable;
        this.selectedDrawable = selectedDrawable;
    }

    public void setDefault(){
        image.setImageResource(defaultDrawable);
        text.setTextColor(context.getResources().getColor(R.color.black));
        layout.setBackgroundColor(context.getResources().getColor(R.color.white));
    }

    public void setSelected(){
        image.setImageResource(selectedDrawable);
        text.setTextColor(context.getResources().getColor(R.color.white));
        layout.setBackgroundColor(context.getResources().getColor(R.color.stepSelected));
    }
}
