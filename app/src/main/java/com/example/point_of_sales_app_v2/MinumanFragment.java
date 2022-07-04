package com.example.point_of_sales_app_v2;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class MinumanFragment extends Fragment implements MakananGridRecyclerAdapter.OnDataMakanan{

    ArrayList<String> namaMinuman;
    ArrayList<String> namaMinuman_sorted;
    ArrayList<Integer> gambarMinuman;
    ArrayList<Integer> hargaSatuanMinuman;
    RecyclerView recyclerViewMakanan;
    OnDataMinumanFragment datapasser;



    public MinumanFragment(ArrayList<String> namaMinuman, ArrayList<String> namaMinuman_sorted, ArrayList<Integer> gambarMinuman, ArrayList<Integer> hargaSatuanMinuman) {
        this.namaMinuman = namaMinuman;
        this.namaMinuman_sorted = namaMinuman_sorted;
        this.gambarMinuman = gambarMinuman;
        this.hargaSatuanMinuman = hargaSatuanMinuman;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragment = inflater.inflate(R.layout.fragment_minuman, container, false);

        MakananGridRecyclerAdapter makananGridRecyclerAdapter = new MakananGridRecyclerAdapter(getContext(), namaMinuman, namaMinuman_sorted, gambarMinuman, hargaSatuanMinuman, this);
        recyclerViewMakanan = fragment.findViewById(R.id.MenuMinumanRecyclerView);
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
        datapasser.OnDataMinumanFragment(namaMakanan, hargaSatuan);
    }




    public interface OnDataMinumanFragment {
        void OnDataMinumanFragment(String namaMakanan, int hargaSatuan);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        datapasser = (OnDataMinumanFragment) context;
    }
}