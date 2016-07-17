package br.com.cardtracker;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

        editTextAgencia = (EditText) findViewById(R.id.nAgencia);
        editTextConta = (EditText) findViewById(R.id.nConta);
        Cartao = (EditText) findViewById(R.id.nNumero);

        btnLocais = (Button) findViewById(R.id.btnLocais);
        btnInfos = (Button) findViewById(R.id.btnInfos);
        btnCompra = (Button) findViewById(R.id.btnCompra);
        btnConfirma = (Button) findViewById(R.id.btnConfirma);

        btnLocais.setOnClickListener(this);

        btnInfos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dig1 = new AlertDialog.Builder(Menu.this);
                Intent it1 = new Intent(Menu.this, Info.class);
                //iti.putExtra("Agencia ",editTextAgencia.getText().toString());
                //iti.putExtra("Conta ",editTextConta.getText().toString());
                dig1.setMessage("Consultando Informações...");
                dig1.show();
                startActivity(it1);

            }
        }); // End of Infos

        btnCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dig2 = new AlertDialog.Builder(Menu.this);
                Intent it2 = new Intent(Menu.this, Compra.class);
                dig2.setMessage("Criando Compra...");
                dig2.show();
                startActivity(it2);
            }
        }); // End of Compra

       btnConfirma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dig3 = new AlertDialog.Builder(Menu.this);
                Intent it3 = new Intent(Menu.this, Confirm.class);
                dig3.setMessage("Confirmação de Compra...");
                dig3.show();
                startActivity(it3);
            }
        }); // End of Confirma
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder dig = new AlertDialog.Builder(Menu.this);
        Intent it = new Intent(this, Local.class);
        dig.setMessage("Localizando Compras...");
        dig.show();
        startActivity(it);
    }
    public void onBackPressed(){
        AlertDialog.Builder dig = new AlertDialog.Builder(Menu.this);
        dig.setMessage("Logout...");
        dig.show();
        Intent it = new Intent(this, MainActivity.class);
        startActivity(it);
    }

}
