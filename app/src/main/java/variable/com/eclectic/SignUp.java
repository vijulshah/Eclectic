package variable.com.eclectic;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import variable.com.eclectic.Chats.Chat_RecyclerView_DataModel;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private TextView main_login;
    private EditText edt_email;
    private ShowHidePasswordEditText edt_password;
    private ShowHidePasswordEditText edt_confirm_password;
    private RelativeLayout relativelayout_signup;
    private ProgressBar progressbar;
    private FirebaseAuth mAuth;
    private FloatingActionButton fab_signup;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private FirebaseUser firebaseUser;
    private DatabaseReference rootRef;
    private DatabaseReference profPhotoRef;
    private static final int RC_SIGN_IN=1;
    private Button btn_verify_otp;
    private EditText edt_fname;
    private EditText edt_lname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();
        loadFirebaseMethods();
        loadOnClickListners();

    }

    private void loadOnClickListners() {

        main_login.setOnClickListener(this);
        fab_signup.setOnClickListener(this);
    }

    private void loadFirebaseMethods() {

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        rootRef = FirebaseDatabase.getInstance().getReference("User");

        mAuthListner = new FirebaseAuth.AuthStateListener(){

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseUser != null && firebaseUser.isEmailVerified()==false)
                {
                    Intent intent = new Intent(SignUp.this,Login.class);
                    startActivity(intent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    finish();
                }
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = mAuth.getCurrentUser();
        mAuth.addAuthStateListener(mAuthListner);
    }

    private void init() {

        relativelayout_signup = (RelativeLayout)findViewById(R.id.relativelayout_signup);
        main_login = (TextView)findViewById(R.id.main_login);
        edt_email = (EditText)findViewById(R.id.edt_email);
        edt_password = (ShowHidePasswordEditText)findViewById(R.id.edt_password);
        edt_confirm_password = (ShowHidePasswordEditText)findViewById(R.id.edt_confirm_password);
        fab_signup = (FloatingActionButton)findViewById(R.id.fab_signup);
        progressbar = (ProgressBar)findViewById(R.id.progressbar);
        edt_fname = (EditText)findViewById(R.id.edt_fname);
        edt_lname = (EditText)findViewById(R.id.edt_lname);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.fab_signup:
            {
                String email = edt_email.getText().toString();
                String password = edt_password.getText().toString();
                String confirm_password = edt_confirm_password.getText().toString();
                String firstName = edt_fname.getText().toString();
                String lastName = edt_lname.getText().toString();

                if(email.matches("")||password.matches("")||
                        confirm_password.matches("")||!confirm_password.equals(password)) {

                    @SuppressLint("WrongConstant") Snackbar snackbar = Snackbar
                            .make(relativelayout_signup, "Please enter a valid name, email and password", Snackbar.LENGTH_LONG)
                            .setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });
                    snackbar.show();
                }
                else {

                    setdata(email, password, firstName, lastName);
                }
            }
            break;

            case R.id.main_login:
            {
                String email = edt_email.getText().toString();
                Intent intent = new Intent(SignUp.this,Login.class);
                intent.putExtra("EMAIL_ID",email);
                startActivity(intent);
                finish();
            }
            break;
        }
    }

    private void setdata(final String email, final String password, final String firstName, final String lastName) {

        progressbar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        progressbar.setVisibility(View.GONE);

                        if (task.isSuccessful()) {

                            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful())
                                    {
                                        String currentUserId = mAuth.getCurrentUser().getUid();
                                        Chat_RecyclerView_DataModel chat_recyclerView_dataModel = new Chat_RecyclerView_DataModel();

                                        chat_recyclerView_dataModel.setUid(currentUserId);
                                        chat_recyclerView_dataModel.setEmail(email);
                                        chat_recyclerView_dataModel.setFirstName(firstName);
                                        chat_recyclerView_dataModel.setLastName(lastName);

                                        rootRef.child(currentUserId).setValue(chat_recyclerView_dataModel);
                                        rootRef.child(currentUserId).child("ProfilePic").child("HasProfilePic").setValue("no");

                                        Toast.makeText(SignUp.this, "Please check your email for verification", Toast.LENGTH_SHORT).show();
                                        sendToLoginActivity(email,password);
                                    }
                                    else
                                    {
                                        Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        } else {

                            Toast.makeText(SignUp.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void sendToLoginActivity(String email, String password) {

        Intent intent = new Intent(SignUp.this,Login.class);
        intent.putExtra("EMAIL_ID",email);
        intent.putExtra("PASSWORD",password);
        startActivity(intent);
        finish();
    }
}
