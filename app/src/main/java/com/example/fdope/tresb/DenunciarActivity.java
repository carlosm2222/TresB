package com.example.fdope.tresb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.fdope.tresb.DB.ConsultasProductos;

import static com.example.fdope.tresb.R.id.motivo1;
import static com.example.fdope.tresb.R.id.motivo4;

public class DenunciarActivity extends AppCompatActivity {
    private Button denunciar;
    private  EditText inputMotivo;
    private Button b;
    private int idEvento;
    private RadioGroup radio;
    private RadioButton radioBoton;
    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // check Fields For Empty Values
            checkFieldsForEmptyValues();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_denunciar);
        inputMotivo = (EditText) findViewById(R.id.motivo);
        inputMotivo.setEnabled(false);
        b = (Button) findViewById(R.id.Denunciar);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getIntent().getExtras();
                idEvento = bundle.getInt("idEvento");
                String motivo = (String) radioBoton.getText();
                ConsultasProductos.agregarDenuncia(idEvento,motivo);
            }
        });

        checkFieldsForEmptyValues();
        addListenerOnButton();

    }

    public void addListenerOnButton() {

        radio = (RadioGroup) findViewById(R.id.radio);
        denunciar = (Button) findViewById(R.id.Denunciar);

        denunciar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // get selected radio button from radioGroup
                int selectedId = radio.getCheckedRadioButtonId();
                if (selectedId==motivo4){
                    inputMotivo.setEnabled(true);
                    checkFieldsForEmptyValues();
                } else
                    b.setEnabled(true);
                // find the radiobutton by returned id
                radioBoton = (RadioButton) findViewById(selectedId);


            }

        });

    }

    public void checkFieldsForEmptyValues() {

        if (inputMotivo.equals("")) {
            b.setEnabled(false);
        } else {
            b.setEnabled(true);
        }
    }
}
