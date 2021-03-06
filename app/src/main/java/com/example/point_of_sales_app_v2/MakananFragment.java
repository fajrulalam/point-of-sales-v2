package com.example.point_of_sales_app_v2;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;


public class MakananFragment extends Fragment implements MakananGridRecyclerAdapter.OnDataMakanan{

    OnDataMakananFragment datapasser;
    RecyclerView recyclerViewMakanan;
    ArrayList<String> namaMakanan;
    ArrayList<String> namaMakanan_sorted;
    ArrayList<Integer> gambarMakanan;
    ArrayList<Integer> hargaSatuan;

    public MakananFragment(ArrayList<String> namaMakanan, ArrayList<String> namaMakanan_sorted, ArrayList<Integer> gambarMakanan, ArrayList<Integer> hargaSatuan) {
        this.namaMakanan = namaMakanan;
        this.namaMakanan_sorted = namaMakanan_sorted;
        this.gambarMakanan = gambarMakanan;
        this.hargaSatuan = hargaSatuan;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment  = inflater.inflate(R.layout.fragment_makanan, container, false);


        //Isi Menu makanan
//        ArrayList<String> namaMakanan = new ArrayList<>();
//        ArrayList<String> namaMakanan_sorted= new ArrayList<>();
//        ArrayList<Integer> gambarMakanan = new ArrayList<>();
//        ArrayList<Integer> hargaSatuan = new ArrayList<>();


//        Log.i("Ordered", namaMakanan.toString());
//        Log.i("Unordered", namaMakanan_sorted.toString());


        MakananGridRecyclerAdapter makananGridRecyclerAdapter = new MakananGridRecyclerAdapter(getContext(), namaMakanan, namaMakanan_sorted, gambarMakanan, hargaSatuan, this);
        recyclerViewMakanan = fragment.findViewById(R.id.MenuMakananRecyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3, RecyclerView.VERTICAL, false);
        recyclerViewMakanan.setLayoutManager(gridLayoutManager);
        int spanCount = 3; // 3 columns
        int spacing = 50; // 50px
        boolean includeEdge = false;
        recyclerViewMakanan.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        recyclerViewMakanan.setAdapter(makananGridRecyclerAdapter);



        return fragment;

    }

    @Override
    public void OnDataMakanan(String namaMakanan, int hargaSatuan) {

        //menerima input dari makananGridRecyclerAdapter, kemudian meneruskan ke Main Activity
        datapasser.OnDataMakananFragment(namaMakanan, hargaSatuan);


    }


    public interface OnDataMakananFragment {
        void OnDataMakananFragment(String namaMakanan, int hargaSatuan);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        datapasser = (OnDataMakananFragment) context;
    }
}