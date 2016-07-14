package br.com.cardtracker;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Confirm extends AppCompatActivity implements View.OnClickListener{

    private EditText nDia;
    private EditText nValor;
    private EditText nStatus;

    private Button btnValidar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        nDia = (EditText) findViewById(R.id.nDia);
        nValor = (EditText) findViewById(R.id.nValor);
        nStatus = (EditText) findViewById(R.id.nStatus);

        btnValidar = (Button) findViewById(R.id.btnValidar);
        btnValidar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder dig = new AlertDialog.Builder(Confirm.this);
        Intent it = new Intent(this, Menu.class);
        dig.setMessage("Compra confirmada! Limite atual: R$89,90");
        dig.show();
        startActivity(it);
    }
}