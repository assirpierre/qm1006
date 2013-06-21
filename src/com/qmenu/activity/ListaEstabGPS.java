package com.qmenu.activity;


import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
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
import com.qmenu.model.Estabelecimento;
import com.qmenu.util.AsyncTaskCompleteListener;
import com.qmenu.util.DAO;
import com.qmenu.util.Util;
import com.qmenu.util.WS;

public class ListaEstabGPS extends ListActivity implements AsyncTaskCompleteListener<String>
{    
	private MenuItemAdapter m_adapter;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listaestabgps);
        Button btVoltar = (Button) findViewById(R.id.btVoltar);
        btVoltar.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		Intent intent = new Intent();
        		setResult(RESULT_OK, intent);
        		finish();	
        	}
        });
		this.m_adapter = new MenuItemAdapter(this, R.layout.rowlistaestab, new ArrayList<Estabelecimento>());
		setListAdapter(this.m_adapter);
		getWS(0).execute();
	}

	public void onConfigurationChanged(Configuration newConfig) {  
		super.onConfigurationChanged(newConfig);  
	}

    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Estabelecimento estab = (Estabelecimento)this.getListAdapter().getItem(position);
		Util.gravaSessao(this, "estab", estab.getCodigo());
		Util.gravaSessao(this, "nomeEstab", estab.getNomeFantasia());
		startActivityForResult(new Intent(this, EstabInicio.class), 0);
    }
	
	private WS getWS(int transacao){
    	WS ws = new WS("getListaEstabelecimentoGPS", this, this, transacao);
    	ws.addCampo("latitude", getIntent().getExtras().getString("latitude"));
    	ws.addCampo("longitude", "" + getIntent().getExtras().getString("longitude"));
    	ws.addCampo("tipo", "nolocal");
    	return ws;
	}

	private class MenuItemAdapter extends ArrayAdapter<Estabelecimento> {
		private  ArrayList<Estabelecimento> estab;
		public MenuItemAdapter(Context context, int textViewResourceId,  ArrayList<Estabelecimento> estab) {
			super(context, textViewResourceId, estab);
			this.estab = estab;
		}
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.rowlistaestab, null);
			}
			Util.formataRow(position, v);
			Estabelecimento o = estab.get(position);
			if (o != null) {
				TextView txNomeFantasia = (TextView) v.findViewById(R.id.nomefantasia);
				TextView txDescricao = (TextView) v.findViewById(R.id.descricao);
				TextView txDistancia = (TextView) v.findViewById(R.id.distancia);
				TextView txTempo = (TextView) v.findViewById(R.id.tempo);
				txNomeFantasia.setText(o.getNomeFantasia());
				txDescricao.setText(o.getDescricao());
				txDistancia.setText(o.getDistancia());
				txTempo.setText(o.getTempo());
			}
			return v;
		}
	}
	
    public void onTaskComplete(String metodo, int transacao, String retorno) {
    	if(retorno.equals("-2"))
    		Util.semConexao(this, getWS(transacao));
    	else if(retorno.indexOf("##ERRO##")==-1){
    		DAO rs = new DAO(retorno, this);
			if(rs.next()){
				do{
					Estabelecimento estab = new Estabelecimento();
					estab.setCodigo(rs.getString("codigo"));
					estab.setNomeFantasia(rs.getString("nomeFantasia"));
					estab.setDescricao(rs.getString("descricao"));
					estab.setDistancia(rs.getString("distancia"));
					estab.setTempo(rs.getString("tempo"));
					m_adapter.add(estab);				
				}while(rs.next());					
			}else
				Util.semDados(this);	
    	}
    }	
}
