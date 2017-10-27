package com.example.daily.myapplication;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.daily.myapplication.BroadcastReceiver.ClockReceiver;
import com.example.daily.myapplication.TimeSelector.HMselector;
import com.example.daily.myapplication.TimeSelector.Selector;
import com.example.daily.myapplication.entityClass.Task;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AddNew extends AppCompatActivity {
    private Task newTask;
    private String mtitle,mcontent,msetTime,mdeadLineTime;
    private int mpriority;
    private EditText title, content ,priority;
    private Button bt_date_1,bt_date_2,bt_time_1,bt_time_2,btADD;
    private Selector selector_1 = new Selector();
    private Selector selector_2 = new Selector();
    private HMselector hMselector_1 = new HMselector();
    private HMselector hMselector_2 = new HMselector();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addtask);
        title = (EditText) findViewById(R.id.add_title);
        content = (EditText) findViewById(R.id.add_content);
        priority = (EditText) findViewById(R.id.add_priority);
        bt_date_1 = (Button)findViewById(R.id.add_date_1);
        bt_date_2 = (Button)findViewById(R.id.add_date_2);
        bt_time_1 = (Button)findViewById(R.id.add_time_1);
        bt_time_2 = (Button)findViewById(R.id.add_time_2);
        btADD = (Button)findViewById(R.id.add_btADD);

        bt_date_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selector_1.show(getFragmentManager(),"date_1");
                Log.d("TestIfChan", "date_1: "+selector_1.getTime());
            }
        });

        bt_date_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selector_2.show(getFragmentManager(),"date_2");
            }
        });

        bt_time_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hMselector_1.show(getFragmentManager(),"time_1");
            }
        });

        bt_time_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hMselector_2.show(getFragmentManager(),"time_2");
            }
        });

        btADD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TestIfChan", "before back 1");
                mtitle = title.getText().toString();
                Log.d("TestIfChan", "before back 2");
                mcontent = content.getText().toString();
                Log.d("TestIfChan", "before back 3");
                mpriority = Integer.parseInt(priority.getText().toString());
                Log.d("TestIfChan", "before back 4");
                msetTime = selector_1.getTime()+hMselector_1.getTime();
                Log.d("TestIfChan", "before back 5");
                mdeadLineTime = selector_2.getTime()+hMselector_2.getTime();
                Log.d("TestIfChan", "before back 6");
                newTask = new Task(mtitle,mcontent,msetTime,mdeadLineTime,mpriority,0);
                Log.d("TestIfChan", "before back "+newTask.toString());
                Intent intent = new Intent();
                intent.putExtra("newTask", newTask);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

    }
}
