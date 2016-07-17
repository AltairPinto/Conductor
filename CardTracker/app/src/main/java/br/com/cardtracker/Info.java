package br.com.cardtracker;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Info extends AppCompatActivity implements View.OnClickListener{

    private Button btExtrato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        btExtrato = (Button) findViewById(R.id.btExtrato);
        btExtrato.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        AlertDialog.Builder dig = new AlertDialog.Builder(Info.this);
        dig.setMessage("data: Wed Jul 13 21:45:27 GFT 2016\n" +
                "    tipo: CREDITO\n" +
                "    valor: 100.0\n " +
                "   \ndata: Wed Jul 13 21:45:28 GFT 2016\n" +
                "    tipo: DEBITO\n" +
                "    valor: 0.1");
        dig.setNeutralButton("Ok",null);
        dig.show();
    }

    public void onBackPressed(){
        Intent it = new Intent(this, Menu.class);
        startActivity(it);
    }
}
