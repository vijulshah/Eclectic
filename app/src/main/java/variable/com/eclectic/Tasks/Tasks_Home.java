package variable.com.eclectic.Tasks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import variable.com.eclectic.Blogs.Blog_Home;
import variable.com.eclectic.Chats.Chat_Home;
import variable.com.eclectic.Chats.Chat_Settings;
import variable.com.eclectic.Login;
import variable.com.eclectic.R;

public class Tasks_Home extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference profPicRef;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FloatingActionButton fab_task,fab_taskHome_new_task;
    private BottomSheetDialog bottomSheetDialog;
    private SharedPreferences sharedPreferences;
    private CircleImageView fab_profile;
    private TextView user_name;
    private String email;
    private ImageButton bottom_nav_logout;
    private Toolbar toolbar;
    private RelativeLayout rl_empty_task_home,rl_create_new_fol;
    private RecyclerView rv_task_home,rv_drawer;
    private ImageView nav_btn;
    private NavigationView navigation_view;
    private DrawerLayout nav_drawer_layout;
    private ArrayList<TaskModel> dataModelArrayList;
    private ArrayList<String> taskIdArrayList;
    private TaskArrayAdapter taskArrayAdapter;
    private DatabaseHandler db;
    private View headerLayout;
    private ArrayList<TaskModel> folderNamesArrayList;
    private TaskDrawerAdapter taskDrawerAdapter;
    private TextView tv_tasks_folders_empty;
    public RelativeLayout rl_my_tasks;
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
        setContentView(R.layout.activity_tasks_home);

        loadSharedPrefs();
        init();
        loadFirebaseMethods();
        loadProfilePic();
        loadScreen();
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
        bottomSheetDialog = new BottomSheetDialog(Tasks_Home.this);
        bottomSheetDialog.setContentView(bottomNavigation);

        fab_task = (FloatingActionButton)findViewById(R.id.fab_task);
        rl_empty_task_home = (RelativeLayout)findViewById(R.id.rl_empty_task_home);
        rv_task_home = (RecyclerView)findViewById(R.id.rv_task_home);
        nav_btn = (ImageView)findViewById(R.id.nav_btn);
        fab_taskHome_new_task = (FloatingActionButton)findViewById(R.id.fab_taskHome_new_task);
        nav_drawer_layout = (DrawerLayout)findViewById(R.id.nav_drawer_layout);
        navigation_view = (NavigationView)findViewById(R.id.navigation_view);

        headerLayout = navigation_view.getHeaderView(0);
        rl_my_tasks = (RelativeLayout)headerLayout.findViewById(R.id.rl_my_tasks);
        rl_create_new_fol = (RelativeLayout)headerLayout.findViewById(R.id.rl_create_new_fol);
        rv_drawer = (RecyclerView)headerLayout.findViewById(R.id.rv_drawer);
        tv_tasks_folders_empty = (TextView)headerLayout.findViewById(R.id.tv_tasks_folders_empty);

        user_name = (TextView) bottomNavigation.findViewById(R.id.user_name);
        bottom_nav_logout = (ImageButton) bottomNavigation.findViewById(R.id.bottom_nav_logout);
        fab_profile = (CircleImageView) bottomNavigation.findViewById(R.id.fab_profile);
        navigationView = (NavigationView) bottomNavigation.findViewById(R.id.navigation_menu);
        user_name.setText(email);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        } catch (Exception ignored) {
        }

        db = new DatabaseHandler(this,"HOME");

    }

    private void loadFirebaseMethods() {

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent loginIntent = new Intent(Tasks_Home.this, Login.class);
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

    public void loadScreen() {

        dataModelArrayList = db.getAllRecords();
        taskIdArrayList = db.getAllRecordId();
        if(dataModelArrayList.size()>0)
        {
            rl_empty_task_home.setVisibility(View.GONE);
            rv_task_home.setVisibility(View.VISIBLE);
            GridLayoutManager slm = new GridLayoutManager(this,2);
            rv_task_home.setLayoutManager(slm);
            taskArrayAdapter = new TaskArrayAdapter(dataModelArrayList,taskIdArrayList,getApplicationContext(),"HOME");
            rv_task_home.setAdapter(taskArrayAdapter);
            taskArrayAdapter.notifyDataSetChanged();
        }
        else
        {
            rl_empty_task_home.setVisibility(View.VISIBLE);
            rv_task_home.setVisibility(View.GONE);
        }

        folderNamesArrayList = db.getAllFolderNames();
        if(folderNamesArrayList.size()>0)
        {
            tv_tasks_folders_empty.setVisibility(View.GONE);
            rv_drawer.setVisibility(View.VISIBLE);
            LayoutInflater layoutInflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Tasks_Home.this);
            rv_drawer.setLayoutManager(linearLayoutManager);
            taskDrawerAdapter = new TaskDrawerAdapter(getApplicationContext(),folderNamesArrayList);
            rv_drawer.setAdapter(taskDrawerAdapter);
            taskDrawerAdapter.notifyDataSetChanged();
        }
        else
        {
            tv_tasks_folders_empty.setVisibility(View.VISIBLE);
            rv_drawer.setVisibility(View.GONE);
        }
    }

    private void loadOnClickListners() {

        fab_task.setOnClickListener(this);
        nav_btn.setOnClickListener(this);
        fab_taskHome_new_task.setOnClickListener(this);
        rl_my_tasks.setOnClickListener(this);
        rl_create_new_fol.setOnClickListener(this);
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

    private void setProfilePic() {

        profPicRef.child("profile_pic_url").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String url = dataSnapshot.getValue(String.class);
                if(url!="")
                {
                    Picasso.with(Tasks_Home.this).load(url).into(fab_profile);
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

    private void openNavigationMenu() {

        bottomSheetDialog.show();

        fab_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Tasks_Home.this,Chat_Settings.class);
                bottomSheetDialog.dismiss();
                startActivity(intent);
            }
        });

        bottom_nav_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(Tasks_Home.this, "Logged out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Tasks_Home.this, Login.class);
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
                        Intent intent = new Intent(Tasks_Home.this, Chat_Home.class);
                        bottomSheetDialog.dismiss();
                        startActivity(intent);
                        finish();
                    }
                    break;
                    case R.id.item_feed: {
                        Intent intent = new Intent(Tasks_Home.this, Blog_Home.class);
                        bottomSheetDialog.dismiss();
                        startActivity(intent);
                        finish();
                    }
                    break;
                    case R.id.item_tasks: {
                        Intent intent = new Intent(Tasks_Home.this, Tasks_Home.class);
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

    @SuppressLint("WrongConstant") @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.fab_task:
            {
                openNavigationMenu();
            }
            break;

            case R.id.fab_taskHome_new_task:
            {
                Intent intent = new Intent(Tasks_Home.this,New_Task.class);
                intent.putExtra("FOLDER_NAME","HOME");
                startActivity(intent);
                finish();
            }
            break;

            case R.id.nav_btn:
            {
                nav_drawer_layout.openDrawer(Gravity.START);
            }
            break;

            case R.id.rl_create_new_fol :
            {
                showCustomDialogBox();
            }
            break;

            case R.id.rl_my_tasks :
            {
                nav_drawer_layout.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(this,Tasks_Home.class);
                startActivity(intent);
                finish();
            }
            break;
        }
    }

    private void showCustomDialogBox() {

        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        final Dialog dialog =  new Dialog(this);
        Window window = dialog.getWindow();
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.raw_custom_dialog_tasks);

        Button btn_newFolder_create =(Button)dialog.findViewById(R.id.btn_newFolder_create);
        Button btn_newFolder_cancel = (Button)dialog.findViewById(R.id.btn_newFolder_cancel);
        final EditText edt_new_folder_name = (EditText)dialog.findViewById(R.id.edt_new_folder_name);

        btn_newFolder_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(!edt_new_folder_name.getText().toString().trim().isEmpty())
                {
                    Calendar calForDate = Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM,yy");
                    String currentDate = simpleDateFormat.format(calForDate.getTime());

                    Calendar calForTime = Calendar.getInstance();
                    SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm a");
                    String currentTime = simpleTimeFormat.format(calForTime.getTime());

                    TaskModel taskModel = new TaskModel();
                    taskModel.setFolderTitle(edt_new_folder_name.getText().toString());
                    taskModel.setFolderCreateTime(currentTime);
                    taskModel.setFolderCreateDate(currentDate);
                    taskModel.setFolderUpdateTime(currentTime);
                    taskModel.setFolderUpdateTime(currentDate);

                    db.insertFolderNamesRecord(taskModel);
                    db.createTable(edt_new_folder_name.getText().toString());
                    loadScreen();
                    dialog.dismiss();
                    Toast.makeText(Tasks_Home.this, "Folder created", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(Tasks_Home.this, "Please enter folder name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_newFolder_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
