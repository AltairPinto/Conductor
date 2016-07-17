package br.com.cardtracker;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import br.com.conductor.sdc.api.v1.model.Cartao;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText password;
    private Button btnEntrar;
    private EditText nAgencia;
    public EditText nConta;
    private RadioButton radioButton;

    public int senha = 40028922;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        password = (EditText) findViewById(R.id.password);
        btnEntrar = (Button) findViewById(R.id.btnEntrar);
        nAgencia = (EditText) findViewById(R.id.nAgencia);
        nConta = (EditText) findViewById(R.id.nConta);
        radioButton = (RadioButton) findViewById(R.id.radioButton);

        btnEntrar.setOnClickListener(this);
    }
            @Override
            public void onClick (View v) {
                final int nIntFromText = new Integer(password.getText().toString()).intValue();
                Cartao cartao = new Cartao();
                nConta.setText(cartao.getNome());
                AlertDialog.Builder dig = new AlertDialog.Builder(MainActivity.this);
                System.out.println("SENHA: "+senha);
                System.out.println("PASSWORD: "+password.getText());
                System.out.println("nConta: "+this.nConta.getText());
                if (senha == nIntFromText && radioButton.isChecked()){
                    Intent it = new Intent(this, Menu.class);
                    it.putExtra("Agencia ",nAgencia.getText().toString());
                    it.putExtra("Conta ",nConta.getText().toString());
                    dig.setMessage("Entrando...");
                    dig.show();
                    startActivity(it);
                }else {
                    dig.setMessage("Por favor, preencha os campos corretamente");
                    dig.setNegativeButton("Voltar", null);
                    dig.show();
                    password.setText(null);
                }
            }

    /*MainActivity(String nConta, String nAgencia){ // Construtor
        this.nConta.setText(nConta);
        System.out.println("nConta: "+this.nConta.getText());
        this.nAgencia.setText(nAgencia);
        System.out.println("nAgencia: "+this.nAgencia.getText());

    }*/

    public void onBackPressed(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);    }

    }