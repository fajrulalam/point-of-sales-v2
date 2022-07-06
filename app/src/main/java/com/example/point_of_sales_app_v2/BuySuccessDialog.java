package com.example.point_of_sales_app_v2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class BuySuccessDialog extends AppCompatDialogFragment {


    private TextView nomorPelanggan;
    private TextView nominalKembalian;

    private Button okButton;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_buy_success_dialog, null);

        int kembalian = getArguments().getInt("kembalian");
        int customerNumber_update = getArguments().getInt("customerNumber_update") + 1;
//        Log.i("Kembalianmu... ", "" + kembalian);


        builder.setView(view);


        nomorPelanggan = view.findViewById(R.id.nomorPelanggan);
        nominalKembalian = view.findViewById(R.id.nominalKembalian);
        nomorPelanggan.setText(customerNumber_update +"");
        nominalKembalian.setText("(Rp"+ String.format("%,d", kembalian)+")");


        okButton = view.findViewById(R.id.okButton);


        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BuySuccessDialog.this.dismiss();
            }
        });


        return builder.create();
    }


}