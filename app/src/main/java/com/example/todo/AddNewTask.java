package com.example.todo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.todo.utils.DBHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddNewTask extends BottomSheetDialogFragment {
    public static final String TAG = "AddNewTask";

    private EditText taskName, taskDescription;
    private Button startTime, startDate, endTime, endDate, submitBtn;
    private TextView startT, startD, endT, endD;
    private Long EndDateRange = 0L;
    private Long StartDateRange1=0L;
    private int startHour, startMinute;
    private RadioGroup radioGroup;
    private RadioButton HighPriority, MediumPriority, LowPriority;

    private DBHelper myDB;

    public static AddNewTask newInstance() {
        return new AddNewTask();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.new_task, container, false);


        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        taskName = view.findViewById(R.id.TaskName);
        taskDescription = view.findViewById(R.id.TaskDescription);
        startTime = view.findViewById(R.id.StartTimeButton);
        startDate = view.findViewById(R.id.StartDateButton);
        endTime = view.findViewById(R.id.EndTimeButton);
        endDate = view.findViewById(R.id.EndDateButton);
        startT = view.findViewById(R.id.StartTime);
        startD = view.findViewById(R.id.StartDate);
        endT = view.findViewById(R.id.EndTime);
        endD = view.findViewById(R.id.EndDate);


//

        //time and date picker
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Long StartRange = MaterialDatePicker.todayInUtcMilliseconds();
                CalendarConstraints.DateValidator dateValidator = DateValidatorPointForward.from(StartRange);
                CalendarConstraints calendarConstraints = new CalendarConstraints.Builder().setValidator(dateValidator).build();
//                openDatePicker(startD,calendarConstraints);
//                openDatePicker(startD, calendarConstraints);
                MaterialDatePicker<Long> materialDatePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select Date")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .setCalendarConstraints(calendarConstraints)
                        .build();
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {

                        StartDateRange1 = selection;
//                Toast.makeText(getContext(), ""+EndDateRange, Toast.LENGTH_SHORT).show();
                        String date = new SimpleDateFormat("dd-MM-yy", Locale.getDefault()).format(new Date(selection));

                        startD.setText(date);
                    }
                });
                materialDatePicker.show(getActivity().getSupportFragmentManager(), "tag");
            }
        });
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                openDatePicker(endD,calendarConstraints1);
                CalendarConstraints.DateValidator dateValidator1 = DateValidatorPointForward.from(StartDateRange1);
                CalendarConstraints calendarConstraints1 = new CalendarConstraints.Builder().setValidator(dateValidator1).build();
//                Toast.makeText(getContext(), ""+EndDateRange, Toast.LENGTH_SHORT).show();

                MaterialDatePicker<Long> materialDatePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select Date")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .setCalendarConstraints(calendarConstraints1)
                        .build();
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {

                        EndDateRange = selection;
//                Toast.makeText(getContext(), ""+EndDateRange, Toast.LENGTH_SHORT).show();
                        String date = new SimpleDateFormat("dd-MM-yy", Locale.getDefault()).format(new Date(selection));

                        endD.setText(date);
                    }
                });
                materialDatePicker.show(getActivity().getSupportFragmentManager(), "tag");
//                openDatePicker(endD, calendarConstraints1);
            }
        });
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_12H)
                        .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                        .setTitleText("Pick Start time")
                        .build();
                timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startHour = timePicker.getHour();
                        startMinute = timePicker.getMinute();

                        if (isPastTime(startHour, startMinute) && StartDateRange1 == MaterialDatePicker.todayInUtcMilliseconds()){
                            Toast.makeText(getContext(), "Please select a future time", Toast.LENGTH_LONG).show();
                        } else {
                            String AM_PM = "";
                            if (timePicker.getHour() > 12) {
                                AM_PM = "PM";
                            } else {
                                AM_PM = "AM";
                            }
                            startT.setText(MessageFormat.format("{0}:{1} ", String.format(Locale.getDefault(), "%02d", timePicker.getHour()), String.format(Locale.getDefault(), "%02d", timePicker.getMinute())) + AM_PM);

                        }
                    }
                });
                timePicker.show(getActivity().getSupportFragmentManager(), "tag");
            }
        });
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_12H)
                        .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                        .setTitleText("Pick End time")
                        .build();
                timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int endHour = timePicker.getHour();
                        int endMinute = timePicker.getMinute();
                        if(StartDateRange1.equals(EndDateRange)){
                            if (!isEndTimeValid(startHour, startMinute, endHour, endMinute)) {
//                                Toast.makeText(getContext(), "End time must be greater than start time", Toast.LENGTH_LONG).show();
                            } else {
                                String AM_PM = "";
                                if (timePicker.getHour() > 12) {
                                    AM_PM = "PM";
                                } else {
                                    AM_PM = "AM";
                                }
                                endT.setText(MessageFormat.format("{0}:{1} ", String.format(Locale.getDefault(), "%02d", timePicker.getHour()), String.format(Locale.getDefault(), "%02d", timePicker.getMinute())) + AM_PM);

                            }
                        }else {
//                            Toast.makeText(getContext(), ""+StartDateRange1.equals(EndDateRange), Toast.LENGTH_SHORT).show();
                            Log.d("ENDDATERANGE", "onClick: "+ StartDateRange1+ " ------------------------ "+EndDateRange);

                            String AM_PM = "";
                            if (timePicker.getHour() > 12) {
                                AM_PM = "PM";
                            } else {
                                AM_PM = "AM";
                            }
                            endT.setText(MessageFormat.format("{0}:{1} ", String.format(Locale.getDefault(), "%02d", timePicker.getHour()), String.format(Locale.getDefault(), "%02d", timePicker.getMinute())) + AM_PM);

                        }


                    }
                });
                timePicker.show(getActivity().getSupportFragmentManager(), "tag");
            }
        });
        RadioButton HighPriority = view.findViewById(R.id.HighPriority);
        RadioButton MediumPriority = view.findViewById(R.id.MediumPriority);
        RadioButton LowPriority = view.findViewById(R.id.LowPriority);
        final String[] Priority = {"3"};

        //click listeners for radio buttons
        HighPriority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HighPriority.setChecked(true);
                Priority[0] = "1";
//                Toast.makeText(getContext(), "" + Priority[0], Toast.LENGTH_SHORT).show();
            }
        });
        MediumPriority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediumPriority.setChecked(true);
                Priority[0] = "2";
//                Toast.makeText(getContext(), "" + Priority[0], Toast.LENGTH_SHORT).show();
            }
        });
        LowPriority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LowPriority.setChecked(true);
                Priority[0] = "3";
//                Toast.makeText(getContext(), "" + Priority[0], Toast.LENGTH_SHORT).show();
            }
        });


        submitBtn = view.findViewById(R.id.button);

        myDB = new DBHelper(getActivity());

        boolean isUpdate = false;

        Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;
            String task = bundle.getString("task");
            String description = bundle.getString("description");
            String starttime = bundle.getString("starttime");
            String startdate = bundle.getString("startdate");
            String endtime = bundle.getString("endtime");
            String enddate = bundle.getString("enddate");
            String priority = bundle.getString("priority");

            taskName.setText(task);
            taskDescription.setText(description);
            startT.setText(starttime);
            startD.setText(startdate);
            endT.setText(endtime);
            endD.setText(enddate);

            if (taskName.length() > 0 && taskDescription.length() > 0 && startTime != null && startDate != null && endTime != null && endDate != null & Priority[0] != null) {
                submitBtn.setEnabled(true);
            }
        }
        taskName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("") && taskDescription.length() == 0 && startT == null && startD == null && endT == null && endD == null && Priority[0] == null) {
                    submitBtn.setEnabled(false);
                } else {
                    submitBtn.setEnabled(true);
                    submitBtn.setBackgroundColor(getResources().getColor(com.google.android.material.R.color.design_default_color_primary));
                }
//                if(endD == null){
//                    submitBtn.setEnabled(false);
//                }else if (!endD.getText().equals("")){
//                    submitBtn.setEnabled(true);
//                    submitBtn.setBackgroundColor(getResources().getColor(com.google.android.material.R.color.design_default_color_primary));
//                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        boolean finalIsUpdate = isUpdate;
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (taskName.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Please enter Task Name", Toast.LENGTH_SHORT).show();
                } else if (startT.getText().toString().equals("") || startD.getText().toString().equals("") || endT.getText().toString().equals("") || endD.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Please enter Time and date", Toast.LENGTH_SHORT).show();
                } else {
                    //                 String s = taskName.getText().toString();
                    String task = taskName.getText().toString();
                    String Description = taskDescription.getText().toString();
                    String StartTime = startT.getText().toString();
                    String StartDate = startD.getText().toString();
                    String EndTime = endT.getText().toString();
                    String EndDate = endD.getText().toString();
                    String finalPriority = Priority[0];
//                    Toast.makeText(getContext(), ""+StartTime +" "+StartDate, Toast.LENGTH_SHORT).show();

                    if (finalIsUpdate) {
                        myDB.updateTask(bundle.getInt("id"), task, Description, StartTime, StartDate, EndTime, EndDate, finalPriority);
                    } else {
                        DBToDoModel item = new DBToDoModel();
                        item.setTask(task);
                        item.setDescription(Description);
                        item.setStartTime(StartTime);
                        item.setStartDate(StartDate);
                        item.setEndTime(EndTime);
                        item.setEndDate(EndDate);
                        item.setPriority(finalPriority);
                        myDB.addTask(item);
                    }
                    dismiss();
                }
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof OnDailogCloseListner) {
            ((OnDailogCloseListner) activity).onDailogClose(dialog);
        }
    }

    private void openDatePicker(TextView start, CalendarConstraints calendarConstraints) {
//        CalendarConstraints.DateValidator dateValidator = DateValidatorPointForward.now();
//        CalendarConstraints calendarConstraints = new CalendarConstraints.Builder().setValidator(dateValidator).build();


        MaterialDatePicker<Long> materialDatePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(calendarConstraints)
                .build();
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {

                EndDateRange = selection;
//                Toast.makeText(getContext(), ""+EndDateRange, Toast.LENGTH_SHORT).show();
                String date = new SimpleDateFormat("dd-MM-yy", Locale.getDefault()).format(new Date(selection));

                start.setText(date);
            }
        });
        materialDatePicker.show(getActivity().getSupportFragmentManager(), "tag");
    }



    private boolean isPastTime(int hour, int minute) {
        Calendar currentTime = Calendar.getInstance();
        int currentHour = currentTime.get(Calendar.HOUR_OF_DAY);
        int currentMinute = currentTime.get(Calendar.MINUTE);

        if (hour < currentHour || (hour == currentHour && minute < currentMinute)) {
            return true;
        }
        return false;
    }

    private boolean isEndTimeValid(int startHour, int startMinute, int endHour, int endMinute) {
        int startTimeInMinutes = startHour * 60 + startMinute;
        int endTimeInMinutes = endHour * 60 + endMinute;

        return endTimeInMinutes > startTimeInMinutes;
    }
}
