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
    ArrayList<HashMap<String, ArrayList<Integer>>> makananArrayList;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment  = inflater.inflate(R.layout.fragment_makanan, container, false);


        //Isi Menu makanan
        ArrayList<String> namaMakanan = new ArrayList<>();
        ArrayList<String> namaMakanan_sorted= new ArrayList<>();
        ArrayList<Integer> gambarMakanan = new ArrayList<>();
        ArrayList<Integer> hargaSatuan = new ArrayList<>();
        namaMakanan.add("Bakso");
        namaMakanan.add("Siomay");
        namaMakanan.add("Tahu");
        namaMakanan.add("Kentang Goreng");
        namaMakanan.add("Asparagus");
        namaMakanan.add("Pop Mie");
        namaMakanan_sorted.add("Bakso");
        namaMakanan_sorted.add("Siomay");
        namaMakanan_sorted.add("Tahu");
        namaMakanan_sorted.add("Kentang Goreng");
        namaMakanan_sorted.add("Asparagus");
        namaMakanan_sorted.add("Pop Mie");
        gambarMakanan.add(R.drawable.bakso_compressed);
        gambarMakanan.add(R.drawable.siomay_compressed);
        gambarMakanan.add(R.drawable.tofu);
        gambarMakanan.add(R.drawable.french_fries);
        gambarMakanan.add(R.drawable.mie_ayam);
        gambarMakanan.add(R.drawable.popmie_compressed);
        hargaSatuan.add(7000);
        hargaSatuan.add(7000);
        hargaSatuan.add(5000);
        hargaSatuan.add(5000);
        hargaSatuan.add(7000);
        hargaSatuan.add(11000);



        namaMakanan_sorted.sort(String::compareToIgnoreCase);

        Log.i("Ordered", namaMakanan.toString());
        Log.i("Unordered", namaMakanan_sorted.toString());




        MakananGridRecyclerAdapter makananGridRecyclerAdapter = new MakananGridRecyclerAdapter(namaMakanan, namaMakanan_sorted, gambarMakanan, hargaSatuan, this);
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