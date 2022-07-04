package com.example.point_of_sales_app_v2;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.point_of_sales_app_v2.databinding.FragmentHapusOrEditMenuBinding;
import com.example.point_of_sales_app_v2.databinding.FragmentTambahMenuBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;


public class HapusOrEditMenuFragment extends BottomSheetDialogFragment {

    FragmentHapusOrEditMenuBinding binding;
    Bundle bundle;
    ArrayList<String> namaMakanan;
    ArrayList<String> namaMakanan_sorted;
    ArrayList<Integer> gambar;
    ArrayList<Integer> hargaSatuan;

    public HapusOrEditMenuFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHapusOrEditMenuBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        bundle = this.getArguments();

        namaMakanan = bundle.getStringArrayList("namaMakanan");
        namaMakanan_sorted = bundle.getStringArrayList("namaMakanan_sorted");
        gambar = bundle.getIntegerArrayList("gambar");
        hargaSatuan = bundle.getIntegerArrayList("hargaSatuan");





        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        final View view = View.inflate(getContext(), R.layout.fragment_hapus_or_edit_menu, null);
        dialog.setContentView(view);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setPeekHeight(3000);
        bottomSheetBehavior.setMaxHeight(2100);


        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT);


        return dialog;
    }

    public class MakananListRecyclerAdapter extends RecyclerView.Adapter<MakananListRecyclerAdapter.ViewHolder>{

        Context context;
        ArrayList<String> namaMakanan;
        ArrayList<Integer> hargaSatuan;



        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            ImageView image;
            LinearLayout cardView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);


                image = itemView.findViewById(R.id.gambarMenu);
                cardView = itemView.findViewById(R.id.cardView);



            }


        }
    }
}

