package com.example.point_of_sales_app_v2;

import static java.util.Objects.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;

import com.example.point_of_sales_app_v2.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements MakananFragment.OnDataMakananFragment{

    TabLayout tabLayout;
    ViewPager viewPager;

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //Pager 
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPagerAdapter.addFragment(new MakananFragment(), "Makanan");
        viewPagerAdapter.addFragment(new MinumanFragment(), "Minuman");
        binding.viewPager.setAdapter(viewPagerAdapter);
        




    }


    @Override
    public void OnDataMakananFragment(String namaMakanan, int hargaSatuan) {
        Log.i("DataMakanan", "Nama: " + namaMakanan + " Harga: Rp" +hargaSatuan);

    }
}