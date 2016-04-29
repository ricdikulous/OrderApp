package com.dikulous.ric.orderapp.menu.item;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.dikulous.ric.orderapp.R;

import java.util.ArrayList;

/**
 * Created by ric on 27/04/16.
 */
public class IngredientsExcludedDialog extends DialogFragment {

    private static final String INGREDIENTS = "ingredients";
    private static final String SELECTED = "selected";

    IngredientsExcludedDialogListener mListener;

    public static IngredientsExcludedDialog newInstance(String[] ingredients, boolean[] selected){
        IngredientsExcludedDialog dialog = new IngredientsExcludedDialog();
        Bundle bundle = new Bundle();
        bundle.putStringArray(INGREDIENTS, ingredients);
        bundle.putBooleanArray(SELECTED, selected);
        dialog.setArguments(bundle);
        return dialog;
    }

    private static final String TAG = "Ingredients Dialog";
    ArrayList<Integer> mSelectedItems;
    boolean[] selected;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mSelectedItems = new ArrayList();  // Where we track the selected items

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title
        String[] ingredients = getArguments().getStringArray(INGREDIENTS);
        selected = getArguments().getBooleanArray(SELECTED);
        for(int i=0;i<selected.length;i++){
            if(selected[i]){
                mSelectedItems.add(i);
            }
        }
        builder.setTitle("Optional Ingredients")
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(ingredients, selected,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    mSelectedItems.add(which);
                                } else if (mSelectedItems.contains(which)) {
                                    // Else, if the item is already in the array, remove it
                                    mSelectedItems.remove(Integer.valueOf(which));
                                }
                            }
                        })
                        // Set the action buttons
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //Log.i(TAG, "lisener: " + mListener);

                        mListener.onIngredientsDialogFinish(mSelectedItems);
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        Log.i(TAG, "on attach");
        if (context instanceof IngredientsExcludedDialogListener) {
                mListener = (IngredientsExcludedDialogListener) context;
            } else {
                throw new RuntimeException(context.toString()
                                + " must implement OnFragmentInteractionListener");
            }
       }

    public interface IngredientsExcludedDialogListener{
        void onIngredientsDialogFinish(ArrayList<Integer> selectedItems);
    }
}
