package br.com.cardtracker;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

/**
 * Created by altai on 22/07/2016.
 */
public class Pop extends Activity{

    private Button Sim;
    private Button Nao;

    public static int verifica;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popouwindow);

        Sim = (Button)findViewById(R.id.Sim);
        Nao = (Button)findViewById(R.id.Nao);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int heigh = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(heigh*.4));

        Sim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifica=1;
                setPress(verifica);
                System.out.println("verificar caso sim : "+verifica);
                finish();
            }
        });
        Nao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifica=2;
                setPress(verifica);
                System.out.println("verificar caso nao : "+verifica);
                finish();
            }
        });
    }
    public int getPress(){System.out.println("valor retornado : "+verifica);return verifica;}
    public void setPress(int verifica){this.verifica = verifica;}
}
