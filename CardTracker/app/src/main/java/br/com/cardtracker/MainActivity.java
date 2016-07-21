package br.com.cardtracker;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import br.com.conductor.sdc.api.v1.model.Conta;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText password;
    private Button btnEntrar;
    private EditText nConta;
    public int senha = 40028922;

    // Acesso API
    public String access_token = "3BJU7WSdxYVy";
    public String client_id = "VxUGXKTjnPCa";
    private static final String BASE_PATH = "https://api.conductor.com.br/sdc";

    // Atributos API
    public runAPI runAPI = new runAPI();
    public Conta conta1 = runAPI.getConta1Infos();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        password = (EditText) findViewById(R.id.password);
        btnEntrar = (Button) findViewById(R.id.btnEntrar);
        nConta = (EditText) findViewById(R.id.nConta);

        btnEntrar.setOnClickListener(this);

        nConta.setText(conta1.getNome());
    }

    @Override
    public void onClick(View v){

        AlertDialog.Builder dig = new AlertDialog.Builder(MainActivity.this);
        System.out.println("SENHA: " + senha);
        System.out.println("PASSWORD: " + password.getText());
        System.out.println("nConta: " + this.nConta.getText());
        System.out.println("Conta ID: "+conta1.getId());
        System.out.println("Conta 1: "+conta1);
        System.out.println("this.conta1: "+this.conta1);
        try {
            final int nIntFromText = new Integer(password.getText().toString()).intValue();
            if (senha == nIntFromText) {
                Intent it = new Intent(this, Menu.class);
                dig.setMessage("Entrando...");
                dig.show();
                startActivity(it);
            } else {
                dig.setMessage("Por favor, preencha os campos corretamente");
                dig.setNegativeButton("Voltar", null);
                dig.show();
                password.setText(null);
            }
        }catch (NumberFormatException ene){
            dig.setMessage("Digite a senha");
            dig.setNegativeButton("Ok",null);
            dig.show();
        }
    }

    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}

