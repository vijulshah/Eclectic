package variable.com.eclectic.Tasks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import variable.com.eclectic.R;

public class OpenTaskToEdit extends AppCompatActivity implements View.OnClickListener {

    private String taskId;
    private EditText edt_editTask_title;
    private EditText edt_editTask_notes;
    private ImageView iv_editTask_back;
    private ImageView iv_editTask_del;
    private TextView tv_editTask_update_date;
    private TextView tv_editTask_create_date;
    private Toolbar edit_toolbar;
    private String folderName;
    private DatabaseHandler db;
    private String pageName;
    private ArrayList<String> arrayList;
    private ArrayList<TaskModel> taskModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_task_to_edit);

        init();
        Bundle extras = getIntent().getExtras();
        taskId = extras.getString("SavedTaskId");
        folderName = extras.getString("FOLDER_NAME");
        taskModelArrayList = new ArrayList<TaskModel>();

        db = new DatabaseHandler(this,folderName);
        taskModelArrayList = db.getRecordById(taskId);
        edt_editTask_title.setText(taskModelArrayList.get(0).getTaskTitle());
        edt_editTask_notes.setText(taskModelArrayList.get(0).getTaskNotes());
        tv_editTask_create_date.setText(taskModelArrayList.get(0).getTaskCreateDate()+" "+taskModelArrayList.get(0).getTaskCreateTime());
        tv_editTask_update_date.setText(taskModelArrayList.get(0).getTaskUpdateDate()+" "+taskModelArrayList.get(0).getTaskUpdateTime());

        edit_toolbar = (Toolbar) findViewById(R.id.edit_toolbar);
        setSupportActionBar(edit_toolbar);

        try {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        } catch (Exception ignored) {
        }

        iv_editTask_del.setOnClickListener(this);
        iv_editTask_back.setOnClickListener(this);
    }

    private void init()
    {
        edt_editTask_title = (EditText)findViewById(R.id.edt_editTask_title);
        edt_editTask_notes = (EditText)findViewById(R.id.edt_editTask_notes);
        iv_editTask_back = (ImageView)findViewById(R.id.iv_editTask_back);
        iv_editTask_del = (ImageView)findViewById(R.id.iv_editTask_del);
        tv_editTask_update_date = (TextView)findViewById(R.id.tv_editTask_update_date);
        tv_editTask_create_date = (TextView)findViewById(R.id.tv_editTask_create_date);
        edit_toolbar = (Toolbar) findViewById(R.id.edit_toolbar);
    }

    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.iv_editTask_del:
            {
                db.deleteRecord(taskId);

                edt_editTask_title.setText("");
                edt_editTask_notes.setText("");

                Intent intent = new Intent(OpenTaskToEdit.this,Tasks_Home.class);
                startActivity(intent);
                finish();
            }
            break;

            case R.id.iv_editTask_back:
            {
                String edtTitle = edt_editTask_title.getText().toString();
                String edtNotes = edt_editTask_notes.getText().toString();

                if(!edtNotes.trim().isEmpty() || !edtTitle.trim().isEmpty())
                {
                    if(edtTitle.trim().isEmpty())
                    {
                        edtTitle = "Untitled";
                    }

                    Calendar calForDate = Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM,yy");
                    String currentDate = simpleDateFormat.format(calForDate.getTime());

                    Calendar calForTime = Calendar.getInstance();
                    SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm a");
                    String currentTime = simpleTimeFormat.format(calForTime.getTime());

                    TaskModel taskModel = new TaskModel();
                    taskModel.setID(taskModelArrayList.get(0).getID());
                    taskModel.setTaskTitle(edtTitle);
                    taskModel.setTaskNotes(edtNotes);
                    taskModel.setTaskCreateDate(taskModelArrayList.get(0).getTaskCreateDate());
                    taskModel.setTaskCreateTime(taskModelArrayList.get(0).getTaskCreateTime());
                    taskModel.setTaskUpdateDate(currentDate);
                    taskModel.setTaskUpdateTime(currentTime);
                    db.updateRecord(taskModel);

                    if(folderName.matches("HOME"))
                    {
                        Intent intent = new Intent(OpenTaskToEdit.this,Tasks_Home.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Intent intent = new Intent(OpenTaskToEdit.this,TaskFolder.class);
                        intent.putExtra("FOLDER_NAME",folderName);
                        startActivity(intent);
                        finish();
                    }
                }
                else
                {
                    if(folderName.matches("HOME"))
                    {
                        Intent intent = new Intent(OpenTaskToEdit.this,Tasks_Home.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Intent intent = new Intent(OpenTaskToEdit.this,TaskFolder.class);
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
