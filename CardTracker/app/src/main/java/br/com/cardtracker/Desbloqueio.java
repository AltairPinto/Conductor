package br.com.cardtracker;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import br.com.conductor.sdc.api.v1.CartaoApi;
import br.com.conductor.sdc.api.v1.ContaApi;
import br.com.conductor.sdc.api.v1.invoker.ApiException;
import br.com.conductor.sdc.api.v1.model.Cartao;
import br.com.conductor.sdc.api.v1.model.Conta;

public class Desbloqueio extends AppCompatActivity implements View.OnClickListener{

    private Spinner nIDSpinner;

    private TextView Cartoes;

    private EditText nConta;
    private EditText nID;

    //private Button btnDesbloquear;

    // Atributos API
    public runAPI runAPI = new runAPI();
    public ContaApi contaApi = runAPI.getContaApiInfos();//("3BJU7WSdxYVy","VxUGXKTjnPCa","https://api.conductor.com.br/sdc");
    public CartaoApi cartaoApi = runAPI.getCartaoApiInfos();//("3BJU7WSdxYVy","VxUGXKTjnPCa","https://api.conductor.com.br/sdc");
    public Conta conta1 = runAPI.getConta1Infos();
    public Cartao cartao1 = runAPI.getCartao1Infos();
    public Cartao cartao2 = runAPI.getCartao2Infos();

    public String selectID;
    public List<Cartao> getAPIFromText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desbloqueio);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        } //Thread não dar conflito

        Cartoes = (TextView) findViewById(R.id.Cartoes); // Campo para jogar os cartões existentes

        nIDSpinner = (Spinner) findViewById(R.id.nIDSpinner);

        nConta = (EditText) findViewById(R.id.nConta);
        nID = (EditText) findViewById(R.id.nID);

        //btnDesbloquear = (Button) findViewById(R.id.btnDesbloquearCartao);

        nConta.setText(conta1.getNome());
        nID.setText(conta1.getId().toString());

        // Criando o Spinner para IDs
        ArrayAdapter<Long> arrayAdapter = new ArrayAdapter<Long>(this,android.R.layout.simple_spinner_item);
        final ArrayAdapter<Long> spinnerArrayAdapter = arrayAdapter;
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        nIDSpinner.setAdapter(spinnerArrayAdapter);
        spinnerArrayAdapter.add(cartao1.getId());
        spinnerArrayAdapter.add(cartao2.getId());

        nIDSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            // Pegando ID a partir da posição selecionada
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectID = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinnerArrayAdapter.add(null);
            }
        }); //Fim do Spinner

        try {
                getAPIFromText = cartaoApi.getAllUsingGET(conta1.getId()); // Pegar todos os cartões da conta
                Cartoes.setText(getAPIFromText.toString());
        } catch (ApiException e) {
            System.out.println("Deu pau em Desbloqueio "+e);
        }

        //btnDesbloquear.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        final Long getLongFromText = new Long(selectID);
        AlertDialog.Builder dig = new AlertDialog.Builder(Desbloqueio.this);
        System.out.println("Selected ID Desbloqueio: "+selectID);

        try {
            cartaoApi.desbloquearUsingPUT(conta1.getId(),getLongFromText);
            dig.setTitle("Desbloqueado");
            dig.setMessage("\nCartão de ID "+selectID+" desbloqueado com sucesso!");
            dig.setNeutralButton("OK", null);
            dig.show();
        } catch (ApiException e) {
            dig.setTitle("Erro");
            dig.setMessage("\n"+e);
            dig.setNeutralButton("Voltar", null);
            dig.show();
        }
        //Saída
        try {
            getAPIFromText = cartaoApi.getAllUsingGET(conta1.getId()); // Pegar todos os cartões da conta
            Cartoes.setText(getAPIFromText.toString());
        } catch (ApiException e) {
            System.out.println("Deu pau em Cartoes "+e);
        }
    }

    public void onBackPressed(){
        Intent it = new Intent(this, Menu.class);
        startActivity(it);
    }
}
