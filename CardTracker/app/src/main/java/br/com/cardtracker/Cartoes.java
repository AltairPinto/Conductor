package br.com.cardtracker;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Cartoes extends AppCompatActivity implements View.OnClickListener{

    private EditText nDia;
    private EditText nValor;
    private EditText nStatus;
    private EditText nValidar;

    private Button btnValidar;

    public String validar = "conductor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartoes);

        nDia = (EditText) findViewById(R.id.nDia);
        nValor = (EditText) findViewById(R.id.nValor);
        nStatus = (EditText) findViewById(R.id.nStatus);
        nValidar = (EditText) findViewById(R.id.nValidar);

        btnValidar = (Button) findViewById(R.id.btnValidar);
        btnValidar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final String nIntFromText = nValidar.getText().toString();
        AlertDialog.Builder dig5 = new AlertDialog.Builder(Cartoes.this);
        if(validar.equals(nIntFromText)){
        dig5.setTitle("Confirmação");
        dig5.setMessage("\nCompra confirmada! Limite atual: R$89,90");
        dig5.setNeutralButton("OK", null);
            nValidar.setText(null);
        dig5.show();}
        else {
            dig5.setMessage("Código inválido!");
            dig5.setNegativeButton("Voltar", null);
            nValidar.setText(null);
            dig5.show();
        }
    }
    public void onBackPressed(){
        Intent it = new Intent(this, Menu.class);
        startActivity(it);
    }
}