package com.example.daily.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.daily.myapplication.Adapter.TasksAdapter;
import com.example.daily.myapplication.Comparator.byPriority;
import com.example.daily.myapplication.DBHelper.DBHelper;
import com.example.daily.myapplication.entityClass.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Task> Tasks = new ArrayList<>();
    private DBHelper dbHelper;
    SQLiteDatabase db;
    TasksAdapter tasksAdapter;
    private EditReceiver editReceiver;
    private EditMenuReceiver editMenuReceiver;
    private IntentFilter intentFliter,intentFliter_menu;

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(editReceiver);
        unregisterReceiver(editMenuReceiver);

    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DBHelper(this,"Tasks.db",null,1);
        db = dbHelper.getWritableDatabase();
        //read all in db
        Cursor cursor = db.query("Tasks",null,null,null,null,null,null);
        if(cursor.moveToFirst()) {
            do{
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String conTent = cursor.getString(cursor.getColumnIndex("content"));
                String setTime = cursor.getString(cursor.getColumnIndex("setTime"));
                String deadLineTime = cursor.getString(cursor.getColumnIndex("deadLineTime"));
                Integer priority = cursor.getInt(cursor.getColumnIndex("priority"));
                Integer doneFlag = cursor.getInt(cursor.getColumnIndex("doneFlag"));
                Tasks.add(new Task(title,conTent,setTime,deadLineTime,priority,doneFlag));
            } while (cursor.moveToNext());
        }
        cursor.close();

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycleView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        tasksAdapter = new TasksAdapter(Tasks,MainActivity.this);
        recyclerView.setAdapter(tasksAdapter);
        intentFliter = new IntentFilter();
        intentFliter.addAction("com.example.daily.myapplication.EDIT_RESULT");
        editReceiver = new EditReceiver();
        registerReceiver(editReceiver,intentFliter);

        editMenuReceiver = new EditMenuReceiver();
        intentFliter_menu = new IntentFilter();
        intentFliter_menu.addAction("com.example.daily.myapplication.EDIT_MENU");
        registerReceiver(editMenuReceiver,intentFliter_menu);

        //swipe to delete
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN) {

            //???
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Toast.makeText(MainActivity.this,"onMove",Toast.LENGTH_LONG).show();
                Log.d("onMove IfChan", "onMove: ");
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();

                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(Tasks, i, i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(Tasks, i, i - 1);
                    }
                }
                tasksAdapter.notifyItemMoved(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //Remove swiped item from list and notify the RecyclerView
                final int position = viewHolder.getAdapterPosition();
                Tasks.remove(position);
                dbHelper.delTask(position + 1,db);
                tasksAdapter.notifyItemRemoved(position);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    //滑动时改变Item的透明度
                    final float alpha = 1 - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
                    viewHolder.itemView.setAlpha(alpha);
                    viewHolder.itemView.setTranslationX(dX);
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true; //false == hide menu
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_task:
                Intent intent = new Intent(MainActivity.this,AddNew.class);
                startActivityForResult(intent,1);
                break;
            case R.id.select:
                //List<Task> Tasks_selected = Tasks;
                //Tasks_selected.sort(new byPriority(1));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //如果不去掉呢？
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 1:
                if(resultCode == RESULT_OK) {
                    Task newTask = (Task)data.getSerializableExtra("newTask");
                    Tasks.add(newTask);
                    dbHelper.addTask(newTask,db);
                    tasksAdapter.notifyItemInserted(Tasks.size()-1);
                }
        }
    }

    class EditReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int position = intent.getIntExtra("position",0);
            Task thisTask = (Task) intent.getSerializableExtra("thisTask");
            dbHelper.updateTask(position+1,thisTask,db);
            Tasks.remove(position);
            Tasks.add(position,thisTask);
            tasksAdapter.notifyDataSetChanged();
        }
    }

    class EditMenuReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String menuCommand = bundle.getString("command");
            int position =0;
            if(menuCommand.equals("DELETE")) {
                position = bundle.getInt("DELETE_POSITION",0);
                dbHelper.delTask(position+1,db);
                Tasks.remove(position);
                tasksAdapter.notifyItemRemoved(position);
            } else if(menuCommand.equals("DONE")) {
                position = bundle.getInt("DONE_POSITION",0);
                Task thisTask = Tasks.get(position);
                thisTask.setDoneFlag(1);
                Tasks.remove(position);
                Tasks.add(position,thisTask);
                dbHelper.updateTask(position+1,thisTask,db);
                tasksAdapter.notifyDataSetChanged();
            }
        }
    }

}
