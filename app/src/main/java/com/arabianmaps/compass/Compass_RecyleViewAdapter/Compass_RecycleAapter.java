package com.arabianmaps.compass.Compass_RecyleViewAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.arabianmaps.compass.R;
import com.arabianmaps.compass.Compass_Data.Compass_weatherModel;
import com.arabianmaps.compass.Compass_Utils.Compass_C1105Ui;
import java.util.List;

/* loaded from: classes2.dex */
public class Compass_RecycleAapter extends RecyclerView.Adapter<Compass_RecycleAapter.Myview> {
    private Context context;
    private List<Compass_weatherModel> weatherModels;

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    @NonNull
    public Myview onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Myview(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custm_adpt_layout_for_weather, viewGroup, false));
    }

    public Compass_RecycleAapter(List<Compass_weatherModel> list, Context context) {
        this.weatherModels = list;
        this.context = context;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(@NonNull Myview myview, int i) {
        if (this.weatherModels.size() > 0) {
            myview.weatherImg.setImageResource(this.weatherModels.get(i).getWeatherImage());
            TextView textView = myview.tempView;
            textView.setText(this.weatherModels.get(i).getTemprature() + "");
            myview.timeView.setText(this.weatherModels.get(i).getTime());
            return;
        }
        Log.e("RecycleAdapter", "No value ");
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        List<Compass_weatherModel> list = this.weatherModels;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    /* loaded from: classes2.dex */
    public class Myview extends RecyclerView.ViewHolder {
        ConstraintLayout constraintLayout;
        ImageView img_celsisus;
        TextView tempView;
        TextView timeView;
        ImageView weatherImg;
        ConstraintLayout weatherLayout;

        public Myview(@NonNull View view) {
            super(view);
            this.weatherLayout = (ConstraintLayout) view.findViewById(R.id.weatherLayout);
            this.weatherImg = (ImageView) view.findViewById(R.id.weatherImg);
            this.tempView = (TextView) view.findViewById(R.id.temp);
            this.timeView = (TextView) view.findViewById(R.id.time1);
            this.img_celsisus = (ImageView) view.findViewById(R.id.img_celsius);
            Compass_C1105Ui.setMarginLeft(view.getContext(), this.img_celsisus, 10);
            Compass_C1105Ui.setMarginTop(view.getContext(), this.img_celsisus, 18);
            Compass_C1105Ui.setHeightWidth(view.getContext(), this.img_celsisus, 10, 10);
            Compass_C1105Ui.setHeightWidth(view.getContext(), this.weatherLayout, 117, 276);
            Compass_C1105Ui.setMarginTop(view.getContext(), this.weatherLayout, 20);
            Compass_C1105Ui.setMarginRight(view.getContext(), this.weatherLayout, 48);
            this.constraintLayout = (ConstraintLayout) view.findViewById(R.id.parentConstraint);
            Compass_C1105Ui.setHeight(view.getContext(), this.constraintLayout, 328);
            Compass_C1105Ui.setHeightWidth(view.getContext(), this.timeView, 90, 39);
            Compass_C1105Ui.setMarginTop(view.getContext(), this.timeView, 32);
            Compass_C1105Ui.setHeightWidth(view.getContext(), this.weatherImg, 76, 40);
            Compass_C1105Ui.setHeight(view.getContext(), this.tempView, 53);
        }
    }
}
