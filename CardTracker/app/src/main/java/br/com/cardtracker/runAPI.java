package br.com.cardtracker;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

import br.com.conductor.sdc.api.v1.CartaoApi;
import br.com.conductor.sdc.api.v1.ContaApi;
import br.com.conductor.sdc.api.v1.invoker.ApiException;
import br.com.conductor.sdc.api.v1.invoker.ApiInvoker;
import br.com.conductor.sdc.api.v1.model.Cartao;
import br.com.conductor.sdc.api.v1.model.Conta;

public class runAPI extends Activity {

    // Acesso API
    public String access_token = "3BJU7WSdxYVy";
    public String client_id = "VxUGXKTjnPCa";
    private static final String BASE_PATH = "https://api.conductor.com.br/sdc";

    // Atributos API
    public static ContaApi contaApi = new ContaApi();
    public static CartaoApi cartaoApi = new CartaoApi();

    public static Conta conta1 = new Conta();
    public static Cartao cartao1 = new Cartao();
    public static Cartao cartao2 = new Cartao();

    public static Conta conta2 = new Conta();
    public static Cartao cartao3 = new Cartao();
    public static Cartao cartao4 = new Cartao();

    // Atributos GPS
    public static List<Double> latitudeListCompra = new ArrayList<Double>();
    public static List<Double> longitudeListCompra = new ArrayList<Double>();
    public static List<Double> latitudeListTransferencia = new ArrayList<Double>();
    public static List<Double> longitudeListTransferencia = new ArrayList<Double>();
    public static double latitude;
    public static double longitude;

    // Atributos Operação
    public static double valor;
    public static String destino;
    public static String nomeCartao;
    public static String nomeCartaoDestino;
    public static Long IDCartaoDestino;
    public static Long IDCartaoOrigem;
    public static String numeroCartao;
    public static List<String> operacaoCompra = new ArrayList<String>();
    public static List<String> operacaoTransferencia = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_api);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        } //Thread não dar conflito

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},1);
        //ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_NETWORK_STATE},1);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        //ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);

        try {
            runAPI(cartaoApi,contaApi,conta1,cartao1,cartao2,conta2,cartao3,cartao4,access_token,client_id,BASE_PATH);
        } catch (ApiException e) {
            System.out.println(e);
        }
        Intent it = new Intent(this, MainActivity.class);
        startActivity(it);
        finish();
    } //Fim do onCreate

    public void runAPI(CartaoApi cartaoApi, ContaApi contaApi, Conta conta1, Cartao cartao1, Cartao cartao2,
                       Conta conta2, Cartao cartao3, Cartao cartao4, String access_token, String client_id,
                       String BASE_PATH) throws ApiException {

        ApiInvoker.getInstance().addDefaultHeader("access_token", access_token);
        ApiInvoker.getInstance().addDefaultHeader("client_id", client_id);

        contaApi.setBasePath(BASE_PATH);
        setContaApiInfo(contaApi);
        cartaoApi.setBasePath(BASE_PATH);
        setCartaoApiInfo(cartaoApi);
        /**
         * Criando conta 01
         */
        conta1.setNome("Markão Card");
        conta1 = contaApi.createUsingPOST1(conta1);
        System.out.println(conta1);
        setContaInfo(conta1);

        /**
         * Criando o cartão 01 da conta 01
         */
        cartao1.setNome("Stefano Tomei");
        cartao1.setSenha("123123098asd@");
        cartao1.setCvv("101");
        cartao1 = cartaoApi.createUsingPOST(conta1.getId(), cartao1);
        /**
         * Creditando R$ 300.00 no cartao1
         */
        cartaoApi.creditarUsingPUT(conta1.getId(), cartao1.getId(), 300.00);
        setCartao1Info(cartao1);
        /**
         * Criando o cartão 02 da conta 01
         */
        cartao2.setNome("Emmanuel Pinto");
        cartao2.setSenha("123123098asd@");
        cartao2.setCvv("102");
        cartao2 = cartaoApi.createUsingPOST(conta1.getId(), cartao2);
        /**
         * Creditando R$ 100.00 no cartao2
         */
        cartaoApi.creditarUsingPUT(conta1.getId(), cartao2.getId(), 100.00);
        setCartao2Info(cartao2);
        System.out.println("\nCartão 1 " + cartaoApi.getAllUsingGET(conta1.getId()));

        //------------------------------------FIM DA CONTA 1--------------------------------------
        /**
         * Criando conta 02
         */
        conta2.setNome("Monaci Card");
        conta2 = contaApi.createUsingPOST1(conta2);
        System.out.println(conta2);
        setConta2Info(conta2);

        /**
         * Criando o cartão 03 da conta 02
         */
        cartao3.setNome("Anderson Frazão");
        cartao3.setSenha("123123098asd@");
        cartao3.setCvv("103");
        cartao3 = cartaoApi.createUsingPOST(conta2.getId(), cartao3);
        /**
         * Creditando R$ 250.00 no cartao3
         */
        cartaoApi.creditarUsingPUT(conta2.getId(), cartao3.getId(), 250.00);
        setCartao3Info(cartao3);
        /**
         * Criando o cartão 04 da conta 02
         */
        cartao4.setNome("Altair Pinto");
        cartao4.setSenha("123123098asd@");
        cartao4.setCvv("104");
        cartao4 = cartaoApi.createUsingPOST(conta2.getId(), cartao4);
        /**
         * Creditando R$ 120.00 no cartao4
         */
        cartaoApi.creditarUsingPUT(conta2.getId(), cartao4.getId(), 120.00);
        setCartao4Info(cartao4);
        System.out.println("\nCartão 2 " + cartaoApi.getAllUsingGET(conta2.getId()));
    }

    // Get para os valores obtidos pela API
    public ContaApi getContaApiInfos(){return contaApi;}
    public CartaoApi getCartaoApiInfos(){return cartaoApi;}

    public Conta getConta1Infos(){return conta1; }
    public Cartao getCartao1Infos(){return cartao1;}
    public Cartao getCartao2Infos(){return cartao2;}

    public Conta getConta2Infos(){return conta2;}
    public Cartao getCartao3Infos(){return cartao3;}
    public Cartao getCartao4Infos(){return cartao4;}

    // Sets para os valores obtidos pela API

    public void setContaApiInfo(ContaApi contaApi){this.contaApi = contaApi;}
    public void setCartaoApiInfo(CartaoApi cartaoApi){this.cartaoApi = cartaoApi;}

    public void setContaInfo(Conta conta1){this.conta1 = conta1;}
    public void setCartao1Info(Cartao cartao1){this.cartao1 = cartao1;}
    public void setCartao2Info(Cartao cartao2){this.cartao2 = cartao2;}

    public void setConta2Info(Conta conta2){this.conta2 = conta2;}
    public void setCartao3Info(Cartao cartao3){this.cartao3 = cartao3;}
    public void setCartao4Info(Cartao cartao4){this.cartao4 = cartao4;}

    // Get e Set para GPS

    public double getLatitude(){return latitude;}
    public double getLongitude(){return longitude;}

    public List getLatitudeListCompra(){return latitudeListCompra;}
    public List getLongitudeListCompra(){return longitudeListCompra;}
    public List getOperacaoCompra(){return operacaoCompra;}
    public List getOperacaoTransferencia(){return operacaoTransferencia;}

    public List getLatitudeListTransferencia(){return latitudeListTransferencia;}
    public List getLongitudeListTransferencia(){return longitudeListTransferencia;}

    public void setLocalCompra(double latitude, double longitude){this.latitude = latitude; this.longitude=longitude;
        latitudeListCompra.add(latitude);longitudeListCompra.add(longitude);}
    public void setLocalTransferencia(double latitude, double longitude){this.latitude = latitude; this.longitude=longitude;
        latitudeListTransferencia.add(latitude);longitudeListTransferencia.add(longitude);}

    // Get e Set para Dados da Operação Compra
    public String getDestino(){return destino;}
    public double getValor(){return valor;}
    public String getNomeCartao(){return nomeCartao;}
    public String getNumeroCartao(){return numeroCartao;}
    public void setOperacaoCompra(String destino, double valor, String nomeCartao, Long IDCartaoOrigem){
        this.destino = destino; this.valor = valor; this.nomeCartao = nomeCartao; this.IDCartaoOrigem = IDCartaoOrigem;
        operacaoCompra.add("Destino: "+destino+" | Nome do Cartão: "+nomeCartao+" | ID: "+IDCartaoOrigem+" | Valor: R$"+valor);
    }

    // Get e Set para Dados da Operação Transferência
    public String getNomeCartaoDestino(){return nomeCartaoDestino;}
    public Long getIDCartaoOrigem(){return IDCartaoOrigem;}
    public Long getIDCartaoDestino(){return IDCartaoDestino;}
    public void setOperacaoTransferencia(String nomeCartao, Long IDCartaoOrigem,
                                         String nomeCartaoDestino, Long IDCartaoDestino, Double valor){
        this.nomeCartao = nomeCartao; this.IDCartaoOrigem = IDCartaoOrigem; this.nomeCartaoDestino = nomeCartaoDestino;
        this.IDCartaoDestino = IDCartaoDestino; this.valor = valor;
        operacaoTransferencia.add("Nome do Cartão de Origem: "+nomeCartao+" | ID: "+IDCartaoOrigem+" | Nome do Cartão de Destino: "
        +nomeCartaoDestino+" | ID: "+IDCartaoDestino+" | Valor: R$"+valor);
    }

}
