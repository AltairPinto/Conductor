package br.com.cardtracker;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;

import br.com.conductor.sdc.api.v1.CartaoApi;
import br.com.conductor.sdc.api.v1.ContaApi;
import br.com.conductor.sdc.api.v1.invoker.ApiException;
import br.com.conductor.sdc.api.v1.invoker.ApiInvoker;
import br.com.conductor.sdc.api.v1.model.Cartao;
import br.com.conductor.sdc.api.v1.model.Conta;

public class runAPI extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_api);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        } //Thread não dar conflito

        try {
            runAPI(cartaoApi,contaApi,conta1,cartao1,cartao2,access_token,client_id,BASE_PATH);
        } catch (ApiException e) {
            System.out.println("Deu pau no runAPI"+e);
        }
        Intent it = new Intent(this, MainActivity.class);
        startActivity(it);
        finish();
    } //Fim do onCreate

    public void runAPI(CartaoApi cartaoApi, ContaApi contaApi, Conta conta1, Cartao cartao1, Cartao cartao2,
                              String access_token, String client_id, String BASE_PATH) throws ApiException {

        ApiInvoker.getInstance().addDefaultHeader("access_token", access_token);
        ApiInvoker.getInstance().addDefaultHeader("client_id", client_id);

        contaApi.setBasePath(BASE_PATH);
        getContaApiInfos(access_token,client_id,BASE_PATH);
        cartaoApi.setBasePath(BASE_PATH);
        getCartaoApiInfos(access_token,client_id,BASE_PATH);

        /**
         * Criando conta 01
         */
        conta1.setNome("NOME CONTA 1");
        conta1 = contaApi.createUsingPOST1(conta1);
        System.out.println(conta1);
        //contaInfos(conta1.getNome(),conta1.getId()); //Capturar os valores da Contas
        setContaInfo(conta1);

        /**
         * Criando o cartão 01 da conta 01
         */
        cartao1.setNome("NOME DO CARTAO");
        cartao1.setSenha("123123098asd@");
        cartao1.setCvv("cvv");
        cartao1 = cartaoApi.createUsingPOST(conta1.getId(), cartao1);
        //cartao1Infos(cartao1.getCvv(),cartao1.getId(),cartao1.getNome(),cartao1.getNumero(),cartao1.getSenha(),cartao1.getStatus());
        setCartao1Info(cartao1);
        /**
         * Criando o cartão 02 da conta 01
         */
        cartao2.setNome("NOME DO CARTAO");
        cartao2.setSenha("123123098asd@");
        cartao2.setCvv("cvv");
        cartao2 = cartaoApi.createUsingPOST(conta1.getId(), cartao2);
        //cartao2Infos(cartao2.getCvv(),cartao2.getId(),cartao2.getNome(),cartao2.getNumero(),cartao2.getSenha(),cartao2.getStatus());
        setCartao2Info(cartao2);

        System.out.println("\n" + cartaoApi.getAllUsingGET(conta1.getId()));
    }

    // Get para os valores obtidos pela API
    public ContaApi getContaApiInfos(String access_token, String client_id, String BASE_PATH){
        ApiInvoker.getInstance().addDefaultHeader("access_token", access_token);
        ApiInvoker.getInstance().addDefaultHeader("client_id", client_id);
        contaApi.setBasePath(BASE_PATH);
        return contaApi;}
    public CartaoApi getCartaoApiInfos(String access_token, String client_id, String BASE_PATH){
        ApiInvoker.getInstance().addDefaultHeader("access_token", access_token);
        ApiInvoker.getInstance().addDefaultHeader("client_id", client_id);
        cartaoApi.setBasePath(BASE_PATH);
        return cartaoApi;}
    public Conta getConta1Infos(){
        System.out.println("CONTA 1 RECEBEU ID: "+conta1.getId());
        return conta1; }
    public Cartao getCartao1Infos(){return cartao1;}
    public Cartao getCartao2Infos(){return cartao2;}

    // Sets para os valores obtidos pela API

    public void setContaApiInfo(ContaApi contaApi){this.contaApi = contaApi;}
    public void setCartaoApiInfo(CartaoApi cartaoApi){this.cartaoApi = cartaoApi;}
    public void setContaInfo(Conta conta1){this.conta1 = conta1;}
    public void setCartao1Info(Cartao cartao1){this.cartao1 = cartao1;}
    public void setCartao2Info(Cartao cartao2){this.cartao2 = cartao2;}

    /* public void contaInfos(String nome, Long id){
        conta1.setNome(nome);
        conta1.setId(id);
     }*/
    /*public void cartao1Infos(String cvv, Long id, String nome, String numero, String senha, Cartao.StatusEnum status){
        cartao1.setCvv(cvv);
        cartao1.setId(id);
        cartao1.setNome(nome);
        cartao1.setNumero(numero);
        cartao1.setSenha(senha);
        cartao1.setStatus(status);
      }*/
    /*public void cartao2Infos(String cvv, Long id, String nome, String numero, String senha, Cartao.StatusEnum status){
        cartao2.setCvv(cvv);
        cartao2.setId(id);
        cartao2.setNome(nome);
        cartao2.setNumero(numero);
        cartao2.setSenha(senha);
        cartao2.setStatus(status);
    }*/



}
