package variable.com.eclectic.Chats;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import variable.com.eclectic.R;

public class ChatConversationAdapter extends RecyclerView.Adapter<ChatConversationAdapter.MyViewHolder>{

    private String currentRoom;
    Context context;
    ArrayList<ChatMessageModel> conversation_arrayList;
    private View view;
    public ImageView iv_msg_send_status;
    private DatabaseReference rootRef;
    private String receiverUid;
    private String myUid;
    private String myEmail;
    private String receiverEmail;

    public ChatConversationAdapter(ArrayList<ChatMessageModel> conversation_arrayList, String myEmail, String receiverEmail,String myUid,String receiverUid, Context context, String currentRoom) {

        this.context = context;
        this.myEmail = myEmail;
        this.receiverEmail = receiverEmail;
        this.myUid = myUid;
        this.receiverUid = receiverUid;
        this.conversation_arrayList = conversation_arrayList;
        this.currentRoom=currentRoom;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        rootRef = database.getReference(currentRoom).child("Message");

        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        view = layoutInflater.inflate(R.layout.raw_chat_sender_msg_rv,viewGroup,false);

        MyViewHolder myViewHolder = new MyViewHolder(view,context,conversation_arrayList);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int position) {

        final String sender = conversation_arrayList.get(position).getSenderId();
        final String message = conversation_arrayList.get(position).getMessage();
        final String time = conversation_arrayList.get(position).getCurrentTime();
        final String date = conversation_arrayList.get(position).getCurrentDate();
        final String receiver = conversation_arrayList.get(position).getReceiverId();

        if (sender.matches(myEmail) && receiver.matches(receiverEmail))//i am the sender
        {
            myViewHolder.receiver_rl.setVisibility(View.GONE);
            myViewHolder.sender_rl.setVisibility(View.VISIBLE);
            myViewHolder.tv_msg_sender.setText(message);
            myViewHolder.tv_msg_time.setText(time);
        }
        else if(receiver.matches(myEmail) && sender.matches(receiverEmail))//i am the receiver
        {
            myViewHolder.sender_rl.setVisibility(View.GONE);
            myViewHolder.receiver_rl.setVisibility(View.VISIBLE);
            myViewHolder.tv_msg_receiver.setText(message);
            myViewHolder.tv_msg_received_time.setText(time);
        }
        else
        {
            myViewHolder.rl_chat_rv.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return conversation_arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private final RelativeLayout rl_chat_rv;
        private RelativeLayout receiver_rl;
        private RelativeLayout sender_rl;
        private TextView tv_msg_sender,tv_msg_receiver;
        TextView tv_msg_time,tv_msg_received_time;
        ArrayList<ChatMessageModel> conversation_arrayList;

        public MyViewHolder(@NonNull View itemView,Context context,ArrayList<ChatMessageModel> conversation_arrayList) {
            super(itemView);

            this.conversation_arrayList = conversation_arrayList;
            tv_msg_sender = (TextView)itemView.findViewById(R.id.tv_msg_sender);
            tv_msg_time = (TextView)itemView.findViewById(R.id.tv_msg_time);
            sender_rl = (RelativeLayout)itemView.findViewById(R.id.sender_rl);
            receiver_rl = (RelativeLayout)itemView.findViewById(R.id.receiver_rl);
            tv_msg_receiver = (TextView)itemView.findViewById(R.id.tv_msg_receiver);
            tv_msg_received_time = (TextView)itemView.findViewById(R.id.tv_msg_received_time);
            rl_chat_rv = (RelativeLayout)itemView.findViewById(R.id.rl_chat_rv);
        }
    }
}
