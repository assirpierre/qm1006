package com.qmenu.util;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.qmenu.R;
import com.qmenu.control.EstabProvider;

public class Util {
    public static boolean testConnection(Context ctx) {
    	boolean retorno = true;
    	try
    	{
    		ConnectivityManager cm = (ConnectivityManager)
    		ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

//    		if (!cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()){
//    			if(!cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected()){
//    				Toast.makeText(ctx, R.string.strSemConexao, Toast.LENGTH_LONG).show();
//    				retorno = false;
//    			}
//    		}
    		if (!(cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected())){
	    		Toast.makeText(ctx, R.string.strSemConexao, Toast.LENGTH_LONG).show();
    			retorno = false;
    		}

    	}
    	catch (Exception e)
    	{
    		retorno = false;
    		Toast.makeText(ctx, R.string.strSemConexao, Toast.LENGTH_LONG).show();
    	}
    	return retorno;
    }
    
    public static void gravaArquivo(Context ctx, String valor){
    	try{
	    	FileOutputStream fos = ctx.openFileOutput("l", Context.MODE_PRIVATE);
	    	fos.write(valor.getBytes());
	    	fos.close();
    	}catch (Exception e) {
			e.printStackTrace();
		}				    	
    }
    
	public static String leArquivo(Context ctx, String arquivo){
		
		String retorno = "";
		BufferedReader br = null;
		String linhaAtual;
		try{
			br = new BufferedReader(new InputStreamReader(ctx.openFileInput(arquivo)));
			if (br != null){
				linhaAtual = br.readLine();
				while(linhaAtual !=null){
					retorno += (retorno.equals("")?"":"\n") + linhaAtual;
					linhaAtual = br.readLine();
				}
				br.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}							
		return retorno;	
	}

	public static void limpaLog(Context ctx){
		try{
	    	FileOutputStream fos = ctx.openFileOutput("log", Context.MODE_PRIVATE);
	    	fos.close();
	    	configuraLog(ctx);
    	}catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public static void configuraLog(Context ctx){
		try{
//	    	FileOutputStream fos = ctx.openFileOutput("log", Context.MODE_APPEND);
//	    	PrintStream prsLog = new PrintStream (fos,false);
//	    	System.setOut(prsLog);
    	}catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public static void gravaSessao(Context ctx, String nome, String valor){
    	SharedPreferences settings = ctx.getSharedPreferences("qmenu", 0);
    	SharedPreferences.Editor editor = settings.edit();
    	editor.putString(nome, valor);
    	editor.commit();
	}

	public static String leSessao(Context ctx, String nome){
        SharedPreferences settings = ctx.getSharedPreferences("qmenu", 0);
        return settings.getString(nome, "");  	
	}
	
	public static boolean pinga() {		
		try {
			java.net.URL mandarMail = new java.net.URL("http://www.qmenu.com.br");  
			java.net.URLConnection conn = mandarMail.openConnection();  

			java.net.HttpURLConnection httpConn = (java.net.HttpURLConnection) conn;  
			httpConn.connect();  
			int x = httpConn.getResponseCode();  
			if(x == 200)  
				return true;
			else
				return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static void alert(Context ctx, String msg){
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
//	    builder.setTitle(R.string.strTituloAlert);
	    builder.setMessage(msg);
	    builder.setPositiveButton(R.string.strBtAlertOK, new DialogInterface.OnClickListener() {
	    	public void onClick(DialogInterface dialog, int id) {
	    	}
	    });
	    AlertDialog alert = builder.create();  
        alert.show();        	
	}
	
	
	public static void semConexao(Context ctx, final WS ws){
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setMessage(R.string.strSemConexaoPergunta);
		builder.setPositiveButton(R.string.strBtAlertOK, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				ws.execute();
			}
		});
		builder.setNegativeButton(R.string.strBtAlertCancela, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
			}
		});
		AlertDialog alert = builder.create();  
		alert.show();
	}
	
	public static void semDados( Activity a) {
		Toast.makeText(a, R.string.strSemDados, Toast.LENGTH_LONG).show();
		Intent intent = new Intent();
		a.setResult(-1, intent);
		a.finish();
	}
	
	public static void formataRow(int position, View v) {
//		if(position % 2 == 0)
//			v.setBackgroundColor(Color.rgb(244, 244, 244));			
//		else
//			v.setBackgroundColor(Color.rgb(255, 255, 255));
	}
	
	public static void carregaTitulo(Activity a){
		TextView tx = (TextView)a.findViewById(R.id.txNomeRestaurante);
		tx.setText(EstabProvider.getNomefantasia());
	}
	
	public static String getStringResourceByName(Activity a, String aString) {
		String packageName = a.getPackageName();
		int resId = a.getResources().getIdentifier(aString, "string", packageName);
		return a.getString(resId);
	}

	public static Drawable getBackResource(Activity a) {
        Resources res = a.getResources();
		String packageName = a.getPackageName();
		int backNumber = Numero.getInt(leSessao(a, "backNumber"));		
		int resId = a.getResources().getIdentifier("back" + backNumber, "drawable", packageName);
		if(backNumber == 4)
			backNumber=0;
		else
			backNumber++;
		gravaSessao(a, "backNumber", backNumber + "");		
        return res.getDrawable(resId); 
	}
}
