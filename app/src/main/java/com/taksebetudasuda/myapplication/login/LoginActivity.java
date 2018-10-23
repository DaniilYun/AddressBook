package com.taksebetudasuda.myapplication.login;

import android.support.v4.app.Fragment;

import com.taksebetudasuda.myapplication.SingleFragmentActivity;

/**
 * Created by Daniil on 15.10.2018.
 */
public class LoginActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new LoginFragment();
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
