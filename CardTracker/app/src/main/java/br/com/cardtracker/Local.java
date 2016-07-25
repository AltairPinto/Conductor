package br.com.cardtracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class Local extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private runAPI runAPI = new runAPI();
    private List<String> operacaoCompra = runAPI.getOperacaoCompra();
    private List<Double> latitudeCompra = runAPI.getLatitudeListCompra();
    private List<Double> longitudeCompra = runAPI.getLongitudeListCompra();
    private List<String> operacaoTransferencia = runAPI.getOperacaoTransferencia();
    private List<Double> latitudeTransferencia = runAPI.getLatitudeListTransferencia();
    private List<Double> longitudeTransferencia = runAPI.getLongitudeListTransferencia();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        } //Thread não dar conflito

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    //eu de novo
    protected boolean isRouteDisplayed() {
        return false;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        // Mover a câmera para o último local onde houve operação financeira
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(runAPI.getLatitude(), runAPI.getLongitude()), 16));

        /* DADOS ESTÁTICOS DE LOCALIZAÇÃO
        // Conductor
        googleMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher))
                .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                .position(new LatLng(-7.1124143, -34.8433881)));
       // Markão
        googleMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher))
                .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                .position(new LatLng(-7.0978555, -34.8387047)));
        // Monaci
        googleMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher))
                .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                .position(new LatLng(-7.1756637, -34.8760612)));*/

        // Compra feita MARCAÇÃO INDIVIDUAL
        /*googleMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher))
                .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                .position(new LatLng(runAPI.getLatitude(),runAPI.getLongitude())));*/

        // Percorrer listas de posição de Compras
        try {
            for (int i = 0; i < latitudeCompra.size(); i++) {
                for (int j = 0; j < longitudeCompra.size(); j++) {
                    for (int k = 0; k < operacaoCompra.size(); k++) {
                        googleMap.addMarker(new MarkerOptions().title("Compra")
                                .snippet(operacaoCompra.get(k))
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher))
                                .anchor(1.0f, 0.0f) // Anchors the marker on the bottom left
                                .position(new LatLng(latitudeCompra.get(i), longitudeCompra.get(j))));
                    }
                }
            }//Fim da lista de Compras
            // lista de Transferencias
            for (int i = 0; i < latitudeTransferencia.size();i++){
                for (int j = 0; j < latitudeTransferencia.size(); j++) {
                    for (int k = 0; k < operacaoTransferencia.size(); k++) {
                        googleMap.addMarker(new MarkerOptions().title("Transferência")
                                .snippet(operacaoTransferencia.get(k))
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher))
                                .anchor(1.0f, 0.0f) // Anchors the marker on the bottom left
                                .position(new LatLng(latitudeTransferencia.get(i), longitudeTransferencia.get(j))));
                    }
                }
            }
        }catch (NullPointerException e){
            AlertDialog.Builder dig = new AlertDialog.Builder(Local.this);
            dig.setTitle("Erro");
            dig.setMessage("Nenhuma operação financeira realizada");
            dig.setNegativeButton("Voltar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent it = new Intent(Local.this, Menu.class);
                    startActivity(it);
                }
            });
            dig.show();


        }

    }// Fim do Main


    public void onBackPressed(){
        Intent it = new Intent(this, Menu.class);
        startActivity(it);
    }
}
