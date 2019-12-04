package variable.com.eclectic.Blogs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

import variable.com.eclectic.R;

import static android.content.Context.MODE_PRIVATE;

public class Blog_ProfileAllPostsAdapter extends RecyclerView.Adapter<Blog_ProfileAllPostsAdapter.MyViewHolder>{

    Context context;
    private ArrayList<BlogPosts_DataModel> dataModelArrayList;
    private ArrayList<String> postIdArrayList;
    private DatabaseReference rootRef;
    private View view;
    private SharedPreferences sharedPreferences;
    private String firstName;
    private String lastName;
    private String email;
    private String userId;
    private String currentRoom;

    public Blog_ProfileAllPostsAdapter(ArrayList<BlogPosts_DataModel> dataModelArrayList, Context context, ArrayList<String> postIdArrayList)
    {
        this.dataModelArrayList = dataModelArrayList;
        this.context = context;
        this.postIdArrayList = postIdArrayList;
    }

    @NonNull
    @Override
    public Blog_ProfileAllPostsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        sharedPreferences = context.getSharedPreferences("MY_EMAIL",MODE_PRIVATE);
        email = sharedPreferences.getString("EMAIL","");
        userId = sharedPreferences.getString("UID","");
        firstName = sharedPreferences.getString("FIRST_NAME","");
        lastName = sharedPreferences.getString("LAST_NAME","");
        currentRoom = sharedPreferences.getString("CURRENT_ROOM","");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        rootRef = database.getReference(currentRoom).child("Blogzone");

        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        view = layoutInflater.inflate(R.layout.card_items,viewGroup,false);

        Blog_ProfileAllPostsAdapter.MyViewHolder myViewHolder = new Blog_ProfileAllPostsAdapter.MyViewHolder(view,context,dataModelArrayList);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Blog_ProfileAllPostsAdapter.MyViewHolder viewHolder, final int position) {

        final String title = dataModelArrayList.get(position).getTitle();
        final String desc = dataModelArrayList.get(position).getDesc();
        final String url = dataModelArrayList.get(position).getImageUrl();
        final String date = dataModelArrayList.get(position).getDate();
        final String time = dataModelArrayList.get(position).getTime();
        final String username = dataModelArrayList.get(position).getUsername();

        viewHolder.tv_blog_card_userPostTitle.setText(title);
        viewHolder.tv_blog_card_Post_date.setText(date);
        viewHolder.tv_blog_card_Post_time.setText(time);
        viewHolder.tv_blog_card_Post_user.setText(username);
        if(url!=null) {
            viewHolder.tv_blog_card_userPostDesc.setVisibility(View.GONE);
            viewHolder.post_image.setVisibility(View.VISIBLE);
            Picasso.with(context).load(url).into(viewHolder.post_image);
        }
        else {
            viewHolder.post_image.setVisibility(View.GONE);
            viewHolder.tv_blog_card_userPostDesc.setVisibility(View.VISIBLE);
            viewHolder.tv_blog_card_userPostDesc.setText(desc);
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String uname = firstName+" "+lastName;
                if(username.toLowerCase().matches(uname.toLowerCase())) {

                    String post_key = postIdArrayList.get(position);
                    Intent singleActivity = new Intent(context.getApplicationContext(), Bolg_MyPost_Page.class);
                    singleActivity.putExtra("PostID", post_key);
                    singleActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(singleActivity);
                }
                else
                {
                    String post_key = postIdArrayList.get(position);
                    Intent singleActivity = new Intent(context.getApplicationContext(), Blog_UserPost_Page.class);
                    singleActivity.putExtra("PostID", post_key);
                    singleActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(singleActivity);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
    }

    public void filterList(ArrayList<BlogPosts_DataModel> filteredArrayList) {
        dataModelArrayList = filteredArrayList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_blog_card_userPostTitle;
        private TextView tv_blog_card_userPostDesc;
        private TextView tv_blog_card_Post_date;
        private TextView tv_blog_card_Post_time;
        private TextView tv_blog_card_Post_user;
        private ImageView post_image;
        ArrayList<BlogPosts_DataModel> dataModelArrayList;

        public MyViewHolder(@NonNull View itemView, Context context, ArrayList<BlogPosts_DataModel> dataModelArrayList) {
            super(itemView);

            this.dataModelArrayList = dataModelArrayList;
            tv_blog_card_userPostTitle = (TextView)itemView.findViewById(R.id.tv_blog_card_userPostTitle);
            tv_blog_card_userPostDesc = (TextView)itemView.findViewById(R.id.tv_blog_card_userPostDesc);
            tv_blog_card_Post_date = (TextView)itemView.findViewById(R.id.tv_blog_card_Post_date);
            tv_blog_card_Post_time = (TextView)itemView.findViewById(R.id.tv_blog_card_Post_time);
            tv_blog_card_Post_user = (TextView)itemView.findViewById(R.id.tv_blog_card_Post_user);
            post_image = (ImageView)itemView.findViewById(R.id.post_image);
        }
    }
}
