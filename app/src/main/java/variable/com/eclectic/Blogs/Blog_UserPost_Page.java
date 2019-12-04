package variable.com.eclectic.Blogs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import java.util.ResourceBundle;

import variable.com.eclectic.Chats.ChatMessageActivity;
import variable.com.eclectic.Chats.Chat_RecyclerView_DataModel;
import variable.com.eclectic.R;

public class Blog_UserPost_Page extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_blog_userPost;
    private TextView tv_blog_userTitle;
    private TextView tv_blog_userDesc;
    private Button btn_blog_sendDmMsg;
    private DatabaseReference mDatabase;
    private String post_key;
    private FirebaseAuth mAuth;
    private TextView tv_blog_postedBy_Uname;
    private String blog_PostBy_email_id;
    private DatabaseReference rootRef;
    private TextView tv_blog_user_Post_date;
    private TextView tv_blog_user_Post_time;
    private SharedPreferences sharedPreferences;
    private String email;
    private String userId;
    private String firstName;
    private String lastName;
    private String currentRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_userpost_page);

        init();
        sharedPreferences = getSharedPreferences("MY_EMAIL",MODE_PRIVATE);
        email = sharedPreferences.getString("EMAIL","");
        userId = sharedPreferences.getString("UID","");
        firstName = sharedPreferences.getString("FIRST_NAME","");
        lastName = sharedPreferences.getString("LAST_NAME","");
        currentRoom = sharedPreferences.getString("CURRENT_ROOM","");

        mDatabase = FirebaseDatabase.getInstance().getReference().child(currentRoom).child("Blogzone");
        post_key = getIntent().getExtras().getString("PostID");
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        rootRef = database.getReference("User");

        mDatabase.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_title = (String) dataSnapshot.child("title").getValue();
                String post_desc = (String) dataSnapshot.child("desc").getValue();
                String post_image = (String) dataSnapshot.child("imageUrl").getValue();
                String post_uid = (String) dataSnapshot.child("uid").getValue();
                blog_PostBy_email_id = (String) dataSnapshot.child("email_id").getValue();
                String post_username = (String) dataSnapshot.child("username").getValue();
                String post_date = (String) dataSnapshot.child("post_date").getValue();
                String post_time = (String) dataSnapshot.child("post_time").getValue();

                tv_blog_userTitle.setText(post_title);
                tv_blog_userDesc.setText(post_desc);
                tv_blog_postedBy_Uname.setText(post_username);
                tv_blog_user_Post_date.setText(post_date);
                tv_blog_user_Post_time.setText(post_time);
                Picasso.with(Blog_UserPost_Page.this).load(post_image).into(iv_blog_userPost);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btn_blog_sendDmMsg.setOnClickListener(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void sendMessage(final String blog_PostBy_email_id) {

        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {

                    Chat_RecyclerView_DataModel chatdataModel = dsp.getValue(Chat_RecyclerView_DataModel.class);

                    String strEmail = chatdataModel.getEmail();
                    String strUid = chatdataModel.getUid();
                    String strName = chatdataModel.getFirstName()+" "+chatdataModel.getLastName();

                    if (strEmail.equals(blog_PostBy_email_id))
                    {
                        Intent intent = new Intent(Blog_UserPost_Page.this, ChatMessageActivity.class);
                        intent.putExtra("contact_name",strName);
                        intent.putExtra("contact_email",strEmail);
                        intent.putExtra("contact_uid",strUid);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void init()
    {
        iv_blog_userPost = (ImageView)findViewById(R.id.iv_blog_userPost);
        tv_blog_userTitle = (TextView)findViewById(R.id.tv_blog_userTitle);
        tv_blog_userDesc = (TextView)findViewById(R.id.tv_blog_userDesc);
        btn_blog_sendDmMsg = (Button)findViewById(R.id.btn_blog_sendDmMsg);
        tv_blog_postedBy_Uname = (TextView)findViewById(R.id.tv_blog_postedBy_Uname);
        tv_blog_user_Post_date = (TextView)findViewById(R.id.tv_blog_user_Post_date);
        tv_blog_user_Post_time = (TextView)findViewById(R.id.tv_blog_user_Post_time);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btn_blog_sendDmMsg :
            {
               sendMessage(blog_PostBy_email_id);
            }
            break;
        }
    }
}
