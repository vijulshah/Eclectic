package variable.com.eclectic.Blogs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import variable.com.eclectic.R;

public class Blog_NewPost_Image extends AppCompatActivity implements View.OnClickListener {

    private static final int GALLERY_REQUEST_CODE = 2;
    private static final int CAMERA_REQUEST_CODE = 3;
    private Button btn_blog_newPostImg_discard;
    private Button btn_blog_newPostImg_post;
    private EditText edt_blog_newPostImg_desc;
    private EditText edt_blog_newPostImg_title;
    private FloatingActionButton btn_blog_newPost_capture_photo;
    private FloatingActionButton btn_blog_newPost_select_photo;
    private StorageReference storage;
    private DatabaseReference databaseRef;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabaseUsers;
    private Uri uri;
    private SharedPreferences sharedPreferences ;
    private String email;
    private String userId;
    private String firstName;
    private String lastName;
    private CircleImageView img_blog_new_post;
    private String currentRoom;
    private ImageButton ib_newPostImg_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_new_post_image);

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

        btn_blog_newPostImg_discard.setOnClickListener(this);
        btn_blog_newPostImg_post.setOnClickListener(this);
        btn_blog_newPost_capture_photo.setOnClickListener(this);
        btn_blog_newPost_select_photo.setOnClickListener(this);
        ib_newPostImg_back.setOnClickListener(this);
    }

    private void init()
    {
        btn_blog_newPostImg_discard = (Button)findViewById(R.id.btn_blog_newPostImg_discard);
        btn_blog_newPostImg_post = (Button)findViewById(R.id.btn_blog_newPostImg_post);
        edt_blog_newPostImg_desc = (EditText)findViewById(R.id.edt_blog_newPostImg_desc);
        edt_blog_newPostImg_title = (EditText)findViewById(R.id.edt_blog_newPostImg_title);
        btn_blog_newPost_capture_photo = (FloatingActionButton)findViewById(R.id.btn_blog_newPost_capture_photo);
        btn_blog_newPost_select_photo = (FloatingActionButton)findViewById(R.id.btn_blog_newPost_select_photo);
        img_blog_new_post = (CircleImageView)findViewById(R.id.img_blog_new_post);
        ib_newPostImg_back = (ImageButton)findViewById(R.id.ib_newPostImg_back);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.ib_newPostImg_back :
            {
                Toast.makeText(Blog_NewPost_Image.this, "Post discarded", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Blog_NewPost_Image.this, Blog_Home.class);
                startActivity(intent);
                finish();
            }
            break;

            case R.id.btn_blog_newPostImg_discard :
            {
                Toast.makeText(Blog_NewPost_Image.this, "Post discarded", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Blog_NewPost_Image.this, Blog_Home.class);
                startActivity(intent);
                finish();
            }
            break;

            case R.id.btn_blog_newPostImg_post :
            {
                saveToDb_and_post();
            }
            break;

            case R.id.btn_blog_newPost_capture_photo :
            {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i,CAMERA_REQUEST_CODE);
            }
            break;

            case R.id.btn_blog_newPost_select_photo :
            {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
            }
            break;
        }
    }

    private void saveToDb_and_post() {

        Toast.makeText(Blog_NewPost_Image.this, "POSTING...", Toast.LENGTH_LONG).show();
        final String PostTitle = edt_blog_newPostImg_title.getText().toString().trim();
        final String PostDesc = edt_blog_newPostImg_desc.getText().toString().trim();
        // do a check for empty fields
        if (!TextUtils.isEmpty(PostDesc) && !TextUtils.isEmpty(PostTitle)){
            final StorageReference filepath = storage.child("blog_post_images").child(uri.getLastPathSegment());

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    filepath.child(uri.getLastPathSegment()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {

                            final DatabaseReference newPost = databaseRef.push();
                            //adding post contents to database reference
                            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    Calendar calForDate = Calendar.getInstance();
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd,yyyy");
                                    String currentDate = simpleDateFormat.format(calForDate.getTime());

                                    Calendar calForTime = Calendar.getInstance();
                                    SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm a");
                                    String currentTime = simpleTimeFormat.format(calForTime.getTime());

                                    newPost.child("title").setValue(PostTitle);
                                    newPost.child("desc").setValue(PostDesc);
                                    newPost.child("imageUrl").setValue(uri.toString());
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
                                                        Intent intent = new Intent(Blog_NewPost_Image.this, Blog_Home.class);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                    });
                }
            });
        }
    }

    @Override
    // image from gallery result
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK){
            uri = data.getData();
            img_blog_new_post.setImageURI(uri);
        }

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            uri = data.getData();
            img_blog_new_post.setImageBitmap(bitmap);
        }

    }
}
