package com.huangssh.jikelike;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private LikeView like1;
    private LikeView like2;
    private LikeView like3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        like1 = (LikeView) findViewById(R.id.like1);
        like1.setNewNum(109);

        like2 = (LikeView) findViewById(R.id.like2);
        like2.setNewNum(10999);

        like3 = (LikeView) findViewById(R.id.like3);
        like3.setNewNum(99999);
    }
}
