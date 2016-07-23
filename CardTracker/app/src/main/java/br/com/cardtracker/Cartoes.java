package br.com.cardtracker;

import android.content.DialogInterface;
import android.content.Intent;
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

public class Cartoes extends AppCompatActivity implements View.OnClickListener{

    private EditText nID;
    private EditText nConta;

    private Button btnCancelarCartao;
    private Button btnBloquearCartao;
    private Button btnDesbloquearCartao;

    private RadioButton radioButtonCartao1;
    private RadioButton radioButtonCartao2;

    // Atributos API
    public runAPI runAPI = new runAPI();
    public ContaApi contaApi = runAPI.getContaApiInfos();///("3BJU7WSdxYVy","VxUGXKTjnPCa","https://api.conductor.com.br/sdc");
    public CartaoApi cartaoApi = runAPI.getCartaoApiInfos();//("3BJU7WSdxYVy","VxUGXKTjnPCa","https://api.conductor.com.br/sdc");
    public Conta conta1 = runAPI.getConta1Infos();
    public Cartao cartao1 = runAPI.getCartao1Infos();
    public Cartao cartao2 = runAPI.getCartao2Infos();

    public List<Cartao> getAPIFromText;

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

        btnCancelarCartao = (Button) findViewById(R.id.btnCancelarCartao);
        btnBloquearCartao = (Button) findViewById(R.id.btnBloquearCartao);
        btnDesbloquearCartao = (Button) findViewById(R.id.btnDesbloquearCartao);

        radioButtonCartao1 = (RadioButton) findViewById(R.id.radioButtonCartao1);
        radioButtonCartao2 = (RadioButton) findViewById(R.id.radioButtonCartao2);

        nConta.setText(conta1.getNome());
        nID.setText(conta1.getId().toString());

        try {
            getAPIFromText = cartaoApi.getAllUsingGET(conta1.getId()); // Pegar todos os cartões da conta
            radioButtonCartao1.setText(cartao1.getNumero());
            radioButtonCartao2.setText(cartao2.getNumero());
        } catch (ApiException e) {
            System.out.println("Deu pau em Cartoes "+e);
        }

        btnBloquearCartao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dig = new AlertDialog.Builder(Cartoes.this);
                System.out.println("VALOR STATUS Cartao 1: "+cartao1.getStatus());
                System.out.println("VALOR STATUS Cartao 2: "+cartao2.getStatus());

                if (radioButtonCartao1.isChecked()) {
                    if(cartao1.getStatus()==Cartao.StatusEnum.BLOQUEADO){
                        dig.setTitle("Erro");
                        dig.setMessage("Cartão "+cartao1.getNumero()+" já está bloqueado");
                        dig.setPositiveButton("Voltar",null);
                        dig.show();
                    }else if(cartao1.getStatus()==Cartao.StatusEnum.CANCELADO){
                        dig.setTitle("Erro");
                        dig.setMessage("Cartão "+cartao1.getNumero()+" está cancelado");
                        dig.setPositiveButton("Voltar",null);
                        dig.show();
                    }else {
                        dig.setTitle("Confirmação");
                        dig.setMessage("Confirmar bloqueio do cartão " + cartao1.getNumero() + " ?");
                        dig.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AlertDialog.Builder digsub = new AlertDialog.Builder(Cartoes.this);
                                try {
                                    cartaoApi.bloquearUsingPUT(conta1.getId(),cartao1.getId());
                                    // Retorna como bloqueado no JSON mas não altera o objeto Cartão 1
                                    cartao1.setStatus(Cartao.StatusEnum.BLOQUEADO);
                                    System.out.println("Status pós-bloqueio "+cartao1.getStatus());
                                    digsub.setTitle("Bloqueado");
                                    digsub.setMessage("\nCartão de ID " + cartao1.getId() + " bloqueado com sucesso!");
                                    digsub.setPositiveButton("OK", null);
                                    digsub.show();
                                } catch (ApiException e) {
                                    System.out.println(e);
                                }
                            }
                        }); // Fim do Positive Button 1
                        dig.setNegativeButton("Não", null);
                        dig.show();
                    }// Fim do Else Button 1w
                } //Fim do if Button 1

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
                                AlertDialog.Builder digsub = new AlertDialog.Builder(Cartoes.this);
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
        }); // Fim Bloqueio do Cartão

        btnDesbloquearCartao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dig = new AlertDialog.Builder(Cartoes.this);

                if (radioButtonCartao1.isChecked()) {
                    if(cartao1.getStatus()==Cartao.StatusEnum.ATIVO){
                        dig.setTitle("Erro");
                        dig.setMessage("Cartão "+cartao1.getNumero()+" não está bloqueado.");
                        dig.setPositiveButton("Voltar",null);
                        dig.show();
                    }else if (cartao1.getStatus()==Cartao.StatusEnum.CANCELADO) {
                        dig.setTitle("Erro");
                        dig.setMessage("Cartão "+cartao1.getNumero()+" está cancelado.");
                        dig.setPositiveButton("Voltar",null);
                        dig.show();
                    }else{
                        dig.setTitle("Confirmação");
                        dig.setMessage("Confirmar desbloqueio do cartão " + cartao1.getNumero() + " ?");
                        dig.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AlertDialog.Builder digsub = new AlertDialog.Builder(Cartoes.this);
                                try {
                                    cartaoApi.desbloquearUsingPUT(conta1.getId(), cartao1.getId());
                                    System.out.println("Status pós desbloq "+cartao1.getStatus());
                                    cartao1.setStatus(Cartao.StatusEnum.ATIVO);
                                    System.out.println("Com Force: "+cartao1.getStatus());
                                    digsub.setTitle("Desbloqueado");
                                    digsub.setMessage("\nCartão de ID " + cartao1.getId() + " desbloqueado com sucesso!");
                                    digsub.setPositiveButton("OK", null);
                                    digsub.show();
                                } catch (ApiException e) {
                                    System.out.println(e);
                                }
                            }
                        }); // Fim do Positive Button 1
                        dig.setNegativeButton("Não", null);
                        dig.show();
                    }// Fim do Else Button 1
                } //Fim do if Button 1

                if(radioButtonCartao2.isChecked()){
                    if(cartao2.getStatus()==Cartao.StatusEnum.ATIVO){
                        dig.setTitle("Erro");
                        dig.setMessage("Cartão "+cartao2.getNumero()+" não está bloqueado.");
                        dig.setPositiveButton("Voltar",null);
                        dig.show();
                    }else if (cartao2.getStatus()==Cartao.StatusEnum.CANCELADO) {
                        dig.setTitle("Erro");
                        dig.setMessage("Cartão "+cartao2.getNumero()+" está cancelado.");
                        dig.setPositiveButton("Voltar",null);
                        dig.show();
                    }else {
                        dig.setTitle("Confirmação");
                        dig.setMessage("Confirmar desbloqueio do cartão " + cartao2.getNumero() + " ?");
                        dig.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AlertDialog.Builder digsub = new AlertDialog.Builder(Cartoes.this);
                                try {
                                    cartaoApi.desbloquearUsingPUT(conta1.getId(), cartao2.getId());
                                    cartao2.setStatus(Cartao.StatusEnum.ATIVO);
                                    digsub.setTitle("Desbloqueado");
                                    digsub.setMessage("\nCartão de ID " + cartao2.getId() + " desbloqueado com sucesso!");
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
            }
        }); // Fim Desbloqueio do Cartão

        btnCancelarCartao.setOnClickListener(this); // Cancelamento do Cartão
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder dig = new AlertDialog.Builder(Cartoes.this);

        if (radioButtonCartao1.isChecked()) {
            if(cartao1.getStatus()==Cartao.StatusEnum.CANCELADO){
                dig.setTitle("Erro");
                dig.setMessage("Cartão "+cartao1.getNumero()+" já está cancelado");
                dig.setPositiveButton("Voltar",null);
                dig.show();
            }else {
                dig.setTitle("Confirmação");
                dig.setMessage("Confirmar cancelamento do cartão " + cartao1.getNumero() + " ?");
                dig.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder digsub = new AlertDialog.Builder(Cartoes.this);
                        try {
                            cartaoApi.cancelarUsingDELETE(conta1.getId(), cartao1.getId());
                            cartao1.setStatus(Cartao.StatusEnum.CANCELADO);
                            digsub.setTitle("Cancelado");
                            digsub.setMessage("\nCartão de ID " + cartao1.getId() + " cancelado com sucesso!");
                            digsub.setPositiveButton("OK", null);
                            digsub.show();
                        } catch (ApiException e) {
                            System.out.println(e);
                        }
                    }
                }); // Fim do Positive Button 1
                dig.setNegativeButton("Não", null);
                dig.show();
            }//Fim do Else Button 1
        } //Fim do if Button 1

        if(radioButtonCartao2.isChecked()){
            if(cartao2.getStatus()==Cartao.StatusEnum.CANCELADO){
                dig.setTitle("Erro");
                dig.setMessage("Cartão "+cartao2.getNumero()+" já está cancelado");
                dig.setPositiveButton("Voltar",null);
                dig.show();
            }else {
                dig.setTitle("Confirmação");
                dig.setMessage("Confirmar cancelamento do cartão " + cartao2.getNumero() + " ?");
                dig.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder digsub = new AlertDialog.Builder(Cartoes.this);
                        try {
                            cartaoApi.cancelarUsingDELETE(conta1.getId(), cartao2.getId());
                            cartao2.setStatus(Cartao.StatusEnum.CANCELADO);
                            digsub.setTitle("Cancelado");
                            digsub.setMessage("\nCartão de ID " + cartao2.getId() + " cancelado com sucesso!");
                            digsub.setPositiveButton("OK", null);
                            digsub.show();
                        } catch (ApiException e) {
                            System.out.println(e);
                        }
                    }
                }); // Fim do Positive Button 2
                dig.setNegativeButton("Não", null);
                dig.show();
            }//Fim do Else Button 2
        }// Fim do If Button 2
        //Saída
    }

    public void onBackPressed(){
        Intent it = new Intent(this, Menu.class);
        startActivity(it);
    }
}