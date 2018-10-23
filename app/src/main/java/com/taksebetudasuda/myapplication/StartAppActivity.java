package com.taksebetudasuda.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.taksebetudasuda.myapplication.login.LoginFragment;

public class StartAppActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPref.init(getApplicationContext());
        boolean ok = SharedPref.read(SharedPref.IS_SELECT,false);
        if (!ok){
            Intent intent =LoginFragment.newIntent(this);
            startActivity(intent);
        }else{
            Intent intent =ItemListFragment.newIntent(this);
            startActivity(intent);
        }
    }
}
