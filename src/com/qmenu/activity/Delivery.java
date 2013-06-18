package com.qmenu.activity;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.maps.MapActivity;
import com.qmenu.R;

public class Delivery extends MapActivity 
{    
	TextView locationTXT;
	Button btLocalizacao;
	LocationManager manager;
	LocationListener listener;
	EditText edCEP;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivery);
        Button btVoltar = (Button) findViewById(R.id.btVoltar);

        btVoltar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();			
			}
		});
        
        Button btBuscaCEP = (Button) findViewById(R.id.btBuscaCEP);
        
        btBuscaCEP.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		startActivityForResult(new Intent(v.getContext(), BuscaCEP.class), 0);				
        	}
        });


        locationTXT = (TextView) findViewById(R.id.myLocationText);

        btLocalizacao = (Button) findViewById(R.id.btLocalizacao);
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new LocationListener(){
			public void onLocationChanged(Location location) {
				if(location != null){
	                String latLongString = "";
	                URL url = null;
					try {
						url = new URL("http://maps.googleapis.com/maps/api/geocode/xml?sensor=false&latlng=" + location.getLatitude() + "," + location.getLongitude());
					} catch (MalformedURLException e) {
						System.out.println("Sem acesso ao servidor...");
						latLongString = "Impossível acessar o servidor, verifique a conexão de internet do seu aparelho";
					}
	                BufferedReader in = null;
					try {
						in = new BufferedReader(	new InputStreamReader(url.openStream()));
		                String inputLine;
		                String result = "";
		                while ((inputLine = in.readLine()) != null)
		                	result += inputLine;
		                in.close();
		                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();   
		                Document document = builder.parse(new ByteArrayInputStream(result.getBytes()));		        		        
		                latLongString += document.getElementsByTagName("formatted_address").item(0).getTextContent();
					} catch (Exception e) {
						System.out.println("Problema ao ler retorno do endereço google");
						latLongString = "Impossível acessar o servidor, verifique a conexão de internet do seu aparelho";
						e.printStackTrace();
					}                
	                locationTXT.setText(latLongString);
	                manager.removeUpdates(listener);
				}
			}
			public void onProviderDisabled(String arg0) {}
			public void onProviderEnabled(String arg0) {}
			public void onStatusChanged(String arg0, int arg1, Bundle arg2) {}
		};
        btLocalizacao.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10, listener);
				
			}
		});
		
    }
	public void onConfigurationChanged(Configuration newConfig) {  
		super.onConfigurationChanged(newConfig);  
	}

   
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    
} 