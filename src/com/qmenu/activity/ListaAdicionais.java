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
import android.widget.CheckBox;
import android.widget.TextView;

import com.qmenu.R;
import com.qmenu.control.AdicionaisProvider;
import com.qmenu.control.PedidoProvider;
import com.qmenu.model.Adicionais;
import com.qmenu.model.Item;
import com.qmenu.model.Pedido;
import com.qmenu.util.Util;

public class ListaAdicionais extends ListActivity 
{    
	private MenuItemAdapter m_adapter;
	private Adicionais adicionais;
	private Pedido pedido;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listaadicionais);
		Util.carregaTitulo(this);
		pedido = PedidoProvider.getPedidoAtual();
        Button btOK = (Button) findViewById(R.id.btOK);
        btOK.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		pedido.limpaItemadd();
        		for(Item o:adicionais.getItem())
        			if(o.isSelecionado()){
        				pedido.addItemadd(o, true);
        				o.setSelecionado(false);
        			}
	    		setResult(RESULT_OK, new Intent());
	    		finish();	            		        		
        	}
        });
        adicionais = AdicionaisProvider.getItem(pedido.getItemSelecionado().getGrupoadicionais());
        if(!pedido.getSituacao().equals("P")){
        	TextView txSelItem = (TextView)findViewById(R.id.txSelItem);
        	txSelItem.setVisibility(View.GONE);
        	btOK.setEnabled(false);
        	this.m_adapter = new MenuItemAdapter(this, R.layout.rowitem, pedido.getL_itemadd());
        }else
        	this.m_adapter = new MenuItemAdapter(this, R.layout.rowitem, adicionais.getItem());
		setListAdapter(this.m_adapter);
	}
	
	public void onConfigurationChanged(Configuration newConfig) {  
		super.onConfigurationChanged(newConfig);  
	}

	private class MenuItemAdapter extends ArrayAdapter<Item> {
		private ArrayList<Item> item;
		public MenuItemAdapter(Context context, int textViewResourceId,  ArrayList<Item> item) {
			super(context, textViewResourceId, item);
			this.item = item;
		}
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.rowitem, null);
			}
			Util.formataRow(position, v);
			Item o = item.get(position);
			if (o != null) {
				TextView txDescricao = (TextView) v.findViewById(R.id.descricao);
				TextView txPreco = (TextView) v.findViewById(R.id.preco);
				TextView txDescricaoEstab = (TextView) v.findViewById(R.id.descricaoestab);
				CheckBox chItem = (CheckBox)v.findViewById(R.id.chItem);
				if(!pedido.getSituacao().equals("P"))
					chItem.setVisibility(View.GONE);
				chItem.setChecked(pedido.isItemaddChecked(o));
				o.setSelecionado(chItem.isChecked());
				chItem.setTag(o);
				chItem.setOnClickListener( new View.OnClickListener() {  
					public void onClick(View v) {  
						CheckBox cb = (CheckBox) v ;  
						Item o = (Item) cb.getTag();  
						o.setSelecionado(cb.isChecked());
					}  
				});  
				txDescricao.setText(o.getDescricao());
				txPreco.setText(getString(R.string.strMoeda) + " " + o.getPrecoF());
				if(o.getDescricaoestab().equals(""))
					txDescricaoEstab.setVisibility(View.GONE);
				else
					txDescricaoEstab.setText(o.getDescricaoestab());
			}
			return v;
		}
	}
}
