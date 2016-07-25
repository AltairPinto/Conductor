package br.com.cardtracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
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
import br.com.conductor.sdc.api.v1.model.Extrato;
import br.com.conductor.sdc.api.v1.model.Limite;

public class PopupInfos extends Activity implements View.OnClickListener{

    private Button btnOk;

    private EditText nNumeroCartao;
    private EditText nLimite;

    private TextView nExtrato1;
    private TextView nExtrato2;
    private TextView nExtrato3;
    private TextView nExtrato4;
    private TextView nExtrato5;
    private TextView nExtrato6;


    // Atributos API
    public runAPI runAPI = new runAPI();
    public ContaApi contaApi = runAPI.getContaApiInfos();
    public CartaoApi cartaoApi = runAPI.getCartaoApiInfos();

    public Conta conta1 = runAPI.getConta1Infos();
    public Cartao cartao1 = runAPI.getCartao1Infos();
    public Cartao cartao2 = runAPI.getCartao2Infos();

    public List<Extrato> extratos;
    private String dados[];
    public String extratoData;
    public String extratoTipo;
    public String extratoValor;

    public Long IDContaOrg;
    public Long IDCartaoOrg;
    public String NumeroCartaoOrg;
    public static final String credito = "CREDITO";
    public static final String debito = "DEBITO";
    private String printToString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_infos);

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        } //Thread não dar conflito

        btnOk = (Button)findViewById(R.id.btnOk);

        nNumeroCartao = (EditText) findViewById(R.id.nNumeroCartao);
        nLimite = (EditText) findViewById(R.id.nLimite);

        nExtrato1 = (TextView) findViewById(R.id.nExtrato1);
        nExtrato2 = (TextView) findViewById(R.id.nExtrato2);
        nExtrato3 = (TextView) findViewById(R.id.nExtrato3);
        nExtrato4 = (TextView) findViewById(R.id.nExtrato4);
        nExtrato5 = (TextView) findViewById(R.id.nExtrato5);
        nExtrato6 = (TextView) findViewById(R.id.nExtrato6);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int heigh = dm.heightPixels;

        getWindow().setLayout((int)(width*.9),(int)(heigh*.75));

        btnOk.setOnClickListener(this);

        // Recebimento de dados das outras telas
        Intent intent = getIntent();
        if(intent!=null){
            Bundle params = intent.getExtras();
            if(params!=null) {
                // Recebendo valores de Transferência
                IDContaOrg = params.getLong("IDContaOrg");
                IDCartaoOrg = params.getLong("IDCartaoOrg");
                NumeroCartaoOrg = params.getString("NumeroCartaoOrg");
            }} // Fim do Intent

        nNumeroCartao.setText(NumeroCartaoOrg);
        //Exibição do limite do cartão selecionado
        try {
            Limite limite = (cartaoApi.limiteUsingGET(IDContaOrg, IDCartaoOrg));
            nLimite.setText(limite.getValor().toString());
        } catch (ApiException e) {
            nLimite.setText("Bloqueado" + e);
        }

        //Exibição do extrato do cartão selecionado
        try {
            extratos = cartaoApi.extratosUsingPOST(conta1.getId(),cartao1.getId());
        } catch (ApiException e) {
            System.out.println(e);
        }
        dados = new String[extratos.size()];
        System.out.println("Tamanho de Dados: "+extratos.size());
        for(int i=0;i<extratos.size();i++){
            Extrato TOString = extratos.get(i);
            String str = new String(TOString.toString());
            //Capturando posições do JSON
            int posInicialData = str.indexOf("data:");
            int barraData = str.indexOf("2016\n");
            int posInicialTipo = str.indexOf("tipo:");
            int barraTipo = str.indexOf("TO\n");
            int posInicialValor = str.indexOf("valor:");
            int barraValor = str.indexOf("\n}");
            //Organizando Dados
            extratoData = (str.substring(posInicialData+6,barraData+4));
            extratoTipo = (str.substring(posInicialTipo+6,barraTipo+2));
            extratoValor = (str.substring(posInicialValor+7,barraValor));
            System.out.println(extratoData + extratoTipo + extratoValor);
            //Armazenando Dados
            if (extratoTipo.equals(credito)){dados[i] = extratoData+"\n+ R$"+extratoValor+"\n";}
            if (extratoTipo.equals(debito)){dados[i] = extratoData+"\n- R$"+extratoValor+"\n";}
            System.out.println("Dados índice ["+i+"]: "+dados[i]);
        }// Fim do for

        Carregar();
    }

    private void Carregar() {
        for (int i = 0; i < dados.length; i++ ) {
            System.out.println("Dados armazenados em dados[" + i + "] = " + dados[i]);
            //pesquisa.add(dados[i]);
        }
        if (dados.length>=1){nExtrato1.setText(dados[0]);}
        if (dados.length>=2){nExtrato1.setText(dados[0]);nExtrato2.setText(dados[1]);}
        if (dados.length>=3){nExtrato1.setText(dados[0]);nExtrato2.setText(dados[1]);nExtrato3.setText(dados[2]);}
        if (dados.length>=4){nExtrato1.setText(dados[0]);nExtrato2.setText(dados[1]);nExtrato3.setText(dados[2]);nExtrato4.setText(dados[3]);}
        if (dados.length>=5){nExtrato1.setText(dados[0]);nExtrato2.setText(dados[1]);nExtrato3.setText(dados[2]);nExtrato4.setText(dados[3]);nExtrato5.setText(dados[4]);}
        if (dados.length>=6){nExtrato1.setText(dados[0]);nExtrato2.setText(dados[1]);nExtrato3.setText(dados[2]);nExtrato4.setText(dados[3]);nExtrato5.setText(dados[4]);nExtrato6.setText(dados[5]);}
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, pesquisa);
        //nExtrato.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
