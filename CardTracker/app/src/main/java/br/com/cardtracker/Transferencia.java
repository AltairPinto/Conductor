package br.com.cardtracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
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

    public int SMSCode = 5530928;
    public Double valor;
    public Long IDContaVar;
    public Long IDCartaoVar;
    public int verifica;
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
        System.out.println("TESTE "+cartao1.getNumero()+"\n"+cartao2.getNumero());
        System.out.println("Contas TESTE "+conta1.getId()+"\n"+conta2.getId());

        try {
            getAPIFromText1 = cartaoApi.getAllUsingGET(conta1.getId()); // Pegar todos os cartões da conta
            getAPIFromText2 = cartaoApi.getAllUsingGET(conta2.getId()); // Pegar todos os cartões da conta
            radioButtonCartao1.setText(cartao1.getNumero());
            radioButtonCartao2.setText(cartao2.getNumero());
            System.out.println("TESTE 2 "+cartao1.getNumero()+"\n"+cartao2.getNumero());

        } catch (ApiException e) {
            System.out.println("Deu pau em Cartoes "+e);
        }

        /*btnGerarCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dig = new AlertDialog.Builder(Transferencia.this);
                dig.setTitle("Enviando Código de Confirmação");
                dig.setMessage("\nCódigo enviado via SMS para o número: +5583999887734");
                dig.setNeutralButton("OK", null);
                dig.show();
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage("+5583999887734", null, "Código de Confirmação: 5530928", null, null);
            }
        }); // Fim Gera Código*/

        btnTransferencia.setOnClickListener(this);

    }

    @Override
    public void onClick(final View v) {

        AlertDialog.Builder dig = new AlertDialog.Builder(Transferencia.this);
        //Tratamento dos campos
        try{
            valor = new Double(nValor.getText().toString());
        }catch (NullPointerException n){
            dig.setTitle("Erro");
            dig.setMessage("Preencha o valor da transferência");
            dig.setPositiveButton("Voltar",null);
            dig.show();
        }
        //valor = new Integer(nValor.getText().toString());

        try{
            IDCartaoVar = new Long(nIDCartao.getText().toString());
        }catch (NullPointerException n){
            dig.setTitle("Erro");
            dig.setMessage("Preencha o ID do Cartão Destino");
            dig.setPositiveButton("Voltar",null);
            dig.show();
        }
        //IDCartaoVar = new Long(nIDCartao.getText().toString());

        try{
            IDContaVar = new Long(nIDConta.getText().toString());
        }catch (NullPointerException n){
            dig.setTitle("Erro");
            dig.setMessage("Preencha o ID da Conta Destino");
            dig.setPositiveButton("Voltar",null);
            dig.show();
        }
        //IDContaVar = new Long(nIDConta.getText().toString());

        if (radioButtonCartao1.isChecked()) {
            System.out.println("Button checkado");
            final int nIntFromText1 = new Integer(nIDConta.getText().toString()).intValue();
            final int nIntFromText2 = new Integer(nIDCartao.getText().toString()).intValue();
            if (nIntFromText1 == conta2.getId().intValue()) {
                System.out.println("IDContaVar checkada");
                if (nIntFromText2 == cartao3.getId().intValue()) {
                    System.out.println("IDCartaoVar checkado");
                    dig.setTitle("Transferir");
                    dig.setMessage("Para: " + cartao3.getNome() + "\nValor: R$" + valor);
                    dig.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AlertDialog.Builder digsub = new AlertDialog.Builder(Transferencia.this);//Errado! Activity necessária
                            startActivity(new Intent(Transferencia.this,LoginFragment.class));
                            LoginFragment loginFragment = new LoginFragment();
                            verifica = loginFragment.getPress();
                            System.out.println("Valor de Verifica :"+verifica);
                            if(verifica==1) {
                            try {
                                cartaoApi.transferirUsingPOST(conta1.getId(), cartao1.getId(), IDCartaoVar, valor);
                                digsub.setTitle("Transferido");
                                digsub.setMessage("\nDe: "+cartao1.getNome()+" Cartão de ID: "+ cartao1.getId()+
                                                    "\nPara: "+cartao3.getNome()+" Cartão ID: +"+cartao3.getId()+
                                                    "\nValor: R$"+valor);
                                digsub.setPositiveButton("OK", null);
                                digsub.show();
                            } catch (ApiException e) {
                                System.out.println(e);
                            }
                            }
                        }
                    }); // Fim do Positive Button 1
                    dig.setNegativeButton("Cancelar", null);
                    dig.show();
                }
            }
        }
        if(radioButtonCartao2.isChecked()){
            if(cartao2.getStatus()==Cartao.StatusEnum.BLOQUEADO){
                dig.setTitle("Erro");
                dig.setMessage("Cartão "+cartao2.getNumero()+" já está bloqueado");
                dig.setPositiveButton("Voltar",null);
                dig.show();
            }else if(cartao2.getStatus()==Cartao.StatusEnum.CANCELADO){
                dig.setTitle("Erro");
                dig.setMessage("Cartão "+cartao2.getNumero()+" está cancelado");
                dig.setPositiveButton("Voltar",null);
                dig.show();
            }else {
                dig.setTitle("Confirmação");
                dig.setMessage("Confirmar bloqueio do cartão " + cartao2.getNumero() + " ?");
                dig.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder digsub = new AlertDialog.Builder(Transferencia.this);
                        try {
                            cartaoApi.bloquearUsingPUT(conta1.getId(), cartao2.getId());
                            cartao2.setStatus(Cartao.StatusEnum.BLOQUEADO);
                            digsub.setTitle("Bloqueado");
                            digsub.setMessage("\nCartão de ID " + cartao2.getId() + " bloqueado com sucesso!");
                            digsub.setPositiveButton("OK", null);
                            digsub.show();
                        } catch (ApiException e) {
                            System.out.println(e);
                        }
                    }
                }); // Fim do Positive Button 2
                dig.setNegativeButton("Não", null);
                dig.show();
            }// Fim do Else Button 2
        }// Fim do If Button 2
    }//Saída

public void onBackPressed(){
        Intent it = new Intent(this, Menu.class);
        startActivity(it);
    }
}
