package com.arabianmaps.compass.Compass_Utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

public class Compass_C1105Ui {
    public static int SCALE_HEIGHT = 1920;
    public static int SCALE_WIDTH = 1080;

    public static void setHeightWidth(Context context, View view, int i, int i2) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int i3 = (displayMetrics.widthPixels * i) / SCALE_WIDTH;
        int i4 = (displayMetrics.heightPixels * i2) / SCALE_HEIGHT;
        view.getLayoutParams().width = i3;
        view.getLayoutParams().height = i4;
    }

    public static void setHeightWidthAsWidth(Context context, View view, int i, int i2) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int i3 = (displayMetrics.widthPixels * i) / SCALE_WIDTH;
        int i4 = (displayMetrics.widthPixels * i2) / SCALE_WIDTH;
        view.getLayoutParams().width = i3;
        view.getLayoutParams().height = i4;
    }

    public static void setHeightWidth2(Context context, View view, int i, int i2) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int i3 = (displayMetrics.widthPixels * i) / SCALE_WIDTH;
        int i4 = (displayMetrics.widthPixels * i2) / SCALE_WIDTH;
        view.getLayoutParams().width = i3;
        view.getLayoutParams().height = i4;
    }

    public static void setWidth(Context context, View view, int i) {
        view.getLayoutParams().width = (context.getResources().getDisplayMetrics().widthPixels * i) / SCALE_WIDTH;
    }

    public static void setHeight(Context context, View view, int i) {
        view.getLayoutParams().height = (context.getResources().getDisplayMetrics().heightPixels * i) / SCALE_HEIGHT;
    }

    public static void setPadding(Context context, View view, int i, int i2, int i3, int i4) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        view.setPadding((displayMetrics.widthPixels * i) / SCALE_WIDTH, (displayMetrics.heightPixels * i2) / SCALE_HEIGHT, (displayMetrics.widthPixels * i3) / SCALE_WIDTH, (displayMetrics.heightPixels * i4) / SCALE_HEIGHT);
    }

    public static void setPadding(Context context, View view, int i) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int i2 = (displayMetrics.widthPixels * i) / SCALE_WIDTH;
        int i3 = (displayMetrics.heightPixels * i) / SCALE_HEIGHT;
        view.setPadding(i2, i3, i2, i3);
    }

    public static void setPaddingLeft(Context context, View view, int i) {
        view.setPadding((context.getResources().getDisplayMetrics().widthPixels * i) / SCALE_WIDTH, 0, 0, 0);
    }

    public static void setPaddingTop(Context context, View view, int i) {
        view.setPadding(0, (context.getResources().getDisplayMetrics().heightPixels * i) / SCALE_HEIGHT, 0, 0);
    }

    public static void setPaddingRight(Context context, View view, int i) {
        view.setPadding(0, 0, (context.getResources().getDisplayMetrics().widthPixels * i) / SCALE_WIDTH, 0);
    }

    public static void setPaddingBottom(Context context, View view, int i) {
        view.setPadding(0, 0, 0, (context.getResources().getDisplayMetrics().heightPixels * i) / SCALE_HEIGHT);
    }

    public static void setMargins(Context context, View view, int i, int i2, int i3, int i4) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int i5 = (displayMetrics.widthPixels * i) / SCALE_WIDTH;
        int i6 = (displayMetrics.heightPixels * i2) / SCALE_HEIGHT;
        int i7 = (displayMetrics.widthPixels * i3) / SCALE_WIDTH;
        int i8 = (displayMetrics.heightPixels * i4) / SCALE_HEIGHT;
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).setMargins(i5, i6, i7, i8);
            view.requestLayout();
        }
    }

    public static void setMarginsAsWidth(Context context, View view, int i, int i2, int i3, int i4) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int i5 = (displayMetrics.widthPixels * i) / SCALE_WIDTH;
        int i6 = (displayMetrics.heightPixels * i2) / SCALE_WIDTH;
        int i7 = (displayMetrics.widthPixels * i3) / SCALE_WIDTH;
        int i8 = (displayMetrics.heightPixels * i4) / SCALE_WIDTH;
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).setMargins(i5, i6, i7, i8);
            view.requestLayout();
        }
    }

    public static void setMargins(Context context, View view, int i) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int i2 = (displayMetrics.widthPixels * i) / SCALE_WIDTH;
        int i3 = (displayMetrics.widthPixels * i) / SCALE_WIDTH;
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).setMargins(i2, i3, i2, i3);
            view.requestLayout();
        }
    }

    public static void setMarginLeft(Context context, View view, int i) {
        int i2 = (context.getResources().getDisplayMetrics().widthPixels * i) / SCALE_WIDTH;
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).setMargins(i2, 0, 0, 0);
            view.requestLayout();
        }
    }

    public static void setMarginTop(Context context, View view, int i) {
        int i2 = (context.getResources().getDisplayMetrics().heightPixels * i) / SCALE_HEIGHT;
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).setMargins(0, i2, 0, 0);
            view.requestLayout();
        }
    }

    public static void setMarginTopWidth(Context context, View view, int i) {
        int i2 = (context.getResources().getDisplayMetrics().heightPixels * i) / SCALE_WIDTH;
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).setMargins(0, i2, 0, 0);
            view.requestLayout();
        }
    }

    public static void setMarginRight(Context context, View view, int i) {
        int i2 = (context.getResources().getDisplayMetrics().widthPixels * i) / SCALE_WIDTH;
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).setMargins(0, 0, i2, 0);
            view.requestLayout();
        }
    }

    public static void setMarginBottom(Context context, View view, int i) {
        int i2 = (context.getResources().getDisplayMetrics().heightPixels * i) / SCALE_HEIGHT;
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).setMargins(0, 0, 0, i2);
            view.requestLayout();
        }
    }

    public static float convertDpToPixel(float f, Context context) {
        return f * (context.getResources().getDisplayMetrics().densityDpi / 160.0f);
    }
}
