package variable.com.eclectic.Blogs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;
import variable.com.eclectic.Login;
import variable.com.eclectic.R;

public class Blog_MyProfile extends AppCompatActivity implements View.OnClickListener{

    private StorageReference storage;
    private DatabaseReference databaseRef;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabaseUsers;
    private SharedPreferences sharedPreferences ;
    private String email;
    private String userId;
    private String firstName;
    private String lastName;
    private CircleImageView civ_blog_profile;
    private TextView tv_blog_profile;
    private RecyclerView rv_blog_myProfile_posts;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private FirebaseUser firebaseUser;
    private Blog_ProfileMyPostsAdapter blog_profileMyPostsAdapter;
    ArrayList<BlogPosts_DataModel> dataModelArrayList;
    private ArrayList<String> postIdArrayList;
    private String currentRoom;
    private String hasProfPic;
    private DatabaseReference profPicRef;
    private ProgressBar progressbar_blogProfile;
    private ImageButton ib_blogProfile_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_my_profile);

        sharedPreferences = getSharedPreferences("MY_EMAIL",MODE_PRIVATE);
        email = sharedPreferences.getString("EMAIL","");
        userId = sharedPreferences.getString("UID","");
        firstName = sharedPreferences.getString("FIRST_NAME","");
        lastName = sharedPreferences.getString("LAST_NAME","");
        currentRoom = sharedPreferences.getString("CURRENT_ROOM","");
        hasProfPic = sharedPreferences.getString("HAS_PROF_PIC","");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(currentRoom).child("Blogzone");
        init();
        postIdArrayList = new ArrayList<String>();
        dataModelArrayList = new ArrayList<BlogPosts_DataModel>();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()==null){
                    Intent loginIntent = new Intent(Blog_MyProfile.this, Login.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);startActivity(loginIntent);
                }
            }
        };

        onScreenLoad();
        profPicRef = FirebaseDatabase.getInstance().getInstance().getReference().child("User").child(userId).child("ProfilePic");
        if(sharedPreferences.getString("HAS_PROF_PIC","").matches("yes"))
        {
            loadProfilePic();
        }
        else
        {
            setDp(firstName);
        }

        tv_blog_profile.setText(firstName+" "+lastName);
        rv_blog_myProfile_posts.setLayoutManager(new GridLayoutManager(Blog_MyProfile.this,3));
        rv_blog_myProfile_posts.hasFixedSize();

        blog_profileMyPostsAdapter = new Blog_ProfileMyPostsAdapter(dataModelArrayList,getApplicationContext(),postIdArrayList);
        rv_blog_myProfile_posts.setAdapter(blog_profileMyPostsAdapter);

        ib_blogProfile_back.setOnClickListener(this);
    }

    private void setDp(String usersName) {

        if(usersName.startsWith("a")||usersName.startsWith("A"))
        {
            civ_blog_profile.setImageResource(R.drawable.a);
        }
        if(usersName.startsWith("b")||usersName.startsWith("B"))
        {
            civ_blog_profile.setImageResource(R.drawable.b);
        }
        if(usersName.startsWith("c")||usersName.startsWith("C"))
        {
            civ_blog_profile.setImageResource(R.drawable.c);
        }
        if(usersName.startsWith("d")||usersName.startsWith("D"))
        {
            civ_blog_profile.setImageResource(R.drawable.d);
        }
        if(usersName.startsWith("e")||usersName.startsWith("E"))
        {
            civ_blog_profile.setImageResource(R.drawable.e);
        }
        if(usersName.startsWith("f")||usersName.startsWith("F"))
        {
            civ_blog_profile.setImageResource(R.drawable.f);
        }
        if(usersName.startsWith("g")||usersName.startsWith("G"))
        {
            civ_blog_profile.setImageResource(R.drawable.g);
        }
        if(usersName.startsWith("h")||usersName.startsWith("H"))
        {
            civ_blog_profile.setImageResource(R.drawable.h);
        }
        if(usersName.startsWith("i")||usersName.startsWith("I"))
        {
            civ_blog_profile.setImageResource(R.drawable.i);
        }
        if(usersName.startsWith("j")||usersName.startsWith("J"))
        {
            civ_blog_profile.setImageResource(R.drawable.j);
        }
        if(usersName.startsWith("k")||usersName.startsWith("K"))
        {
            civ_blog_profile.setImageResource(R.drawable.k);
        }
        if(usersName.startsWith("l")||usersName.startsWith("L"))
        {
            civ_blog_profile.setImageResource(R.drawable.l);
        }
        if(usersName.startsWith("m")||usersName.startsWith("M"))
        {
            civ_blog_profile.setImageResource(R.drawable.m);
        }
        if(usersName.startsWith("n")||usersName.startsWith("N"))
        {
            civ_blog_profile.setImageResource(R.drawable.n);
        }
        if(usersName.startsWith("o")||usersName.startsWith("O"))
        {
            civ_blog_profile.setImageResource(R.drawable.o);
        }
        if(usersName.startsWith("p")||usersName.startsWith("P"))
        {
            civ_blog_profile.setImageResource(R.drawable.p);
        }
        if(usersName.startsWith("q")||usersName.startsWith("Q"))
        {
            civ_blog_profile.setImageResource(R.drawable.q);
        }
        if(usersName.startsWith("r")||usersName.startsWith("R"))
        {
            civ_blog_profile.setImageResource(R.drawable.r);
        }
        if(usersName.startsWith("s")||usersName.startsWith("S"))
        {
            civ_blog_profile.setImageResource(R.drawable.s);
        }
        if(usersName.startsWith("t")||usersName.startsWith("T"))
        {
            civ_blog_profile.setImageResource(R.drawable.t);
        }
        if(usersName.startsWith("u")||usersName.startsWith("U"))
        {
            civ_blog_profile.setImageResource(R.drawable.u);
        }
        if(usersName.startsWith("v")||usersName.startsWith("V"))
        {
            civ_blog_profile.setImageResource(R.drawable.v);
        }
        if(usersName.startsWith("w")||usersName.startsWith("W"))
        {
            civ_blog_profile.setImageResource(R.drawable.w);
        }
        if(usersName.startsWith("x")||usersName.startsWith("X"))
        {
            civ_blog_profile.setImageResource(R.drawable.x);
        }
        if(usersName.startsWith("y")||usersName.startsWith("Y"))
        {
            civ_blog_profile.setImageResource(R.drawable.y);
        }
        if(usersName.startsWith("z")||usersName.startsWith("Z"))
        {
            civ_blog_profile.setImageResource(R.drawable.z);
        }
    }

    private void loadProfilePic() {

        profPicRef.child("profile_pic_url").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String url = dataSnapshot.getValue(String.class);
                if(url!="")
                    Picasso.with(Blog_MyProfile.this).load(url).into(civ_blog_profile);
                else
                    civ_blog_profile.setImageResource(R.drawable.mello_head);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void init() {

        civ_blog_profile = (CircleImageView)findViewById(R.id.civ_blog_profile);
        tv_blog_profile = (TextView)findViewById(R.id.tv_blog_profile);
        rv_blog_myProfile_posts = (RecyclerView)findViewById(R.id.rv_blog_myProfile_posts);
        progressbar_blogProfile = (ProgressBar)findViewById(R.id.progressbar_blogProfile);
        ib_blogProfile_back = (ImageButton)findViewById(R.id.ib_blogProfile_back);
    }

    protected void onScreenLoad() {

        progressbar_blogProfile.setVisibility(View.VISIBLE);
        firebaseAuth.addAuthStateListener(mAuthListener);
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if(dataSnapshot.child("email_id").getValue().equals(email))
                {
                    String parentKey = dataSnapshot.getKey();

                    BlogPosts_DataModel blogPosts_dataModel = dataSnapshot.getValue(BlogPosts_DataModel.class);
                    dataModelArrayList.add(blogPosts_dataModel);
                    postIdArrayList.add(parentKey);
                    blog_profileMyPostsAdapter.notifyDataSetChanged();
                }
                progressbar_blogProfile.setVisibility(View.GONE);
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

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.ib_blogProfile_back :
            {
                Intent intent = new Intent(Blog_MyProfile.this,Blog_Home.class);
                startActivity(intent);
                finish();
            }
            break;
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
