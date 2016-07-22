package br.com.cardtracker;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import br.com.conductor.sdc.api.v1.model.Conta;

public class Menu extends AppCompatActivity implements View.OnClickListener {

    // Data of user
    //private EditText editTextAgencia;
    private EditText nConta;
    private EditText nID;

    // Buttons
    private ImageButton btnLocal;
    private ImageButton btnConta;
    private ImageButton btnCompra;
    private ImageButton btnCartoes;
    private ImageButton btnDesbloqueio;
    private ImageButton btnTransferencia;


    // Acesso API
    public runAPI runAPI = new runAPI();
    // Atributos API
    //public ContaApi contaApi = runAPI.getContaApiInfos("3BJU7WSdxYVy","VxUGXKTjnPCa","https://api.conductor.com.br/sdc");
    public Conta conta1 = runAPI.getConta1Infos();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        } //Thread não dar conflito

        nConta = (EditText) findViewById(R.id.nConta);
        nID = (EditText) findViewById(R.id.nID);

        btnLocal = (ImageButton) findViewById(R.id.btnLocal);
        btnConta = (ImageButton) findViewById(R.id.btnConta);
        btnCompra = (ImageButton) findViewById(R.id.btnCompra);
        btnCartoes = (ImageButton) findViewById(R.id.btnCartoes);
        btnDesbloqueio = (ImageButton) findViewById(R.id.btnDesbloqueio);
        btnTransferencia = (ImageButton) findViewById(R.id.btnTransferencia);

        System.out.println("Conta Nome no Menu: "+conta1.getNome());
        System.out.println("Conta ID no Menu: "+conta1.getId());

        nConta.setText(conta1.getNome());
        nID.setText(conta1.getId().toString());

        btnLocal.setOnClickListener(this);

        btnConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dig1 = new AlertDialog.Builder(Menu.this);
                Intent it1 = new Intent(Menu.this, Contas.class);
                dig1.setMessage("Consultando Contas...");
                dig1.show();
                startActivity(it1);

            }
        }); // Fim de Contas

        btnCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dig2 = new AlertDialog.Builder(Menu.this);
                Intent it2 = new Intent(Menu.this, Compra.class);
                dig2.setMessage("Criando Compra...");
                dig2.show();
                startActivity(it2);
            }
        }); // Fim de Compra

       btnCartoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dig3 = new AlertDialog.Builder(Menu.this);
                Intent it3 = new Intent(Menu.this, Cartoes.class);
                dig3.setMessage("Consultando Cartões...");
                dig3.show();
                startActivity(it3);
            }
        }); // Fim de Cartões

        btnTransferencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dig = new AlertDialog.Builder(Menu.this);
                Intent ite = new Intent(Menu.this, Transferencia.class);
                dig.setMessage("Carregando Transferência...");
                dig.show();
                startActivity(ite);
            }
        }); // Fim de Transferência

        btnDesbloqueio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dig = new AlertDialog.Builder(Menu.this);
                Intent its = new Intent(Menu.this, Desbloqueio.class);
                dig.setMessage("Carregando Desbloqueio...");
                dig.show();
                startActivity(its);
            }
        }); // Fim de Desbloqueio
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
