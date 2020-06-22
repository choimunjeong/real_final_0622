package com.nrr.hansol.spot_200510_hs;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import DB.Like_DbOpenHelper;
import Page1.Page1;

public class Page0_5 extends AppCompatActivity implements View.OnClickListener {
    TextView a1_round, a2_rest;
    TextView page5_later, page5_back;

    int[] score;

    private Like_DbOpenHelper mDbOpenHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page0_5);

        score = new int[8];

        a1_round = (TextView)findViewById(R.id.page0_5_a1);
        a2_rest = (TextView)findViewById(R.id.page0_5_a2);

        page5_later = (TextView)findViewById(R.id.page0_5_later);
        page5_back = (TextView) findViewById(R.id.page0_5_back);

        mDbOpenHelper = new Like_DbOpenHelper(Page0_5.this);
        mDbOpenHelper.open();
        mDbOpenHelper.create();

        // 대답 버튼 눌렸을 때
        a1_round.setOnClickListener(this);
        a2_rest.setOnClickListener(this);

        // 나중에하기 버튼 밑줄긋기
        page5_later.setPaintFlags(page5_later.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        // 나중에하기 버튼 눌렸을 때
        page5_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDbOpenHelper.open();
                mDbOpenHelper.deleteAllColumns();
                mDbOpenHelper.insertLikeColumn("0 3 0 0 1 0 0 0", "열정 개미", "자연속에서 힐링하기 좋아하는");
                mDbOpenHelper.close();

                score[1] = 3; score[4] = 1; score[5] = 0;
                Intent intent = new Intent(Page0_5.this, Page1.class);
                intent.putExtra("Main", score);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        // 뒤로가기 버튼 눌렀을 때
        page5_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 첫 번째 인자: 새로 불러오는 activity효과
                // 두 번째 인자: 현재 activity효과
                finish();
                overridePendingTransition(R.anim.hold, R.anim.anim_slide);
            }
        });

        // 이전 페이지 값 받아오기
        Intent intent = getIntent();
        score = intent.getIntArrayExtra("Page4");
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(), Page0_6.class);
        if (view.getId() == R.id.page0_5_a1){
            score[3] = 0;
            intent.putExtra("Page5",score);
        } else if (view.getId() == R.id.page0_5_a2) {
            score[3] = 1;
            intent.putExtra("Page5",score);
        }

        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_out, R.anim.hold);
    }


    // 뒤로가기 버튼 막기
    @Override
    public void onBackPressed() {
    }
}