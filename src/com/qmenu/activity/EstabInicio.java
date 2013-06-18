package com.qmenu.activity;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.qmenu.R;
import com.qmenu.util.Util;

public class EstabInicio extends Activity 
{    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.estabinicio);
        Button btVoltar = (Button) findViewById(R.id.btVoltar);

        btVoltar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();			
			}
		});
        
        TextView txNomeEstab = (TextView) findViewById(R.id.txNomeEstab);
        txNomeEstab.setText("\n" + Util.leSessao(this, "nomeEstab") + "\n");

        
        Button btVerMenu = (Button) findViewById(R.id.btVerMenu);
        
        btVerMenu.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		startActivityForResult(new Intent(v.getContext(), MenuPrincipal.class), 0);				
        	}
        });
		
    } 
    
	public void onConfigurationChanged(Configuration newConfig) {  
		super.onConfigurationChanged(newConfig);  
	}    
} 