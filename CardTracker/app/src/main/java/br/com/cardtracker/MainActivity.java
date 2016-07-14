package br.com.cardtracker;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText password;
    private Button btnEntrar;
    private EditText Agencia;
    private EditText Conta;
    public int senha = 40028922;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        password = (EditText) findViewById(R.id.password);
        btnEntrar = (Button) findViewById(R.id.btnEntrar);
        Agencia = (EditText) findViewById(R.id.Agencia);
        Conta = (EditText) findViewById(R.id.Conta);

        btnEntrar.setOnClickListener(this);
    }

        //btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int nIntFromText = new Integer(password.getText().toString()).intValue();

                AlertDialog.Builder dig = new AlertDialog.Builder(MainActivity.this);
                System.out.println("SENHA: "+senha);
                System.out.println("PASSWORD: "+password.getText());
                if (senha == nIntFromText){
                    Intent it = new Intent(this, Menu.class);
                    it.putExtra("Agencia ",Agencia.getText().toString());
                    it.putExtra("Conta ",Conta.getText().toString());
                    dig.setMessage("Entrando...");
                    dig.show();
                    startActivity(it);
                }else {
                    dig.setMessage("Dados Incorretos");
                    dig.setNegativeButton("Voltar", null);
                    dig.show();
                }
            }
       // });


    }
//}
