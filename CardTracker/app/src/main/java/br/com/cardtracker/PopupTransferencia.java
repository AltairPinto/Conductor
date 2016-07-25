package br.com.cardtracker;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
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
 * Created by altai on 22/07/2016.
 */
public class PopupTransferencia extends Activity implements View.OnClickListener{

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

    public Conta conta2 = runAPI.getConta2Infos();
    public Cartao cartao3 = runAPI.getCartao3Infos();
    public Cartao cartao4 = runAPI.getCartao4Infos();

    public Double valor;
    public Long IDContaOrg;
    public Long IDCartaoOrg;
    public String NomeCartaoOrg;
    public Long IDContaVar;
    public Long IDCartaoVar;
    public String NomeCartaoVar;
    public Long CODValidar;
    public String destino;

    public Long getLongFromText;

    //GPS
    private Location location;
    private LocationManager locationManager;
    double latitude;
    double longitude;

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
                IDContaVar = params.getLong("IDContaVar");
                IDCartaoVar = params.getLong("IDCartaoVar");
                NomeCartaoVar = params.getString("NomeCartaoVar");
                CODValidar = params.getLong("CODValidar");
                destino = params.getString("destino");
            }} // Fim do Intent

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dig = new AlertDialog.Builder(PopupTransferencia.this);
                try {
                    getLongFromText = new Long(editText.getText().toString());
                } catch (NumberFormatException ene) {
                    dig.setTitle("Código inválido");
                    dig.setMessage("\nPreencha o campo em branco");
                    dig.setNegativeButton("Voltar", null);
                    dig.show();
                }

                //Tratamento para código
                if (CODValidar.equals(getLongFromText)) {
                    //Validado
                    // Efetuando a transferência
                    try {
                        System.out.println("Verificado");
                        cartaoApi.transferirUsingPOST(IDContaOrg, IDCartaoOrg, IDCartaoVar, valor);
                        dig.setTitle("Informações");
                        gpsSetLocal(); //Captura a localização
                        // Impressão de informações da Transferência
                        dig.setMessage("De: " + NomeCartaoOrg + " | Cartão ID: " + IDCartaoOrg +
                                "\nPara: " + NomeCartaoVar + " | Cartão ID: " + IDCartaoVar +
                                "\nValor: R$" + valor+
                                "\nLatitude: "+latitude+" Longitude: "+longitude);
                        dig.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
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
                }// Fim da Tela de Transferência
            }); // Fim

        btnCancelar.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        AlertDialog.Builder dig = new AlertDialog.Builder(PopupTransferencia.this);

            dig.setTitle("Cancelamento");
            dig.setMessage("\nCancelar transferência?");
            dig.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AlertDialog.Builder digsub = new AlertDialog.Builder(PopupTransferencia.this);

                    digsub.setTitle("Cancelada");
                    digsub.setMessage("\nTransferência cancelada");
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

    private void gpsSetLocal() {
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

        }else{
            locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        System.out.println("Latitude Transferência: "+latitude);
        System.out.println("Longitude Transferência: "+longitude);
        runAPI.setLocalTransferencia(latitude,longitude);
        runAPI.setOperacaoTransferencia(NomeCartaoOrg,IDCartaoOrg,NomeCartaoVar,IDCartaoVar,valor);
    }
}
