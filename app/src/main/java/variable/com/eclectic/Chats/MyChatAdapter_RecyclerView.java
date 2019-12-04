package variable.com.eclectic.Chats;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import variable.com.eclectic.R;

class MyChatAdapter_RecyclerView extends RecyclerView.Adapter<MyChatAdapter_RecyclerView.MyViewHolder> {
    Context context;
    ArrayList<Chat_RecyclerView_DataModel> dataModelArrayList;
    public MyChatAdapter_RecyclerView(Context context, ArrayList<Chat_RecyclerView_DataModel> dataModelArrayList) {
        this.context = context;
        this.dataModelArrayList = dataModelArrayList;
    }
    @NonNull
    @Override
    public MyChatAdapter_RecyclerView.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.raw_chat_recycler_view,viewGroup,false);
        MyViewHolder myViewHolder = new MyViewHolder(view,context,dataModelArrayList);
        return myViewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull MyChatAdapter_RecyclerView.MyViewHolder myViewHolder, int i) {
        String usersFirstName = dataModelArrayList.get(i).getFirstName();
        String userLastName = dataModelArrayList.get(i).getLastName();
        myViewHolder.rv_chat_name.setText(usersFirstName+" "+userLastName);
        setDp(usersFirstName,myViewHolder);
    }

    private void setDp(String usersName, MyViewHolder myViewHolder)
    {
        if(usersName.startsWith("a")||usersName.startsWith("A"))
        {
            myViewHolder.rv_chat_img.setImageResource(R.drawable.a);
        }
        if(usersName.startsWith("b")||usersName.startsWith("B"))
        {
            myViewHolder.rv_chat_img.setImageResource(R.drawable.b);
        }
        if(usersName.startsWith("c")||usersName.startsWith("C"))
        {
            myViewHolder.rv_chat_img.setImageResource(R.drawable.c);
        }
        if(usersName.startsWith("d")||usersName.startsWith("D"))
        {
            myViewHolder.rv_chat_img.setImageResource(R.drawable.d);
        }
        if(usersName.startsWith("e")||usersName.startsWith("E"))
        {
            myViewHolder.rv_chat_img.setImageResource(R.drawable.e);
        }
        if(usersName.startsWith("f")||usersName.startsWith("F"))
        {
            myViewHolder.rv_chat_img.setImageResource(R.drawable.f);
        }
        if(usersName.startsWith("g")||usersName.startsWith("G"))
        {
            myViewHolder.rv_chat_img.setImageResource(R.drawable.g);
        }
        if(usersName.startsWith("h")||usersName.startsWith("H"))
        {
            myViewHolder.rv_chat_img.setImageResource(R.drawable.h);
        }
        if(usersName.startsWith("i")||usersName.startsWith("I"))
        {
            myViewHolder.rv_chat_img.setImageResource(R.drawable.i);
        }
        if(usersName.startsWith("j")||usersName.startsWith("J"))
        {
            myViewHolder.rv_chat_img.setImageResource(R.drawable.j);
        }
        if(usersName.startsWith("k")||usersName.startsWith("K"))
        {
            myViewHolder.rv_chat_img.setImageResource(R.drawable.k);
        }
        if(usersName.startsWith("l")||usersName.startsWith("L"))
        {
            myViewHolder.rv_chat_img.setImageResource(R.drawable.l);
        }
        if(usersName.startsWith("m")||usersName.startsWith("M"))
        {
            myViewHolder.rv_chat_img.setImageResource(R.drawable.m);
        }
        if(usersName.startsWith("n")||usersName.startsWith("N"))
        {
            myViewHolder.rv_chat_img.setImageResource(R.drawable.n);
        }
        if(usersName.startsWith("o")||usersName.startsWith("O"))
        {
            myViewHolder.rv_chat_img.setImageResource(R.drawable.o);
        }
        if(usersName.startsWith("p")||usersName.startsWith("P"))
        {
            myViewHolder.rv_chat_img.setImageResource(R.drawable.p);
        }
        if(usersName.startsWith("q")||usersName.startsWith("Q"))
        {
            myViewHolder.rv_chat_img.setImageResource(R.drawable.q);
        }
        if(usersName.startsWith("r")||usersName.startsWith("R"))
        {
            myViewHolder.rv_chat_img.setImageResource(R.drawable.r);
        }
        if(usersName.startsWith("s")||usersName.startsWith("S"))
        {
            myViewHolder.rv_chat_img.setImageResource(R.drawable.s);
        }
        if(usersName.startsWith("t")||usersName.startsWith("T"))
        {
            myViewHolder.rv_chat_img.setImageResource(R.drawable.t);
        }
        if(usersName.startsWith("u")||usersName.startsWith("U"))
        {
            myViewHolder.rv_chat_img.setImageResource(R.drawable.u);
        }
        if(usersName.startsWith("v")||usersName.startsWith("V"))
        {
            myViewHolder.rv_chat_img.setImageResource(R.drawable.v);
        }
        if(usersName.startsWith("w")||usersName.startsWith("W"))
        {
            myViewHolder.rv_chat_img.setImageResource(R.drawable.w);
        }
        if(usersName.startsWith("x")||usersName.startsWith("X"))
        {
            myViewHolder.rv_chat_img.setImageResource(R.drawable.x);
        }
        if(usersName.startsWith("y")||usersName.startsWith("Y"))
        {
            myViewHolder.rv_chat_img.setImageResource(R.drawable.y);
        }
        if(usersName.startsWith("z")||usersName.startsWith("Z"))
        {
            myViewHolder.rv_chat_img.setImageResource(R.drawable.z);
        }
    }

    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final CircleImageView rv_chat_img;
        private final TextView rv_chat_name;
        Context context;
        ArrayList<Chat_RecyclerView_DataModel> dataModelArrayList;
        public MyViewHolder(@NonNull View itemView,Context context,ArrayList<Chat_RecyclerView_DataModel> dataModelArrayList) {
            super(itemView);
            this.context = context;
            this.dataModelArrayList = dataModelArrayList;
            rv_chat_name = (TextView)itemView.findViewById(R.id.rv_chat_name);
            rv_chat_img = (CircleImageView)itemView.findViewById(R.id.rv_chat_img);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Chat_RecyclerView_DataModel chat_recyclerView_dataModel = this.dataModelArrayList.get(position);
            Intent intent = new Intent(context,ChatMessageActivity.class);
            intent.putExtra("contact_name",chat_recyclerView_dataModel.getFirstName()+" "+chat_recyclerView_dataModel.getLastName());
            intent.putExtra("contact_email",chat_recyclerView_dataModel.getEmail());
            intent.putExtra("contact_uid",chat_recyclerView_dataModel.getUid());
            this.context.startActivity(intent);
        }
    }
}