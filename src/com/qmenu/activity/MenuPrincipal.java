package com.qmenu.activity;


import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.qmenu.R;
import com.qmenu.control.EstabProvider;
import com.qmenu.control.MenuProvider;
import com.qmenu.control.MesaProvider;
import com.qmenu.model.MPrincipal;
import com.qmenu.util.AsyncTaskCompleteListener;
import com.qmenu.util.Util;
import com.qmenu.util.WS;

public class MenuPrincipal extends ListActivity implements AsyncTaskCompleteListener<String>
{    
	private MenuItemAdapter m_adapter;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menuprincipal);
		Util.carregaTitulo(this);
        Button btListaPedido = (Button) findViewById(R.id.btListaPedido);
        btListaPedido.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		startActivityForResult(new Intent(MenuPrincipal.this, ListaPedido.class), 0);            
        	}
        });

        Button btFecharMesa = (Button) findViewById(R.id.btFecharMesa);
        btFecharMesa.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		AlertDialog.Builder builder = new AlertDialog.Builder(MenuPrincipal.this);
        	    builder.setMessage(getString(R.string.strConfirmFechaMesa));
        	    builder.setPositiveButton(R.string.strBtAlertOK, new DialogInterface.OnClickListener() {
        	    	public void onClick(DialogInterface dialog, int id) {
                    	getWSFechaMesa(0).execute();	    		        
        	    	}
        	    });
        	    builder.setNegativeButton(R.string.strBtAlertCancela, new DialogInterface.OnClickListener() {
        	    	public void onClick(DialogInterface dialog, int id) {
        	    	}
        	    });
        	    builder.show();           		
        	}
        });
		this.m_adapter = new MenuItemAdapter(this, R.layout.rowmenuprincipal, MenuProvider.getMenu());
		setListAdapter(this.m_adapter);
	}

	public void onConfigurationChanged(Configuration newConfig) {  
		super.onConfigurationChanged(newConfig);  
	}

    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
		Intent i = new Intent(this, MenuItem.class);
		Bundle bundle = new Bundle();
		bundle.putInt("positiongrupo", position);
		i.putExtras(bundle);
		startActivityForResult(i, 0);            
    }
	
    private WS getWSFechaMesa(int transacao){
    	WS ws = new WS("fechaMesa", this, this, transacao);
    	ws.addCampo("estab",EstabProvider.getCodigo());
    	ws.addCampo("mesa", Util.leSessao(this, "mesa"));
    	return ws;
    }
    
	private class MenuItemAdapter extends ArrayAdapter<MPrincipal> {
		private  ArrayList<MPrincipal> o;
		public MenuItemAdapter(Context context, int textViewResourceId,  ArrayList<MPrincipal> o) {
			super(context, textViewResourceId, o);
			this.o = o;
		}
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.rowmenuprincipal, null);
			}
			Util.formataRow(position, v);
			MPrincipal ol = o.get(position);
			TextView txDescricao = (TextView) v.findViewById(R.id.descricao);
			txDescricao.setText(ol.getDescricao());
			return v;
		}
	}
	
    public void onTaskComplete(String metodo, int transacao, String retorno) {
    	if(retorno.equals("-2"))
   			Util.semConexao(this, getWSFechaMesa(transacao));
    	else if(retorno.equals("##pedidopendente##")){
    		Util.alert(this, getString(R.string.strFechaMesaInvPedido));
    	}else if(retorno.equals("##situacaodisponivel##")){
    		Util.alert(this, getString(R.string.strFechaMesaInvSituacao));
    	}else if(retorno.indexOf("##ERRO##")==-1){
    		MesaProvider.atualiza(retorno, this);
    		setResult(RESULT_OK, new Intent());
    		finish();
	    }
    }
}
