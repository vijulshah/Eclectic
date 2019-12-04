package variable.com.eclectic.Chats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import variable.com.eclectic.Blogs.Blog_Home;
import variable.com.eclectic.Login;
import variable.com.eclectic.R;
import variable.com.eclectic.Tasks.Tasks_Home;

public class Chat_Home extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference profPicRef;
    public FloatingActionButton fab_main;
    private BottomSheetDialog bottomSheetDialog;
    private CircleImageView fab_profile;
    private TextView user_name;
    private SharedPreferences sharedPreferences;
    private ViewPager tab_viewpager;
    private TabLayout tab_tablayout;
    private String email;
    private ImageButton bottom_nav_logout;
    private ImageButton imgbtn_chat_search;
    private String userId;
    private String firstName;
    private String lastName;
    private String currentRoom;
    private String hasProfPic;
    private View bottomNavigation;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_home);

        loadSharedPrefs();
        init();
        loadFirebaseMethods();
        loadProfilePic();
        loadLayout();
        //loadSearchActive();
        loadOnClickListners();

    }

    private void loadLayout() {

        tab_tablayout.setOnTabSelectedListener(Chat_Home.this);

        MyTabFrag_PagerAdapter myTabFrag_pagerAdapter = new MyTabFrag_PagerAdapter(getSupportFragmentManager(),tab_tablayout.getTabCount());
        tab_viewpager.setAdapter(myTabFrag_pagerAdapter);
        TabLayout.Tab tab = tab_tablayout.getTabAt(0);
        tab.select();
        tab_viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab_tablayout));
    }

    private void loadFirebaseMethods() {

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent loginIntent = new Intent(Chat_Home.this, Login.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                }
            }
        };

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
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

    private void setProfilePic() {

        profPicRef.child("profile_pic_url").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String url = dataSnapshot.getValue(String.class);
                if(url!="")
                {
                    Picasso.with(Chat_Home.this).load(url).into(fab_profile);
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
            fab_profile.setImageResource(R.drawable.a);
        }
        if(usersName.startsWith("b")||usersName.startsWith("B"))
        {
            fab_profile.setImageResource(R.drawable.b);
        }
        if(usersName.startsWith("c")||usersName.startsWith("C"))
        {
            fab_profile.setImageResource(R.drawable.c);
        }
        if(usersName.startsWith("d")||usersName.startsWith("D"))
        {
            fab_profile.setImageResource(R.drawable.d);
        }
        if(usersName.startsWith("e")||usersName.startsWith("E"))
        {
            fab_profile.setImageResource(R.drawable.e);
        }
        if(usersName.startsWith("f")||usersName.startsWith("F"))
        {
            fab_profile.setImageResource(R.drawable.f);
        }
        if(usersName.startsWith("g")||usersName.startsWith("G"))
        {
            fab_profile.setImageResource(R.drawable.g);
        }
        if(usersName.startsWith("h")||usersName.startsWith("H"))
        {
            fab_profile.setImageResource(R.drawable.h);
        }
        if(usersName.startsWith("i")||usersName.startsWith("I"))
        {
            fab_profile.setImageResource(R.drawable.i);
        }
        if(usersName.startsWith("j")||usersName.startsWith("J"))
        {
            fab_profile.setImageResource(R.drawable.j);
        }
        if(usersName.startsWith("k")||usersName.startsWith("K"))
        {
            fab_profile.setImageResource(R.drawable.k);
        }
        if(usersName.startsWith("l")||usersName.startsWith("L"))
        {
            fab_profile.setImageResource(R.drawable.l);
        }
        if(usersName.startsWith("m")||usersName.startsWith("M"))
        {
            fab_profile.setImageResource(R.drawable.m);
        }
        if(usersName.startsWith("n")||usersName.startsWith("N"))
        {
            fab_profile.setImageResource(R.drawable.n);
        }
        if(usersName.startsWith("o")||usersName.startsWith("O"))
        {
            fab_profile.setImageResource(R.drawable.o);
        }
        if(usersName.startsWith("p")||usersName.startsWith("P"))
        {
            fab_profile.setImageResource(R.drawable.p);
        }
        if(usersName.startsWith("q")||usersName.startsWith("Q"))
        {
            fab_profile.setImageResource(R.drawable.q);
        }
        if(usersName.startsWith("r")||usersName.startsWith("R"))
        {
            fab_profile.setImageResource(R.drawable.r);
        }
        if(usersName.startsWith("s")||usersName.startsWith("S"))
        {
            fab_profile.setImageResource(R.drawable.s);
        }
        if(usersName.startsWith("t")||usersName.startsWith("T"))
        {
            fab_profile.setImageResource(R.drawable.t);
        }
        if(usersName.startsWith("u")||usersName.startsWith("U"))
        {
            fab_profile.setImageResource(R.drawable.u);
        }
        if(usersName.startsWith("v")||usersName.startsWith("V"))
        {
            fab_profile.setImageResource(R.drawable.v);
        }
        if(usersName.startsWith("w")||usersName.startsWith("W"))
        {
            fab_profile.setImageResource(R.drawable.w);
        }
        if(usersName.startsWith("x")||usersName.startsWith("X"))
        {
            fab_profile.setImageResource(R.drawable.x);
        }
        if(usersName.startsWith("y")||usersName.startsWith("Y"))
        {
            fab_profile.setImageResource(R.drawable.y);
        }
        if(usersName.startsWith("z")||usersName.startsWith("Z"))
        {
            fab_profile.setImageResource(R.drawable.z);
        }
    }

    private void loadOnClickListners() {

        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNavigationMenu();
            }
        });
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

    private void openNavigationMenu() {

        bottomSheetDialog.show();

        fab_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Chat_Home.this,Chat_Settings.class);
                bottomSheetDialog.dismiss();
                startActivity(intent);
            }
        });

        bottom_nav_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(Chat_Home.this, "Logged out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Chat_Home.this, Login.class);
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
                    case R.id.item_chat :
                    {
                        Intent intent = new Intent(Chat_Home.this, Chat_Home.class);
                        bottomSheetDialog.dismiss();
                        startActivity(intent);
                        finish();
                    }
                    break;
                    case R.id.item_feed :
                    {
                        Intent intent = new Intent(Chat_Home.this, Blog_Home.class);
                        bottomSheetDialog.dismiss();
                        startActivity(intent);
                        finish();
                    }
                    break;
                    case R.id.item_tasks :
                    {
                        Intent intent = new Intent(Chat_Home.this, Tasks_Home.class);
                        bottomSheetDialog.dismiss();
                        startActivity(intent);
                        finish();
                    }
                    break;
                    case R.id.item_share :
                    {
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

    private void init() {

        bottomNavigation = getLayoutInflater().inflate(R.layout.navigation_menu,null);
        bottomSheetDialog = new BottomSheetDialog(Chat_Home.this);
        bottomSheetDialog.setContentView(bottomNavigation);

        fab_main = findViewById(R.id.fab_main);
        tab_viewpager = (ViewPager)findViewById(R.id.tab_viewpager);
        tab_tablayout = (TabLayout)findViewById(R.id.tab_tablayout);

        tab_tablayout.addTab(tab_tablayout.newTab().setText("CHATS"));
        tab_tablayout.addTab(tab_tablayout.newTab().setText("GROUPS"));
        tab_tablayout.setTabGravity(TabLayout.GRAVITY_FILL);

        LinearLayout layout = ((LinearLayout) ((LinearLayout) tab_tablayout.getChildAt(0)).getChildAt(0));
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layout.getLayoutParams();
        layout.setLayoutParams(layoutParams);

        imgbtn_chat_search = (ImageButton)findViewById(R.id.imgbtn_chat_search);
        user_name = (TextView) bottomNavigation.findViewById(R.id.user_name);
        bottom_nav_logout = (ImageButton) bottomNavigation.findViewById(R.id.bottom_nav_logout);
        fab_profile = (CircleImageView)bottomNavigation.findViewById(R.id.fab_profile);
        navigationView = (NavigationView) bottomNavigation.findViewById(R.id.navigation_menu);
        user_name.setText(email);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        tab_viewpager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
