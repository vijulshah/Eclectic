package variable.com.eclectic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {

  private EditText edt_email;
  private FloatingActionButton fab_forgot_passowrd;
  private TextView main_signup;
  private TextView main_login;
  private RelativeLayout relativelayout_forgot_password;
  private ProgressBar progressbar;
  private FirebaseAuth mAuth;
  private FirebaseUser firebaseUser;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_forgot_password);

    init();
    mAuth = FirebaseAuth.getInstance();
    firebaseUser = mAuth.getCurrentUser();

    Bundle extras = getIntent().getExtras();
    if(extras != null) {

      edt_email.setText(extras.getString("EMAIL_ID"));
    }

    fab_forgot_passowrd.setOnClickListener(this);
    main_login.setOnClickListener(this);
    main_signup.setOnClickListener(this);
  }

  private void init() {

    edt_email = (EditText)findViewById(R.id.edt_email);
    fab_forgot_passowrd = (FloatingActionButton)findViewById(R.id.fab_forgot_passowrd);
    main_signup = (TextView)findViewById(R.id.main_signup);
    main_login = (TextView)findViewById(R.id.main_login);
    relativelayout_forgot_password = (RelativeLayout)findViewById(R.id.relativelayout_forgot_password);
    progressbar = (ProgressBar)findViewById(R.id.progressbar);
  }

  @Override
  public void onClick(View v) {

    switch (v.getId())
    {
      case R.id.fab_forgot_passowrd:
      {
        String email = edt_email.getText().toString();
        if(email.matches("")) {

          @SuppressLint("WrongConstant") Snackbar snackbar = Snackbar
                  .make(relativelayout_forgot_password, "Please enter a valid email", Snackbar.LENGTH_LONG)
                  .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                  });
          snackbar.show();
        }
        else {
          sendResetEmail(email);
        }
      }
      break;

      case R.id.main_login:
      {
        goto_LoginPage();
      }
      break;

      case R.id.main_signup:
      {
        goto_SignupPage();
      }
      break;

    }
  }

  private void goto_SignupPage() {

    String email = edt_email.getText().toString();
    Intent intent = new Intent(ForgotPassword.this,SignUp.class);
    intent.putExtra("EMAIL_ID",email);
    startActivity(intent);
    finish();
  }

  private void goto_LoginPage() {

    String email = edt_email.getText().toString();
    Intent intent = new Intent(ForgotPassword.this,Login.class);
    intent.putExtra("EMAIL_ID",email);
    startActivity(intent);
    finish();

  }

  private void sendResetEmail(String email) {

    progressbar.setVisibility(View.VISIBLE);

    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
      @Override
      public void onComplete(@NonNull Task<Void> task) {

        progressbar.setVisibility(View.GONE);

        if(task.isSuccessful())
        {
          Toast.makeText(ForgotPassword.this, "Check you email for reset link", Toast.LENGTH_SHORT).show();
          goto_LoginPage();
        }
        else
        {
          Toast.makeText(ForgotPassword.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
        }
      }
    });
  }
}
