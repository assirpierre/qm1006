package com.qmenu.control;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;

import com.qmenu.R;
import com.qmenu.activity.NoLocal;
import com.qmenu.util.Util;

public class ThreadConectaWiFI extends Thread{  
	private static Handler handler;  
	private WifiManager mainWifi;
    private String SSID;
    private String tipoAut;
    private NoLocal ctx;
    
    public ThreadConectaWiFI(WifiManager mainWifi, String SSID, String tipoAut, NoLocal ctx) {  
        this.mainWifi = mainWifi;
        this.SSID = SSID;
        this.tipoAut = tipoAut;
        this.ctx = ctx;
		final ProgressDialog pDialog = ProgressDialog.show(ctx, null, ctx.getString(R.string.strMsgConectando));
		handler = new Handler(){  
			public void handleMessage(Message msg) {  
				pDialog.dismiss();  
				if(msg.arg1 == -1 || msg.arg1 == 0)
					Util.alert(ThreadConectaWiFI.this.ctx, ThreadConectaWiFI.this.ctx.getString(R.string.strErroConexaoWIFI));
				else if(msg.arg1 >= 30)
					Util.alert(ThreadConectaWiFI.this.ctx, ThreadConectaWiFI.this.ctx.getString(R.string.strMsgTempoConexaoExcedido));
				else
					ThreadConectaWiFI.this.ctx.getWS(0).execute();
			}  
		};  

    }
      
    @Override  
    public void run() {  
    	Message msg = new Message();
    	msg.arg1 = conectaWiFi();
        handler.sendMessage(msg);
    }
    
    private int conectaWiFi(){
		WifiConfiguration wc = new WifiConfiguration(); // This is must be quoted according to the documentation // http://developer.android.com/reference/android/net/wifi/WifiConfiguration.html#SSID
        wc.SSID = "\"" + SSID + "\"";
        wc.status = WifiConfiguration.Status.ENABLED;       
        wc.priority = 40;

		if(tipoAut.indexOf("WEP")>-1){
            wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            wc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            wc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            wc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
        	wc.wepKeys[0] = "\"".concat("fastway123456").concat("\"");
            wc.wepTxKeyIndex = 0;
		}else{	                
            wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            wc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            wc.preSharedKey = "\"".concat("fastway00").concat("\"");
		}

		int netId = mainWifi.addNetwork(wc);
        if (netId == -1){
            return - 1;
        }
        boolean b = mainWifi.enableNetwork(netId, true);
        mainWifi.setWifiEnabled(true);	                
        if(!b)
        	return - 1;
        int x = 0, i = 0;
        try {			  
        	ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);//Pego a conectividade do contexto o qual o metodo foi chamado
        	if ( cm.getActiveNetworkInfo() == null || (cm.getActiveNetworkInfo() != null && cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected() && 
					!cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected())){
        		for (i = 0; i<10;i++){
        			Thread.sleep(1000);
        			if ( cm.getActiveNetworkInfo() == null || (cm.getActiveNetworkInfo() != null && cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()  && 
        					!cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected()))
        				break;
        		}
        		if(i==10)
        			return 0;
        		else{	
	        		for (x = 0; x<30;x++){
	        			if (!cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected() &&
	        					cm.getActiveNetworkInfo() != null && 
	        						cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected() &&
	        							Util.pinga())
	        				break;
	        			Thread.sleep(2000);
	        		}
        		}
        	}
        } catch (InterruptedException e) {
        	e.printStackTrace();
        	return -1;
        }        		  	        
        return x;
    }

}
