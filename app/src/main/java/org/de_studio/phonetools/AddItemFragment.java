package org.de_studio.phonetools;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

/**
 * Created by hai on 10/25/2015.
 */
public class AddItemFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {
    private String mType;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_item,null);
        builder.setView(dialogView)
                .setPositiveButton(R.string.add_item_fragment_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(R.string.add_item_fragment_negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        Spinner spinner = (Spinner) dialogView.findViewById(R.id.new_item_type_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity()
                ,R.array.new_item_type_spinner_array
                ,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        return builder.create();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        LinearLayout linearLayout = (LinearLayout) parent.getParent();
        EditText editText =(EditText) linearLayout.findViewById(R.id.new_item_text);
        switch (position){

            case 1:{

                editText.setVisibility(View.VISIBLE);
                mType = "sms";
                break;
            }
            case 0:{
                editText.setVisibility(View.GONE);
                mType = "call";
                break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        mType = "call";
    }

}
