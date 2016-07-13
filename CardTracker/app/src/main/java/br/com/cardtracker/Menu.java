package br.com.cardtracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class Menu extends AppCompatActivity implements View.OnClickListener {

    // Data of user
    private EditText editTextAgencia;
    private EditText editTextConta;
    private EditText Cartao;

    // Buttons
    private Button btnLocais;
    private Button btnInfos;
    private Button btnCompra;
    private Button btnConfirma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        editTextAgencia = (EditText) findViewById(R.id.Agencia);
        editTextConta = (EditText) findViewById(R.id.Conta);
        Cartao = (EditText) findViewById(R.id.Cartao);

        btnLocais = (Button) findViewById(R.id.btnLocais);
        btnInfos = (Button) findViewById(R.id.btnInfos);
        btnCompra = (Button) findViewById(R.id.btnCompra);
        btnConfirma = (Button) findViewById(R.id.btnConfirma);

        btnLocais.setOnClickListener(this);
        btnInfos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }); // End of Infos
        btnCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }); // End of Compra
        btnConfirma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }); // End of Confirma
    }

    @Override
    public void onClick(View v) {

    }

}
