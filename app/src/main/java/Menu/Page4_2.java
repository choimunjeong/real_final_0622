package Menu;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.nrr.hansol.spot_200510_hs.Page0_9_PopUp;
import com.nrr.hansol.spot_200510_hs.R;

import java.util.ArrayList;

import DB.DbOpenHelper;
import DB.Like_DbOpenHelper;
import DB.Second_MainDBHelper;
import DB.Train_DbOpenHelper;
import Page1.EndDrawerToggle;
import Page1.Main_RecyclerviewAdapter;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class Page4_2 extends AppCompatActivity implements Page4_sendData {

    //데베 관련
    private int first_data;
    private Train_DbOpenHelper mDbOpenHelper;
    private ArrayList<Train_Data> train_data = new ArrayList<Train_Data>();
    private ArrayList<Train_Data_forRecyclerview> train_data_forRecyclerview = new ArrayList<Train_Data_forRecyclerview>();

    //리사이클러뷰 관련
    private RecyclerView recyclerView;
    private Page4_2_adapter adapter;
    private Context context;
    private TextView info_no_data;

    //메뉴 관련
    private ImageButton menu_edit;
    private ImageView userImg;
    private TextView userText1;
    private TextView userText2;
    private RecyclerView recyclerView1;
    private Switch positionBtn;
    private Switch alramBtn;
    Main_RecyclerviewAdapter adapter2;
    ArrayList<String> name = new ArrayList<>();
    private Toolbar toolbar2;
    private DrawerLayout drawer;
    private EndDrawerToggle mDrawerToggle;
    private boolean EndDrawerToggle_open = false;
    ImageButton logo;

    //프로필 관련
    ImageButton main_schedule;
    ImageButton main_register;
    ImageButton main_like;
    ImageView menu_img;
    TextView menu_text1, menu_text2;

    // 취향파악 부분
    private Like_DbOpenHelper mLikeDpOpenHelper;
    String  like, nickName, sub;
    String mScore[] = new String[8];
    int[] score = new int[8];
    ImageButton edit_nickname;
    private Second_MainDBHelper second_mainDBHelper;
    private String second_key;
    private DbOpenHelper spotDbOpenHelper;

    // 찜한 여행지 저장하는 리스트
    private ArrayList<String > mySpot = new ArrayList<String >();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page4_2);
        context = getApplicationContext();

        //데베 관련
        second_mainDBHelper = new Second_MainDBHelper(Page4_2.this);
        second_mainDBHelper.open();
        second_mainDBHelper.create();

        mDbOpenHelper = new Train_DbOpenHelper(Page4_2.this);
        mDbOpenHelper.open();
        mDbOpenHelper.create();
        showDatabase();

        // 찜한 관광지 DB열기
        spotDbOpenHelper = new DbOpenHelper(this);
        spotDbOpenHelper.open();
        spotDbOpenHelper.create();
        showSpotDatabase();


        //데이터가 없으면
        info_no_data = (TextView)findViewById(R.id.page4_2_noData);
        if(train_data.size() == 0){
            info_no_data.setVisibility(View.VISIBLE);
        } else
            info_no_data.setVisibility(View.INVISIBLE);


        // 리사이클러뷰 설정
        recyclerView = findViewById(R.id.page4_2_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new Page4_2_adapter(context, train_data_forRecyclerview, this);
        recyclerView.setAdapter(adapter);

        //객체 연결
        context = getApplicationContext();
        toolbar2 = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        userImg = (ImageView)findViewById(R.id.menu_userImage);
        userText1 = (TextView)findViewById(R.id.menu_text1);
        userText2 = (TextView)findViewById(R.id.menu_text2);
        positionBtn = (Switch)findViewById(R.id.menu_postion_btn);
        recyclerView1 = (RecyclerView)findViewById(R.id.menu_recyclerview1);

        menu_img = (ImageView)findViewById(R.id.menu_userImage);
        menu_text1 = (TextView) findViewById(R.id.menu_text1);
        menu_text2 = (TextView) findViewById(R.id.menu_text2);
        edit_nickname = (ImageButton)findViewById(R.id.menu_edit_btn);

        // 취향파악 DB열기
        mLikeDpOpenHelper = new Like_DbOpenHelper(this);
        mLikeDpOpenHelper.open();
        mLikeDpOpenHelper.create();
        showLikeDB();


        //메인 로고
        logo = (ImageButton) findViewById(R.id.main_logo_page4_2);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Page1.Page1.class);
                intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("Logo", "1");
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });

        // 프로필편집 버튼 눌렀을 때
        edit_nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Page0_9_PopUp.class);
                intent.putExtra("서브이름", sub);
                intent.putExtra("닉네임", nickName);
                intent.putExtra("Page9",score);
                intent.addFlags(intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(intent, 1);
            }
        });


        mDrawerToggle = new EndDrawerToggle(this,drawer,toolbar2,R.string.open_drawer,R.string.close_drawer){
            @Override //드로어가 열렸을때
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                EndDrawerToggle_open = true;
            }
            @Override //드로어가 닫혔을때
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                EndDrawerToggle_open = false;
            }
        };

        setSupportActionBar(toolbar2);
        drawer.addDrawerListener(mDrawerToggle);

        //등록한 일정이 있는지 검사
        Cursor iCursor = second_mainDBHelper.selectColumns();
        while (iCursor.moveToNext()){
            String Key = iCursor.getString(iCursor.getColumnIndex("userid"));
            second_key = Key;
        }

        //메뉴 안 내용 구성
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        adapter2 = new Main_RecyclerviewAdapter(name, context, mySpot.size(), second_key);
        recyclerView1.setAdapter(adapter2);

        //리사이클러뷰 헤더
        name.add("0");
        name.add("1");
        name.add("2");

        //툴바 타이틀 없애기
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    //데베 값 받기
    public void showDatabase(){
        Cursor iCursor = mDbOpenHelper.selectColumns();
        train_data.clear();

        while(iCursor.moveToNext()){
            String Key = iCursor.getString(iCursor.getColumnIndex("number"));
            String tempDate = iCursor.getString(iCursor.getColumnIndex("date"));
            train_data.add(new Train_Data( Key, tempDate));
        }

        //값 정제
        first_data = 0;
        for(int i =0; i < train_data.size(); i++){
            if(!train_data.get(first_data).keyDate.equals(train_data.get(i).keyDate)){
                train_data_forRecyclerview.add(new Train_Data_forRecyclerview(
                        train_data.get(i-1).keyDate,
                        train_data.get(first_data).date,
                        train_data.get(i-1).date));
                first_data = i+1;
            }
            if(i == train_data.size()-1){
                train_data_forRecyclerview.add(new Train_Data_forRecyclerview(
                        train_data.get(i).keyDate,
                        train_data.get(first_data).date,
                        train_data.get(i).date));
            }
        }

    }


    public void showLikeDB() {
        Cursor likeCursor = mLikeDpOpenHelper.selectColumns();

        while (likeCursor.moveToNext()) {
            String tempLike = likeCursor.getString(likeCursor.getColumnIndex("userid"));
            String tempNickname = likeCursor.getString(likeCursor.getColumnIndex("nickname"));
            String tempSub = likeCursor.getString(likeCursor.getColumnIndex("sub"));
            like = tempLike;
            nickName = tempNickname;
            sub = tempSub;
        }

        menu_text1.setText(sub);
        menu_text2.setText(nickName);

        // DB에 값이 있다면
        if (like != null) {
            mScore = like.split(" ");
            for (int i = 0 ; i < mScore.length ; i++) {
                score[i] = Integer.parseInt(mScore[i]); // Int로 캐스팅
            }

            if (score[2] == 0 && score[3] == 0) {
                menu_img.setBackgroundResource(R.drawable.ic_ant);
            }

            if (score[2] == 1 && score[3] == 1) {
                menu_img.setBackgroundResource(R.drawable.ic_sloth);
            }

            if (score[2] != score[3]) {
                if (score[6] == 0) {
                    menu_img.setBackgroundResource(R.drawable.ic_otter);
                } else if (score[2] == 1 ) {
                    menu_img.setBackgroundResource(R.drawable.ic_soul);
                } else if (score[2] == 0) {
                    menu_img.setBackgroundResource(R.drawable.ic_excel);
                }
            }

            if (score[1] == 0) {
                if (score[4] == 0 && score[5] == 1) {
                    menu_img.setBackgroundResource(R.drawable.ic_sprout);
                }
                else if (score[4] == 1&&score[5] == 0) {
                    menu_img.setBackgroundResource(R.drawable.ic_chick);
                }
            }

            if (score[1] == 4&&score[5] == 0) {
                menu_img.setBackgroundResource(R.drawable.ic_chick);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("result");
                menu_text2.setText(result);
                nickName = result;
            }
        }
    }







    public void showSpotDatabase(){
        Cursor iCursor = spotDbOpenHelper.selectColumns();
        //iCursor.moveToFirst();
        Log.d("showDatabase", "DB Size: " + iCursor.getCount());
        mySpot.clear();

        while(iCursor.moveToNext()){
            String tempName = iCursor.getString(iCursor.getColumnIndex("name"));

            mySpot.add(tempName);
        }
    }


    //데베 삭제할 때
    @Override
    public void onDelete(final String number2, final int position) {
        final String key = number2.trim();
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Page4_2.this);
        builder.setMessage("저장한 스케쥴을 삭제하시겠습니까?");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //뷰페이져 선택된거 삭제
                SharedPreferences a = getSharedPreferences("viewpager", MODE_PRIVATE);
                final SharedPreferences.Editor editor = a.edit();
                editor.remove(key);

                if(key.equals(second_key)){
                    second_mainDBHelper.deleteAllColumns();
                    second_mainDBHelper.close();
                }

                mDbOpenHelper.open();
                mDbOpenHelper.deleteColumnByKey(key);
                mDbOpenHelper.close();
                train_data_forRecyclerview.remove(position);
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    @Override
    public void onBackPressed() {
        if (EndDrawerToggle_open) {
            drawer.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }
}


//데베에서 바로 받는 값 구성
class Train_Data {
    String keyDate;
    String date;

    public Train_Data(String keyDate, String date) {
        this.keyDate = keyDate;
        this.date = date;
    }
}




//데베값 정제한 값 구성
class Train_Data_forRecyclerview {
    String keyDate;
    String startdate;
    String enddate;

    public Train_Data_forRecyclerview(String keyDate, String startdate, String enddate) {
        this.keyDate = keyDate;
        this.startdate = startdate;
        this.enddate = enddate;
    }


}

