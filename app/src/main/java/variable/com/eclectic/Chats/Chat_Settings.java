package variable.com.eclectic.Chats;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;
import variable.com.eclectic.MainActivity;
import variable.com.eclectic.R;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Chat_Settings extends AppCompatActivity implements View.OnClickListener{


    private static final int GALLERY_REQUEST_CODE = 2;
    private static final int CAMERA_REQUEST_CODE = 3;
    private CircleImageView chat_profile_pic;
    private ImageView iv_back_chat_settings;
    private TextView tv_chat_user_email;
    private SharedPreferences sharedPreferences;
    private String email;
    private String firstName;
    private String lastName;
    private TextView chat_username;
    private BottomSheetDialog bottomSheetDialog;
    private LinearLayout ll_gallery_chat_profile_pic;
    private LinearLayout ll_camera_chat_profile_pic;
    private LinearLayout ll_remove_chat_profile_pic;
    private Uri uri;
    private TextView tv_chat_user_in_room;
    private String userId;
    private String currentRoom;
    private StorageReference storage;
    private DatabaseReference databaseRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabaseUsers;
    private String hasProfPic;
    private Button btn_uploadProfilePic;
    private ProgressBar progressbar_chatSettings;
    private DatabaseReference profPicRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_settings);

        init();
        sharedPreferences = getSharedPreferences("MY_EMAIL",MODE_PRIVATE);
        email = sharedPreferences.getString("EMAIL","");
        userId = sharedPreferences.getString("UID", "");
        firstName = sharedPreferences.getString("FIRST_NAME","");
        lastName = sharedPreferences.getString("LAST_NAME","");
        currentRoom = sharedPreferences.getString("CURRENT_ROOM","");
        hasProfPic = sharedPreferences.getString("HAS_PROF_PIC","");

        chat_username.setText(firstName);
        chat_username.append(" "+lastName);
        tv_chat_user_email.setText(email);
        tv_chat_user_in_room.setText(currentRoom);

        storage = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        profPicRef = FirebaseDatabase.getInstance().getInstance().getReference().child("User").child(userId).child("ProfilePic");
        loadProfilePic();
        btn_uploadProfilePic.setVisibility(View.GONE);
        chat_profile_pic.setOnClickListener(this);
        iv_back_chat_settings.setOnClickListener(this);
        btn_uploadProfilePic.setOnClickListener(this);
    }

    private void loadProfilePic() {

        profPicRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String url = dataSnapshot.child("profile_pic_url").getValue(String.class);
                String hasPic = dataSnapshot.child("HasProfilePic").getValue(String.class);
                if(hasPic.matches("yes"))
                {
                    Picasso.with(Chat_Settings.this).load(url).into(chat_profile_pic);
                }
                else
                {
                    setDp(firstName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setDp(String usersName) {

        if(usersName.startsWith("a")||usersName.startsWith("A"))
        {
            chat_profile_pic.setImageResource(R.drawable.a);
        }
        if(usersName.startsWith("b")||usersName.startsWith("B"))
        {
            chat_profile_pic.setImageResource(R.drawable.b);
        }
        if(usersName.startsWith("c")||usersName.startsWith("C"))
        {
            chat_profile_pic.setImageResource(R.drawable.c);
        }
        if(usersName.startsWith("d")||usersName.startsWith("D"))
        {
            chat_profile_pic.setImageResource(R.drawable.d);
        }
        if(usersName.startsWith("e")||usersName.startsWith("E"))
        {
            chat_profile_pic.setImageResource(R.drawable.e);
        }
        if(usersName.startsWith("f")||usersName.startsWith("F"))
        {
            chat_profile_pic.setImageResource(R.drawable.f);
        }
        if(usersName.startsWith("g")||usersName.startsWith("G"))
        {
            chat_profile_pic.setImageResource(R.drawable.g);
        }
        if(usersName.startsWith("h")||usersName.startsWith("H"))
        {
            chat_profile_pic.setImageResource(R.drawable.h);
        }
        if(usersName.startsWith("i")||usersName.startsWith("I"))
        {
            chat_profile_pic.setImageResource(R.drawable.i);
        }
        if(usersName.startsWith("j")||usersName.startsWith("J"))
        {
            chat_profile_pic.setImageResource(R.drawable.j);
        }
        if(usersName.startsWith("k")||usersName.startsWith("K"))
        {
            chat_profile_pic.setImageResource(R.drawable.k);
        }
        if(usersName.startsWith("l")||usersName.startsWith("L"))
        {
            chat_profile_pic.setImageResource(R.drawable.l);
        }
        if(usersName.startsWith("m")||usersName.startsWith("M"))
        {
            chat_profile_pic.setImageResource(R.drawable.m);
        }
        if(usersName.startsWith("n")||usersName.startsWith("N"))
        {
            chat_profile_pic.setImageResource(R.drawable.n);
        }
        if(usersName.startsWith("o")||usersName.startsWith("O"))
        {
            chat_profile_pic.setImageResource(R.drawable.o);
        }
        if(usersName.startsWith("p")||usersName.startsWith("P"))
        {
            chat_profile_pic.setImageResource(R.drawable.p);
        }
        if(usersName.startsWith("q")||usersName.startsWith("Q"))
        {
            chat_profile_pic.setImageResource(R.drawable.q);
        }
        if(usersName.startsWith("r")||usersName.startsWith("R"))
        {
            chat_profile_pic.setImageResource(R.drawable.r);
        }
        if(usersName.startsWith("s")||usersName.startsWith("S"))
        {
            chat_profile_pic.setImageResource(R.drawable.s);
        }
        if(usersName.startsWith("t")||usersName.startsWith("T"))
        {
            chat_profile_pic.setImageResource(R.drawable.t);
        }
        if(usersName.startsWith("u")||usersName.startsWith("U"))
        {
            chat_profile_pic.setImageResource(R.drawable.u);
        }
        if(usersName.startsWith("v")||usersName.startsWith("V"))
        {
            chat_profile_pic.setImageResource(R.drawable.v);
        }
        if(usersName.startsWith("w")||usersName.startsWith("W"))
        {
            chat_profile_pic.setImageResource(R.drawable.w);
        }
        if(usersName.startsWith("x")||usersName.startsWith("X"))
        {
            chat_profile_pic.setImageResource(R.drawable.x);
        }
        if(usersName.startsWith("y")||usersName.startsWith("Y"))
        {
            chat_profile_pic.setImageResource(R.drawable.y);
        }
        if(usersName.startsWith("z")||usersName.startsWith("Z"))
        {
            chat_profile_pic.setImageResource(R.drawable.z);
        }
    }


    private void init()
    {
        chat_profile_pic = (CircleImageView)findViewById(R.id.chat_profile_pic);
        iv_back_chat_settings = (ImageView)findViewById(R.id.iv_back_chat_settings);
        tv_chat_user_email = (TextView)findViewById(R.id.tv_chat_user_email);
        chat_username = (TextView)findViewById(R.id.chat_username);
        tv_chat_user_in_room = (TextView)findViewById(R.id.tv_chat_user_in_room);
        btn_uploadProfilePic = (Button)findViewById(R.id.btn_uploadProfilePic);
        progressbar_chatSettings = (ProgressBar)findViewById(R.id.progressbar_chatSettings);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.iv_back_chat_settings:
            {
                finish();
            }
            break;

            case R.id.chat_profile_pic :
            {
                openNavigationForDP();
            }
            break;

            case R.id.ll_gallery_chat_profile_pic:
            {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
            }
            break;

            case R.id.ll_camera_chat_profile_pic:
            {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i,CAMERA_REQUEST_CODE);
            }
            break;

            case R.id.ll_remove_chat_profile_pic:
            {
                removeProfilePic();
            }
            break;

            case R.id.btn_uploadProfilePic :
            {
                uploadPhotoToDB();
            }
            break;
        }
    }

    private void removeProfilePic()
    {
        bottomSheetDialog.dismiss();

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd,yyyy");
        String currentDate = simpleDateFormat.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm a");
        String currentTime = simpleTimeFormat.format(calForTime.getTime());

        profPicRef.child("upload_date").setValue(currentDate);
        profPicRef.child("upload_time").setValue(currentTime);
        profPicRef.child("HasProfilePic").setValue("no");
        profPicRef.child("profile_pic_url").setValue("");

        Toast.makeText(this, "Profile photo removed", Toast.LENGTH_SHORT).show();

        SharedPreferences.Editor editor =  sharedPreferences.edit();
        editor.putString("HAS_PROF_PIC","no");
        editor.commit();

        setDp(firstName);
    }

    private void uploadPhotoToDB()
    {
        Toast.makeText(Chat_Settings.this, "SAVING...", Toast.LENGTH_LONG).show();

            final StorageReference filepath = storage.child("Profile_pic_images").child(uri.getLastPathSegment());

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    filepath.child(uri.getLastPathSegment()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {

                            //adding post contents to database reference
                            profPicRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    Calendar calForDate = Calendar.getInstance();
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd,yyyy");
                                    String currentDate = simpleDateFormat.format(calForDate.getTime());

                                    Calendar calForTime = Calendar.getInstance();
                                    SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm a");
                                    String currentTime = simpleTimeFormat.format(calForTime.getTime());

                                    profPicRef.child("profile_pic_url").setValue(uri.toString());
                                    profPicRef.child("upload_date").setValue(currentDate);
                                    profPicRef.child("upload_time").setValue(currentTime)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful())
                                                    {
                                                        SharedPreferences.Editor editor =  sharedPreferences.edit();
                                                        editor.putString("HAS_PROF_PIC","yes");
                                                        editor.commit();
                                                        profPicRef.child("HasProfilePic").setValue("yes");
                                                        Toast.makeText(getApplicationContext(), "Profile photo saved", Toast.LENGTH_SHORT).show();
                                                        btn_uploadProfilePic.setVisibility(View.GONE);
                                                        loadProfilePic();
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

    private void openNavigationForDP() {

        final View bottomNavigation = getLayoutInflater().inflate(R.layout.navigation_chat_set_dp,null);
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(bottomNavigation);
        bottomSheetDialog.show();

        ll_gallery_chat_profile_pic = (LinearLayout)bottomSheetDialog.findViewById(R.id.ll_gallery_chat_profile_pic);
        ll_camera_chat_profile_pic = (LinearLayout)bottomSheetDialog.findViewById(R.id.ll_camera_chat_profile_pic);
        ll_remove_chat_profile_pic = (LinearLayout)bottomSheetDialog.findViewById(R.id.ll_remove_chat_profile_pic);

        ll_gallery_chat_profile_pic.setOnClickListener(this);
        ll_camera_chat_profile_pic.setOnClickListener(this);
        ll_remove_chat_profile_pic.setOnClickListener(this);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK){
            bottomSheetDialog.dismiss();
            uri = data.getData();
            chat_profile_pic.setImageURI(uri);
            btn_uploadProfilePic.setVisibility(View.VISIBLE);
        }
        else
        {
            Toast.makeText(this, "Failed....", Toast.LENGTH_SHORT).show();
        }

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            bottomSheetDialog.dismiss();
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            uri = data.getData();
            chat_profile_pic.setImageBitmap(bitmap);
            btn_uploadProfilePic.setVisibility(View.VISIBLE);
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
