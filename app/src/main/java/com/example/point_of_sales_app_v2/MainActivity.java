package com.example.point_of_sales_app_v2;

import static java.util.Objects.*;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.point_of_sales_app_v2.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements MakananFragment.OnDataMakananFragment, MinumanFragment.OnDataMinumanFragment, KonfirmasiPembelianDialog.DialogBuyListener {


    FirebaseFirestore fs;

    TabLayout tabLayout;
    ViewPager viewPager;
    ListTransactionRecyclerAdapter listTransactionRecyclerAdapter;

    ArrayList<String> namaPesanan;
    ArrayList<Integer> hargaSatuanPesanan;
    ArrayList<Integer> subtotalPesanan;
    ArrayList<Integer> quantityPesanan;


    ArrayList<String> namaMakanan = new ArrayList<>();
    ArrayList<String> namaMakanan_sorted= new ArrayList<>();
    ArrayList<Integer> gambarMakanan = new ArrayList<>();
    ArrayList<Integer> hargaSatuan = new ArrayList<>();

    ArrayList<String> namaMinuman;
    ArrayList<String> namaMinuman_sorted;
    ArrayList<Integer> gambarMinuman;
    ArrayList<Integer> hargaSatuanMinuman;

    int customerID;

    SharedPreferences sharedPreferencesMenu;

    ActivityMainBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        fs = FirebaseFirestore.getInstance();


//        //Isi Menu makanan
        namaMakanan = new ArrayList<>();
        namaMakanan_sorted= new ArrayList<>();
        gambarMakanan = new ArrayList<>();
        hargaSatuan = new ArrayList<>();

        namaMinuman = new ArrayList<>();
        namaMinuman_sorted= new ArrayList<>();
        gambarMinuman = new ArrayList<>();
        hargaSatuanMinuman = new ArrayList<>();

        namaMinuman.add("Kopi Hitam");
        namaMinuman.add("Teh Panas");
        namaMinuman.add("Es Teh");
        namaMinuman.add("Es Milo");
        namaMinuman.add("Es Buah");
        namaMinuman.add("Es Dawet");
        namaMinuman_sorted.add("Kopi Hitam");
        namaMinuman_sorted.add("Teh Panas");
        namaMinuman_sorted.add("Es Teh");
        namaMinuman_sorted.add("Es Milo");
        namaMinuman_sorted.add("Es Buah");
        namaMinuman_sorted.add("Es Dawet");
        gambarMinuman.add(R.drawable.coffee);
        gambarMinuman.add(R.drawable.hot_tea);
        gambarMinuman.add(R.drawable.ice_tea);
        gambarMinuman.add(R.drawable.milo1);
        gambarMinuman.add(R.drawable.es_buah);
        gambarMinuman.add(R.drawable.cendol);
        hargaSatuanMinuman.add(5000);
        hargaSatuanMinuman.add(5000);
        hargaSatuanMinuman.add(3000);
        hargaSatuanMinuman.add(5000);
        hargaSatuanMinuman.add(5000);
        hargaSatuanMinuman.add(5000);


        namaMakanan_sorted.sort(String::compareToIgnoreCase);
        namaMakanan_sorted.sort(String::compareToIgnoreCase);






        //Get Value from the Saved SharedPreferences
        sharedPreferencesMenu = getApplicationContext().getSharedPreferences("Menu", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesMenu.edit();
        customerID = sharedPreferencesMenu.getInt("customerID", 0);
        int customerID_next = customerID+1;
        binding.nomorPelangganBerikutnya.setText("Nomor Berikutnya: " + customerID_next);



        try {
            namaMakanan = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferencesMenu.getString("namaMakanan",  ObjectSerializer.serialize(new ArrayList<String>())));
            namaMakanan_sorted = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferencesMenu.getString("namaMakanan_sorted",  ObjectSerializer.serialize(new ArrayList<String>())));
            gambarMakanan = (ArrayList<Integer>) ObjectSerializer.deserialize(sharedPreferencesMenu.getString("gambarMakanan",  ObjectSerializer.serialize(new ArrayList<String>())));
            hargaSatuan = (ArrayList<Integer>) ObjectSerializer.deserialize(sharedPreferencesMenu.getString("hargaSatuan",  ObjectSerializer.serialize(new ArrayList<String>())));


            namaMinuman = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferencesMenu.getString("namaMinuman",  ObjectSerializer.serialize(new ArrayList<String>())));
            namaMinuman_sorted = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferencesMenu.getString("namaMinuman_sorted",  ObjectSerializer.serialize(new ArrayList<String>())));
            gambarMinuman = (ArrayList<Integer>) ObjectSerializer.deserialize(sharedPreferencesMenu.getString("gambarMinuman",  ObjectSerializer.serialize(new ArrayList<String>())));
            hargaSatuanMinuman = (ArrayList<Integer>) ObjectSerializer.deserialize(sharedPreferencesMenu.getString("hargaSatuanMinuman",  ObjectSerializer.serialize(new ArrayList<String>())));

            namaMakanan_sorted.sort(String::compareToIgnoreCase);
            namaMinuman_sorted.sort(String::compareToIgnoreCase);

        } catch (IOException e) {
            e.printStackTrace();
        }


        //Update the ArrayLists if an intent is received
        Bundle extras = getIntent().getBundleExtra("bundle");
        if (extras != null) {
            namaMakanan = extras.getStringArrayList("namaMakanan");
            namaMakanan_sorted = extras.getStringArrayList("namaMakanan_sorted");
            gambarMakanan = extras.getIntegerArrayList("gambarMakanan");
            hargaSatuan = extras.getIntegerArrayList("hargaSatuan");

            namaMinuman = extras.getStringArrayList("namaMinuman");
            namaMinuman_sorted = extras.getStringArrayList("namaMinuman_sorted");
            gambarMinuman = extras.getIntegerArrayList("gambarMinuman");
            hargaSatuanMinuman = extras.getIntegerArrayList("hargaSatuanMinuman");

            namaMinuman_sorted.sort(String::compareToIgnoreCase);


            try {
                String namaMakanan_serialized = ObjectSerializer.serialize(namaMakanan);
                String namaMakanan_sorted_serialized = ObjectSerializer.serialize(namaMakanan_sorted);
                String gambarMakanan_serialized = ObjectSerializer.serialize(gambarMakanan);
                String hargaSatuan_serialized = ObjectSerializer.serialize(hargaSatuan);



                editor.putString("namaMakanan", namaMakanan_serialized).apply();
                editor.putString("namaMakanan_sorted", namaMakanan_sorted_serialized).apply();
                editor.putString("gambarMakanan", gambarMakanan_serialized).apply();
                editor.putString("hargaSatuan", hargaSatuan_serialized).apply();


                String namaMinuman_serialized = ObjectSerializer.serialize(namaMinuman);
                String namaMinuman_sorted_serialized = ObjectSerializer.serialize(namaMinuman);
                String gambarMinuman_serialized = ObjectSerializer.serialize(gambarMinuman);
                String hargaSatuanMinuman_serialized = ObjectSerializer.serialize(hargaSatuanMinuman);

                editor.putString("namaMinuman", namaMinuman_serialized).apply();
                editor.putString("namaMinuman_sorted", namaMinuman_sorted_serialized).apply();
                editor.putString("gambarMinuman", gambarMinuman_serialized).apply();
                editor.putString("hargaSatuanMinuman", hargaSatuanMinuman_serialized).apply();

            } catch (IOException e) {
                e.printStackTrace();
            }


            Log.i("namaMakanan", namaMakanan.toString());
            Log.i("namaMakanan_sorted", namaMakanan_sorted.toString());
            Log.i("gambarMakanan", gambarMakanan.toString());
            Log.i("hargaSatuan", hargaSatuan.toString());
        }






        binding.settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("namaMinuman", namaMinuman);
                bundle.putStringArrayList("namaMinuman_sorted", namaMinuman_sorted);
                bundle.putIntegerArrayList("gambarMinuman", gambarMinuman);
                bundle.putIntegerArrayList("hargaSatuanMinuman", hargaSatuanMinuman);

                bundle.putStringArrayList("namaMakanan", namaMakanan);
                bundle.putStringArrayList("namaMakanan_sorted", namaMakanan_sorted);
                bundle.putIntegerArrayList("gambarMakanan", gambarMakanan);
                bundle.putIntegerArrayList("hargaSatuan", hargaSatuan);

                intent.putExtra("bundle", bundle);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });


        //Pager
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPagerAdapter.addFragment(new MakananFragment(namaMakanan, namaMakanan_sorted, gambarMakanan, hargaSatuan), "Makanan");
        viewPagerAdapter.addFragment(new MinumanFragment(namaMinuman, namaMinuman_sorted, gambarMinuman, hargaSatuanMinuman), "Minuman");
        binding.viewPager.setAdapter(viewPagerAdapter);

        //ArrayLists
        namaPesanan = new ArrayList<>();
        hargaSatuanPesanan = new ArrayList<>();
        subtotalPesanan = new ArrayList<>();
        quantityPesanan = new ArrayList<>();

        binding.total.setText("Rp0");
        listTransactionRecyclerAdapter = new ListTransactionRecyclerAdapter( namaPesanan, quantityPesanan, hargaSatuanPesanan, subtotalPesanan, this);
        binding.ListTransactionRecyclerView.setAdapter(listTransactionRecyclerAdapter);


        //Beli Button
        binding.buttonBeli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (namaPesanan.size()==0) {
                    Toast.makeText(getApplicationContext(), "Tidak ada pesanan", Toast.LENGTH_SHORT).show();
                    return;
                }
                Bundle bundle = new Bundle();
                if (binding.pesanCheckbox.isChecked()){
                    bundle.putInt("pesan", 1);

                } else {
                    bundle.putInt("pesan", 0);
                }
                bundle.putInt("totalValue", countTotal(subtotalPesanan));
                KonfirmasiPembelianDialog buyDialog = new KonfirmasiPembelianDialog();
                buyDialog.setArguments(bundle);
                buyDialog.show(getSupportFragmentManager(), "test");

            }
        });


        binding.restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferencesMenu.edit().putInt("customerID", 1).commit();
                customerID = sharedPreferencesMenu.getInt("customerID", 0);
                Log.i("customerID", customerID+"");
                int customerID_next = customerID;
                binding.nomorPelangganBerikutnya.setText("Nomor Berikutnya: " + customerID_next);
                Toast.makeText(getApplicationContext(), "Nomor pelanggan berhasil di-restart", Toast.LENGTH_SHORT).show();
            }
        });





    }



    ///What happens when a food item menu is clicked
    @Override
    public void OnDataMakananFragment(String namaMakanan, int hargaSatuan) {
        Log.i("DataMakanan", "Nama: " + namaMakanan + " Harga: Rp" +hargaSatuan);

        int index_existOrNot = namaPesanan.indexOf(namaMakanan);


        if (index_existOrNot == -1){
            namaPesanan.add(namaMakanan);
            hargaSatuanPesanan.add(hargaSatuan);
            subtotalPesanan.add(hargaSatuan);
            quantityPesanan.add(1);
            listTransactionRecyclerAdapter.notifyDataSetChanged();
            countTotal(subtotalPesanan);
            return;
        }


        if (index_existOrNot != -1) {
            //namaPesanan sudah ada
            //hargasatuantidakberubah
            subtotalPesanan.set(index_existOrNot, subtotalPesanan.get(index_existOrNot) + hargaSatuan);
            quantityPesanan.set(index_existOrNot, quantityPesanan.get(index_existOrNot) + 1);
            listTransactionRecyclerAdapter.notifyDataSetChanged();
            countTotal(subtotalPesanan);

        }

//        Log.i("DataMakanan", "Nama: " + namaMakanan + " Harga: Rp" +hargaSatuan + "Subtotal: " + subtotalPesanan.get(index_existOrNot) + " Quant: " + quantityPesanan.get(index_existOrNot));
//        Log.i("Nama Pesanan", "Length : " + namaPesanan.size()+ " " + namaPesanan.toString());
//        Log.i("subtotal", "Length : " + subtotalPesanan.size()+ " " + subtotalPesanan.toString());
//        Log.i("harga satuan", "Length : " + hargaSatuanPesanan.size()+ " " + hargaSatuanPesanan.toString());
//        Log.i("Quantity", "Length : " + quantityPesanan.size()+ " " + quantityPesanan.toString());
//        listTransactionRecyclerAdapter = new ListTransactionRecyclerAdapter( namaPesanan, hargaSatuanPesanan, subtotalPesanan, quantityPesanan, this);

        listTransactionRecyclerAdapter.notifyDataSetChanged();

    }



    ///What happens when a beverage item menu is clicked
    @Override
    public void OnDataMinumanFragment(String namaMakanan, int hargaSatuan) {
        Log.i("DataMakanan", "Nama: " + namaMakanan + " Harga: Rp" +hargaSatuan);

        int index_existOrNot = namaPesanan.indexOf(namaMakanan);


        if (index_existOrNot == -1){
            namaPesanan.add(namaMakanan);
            hargaSatuanPesanan.add(hargaSatuan);
            subtotalPesanan.add(hargaSatuan);
            quantityPesanan.add(1);
            listTransactionRecyclerAdapter.notifyDataSetChanged();
            countTotal(subtotalPesanan);
            return;
        }


        if (index_existOrNot != -1) {
            //namaPesanan sudah ada
            //hargasatuantidakberubah
            subtotalPesanan.set(index_existOrNot, subtotalPesanan.get(index_existOrNot) + hargaSatuan);
            quantityPesanan.set(index_existOrNot, quantityPesanan.get(index_existOrNot) + 1);
            listTransactionRecyclerAdapter.notifyDataSetChanged();
            countTotal(subtotalPesanan);

        }


        listTransactionRecyclerAdapter.notifyDataSetChanged();
    }




    public int countTotal(ArrayList<Integer> subtotalPesanan) {
        int total = 0;
        for (int i : subtotalPesanan) {
            total += i;
        }

        binding.total.setText("Rp" + String.format("%,d", total).replace(",", "."));

        return total;
    }

    @Override
    public void countChange(int result, String waktuPengambilan) {


        int bungkus = 0;
        if(binding.bungkusCheckbox.isChecked()) {
            bungkus = 1;
        } else if (binding.pesanCheckbox.isChecked()) {
            bungkus = 2;
        }

        ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Mohon tunggu sebentar...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();


        HashMap<String, Object> map = new HashMap<>();
        for (String str : namaPesanan) {
            map.put(str, FieldValue.increment(quantityPesanan.get(namaPesanan.indexOf(str))));
        }
            map.put("total", FieldValue.increment(countTotal(subtotalPesanan)));
            map.put("year", getYear());
            map.put("month", getMonth());
            map.put("date", getDate());
            map.put("customerNumber", FieldValue.increment(1));
            map.put("timestamp", FieldValue.serverTimestamp());


        WriteBatch batch = fs.batch();

        DocumentReference test_v2 = fs.collection("DailyTransaction").document(getDate());
        batch.set(test_v2, map, SetOptions.merge());

        DocumentReference test_v2_monthly = fs.collection("MonthlyTransaction").document(getMonth());
        batch.set(test_v2_monthly, map, SetOptions.merge());

        DocumentReference test_v2_yearly = fs.collection("YearlyTransaction").document(getYear());
        batch.set(test_v2_yearly, map, SetOptions.merge());

        String namaPesanan_serialized = "";
        String quantityPesanan_serialized = "";
        for (String t : namaPesanan){
            namaPesanan_serialized += t +", ";
            quantityPesanan_serialized += quantityPesanan.get(namaPesanan.indexOf(t))+", ";
        }
        namaPesanan_serialized = namaPesanan_serialized.substring(0, namaPesanan_serialized.length()-2);
        quantityPesanan_serialized = quantityPesanan_serialized.substring(0, quantityPesanan_serialized.length()-2);

        HashMap<String, Object> map_status = new HashMap<>();
        map_status.put("customerNumber", customerID);
        map_status.put("itemID", namaPesanan_serialized);
        map_status.put("quantity", quantityPesanan_serialized);
        map_status.put("status", "Serving");
        map_status.put("bungkus", bungkus);
        map_status.put("total", countTotal(subtotalPesanan));
        map_status.put("waktuPengambilan", waktuPengambilan);

        DocumentReference status = fs.collection("Status").document(""+customerID);
        batch.set(status, map_status, SetOptions.merge());



        batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                progress.dismiss();

                Bundle bundle = new Bundle();
                bundle.putInt("kembalian", result);
                bundle.putInt("customerNumber_update", customerID-1);
                BuySuccessDialog buySuccessDialog = new BuySuccessDialog();
                buySuccessDialog.setArguments(bundle);
                buySuccessDialog.show(getSupportFragmentManager(), "test");

                namaPesanan.clear();
                hargaSatuanPesanan.clear();
                subtotalPesanan.clear();
                quantityPesanan.clear();
                listTransactionRecyclerAdapter.notifyDataSetChanged();

                sharedPreferencesMenu.edit().putInt("customerID", customerID+1).commit();
                customerID = sharedPreferencesMenu.getInt("customerID", 0);
                binding.nomorPelangganBerikutnya.setText("Nomor Berikutnya: " + customerID);

                Log.i("customerID", customerID+"");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });





    }

    class ListTransactionRecyclerAdapter extends RecyclerView.Adapter<ListTransactionRecyclerAdapter.ViewHolder>{
        ArrayList<String> namaMakanan;
        ArrayList<Integer> quantity;
        ArrayList<Integer> hargaSatuan;
        ArrayList<Integer> subtotal;
        Context context;
//        OnListTransaction OnListTransaction;

//        public interface OnListTransaction{
//            void OnListTransaction(String namaMakanan, int hargaSatuan);
//        }


        public ListTransactionRecyclerAdapter(ArrayList<String> namaMakanan, ArrayList<Integer> quantity, ArrayList<Integer> hargaSatuan, ArrayList<Integer> subtotal, Context context) {
            this.namaMakanan = namaMakanan;
            this.quantity = quantity;
            this.hargaSatuan = hargaSatuan;
            this.subtotal = subtotal;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.list_transaction, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return  viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.itemName.setText(namaMakanan.get(position));
            holder.quantity.setText("" + quantity.get(position));
            holder.subtotal.setText("Rp" + String.format("%,d", subtotal.get(position)).replace(",", "."));



            holder.plusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pcc = holder.getAdapterPosition();
                    quantity.set(pcc, quantity.get(pcc)+1);
                    subtotal.set(pcc, subtotal.get(pcc)+hargaSatuan.get(pcc));
                    countTotal(subtotalPesanan);
                    listTransactionRecyclerAdapter.notifyDataSetChanged();
                }
            });

            holder.minusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pcc = holder.getAdapterPosition();
                    quantity.set(pcc, quantity.get(pcc)-1);
                    subtotal.set(pcc, subtotal.get(pcc)-hargaSatuan.get(pcc));
                    countTotal(subtotalPesanan);


                    if (quantity.get(pcc) == 0) {
                        namaMakanan.remove(pcc);
                        quantity.remove(pcc);
                        hargaSatuan.remove(pcc);
                        subtotal.remove(pcc);
                    }

                    listTransactionRecyclerAdapter.notifyDataSetChanged();


                }
            });



        }

        @Override
        public int getItemCount() {
            return namaMakanan.size();
        }




        class ViewHolder extends RecyclerView.ViewHolder{
            TextView itemName;
            TextView subtotal;
            TextView minusButton;
            TextView plusButton;
            Button quantity;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                itemName = itemView.findViewById(R.id.itemName);
                subtotal = itemView.findViewById(R.id.subtotalTextView);
                minusButton = itemView.findViewById(R.id.minusButton);
                plusButton = itemView.findViewById(R.id.plusButton);
                quantity = itemView.findViewById(R.id.quantity);


            }


        }








    }

    public String getDate() {
        Long datetime = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(datetime);
        String date_full = (String) String.valueOf(timestamp);
        String date = date_full.substring(0, 10);
        return date;
    }

    public String getMonth() {
        Long datetime = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(datetime);
        String date_full = (String) String.valueOf(timestamp);
        String month = date_full.substring(0, 7);
        return month;
    }

    public String getYear() {
        Long datetime = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(datetime);
        String date_full = (String) String.valueOf(timestamp);
        String year = date_full.substring(0, 4);
        return year;
    }




}