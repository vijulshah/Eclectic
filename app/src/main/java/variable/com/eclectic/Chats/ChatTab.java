package variable.com.eclectic.Chats;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import variable.com.eclectic.R;

import static android.content.Context.MODE_PRIVATE;

public class ChatTab extends Fragment {

    private FloatingActionButton fab_new_chat;
    private FrameLayout frame_tab_chat;
    private RecyclerView chat_recycler_view;
    private DatabaseReference rootRef;
    ArrayList<Chat_RecyclerView_DataModel> dataModelArrayList;
    private SharedPreferences sharedPreferences;
    private String myEmail;
    private String userId;
    private String firstName;
    private String lastName;
    private String currentRoom;
    private DatabaseReference databaseRef;
    private ProgressBar progressbar_chatTab;
    private MyChatAdapter_RecyclerView myAdapter_recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.tab_chat,container,false);
        setHasOptionsMenu(true);

        sharedPreferences = getActivity().getSharedPreferences("MY_EMAIL",MODE_PRIVATE);
        myEmail = sharedPreferences.getString("EMAIL", "");
        userId = sharedPreferences.getString("UID", "");
        firstName = sharedPreferences.getString("FIRST_NAME", "");
        lastName = sharedPreferences.getString("LAST_NAME", "");
        currentRoom = sharedPreferences.getString("CURRENT_ROOM","");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        rootRef = database.getReference(currentRoom).child("Reg_Users");
        databaseRef = database.getReference(currentRoom).child("Message");

        fab_new_chat = rootView.findViewById(R.id.fab_new_chat);
        frame_tab_chat= rootView.findViewById(R.id.frame_tab_chat);
        chat_recycler_view = (RecyclerView)rootView.findViewById(R.id.chat_recycler_view);
        progressbar_chatTab = (ProgressBar)rootView.findViewById(R.id.progressbar_chatTab);

        fab_new_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(),Chat_New.class);
                startActivity(intent);
            }
        });

        dataModelArrayList = new ArrayList<>();

        return rootView;
    }

    private void readUsers() {

        progressbar_chatTab.setVisibility(View.VISIBLE);

        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                for(final DataSnapshot dsp : dataSnapshot.getChildren())
                {
                    final Chat_RecyclerView_DataModel chatdataModel = dsp.getValue(Chat_RecyclerView_DataModel.class);
                    final String matchName = chatdataModel.getFirstName()+" "+chatdataModel.getLastName();
                    final String myName = firstName+" "+lastName;
                    databaseRef.child(myName).child(myName+"_"+matchName).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snap)
                        {
                            if(snap.exists())
                            {
                                if(!dataModelArrayList.contains(chatdataModel))
                                {
                                    dataModelArrayList.add(chatdataModel);
                                    myAdapter_recyclerView.notifyDataSetChanged();
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                progressbar_chatTab.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        readUsers();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        chat_recycler_view.setLayoutManager(layoutManager);
        myAdapter_recyclerView = new MyChatAdapter_RecyclerView(getContext(),dataModelArrayList);
        chat_recycler_view.setAdapter(myAdapter_recyclerView);
    }

}