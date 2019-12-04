package variable.com.eclectic;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
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

import variable.com.eclectic.Chats.Chat_RecyclerView_DataModel;

public class EnterRoom extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference databaseRef;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabaseUsers;
    private SharedPreferences sharedPreferences;
    private EditText edt_room_password;
    private Button btn_enter_room;
    private String lastName;
    private String firstName;
    private String email;
    private ImageButton ib_enterRoom_exit;
    private TextView tv_room_uname;
    private FirebaseUser firebaseUser;
    private LinearLayout ll_enterRoom;
    boolean doubleback=false;
    private String uid;
    private String profilePic;
    private Button btn_create_room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_room);

        loadSharedPrefs();
        init();
        loadFirebaseMethods();
        loadOnClickListners();

    }

    private void loadOnClickListners() {

        btn_enter_room.setOnClickListener(this);
        ib_enterRoom_exit.setOnClickListener(this);
        btn_create_room.setOnClickListener(this);
    }

    private void loadFirebaseMethods() {

        databaseRef = database.getInstance().getReference().child("Room_keys");
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
    }

    private void loadSharedPrefs() {

        sharedPreferences = getSharedPreferences("MY_EMAIL",MODE_PRIVATE);
        uid = sharedPreferences.getString("UID","");
        email = sharedPreferences.getString("EMAIL","");
        firstName = sharedPreferences.getString("FIRST_NAME","");
        lastName = sharedPreferences.getString("LAST_NAME","");
        profilePic = sharedPreferences.getString("HAS_PROF_PIC","");
    }


    private void init() {
        edt_room_password = (EditText)findViewById(R.id.edt_room_password);
        btn_enter_room = (Button)findViewById(R.id.btn_enter_room);
        ib_enterRoom_exit = (ImageButton)findViewById(R.id.ib_enterRoom_exit);
        tv_room_uname = (TextView)findViewById(R.id.tv_room_uname);
        ll_enterRoom = (LinearLayout)findViewById(R.id.ll_enterRoom);
        btn_create_room = (Button)findViewById(R.id.btn_create_room);
        tv_room_uname.setText("Hello, "+firstName);
    }

    @Override
    public void onBackPressed() {

        if(doubleback)
        {
            finish();
        }

        this.doubleback = true;

        @SuppressLint("WrongConstant") Snackbar snackbar = Snackbar
                .make(ll_enterRoom, "Press \"BACK\" again to exit", Snackbar.LENGTH_LONG)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
        snackbar.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleback = false;
            }
        }, 2000);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.ib_enterRoom_exit :
            {
                mAuth.signOut();
                sharedPreferences = getSharedPreferences("MYAPP",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("UID");
                editor.remove("EMAIL");
                editor.remove("FIRST_NAME");
                editor.remove("LAST_NAME");
                editor.remove("HAS_PROF_PIC");
                editor.commit();
                Toast.makeText(EnterRoom.this, "Logged out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EnterRoom.this, Login.class);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
            }
            break;

            case R.id.btn_enter_room:
            {
                mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("User").child(mCurrentUser.getUid());
                databaseRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        if(dataSnapshot.exists())
                        {
                            if(((String)dataSnapshot.getValue()).matches(edt_room_password.getText().toString()))
                            {
                                SharedPreferences.Editor editor =  sharedPreferences.edit();

                                Chat_RecyclerView_DataModel chat_recyclerView_dataModel = new Chat_RecyclerView_DataModel();

                                chat_recyclerView_dataModel.setUid(uid);
                                chat_recyclerView_dataModel.setEmail(email);
                                chat_recyclerView_dataModel.setFirstName(firstName);
                                chat_recyclerView_dataModel.setLastName(lastName);

                                String parentKey = (String)dataSnapshot.getKey();
                                FirebaseDatabase.getInstance().getReference().child(parentKey+"_Room").child("Reg_Users").child(firstName+"_"+lastName).setValue(chat_recyclerView_dataModel);
                                mDatabaseUsers.child("Reg_Room").setValue(parentKey+"_Room");

                                editor.putString("CURRENT_ROOM",parentKey+"_Room");
                                editor.commit();

                                Intent intent = new Intent(EnterRoom.this,MainActivity.class);
                                startActivity(intent);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                finish();
                            }
                            else
                            {
                                    @SuppressLint("WrongConstant") Snackbar snackbar = Snackbar
                                            .make(ll_enterRoom, "Please enter a valid password", Snackbar.LENGTH_LONG)
                                            .setAction("OK", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {

                                                }
                                            });
                                    snackbar.show();
                            }
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            break;

            case R.id.btn_create_room:
            {
                Intent intent = new Intent(EnterRoom.this,CreateRoomPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
            break;
        }
    }
}
