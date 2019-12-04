package variable.com.eclectic;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import variable.com.eclectic.Chats.Chat_RecyclerView_DataModel;

public class Login extends AppCompatActivity implements View.OnClickListener{

    private TextView main_signup;
    private FloatingActionButton fab_login;
    private TextView tv_forgot_password;
    private EditText edt_email;
    private EditText edt_password;
    private ProgressBar progressbar;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private RelativeLayout relativelayout_login;
    private SharedPreferences sharedPreferences;
    private String uid;
    private String email_id;
    private String lastName;
    private String firstName;
    private String profilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        loadIntents();
        loadOnClickListners();

    }

    private void loadOnClickListners() {

        main_signup.setOnClickListener(this);
        fab_login.setOnClickListener(this);
        tv_forgot_password.setOnClickListener(this);
    }

    private void loadIntents() {
        Bundle extras = getIntent().getExtras();
        if(extras != null) {

            edt_email.setText(extras.getString("EMAIL_ID"));
            edt_password.setText(extras.getString("PASSWORD"));
        }
    }

    private void init() {

        main_signup = (TextView)findViewById(R.id.main_signup);
        fab_login = (FloatingActionButton)findViewById(R.id.fab_login);
        tv_forgot_password = (TextView)findViewById(R.id.tv_forgot_password);
        edt_email = (EditText)findViewById(R.id.edt_email);
        edt_password = (EditText)findViewById(R.id.edt_password);
        progressbar = (ProgressBar)findViewById(R.id.progressbar);
        relativelayout_login = (RelativeLayout)findViewById(R.id.relativelayout_login);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.fab_login:
            {
                String email = edt_email.getText().toString();
                String password = edt_password.getText().toString();

                if(email.matches("")&&password.matches("")) {

                    @SuppressLint("WrongConstant") Snackbar snackbar = Snackbar
                            .make(relativelayout_login, "Please enter a valid email and password", Snackbar.LENGTH_LONG)
                            .setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });
                    snackbar.show();
                }
                else {
                    getData(email, password);
                }
            }
            break;

            case R.id.main_signup:
            {
                String email = edt_email.getText().toString();
                Intent intent = new Intent(Login.this,SignUp.class);
                intent.putExtra("EMAIL_ID",email);
                startActivity(intent);
                finish();
            }
            break;

            case R.id.tv_forgot_password:
            {
                String email = edt_email.getText().toString();
                Intent intent = new Intent(Login.this,ForgotPassword.class);
                intent.putExtra("EMAIL_ID",email);
                startActivity(intent);
                finish();
            }
            break;

        }
    }

    private void getData(final String email, String password) {

        progressbar.setVisibility(View.VISIBLE);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        progressbar.setVisibility(View.GONE);

                        if (task.isSuccessful()) {

                            if(mAuth.getCurrentUser().isEmailVerified())
                            {
                                 setDataToPref(mAuth.getCurrentUser().getUid());
                            }
                            else
                            {
                                Toast.makeText(Login.this, "Please verify your email address", Toast.LENGTH_SHORT).show();
                            }

                        } else {

                            Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void setDataToPref(String user_id) {

        DatabaseReference mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("User").child(user_id);
        mDatabaseUsers.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                email_id = dataSnapshot.child("email").getValue(String.class);
                uid = dataSnapshot.child("uid").getValue(String.class);
                firstName = dataSnapshot.child("firstName").getValue(String.class);
                lastName = dataSnapshot.child("lastName").getValue(String.class);
                profilePic = dataSnapshot.child("ProfilePic").child("HasProfilePic").getValue(String.class);

                SharedPreferences  sharedPreferences = getSharedPreferences("MY_EMAIL",MODE_PRIVATE);
                SharedPreferences.Editor editor =  sharedPreferences.edit();
                editor.putString("EMAIL",email_id);
                editor.putString("UID", uid);
                editor.putString("FIRST_NAME",firstName);
                editor.putString("LAST_NAME",lastName);
                editor.putString("HAS_PROF_PIC",profilePic);
                editor.commit();

                Intent intent = new Intent(Login.this,EnterRoom.class);
                startActivity(intent);
                finish();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
