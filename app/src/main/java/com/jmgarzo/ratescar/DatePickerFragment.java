package com.jmgarzo.ratescar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

/**
 * Created by jmgarzo on 16/12/14.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private Context context;
    private EditText editText;

    public DatePickerFragment() {
        super();
    }

    @SuppressLint("ValidFragment")
    public DatePickerFragment(Context context, EditText editText) {
        super();
        this.context = context;
        this.editText = editText;

    }

    @Override

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if(null!= editText && null != Integer.valueOf(year) && null != Integer.valueOf(monthOfYear) && null!= Integer.valueOf(dayOfMonth)) {
            editText.setText(Integer.valueOf(dayOfMonth).toString().concat("/").concat(Integer.valueOf(monthOfYear + 1).toString().concat("/").concat(Integer.valueOf(year).toString())));
        }
    }


}
