package br.com.cardtracker;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import br.com.conductor.sdc.api.v1.CartaoApi;
import br.com.conductor.sdc.api.v1.ContaApi;
import br.com.conductor.sdc.api.v1.invoker.ApiException;
import br.com.conductor.sdc.api.v1.model.Cartao;
import br.com.conductor.sdc.api.v1.model.Conta;

/**
 * Created by altai on 24/07/2016.
 */
public class PopupCompra extends Activity implements View.OnClickListener{

    private Button btnOk;
    private Button btnCancelar;

    private EditText editText;

    // Atributos API
    public runAPI runAPI = new runAPI();
    public ContaApi contaApi = runAPI.getContaApiInfos();
    public CartaoApi cartaoApi = runAPI.getCartaoApiInfos();

    public Conta conta1 = runAPI.getConta1Infos();
    public Cartao cartao1 = runAPI.getCartao1Infos();
    public Cartao cartao2 = runAPI.getCartao2Infos();

    public Double valor;
    public Long IDContaOrg;
    public Long IDCartaoOrg;
    public String NomeCartaoOrg;
    public Long CODValidar;
    public String destino;

    public int getIntFromText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_window);

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        } //Thread não dar conflito

        btnOk = (Button)findViewById(R.id.btnOk);
        btnCancelar = (Button)findViewById(R.id.btnCancelar);

        editText = (EditText) findViewById(R.id.editText);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int heigh = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(heigh*.4));

        // Recebimento de dados das outras telas
        Intent intent = getIntent();
        if(intent!=null){
            Bundle params = intent.getExtras();
            if(params!=null) {
                // Recebendo valores de Transferência
                valor = params.getDouble("valor");
                IDContaOrg = params.getLong("IDContaOrg");
                IDCartaoOrg = params.getLong("IDCartaoOrg");
                NomeCartaoOrg = params.getString("NomeCartaoOrg");
                CODValidar = params.getLong("CODValidar");
                destino = params.getString("destino");
            }} // Fim do Intent

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dig = new AlertDialog.Builder(PopupCompra.this);
                try {
                    getIntFromText = new Integer(editText.getText().toString());
                } catch (NumberFormatException ene) {
                    dig.setTitle("Código inválido");
                    dig.setMessage("\nPreencha o campo em branco");
                    dig.setNegativeButton("Voltar", null);
                    dig.show();
                }

                //Tratamento para Tela de Compra
                if (getIntFromText == CODValidar) {
                        //Validado
                        // Efetuando a Compra
                        try {
                            System.out.println("Verificado");
                            cartaoApi.transacionarUsingPUT(IDContaOrg, IDCartaoOrg, valor);
                            dig.setTitle("Compra");
                            // Impressão de informações da Compra
                            dig.setMessage("\nDe: " + NomeCartaoOrg + " | Cartão ID: " + IDCartaoOrg +
                                    "\nDestino: " + destino +"\nValor: R$" + valor);
                            dig.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                   GPSTracker gps = new GPSTracker(PopupCompra.this);

// checa se o GPS está habilitado
                                    if(gps.canGetLocation()){
                                        double latitude = gps.getLatitude();
                                        double longitude = gps.getLongitude();
                                    }else{
// não pôde pegar a localização
// GPS ou a Rede não está habilitada
// Pergunta ao usuário para habilitar GPS/Rede em configurações
                                        gps.showSettingsAlert();
                                    }
                                    finish();
                                }
                            });
                            dig.show();
                        } catch (ApiException e) {
                            dig.setTitle("Erro");
                            dig.setMessage("\n" + e);
                            dig.setNegativeButton("Voltar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                            dig.show();
                        }
                        //}} //Fim do intent
                    } else {
                        dig.setTitle("Erro");
                        dig.setMessage("\nCódigo inválido");
                        dig.setNegativeButton("Voltar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                editText.setText(null);
                            }
                        });
                        dig.show();
                    }
                }// Fim da Tela de Compra
        }); // Fim

        btnCancelar.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        AlertDialog.Builder dig = new AlertDialog.Builder(PopupCompra.this);

            dig.setTitle("Cancelamento");
            dig.setMessage("\nCancelar Compra?");
            dig.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AlertDialog.Builder digsub = new AlertDialog.Builder(PopupCompra.this);

                    digsub.setTitle("Cancelada");
                    digsub.setMessage("\nCompra cancelada");
                    digsub.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    digsub.show();
                }
            });//Fim positive
            dig.setNegativeButton("Não", null);
            dig.show();
    }
}
