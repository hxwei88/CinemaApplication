package com.example.cinemaapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity{

    private CollapsingToolbarLayout ctl;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Intent i = getIntent();

        ctl = findViewById(R.id.toolbar_layout);
        ctl.setBackgroundResource(i.getIntExtra("image", R.drawable.facebook_cover_photo_1));
        ctl.setTitle(i.getStringExtra("title"));

        tv = findViewById(R.id.tv_detail);
        tv.setText("Price: RM" + String.format("%.2f", i.getDoubleExtra("price", 0)) + "\n\n" +
                "Genre: " + i.getStringExtra("genre") + "\n\n" + i.getStringExtra("desc"));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i2 = new Intent(DetailActivity.this, BookingActivity.class);
                i2.putExtra("path", i.getStringExtra("path"));
                i2.putExtra("price", i.getDoubleExtra("price", 0));
                startActivity(i2);
                finish();
            }
        });
    }
}
