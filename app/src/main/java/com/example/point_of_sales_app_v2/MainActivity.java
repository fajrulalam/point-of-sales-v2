package com.example.point_of_sales_app_v2;

import static java.util.Objects.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.point_of_sales_app_v2.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements MakananFragment.OnDataMakananFragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    ListTransactionRecyclerAdapter listTransactionRecyclerAdapter;

    ArrayList<String> namaPesanan;
    ArrayList<Integer> hargaSatuanPesanan;
    ArrayList<Integer> subtotalPesanan;
    ArrayList<Integer> quantityPesanan;

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

        //ArrayLists
        namaPesanan = new ArrayList<>();
        hargaSatuanPesanan = new ArrayList<>();
        subtotalPesanan = new ArrayList<>();
        quantityPesanan = new ArrayList<>();

        binding.total.setText("Rp0");
        listTransactionRecyclerAdapter = new ListTransactionRecyclerAdapter( namaPesanan, quantityPesanan, hargaSatuanPesanan, subtotalPesanan, this);
        binding.ListTransactionRecyclerView.setAdapter(listTransactionRecyclerAdapter);





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

        Log.i("DataMakanan", "Nama: " + namaMakanan + " Harga: Rp" +hargaSatuan + "Subtotal: " + subtotalPesanan.get(index_existOrNot) + " Quant: " + quantityPesanan.get(index_existOrNot));
//        listTransactionRecyclerAdapter = new ListTransactionRecyclerAdapter( namaPesanan, hargaSatuanPesanan, subtotalPesanan, quantityPesanan, this);

        listTransactionRecyclerAdapter.notifyDataSetChanged();



    }


    public void countTotal(ArrayList<Integer> subtotalPesanan) {
        int total = 0;
        for (int i : subtotalPesanan) {
            total += i;
        }

        binding.total.setText("Rp" + String.format("%,d", total).replace(",", "."));

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



}