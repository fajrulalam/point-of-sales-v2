package com.example.point_of_sales_app_v2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.TextView;

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
    MakananListRecyclerAdapter makananListRecyclerAdapter;

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
        gambar = bundle.getIntegerArrayList("gambarMakanan");
        hargaSatuan = bundle.getIntegerArrayList("hargaSatuan");

        makananListRecyclerAdapter = new MakananListRecyclerAdapter(getContext(), namaMakanan_sorted, hargaSatuan);
        binding.makananRecyclerView.setAdapter(makananListRecyclerAdapter);





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
        ArrayList<String> namaMakanan_sorted_rv;
        ArrayList<Integer> hargaSatuan_rv;

        public MakananListRecyclerAdapter(Context context, ArrayList<String> namaMakanan_rv, ArrayList<Integer> hargaSatuan_rv) {
            this.context = context;
            this.namaMakanan_sorted_rv = namaMakanan_rv;
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
            holder.harga.setText("Rp"+String.format("%,d", hargaSatuan_rv.get(position)));

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                                    gambar.remove(index);

                                    makananListRecyclerAdapter.notifyDataSetChanged();


                                }
                            }).show();
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

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                nama = itemView.findViewById(R.id.nama);
                harga = itemView.findViewById(R.id.harga);
                linearLayout = itemView.findViewById(R.id.linearLayout);



            }


        }
    }
}

