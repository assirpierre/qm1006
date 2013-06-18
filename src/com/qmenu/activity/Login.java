package com.qmenu.activity;


import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.qmenu.R;
import com.qmenu.control.MenuProvider;
import com.qmenu.util.AsyncTaskCompleteListener;
import com.qmenu.util.DAO;
import com.qmenu.util.Util;
import com.qmenu.util.WS;


public class Login extends Activity implements AsyncTaskCompleteListener<String> {

	private EditText edLogin;
	private EditText edSenha;
	private CheckBox chGravarLogin;
	private Button btEntrar;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        edLogin = (EditText) findViewById(R.id.edLogin);
        edLogin.setText(Util.leSessao(this, "usuario"));
        edSenha = (EditText) findViewById(R.id.edSenha);
        if(edLogin.getText().length()>0)
        	edSenha.requestFocus();
        chGravarLogin = (CheckBox) findViewById(R.id.chGravarLogin);
        btEntrar = (Button) findViewById(R.id.btEntrar);
        btEntrar.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) { 
        		getWS(0).execute();
        	}

        });
//        edSenha.setOnKeyListener(new OnKeyListener() {
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if(keyCode == 66 && edSenha.length() > 0) {
//                	btEntrar.performClick();
//                	return true;
//                }
//                return false;
//            }
//
//        });
        edSenha.setOnEditorActionListener(new EditText.OnEditorActionListener() {
		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE && edSenha.length() > 0) {
                	getWS(0).execute();
                	return true;
                }
                return false;
            }
        });		

    }    

	public void onConfigurationChanged(Configuration newConfig) {  
		super.onConfigurationChanged(newConfig);  
	}

	private WS getWS(int transacao) {
		WS ws = new WS("validalogin", this, this, transacao);
		ws.addCampo("usuario", edLogin.getText().toString());
	    ws.addCampo("senha", edSenha.getText().toString());
	    return ws;
	}

    public void onTaskComplete(String metodo, int transacao, String retorno) {
    	if(retorno.equals("-2"))
    		Util.semConexao(this, getWS(transacao));
    	else if(retorno.equals("")){
	    	Toast.makeText(Login.this, R.string.strUsuarioIncorreto, Toast.LENGTH_LONG).show();
	    	edSenha.setText("");
	    	edSenha.requestFocus();
	    }else{
	    	DAO rs = new DAO(retorno, this);
	    	if(rs.next()){
		    	Util.gravaSessao(Login.this, "usuario", edLogin.getText().toString().toLowerCase(Locale.getDefault()));
		    	Util.gravaSessao(Login.this, "usuario_id", rs.getString("id"));
				Util.gravaSessao(Login.this, "usuariopersistente", chGravarLogin.isChecked() + "");
				Util.gravaSessao(Login.this, "logado", "true");
				Util.gravaSessao(Login.this, "estab", rs.getString("estab_id"));				
	    	}
			Intent intent = new Intent();
	        setResult(RESULT_OK, intent);
	        finish();							    	
	    }
    }
} 