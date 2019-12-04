package variable.com.eclectic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CreateRoomPage extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences sharedPreferences;
    private String uid;
    private String email;
    private String firstName;
    private String lastName;
    private String profilePic;
    private TextView tv_cr_userName;
    private TextView tv_cr_userEmail;
    private EditText edt_cr_companyName;
    private EditText edt_cr_password;
    private Button btn_cr_pay;
    private ImageButton ib_cr_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room_page);

        loadSharedPrefs();
        init();
        loadFirebaseMethods();
        loadOnClickListners();
    }

    private void loadOnClickListners() {

        btn_cr_pay.setOnClickListener(this);
        ib_cr_exit.setOnClickListener(this);
    }

    private void loadFirebaseMethods() {

    }

    private void init() {

        tv_cr_userName = (TextView)findViewById(R.id.tv_cr_userName);
        tv_cr_userEmail = (TextView)findViewById(R.id.tv_cr_userEmail);
        edt_cr_companyName = (EditText)findViewById(R.id.edt_cr_companyName);
        edt_cr_password = (EditText)findViewById(R.id.edt_cr_password);
        btn_cr_pay = (Button)findViewById(R.id.btn_cr_pay);
        ib_cr_exit = (ImageButton)findViewById(R.id.ib_cr_exit);

        tv_cr_userName.setText(firstName+" "+lastName);
        tv_cr_userEmail.setText(email);
    }

    private void loadSharedPrefs() {

        sharedPreferences = getSharedPreferences("MY_EMAIL",MODE_PRIVATE);
        uid = sharedPreferences.getString("UID","");
        email = sharedPreferences.getString("EMAIL","");
        firstName = sharedPreferences.getString("FIRST_NAME","");
        lastName = sharedPreferences.getString("LAST_NAME","");
        profilePic = sharedPreferences.getString("HAS_PROF_PIC","");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.btn_cr_pay :
            {
                String companyName = edt_cr_companyName.getText().toString();
                String roomKey = edt_cr_password.getText().toString();

                if(!companyName.trim().matches("") && !roomKey.trim().matches(""))
                {
                    if(roomKey.length()>6)
                    {
                        Intent intent = new Intent(CreateRoomPage.this, PaymentWebView.class);
                        intent.putExtra("COMPANY_NAME",companyName);
                        intent.putExtra("ROOM_KEY",roomKey);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(this, "Room Key must have at least 7 characters", Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    Toast.makeText(this, "Please enter a valid name and key", Toast.LENGTH_SHORT).show();
                }
            }
            break;

            case R.id.ib_cr_exit :
            {
                Intent intent = new Intent(CreateRoomPage.this,EnterRoom.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
            break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(CreateRoomPage.this,EnterRoom.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
