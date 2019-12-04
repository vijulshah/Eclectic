package variable.com.eclectic.Chats;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import variable.com.eclectic.R;

public class ChatMessageActivity extends AppCompatActivity {

    private TextView tv_chat_contact_name;
    private EditText edt_type_msg;
    private ImageButton btn_send_msg;
    private String currentDate,currentTime;
    private String receiverEmail;
    private DatabaseReference rootRef;
    private SharedPreferences sharedPreferences;
    private String myEmail;
    private String message;
    private String myUid;
    private String receiverUid;
    private String temp_receiverEmail;
    private String temp_myEmail;
    private RecyclerView rv_messages;
    private String timeToShow;
    private ChatConversationAdapter senderconversationAdapter;
    private ArrayList<ChatMessageModel> sender_msg_arrayList;
    private String firstName;
    private String lastName;
    private String currentRoom;
    private String receiver_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_message);

        init();
        sharedPreferences = getSharedPreferences("MY_EMAIL",MODE_PRIVATE);
        myEmail = sharedPreferences.getString("EMAIL", "");
        myUid = sharedPreferences.getString("UID", "");
        firstName = sharedPreferences.getString("FIRST_NAME", "");
        lastName = sharedPreferences.getString("LAST_NAME", "");
        currentRoom = sharedPreferences.getString("CURRENT_ROOM","");

        rootRef = FirebaseDatabase.getInstance().getReference(currentRoom).child("Message");

        Bundle extras = getIntent().getExtras();
        receiver_name = extras.getString("contact_name");
        receiverEmail = extras.getString("contact_email");
        receiverUid = extras.getString("contact_uid");

        tv_chat_contact_name.setText(receiver_name);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
        layoutManager.setSmoothScrollbarEnabled(true);
        rv_messages.setLayoutManager(layoutManager);

        sender_msg_arrayList = new ArrayList<>();

        displaySenderMessage();

        btn_send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                message = edt_type_msg.getText().toString();
                send_MsgInfo_ToDB();
            }
        });
        senderconversationAdapter = new ChatConversationAdapter(sender_msg_arrayList,myEmail,receiverEmail,myUid,receiverUid,getApplicationContext(),currentRoom);
        rv_messages.setAdapter(senderconversationAdapter);

    }

    private void displaySenderMessage() {

        String myName = firstName+" "+lastName;
        rootRef.child(myName).child(myName+"_"+receiver_name).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (dataSnapshot.exists())
                {
                    ChatMessageModel chatMessageModel = dataSnapshot.getValue(ChatMessageModel.class);
                    sender_msg_arrayList.add(chatMessageModel);
                    senderconversationAdapter.notifyDataSetChanged();
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

    private void send_MsgInfo_ToDB() {

        if(TextUtils.isEmpty(message)||message.trim().equals(""))
        {
            Toast.makeText(this, "Type a message to send", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Calendar calForDate = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd,yyyy");
            currentDate= simpleDateFormat.format(calForDate.getTime());

            Calendar calForTime = Calendar.getInstance();
            SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm:ss a");
            currentTime = simpleTimeFormat.format(calForTime.getTime());

            SimpleDateFormat timeToShow_InMsg = new SimpleDateFormat("hh:mm a");
            timeToShow = timeToShow_InMsg.format(calForTime.getTime());

            message = edt_type_msg.getText().toString();

            ChatMessageModel chatMessageModel = new ChatMessageModel();

            chatMessageModel.setSenderId(myEmail);
            chatMessageModel.setReceiverId(receiverEmail);
            chatMessageModel.setMessage(message);
            chatMessageModel.setCurrentDate(currentDate);
            chatMessageModel.setCurrentTime(timeToShow);

            String strmsgkey =  rootRef.push().getKey();
            String myName = firstName+" "+lastName;
            rootRef.child(myName).child(myName+"_"+receiver_name).child(strmsgkey).setValue(chatMessageModel);

            rootRef.child(receiver_name).child(receiver_name+"_"+myName).child(strmsgkey).setValue(chatMessageModel);

            edt_type_msg.setText("");

        }
    }

    private void init() {

        tv_chat_contact_name = (TextView)findViewById(R.id.tv_chat_contact_name);
        edt_type_msg = (EditText)findViewById(R.id.edt_type_msg);
        btn_send_msg = (ImageButton)findViewById(R.id.btn_send_msg);
        rv_messages = (RecyclerView)findViewById(R.id.rv_messages);

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
