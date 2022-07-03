package com.example.point_of_sales_app_v2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.point_of_sales_app_v2.databinding.ActivityMainBinding;
import com.example.point_of_sales_app_v2.databinding.ActivitySettingsBinding;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity implements TambahMenuFragment.OnDataMenuBaru{

    ActivitySettingsBinding binding;
    Bundle bundle;
    boolean wasAnythingChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        bundle = getIntent().getExtras();
        wasAnythingChanged = false;



        binding.mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });


        binding.AddcardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TambahMenuFragment tambahMenuFragment = new TambahMenuFragment();
                bundle.putString("query", "create");
                tambahMenuFragment.setArguments(bundle);
                tambahMenuFragment.show(getSupportFragmentManager(), tambahMenuFragment.getTag());

            }
        });




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), TambahMenuFragment.class);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
        Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnDataMenuBaru(ArrayList<String> namaMakanan, ArrayList<String> namaMakanan_sorted, ArrayList<Integer> gambarMakanan, ArrayList<Integer> hargaSatuan) {
        wasAnythingChanged = true;
        bundle.putStringArrayList("namaMakanan", namaMakanan);
        bundle.putStringArrayList("namaMakanan_sorted", namaMakanan_sorted);
        bundle.putIntegerArrayList("gambarMakanan", gambarMakanan);
        bundle.putIntegerArrayList("hargaSatuan", hargaSatuan);

    }
}