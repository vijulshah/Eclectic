package variable.com.eclectic.Blogs;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import variable.com.eclectic.R;

public class Blog_New_Post extends AppCompatActivity implements View.OnClickListener {

    private Button btn_blog_newPost_post;
    private Button btn_blog_newPost_discard;
    private EditText edt_blog_newPost_desc;
    private EditText edt_blog_newPost_title;
    private StorageReference storage;
    private DatabaseReference databaseRef;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabaseUsers;
    private SharedPreferences sharedPreferences;
    private String email;
    private String userId;
    private String firstName;
    private String lastName;
    private String currentRoom;
    private ImageButton ib_newPost_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_new_post);

        init();
        sharedPreferences = getSharedPreferences("MY_EMAIL",MODE_PRIVATE);
        email = sharedPreferences.getString("EMAIL","");
        userId = sharedPreferences.getString("UID","");
        firstName = sharedPreferences.getString("FIRST_NAME","");
        lastName = sharedPreferences.getString("LAST_NAME","");
        currentRoom = sharedPreferences.getString("CURRENT_ROOM","");

        storage = FirebaseStorage.getInstance().getReference();
        databaseRef = database.getInstance().getReference().child(currentRoom).child("Blogzone");
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("User").child(mCurrentUser.getUid());

        btn_blog_newPost_post.setOnClickListener(this);
        btn_blog_newPost_discard.setOnClickListener(this);
        ib_newPost_back.setOnClickListener(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void init()
    {
        btn_blog_newPost_post = (Button)findViewById(R.id.btn_blog_newPost_post);
        btn_blog_newPost_discard = (Button)findViewById(R.id.btn_blog_newPost_discard);
        edt_blog_newPost_desc= (EditText)findViewById(R.id.edt_blog_newPost_desc);
        edt_blog_newPost_title = (EditText)findViewById(R.id.edt_blog_newPost_title);
        ib_newPost_back = (ImageButton)findViewById(R.id.ib_newPost_back);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.ib_newPost_back :
            {
                Toast.makeText(Blog_New_Post.this, "Post discarded", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Blog_New_Post.this, Blog_Home.class);
                startActivity(intent);
                finish();
            }
            break;

            case R.id.btn_blog_newPost_post :
            {
                saveToDb_and_post();
            }
            break;

            case R.id.btn_blog_newPost_discard :
            {
                Toast.makeText(Blog_New_Post.this, "Post discarded", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Blog_New_Post.this, Blog_Home.class);
                startActivity(intent);
                finish();
            }
            break;
        }
    }

    private void saveToDb_and_post()
    {

        Toast.makeText(Blog_New_Post.this, "POSTING...", Toast.LENGTH_LONG).show();
        final String PostTitle = edt_blog_newPost_title.getText().toString().trim();
        final String PostDesc = edt_blog_newPost_desc.getText().toString().trim();
        // do a check for empty fields
        if (!TextUtils.isEmpty(PostDesc) && !TextUtils.isEmpty(PostTitle)){

            Calendar calForDate = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd,yyyy");
            String currentDate = simpleDateFormat.format(calForDate.getTime());

            Calendar calForTime = Calendar.getInstance();
            SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm a");
            String currentTime = simpleTimeFormat.format(calForTime.getTime());

            final DatabaseReference newPost = databaseRef.push();
            newPost.child("title").setValue(PostTitle);
            newPost.child("desc").setValue(PostDesc);
            newPost.child("uid").setValue(userId);
            newPost.child("email_id").setValue(email);
            newPost.child("post_date").setValue(currentDate);
            newPost.child("post_time").setValue(currentTime);
            newPost.child("username").setValue(firstName+" "+lastName)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()){

                        Toast.makeText(getApplicationContext(), "Succesfully Uploaded", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Blog_New_Post.this, Blog_Home.class);
                        startActivity(intent);
                    }
                }
            });
        }
        else
        {
            Toast.makeText(Blog_New_Post.this, "Please Enter the details", Toast.LENGTH_SHORT).show();
        }
    }
}
