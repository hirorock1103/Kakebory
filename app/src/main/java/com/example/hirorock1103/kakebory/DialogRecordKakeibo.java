package com.example.hirorock1103.kakebory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class DialogRecordKakeibo extends DialogFragment {

    EditText price;
    EditText title;

    private int itemId;

    private CommunicateListener listener;
    private CommunicateListenerUpdate updateListener;

    private KakaiboManager manager;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_dialog_enter_price, null);

        price = view.findViewById(R.id.price);
        title = view.findViewById(R.id.title);

        price.setInputType(InputType.TYPE_CLASS_NUMBER);

        final int categoryId = getArguments().getInt("categoryId");
        manager = new KakaiboManager(getActivity());
        final Category category = manager.getCategoryById(categoryId);

        //itemId
        try{
            itemId = getArguments().getInt("itemId");
            PurchaseItem item = manager.getPurchaseItemById(itemId);
            title.setText(item.getPurchaseItemTitle());
            price.setText(String.valueOf(item.getPurchaseItemPrice()));
        }catch (Exception e){
            itemId = 0;
        }

        builder
                .setView(view)
                .setTitle("Category:" + category.getCategoryTitle())
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(price.getText().toString().isEmpty() || price.getText().toString() == null){
                            Common.toast(getActivity(),"there is something you forget to enter!");
                        }else{

                            if(itemId > 0){

                                PurchaseItem item = manager.getPurchaseItemById(itemId);
                                item.setPurchaseItemPrice(Integer.parseInt(price.getText().toString()));
                                item.setPurchaseItemTitle(title.getText().toString());
                                item.setCategoryId(categoryId);

                                //add item
                                int id = (int)manager.updatePurchaceItem(item);
                                //this method is called in activity
                                updateListener.getUpdateResult(id);

                            }else{

                                PurchaseItem item = new PurchaseItem();
                                item.setPurchaseItemPrice(Integer.parseInt(price.getText().toString()));
                                item.setPurchaseItemTitle(title.getText().toString());
                                item.setCategoryId(categoryId);

                                //add item
                                int id = manager.addPurchaceItem(item);
                                //this method is called in activity
                                listener.getInsertResult(id);

                            }

                        }

                    }
                });

        return builder.create();


    }

    public interface CommunicateListener{
        void getInsertResult(int insertId);
    }
    public interface CommunicateListenerUpdate{
        void getUpdateResult(int insertId);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            listener = (CommunicateListener)context;
            updateListener = (CommunicateListenerUpdate) context;
        }catch(ClassCastException e){

        }


    }
}
