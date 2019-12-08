package com.example.cinemaapplication.ui.home;

import android.animation.ArgbEvaluator;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.example.cinemaapplication.SliderAdapter;
import com.example.cinemaapplication.ViewPagerAdapter;
import com.example.cinemaapplication.Model;
import com.example.cinemaapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {

    ViewPager viewPager;
    ViewPagerAdapter mViewPagerAdapter;
    List<Model> modelsShowing;
    List<Model> modelsComing;
    Integer[] colors = null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    SliderView sliderView;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    int aCount = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_home, container, false);

        modelsShowing = new ArrayList<>();
        modelsComing = new ArrayList<>();

        myRef.child("movie").child("showing").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                modelsShowing.clear();
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    modelsShowing.add(new Model(childSnapshot.child("image").getValue(int.class), childSnapshot.child("title").getValue(String.class), childSnapshot.child("desc").getValue(String.class), childSnapshot.child("genre").getValue(String.class), childSnapshot.child("price").getValue(Double.class), childSnapshot.getRef().getPath().toString()));
                }

                mViewPagerAdapter = new ViewPagerAdapter(modelsShowing, root.getContext());

                viewPager = root.findViewById(R.id.vpToday);
                viewPager.setAdapter(mViewPagerAdapter);
                viewPager.setPadding(30, 0, 30, 0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myRef.child("movie").child("coming").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                modelsComing.clear();
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    modelsComing.add(new Model(childSnapshot.child("image").getValue(int.class), childSnapshot.child("title").getValue(String.class), childSnapshot.child("desc").getValue(String.class), childSnapshot.child("genre").getValue(String.class), childSnapshot.child("price").getValue(Double.class), childSnapshot.getRef().getPath().toString()));
                }

                mViewPagerAdapter = new ViewPagerAdapter(modelsComing, root.getContext());

                mViewPagerAdapter.notifyDataSetChanged();

                viewPager = root.findViewById(R.id.vpComing);
                viewPager.setAdapter(mViewPagerAdapter);
                viewPager.setPadding(30, 0, 30, 0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        sliderView = root.findViewById(R.id.imageSlider);

        final SliderAdapter adapter = new SliderAdapter(root.getContext());

        myRef.child("announcement").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                aCount = (int)dataSnapshot.getChildrenCount();

                adapter.setCount(aCount);
                sliderView.setSliderAdapter(adapter);

                sliderView.setIndicatorAnimation(IndicatorAnimations.COLOR.SLIDE); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
                sliderView.setIndicatorSelectedColor(Color.WHITE);
                sliderView.setIndicatorUnselectedColor(Color.GRAY);
                sliderView.setScrollTimeInSec(5);
                sliderView.startAutoCycle();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return root;
    }
}