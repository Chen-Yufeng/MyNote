package com.example.daily.myapplication;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.daily.myapplication.TimeSelector.HMselector;
import com.example.daily.myapplication.TimeSelector.Selector;
import com.example.daily.myapplication.entityClass.Task;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
public class EditPage extends AppCompatActivity {
    private EditText title, content, priority;
    private Button bt_date_1,bt_date_2,bt_time_1,bt_time_2,btUPDATE;
    private Selector selector_1 = new Selector();
    private Selector selector_2 = new Selector();
    private HMselector hMselector_1 = new HMselector();
    private HMselector hMselector_2 = new HMselector();
    private String mtitle,mcontent,msetTime,mdeadLineTime;
    private int mpriority;
    private Task thisTask;
    private int position;
    boolean sync_date_1 = false,sync_date_2 = false,sync_time_1 = false,sync_time_2 = false;
    File mediaFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_page);
        final Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        position = bundle.getInt("position",0);
        thisTask = (Task)bundle.getSerializable("thisTask");
        title = (EditText)findViewById(R.id.edit_title);
        content = (EditText)findViewById(R.id.edit_content);
        priority = (EditText)findViewById(R.id.edit_priority);
        title.setText(thisTask.getTitle());
        content.setText(thisTask.getContent());
        priority.setText(Integer.toString(thisTask.getPriority()));
        bt_date_1 = (Button)findViewById(R.id.edit_date_1);
        bt_date_2 = (Button)findViewById(R.id.edit_date_2);
        bt_time_1 = (Button)findViewById(R.id.edit_time_1);
        bt_time_2 = (Button)findViewById(R.id.edit_time_2);
        btUPDATE = (Button)findViewById(R.id.edit_btADD);

        bt_date_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sync_date_1 = true;
                selector_1.show(getFragmentManager(),"date_1");
            }
        });

        bt_date_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sync_date_2 = true;
                selector_2.show(getFragmentManager(),"date_2");
            }
        });

        bt_time_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sync_time_1 = true;
                hMselector_1.show(getFragmentManager(),"time_1");
            }
        });

        bt_time_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sync_time_2 = true;
                hMselector_2.show(getFragmentManager(),"time_2");
            }
        });

        btUPDATE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mtitle = title.getText().toString();
                mcontent = content.getText().toString();
                mpriority = Integer.parseInt(priority.getText().toString());
                msetTime = selector_1.getTime()+hMselector_1.getTime();
                mdeadLineTime = selector_2.getTime()+hMselector_2.getTime();
                mpriority = Integer.parseInt(priority.getText().toString());
                Task thisTask_return = new Task(mtitle,mcontent,msetTime,mdeadLineTime,mpriority,mpriority);
                Intent intent_toMain = new Intent("com.example.daily.myapplication.EDIT_RESULT");
                intent_toMain.putExtra("thisTask",thisTask_return);
                intent_toMain.putExtra("position",position);
                sendBroadcast(intent_toMain);
                finish();
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit,menu);
        return true; //false == hide menu
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_menu_delete:
                //incomplete!
                Intent intent_del = new Intent("com.example.daily.myapplication.EDIT_MENU");
                Bundle bundle_del = new Bundle();
                bundle_del.putString("command","DELETE");
                bundle_del.putInt("DELETE_POSITION",position);
                intent_del.putExtras(bundle_del);
                sendBroadcast(intent_del,null);
                finish();
                break;
            case R.id.edit_menu_done:
                Intent intent_done = new Intent("com.example.daily.myapplication.EDIT_MENU");
                Bundle bundle_done = new Bundle();
                bundle_done.putString("command","DONE");
                bundle_done.putInt("DONE_POSITION",position);
                intent_done.putExtras(bundle_done);
                sendBroadcast(intent_done);
                finish();
                break;
            case R.id.add_new_menu_media:
                //File mediaDerector = new File("/system/media/audio/alarms/");
                //File[] mediaFiles = mediaDerector.listFiles();
                performFileSearch();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private static final int READ_REQUEST_CODE = 42;

    /**
     * Fires an intent to spin up the "file chooser" UI and select an image.
     */
    public void performFileSearch() {

        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Filter to show only images, using the image MIME data type.
        // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
        // To search for all documents available via installed storage providers,
        // it would be "*/*".

        //MAY BE A ERROR!!!
        intent.setType("audio/*");

        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    /**
     * 该方法使得notification播放用户自定义的media文件
     * 注意使用的数据是未被此次edit修改的Task！
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                mediaFile = new File(uri.getPath());
                //未完成！！！
                //get time
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                    Date date = sdf.parse(thisTask.getdeadLineTime());
                    long dateFromEpoch = date.getTime();
                    //set clock
                    Intent intent = new Intent("com.example.daily.myapplication.ACTION_SEND");
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("aTask",thisTask);
                    bundle.putInt("position",position);
                    bundle.putSerializable("mediaFile",mediaFile);
                    intent.putExtras(bundle);
                    PendingIntent sendIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    am.cancel(sendIntent);
                    am.set(AlarmManager.RTC_WAKEUP,dateFromEpoch,sendIntent);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
