package br.com.cardtracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import java.util.List;
import java.util.Random;

import br.com.conductor.sdc.api.v1.CartaoApi;
import br.com.conductor.sdc.api.v1.ContaApi;
import br.com.conductor.sdc.api.v1.invoker.ApiException;
import br.com.conductor.sdc.api.v1.model.Cartao;
import br.com.conductor.sdc.api.v1.model.Conta;


public class Compra extends AppCompatActivity implements View.OnClickListener {

    // Data of user
    private EditText nValor;
    private EditText nConta;
    private EditText nID;
    private EditText nDestino;

    private Button btnComprar;

    private RadioButton radioButtonCartao1;
    private RadioButton radioButtonCartao2;

    // Atributos API
    public runAPI runAPI = new runAPI();
    public ContaApi contaApi = runAPI.getContaApiInfos();
    public CartaoApi cartaoApi = runAPI.getCartaoApiInfos();

    public Conta conta1 = runAPI.getConta1Infos();
    public Cartao cartao1 = runAPI.getCartao1Infos();
    public Cartao cartao2 = runAPI.getCartao2Infos();

    public Double valor;
    public Long IDCartaoOrg;
    public Long IDContaOrg;
    public String NomeCartaoOrg;
    public String destino;
    public Long CODValidar;

    public List<Cartao> getAPIFromText1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compra);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        } //Thread não dar conflito

        nValor = (EditText) findViewById(R.id.nValor);
        nConta = (EditText) findViewById(R.id.nConta);
        nID = (EditText) findViewById(R.id.nID);
        nDestino = (EditText) findViewById(R.id.nDestino);


        btnComprar = (Button) findViewById(R.id.btnComprar);

        radioButtonCartao1 = (RadioButton) findViewById(R.id.radioButtonCartao1);
        radioButtonCartao2 = (RadioButton) findViewById(R.id.radioButtonCartao2);

        nConta.setText(conta1.getNome());
        nID.setText(conta1.getId().toString());

        try {
            getAPIFromText1 = cartaoApi.getAllUsingGET(conta1.getId()); // Pegar todos os cartões da conta 1
            radioButtonCartao1.setText(cartao1.getNumero());
            radioButtonCartao2.setText(cartao2.getNumero());
        } catch (ApiException e) {
            System.out.println("Erro em Comprar"+e);
        }

        btnComprar.setOnClickListener(this);
        // Limite dos Cartões NÃO APAGAR!!!!
        /*try {
            Limites.setText("Cartão ID "+cartao1.getId()+" : "+cartaoApi.limiteUsingGET(conta1.getId(), cartao1.getId())+
                    "\nCartão ID "+cartao2.getId()+" : "+cartaoApi.limiteUsingGET(conta1.getId(), cartao2.getId()));
            Limite limite = (cartaoApi.limiteUsingGET(conta1.getId(), cartao1.getId()));
            System.out.println(limite.getValor()); //FUNCIONOU
        } catch (ApiException e) {
            Limites.setText("Algum dos cartões está bloqueado. Para verificar apenas o Limite de um cartão, vá até a aba 'Cartões' - " + e);
        }*/


    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder dig = new AlertDialog.Builder(Compra.this);

        //Gerador de Códigos
        final Random numRandom = new Random();
        CODValidar = numRandom.nextInt()& 0x00000000ffffffffL; //Unsigned Long

        //Tratamento dos campos
        try{
            valor = new Double(nValor.getText().toString());
        }catch (NumberFormatException n){
            dig.setTitle("Erro");
            dig.setMessage("\nPreencha o valor da transferência");
            dig.setPositiveButton("Voltar",null);
            dig.show();
        }

        try{
            destino = new String(nDestino.getText().toString());
        }catch (NullPointerException n){
            dig.setTitle("Erro");
            dig.setMessage("\nPreencha o destino");
            dig.setPositiveButton("Voltar",null);
            dig.show();
        }

        if (radioButtonCartao1.isChecked()==false && radioButtonCartao2.isChecked()==false) {
            dig.setTitle("Erro");
            dig.setMessage("\nSelecione um cartão");
            dig.setPositiveButton("Voltar",null);
            dig.show();
        }

        //Compra com Cartão 1
        if (radioButtonCartao1.isChecked()) {
            IDContaOrg = conta1.getId();
            IDCartaoOrg = cartao1.getId();
            NomeCartaoOrg = cartao1.getNome();
                    dig.setTitle("Comprar");
                    dig.setMessage("\nCartão: " + NomeCartaoOrg + "\nDestino: "+destino+"\nValor: R$" + valor);
                    dig.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AlertDialog.Builder dig = new AlertDialog.Builder(Compra.this);

                            dig.setTitle("Enviando Código de Confirmação");
                            dig.setMessage("\nCódigo enviado via SMS para o número: +5583999887734");
                            dig.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Bundle params = new Bundle();
                                    params.putDouble("valor",valor);
                                    params.putLong("IDContaOrg",IDContaOrg);
                                    params.putLong("IDCartaoOrg",IDCartaoOrg);
                                    params.putString("NomeCartaoOrg",NomeCartaoOrg);
                                    params.putLong("CODValidar",CODValidar);
                                    params.putString("destino",destino);

                                    Intent intent = new Intent(Compra.this,PopupCompra.class);
                                    intent.putExtras(params);

                                    startActivity(intent);//ou if result
                                }
                            });
                            dig.show();
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage("+5583999887734", null, "Código de Confirmação: "+CODValidar, null, null);
                        }
                    }); // Fim do Positive Button 1
                    dig.setNegativeButton("Cancelar", null);
                    dig.show();
                }// Fim do if Cartão 1

        //Compra com Cartão 2
        if (radioButtonCartao2.isChecked()) {
            IDContaOrg = conta1.getId();
            IDCartaoOrg = cartao2.getId();
            NomeCartaoOrg = cartao2.getNome();
            dig.setTitle("Comprar");
            dig.setMessage("\nCartão: " + NomeCartaoOrg + "\nDestino: "+destino+"\nValor: R$" + valor);
            dig.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AlertDialog.Builder dig = new AlertDialog.Builder(Compra.this);

                    dig.setTitle("Enviando Código de Confirmação");
                    dig.setMessage("\nCódigo enviado via SMS para o número: +5583999887734");
                    dig.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Bundle params = new Bundle();
                            params.putDouble("valor",valor);
                            params.putLong("IDContaOrg",IDContaOrg);
                            params.putLong("IDCartaoOrg",IDCartaoOrg);
                            params.putString("NomeCartaoOrg",NomeCartaoOrg);
                            params.putLong("CODValidar",CODValidar);
                            params.putString("destino",destino);

                            Intent intent = new Intent(Compra.this,PopupCompra.class);
                            intent.putExtras(params);

                            startActivity(intent);//ou if result
                        }
                    });
                    dig.show();
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage("+5583999887734", null, "Código de Confirmação: "+CODValidar, null, null);
                }
            }); // Fim do Positive Button 1
            dig.setNegativeButton("Cancelar", null);
            dig.show();
        }// Fim do if Cartão 1

    }

    public void onBackPressed(){
        Intent it = new Intent(this, Menu.class);
        startActivity(it);
    }
}
