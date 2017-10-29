package com.example.daily.myapplication.DBHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.daily.myapplication.entityClass.Task;


public class DBHelper extends SQLiteOpenHelper {
    private static final String NAME = "Tasks.db";
    private static int VERSION = 1;
    private static DBHelper dbHelper = null;
    private final String TAG = "@vir DBHelper";

    public static final String CREATE_BOOK = "create table Tasks(" +
            "id integer primary key autoincrement" +
            ",title text," +
            "content text," +
            "setTime text," +
            "deadLineTime text," +
            "priority integer," +
            "doneFlag integer," +
            "hashCode integer)";

    public DBHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    public static DBHelper getInstance(Context context) {
        if (dbHelper == null) {
            dbHelper = new DBHelper(context);
        }
        return dbHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addTask(Task task, SQLiteDatabase db) {
        Log.d(TAG, "addTask: Task = "+task.toString());
        db.execSQL("insert into Tasks (title, content, setTime, deadLineTime, priority, doneFlag, hashCode) values(?, ?, ?, ?, ?, ?, ?)"
                , new String[]{task.getTitle(), task.getContent(), task.getSetTime(),
                        task.getdeadLineTime(), Integer.toString(task.getPriority()), Integer
                        .toString(task.getDoneFlag()), Integer.toString(task.getHashCode())});
    }

    public void delTask(int position, SQLiteDatabase db) {
        db.execSQL("delete from Tasks where id = ?", new String[]{Integer.toString(position)});
        //后续要优化（触发器？？？）
        db.execSQL("update Tasks set id=id-1 where id > ?", new String[]{Integer.toString
                (position)});
    }

    public void updateTask(int position, Task thisTask, SQLiteDatabase db) {
        /*db.execSQL("update Tasks set " +
                "title ?," +
                "content ?," +
                "setTime ?," +
                "deadLineTime ?," +
                "priority ? " +
                "where id = ?",new String[] {thisTask.getTitle(),thisTask.getContent(),
        thisTask.getSetTime(),thisTask.getdeadLineTime(),Integer.toString(thisTask.getPriority()),
        Integer.toString(position)});*/
        /*String strSQL = "UPDATE Tasks SET title = " +
                thisTask.getTitle()+", content = " +
                thisTask.getContent()+", setTime= " +
                thisTask.getSetTime()+", deadLineTime = " +
                thisTask.getdeadLineTime()+", priority = " +
                Integer.toString(thisTask.getPriority())+" " +
                "WHERE columnId = "+ position;

        db.execSQL(strSQL);*/
        db.beginTransaction();
        db.execSQL("update Tasks set title = ? where id = ?", new String[]{thisTask.getTitle(),
                Integer.toString(position)});
        db.execSQL("update Tasks set content = ? where id = ?", new String[]{thisTask.getContent
                (), Integer.toString(position)});
        db.execSQL("update Tasks set setTime = ? where id = ?", new String[]{thisTask.getSetTime
                (), Integer.toString(position)});
        db.execSQL("update Tasks set deadLineTime = ? where id = ?", new String[]{thisTask
                .getdeadLineTime(), Integer.toString(position)});
        db.execSQL("update Tasks set priority = ? where id = ?", new String[]{Integer.toString
                (thisTask.getPriority()), Integer.toString(position)});
        db.execSQL("update Tasks set hashCode = ? where id = ?", new String[]{Integer.toString
                (thisTask.getHashCode()), Integer.toString(position)});
//        db.beginTransaction();
        db.setTransactionSuccessful();
        db.endTransaction();
    }


    public void removeAllColumns(SQLiteDatabase db) {
        db.delete("Tasks", null, null);
    }

}
