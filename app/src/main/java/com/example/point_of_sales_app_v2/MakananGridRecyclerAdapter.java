package com.example.point_of_sales_app_v2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MakananGridRecyclerAdapter extends RecyclerView.Adapter<MakananGridRecyclerAdapter.ViewHolder>{
    Context context;
    ArrayList<String> namaMakanan_unsorted;
    ArrayList<String> namaMakanan_sorted;
    ArrayList<Integer> image;
    ArrayList<Integer> harga;
    OnDataMakanan onDataMakanan;

    public interface OnDataMakanan{
        void OnDataMakanan(String namaMakanan, int hargaSatuan);
    }

    public MakananGridRecyclerAdapter(Context context, ArrayList<String> namaMakanan_unsorted, ArrayList<String> namaMakanan_sorted, ArrayList<Integer> image, ArrayList<Integer> harga, OnDataMakanan onDataMakanan) {
        this.context = context;
        this.namaMakanan_unsorted = namaMakanan_unsorted;
        this.namaMakanan_sorted = namaMakanan_sorted;
        this.image = image;
        this.harga = harga;
        this.onDataMakanan = onDataMakanan;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.makanan_single_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, onDataMakanan);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        int position_unsorted = namaMakanan_unsorted.indexOf(namaMakanan_sorted.get(position));

//        holder.gambarMakanan.setImageResource(image.get(position_unsorted));
        Glide.with(context).load(image.get(position_unsorted)).into(holder.gambarMakanan);
        holder.namaMakanan.setText(namaMakanan_unsorted.get(position_unsorted));




        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pcc = holder.getAdapterPosition();
                int pcc_unsorted = namaMakanan_unsorted.indexOf(namaMakanan_sorted.get(pcc));
                Log.i("pcc", pcc +"");
                Log.i("pcc_unsorted", pcc_unsorted +"");
                onDataMakanan.OnDataMakanan(namaMakanan_unsorted.get(pcc_unsorted), harga.get(pcc_unsorted));
            }
        });

    }

    @Override
    public int getItemCount() {
        return namaMakanan_unsorted.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView namaMakanan;
        ImageView gambarMakanan;
        CardView cardView;

        public ViewHolder(@NonNull View itemView, OnDataMakanan onDataMakanan) {
            super(itemView);

            namaMakanan = itemView.findViewById(R.id.namaMenu);
            gambarMakanan = itemView.findViewById(R.id.gambarMenu);
            cardView = itemView.findViewById(R.id.cardView);


        }


    }








}





