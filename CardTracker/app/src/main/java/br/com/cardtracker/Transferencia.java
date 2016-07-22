package br.com.cardtracker;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import br.com.conductor.sdc.api.v1.CartaoApi;
import br.com.conductor.sdc.api.v1.ContaApi;
import br.com.conductor.sdc.api.v1.invoker.ApiException;
import br.com.conductor.sdc.api.v1.model.Cartao;
import br.com.conductor.sdc.api.v1.model.Conta;


public class Transferencia extends AppCompatActivity implements View.OnClickListener{

    private Spinner nIDOrigem;
    private Spinner nIDDestino;

    private TextView Limites;
    private TextView Extratos;

    private Button btnGerarCodigo;
    private Button btnValidar;

    private EditText nValor;
    private EditText nConfirmacao;
    private EditText nConta;
    private EditText nID;

    // Atributos API
    public runAPI runAPI = new runAPI();
    public ContaApi contaApi = runAPI.getContaApiInfos("3BJU7WSdxYVy","VxUGXKTjnPCa","https://api.conductor.com.br/sdc");
    public CartaoApi cartaoApi = runAPI.getCartaoApiInfos("3BJU7WSdxYVy","VxUGXKTjnPCa","https://api.conductor.com.br/sdc");
    public Conta conta1 = runAPI.getConta1Infos();
    public Cartao cartao1 = runAPI.getCartao1Infos();
    public Cartao cartao2 = runAPI.getCartao2Infos();

    public String selectID1;
    public String selectID2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transferencia);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        } //Thread não dar conflito

        Limites = (TextView) findViewById(R.id.Limites); // Campo para jogar os cartões existentes
        Extratos = (TextView) findViewById(R.id.Extratos); // Campo para jogar os cartões existentes

        nValor = (EditText) findViewById(R.id.nValor);
        nConfirmacao = (EditText) findViewById(R.id.nConfirmacao);
        nConta = (EditText) findViewById(R.id.nConta);
        nID = (EditText) findViewById(R.id.nID);


        nIDOrigem = (Spinner) findViewById(R.id.nIDOrigem);
        nIDDestino = (Spinner) findViewById(R.id.nIDDestino);

        btnGerarCodigo = (Button) findViewById(R.id.btnGerarCodigo);
        btnValidar = (Button) findViewById(R.id.btnValidar);

        nConta.setText(conta1.getNome());
        nID.setText(conta1.getId().toString());

        // Limite dos Cartões
        try {

            Limites.setText("Cartão ID "+cartao1.getId()+" : "+cartaoApi.limiteUsingGET(conta1.getId(), cartao1.getId())+
                            "\nCartão ID "+cartao2.getId()+" : "+cartaoApi.limiteUsingGET(conta1.getId(), cartao2.getId()));
        } catch (ApiException e) {
            Extratos.setText("Algum dos cartões está bloqueado. Para verificar apenas o Limite de um cartão, vá até a aba 'Cartões' - " + e);
        }

        // Extrato dos Cartões
        try {

            List<Cartao> getAPIFromText = cartaoApi.getAllUsingGET(conta1.getId()); // Pegar todos os cartões da conta

            Extratos.setText("Cartão ID "+cartao1.getId()+" : "+cartaoApi.extratosUsingPOST(conta1.getId(),cartao1.getId())+
                            "\nCartão ID "+cartao2.getId()+" : "+cartaoApi.extratosUsingPOST(conta1.getId(),cartao2.getId()));
        } catch (ApiException e) {
            Extratos.setText("Algum dos cartões está bloqueado. Para verificar apenas o extrato de um cartão, vá até a aba 'Cartões' - " + e);
        }

        // Criando o Spinner para IDs
        ArrayAdapter<Long> arrayAdapter = new ArrayAdapter<Long>(this, android.R.layout.simple_spinner_item);
        final ArrayAdapter<Long> spinnerArrayAdapter = arrayAdapter;
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        nIDOrigem.setAdapter(spinnerArrayAdapter);
        nIDDestino.setAdapter(spinnerArrayAdapter);
        spinnerArrayAdapter.add(cartao1.getId());
        spinnerArrayAdapter.add(cartao2.getId());

        nIDOrigem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            // Pegando ID a partir da posição selecionada
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectID1 = parent.getItemAtPosition(position).toString();
                System.out.println("SelectedID1 inicial : " + selectID1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinnerArrayAdapter.add(null);
            }
        }); //Fim do Spinner nIDOrigem

        nIDDestino.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            // Pegando ID a partir da posição selecionada
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectID2 = parent.getItemAtPosition(position).toString();
                System.out.println("SelectedID2 inicial : " + selectID2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinnerArrayAdapter.add(null);
            }
        }); //Fim do Spinner nIDDestino

        btnGerarCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }); // Fim Gera Código

        btnValidar.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder dig = new AlertDialog.Builder(Transferencia.this);

        final Long getLongFromText1 = new Long(selectID1);
        final Long getLongFromText2 = new Long(selectID2);
        final Double getDoubleFromText = new Double(nValor.getText().toString());

        if ((nConfirmacao.getText().toString())!=null){
            try {
                cartaoApi.transferirUsingPOST(conta1.getId(),getLongFromText1,getLongFromText2,getDoubleFromText);
                dig.setTitle("Transferência efetuada com sucesso");
                dig.setMessage("\nTransferência do Cartão ID " + getLongFromText1+" para o Cartão ID "+getLongFromText2 + " no valor de: R$"+getDoubleFromText);
                dig.setNeutralButton("OK", null);
                dig.show();
                nValor.setText(null);
            } catch (ApiException e) {
                dig.setTitle("Erro");
                dig.setMessage(""+e);
                dig.setNeutralButton("Voltar", null);
                dig.show();
            }
        }else{
            dig.setTitle("Erro");
            dig.setMessage("\nPreencha os campos corretamente");
            dig.setNeutralButton("Voltar", null);
            dig.show();
        }
    }

    public void onBackPressed(){
        Intent it = new Intent(this, Menu.class);
        startActivity(it);
    }
}
