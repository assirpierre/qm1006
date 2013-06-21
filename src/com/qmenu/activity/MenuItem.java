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
import android.widget.ListView;
import android.widget.TextView;

import com.qmenu.R;
import com.qmenu.control.MenuProvider;
import com.qmenu.control.PedidoProvider;
import com.qmenu.model.Item;
import com.qmenu.model.MPrincipal;
import com.qmenu.model.Pedido;
import com.qmenu.util.Util;

public class MenuItem extends ListActivity 
{    
	private MenuItemAdapter m_adapter;
	private MPrincipal mprincipal;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menuitem);
		Util.carregaTitulo(this);
        Button btListaPedido = (Button) findViewById(R.id.btListaPedido);
        btListaPedido.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		startActivityForResult(new Intent(MenuItem.this, ListaPedido.class), 0);            
        	}
        });
        Button btSelecionaItemC = (Button) findViewById(R.id.btSelecionaItemC);
        btSelecionaItemC.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
                Pedido pedido = new Pedido();
        		for(Item o:mprincipal.getItem())
        			if(o.isSelecionado())
        				pedido.addItem(o);
        		if(pedido.getQtdeItem() != mprincipal.getQtdeitem())
        			Util.alert(MenuItem.this, getString(R.string.strQtdeItemSelIncorreto).replaceAll("##S", "" + pedido.getQtdeItem()).replaceAll("##E", "" + mprincipal.getQtdeitem()));
        		else
        			abrePedido(pedido);
        	}
        });
        TextView txSelItem = (TextView)findViewById(R.id.txSelItem);
        mprincipal = MenuProvider.getMenu().get(getIntent().getExtras().getInt("positiongrupo"));
        if( mprincipal.getQtdeitem() == 1)
        	btSelecionaItemC.setVisibility(View.GONE);
        else
        	txSelItem.setText(R.string.strMsgSelItemMultiplo);
        	
		this.m_adapter = new MenuItemAdapter(this, R.layout.rowitem, mprincipal.getItem(), mprincipal.getQtdeitem());
		setListAdapter(this.m_adapter);
	}
	
	public void onConfigurationChanged(Configuration newConfig) {  
		super.onConfigurationChanged(newConfig);  
	}

    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Pedido pedido = new Pedido();
        pedido.addItem((Item)this.getListAdapter().getItem(position));
		abrePedido(pedido);
    }

	private void abrePedido(Pedido pedido) {
		Intent i = null;
		if(pedido.getQtdeItem()==1)
			i = new Intent(this, EfetuaPedido.class);
		else
			i = new Intent(this, EfetuaPedidoMItem.class);
		pedido.setMprincipalIni(mprincipal);
		pedido.setUsuario(Util.leSessao(this, "usuario"));
		pedido.setSituacao("P");
		PedidoProvider.setPedidoAtual(pedido, true);
		startActivityForResult(i, 0);
	}
	
	private class MenuItemAdapter extends ArrayAdapter<Item> {
		private ArrayList<Item> item;
		private int qtdeItem;
		public MenuItemAdapter(Context context, int textViewResourceId,  ArrayList<Item> item, int qtdeItem) {
			super(context, textViewResourceId, item);
			this.item = item;
			this.qtdeItem = qtdeItem;
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
				o.setSelecionado(false);
				TextView txDescricao = (TextView) v.findViewById(R.id.descricao);
				TextView txPreco = (TextView) v.findViewById(R.id.preco);
				TextView txDescricaoEstab = (TextView) v.findViewById(R.id.descricaoestab);
				CheckBox chItem = (CheckBox)v.findViewById(R.id.chItem);
				chItem.setTag(o);
				if(qtdeItem == 1)
					chItem.setVisibility(View.GONE);
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
