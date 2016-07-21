package br.com.cardtracker;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import br.com.conductor.sdc.api.v1.CartaoApi;
import br.com.conductor.sdc.api.v1.ContaApi;
import br.com.conductor.sdc.api.v1.invoker.ApiException;
import br.com.conductor.sdc.api.v1.model.Cartao;
import br.com.conductor.sdc.api.v1.model.Conta;

public class Cartoes extends AppCompatActivity implements View.OnClickListener{

    private EditText nID;
    private EditText nConta;
    private EditText nIDNovo;
    private TextView Cards;
    private Button btnAlterarCartao;
    private Button btnCancelarCartao;

    // Atributos API
    public runAPI runAPI = new runAPI();
    public ContaApi contaApi = runAPI.getContaApiInfos("3BJU7WSdxYVy","VxUGXKTjnPCa","https://api.conductor.com.br/sdc");
    public CartaoApi cartaoApi = runAPI.getCartaoApiInfos("3BJU7WSdxYVy","VxUGXKTjnPCa","https://api.conductor.com.br/sdc");
    public Conta conta1 = runAPI.getConta1Infos();
    public Cartao cartao1 = runAPI.getCartao1Infos();
    public Cartao cartao2 = runAPI.getCartao2Infos();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartoes);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        } //Thread não dar conflito

        nConta = (EditText) findViewById(R.id.nConta);
        nID = (EditText) findViewById(R.id.nID);
        nIDNovo = (EditText) findViewById(R.id.nIDNovo);

        Cards = (TextView) findViewById(R.id.Cards); // Campo para jogar os cartões existentes

        btnAlterarCartao = (Button) findViewById(R.id.btnAlterarCartao);
        btnCancelarCartao = (Button) findViewById(R.id.btnCancelarCartao);
        btnCancelarCartao.setOnClickListener(this);

        nConta.setText(conta1.getNome());
        nID.setText(conta1.getId().toString());

        try {
            contaApi.getOneUsingGET1(conta1.getId()); // Pega os dados da conta
            cartaoApi.getAllUsingGET(conta1.getId()); // Pega os cartões da conta

            List<Cartao> getAPIFromText = cartaoApi.getAllUsingGET(conta1.getId());

            System.out.println("Get da Conta em Cartoes: "+contaApi.getOneUsingGET1(conta1.getId()));
            System.out.println("Get da Cartao em Cartoes: "+cartaoApi.getAllUsingGET(conta1.getId()));

            Cards.setText(getAPIFromText.toString());
        } catch (ApiException e) {
            System.out.println("Deu pau em Cartoes "+e);
        }
    }

    @Override
    public void onClick(View v) {
        /*final String nIntFromText = nValidar.getText().toString();
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
        }*/
    }
    public void onBackPressed(){
        Intent it = new Intent(this, Menu.class);
        startActivity(it);
    }
}