package br.com.cardtracker;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import java.util.List;

import br.com.conductor.sdc.api.v1.CartaoApi;
import br.com.conductor.sdc.api.v1.ContaApi;
import br.com.conductor.sdc.api.v1.invoker.ApiException;
import br.com.conductor.sdc.api.v1.model.Cartao;
import br.com.conductor.sdc.api.v1.model.Conta;
import br.com.conductor.sdc.api.v1.model.Extrato;

public class Infos extends AppCompatActivity implements View.OnClickListener{

    private EditText nID;
    private EditText nConta;

    private Button btnConsultar;

    private RadioButton radioButtonCartao1;
    private RadioButton radioButtonCartao2;

    // Atributos API
    public runAPI runAPI = new runAPI();
    public ContaApi contaApi = runAPI.getContaApiInfos();
    public CartaoApi cartaoApi = runAPI.getCartaoApiInfos();
    public Conta conta1 = runAPI.getConta1Infos();
    public Cartao cartao1 = runAPI.getCartao1Infos();
    public Cartao cartao2 = runAPI.getCartao2Infos();

    public Long IDCartaoOrg;
    public Long IDContaOrg;
    public String NumeroCartaoOrg;

    public List<Cartao> getAPIFromText;
    public List<Extrato> extratos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infos);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        } //Thread não dar conflito

        nConta = (EditText) findViewById(R.id.nConta);
        nID = (EditText) findViewById(R.id.nID);

        btnConsultar = (Button) findViewById(R.id.btnConsultar);

        radioButtonCartao1 = (RadioButton) findViewById(R.id.radioButtonCartao1);
        radioButtonCartao2 = (RadioButton) findViewById(R.id.radioButtonCartao2);

        nConta.setText(conta1.getNome());
        nID.setText(conta1.getId().toString());

        try {
            getAPIFromText = cartaoApi.getAllUsingGET(conta1.getId()); // Pegar todos os cartões da conta
            radioButtonCartao1.setText(cartao1.getNumero());
            radioButtonCartao2.setText(cartao2.getNumero());
        } catch (ApiException e) {
            System.out.println("Erro em Cartoes "+e);
        }

        btnConsultar.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        AlertDialog.Builder dig = new AlertDialog.Builder(Infos.this);

        if (radioButtonCartao1.isChecked()==false && radioButtonCartao2.isChecked()==false) {
            dig.setTitle("Erro");
            dig.setMessage("\nSelecione um cartão");
            dig.setPositiveButton("Voltar",null);
            dig.show();
        }

        //Informações do Cartão 1
        if (radioButtonCartao1.isChecked()) {
            IDContaOrg = conta1.getId();
            IDCartaoOrg = cartao1.getId();
            NumeroCartaoOrg = cartao1.getNumero();

                    Bundle params = new Bundle();
                    params.putLong("IDContaOrg",IDContaOrg);
                    params.putLong("IDCartaoOrg",IDCartaoOrg);
                    params.putString("NumeroCartaoOrg",NumeroCartaoOrg);

                    Intent intent = new Intent(Infos.this,PopupInfos.class);
                    intent.putExtras(params);

                    startActivity(intent);

        }// Fim do if Cartão 1

        //Informações do Cartão 2
        if (radioButtonCartao2.isChecked()) {
            IDContaOrg = conta1.getId();
            IDCartaoOrg = cartao2.getId();
            NumeroCartaoOrg = cartao2.getNumero();
            //dig.setTitle("Consulta");
            //dig.setMessage("\nConsultando dados do Cartão");

            Bundle params = new Bundle();
            params.putLong("IDContaOrg",IDContaOrg);
            params.putLong("IDCartaoOrg",IDCartaoOrg);
            params.putString("NumeroCartaoOrg",NumeroCartaoOrg);

            Intent intent = new Intent(Infos.this,PopupInfos.class);
            intent.putExtras(params);

            startActivity(intent);
            //dig.show();
        }// Fim do if Cartão 1

    }

    public void onBackPressed(){
        Intent it = new Intent(this, Menu.class);
        startActivity(it);
    }

}
