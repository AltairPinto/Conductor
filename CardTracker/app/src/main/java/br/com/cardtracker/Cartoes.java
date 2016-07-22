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

public class Cartoes extends AppCompatActivity implements View.OnClickListener{

    private EditText nID;
    private EditText nConta;
    private Spinner nIDNovo;
    private TextView Cards;

    private Button btnAlterarCartao;
    private Button btnCancelarCartao;
    private Button btnExtratoCartao;
    private Button btnBloqueio;
    private Button btnLimite;


    private EditText nCVVNovo;
    private EditText nNomeNovo;
    private EditText nNumeroNovo;
    private EditText nSenhaNova;


    // Atributos API
    public runAPI runAPI = new runAPI();
    public ContaApi contaApi = runAPI.getContaApiInfos("3BJU7WSdxYVy","VxUGXKTjnPCa","https://api.conductor.com.br/sdc");
    public CartaoApi cartaoApi = runAPI.getCartaoApiInfos("3BJU7WSdxYVy","VxUGXKTjnPCa","https://api.conductor.com.br/sdc");
    public Conta conta1 = runAPI.getConta1Infos();
    public Cartao cartao1 = runAPI.getCartao1Infos();
    public Cartao cartao2 = runAPI.getCartao2Infos();

    public List<Cartao> getAPIFromText;
    public String selectID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartoes);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        } //Thread não dar conflito

        Cards = (TextView) findViewById(R.id.Cards); // Campo para jogar os cartões existentes

        nConta = (EditText) findViewById(R.id.nConta);
        nID = (EditText) findViewById(R.id.nID);
        nCVVNovo = (EditText) findViewById(R.id.nCVVNovo);
        nNomeNovo = (EditText) findViewById(R.id.nNomeNovo);
        nNumeroNovo = (EditText) findViewById(R.id.nNumeroNovo);
        nSenhaNova = (EditText) findViewById(R.id.nSenhaNova);

        nIDNovo = (Spinner) findViewById(R.id.nIDNovo);

        btnAlterarCartao = (Button) findViewById(R.id.btnAlterarCartao);
        btnCancelarCartao = (Button) findViewById(R.id.btnCancelarCartao);
        btnExtratoCartao = (Button) findViewById(R.id.btnExtratoCartao);
        btnBloqueio = (Button) findViewById(R.id.btnBloqueio);
        btnLimite = (Button) findViewById(R.id.btnLimite);

        nConta.setText(conta1.getNome());
        nID.setText(conta1.getId().toString());

        try {
            getAPIFromText = cartaoApi.getAllUsingGET(conta1.getId()); // Pegar todos os cartões da conta
            Cards.setText(getAPIFromText.toString());
        } catch (ApiException e) {
            System.out.println("Deu pau em Cartoes "+e);
        }

        // Criando o Spinner para IDs
        ArrayAdapter<Long> arrayAdapter = new ArrayAdapter<Long>(this,android.R.layout.simple_spinner_item);
        final ArrayAdapter<Long> spinnerArrayAdapter = arrayAdapter;
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        nIDNovo.setAdapter(spinnerArrayAdapter);
        spinnerArrayAdapter.add(cartao1.getId());
        spinnerArrayAdapter.add(cartao2.getId());

        nIDNovo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            // Pegando ID a partir da posição selecionada
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectID = parent.getItemAtPosition(position).toString();
                System.out.println("SelectedID inicial : "+selectID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinnerArrayAdapter.add(null);
            }
        }); //Fim do Spinner

        btnExtratoCartao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Long getLongFromText = new Long(selectID);
                System.out.println("Selected ID PRA PEGAR "+getLongFromText);
                AlertDialog.Builder dig = new AlertDialog.Builder(Cartoes.this);
                try {
                    cartaoApi.extratosUsingPOST(conta1.getId(),getLongFromText);
                    dig.setTitle("Extrato do Cartão ID "+getLongFromText);
                    dig.setMessage("\n"+cartaoApi.extratosUsingPOST(conta1.getId(),getLongFromText));
                    dig.setNeutralButton("OK", null);
                    dig.show();
                } catch (ApiException e) {
                    e.printStackTrace();
                }

            }
        });

        btnAlterarCartao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Long getLongFromText = new Long(selectID);
                final int getCartao1ID = new Integer(cartao1.getId().toString());

                AlertDialog.Builder dig = new AlertDialog.Builder(Cartoes.this);
                System.out.println("Selected ID FINAL: "+selectID);

                if (getLongFromText == getCartao1ID) {
                    cartao1.setCvv(nCVVNovo.getText().toString());
                    cartao1.setNome(nNomeNovo.getText().toString());
                    cartao1.setNumero(nNumeroNovo.getText().toString());
                    cartao1.setSenha(nSenhaNova.getText().toString());
                    try {
                        cartaoApi.updateUsingPUT(conta1.getId(), cartao1);//id conta e objeto cartao}
                        dig.setTitle("Confirmação");
                        dig.setMessage("\nCartão de ID "+getLongFromText+" alterado com sucesso!");
                        dig.setNeutralButton("OK", null);
                        dig.show();
                    } catch (ApiException e) {
                        e.printStackTrace();
                    }

                }else {
                    cartao2.setCvv(nCVVNovo.getText().toString());
                    cartao2.setNome(nNomeNovo.getText().toString());
                    cartao2.setNumero(nNumeroNovo.getText().toString());
                    cartao2.setSenha(nSenhaNova.getText().toString());
                    try {
                        cartaoApi.updateUsingPUT(conta1.getId(), cartao2);//id conta e objeto cartao}
                        dig.setTitle("Confirmação");
                        dig.setMessage("\nCartão de ID "+getLongFromText+" alterado com sucesso!");
                        dig.setNeutralButton("OK", null);
                        dig.show();
                    } catch (ApiException e) {
                        e.printStackTrace();
                    }
                } //Fim do if/

                //Saída
                try {
                    getAPIFromText = cartaoApi.getAllUsingGET(conta1.getId()); // Pegar todos os cartões da conta
                    Cards.setText(getAPIFromText.toString());
                } catch (ApiException e) {
                    System.out.println("Deu pau em Cartoes "+e);
                }
            }// Fim do onClick
        });

        btnBloqueio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Long getLongFromText = new Long(selectID);
                AlertDialog.Builder dig = new AlertDialog.Builder(Cartoes.this);
                System.out.println("Selected ID Bloqueio: "+selectID);

                try {
                    cartaoApi.bloquearUsingPUT(conta1.getId(),getLongFromText);
                    dig.setTitle("Bloqueado");
                    dig.setMessage("\nCartão de ID "+selectID+" bloqueado com sucesso!");
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
                    Cards.setText(getAPIFromText.toString());
                } catch (ApiException e) {
                    System.out.println("Deu pau em Cartoes "+e);
                }
            }
        });

        btnLimite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Long getLongFromText = new Long(selectID);
                AlertDialog.Builder dig = new AlertDialog.Builder(Cartoes.this);
                System.out.println("Selected ID Bloqueio: "+selectID);

                try {
                    dig.setTitle("Limite");
                    dig.setMessage("\nCartão ID "+getLongFromText+" : "+cartaoApi.limiteUsingGET(conta1.getId(), getLongFromText));
                    dig.setNeutralButton("OK", null);
                    dig.show();
                } catch (ApiException e) {
                    dig.setTitle("Erro");
                    dig.setMessage("\n"+e);
                    dig.setNeutralButton("Voltar", null);
                    dig.show();
                }
            }
        });

        btnCancelarCartao.setOnClickListener(this); // Cancelamento do Cartão
    }

    @Override
    public void onClick(View v) {
        final Long getLongFromText = new Long(selectID);
        AlertDialog.Builder dig5 = new AlertDialog.Builder(Cartoes.this);

            try {
                cartaoApi.cancelarUsingDELETE(conta1.getId(),getLongFromText);
                dig5.setTitle("Confirmação");
                dig5.setMessage("\nCartão cancelado com sucesso");
                dig5.setNeutralButton("OK", null);
                dig5.show();
            } catch (ApiException e) {
                dig5.setTitle("Aviso");
                dig5.setMessage("\nID não identificado, por favor, insira um ID válido");
                dig5.setNeutralButton("Voltar", null);
                dig5.show();
            }
        //Saída
        try {
            getAPIFromText = cartaoApi.getAllUsingGET(conta1.getId()); // Pegar todos os cartões da conta
            Cards.setText(getAPIFromText.toString());
        } catch (ApiException e) {
            System.out.println("Deu pau em Cartoes "+e);
        }
    }

    public void onBackPressed(){
        Intent it = new Intent(this, Menu.class);
        startActivity(it);
    }
}