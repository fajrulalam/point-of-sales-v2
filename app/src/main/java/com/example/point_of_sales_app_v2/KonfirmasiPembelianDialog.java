package com.example.point_of_sales_app_v2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class KonfirmasiPembelianDialog extends AppCompatDialogFragment {

    private TextInputEditText editText;
    private TextView totalTextView;
    private DialogBuyListener dialogBuyListener;
    private Button okButton;
    private ImageView cancelButton;
    private RelativeLayout pesanRelativeLayout;
    private EditText diambilKapanJam;
    private EditText diambilKapanMenit;
    Intent intent;
    Integer kembalian;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_konfirmasi_pembelian_dialog, null);

        int total = getArguments().getInt("totalValue");
        int pesan = getArguments().getInt("pesan");
        Log.i("Your total is... ", String.format("%,d", total));


        builder.setView(view);
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                })
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        String uangYangDikembalikan = editText.getText().toString();
//                        try {
//                            int kembalian = (int) (Integer.parseInt(uangYangDikembalikan) - Integer.parseInt(total));
//                            Log.i("Kembalianmu...","Rp" + kembalian);
//                            dialogBuyListener.countChange(kembalian);
//                        } catch (ClassCastException e) {
//                            Log.i("Test","Test");
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                });
        pesanRelativeLayout = (RelativeLayout) view.findViewById(R.id.pesan);
        diambilKapanJam = view.findViewById(R.id.diambilKapanJam);
        diambilKapanMenit = view.findViewById(R.id.diambilKapanMenit);

        editText = view.findViewById(R.id.uangYangDiterima);
        totalTextView = view.findViewById(R.id.nominalTagihan);
        totalTextView.setText("Rp" + String.format("%,d", total));
        okButton = view.findViewById(R.id.okButton);
        cancelButton = view.findViewById(R.id.cancelButton);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        editText.setTransformationMethod(new NumericKeyBoardTransformationMethod());
        editText.addTextChangedListener(new NumberTextWatcherForThousand(editText));
        diambilKapanMenit.setTransformationMethod(new NumericKeyBoardTransformationMethod());
        diambilKapanJam.setTransformationMethod(new NumericKeyBoardTransformationMethod());

        if (pesan == 0) {
            pesanRelativeLayout.setVisibility(View.GONE);
        }

        diambilKapanMenit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(diambilKapanMenit.getText().length()==0) {
                    diambilKapanJam.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        diambilKapanJam.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(diambilKapanJam.getText().length()>2) {
                    diambilKapanMenit.requestFocus();
                }

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(diambilKapanJam.getText().length()>=2) {
                    diambilKapanMenit.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uangYangDikembalikan = editText.getText().toString();
                String waktuPengambilan = diambilKapanJam.getText().toString() + ":" + diambilKapanMenit.getText().toString();
                if (pesan == 0) {
                    diambilKapanMenit.setText("00");
                    diambilKapanJam.setText("00");
                    waktuPengambilan = "Tidak Memesan";
                }
                try {
                    int kembalian = (int) (Integer.parseInt(uangYangDikembalikan) - total);
//                    Log.i("Kembalianmu...","Rp" + kembalian);
                    if (kembalian >= 0 ) {
                        if (!diambilKapanJam.getText().toString().matches("") && !diambilKapanMenit.getText().toString().matches("")) {
                            dialogBuyListener.countChange(kembalian, waktuPengambilan);
                            KonfirmasiPembelianDialog.this.dismiss();
                        } else {
                            Toast.makeText(getContext(), "Tentukan waktu pengambilannya...", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Uangnya kurang...", Toast.LENGTH_SHORT).show();
                    }
                } catch (ClassCastException e) {
                    Toast.makeText(getContext(), "Salah input. Harus angka", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Terjadi kesalahan teknis", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KonfirmasiPembelianDialog.this.dismiss();
            }
        });
        return builder.create();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            dialogBuyListener = (DialogBuyListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement ExampleDialogListener");
        }
    }

    public interface DialogBuyListener{
        void countChange(int result, String waktuPengambilan);

    }

    private class NumericKeyBoardTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return source;
        }
    }
}
