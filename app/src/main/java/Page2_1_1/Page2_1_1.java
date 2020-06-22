package Page2_1_1;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nrr.hansol.spot_200510_hs.R;

import java.util.ArrayList;

public class Page2_1_1 extends AppCompatActivity {
    Page2_1_1_ViewPagerAdapter viewpager_adapter;
    ArrayList<course> items = new ArrayList<>();

    Recyclerview_Rearrange recyclerview_rearrange;

    //관광지 주제별 코스를 저장하는 배열
    String getSubject;
    String[] st1;
    String[] st2;
    String[] st3;
    String[] st4;

    // 앞에서 받아온 포지션
    int numCourse;

    //코스 전체 추가 버튼
    Button schedule_btn;

    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page2_1_1);

        //앞에서 값을 받아옴
        Intent intent = getIntent();
        getSubject = intent.getStringExtra("subject_title");
        numCourse = intent.getIntExtra("position", numCourse);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.page2_1_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //뷰페이저 어댑터를 연결해주면서 프래그먼트 연결
        viewpager_adapter = new Page2_1_1_ViewPagerAdapter(getSupportFragmentManager(), items, recyclerview_rearrange, numCourse);
        recyclerView.setAdapter(viewpager_adapter);

        schedule_btn = (Button) findViewById(R.id.page2_1_schedulePlus_btn);
        progress = (ProgressBar) findViewById(R.id.page2_1_1_progress);


        //뒤로가기 버튼 구현
        ImageView back_btn = (ImageView) findViewById(R.id.page2_1_back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewpager_adapter.onRearrange();
                onBackPressed();
            }
        });

        getData();
    }

    private void getData() {
        switch (getSubject) {
            case "자연":
                st1 = new String[]{"양평", "여수", "제천"};
                st2 = new String[]{"강릉", "남원", "경주"};
                st3 = new String[]{"동해", "임실", "포항"};
                st4 = new String[]{"태백", "군산", "영덕"};
                break;
            case "역사":
                st1 = new String[]{"광주", "전주", "나주", "경주", "제천"};
                st2 = new String[]{"김제", "남원", "보성", "안동", "충주"};
                st3 = new String[]{"익산", "곡성", "순천", "영주", "청주"};
                st4 = new String[]{"군산", "임실", "화순", "서울", "천안"};
                break;
            case "휴양":
                st1 = new String[]{"아산", "동해", "포항"};
                st2 = new String[]{"보령", "영주", "부산"};
                st3 = new String[]{"정읍", "제천", "아산"};
                st4 = new String[]{"여수", "양평", "서울"};
                break;
            case "체험":
                st1 = new String[]{"익산", "곡성", "서울"};
                st2 = new String[]{"보성", "순천", "양평"};
                st3 = new String[]{"광양", "광양", "강릉"};
                st4 = new String[]{"순천", "논산", "보성"};
                break;
            case "산업":
                st1 = new String[]{"용산","김제"};
                st2 = new String[]{"대전","광양"};
                st3 = new String[]{"대구","창원"};
                st4 = new String[]{"포항","부전"};
                break;
            case "건축/조형":
                st1 = new String[]{"서울", "용산"};
                st2 = new String[]{"수원", "양평"};
                st3 = new String[]{"군산", "가평"};
                st4 = new String[]{"부산", "춘천"};
                break;
            case "문화":
                st1 = new String[]{"목포", "수원"};
                st2 = new String[]{"광주", "천안"};
                st3 = new String[]{"공주", "대전"};
                st4 = new String[]{"청주", "대구"};
                break;
            case "레포츠":
                st1 = new String[]{"덕소", "서울"};
                st2 = new String[]{"단양", "덕소"};
                st3 = new String[]{"영주", "광명"};
                st4 = new String[]{"안동", "가평"};
                break;
            default:
                break;
        }

        if(st1.length != 0 ){
            for (int i = 0; i < st1.length; i++) {
                items.add(new course(getSubject, st1[i], st2[i], st3[i], st4[i]));
            }
        }


        // adapter의 값이 변경(+추가) 되었다는 것을 알려줌 .
        viewpager_adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        viewpager_adapter.onRearrange();
        overridePendingTransition(0,0);
    }



    //뒤로가기 누르면 확장된 레이아웃을 닫는 인터페이스
    public interface Recyclerview_Rearrange {
        void onRearrange();
    }
}
