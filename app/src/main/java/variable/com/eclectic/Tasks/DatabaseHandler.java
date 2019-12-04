package variable.com.eclectic.Tasks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "NotesDatabase.db";
    public static final String TABLE_NAME = "TASKS";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_TASK_TITLE = "TASK_TITLE";
    public static final String COLUMN_TASK_NOTES = "TASK_NOTES";
    public static final String COLUMN_TASK_CREATE_DATE = "TASK_CREATE_DATE";
    public static final String COLUMN_TASK_UPDATE_DATE = "TASK_UPDATE_DATE";
    public static final String COLUMN_TASK_CREATE_TIME = "TASK_CREATE_TIME";
    public static final String COLUMN_TASK_UPDATE_TIME = "TASK_UPDATE_TIME";
    public static final String FOLDER_NAMES_TABLE = "FOLDERS";
    public static final String COLUMN_FOLDER_ID = "ID";
    public static final String COLUMN_FOLDER_TITLE = "FOLDER_TITLE";
    public static final String COLUMN_FOLDER_CREATE_DATE = "FOLDER_CREATE_DATE";
    public static final String COLUMN_FOLDER_UPDATE_DATE = "FOLDER_UPDATE_DATE";
    public static final String COLUMN_FOLDER_CREATE_TIME = "FOLDER_CREATE_TIME";
    public static final String COLUMN_FOLDER_UPDATE_TIME = "FOLDER_UPDATE_TIME";
    private final String folderName;
    private final Context context;
    private SQLiteDatabase database;

    public DatabaseHandler(Context context, String folderName) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.folderName = folderName;
        this.context = context;
     }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + folderName+"_"+TABLE_NAME + " ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_TASK_TITLE + " VARCHAR, " + COLUMN_TASK_NOTES + " VARCHAR, " + COLUMN_TASK_CREATE_DATE + " VARCHAR, " + COLUMN_TASK_CREATE_TIME + " VARCHAR, "+ COLUMN_TASK_UPDATE_DATE + " VARCHAR, "+ COLUMN_TASK_UPDATE_TIME + " VARCHAR );");
        db.execSQL("create table " + FOLDER_NAMES_TABLE + " ( " + COLUMN_FOLDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_FOLDER_TITLE + " VARCHAR, " + COLUMN_FOLDER_CREATE_DATE + " VARCHAR, " + COLUMN_FOLDER_CREATE_TIME + " VARCHAR, "+ COLUMN_FOLDER_UPDATE_DATE + " VARCHAR, "+ COLUMN_FOLDER_UPDATE_TIME + " VARCHAR );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + folderName+"_"+TABLE_NAME);
        onCreate(db);
    }

    /*insert data*/
    public void insertRecord(TaskModel taskModel) {
        database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TASK_TITLE, taskModel.getTaskTitle());
        contentValues.put(COLUMN_TASK_NOTES, taskModel.getTaskNotes());
        contentValues.put(COLUMN_TASK_CREATE_DATE, taskModel.getTaskCreateDate());
        contentValues.put(COLUMN_TASK_CREATE_TIME, taskModel.getTaskCreateTime());
        contentValues.put(COLUMN_TASK_UPDATE_DATE, taskModel.getTaskUpdateDate());
        contentValues.put(COLUMN_TASK_UPDATE_TIME, taskModel.getTaskUpdateTime());
        database.insert(folderName+"_"+TABLE_NAME, null, contentValues);
        database.close();
    }

    public void insertRecordAlternate(TaskModel taskModel) {
        database = this.getReadableDatabase();
        database.execSQL("INSERT INTO " + folderName+"_"+TABLE_NAME + "(" + COLUMN_TASK_TITLE + "," + COLUMN_TASK_NOTES + "," + COLUMN_TASK_CREATE_DATE + "," + COLUMN_TASK_CREATE_TIME + "," + COLUMN_TASK_UPDATE_DATE + "," + COLUMN_TASK_UPDATE_TIME +") VALUES('" + taskModel.getTaskTitle() + "','" + taskModel.getTaskNotes() + "','" + taskModel.getTaskCreateDate() + "','" + taskModel.getTaskCreateTime() + "','" + taskModel.getTaskUpdateDate() + "','" + taskModel.getTaskUpdateTime() + "')");
        database.close();
    }

    public ArrayList<TaskModel> getAllRecords() {
        database = this.getReadableDatabase();
        Cursor cursor = database.query(folderName+"_"+TABLE_NAME, null, null, null, null, null, null);
        ArrayList<TaskModel> tasks = new ArrayList<TaskModel>();
        TaskModel taskModel;
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                taskModel = new TaskModel();
                taskModel.setID(cursor.getString(0));
                taskModel.setTaskTitle(cursor.getString(1));
                taskModel.setTaskNotes(cursor.getString(2));
                taskModel.setTaskCreateDate(cursor.getString(3));
                taskModel.setTaskCreateTime(cursor.getString(4));
                taskModel.setTaskUpdateDate(cursor.getString(5));
                taskModel.setTaskUpdateTime(cursor.getString(6));
                tasks.add(taskModel);
            }
        }
        cursor.close();
        database.close();
        return tasks;
    }

    /*Select data*/
    public ArrayList<TaskModel> getAllRecordsAlternate() {
        database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + folderName+"_"+TABLE_NAME, null);
        ArrayList<TaskModel> tasks = new ArrayList<TaskModel>();
        TaskModel taskModel;
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();

                taskModel = new TaskModel();
                taskModel.setID(cursor.getString(0));
                taskModel.setTaskTitle(cursor.getString(1));
                taskModel.setTaskNotes(cursor.getString(2));
                taskModel.setTaskCreateDate(cursor.getString(3));
                taskModel.setTaskCreateTime(cursor.getString(4));
                taskModel.setTaskUpdateDate(cursor.getString(5));
                taskModel.setTaskUpdateTime(cursor.getString(6));
                tasks.add(taskModel);
            }
        }
        cursor.close();
        database.close();
        return tasks;
    }
    public void updateRecord(TaskModel taskModel) {
        database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TASK_TITLE, taskModel.getTaskTitle());
        contentValues.put(COLUMN_TASK_NOTES, taskModel.getTaskNotes());
        contentValues.put(COLUMN_TASK_CREATE_DATE, taskModel.getTaskCreateDate());
        contentValues.put(COLUMN_TASK_CREATE_TIME, taskModel.getTaskCreateTime());
        contentValues.put(COLUMN_TASK_UPDATE_DATE, taskModel.getTaskUpdateDate());
        contentValues.put(COLUMN_TASK_UPDATE_TIME, taskModel.getTaskUpdateTime());
        database.update(folderName+"_"+TABLE_NAME, contentValues, COLUMN_ID + " = ?", new String[]{taskModel.getID()});
        database.close();
    }
    public void updateRecordAlternate(TaskModel taskModel) {
        database = this.getReadableDatabase();
        database.execSQL("update " + folderName+"_"+TABLE_NAME + " set " + COLUMN_TASK_TITLE + " = '" + taskModel.getTaskTitle() + "', " + COLUMN_TASK_NOTES + " = '" + taskModel.getTaskNotes() + "' , " + COLUMN_TASK_UPDATE_DATE + " = '" + taskModel.getTaskUpdateDate() + "' and " + COLUMN_TASK_UPDATE_TIME + " = '" + taskModel.getTaskUpdateTime() + "' where '" + COLUMN_ID + " = '" + taskModel.getID() + "'");
        database.close();
    }
    public void deleteAllRecords() {
        database = this.getReadableDatabase();
        database.delete(folderName+"_"+TABLE_NAME, null, null);
        database.close();
    }
    public void deleteAllRecordsAlternate() {
        database = this.getReadableDatabase();
        database.execSQL("delete from " + folderName+"_"+TABLE_NAME);
        database.close();
    }
    public void deleteFolderNameRecord(String folderName) {
        database = this.getReadableDatabase();
        database.delete(FOLDER_NAMES_TABLE, COLUMN_FOLDER_TITLE + " = ?", new String[]{folderName});
        database.close();
    }
    public void deleteRecord(String taskId) {
        database = this.getReadableDatabase();
        database.delete(folderName+"_"+TABLE_NAME, COLUMN_ID + " = ?", new String[]{taskId});
        database.close();
    }
    public void deleteRecordAlternate(TaskModel taskModel) {
        database = this.getReadableDatabase();
        database.execSQL("delete from " + TABLE_NAME + " where " + COLUMN_ID + " = '" + taskModel.getID() + "'");
        database.close();
    }

    public ArrayList<String> getAllTableName() {
        database = this.getReadableDatabase();
        ArrayList<String> allTableNames = new ArrayList<String>();
        Cursor cursor = database.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                allTableNames.add(cursor.getString(cursor.getColumnIndex("name")));
            }
        }
        cursor.close();
        database.close();
        return allTableNames;
    }

    public ArrayList<String> getAllRecordId()
    {
        database = this.getReadableDatabase();
        ArrayList<String> allRecordIds = new ArrayList<String>();
        Cursor cursor = database.rawQuery("SELECT "+COLUMN_ID+" FROM "+folderName+"_"+TABLE_NAME,null);
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                allRecordIds.add(cursor.getString(cursor.getColumnIndex("ID")));
            }
        }
        cursor.close();
        database.close();
       return allRecordIds;
    }

    public void createTable(String folderName)
    {
        database = this.getReadableDatabase();
        try{
            database.execSQL("create table " + folderName+"_"+TABLE_NAME + " ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_TASK_TITLE + " VARCHAR, " + COLUMN_TASK_NOTES + " VARCHAR, " + COLUMN_TASK_CREATE_DATE + " VARCHAR, " + COLUMN_TASK_CREATE_TIME + " VARCHAR, "+ COLUMN_TASK_UPDATE_DATE + " VARCHAR, "+ COLUMN_TASK_UPDATE_TIME + " VARCHAR );");
        }
        catch (Exception e)
        {
            database.execSQL("DROP TABLE IF EXISTS " + folderName+"_"+TABLE_NAME);
            database.execSQL("create table " + folderName+"_"+TABLE_NAME + " ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_TASK_TITLE + " VARCHAR, " + COLUMN_TASK_NOTES + " VARCHAR, " + COLUMN_TASK_CREATE_DATE + " VARCHAR, " + COLUMN_TASK_CREATE_TIME + " VARCHAR, "+ COLUMN_TASK_UPDATE_DATE + " VARCHAR, "+ COLUMN_TASK_UPDATE_TIME + " VARCHAR );");
        }
        database.close();
    }

    public ArrayList<TaskModel> getRecordById(String taskId)
    {
        database = this.getReadableDatabase();
        ArrayList<TaskModel> tasks = new ArrayList<TaskModel>();
        Cursor cursor = database.rawQuery("SELECT * FROM "+folderName+"_"+TABLE_NAME+" WHERE ID = "+ taskId,null);
        TaskModel taskModel;
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                taskModel = new TaskModel();
                taskModel.setID(cursor.getString(0));
                taskModel.setTaskTitle(cursor.getString(1));
                taskModel.setTaskNotes(cursor.getString(2));
                taskModel.setTaskCreateDate(cursor.getString(3));
                taskModel.setTaskCreateTime(cursor.getString(4));
                taskModel.setTaskUpdateDate(cursor.getString(5));
                taskModel.setTaskUpdateTime(cursor.getString(6));
                tasks.add(taskModel);
            }
        }
        cursor.close();
        database.close();
        return tasks;
    }

    public void insertFolderNamesRecord(TaskModel taskModel)
    {
        database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_FOLDER_ID, taskModel.getFid());
        contentValues.put(COLUMN_FOLDER_TITLE, taskModel.getFolderTitle());
        contentValues.put(COLUMN_FOLDER_CREATE_DATE, taskModel.getFolderCreateDate());
        contentValues.put(COLUMN_FOLDER_CREATE_TIME, taskModel.getFolderCreateTime());
        contentValues.put(COLUMN_FOLDER_UPDATE_DATE, taskModel.getFolderUpdateDate());
        contentValues.put(COLUMN_FOLDER_UPDATE_TIME, taskModel.getFolderUpdateTime());
        database.insert(FOLDER_NAMES_TABLE, null, contentValues);
        database.close();
    }

    public ArrayList<TaskModel> getAllFolderNames()
    {
        database = this.getReadableDatabase();
        Cursor cursor = database.query(FOLDER_NAMES_TABLE, null, null, null, null, null, null);
        ArrayList<TaskModel> tasks = new ArrayList<TaskModel>();
        TaskModel taskModel;
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                taskModel = new TaskModel();
                taskModel.setFid(cursor.getString(0));
                taskModel.setFolderTitle(cursor.getString(1));
                taskModel.setFolderCreateDate(cursor.getString(2));
                taskModel.setFolderCreateTime(cursor.getString(3));
                taskModel.setFolderUpdateDate(cursor.getString(4));
                taskModel.setFolderUpdateTime(cursor.getString(5));
                tasks.add(taskModel);
            }
        }
        cursor.close();
        database.close();
        return tasks;
    }
}
