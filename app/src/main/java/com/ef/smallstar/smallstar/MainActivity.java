package com.ef.smallstar.smallstar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by ext.ezreal.cai on 2016/11/23.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void toTest(View view) {
        startActivity(new Intent(this, TestActivity.class));
        finish();
    }

    public void toTest3(View view) {

        startActivity(new Intent(this,Test3Activity.class));
        finish();

    }
}


