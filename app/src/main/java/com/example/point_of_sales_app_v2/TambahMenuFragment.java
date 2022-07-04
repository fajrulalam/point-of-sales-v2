package com.example.point_of_sales_app_v2;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.ContentInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.point_of_sales_app_v2.databinding.FragmentTambahMenuBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;


public class TambahMenuFragment extends BottomSheetDialogFragment {

    FragmentTambahMenuBinding binding;
    ArrayList<Integer> pilihanGambar;
    String makananOrMinuman;
    int selectedImageBitMap;
    int selectedImagePosition;
    Bundle bundle;

    OnDataMenuBaru datapasser;

    ArrayList<String> namaMakanan;
    ArrayList<String> namaMakanan_sorted;
    ArrayList<Integer> gambarMakanan;
    ArrayList<Integer> hargaSatuan;

    int pcc;

        public TambahMenuFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTambahMenuBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        bundle = this.getArguments();
        namaMakanan = bundle.getStringArrayList("namaMakanan");
        namaMakanan_sorted = bundle.getStringArrayList("namaMakanan_sorted");
        gambarMakanan = bundle.getIntegerArrayList("gambarMakanan");
        hargaSatuan = bundle.getIntegerArrayList("hargaSatuan");




        //Meng-format nama menu
        binding.namaMenuBaru.getEditText().setInputType(InputType.TYPE_CLASS_TEXT);


        //Meng-format harga
        binding.hargaMenuBaru.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        binding.hargaMenuBaru.getEditText().setTransformationMethod(new NumericKeyBoardTransformationMethod());
        binding.hargaMenuBaru.getEditText().addTextChangedListener(new NumberTextWatcherForThousand(binding.hargaMenuBaru.getEditText()));


        //Mengatur Radio Button Makanan atau Minuman
        makananOrMinuman = "";
        binding.radioButtonMakanan.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                binding.radioButtonMakanan.setBackgroundTintList(getResources().getColorStateList(R.color.chosen));
                binding.radioButtonMinuman.setBackgroundTintList(getResources().getColorStateList(R.color.border));
                makananOrMinuman = "Makanan";
            }
        });
        binding.radioButtonMinuman.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                binding.radioButtonMinuman.setBackgroundTintList(getResources().getColorStateList(R.color.chosen));
                binding.radioButtonMakanan.setBackgroundTintList(getResources().getColorStateList(R.color.border));
                makananOrMinuman = "Minuman";
            }
        });


        //If the query is edit
        pcc = -1;
        selectedImagePosition = -1;
        if (bundle.getString("query").matches("edit")) {
            pcc = bundle.getInt("pcc");
            String makananYangDiedit = namaMakanan_sorted.get(pcc);

            int index_unsorted = namaMakanan.indexOf(makananYangDiedit);
            selectedImagePosition = pilihanGambar.indexOf(gambarMakanan.get(index_unsorted));
            binding.tambahkanButton.setText("Selesai");


            binding.namaMenuBaru.getEditText().setText(makananYangDiedit);
            binding.hargaMenuBaru.getEditText().setText(String.format("%,d", hargaSatuan.get(index_unsorted)));

            makananOrMinuman = bundle.getString("makananOrMinuman");
            switch (makananOrMinuman) {
                case "Minuman":
                    binding.radioButtonMinuman.setBackgroundTintList(getResources().getColorStateList(R.color.chosen));
                    binding.radioButtonMakanan.setBackgroundTintList(getResources().getColorStateList(R.color.border));
                    break;
                case "Makanan":
                    binding.radioButtonMakanan.setBackgroundTintList(getResources().getColorStateList(R.color.chosen));
                    binding.radioButtonMinuman.setBackgroundTintList(getResources().getColorStateList(R.color.border));
                    break;
            }


        }

        //Pilihan gambar
        RecyclerAdapterPilihGambar recyclerAdapterPilihGambar = new RecyclerAdapterPilihGambar(getActivity(), pilihanGambar, selectedImagePosition);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4, RecyclerView.VERTICAL, false);
        binding.pilihGambarRecyclerView.setLayoutManager(gridLayoutManager);
        int spanCount = 5; // 3 columns
        int spacing = 0; // 50px
        boolean includeEdge = false;
        binding.pilihGambarRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        binding.pilihGambarRecyclerView.setAdapter(recyclerAdapterPilihGambar);




        //Tambahkan Button
        binding.tambahkanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namaMenuBaru = binding.namaMenuBaru.getEditText().getText().toString();
                String hargaMenuBaru = binding.hargaMenuBaru.getEditText().getText().toString();
                int hargaMenuBaru_int = Integer.parseInt(hargaMenuBaru.replace(",", "") );

                //Validate form completion
                if (selectedImagePosition == -1 || makananOrMinuman.matches("") || namaMenuBaru.matches("") || hargaMenuBaru.matches("")) {
                    Toast.makeText(getActivity(), "Lengkapi data terlebih dahulu", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (bundle.getString("query").matches("create")){
                    namaMakanan.add(namaMenuBaru);
                    namaMakanan_sorted.add(namaMenuBaru);
                    gambarMakanan.add(selectedImageBitMap);
                    hargaSatuan.add(hargaMenuBaru_int);
                    datapasser.OnDataMenuBaru(makananOrMinuman, namaMakanan, namaMakanan_sorted, gambarMakanan, hargaSatuan);
                    Toast.makeText(getActivity(), "Data akan disimpan", Toast.LENGTH_SHORT).show();
                    dismiss();
                } else {

                    String makananYangDiedit = namaMakanan_sorted.get(pcc);
                    int index_unsorted = namaMakanan.indexOf(makananYangDiedit);
                    namaMakanan_sorted.set(pcc, namaMenuBaru);
                    namaMakanan.set(index_unsorted, namaMenuBaru);
                    gambarMakanan.set(index_unsorted, selectedImageBitMap);
                    hargaSatuan.set(index_unsorted, hargaMenuBaru_int);
                    datapasser.OnDataMenuBaru(makananOrMinuman, namaMakanan, namaMakanan_sorted, gambarMakanan, hargaSatuan);
                    Toast.makeText(getActivity(), "Data akan disimpan", Toast.LENGTH_SHORT).show();
                    dismiss();


                }





//                } else {
//                    Log.i("selectedImagePosition", ""+ selectedImagePosition);
//                    Log.i("makananOrMinuman", ""+ makananOrMinuman);
//                    Log.i("namaMenuBaru", ""+ namaMenuBaru);
//                    Log.i("hargaMenuBaru", ""+ hargaMenuBaru);
//                    Toast.makeText(getActivity(), "Lengkapi data terlebih dahulu", Toast.LENGTH_SHORT).show();
//                }
            }
        });

        return view;

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        final View view = View.inflate(getContext(), R.layout.fragment_tambah_menu, null);
        dialog.setContentView(view);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setPeekHeight(3000);
        bottomSheetBehavior.setMaxHeight(2100);


        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        //opsiGambar
        pilihanGambar = new ArrayList<>();
        pilihanGambar.add(R.drawable.bakso_compressed);
        pilihanGambar.add(R.drawable.kentang_compressed);
        pilihanGambar.add(R.drawable.nasbung_a_compressed);
        pilihanGambar.add(R.drawable.nasbung_b_compressed);
        pilihanGambar.add(R.drawable.siomay_compressed);
        pilihanGambar.add(R.drawable.pisang_goreng);
        pilihanGambar.add(R.drawable.popmie_compressed);
        pilihanGambar.add(R.drawable.bakso_compressed);
        pilihanGambar.add(R.drawable.sereal_real);
        pilihanGambar.add(R.drawable.sosis_nugget);
        pilihanGambar.add(R.drawable.mie_ayam);
        pilihanGambar.add(R.drawable.tofu);
        pilihanGambar.add(R.drawable.nasi_pindang);
        pilihanGambar.add(R.drawable.nasi_ayam_compressed);
        pilihanGambar.add(R.drawable.egg_rice);
//        pilihanGambar.add(R.drawable.french_fries);


        Bundle bundle = getArguments();
        String query = bundle.getString("query");

        if (query.matches("update")){
            //get and insert the values....

        }

        return dialog;
    }

    public interface OnDataMenuBaru{
            void OnDataMenuBaru(String makananOrMinuman, ArrayList<String> namaMakanan, ArrayList<String> namaMakanan_sorted, ArrayList<Integer> gambarMakanan, ArrayList<Integer> hargaSatuan);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        datapasser = (TambahMenuFragment.OnDataMenuBaru) context;
    }

    public class RecyclerAdapterPilihGambar extends RecyclerView.Adapter<RecyclerAdapterPilihGambar.ViewHolder>{
        Context context;
        ArrayList<Integer> gambar;
        int mselectedImagePosition;

        public RecyclerAdapterPilihGambar(Context context, ArrayList<Integer> gambar, int mselectedImagePosition) {
            this.context = context;
            this.gambar = gambar;
            this.mselectedImagePosition = mselectedImagePosition;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.pilih_gambar_single_view, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);

            String makananYangDiedit = namaMakanan_sorted.get(pcc);
            int index_unsorted = namaMakanan.indexOf(makananYangDiedit);
            selectedImageBitMap = pilihanGambar.get(index_unsorted);

            return  viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Glide.with(context).load(gambar.get(position)).into(holder.image);

            if (position == mselectedImagePosition) {
                holder.cardView.setBackgroundTintList(getResources().getColorStateList(R.color.chosen));
            }



            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("ItemClickSupport", "It's clicked");
                    selectedImageBitMap = pilihanGambar.get(holder.getAdapterPosition());
                    selectedImagePosition = holder.getAdapterPosition();

                    RecyclerAdapterPilihGambar recyclerAdapterPilihGambar = new RecyclerAdapterPilihGambar(getActivity(), pilihanGambar, selectedImagePosition);
                    binding.pilihGambarRecyclerView.setAdapter(recyclerAdapterPilihGambar);
                }
            });
        }

        @Override
        public int getItemCount() {
            return gambar.size();
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

    private class NumericKeyBoardTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return source;
        }
    }
}