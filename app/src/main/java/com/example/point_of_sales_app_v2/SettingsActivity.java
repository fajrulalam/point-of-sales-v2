package com.example.point_of_sales_app_v2;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.point_of_sales_app_v2.databinding.ActivityMainBinding;
import com.example.point_of_sales_app_v2.databinding.ActivitySettingsBinding;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity implements TambahMenuFragment.OnDataMenuBaru{

    ActivitySettingsBinding binding;
    Bundle bundle;
    boolean wasAnythingChanged;
    ArrayList<String> namaMakanan;
    ArrayList<String> namaMakanan_sorted;
    ArrayList<Integer> gambarMakanan;
    ArrayList<Integer> hargaSatuan;
    MakananListRecyclerAdapter makananListRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        bundle = getIntent().getBundleExtra("bundle");
        namaMakanan = bundle.getStringArrayList("namaMakanan");
        namaMakanan_sorted = bundle.getStringArrayList("namaMakanan_sorted");
        gambarMakanan = bundle.getIntegerArrayList("gambarMakanan");
        hargaSatuan = bundle.getIntegerArrayList("hargaSatuan");

        wasAnythingChanged = false;



        binding.mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

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

        binding.DeletecardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HapusOrEditMenuFragment hapusOrEditMenuFragment = new HapusOrEditMenuFragment();
                bundle.putString("query", "create");
                hapusOrEditMenuFragment.setArguments(bundle);
                hapusOrEditMenuFragment.show(getSupportFragmentManager(),hapusOrEditMenuFragment.getTag());
            }
        });

        makananListRecyclerAdapter = new MakananListRecyclerAdapter(this, namaMakanan_sorted, namaMakanan, hargaSatuan);
        binding.makananRecyclerView.setAdapter(makananListRecyclerAdapter);






    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("bundle", bundle);
        Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void OnDataMenuBaru(String makananOrMinuman, ArrayList<String> namaMakanan, ArrayList<String> namaMakanan_sorted_, ArrayList<Integer> gambarMakanan, ArrayList<Integer> hargaSatuan) {
        wasAnythingChanged = true;
        bundle.putStringArrayList("namaMakanan", namaMakanan);
        bundle.putStringArrayList("namaMakanan_sorted", namaMakanan_sorted_);
        bundle.putIntegerArrayList("gambarMakanan", gambarMakanan);
        bundle.putIntegerArrayList("hargaSatuan", hargaSatuan);
        namaMakanan_sorted.sort(String::compareToIgnoreCase);
        makananListRecyclerAdapter.notifyDataSetChanged();
    }


    public class MakananListRecyclerAdapter extends RecyclerView.Adapter<MakananListRecyclerAdapter.ViewHolder>{

        Context context;
        ArrayList<String> namaMakanan_sorted_rv;
        ArrayList<String> namaMakanan_rv;
        ArrayList<Integer> hargaSatuan_rv;

        public MakananListRecyclerAdapter(Context context, ArrayList<String> namaMakanan_sorted_rv, ArrayList<String> namaMakanan_rv, ArrayList<Integer> hargaSatuan_rv) {
            this.context = context;
            this.namaMakanan_sorted_rv = namaMakanan_sorted_rv;
            this.namaMakanan_rv = namaMakanan_rv;
            this.hargaSatuan_rv = hargaSatuan_rv;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.simple_list_single_view, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return  viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.nama.setText(namaMakanan_sorted_rv.get(position));
            int index = namaMakanan.indexOf(namaMakanan_sorted_rv.get(position));
            holder.harga.setText("Rp"+String.format("%,d", hargaSatuan_rv.get(index)));

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Yakin hapus menu ini?")
                            .setMessage("\n")
                            .setPositiveButton("Tidak", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setNegativeButton("Ya", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String namaMenu = namaMakanan_sorted_rv.get(holder.getAdapterPosition());
                                    namaMakanan_sorted.remove(holder.getAdapterPosition());
                                    int index =  namaMakanan.indexOf(namaMenu);

                                    namaMakanan.remove(index);
                                    hargaSatuan.remove(index);
                                    gambarMakanan.remove(index);

                                    makananListRecyclerAdapter.notifyDataSetChanged();


                                }
                            }).show();
                }
            });

            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TambahMenuFragment tambahMenuFragment = new TambahMenuFragment();
                    bundle.putString("query", "edit");
                    bundle.putInt("pcc", holder.getAdapterPosition());
                    bundle.putString("makananOrMinuman", "Makanan");
                    tambahMenuFragment.setArguments(bundle);
                    tambahMenuFragment.show(getSupportFragmentManager(), tambahMenuFragment.getTag());

                }
            });

        }

        @Override
        public int getItemCount() {
            return namaMakanan_sorted_rv.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            TextView nama;
            TextView harga;
            LinearLayout linearLayout;
            ImageView delete;
            ImageView edit;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                nama = itemView.findViewById(R.id.nama);
                harga = itemView.findViewById(R.id.harga);
                linearLayout = itemView.findViewById(R.id.linearLayout);
                delete = itemView.findViewById(R.id.delete);
                edit = itemView.findViewById(R.id.edit);



            }


        }
    }
}