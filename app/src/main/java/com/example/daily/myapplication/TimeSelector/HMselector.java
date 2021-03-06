package com.example.daily.myapplication.TimeSelector;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

public class HMselector extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {
    private String time;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        String formal_hour, formal_minute;
        if (hourOfDay < 10) {
            formal_hour = "0" + hourOfDay;
        } else {
            formal_hour = Integer.toString(hourOfDay);
        }
        if (minute < 10) {
            formal_minute = "0" + minute;
        } else {
            formal_minute = Integer.toString(minute);
        }
        time = " " + formal_hour + ":" + formal_minute;
    }

    public String getTime() {
        return time;
    }
}