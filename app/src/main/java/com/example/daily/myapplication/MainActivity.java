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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.daily.myapplication.Adapter.TasksAdapter;
import com.example.daily.myapplication.Comparator.DoneFlag;
import com.example.daily.myapplication.Comparator.Priority;
import com.example.daily.myapplication.Comparator.Time;
import com.example.daily.myapplication.DBHelper.DBHelper;
import com.example.daily.myapplication.Dialog.SortSelector;
import com.example.daily.myapplication.EntityClass.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "@vir MainActivity";

    private List<Task> Tasks = new ArrayList<>();
    private DBHelper dbHelper;
    SQLiteDatabase db;
    TasksAdapter tasksAdapter;
    private EditReceiver editReceiver;
    private EditMenuReceiver editMenuReceiver;
    private IntentFilter intentFliter, intentFliter_menu;
    private int containNum = 0;
    private boolean sortFlagPriority = true, sortFlagDoneFlag = true, sortFlagSetTime = true,
            sortFlagDeadLineTime = true;

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
        db.close();
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
        dbHelper = DBHelper.getInstance(this);
        db = dbHelper.getWritableDatabase();
        //test
//        dbHelper.addTask(new Task("1", "1", "2017/11/11 11:11", "2017/11/12 11:11", 0, 0), db);
//        dbHelper.addTask(new Task("1", "1", "2017/11/11 11:11", "2017/11/12 11:11", 4, 0), db);
//        dbHelper.addTask(new Task("1", "1", "2017/11/11 11:11", "2017/11/12 11:11", 1, 0), db);
//        dbHelper.addTask(new Task("1", "1", "2017/11/11 11:11", "2017/11/12 11:11", 3, 0), db);
        //read all in db
        Cursor cursor = db.query("Tasks", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String conTent = cursor.getString(cursor.getColumnIndex("content"));
                String setTime = cursor.getString(cursor.getColumnIndex("setTime"));
                String deadLineTime = cursor.getString(cursor.getColumnIndex("deadLineTime"));
                int priority = cursor.getInt(cursor.getColumnIndex("priority"));
                int doneFlag = cursor.getInt(cursor.getColumnIndex("doneFlag"));
                Task aTask = new Task(title, conTent, setTime, deadLineTime, priority, doneFlag);
                aTask.setHashCode(aTask.hashCode());
                Tasks.add(aTask);
                containNum++;
            } while (cursor.moveToNext());
        }
        cursor.close();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        tasksAdapter = new TasksAdapter(Tasks, MainActivity.this);
        recyclerView.setAdapter(tasksAdapter);
        intentFliter = new IntentFilter();
        intentFliter.addAction("com.example.daily.myapplication.EDIT_RESULT");
        editReceiver = new EditReceiver();
        registerReceiver(editReceiver, intentFliter);
        editMenuReceiver = new EditMenuReceiver();

        intentFliter_menu = new IntentFilter();
        intentFliter_menu.addAction("com.example.daily.myapplication.EDIT_MENU");
        registerReceiver(editMenuReceiver, intentFliter_menu);

        //swipe to delete
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper
                .SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT |
                ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                Log.d(TAG, "onMove: fromPosition=" + fromPosition + "toPosition=" + toPosition);

                dbHelper.updateTask(fromPosition + 1, Tasks.get(toPosition), db);
                dbHelper.updateTask(toPosition + 1, Tasks.get(fromPosition), db);

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
                containNum--;
                dbHelper.delTask(position + 1, db);
                tasksAdapter.notifyItemRemoved(position);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder
                    viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState,
                        isCurrentlyActive);
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true; //false == hide menu
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_task:
                Intent intent = new Intent(MainActivity.this, AddNewActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.select:
                final SortSelector sortSelector = new SortSelector(MainActivity.this);
                //加载布局管理器
                LayoutInflater layoutInflater = LayoutInflater.from(this);
                //将xml布局转换为view对象
                LinearLayout layout = (LinearLayout) layoutInflater.inflate(R.layout
                        .sort_selector, null);
                Button btSortByPriority = layout.findViewById(R.id.selector_byPriority);
                Button btSortByDoneFlag = layout.findViewById(R.id.selector_byDoneFlag);
                Button btSortBySetTime = layout.findViewById(R.id.selector_bySetTime);
                Button btSortByDeadLineTime = layout.findViewById(R.id.selector_byDeadLineTime);
                //test
                Button test = layout.findViewById(R.id.selector_test);
                test.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Task testTask1 = new Task("1","1","2017/10/15 20:20","2017/10/15 20:30",
                                5,0);
                        testTask1.setHashCode(testTask1.hashCode());
                        Task testTask2 = new Task("2","2","2017/10/14 20:20","2017/10/16 20:30",
                                3,1);
                        testTask1.setHashCode(testTask1.hashCode());
                        Task testTask3 = new Task("3","3","2017/11/15 20:20","2017/11/15 20:30",
                                2,0);
                        testTask1.setHashCode(testTask1.hashCode());
                        Task testTask4 = new Task("4","4","2017/10/15 02:20","2017/10/15 03:30",
                                7,1);
                        testTask1.setHashCode(testTask1.hashCode());
                        testTask2.setHashCode(testTask2.hashCode());
                        testTask3.setHashCode(testTask3.hashCode());
                        testTask4.setHashCode(testTask4.hashCode());
                        dbHelper.addTask(++containNum, testTask1, db);
                        dbHelper.addTask(++containNum, testTask2, db);
                        dbHelper.addTask(++containNum, testTask3, db);
                        dbHelper.addTask(++containNum, testTask4, db);
                    }
                });
                btSortByPriority.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Collections.sort(Tasks, new Priority(sortFlagPriority));
                        tasksAdapter.notifyDataSetChanged();
                        int i = 1;
                        for (Task task : Tasks) {
                            dbHelper.updateTask(i, task, db);
                            i++;
                        }
                        sortFlagPriority = !sortFlagPriority;
                    }
                });
                btSortByDoneFlag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Collections.sort(Tasks, new DoneFlag(sortFlagDoneFlag));
                        tasksAdapter.notifyDataSetChanged();
                        int i = 1;
                        for (Task task : Tasks) {
                            dbHelper.updateTask(i, task, db);
                            i++;
                        }
                        sortFlagDoneFlag = !sortFlagDoneFlag;
                    }
                });
                btSortBySetTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Collections.sort(Tasks, new Time(1,sortFlagSetTime));
                        tasksAdapter.notifyDataSetChanged();
                        int i = 1;
                        for (Task task : Tasks) {
                            dbHelper.updateTask(i, task, db);
                            i++;
                        }
                        sortFlagSetTime = !sortFlagSetTime;
                    }
                });
                btSortByDeadLineTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Collections.sort(Tasks, new Time(-1,sortFlagDeadLineTime));
                        tasksAdapter.notifyDataSetChanged();
                        int i = 1;
                        for (Task task : Tasks) {
                            dbHelper.updateTask(i, task, db);
                            i++;
                        }
                        sortFlagDeadLineTime = !sortFlagDeadLineTime;
                    }
                });

                sortSelector.setContentView(layout);
                sortSelector.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //如果不去掉呢？
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Task newTask = (Task) data.getSerializableExtra("newTask");
                    Tasks.add(newTask);
                    dbHelper.addTask(containNum + 1, newTask, db);
                    containNum++;
                    tasksAdapter.notifyItemInserted(Tasks.size() - 1);
                }
        }
    }

    class EditReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int position = intent.getIntExtra("position", 0);
            Task thisTask = (Task) intent.getSerializableExtra("thisTask");
            dbHelper.updateTask(position + 1, thisTask, db);
            Tasks.remove(position);
            Tasks.add(position, thisTask);
            tasksAdapter.notifyDataSetChanged();
        }
    }

    class EditMenuReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String menuCommand = bundle.getString("command");
            int position = 0;
            if (menuCommand.equals("DELETE")) {
                position = bundle.getInt("DELETE_POSITION", 0);
                dbHelper.delTask(position + 1, db);
                Tasks.remove(position);
                containNum--;
                tasksAdapter.notifyItemRemoved(position);
            } else if (menuCommand.equals("DONE")) {
                position = bundle.getInt("DONE_POSITION", 0);
                Log.d(TAG, "onReceive: bundle.position = " + position);
                Task thisTask = Tasks.get(position);
                thisTask.setDoneFlag(1);
                Log.d(TAG, "onReceive: thisTask = " + thisTask);
                Tasks.remove(position);
                Tasks.add(position, thisTask);
                dbHelper.updateTask(position + 1, thisTask, db);
                tasksAdapter.notifyDataSetChanged();
            }
        }
    }

}
