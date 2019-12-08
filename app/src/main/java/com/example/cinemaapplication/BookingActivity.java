package com.example.cinemaapplication;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BookingActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

//    private ScaleGestureDetector mScaleGestureDetector;
//    private float mScaleFactor = 1.0f;
//    private TableLayout cinema;

    private float mScale = 1f;
    private ScaleGestureDetector mScaleDetector;
    GestureDetector gestureDetector;

    private TableLayout tl;
    private List<List<Boolean>> seat;
    private Button btnDate;
    private Spinner spTime;
    private List<Calendar> mCalendar;
    List<String> spinnerArray;
    private Calendar mCalenderInsert[];
    private int count = 0;
    private String date;
    private String selected;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();

        myRef = myRef.child(i.getStringExtra("path"));
        mCalendar = new ArrayList<>();
        spinnerArray = new ArrayList<>();
        seat = new ArrayList<>();
        spTime = findViewById(R.id.sp_time);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (date == null || count == 0)
                    Toast.makeText(BookingActivity.this, "Please select your date, time and seat!", Toast.LENGTH_SHORT).show();
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(BookingActivity.this);
                    builder.setTitle("Payment Confirmation");
                    builder.setMessage("Total Amount: RM" + String.format("%.2f", count * i.getDoubleExtra("price", 0)));
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            myRef.child("date").child(date).child(selected).child("seat").setValue(seat);
                            Toast.makeText(getApplicationContext(), "You have booked your tickets.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    builder.show();
                }
            }
        });

        tl = (TableLayout) findViewById(R.id.cinema);
        btnDate = findViewById(R.id.btn_date);

        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this);

        myRef.child("date").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    Calendar temp = Calendar.getInstance();
                    String dateTemp = childSnapshot.getKey();

                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    try {
                        Date d = sdf.parse(dateTemp);
                        temp.setTime(d);
                        mCalendar.add(temp);
                    } catch (ParseException ex) {
                        Log.v("Exception", ex.getLocalizedMessage());
                    }
                }

                mCalenderInsert = new Calendar[mCalendar.size()];
                mCalenderInsert = mCalendar.toArray(mCalenderInsert);

                datePickerDialog.setSelectableDays(mCalenderInsert);

                btnDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        datePickerDialog.show(getSupportFragmentManager(), "Datepickerdialog");
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        spTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected = spTime.getSelectedItem().toString();

                myRef.child("date").child(date).child(selected).child("seat").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        seat = (List<List<Boolean>>) dataSnapshot.getValue();

                        ImageButton temp;
                        TableRow tr;

                        tl.getChildAt(1).setVisibility(View.VISIBLE);
                        tl.getChildAt(7).setVisibility(View.VISIBLE);

                        for (int i = 0; i < seat.size(); i++) {
                            tl.getChildAt(i + 2).setVisibility(View.VISIBLE);
                            for (int j = 0; j < seat.get(i).size(); j++) {
                                if (seat.get(i).get(j) == true) {
                                    tr = (TableRow) tl.getChildAt(i + 2);
                                    temp = (ImageButton) tr.getChildAt(j + 1);
                                    temp.setImageResource(R.drawable.seat_red);
                                } else if (seat.get(i).get(j) == false) {
                                    tr = (TableRow) tl.getChildAt(i + 2);
                                    temp = (ImageButton) tr.getChildAt(j + 1);
                                    temp.setImageResource(R.drawable.seat_green);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public int tableRowChildCount(ViewGroup parent) {
        int count = 0;
        for (int x = 0; x < parent.getChildCount() - 1; x++) {
            if (parent.getChildAt(x) instanceof TableRow) {
                count++;
            }
        }

        return count;
    }

    public void seat_onClick(View view) {
        ImageButton btn = (ImageButton) findViewById(view.getId());
        Drawable drawable = btn.getDrawable();
        TableRow tr = (TableRow) view.getParent();
        int column = Integer.parseInt(btn.getTag().toString()) - 1;
        int row = Integer.parseInt(tr.getTag().toString()) - 1;

        if (drawable.getConstantState().equals(getResources().getDrawable(R.drawable.seat_green).getConstantState())){
            btn.setImageResource(R.drawable.seat_blue);
            seat.get(row).set(column, true);
            count++;
        }
        else if (drawable.getConstantState().equals(getResources().getDrawable(R.drawable.seat_blue).getConstantState())){
            btn.setImageResource(R.drawable.seat_green);
            seat.get(row).set(column, false);
            count--;
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        date = String.format("%02d",dayOfMonth) + "-" + String.format("%02d",monthOfYear + 1) + "-" + String.format("%04d",year);
        btnDate.setText(date);

        spinnerArray.clear();

        myRef.child("date").child(date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot childSnapshot : dataSnapshot.getChildren())
                {
                    spinnerArray.add(childSnapshot.getKey());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        BookingActivity.this, android.R.layout.simple_spinner_item, spinnerArray);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spTime.setAdapter(adapter);
                spTime.setClickable(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
