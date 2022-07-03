package com.example.point_of_sales_app_v2;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContentInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.point_of_sales_app_v2.databinding.FragmentTambahMenuBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;


public class TambahMenuFragment extends BottomSheetDialogFragment {

    FragmentTambahMenuBinding binding;
    ArrayList<Integer> pilihanGambar;

        public TambahMenuFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTambahMenuBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        RecyclerAdapterPilihGambar recyclerAdapterPilihGambar = new RecyclerAdapterPilihGambar(getActivity(), pilihanGambar);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4, RecyclerView.VERTICAL, false);
        binding.pilihGambarRecyclerView.setLayoutManager(gridLayoutManager);
        int spanCount = 5; // 3 columns
        int spacing = 0; // 50px
        boolean includeEdge = false;
        binding.pilihGambarRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        binding.pilihGambarRecyclerView.setAdapter(recyclerAdapterPilihGambar);




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

    public class RecyclerAdapterPilihGambar extends RecyclerView.Adapter<RecyclerAdapterPilihGambar.ViewHolder>{
        Context context;
        ArrayList<Integer> gambar;

        public RecyclerAdapterPilihGambar(Context context, ArrayList<Integer> gambar) {
            this.context = context;
            this.gambar = gambar;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.pilih_gambar_single_view, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return  viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Glide.with(context).load(gambar.get(position)).into(holder.image);
        }

        @Override
        public int getItemCount() {
            return gambar.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            ImageView image;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);


                image = itemView.findViewById(R.id.gambarMenu);



            }


        }
    }
}