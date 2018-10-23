package com.taksebetudasuda.myapplication;

import android.support.v4.app.Fragment;

public class ItemActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new ItemFragment();
    }
}
