package variable.com.eclectic;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InvoicePage extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences sharedPreferences;
    private String uid;
    private String email;
    private String firstName;
    private String lastName;
    private String profilePic;
    private String companyName;
    private String roomKey;
    private String orderId;
    private Button btn_invoice_done;
    private String date;
    private String time;
    private DatabaseReference ordersDB;
    private DatabaseReference roomKeyDB;
    private TextView tv_invoice_orderId_value;
    private TextView tv_invoice_name_value;
    private TextView tv_invoice_email_value;
    private TextView tv_invoice_companyName_value;
    private TextView tv_invoice_roomKeyValue;
    private TextView tv_invoice_date_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_page);

        loadSharedPrefs();
        loadIntents();
        init();
        setAllTextViews();
        loadOnLickListners();

    }

    private void loadOnLickListners() {

        btn_invoice_done.setOnClickListener(this);
    }

    private void setAllTextViews() {

        tv_invoice_orderId_value.setText(orderId);
        tv_invoice_name_value.setText(firstName+" "+lastName);
        tv_invoice_email_value.setText(email);
        tv_invoice_companyName_value.setText(companyName);
        tv_invoice_roomKeyValue.setText(roomKey);
        tv_invoice_date_value.setText(date);
    }

    private void loadIntents() {

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            orderId = extras.getString("ORDER_ID");
            companyName = extras.getString("COMPANY_NAME");
            roomKey = extras.getString("ROOM_KEY");
            date = extras.getString("DATE");
            time = extras.getString("TIME");
        }
    }

    private void loadSharedPrefs() {

        sharedPreferences = getSharedPreferences("MY_EMAIL",MODE_PRIVATE);
        uid = sharedPreferences.getString("UID","");
        email = sharedPreferences.getString("EMAIL","");
        firstName = sharedPreferences.getString("FIRST_NAME","");
        lastName = sharedPreferences.getString("LAST_NAME","");
        profilePic = sharedPreferences.getString("HAS_PROF_PIC","");
    }

    private void init() {

        btn_invoice_done = (Button)findViewById(R.id.btn_invoice_done);
        tv_invoice_orderId_value = (TextView)findViewById(R.id.tv_invoice_orderId_value);
        tv_invoice_name_value = (TextView)findViewById(R.id.tv_invoice_name_value);
        tv_invoice_email_value = (TextView)findViewById(R.id.tv_invoice_email_value);
        tv_invoice_companyName_value = (TextView)findViewById(R.id.tv_invoice_companyName_value);
        tv_invoice_roomKeyValue = (TextView)findViewById(R.id.tv_invoice_roomKeyValue);
        tv_invoice_date_value = (TextView)findViewById(R.id.tv_invoice_date_value);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.btn_invoice_done:
            {
                enterDataToDb(orderId);
            }
            break;
        }
    }

    private void enterDataToDb(final String orderId) {

        PaymentsModel paymentsModel = new PaymentsModel();
        paymentsModel.setUid(uid);
        paymentsModel.setName(firstName+" "+lastName);
        paymentsModel.setEmail(email);
        paymentsModel.setOrderId(orderId);
        paymentsModel.setCompanyName(companyName);
        paymentsModel.setRoomKey(roomKey);
        paymentsModel.setOrderDate(date);
        paymentsModel.setOrderTime(time);

        ordersDB = FirebaseDatabase.getInstance().getInstance().getReference().child("Orders").child(orderId);
        roomKeyDB = FirebaseDatabase.getInstance().getInstance().getReference().child("Room_keys");
        roomKeyDB.child(companyName).setValue(roomKey);
        ordersDB.setValue(paymentsModel);

        Intent intent = new Intent(InvoicePage.this,EnterRoom.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {

        enterDataToDb(orderId);
    }
}
