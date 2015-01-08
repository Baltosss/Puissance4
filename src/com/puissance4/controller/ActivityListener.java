package com.puissance4.controller;

import android.app.Activity;
import android.view.View;

/**
 * Created by fred on 08/01/15.
 */
public abstract class ActivityListener implements View.OnClickListener {
    protected Activity context;

    public ActivityListener(Activity context) {
        this.context = context;
    }
}
