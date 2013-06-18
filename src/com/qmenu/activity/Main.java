package com.qmenu.activity;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qmenu.R;
import com.qmenu.control.MenuProvider;
import com.qmenu.util.Util;

public class Main extends Activity 
{    
	Button btLogin;
	Button btLogout;
	Button btCadastroEstab;
	Button btDemo;
	Button btEstab;
	TextView txUsuarioLogado;
	TextView txRodapeLogin;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);     
        Util.configuraLog(this);
        txUsuarioLogado = (TextView) findViewById(R.id.txUsuarioLogado);
        txRodapeLogin = (TextView) findViewById(R.id.txRodapeLogin);
        //Linkify.addLinks(txRodapeLogin, Linkify.ALL);
        btLogout = (Button) findViewById(R.id.btLogout);
        btCadastroEstab = (Button) findViewById(R.id.btCadastroEstab);
        btCadastroEstab.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.qmenu.com.br"));
                startActivity(i);       		
        	}
        });
        btDemo = (Button) findViewById(R.id.btDemo);
        btDemo.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		startActivityForResult(new Intent(v.getContext(), ListaMesas.class), 0);
        	}
        });	
                
        btEstab = (Button) findViewById(R.id.btEstab);
        btEstab.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		startActivityForResult(new Intent(v.getContext(), ListaMesas.class), 0);		        		
        	}
        });
        
        Button btNoLocal = (Button) findViewById(R.id.btNoLocal);
        btNoLocal.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
                //startActivityForResult(new Intent(v.getContext(), NoLocal.class), 0);		
			}
		});
        Button btDelivery = (Button) findViewById(R.id.btDelivery);
        btDelivery.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		//startActivityForResult(new Intent(v.getContext(), Delivery.class), 0);				
        	}
        });
        btLogin = (Button) findViewById(R.id.btLogin);
        btLogin.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		startActivityForResult(new Intent(v.getContext(), Login.class), 0);
        	}
        });	
        btLogout.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		MenuProvider.limpaMenu();
        		Util.gravaSessao(Main.this, "logado", "false");
        		onStart();	    	
        	}
        });
        boolean ping = Util.pinga();        
        if(ping==false)
        	Util.alert(this, getString(R.string.strSemConexao));        
        if(!Util.leSessao(this, "estab").equals("") && !Util.leSessao(this, "estab").equals("1") && ping)
    		startActivityForResult(new Intent(this, ListaMesas.class), 0);
        
    }
    
	public void onConfigurationChanged(Configuration newConfig) {  
		super.onConfigurationChanged(newConfig);  
	}

    protected void onStart(){
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.layoutMain);
        linearLayout.setBackgroundDrawable(Util.getBackResource(this));
        super.onStart();
        String login = "";
        if(Util.leSessao(this, "logado").equals("true"))
        	login = Util.leSessao(this, "usuario");        
        if(!login.equals("")){
        	btDemo.setVisibility(View.GONE);
        	btCadastroEstab.setVisibility(View.GONE);
        	btEstab.setVisibility(View.VISIBLE);        	
        	txRodapeLogin.setVisibility(View.GONE);
        	btLogin.setVisibility(View.GONE);
        	btLogout.setVisibility(View.VISIBLE);
        	txUsuarioLogado.setText(getString(R.string.strLbUsuarioLogado) + " " + login);
        	txUsuarioLogado.setVisibility(View.VISIBLE);	    	
        }else{
        	btDemo.setVisibility(View.VISIBLE);
//        	btCadastroEstab.setVisibility(View.VISIBLE);
        	btEstab.setVisibility(View.GONE);        	        	
        	txRodapeLogin.setVisibility(View.VISIBLE);
        	btLogin.setVisibility(View.VISIBLE);
        	btLogout.setVisibility(View.GONE);
        	txUsuarioLogado.setVisibility(View.GONE);
        	Util.gravaSessao(this, "estab", "1");
        	Util.gravaSessao(this, "usuario", "demouser");
        	Util.gravaSessao(this, "usuario_id", "2");
        }
    } 
    
//	protected void onRestart(){
//		super.onRestart();
//	}
//		Button btLogin = (Button) findViewById(R.id.btLogin);
//		btLogin.setVisibility(View.INVISIBLE);
//		String message = "onRestart() called" + Util.leArquivo(this, "l");
//		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//	}
//	protected void onPause(){
//		super.onPause();
//		String message = "onPause() called";
//		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//	}
//    protected void onStop(){
//        super.onStop();
//        String message = "onStop() called";
//		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//	}
    
    /** Lifecycle method: The final call received before the activity is destroyed. */
    @Override
    protected void onDestroy(){
    	super.onDestroy();
//		WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
//		Toast.makeText(this, "saindo 2..", Toast.LENGTH_SHORT).show();	
//		if(wifi.getConnectionInfo().getSSID() != null && wifi.getConnectionInfo().getSSID().toLowerCase().indexOf("qmenu")>-1){
//			wifi.disconnect();
//			wifi.removeNetwork(wifi.getConnectionInfo().getNetworkId());
//		}else{
//			Toast.makeText(this, "sem wifi QMenu..", Toast.LENGTH_SHORT).show();	
//		}
        if(Util.leSessao(this, "usuariopersistente").equals("false"))
        	Util.gravaSessao(this, "logado", "false");		
    }    	
} 