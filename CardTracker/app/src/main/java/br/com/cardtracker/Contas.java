package br.com.cardtracker;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import br.com.conductor.sdc.api.v1.ContaApi;
import br.com.conductor.sdc.api.v1.invoker.ApiException;
import br.com.conductor.sdc.api.v1.model.Conta;

public class Contas extends AppCompatActivity implements View.OnClickListener{

    private EditText nConta;
    private EditText nID;
    private EditText nContaNovo;
    private EditText nIDNovo;
    private Button btnAlterarConta;
    private Button btnDeletarConta;


    // Atributos API
    public runAPI runAPI = new runAPI();
    public ContaApi contaApi = runAPI.getContaApiInfos("3BJU7WSdxYVy","VxUGXKTjnPCa","https://api.conductor.com.br/sdc");
    public Conta conta1 = runAPI.getConta1Infos();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contas);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        } //Thread não dar conflito

        nConta = (EditText) findViewById(R.id.nConta);
        nID = (EditText) findViewById(R.id.nID);
        nContaNovo = (EditText) findViewById(R.id.nContaNovo);
        nIDNovo = (EditText) findViewById(R.id.nIDNovo);
        btnAlterarConta = (Button) findViewById(R.id.btnAlterarConta);
        btnDeletarConta = (Button) findViewById(R.id.btnDeletarConta);

        try {
            contaApi.getOneUsingGET1(conta1.getId());
            System.out.println("Get da Conta1 em Contas: "+contaApi.getOneUsingGET1(conta1.getId()));
            nConta.setText(conta1.getNome());
            nID.setText(conta1.getId().toString());
            nIDNovo.setText(conta1.getId().toString());
        } catch (ApiException e) {
            System.out.println("Deu pau em Contas "+e);
        }

        btnAlterarConta.setOnClickListener(this);

        btnDeletarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dig = new AlertDialog.Builder(Contas.this);
                try {
                    contaApi.deleteUsingDELETE(conta1.getId()); //Uso da API para atualizar conta this.
                    dig.setMessage("Conta deletada com sucesso! Voltando a tela inicial");
                    dig.show();
                    System.out.println("Dados da conta apagada: "+conta1);
                    Intent it1 = new Intent(Contas.this, runAPI.class);
                    startActivity(it1);
                } catch (ApiException e) {
                    dig.setMessage("Conta não encontrada");
                    dig.setNeutralButton("Voltar",null);
                    nConta.setText(conta1.getNome());
                    nID.setText(conta1.getId().toString());
                    dig.show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {

        AlertDialog.Builder dig = new AlertDialog.Builder(Contas.this);
        final Long getLongFromText = new Long(nIDNovo.getText().toString());
        //conta1.setNome(nContaNovo.getText().toString());
        //System.out.println("Novo nome da conta: "+conta1.getNome());

        try {
            conta1.setId(getLongFromText);
            System.out.println("Novo Nome Conta: "+conta1.getNome());
            System.out.println("Novo ID Conta: "+conta1.getId());
        }catch (NumberFormatException ene) {
            dig.setMessage("Digite valores válidos para que o ID seja alterado");
            dig.setNegativeButton("Voltar", null);
            dig.show();
            nIDNovo.setText(null);
        }

        if((nContaNovo.getText().toString()).equals("")){
            dig.setMessage("Digite valores válidos para que o NOME DA CONTA seja alterado");
            dig.setNegativeButton("Voltar", null);
            dig.show();
            nContaNovo.setText(null);
        }else{
            conta1.setNome(new String(nContaNovo.getText().toString()));
        }
        System.out.println("Novo Nome: "+conta1.getNome());

        try {
            contaApi.updateUsingPUT1(conta1); //Uso da API para atualizar conta this.
            dig.setMessage("Dados alterados com sucesso! Novos dados: \n"+this.conta1);
            dig.setNeutralButton("Ok",null);
            dig.show();
        } catch (ApiException e) {
            dig.setMessage("Conta não encontrada "+e);
            dig.setNeutralButton("Voltar",null);
            nConta.setText(conta1.getNome());
            nID.setText(conta1.getId().toString());
            dig.show();
        }
        // Saída
        nConta.setText(conta1.getNome());
        nID.setText(conta1.getId().toString());
    }

    public void onBackPressed(){
        Intent it = new Intent(this, Menu.class);
        startActivity(it);
    }

}
