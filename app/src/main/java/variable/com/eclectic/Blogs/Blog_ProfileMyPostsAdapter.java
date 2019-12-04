package variable.com.eclectic.Blogs;

import android.app.MediaRouteButton;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.BreakIterator;
import java.util.ArrayList;

import variable.com.eclectic.R;

import static android.content.Context.MODE_PRIVATE;

public class Blog_ProfileMyPostsAdapter extends RecyclerView.Adapter<Blog_ProfileMyPostsAdapter.MyViewHolder>{

    Context context;
    private ArrayList<BlogPosts_DataModel> dataModelArrayList;
    private ArrayList<String> postIdArrayList;
    private DatabaseReference rootRef;
    private View view;
    private SharedPreferences sharedPreferences;
    private String email;
    private String userId;
    private String firstName;
    private String lastName;
    private String currentRoom;

    public Blog_ProfileMyPostsAdapter(ArrayList<BlogPosts_DataModel> dataModelArrayList, Context context, ArrayList<String> postIdArrayList)
    {
        this.dataModelArrayList = dataModelArrayList;
        this.context = context;
        this.postIdArrayList = postIdArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        sharedPreferences = context.getSharedPreferences("MY_EMAIL",MODE_PRIVATE);
        email = sharedPreferences.getString("EMAIL","");
        userId = sharedPreferences.getString("UID","");
        firstName = sharedPreferences.getString("FIRST_NAME","");
        lastName = sharedPreferences.getString("LAST_NAME","");
        currentRoom = sharedPreferences.getString("CURRENT_ROOM","");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        rootRef = database.getReference(currentRoom).child("Blogzone");

        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        view = layoutInflater.inflate(R.layout.blog_mypost_view,viewGroup,false);

        MyViewHolder myViewHolder = new MyViewHolder(view,context,dataModelArrayList);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, final int position) {

        final String title = dataModelArrayList.get(position).getTitle();
        final String desc = dataModelArrayList.get(position).getDesc();
        final String url = dataModelArrayList.get(position).getImageUrl();

        viewHolder.tv_blog_card_myPostTitle.setText(title);
        if(url!=null) {
            viewHolder.tv_blog_card_myPostDesc.setVisibility(View.GONE);
            viewHolder.mypost_image.setVisibility(View.VISIBLE);
            Picasso.with(context).load(url).into(viewHolder.mypost_image);
        }
        else {
            viewHolder.mypost_image.setVisibility(View.GONE);
            viewHolder.tv_blog_card_myPostDesc.setVisibility(View.VISIBLE);
            viewHolder.tv_blog_card_myPostDesc.setText(desc);
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String post_key = postIdArrayList.get(position);
                Intent singleActivity = new Intent(context.getApplicationContext(), Bolg_MyPost_Page.class);
                singleActivity.putExtra("PostID", post_key);
                singleActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(singleActivity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_blog_card_myPostTitle;
        public TextView tv_blog_card_myPostDesc;
        private ImageView mypost_image;
        ArrayList<BlogPosts_DataModel> dataModelArrayList;

        public MyViewHolder(@NonNull View itemView, Context context, ArrayList<BlogPosts_DataModel> dataModelArrayList) {
            super(itemView);

            this.dataModelArrayList = dataModelArrayList;
            tv_blog_card_myPostTitle = (TextView)itemView.findViewById(R.id.tv_blog_card_myPostTitle);
            tv_blog_card_myPostDesc = (TextView)itemView.findViewById(R.id.tv_blog_card_myPostDesc);
            mypost_image = (ImageView)itemView.findViewById(R.id.mypost_image);
        }
    }
}
