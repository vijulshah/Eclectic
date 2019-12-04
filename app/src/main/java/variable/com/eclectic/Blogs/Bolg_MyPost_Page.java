package variable.com.eclectic.Blogs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import variable.com.eclectic.R;

public class Bolg_MyPost_Page extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_blog_myPost;
    private TextView tv_blog_myPostTitle;
    private TextView tv_blog_myPostDesc;
    private Button btn_blog_deleteMyPost;
    private DatabaseReference mDatabase;
    private String post_key;
    private FirebaseAuth mAuth;
    private TextView tv_blog_my_Post_date;
    private TextView tv_blog_my_Post_time;
    private SharedPreferences sharedPreferences;
    private String email;
    private String userId;
    private String firstName;
    private String lastName;
    private String currentRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bolg_mypost_page);

        init();
        sharedPreferences = getSharedPreferences("MY_EMAIL",MODE_PRIVATE);
        email = sharedPreferences.getString("EMAIL","");
        userId = sharedPreferences.getString("UID","");
        firstName = sharedPreferences.getString("FIRST_NAME","");
        lastName = sharedPreferences.getString("LAST_NAME","");
        currentRoom = sharedPreferences.getString("CURRENT_ROOM","");

        mDatabase = FirebaseDatabase.getInstance().getReference().child(currentRoom).child("Blogzone");
        post_key = getIntent().getExtras().getString("PostID");
        Log.e("MYPID",post_key);
        mAuth = FirebaseAuth.getInstance();
        btn_blog_deleteMyPost.setVisibility(View.INVISIBLE);

        mDatabase.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_title = (String) dataSnapshot.child("title").getValue();
                String post_desc = (String) dataSnapshot.child("desc").getValue();
                String post_image = (String) dataSnapshot.child("imageUrl").getValue();
                String post_uid = (String) dataSnapshot.child("uid").getValue();
                String email_id = (String) dataSnapshot.child("email_id").getValue();
                String post_date = (String) dataSnapshot.child("post_date").getValue();
                String post_time = (String) dataSnapshot.child("post_time").getValue();

                tv_blog_myPostTitle.setText(post_title);
                tv_blog_myPostDesc.setText(post_desc);
                tv_blog_my_Post_date.setText(post_date);
                tv_blog_my_Post_time.setText(post_time);
                Picasso.with(Bolg_MyPost_Page.this).load(post_image).into(iv_blog_myPost);
                if (mAuth.getCurrentUser().getUid().equals(post_uid)){

                    btn_blog_deleteMyPost.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        btn_blog_deleteMyPost.setOnClickListener(this);
    }

    private void init()
    {
        iv_blog_myPost = (ImageView)findViewById(R.id.iv_blog_myPost);
        tv_blog_myPostTitle = (TextView)findViewById(R.id.tv_blog_myPostTitle);
        tv_blog_myPostDesc = (TextView)findViewById(R.id.tv_blog_myPostDesc);
        btn_blog_deleteMyPost = (Button)findViewById(R.id.btn_blog_deleteMyPost);
        tv_blog_my_Post_date = (TextView)findViewById(R.id.tv_blog_my_Post_date);
        tv_blog_my_Post_time = (TextView)findViewById(R.id.tv_blog_my_Post_time);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btn_blog_deleteMyPost :
            {
                mDatabase.child(post_key).removeValue();

                Intent mainintent = new Intent(Bolg_MyPost_Page.this, Blog_Home.class);
                startActivity(mainintent);
            }
            break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
