package variable.com.eclectic.Blogs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;
import variable.com.eclectic.Chats.Chat_Home;
import variable.com.eclectic.Chats.Chat_Settings;
import variable.com.eclectic.Login;
import variable.com.eclectic.R;
import variable.com.eclectic.Tasks.Tasks_Home;

public class Blog_Home extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth.AuthStateListener mAuthListener;
    private BottomSheetDialog bottomSheetDialog;
    private DatabaseReference profPicRef;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference mDatabase;
    private SharedPreferences sharedPreferences;
    private TextView user_name;
    private CircleImageView fab_profile;
    private ImageButton bottom_nav_logout;
    private FloatingActionButton fab_blog;
    private FloatingActionButton fab_blog_new_post;
    private RecyclerView rv_blog_container;
    private ImageButton imgbtn_blog_search;
    private String email;
    private String firstName;
    private String lastName;
    private String userId;
    private FloatingActionButton fab_blog_newPostImg;
    private FloatingActionButton fab_blog_newPost_simple;
    static int flag = 1;
    private CircleImageView iv_blog_myProfile;
    private ArrayList<BlogPosts_DataModel> dataModelArrayList;
    private Blog_ProfileAllPostsAdapter blog_profileAllPostsAdapter;
    private RelativeLayout rl_blog_search_bar;
    private ImageButton ib_blog_close_search;
    private EditText edt_blog_search;
    private ArrayList<String> postIdArrayList;
    private String currentRoom;
    private String hasProfPic;
    private ProgressBar progressbar_blogHome;
    private View bottomNavigation;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_home);

        loadSharedPrefs();
        init();
        loadFirebaseMethods();
        loadProfilePic();
        loadScreen();
        loadLayout();
        loadSearchActive();
        loadOnClickListners();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void loadSharedPrefs() {
        sharedPreferences = getSharedPreferences("MY_EMAIL", MODE_PRIVATE);
        email = sharedPreferences.getString("EMAIL", "");
        userId = sharedPreferences.getString("UID", "");
        firstName = sharedPreferences.getString("FIRST_NAME", "");
        lastName = sharedPreferences.getString("LAST_NAME", "");
        currentRoom = sharedPreferences.getString("CURRENT_ROOM","");
        hasProfPic = sharedPreferences.getString("HAS_PROF_PIC","");
    }

    private void init() {

        bottomNavigation = getLayoutInflater().inflate(R.layout.navigation_menu, null);
        bottomSheetDialog = new BottomSheetDialog(Blog_Home.this);
        bottomSheetDialog.setContentView(bottomNavigation);

        fab_blog = (FloatingActionButton) findViewById(R.id.fab_blog);
        fab_blog_new_post = (FloatingActionButton) findViewById(R.id.fab_blog_new_post);
        rv_blog_container = (RecyclerView) findViewById(R.id.rv_blog_container);
        imgbtn_blog_search = (ImageButton) findViewById(R.id.imgbtn_blog_search);
        fab_blog_newPostImg = (FloatingActionButton) findViewById(R.id.fab_blog_newPostImg);
        fab_blog_newPost_simple = (FloatingActionButton) findViewById(R.id.fab_blog_newPost_simple);
        iv_blog_myProfile = (CircleImageView) findViewById(R.id.iv_blog_myProfile);
        rl_blog_search_bar = (RelativeLayout)findViewById(R.id.rl_blog_search_bar);
        ib_blog_close_search = (ImageButton)findViewById(R.id.ib_blog_close_search);
        edt_blog_search = (EditText)findViewById(R.id.edt_blog_search);
        progressbar_blogHome = (ProgressBar)findViewById(R.id.progressbar_blogHome);

        user_name = (TextView) bottomNavigation.findViewById(R.id.user_name);
        bottom_nav_logout = (ImageButton) bottomNavigation.findViewById(R.id.bottom_nav_logout);
        fab_profile = (CircleImageView) bottomNavigation.findViewById(R.id.fab_profile);
        navigationView = (NavigationView) bottomNavigation.findViewById(R.id.navigation_menu);
        user_name.setText(email);
    }

    private void loadFirebaseMethods() {

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent loginIntent = new Intent(Blog_Home.this, Login.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                }
            }
        };

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(currentRoom).child("Blogzone");
        profPicRef = FirebaseDatabase.getInstance().getInstance().getReference().child("User").child(userId).child("ProfilePic");

    }

    private void loadProfilePic() {

        if(sharedPreferences.getString("HAS_PROF_PIC","").matches("yes"))
        {
            setProfilePic();
        }
        else
        {
            setDp(firstName);
        }

    }

    protected void loadScreen() {

        postIdArrayList = new ArrayList<String>();
        dataModelArrayList = new ArrayList<BlogPosts_DataModel>();

        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (dataSnapshot.exists()) {

                    String parentKey = dataSnapshot.getKey();

                    BlogPosts_DataModel blogPosts_dataModel = dataSnapshot.getValue(BlogPosts_DataModel.class);
                    dataModelArrayList.add(blogPosts_dataModel);
                    postIdArrayList.add(parentKey);
                    blog_profileAllPostsAdapter.notifyDataSetChanged();
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

    private void loadLayout() {
        rv_blog_container.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rv_blog_container.setHasFixedSize(true);
        blog_profileAllPostsAdapter = new Blog_ProfileAllPostsAdapter(dataModelArrayList, getApplicationContext(),postIdArrayList);
        rv_blog_container.setAdapter(blog_profileAllPostsAdapter);
    }

    private void loadSearchActive() {

        edt_blog_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
    }

    private void loadOnClickListners() {

        fab_blog.setOnClickListener(this);
        fab_blog_new_post.setOnClickListener(this);
        fab_blog_newPostImg.setOnClickListener(this);
        fab_blog_newPost_simple.setOnClickListener(this);
        iv_blog_myProfile.setOnClickListener(this);
        imgbtn_blog_search.setOnClickListener(this);
        ib_blog_close_search.setOnClickListener(this);
    }

    private void setDp(String usersName) {

        if(usersName.startsWith("a")||usersName.startsWith("A"))
        {
            iv_blog_myProfile.setImageResource(R.drawable.a);
            fab_profile.setImageResource(R.drawable.a);
        }
        if(usersName.startsWith("b")||usersName.startsWith("B"))
        {
            iv_blog_myProfile.setImageResource(R.drawable.b);
            fab_profile.setImageResource(R.drawable.b);
        }
        if(usersName.startsWith("c")||usersName.startsWith("C"))
        {
            iv_blog_myProfile.setImageResource(R.drawable.c);
            fab_profile.setImageResource(R.drawable.c);
        }
        if(usersName.startsWith("d")||usersName.startsWith("D"))
        {
            iv_blog_myProfile.setImageResource(R.drawable.d);
            fab_profile.setImageResource(R.drawable.d);
        }
        if(usersName.startsWith("e")||usersName.startsWith("E"))
        {
            iv_blog_myProfile.setImageResource(R.drawable.e);
            fab_profile.setImageResource(R.drawable.e);
        }
        if(usersName.startsWith("f")||usersName.startsWith("F"))
        {
            iv_blog_myProfile.setImageResource(R.drawable.f);
            fab_profile.setImageResource(R.drawable.f);
        }
        if(usersName.startsWith("g")||usersName.startsWith("G"))
        {
            iv_blog_myProfile.setImageResource(R.drawable.g);
            fab_profile.setImageResource(R.drawable.g);
        }
        if(usersName.startsWith("h")||usersName.startsWith("H"))
        {
            iv_blog_myProfile.setImageResource(R.drawable.h);
            fab_profile.setImageResource(R.drawable.h);
        }
        if(usersName.startsWith("i")||usersName.startsWith("I"))
        {
            iv_blog_myProfile.setImageResource(R.drawable.i);
            fab_profile.setImageResource(R.drawable.i);
        }
        if(usersName.startsWith("j")||usersName.startsWith("J"))
        {
            iv_blog_myProfile.setImageResource(R.drawable.j);
            fab_profile.setImageResource(R.drawable.j);
        }
        if(usersName.startsWith("k")||usersName.startsWith("K"))
        {
            iv_blog_myProfile.setImageResource(R.drawable.k);
            fab_profile.setImageResource(R.drawable.k);
        }
        if(usersName.startsWith("l")||usersName.startsWith("L"))
        {
            iv_blog_myProfile.setImageResource(R.drawable.l);
            fab_profile.setImageResource(R.drawable.l);
        }
        if(usersName.startsWith("m")||usersName.startsWith("M"))
        {
            iv_blog_myProfile.setImageResource(R.drawable.m);
            fab_profile.setImageResource(R.drawable.m);
        }
        if(usersName.startsWith("n")||usersName.startsWith("N"))
        {
            iv_blog_myProfile.setImageResource(R.drawable.n);
            fab_profile.setImageResource(R.drawable.n);
        }
        if(usersName.startsWith("o")||usersName.startsWith("O"))
        {
            iv_blog_myProfile.setImageResource(R.drawable.o);
            fab_profile.setImageResource(R.drawable.o);
        }
        if(usersName.startsWith("p")||usersName.startsWith("P"))
        {
            iv_blog_myProfile.setImageResource(R.drawable.p);
            fab_profile.setImageResource(R.drawable.p);
        }
        if(usersName.startsWith("q")||usersName.startsWith("Q"))
        {
            iv_blog_myProfile.setImageResource(R.drawable.q);
            fab_profile.setImageResource(R.drawable.q);
        }
        if(usersName.startsWith("r")||usersName.startsWith("R"))
        {
            iv_blog_myProfile.setImageResource(R.drawable.r);
            fab_profile.setImageResource(R.drawable.r);
        }
        if(usersName.startsWith("s")||usersName.startsWith("S"))
        {
            iv_blog_myProfile.setImageResource(R.drawable.s);
            fab_profile.setImageResource(R.drawable.s);
        }
        if(usersName.startsWith("t")||usersName.startsWith("T"))
        {
            iv_blog_myProfile.setImageResource(R.drawable.t);
            fab_profile.setImageResource(R.drawable.t);
        }
        if(usersName.startsWith("u")||usersName.startsWith("U"))
        {
            iv_blog_myProfile.setImageResource(R.drawable.u);
            fab_profile.setImageResource(R.drawable.u);
        }
        if(usersName.startsWith("v")||usersName.startsWith("V"))
        {
            iv_blog_myProfile.setImageResource(R.drawable.v);
            fab_profile.setImageResource(R.drawable.v);
        }
        if(usersName.startsWith("w")||usersName.startsWith("W"))
        {
            iv_blog_myProfile.setImageResource(R.drawable.w);
            fab_profile.setImageResource(R.drawable.w);
        }
        if(usersName.startsWith("x")||usersName.startsWith("X"))
        {
            iv_blog_myProfile.setImageResource(R.drawable.x);
            fab_profile.setImageResource(R.drawable.x);
        }
        if(usersName.startsWith("y")||usersName.startsWith("Y"))
        {
            iv_blog_myProfile.setImageResource(R.drawable.y);
            fab_profile.setImageResource(R.drawable.y);
        }
        if(usersName.startsWith("z")||usersName.startsWith("Z"))
        {
            iv_blog_myProfile.setImageResource(R.drawable.z);
            fab_profile.setImageResource(R.drawable.z);
        }
    }

    private void setProfilePic() {

        profPicRef.child("profile_pic_url").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String url = dataSnapshot.getValue(String.class);
                if(url!="")
                {
                    Picasso.with(Blog_Home.this).load(url).into(iv_blog_myProfile);
                    Picasso.with(Blog_Home.this).load(url).into(fab_profile);
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

    private void filter(String text) {
        ArrayList<BlogPosts_DataModel> filteredArrayList = new ArrayList<BlogPosts_DataModel>();
        ArrayList<String> filteredListId = new ArrayList<String>();
        for(BlogPosts_DataModel bpd: dataModelArrayList){
            if(bpd.getUsername().toLowerCase().contains(text.toLowerCase()))
            {
                filteredArrayList.add(bpd);
            }
        }
        blog_profileAllPostsAdapter.filterList(filteredArrayList);
    }

    private void openNavigationMenu() {

        bottomSheetDialog.show();

        fab_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Blog_Home.this,Chat_Settings.class);
                bottomSheetDialog.dismiss();
                startActivity(intent);
            }
        });

        bottom_nav_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(Blog_Home.this, "Logged out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Blog_Home.this, Login.class);
                bottomSheetDialog.dismiss();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id)
                {
                    case R.id.item_chat:
                    {
                        Intent intent = new Intent(Blog_Home.this, Chat_Home.class);
                        bottomSheetDialog.dismiss();
                        startActivity(intent);
                        finish();
                    }
                    break;
                    case R.id.item_feed: {
                        Intent intent = new Intent(Blog_Home.this, Blog_Home.class);
                        bottomSheetDialog.dismiss();
                        startActivity(intent);
                        finish();
                    }
                    break;
                    case R.id.item_tasks: {
                        Intent intent = new Intent(Blog_Home.this, Tasks_Home.class);
                        bottomSheetDialog.dismiss();
                        startActivity(intent);
                        finish();
                    }
                    break;
                    case R.id.item_share: {
                        shareViaEclectic();
                    }
                    break;
                }
                return true;
            }
        });
    }

    private void shareViaEclectic() {
        Intent intent = new Intent();
        String msg = "Hey,\nCheck out this awesome app : https://drive.google.com/open?id=1kh0l2QljN2WKkccDO5_VJyjTN9e-3nHA";
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, msg);
        intent.setType("text/plain");
        startActivity(intent);
    }

    @SuppressLint("RestrictedApi") @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_blog: {
                openNavigationMenu();
            }
            break;

            case R.id.fab_blog_new_post: {
                if (flag == 1) {
                    fab_blog_newPostImg.setVisibility(View.VISIBLE);
                    fab_blog_newPost_simple.setVisibility(View.VISIBLE);
                    flag = 0;
                } else {
                    fab_blog_newPostImg.setVisibility(View.GONE);
                    fab_blog_newPost_simple.setVisibility(View.GONE);
                    flag = 1;
                }
            }
            break;

            case R.id.imgbtn_blog_search: {

                rl_blog_search_bar.setVisibility(View.VISIBLE);
                edt_blog_search.requestFocus();
                InputMethodManager imm = (InputMethodManager)this.getSystemService(Service.INPUT_METHOD_SERVICE);
                imm.showSoftInput(edt_blog_search, 0);
                fab_blog.setVisibility(View.GONE);
                fab_blog_new_post.setVisibility(View.GONE);
            }
            break;

            case R.id.fab_blog_newPostImg: {
                Intent intent = new Intent(Blog_Home.this, Blog_NewPost_Image.class);
                startActivity(intent);
                fab_blog_newPostImg.setVisibility(View.GONE);
                fab_blog_newPost_simple.setVisibility(View.GONE);
                flag = 1;
            }
            break;

            case R.id.fab_blog_newPost_simple: {
                Intent intent = new Intent(Blog_Home.this, Blog_New_Post.class);
                startActivity(intent);
                fab_blog_newPostImg.setVisibility(View.GONE);
                fab_blog_newPost_simple.setVisibility(View.GONE);
                flag = 1;
            }
            break;

            case R.id.iv_blog_myProfile: {
                Intent intent = new Intent(Blog_Home.this, Blog_MyProfile.class);
                startActivity(intent);
                fab_blog_newPostImg.setVisibility(View.GONE);
                fab_blog_newPost_simple.setVisibility(View.GONE);
                flag = 1;
            }
            break;

            case R.id.ib_blog_close_search :{
                rl_blog_search_bar.setVisibility(View.GONE);
                edt_blog_search.setText("");
                edt_blog_search.clearFocus();
                InputMethodManager imm = (InputMethodManager)this.getSystemService(Service.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edt_blog_search.getWindowToken(), 0);
                fab_blog.setVisibility(View.VISIBLE);
                fab_blog_new_post.setVisibility(View.VISIBLE);
            }
            break;
        }
    }
}
