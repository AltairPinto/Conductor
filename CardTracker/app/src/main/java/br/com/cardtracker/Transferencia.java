package br.com.cardtracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
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


public class Transferencia extends AppCompatActivity implements View.OnClickListener{

    private Button btnTransferencia;

    private EditText nIDConta;
    private EditText nIDCartao;
    private EditText nValor;
    private EditText nConta;
    private EditText nID;

    private RadioButton radioButtonCartao1;
    private RadioButton radioButtonCartao2;

    // Atributos API
    public runAPI runAPI = new runAPI();
    public ContaApi contaApi = runAPI.getContaApiInfos();
    public CartaoApi cartaoApi = runAPI.getCartaoApiInfos();

    public Conta conta1 = runAPI.getConta1Infos();
    public Cartao cartao1 = runAPI.getCartao1Infos();
    public Cartao cartao2 = runAPI.getCartao2Infos();

    public Conta conta2 = runAPI.getConta2Infos();
    public Cartao cartao3 = runAPI.getCartao3Infos();
    public Cartao cartao4 = runAPI.getCartao4Infos();

    public Double valor;
    public Long IDCartaoOrg;
    public Long IDContaOrg;
    public String NomeCartaoOrg;
    public Long IDContaVar;
    public Long IDCartaoVar;
    public String NomeCartaoVar;
    public Long CODValidar;

    public List<Cartao> getAPIFromText1;
    public List<Cartao> getAPIFromText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transferencia);

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        } //Thread não dar conflito

        //ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.SEND_SMS},1);

        nValor = (EditText) findViewById(R.id.nValor);
        nIDConta = (EditText) findViewById(R.id.nIDConta);
        nIDCartao = (EditText) findViewById(R.id.nIDCartao);
        nConta = (EditText) findViewById(R.id.nConta);
        nID = (EditText) findViewById(R.id.nID);

        btnTransferencia = (Button) findViewById(R.id.btnTransferencia);

        radioButtonCartao1 = (RadioButton) findViewById(R.id.radioButtonCartao1);
        radioButtonCartao2 = (RadioButton) findViewById(R.id.radioButtonCartao2);

        nConta.setText(conta1.getNome());
        nID.setText(conta1.getId().toString());

        try {
            getAPIFromText1 = cartaoApi.getAllUsingGET(conta1.getId()); // Pegar todos os cartões da conta
            getAPIFromText2 = cartaoApi.getAllUsingGET(conta2.getId()); // Pegar todos os cartões da conta
            radioButtonCartao1.setText(cartao1.getNumero());
            radioButtonCartao2.setText(cartao2.getNumero());
        } catch (ApiException e) {
            System.out.println("Erro em Cartoes "+e);
        }

        System.out.println("conta 2: "+conta2.getId()+"\ncartao 3: "+cartao3.getId()+"\ncartao 4: "+cartao4.getId());

        btnTransferencia.setOnClickListener(this);

    }

    @Override
    public void onClick(final View v) {

        AlertDialog.Builder dig = new AlertDialog.Builder(Transferencia.this);

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
            IDCartaoVar = new Long(nIDCartao.getText().toString());
        }catch (NumberFormatException n){
            dig.setTitle("Erro");
            dig.setMessage("\nPreencha o ID do Cartão Destino");
            dig.setPositiveButton("Voltar",null);
            dig.show();
        }

        try{
            IDContaVar = new Long(nIDConta.getText().toString());
        }catch (NumberFormatException n){
            dig.setTitle("Erro");
            dig.setMessage("\nPreencha o ID da Conta Destino");
            dig.setPositiveButton("Voltar",null);
            dig.show();
        }

        if (radioButtonCartao1.isChecked()==false && radioButtonCartao2.isChecked()==false) {
            dig.setTitle("Erro");
            dig.setMessage("\nSelecione um cartão");
            dig.setPositiveButton("Voltar",null);
            dig.show();
        }
        /*/ Tratamento de Dados Inválidos
        /if(IDContaVar!=cartao2.getId()){
            dig.setTitle("Erro");
            dig.setMessage("Conta inválida");
            dig.setPositiveButton("Voltar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    nIDConta.setText(null);
                }
            });
            dig.show();
        }
        if(IDCartaoVar!=cartao3.getId() || IDCartaoVar!=cartao4.getId()){
            dig.setTitle("Erro");
            dig.setMessage("Cartão inválido");
            dig.setPositiveButton("Voltar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    nIDCartao.setText(null);
                }
            });
            dig.show();
        }
        */

        // Tratamento dos cartões selecionados
        if (radioButtonCartao1.isChecked()) {
            System.out.println("Button checkado");
            IDContaOrg = conta1.getId();
            IDCartaoOrg = cartao1.getId();
            NomeCartaoOrg = cartao1.getNome();
            if (IDContaVar == conta2.getId().longValue()) {
                System.out.println("IDContaVar checkada");
                if (IDCartaoVar == cartao3.getId().longValue()) {
                    NomeCartaoVar = cartao3.getNome();
                    System.out.println("IDCartaoVar checkado");
                    dig.setTitle("Transferir");
                    dig.setMessage("\nPara: " + NomeCartaoVar + "\nValor: R$" + valor);
                    dig.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AlertDialog.Builder dig = new AlertDialog.Builder(Transferencia.this);

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
                                    params.putLong("IDCartaoOrg",IDCartaoOrg);
                                    params.putLong("IDCartaoVar",IDCartaoVar);
                                    params.putString("NomeCartaoVar",NomeCartaoVar);
                                    params.putLong("CODValidar",CODValidar);

                                    Intent intent = new Intent(Transferencia.this,PopupTransferencia.class);
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
                }// Fim do if Cartão 3
                if (IDCartaoVar == cartao4.getId().longValue()) {
                    NomeCartaoVar = cartao4.getNome();
                    System.out.println("IDCartaoVar checkado");
                    dig.setTitle("Transferir");
                    dig.setMessage("\nPara: " + NomeCartaoVar + "\nValor: R$" + valor);
                    dig.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AlertDialog.Builder dig = new AlertDialog.Builder(Transferencia.this);

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
                                    params.putLong("IDCartaoOrg",IDCartaoOrg);
                                    params.putLong("IDCartaoVar",IDCartaoVar);
                                    params.putString("NomeCartaoVar",NomeCartaoVar);
                                    params.putLong("CODValidar",CODValidar);

                                    Intent intent = new Intent(Transferencia.this,PopupTransferencia.class);
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
                }//Fim do if Cartão 4
            }//Fim do if Conta 2
        }//Fim do cartão 1

        if(radioButtonCartao2.isChecked()){
            IDContaOrg = conta1.getId();
            IDCartaoOrg = cartao2.getId();
            NomeCartaoOrg = cartao2.getNome();
            if (IDContaVar == conta2.getId().longValue()) {
                System.out.println("IDContaVar checkada");
                if (IDCartaoVar == cartao3.getId().longValue()) {
                    NomeCartaoVar = cartao3.getNome();
                    System.out.println("IDCartaoVar checkado");
                    dig.setTitle("Transferir");
                    dig.setMessage("\nPara: " + NomeCartaoVar + "\nValor: R$" + valor);
                    dig.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AlertDialog.Builder dig = new AlertDialog.Builder(Transferencia.this);

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
                                    params.putLong("IDCartaoOrg",IDCartaoOrg);
                                    params.putLong("IDCartaoVar",IDCartaoVar);
                                    params.putString("NomeCartaoVar",NomeCartaoVar);
                                    params.putLong("CODValidar",CODValidar);

                                    Intent intent = new Intent(Transferencia.this,PopupTransferencia.class);
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
                }// Fim do if Cartão 3
                if (IDCartaoVar == cartao4.getId().longValue()) {
                    NomeCartaoVar = cartao4.getNome();
                    System.out.println("IDCartaoVar checkado");
                    dig.setTitle("Transferir");
                    dig.setMessage("\nPara: " + NomeCartaoVar + "\nValor: R$" + valor);
                    dig.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AlertDialog.Builder dig = new AlertDialog.Builder(Transferencia.this);

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
                                    params.putLong("IDCartaoOrg",IDCartaoOrg);
                                    params.putLong("IDCartaoVar",IDCartaoVar);
                                    params.putString("NomeCartaoVar",NomeCartaoVar);
                                    params.putLong("CODValidar",CODValidar);

                                    Intent intent = new Intent(Transferencia.this,PopupTransferencia.class);
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
                }//Fim do if Cartão 4
            }//Fim do if Conta 2
        }// Fim do If Button 2
    }//Saída

public void onBackPressed(){
        Intent it = new Intent(this, Menu.class);
        startActivity(it);
    }
}
