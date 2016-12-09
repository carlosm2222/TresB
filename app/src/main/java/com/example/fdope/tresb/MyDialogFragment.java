package com.example.fdope.tresb;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by fdope on 04-10-2016.
 */

public class MyDialogFragment extends DialogFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog, container, false);
        getDialog().setTitle("¡Gracias!");
        Button dismiss = (Button) rootView.findViewById(R.id.aceptar);
        dismiss.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
               getActivity().finish();
            }
        });
        return rootView;

    }
}
