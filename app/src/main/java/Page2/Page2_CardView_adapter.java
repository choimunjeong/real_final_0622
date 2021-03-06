package Page2;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nrr.hansol.spot_200510_hs.R;

import java.util.List;

import Page2_1_X.Page2_1_X;

public class Page2_CardView_adapter extends RecyclerView.Adapter<Page2_CardView_adapter.ViewHolder> {

    Context context;
    private Page2 mainActivity;
    private String[] stay = new String[999];  // 하트의 클릭 여부
    private List<Recycler_item> cardview_items;  //리사이클러뷰 안에 들어갈 값 저장
   Page2_OnItemClick mCallback;
    String cityName, click;

    //메인에서 불러올 때, 이 함수를 씀
    public Page2_CardView_adapter(List<Recycler_item> items, Page2 mainActivity, String cityName, Page2_OnItemClick mCallback) {
        this.cardview_items = items;
        this.mainActivity = mainActivity;
        this.cityName = cityName;
        this.mCallback = mCallback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.page2_cardview_item, null);
        return new ViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Recycler_item item = cardview_items.get(position);

        Glide.with(context).load(item.getImage()).centerCrop().into(holder.image);
        holder.title.setText(item.getTitle());
        holder.type.setText(item.getType());

        click = mCallback.isClick(item.getContentviewID());
        if(click ==null){
            click = "";
        }
        if (click.equals(item.getContentviewID())) {
            holder.heart.setBackgroundResource(R.drawable.ic_heart_off);
            stay[position] = "ON";
        } else {
            holder.heart.setBackgroundResource(R.drawable.ic_icon_addmy);
            stay[position] = null;
        }

        //하트누르면 내부 데이터에 저장
        holder.heart.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                if (stay[position] == null) {
                    holder.heart.setBackgroundResource(R.drawable.ic_heart_off);
                    mCallback.make_db(item.getContentviewID(), item.getTitle(),cityName, item.getType(), item.getImage(), "1", item.getAreaCode(), item.getSigunguCode());   //countId랑 title을 db에 넣으려고 함( make_db라는 인터페이스 이용)
                    mCallback.make_dialog();                                       //db에 잘 넣으면 띄우는 다이얼로그(위와 마찬가지로 인터페이스 이용
                    stay[position] = "ON";
                } else {
                    holder.heart.setBackgroundResource(R.drawable.ic_icon_addmy);
                    mCallback.delete_db(item.getContentviewID());
                    stay[position] = null;
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (view.getId() == R.id.page2_linearitem) {
                    Intent intent = new Intent(context, Page2_1_X.class);
                    intent.putExtra("title", item.getTitle());
                    intent.putExtra("contentID", item.getContentviewID());
                    intent.putExtra("contenttypeid", item.getType());
                    intent.putExtra("image", item.getImage());
                    intent.putExtra("cityname", cityName);
                    intent.putExtra("areaCode", item.getAreaCode());
                    intent.putExtra("sigunguCode", item.getSigunguCode());
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cardview_items.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, type;
        CardView cardview;
        Button heart;

        public ViewHolder(View itemView) {
            super(itemView);
            heart = (Button) itemView.findViewById(R.id.page2_cardview_heart);
            image = (ImageView) itemView.findViewById(R.id.page2_no_image);
            title = (TextView) itemView.findViewById(R.id.page2_cardview_title);
            type = (TextView) itemView.findViewById(R.id.page2_cardview_type);
            cardview = (CardView) itemView.findViewById(R.id.page2_cardview);
        }
    }
}
