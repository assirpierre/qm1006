package com.qmenu.activity;


import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.location.Location;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.maps.MapActivity;
import com.qmenu.R;
import com.qmenu.control.ThreadConectaWiFI;
import com.qmenu.util.AsyncTaskCompleteListener;
import com.qmenu.util.DAO;
import com.qmenu.util.MyLocation;
import com.qmenu.util.MyLocation.LocationResult;
import com.qmenu.util.Util;
import com.qmenu.util.WS;

public class NoLocal extends MapActivity implements AsyncTaskCompleteListener<String>
{    
	private Button btBuscaGPS;
	private Button btBuscaCidadeNome;
	private WifiManager mainWifi;
    private ListView listWIFI;
    private WifiReceiver receiverWifi;
    private String[][] listaWiFi;
    private boolean conecta;
    private int position;
    private ProgressDialog m_ProgressDialog = null; 
	private Location location;	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nolocal);
        Button btVoltar = (Button) findViewById(R.id.btVoltar);
        
        listWIFI = (ListView) findViewById(R.id.listWIFI);
        
        mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        receiverWifi = new WifiReceiver();
        registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        mainWifi.startScan();
  
        btVoltar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();		
			}
		});
         
        
        btBuscaCidadeNome = (Button) findViewById(R.id.btBuscaCidadeNome);
        
        btBuscaCidadeNome.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		startActivityForResult(new Intent(v.getContext(), ListaEstabCidadeNome.class), 0);
        	}
        });
        
        btBuscaGPS = (Button) findViewById(R.id.btBuscaGPS);
                
		btBuscaGPS.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				LocationResult locationResult = new LocationResult(){
				    public void gotLocation(Location location){
				    	NoLocal.this.location = location;		    	
				    	runOnUiThread(returnResGPS);
				    }
				};
				MyLocation myLocation = new MyLocation();
		        if(myLocation.getLocation(NoLocal.this, locationResult))		
					m_ProgressDialog = ProgressDialog.show(NoLocal.this, null, getString(R.string.strMsgObtendoGPS), true);
		        else{
		        	Util.alert(NoLocal.this, getString(R.string.strErroGPS));
		        }
			}
		});
        listWIFI.setOnItemClickListener(new OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {      
        		NoLocal.this.position = position;
            	if(!(mainWifi.getConnectionInfo().getSSID() != null && mainWifi.getConnectionInfo().getSSID().equals(listaWiFi[position][0]))){
            		if(Util.pinga()){
            			conecta = false;
        	    		AlertDialog.Builder builder = new AlertDialog.Builder(NoLocal.this);
        			    builder.setTitle(R.string.strConexaoExistente);
        			    builder.setSingleChoiceItems(R.array.conexaoEstab, 0, new DialogInterface.OnClickListener() {
        			        public void onClick(DialogInterface arg0, int arg1) {
        			            conecta = arg1 == 1;
        			        }
        			    });
        			    builder.setPositiveButton(R.string.strBtAlertOK, new DialogInterface.OnClickListener() {
        			    	public void onClick(DialogInterface dialog, int id) {
        		            	if(conecta){
        			        		ThreadConectaWiFI thread = new ThreadConectaWiFI(mainWifi, listaWiFi[NoLocal.this.position][0], listaWiFi[NoLocal.this.position][1], NoLocal.this);  
        			        		thread.start();
        		            	}else
        		            		getWS(0).execute();
        			    	}
        			    });
        			    AlertDialog alert = builder.create();  
        		        alert.show();        	
            		}else{
		        		ThreadConectaWiFI thread = new ThreadConectaWiFI(mainWifi, listaWiFi[NoLocal.this.position][0], listaWiFi[NoLocal.this.position][1], NoLocal.this);  
		        		thread.start();
            		}
            	}else
            		getWS(0).execute();            	
        	}
        }); 

    }
    
	public void onConfigurationChanged(Configuration newConfig) {  
		super.onConfigurationChanged(newConfig);  
	}
    
	private Runnable returnResGPS = new Runnable() {
		public void run() {
			m_ProgressDialog.dismiss();
	    	if(location==null){
	    		Util.alert(NoLocal.this, getString(R.string.strGPSSemResposta));
	    	}else{	    	
				Intent i = new Intent(NoLocal.this, ListaEstabGPS.class);
				Bundle bundle = new Bundle();
				bundle.putString("latitude", "" + location.getLatitude());
				bundle.putString("longitude", "" + location.getLongitude());
				i.putExtras(bundle);
				startActivityForResult(i, 0);            	    		
	    	}
		}
	};
    
    public WS getWS(int transacao){
    	WS ws = new WS("getEstabelecimento", this, this, transacao);
    	ws.addCampo("SSID", listaWiFi[position][0]);
    	return ws;
    }
    
    
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    class WifiReceiver extends BroadcastReceiver {
		public void onReceive(Context c, Intent intent) {
            List<ScanResult> wifiList = mainWifi.getScanResults();
            int count = 0, a = 0;
            for(int i = 0; i < wifiList.size(); i++)
            	if(wifiList.get(i).SSID.toLowerCase(Locale.getDefault()).indexOf(("qmenu"))>-1)
            		count++;
            if(count>0){
	        	String[] lista =  new String[count];
	        	listaWiFi =  new String[count][2];
	            for(int i = 0; i < wifiList.size(); i++){
	            	if(wifiList.get(i).SSID.toLowerCase(Locale.getDefault()).indexOf(("qmenu"))>-1){
		            	lista[a] = wifiList.get(i).SSID;
		            	lista[a] = lista[a].substring(0,lista[a].toLowerCase(Locale.getDefault()).indexOf("qmenu"));
		            	listaWiFi[a][0] = wifiList.get(i).SSID;
		            	listaWiFi[a][1] = wifiList.get(i).capabilities;
		            	a++;
	            	}
	            }
	            ArrayAdapter<String> adapter = new ArrayAdapter<String>(NoLocal.this, android.R.layout.simple_list_item_1, android.R.id.text1, lista);
	            listWIFI.setAdapter(adapter);
            }	            	
            unregisterReceiver(receiverWifi);
        }
    }
    protected void onRestart(){
    	super.onRestart();
    	registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    	mainWifi.startScan();
    }
    
    protected void onPause(){
    	super.onPause();
    }
    
    public void onTaskComplete(String metodo, int transacao, String retorno) {
    	if(retorno.equals("-2"))
    		Util.semConexao(this, getWS(transacao));
    	else if(retorno.indexOf("##ERRO##")==-1){
    		DAO rs = new DAO(retorno, this);
			if(rs.next()){
	    		Util.gravaSessao(NoLocal.this, "estab", rs.getString("codigo"));
	    		Util.gravaSessao(NoLocal.this, "nomeEstab", rs.getString("nomefantasia"));
	    		startActivityForResult(new Intent(this, EstabInicio.class), 0);
			}else
				Util.semDados(this);	
    	}
    }
}
