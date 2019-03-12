package com.example.hirorock1103.kakebory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class DialogPastListPicker extends AppCompatDialogFragment {

    private Spinner spinner;

    public DialogPastListListener listListener;

    public interface DialogPastListListener{
        public void DialogPastListNoticeResult(String value);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listListener = (DialogPastListListener)context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.f_pastlist,null);

        //データが存在する年月
        KakaiboManager manager = new KakaiboManager(getContext());

        spinner = view.findViewById(R.id.spinner);
        List<String> spinnerArray = manager.getItemYm();

        spinnerArray.add(0, "-対象年月選択-");

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        spinner.setAdapter(spinnerArrayAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setView(view);
        builder.setTitle(" Select Past Data");
        builder.setNegativeButton("CANCEL", null);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String value = spinner.getSelectedItem().toString();
                listListener.DialogPastListNoticeResult(value);
            }
        });
        Dialog dialog = builder.create();

        return dialog;
    }
}
