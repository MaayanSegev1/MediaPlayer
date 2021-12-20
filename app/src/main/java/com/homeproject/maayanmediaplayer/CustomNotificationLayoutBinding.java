package com.homeproject.maayanmediaplayer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.viewbinding.ViewBinding;



/* loaded from: classes.dex */
public final class CustomNotificationLayoutBinding implements ViewBinding {
    public final ImageButton exitBtnNotif;
    public final ImageButton nextBtn;
    public final ImageView picNotif;
    public final ImageButton playPauseBtn;
    public final ImageButton previousBtn;
    private final LinearLayout rootView;
    public final TextView titleTxtNotif;

    public CustomNotificationLayoutBinding(LinearLayout linearLayout, ImageButton imageButton, ImageButton imageButton2, ImageView imageView, ImageButton imageButton3, ImageButton imageButton4, TextView textView2) {
        this.rootView = linearLayout;
        this.exitBtnNotif = imageButton;
        this.nextBtn = imageButton2;
        this.picNotif = imageView;
        this.playPauseBtn = imageButton3;
        this.previousBtn = imageButton4;
        this.titleTxtNotif = textView2;
    }

    @Override
    public LinearLayout getRoot() {
        return this.rootView;
    }

    public static CustomNotificationLayoutBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, null, false);
    }

    public static CustomNotificationLayoutBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        View inflate = layoutInflater.inflate(R.layout.custom_notification_layout, viewGroup, false);
        if (z) {
            viewGroup.addView(inflate);
        }
        return bind(inflate);
    }

    public static CustomNotificationLayoutBinding bind(View view) {
        Log.i(MainActivity.LOG_TAG, "custom");
        return null;

    }
}