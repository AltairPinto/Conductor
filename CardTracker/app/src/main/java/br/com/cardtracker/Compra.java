package br.com.cardtracker;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import br.com.conductor.sdc.api.v1.CartaoApi;
import br.com.conductor.sdc.api.v1.ContaApi;
import br.com.conductor.sdc.api.v1.invoker.ApiException;
import br.com.conductor.sdc.api.v1.model.Cartao;
import br.com.conductor.sdc.api.v1.model.Conta;


public class Compra extends AppCompatActivity implements View.OnClickListener {

    // Data of user
    private EditText nConta;
    private EditText nID;
    private EditText nValor;
    private EditText nConfirmacao;

    private Spinner nIDSpinner;

    private TextView Limites;

    private Button btnGerarCodigo;
    private Button btnValidar;

    // Atributos API
    public runAPI runAPI = new runAPI();
    public ContaApi contaApi = runAPI.getContaApiInfos();//("3BJU7WSdxYVy","VxUGXKTjnPCa","https://api.conductor.com.br/sdc");
    public CartaoApi cartaoApi = runAPI.getCartaoApiInfos();//("3BJU7WSdxYVy","VxUGXKTjnPCa","https://api.conductor.com.br/sdc");
    public Conta conta1 = runAPI.getConta1Infos();
    public Cartao cartao1 = runAPI.getCartao1Infos();
    public Cartao cartao2 = runAPI.getCartao2Infos();

    public String selectID;
    public int SMSCode = 5530928;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compra);

        ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.SEND_SMS},1);


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        } //Thread não dar conflito

        Limites = (TextView) findViewById(R.id.Limites); // Campo para jogar os cartões existentes

        nValor = (EditText) findViewById(R.id.nValor);
        nConfirmacao = (EditText) findViewById(R.id.nConfirmacao);
        nConta = (EditText) findViewById(R.id.nConta);
        nID = (EditText) findViewById(R.id.nID);

        nIDSpinner = (Spinner) findViewById(R.id.nIDSpinner);

        btnGerarCodigo = (Button) findViewById(R.id.btnGerarCodigo);
        btnValidar = (Button) findViewById(R.id.btnValidar);

        nConta.setText(conta1.getNome());
        nID.setText(conta1.getId().toString());

        // Limite dos Cartões
        try {
            Limites.setText("Cartão ID "+cartao1.getId()+" : "+cartaoApi.limiteUsingGET(conta1.getId(), cartao1.getId())+
                    "\nCartão ID "+cartao2.getId()+" : "+cartaoApi.limiteUsingGET(conta1.getId(), cartao2.getId()));
        } catch (ApiException e) {
            Limites.setText("Algum dos cartões está bloqueado. Para verificar apenas o Limite de um cartão, vá até a aba 'Cartões' - " + e);
        }

        // Criando o Spinner para IDs
        ArrayAdapter<Long> arrayAdapter = new ArrayAdapter<Long>(this, android.R.layout.simple_spinner_item);
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

        btnGerarCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dig = new AlertDialog.Builder(Compra.this);
                dig.setTitle("Enviando Código de Confirmação");
                dig.setMessage("\nCódigo enviado via SMS para o número: +5583999887734");
                dig.setNeutralButton("OK", null);
                dig.show();
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage("+5583999887734", null, "Código de Confirmação: 5530928", null, null);
            }
        }); // Fim Gera Código

        btnValidar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder dig = new AlertDialog.Builder(Compra.this);

        final Long getLongFromText = new Long(selectID);
        final Double getDoubleFromText = new Double(nValor.getText().toString());
        final int nIntFromText = new Integer(nConfirmacao.getText().toString()).intValue();

        if (nIntFromText == SMSCode){
            try {
                cartaoApi.transacionarUsingPUT(conta1.getId(),getLongFromText,getDoubleFromText);
                dig.setTitle("Compra efetuada com sucesso");
                dig.setMessage("\nCompra do Cartão ID " + getLongFromText+" no valor de: R$"+getDoubleFromText);
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
        // Saída
        try {
            Limites.setText("Cartão ID "+cartao1.getId()+" : "+cartaoApi.limiteUsingGET(conta1.getId(), cartao1.getId())+
                    "\nCartão ID "+cartao2.getId()+" : "+cartaoApi.limiteUsingGET(conta1.getId(), cartao2.getId()));
        } catch (ApiException e) {
            Limites.setText("Algum dos cartões está bloqueado. Para verificar apenas o Limite de um cartão, vá até a aba 'Cartões' - " + e);
        }
    }

    public void onBackPressed(){
        Intent it = new Intent(this, Menu.class);
        startActivity(it);
    }
}
