package variable.com.eclectic.Chats;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import variable.com.eclectic.R;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Chat_New extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private String myEmail;
    private String userId;
    private String firstName;
    private String lastName;
    private String currentRoom;
    private DatabaseReference rootRef;
    private RecyclerView chat_new_recycler_view;
    private ImageButton imgbtn_new_chat_search;
    ArrayList<Chat_RecyclerView_DataModel> dataModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_new);

        chat_new_recycler_view = (RecyclerView)findViewById(R.id.chat_new_recycler_view);
        imgbtn_new_chat_search = (ImageButton)findViewById(R.id.imgbtn_new_chat_search);

        sharedPreferences = getSharedPreferences("MY_EMAIL",MODE_PRIVATE);
        myEmail = sharedPreferences.getString("EMAIL", "");
        userId = sharedPreferences.getString("UID", "");
        firstName = sharedPreferences.getString("FIRST_NAME", "");
        lastName = sharedPreferences.getString("LAST_NAME", "");
        currentRoom = sharedPreferences.getString("CURRENT_ROOM","");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        rootRef = database.getReference(currentRoom).child("Reg_Users");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        chat_new_recycler_view.setLayoutManager(layoutManager);

        dataModelArrayList = new ArrayList<>();

        readUsers();
    }

    private void readUsers() {

        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot dsp : dataSnapshot.getChildren())
                {
                    Chat_RecyclerView_DataModel chatdataModel = dsp.getValue(Chat_RecyclerView_DataModel.class);

                    String strEmail = chatdataModel.getEmail();

                    if(!strEmail.equals(myEmail))
                        dataModelArrayList.add(chatdataModel);
                }

                setAdapter(dataModelArrayList);

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }

    private void setAdapter(ArrayList<Chat_RecyclerView_DataModel> dataModelArrayList) {

        MyChatAdapter_RecyclerView myAdapter_recyclerView = new MyChatAdapter_RecyclerView(this,dataModelArrayList);
        chat_new_recycler_view.setAdapter(myAdapter_recyclerView);

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
