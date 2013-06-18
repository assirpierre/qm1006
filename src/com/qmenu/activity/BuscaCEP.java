package com.qmenu.activity;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.qmenu.R;
import com.qmenu.cep.CEPService;

public class BuscaCEP extends Activity 
{    
	TextView locationTXT;
	Button btLocalizacao;
	LocationManager manager;
	LocationListener listener;
	EditText edCEP;
	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buscacep);
        Button btVoltar = (Button) findViewById(R.id.btVoltar);

        btVoltar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();			
			}
		});

        locationTXT = (TextView) findViewById(R.id.myLocationText);

        edCEP = (EditText) findViewById(R.id.edCEP);
        edCEP.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == 66 && edCEP.length() > 0) {
                    CEPService cep = new CEPService();
                    try {
						locationTXT.setText(cep.obterLogradouro(edCEP.getText() + "", "usuario", "senha"));
					} catch (Exception e) {
						locationTXT.setText("Erro ao consultar o CEP");
						e.printStackTrace();
					}
                	edCEP.setText("");
                	return true;
                }
                return false;
            }

        });
    }
	public void onConfigurationChanged(Configuration newConfig) {  
		super.onConfigurationChanged(newConfig);  
	}
} 