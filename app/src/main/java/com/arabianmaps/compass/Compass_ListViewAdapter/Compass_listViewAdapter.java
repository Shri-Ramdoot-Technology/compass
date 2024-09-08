package com.arabianmaps.compass.Compass_ListViewAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.arabianmaps.compass.R;
import com.arabianmaps.compass.Compass_Data.Compass_oneWeekData;
import com.arabianmaps.compass.Compass_Utils.Compass_C1105Ui;
import java.util.List;

/* loaded from: classes2.dex */
public class Compass_listViewAdapter extends BaseAdapter {
    private Context context;
    List<Compass_oneWeekData> data;
    LayoutInflater inflater;

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return 0L;
    }

    public Compass_listViewAdapter(List<Compass_oneWeekData> list, Context context) {
        this.data = list;
        this.context = context;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.data.size();
    }

    @Override // android.widget.Adapter
    public Compass_oneWeekData getItem(int i) {
        return this.data.get(i);
    }

    @Override // android.widget.Adapter
    @SuppressLint({"ViewHolder", "SetTextI18n"})
    public View getView(int i, View view, ViewGroup viewGroup) {
        this.inflater = (LayoutInflater) this.context.getSystemService("layout_inflater");
        View inflate = this.inflater.inflate(R.layout.custm_adpt_for_listview, (ViewGroup) null);
        ConstraintLayout constraintLayout = (ConstraintLayout) inflate.findViewById(R.id.constraint12Day);
        Compass_C1105Ui.setHeight(inflate.getContext(), constraintLayout, 139);
        Compass_C1105Ui.setMarginTop(inflate.getContext(), constraintLayout, 45);
        Compass_C1105Ui.setHeightWidth(inflate.getContext(), inflate.findViewById(R.id.oblique), 2, 50);
        ImageView imageView = (ImageView) inflate.findViewById(R.id.DividerLayout);
        Compass_C1105Ui.setHeight(inflate.getContext(), imageView, 4);
        Compass_C1105Ui.setMarginTop(inflate.getContext(), imageView, 27);
        Compass_C1105Ui.setMarginLeft(inflate.getContext(), imageView, 31);
        Compass_C1105Ui.setMarginsAsWidth(inflate.getContext(), imageView, 31, 0, 31, 0);
        ((TextView) inflate.findViewById(R.id.date)).setText(this.data.get(i).getDate());
        ((TextView) inflate.findViewById(R.id.humidityAdt)).setText(String.valueOf(this.data.get(i).getHumidity() + "%"));
        ((ImageView) inflate.findViewById(R.id.weatherImglis)).setImageResource(this.data.get(i).getWeatherIcon());
        ((TextView) inflate.findViewById(R.id.minTemp)).setText(String.valueOf(this.data.get(i).getMinTemp()) + " ℃");
        ((TextView) inflate.findViewById(R.id.maxTemp)).setText(String.valueOf(this.data.get(i).getMaxTemp() + " ℃"));
        return inflate;
    }
}
