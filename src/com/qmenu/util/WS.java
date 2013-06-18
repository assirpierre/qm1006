package com.qmenu.util;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.qmenu.R;

public class WS extends AsyncTask<String, String, String> {
	private AsyncTaskCompleteListener<String> callback;
    private ProgressDialog progress;
	private SoapObject request;
	private String URL;
	private String NAMESPACE;
	private String metodo;
	private Context ctx;
	private int transacao;
	private static boolean executando = false;
	
	
	public WS(String metodo, Context ctx, AsyncTaskCompleteListener<String> cb, int transacao){
//		NAMESPACE = "http://169.254.248.161:8080/";
//		URL = "http://169.254.248.161:8080/qmw/ws/trocadados.jws";
		NAMESPACE = "http://www.qmenu.com.br/";
		URL = "http://www.qmenu.com.br:8080/qmenuws/ws/trocadados.jws";	
//		NAMESPACE = "http://192.168.1.5:8080/";
//		URL = "http://192.168.1.5:8080/qmw/ws/trocadados.jws";
//		NAMESPACE = "http://10.0.1.3:8080/";
//		URL = "http://10.0.1.3:8080/qmw/ws/trocadados.jws";
		this.ctx = ctx;		
		this.callback = cb;
		this.metodo = metodo;
		this.transacao = transacao;
		request = getRequest(metodo);		
	}

	private SoapObject getRequest(String metodo) {
		SoapObject request = new SoapObject(NAMESPACE, metodo);
        request.addProperty("chave", "823742jnkjdshfsa[sdf'sasd[]adf]084@#$@@#ASFF");
        return request;
	}
	
    protected void onPreExecute() {
    	if(executando == false){
	        progress = ProgressDialog.show(ctx, null, ctx.getString(R.string.strMsgObtendoDados), true);
	        if(transacao == 0)
	        	transacao = Numero.getInt(Util.leSessao(ctx, "transacao")) + 1;
	        addCampo("dispositivo", getDispositivo());
	        addCampo("transacao", "" + transacao);
    	}else
    		System.out.println("onPreExecute pela segunda vez");
    }
    
    protected String doInBackground(String... paramss) {
    	if(executando == false){
    		executando = true;
    		return processa(true, request);
    	}else{
    		System.out.println("doInBackground pela segunda vez");
    		return "exe";
    	}
    }
    
    protected void onPostExecute(String retorno) {
    	if(executando && progress !=null && progress.isShowing()){
			progress.dismiss();
	        if(!retorno.equals("-2"))
	        	Util.gravaSessao(ctx, "transacao", "" + transacao);        	
			if(retorno.equals("-1")){
				Toast.makeText(ctx, R.string.strAcessoNegadoChave, Toast.LENGTH_LONG).show();
				retorno = "##ERRO##-1";
			}else if(retorno.equals("##ERRO##")){
				Toast.makeText(ctx, R.string.strErroDados, Toast.LENGTH_LONG).show();
			}else if(retorno.equals("exe")){
				System.out.println("onPostExecute pela segunda vez");
			}
			executando = false;
	        callback.onTaskComplete(metodo, transacao, retorno);
    	}
    }

	public void addCampo(String campo, String valor){
		request.addProperty(campo, valor);		
	}

	public String processa(boolean primeiraChamada, SoapObject request){
		String retorno = "";
		try {
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	        envelope.setOutputSoapObject(request);
	        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	        androidHttpTransport.call(request.getName(), envelope);
	        androidHttpTransport.reset();
	        androidHttpTransport.getServiceConnection().disconnect();
	        retorno = "" + envelope.getResponse();
		}catch (Exception e) {
			if(primeiraChamada){
				System.out.println("processa segunda chamada");
				processa(false, request);				
			}else{
				retorno = "-2";
			}
		}
		return retorno;
	}
	
	public String getDispositivo(){
		String dispositivo = Util.leSessao(ctx, "dispositivo");
		if(dispositivo.equals("")){
			dispositivo = processa(true, getRequest("getNovoDispositivo"));
			if(dispositivo.indexOf("##ERRO##")==-1){
				Util.gravaSessao(ctx, "dispositivo", dispositivo);
				Util.gravaSessao(ctx, "transacao", "0");
			}
		}
		return dispositivo;
	}
	
}
