package variable.com.eclectic.Tasks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import variable.com.eclectic.R;

public class New_Task extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_task_created_date;
    private TextView tv_task_updated_date;
    private EditText edt_newTask_title;
    private EditText edt_newTask_notes;
    private ImageView iv_newTask_del;
    private ImageView iv_newTask_back;
    private String folderName;
    private DatabaseHandler db;
    private String currentDate;
    private String currentTime;
    private String pageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        init();
        Bundle extras = getIntent().getExtras();
        folderName = extras.getString("FOLDER_NAME");
        db = new DatabaseHandler(this,folderName);

        iv_newTask_back.setOnClickListener(this);
        iv_newTask_del.setOnClickListener(this);
    }

    private void init()
    {
        tv_task_created_date = (TextView)findViewById(R.id.tv_task_created_date);
        tv_task_updated_date = (TextView)findViewById(R.id.tv_task_updated_date);
        edt_newTask_title = (EditText)findViewById(R.id.edt_newTask_title);
        edt_newTask_notes = (EditText)findViewById(R.id.edt_newTask_notes);
        iv_newTask_del = (ImageView)findViewById(R.id.iv_newTask_del);
        iv_newTask_back = (ImageView)findViewById(R.id.iv_newTask_back);

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM,yy");
        currentDate = simpleDateFormat.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm a");
        currentTime = simpleTimeFormat.format(calForTime.getTime());

        tv_task_created_date.setText(currentDate+"  "+currentTime);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.iv_newTask_del:
            {
                edt_newTask_title.setText("");
                edt_newTask_notes.setText("");

                Intent intent = new Intent(New_Task.this,Tasks_Home.class);
                startActivity(intent);
                finish();
            }
            break;

            case R.id.iv_newTask_back:
            {
                String edtTitle = edt_newTask_title.getText().toString();
                String edtNotes = edt_newTask_notes.getText().toString();

                if(!edtNotes.trim().isEmpty() || !edtTitle.trim().isEmpty())
                {
                    if(edtTitle.trim().isEmpty())
                    {
                        edtTitle = "Untitled";
                    }

                    TaskModel taskModel = new TaskModel();
                    taskModel.setTaskTitle(edtTitle);
                    taskModel.setTaskNotes(edtNotes);
                    taskModel.setTaskCreateDate(currentDate);
                    taskModel.setTaskCreateTime(currentTime);
                    taskModel.setTaskUpdateDate(currentDate);
                    taskModel.setTaskUpdateTime(currentTime);
                    db.insertRecord(taskModel);

                    if(folderName.matches("HOME"))
                    {
                        Intent intent = new Intent(New_Task.this,Tasks_Home.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Intent intent = new Intent(New_Task.this,TaskFolder.class);
                        intent.putExtra("FOLDER_NAME",folderName);
                        startActivity(intent);
                        finish();
                    }
                }
                else
                {
                    if(folderName.matches("HOME"))
                    {
                        Intent intent = new Intent(New_Task.this,Tasks_Home.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Intent intent = new Intent(New_Task.this,TaskFolder.class);
                        intent.putExtra("FOLDER_NAME",folderName);
                        startActivity(intent);
                        finish();
                    }
                }

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
