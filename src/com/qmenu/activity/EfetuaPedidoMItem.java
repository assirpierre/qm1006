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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.qmenu.R;
import com.qmenu.control.PedidoProvider;
import com.qmenu.model.Item;
import com.qmenu.model.Pedido;
import com.qmenu.util.Data;
import com.qmenu.util.Util;

public class EfetuaPedidoMItem extends ListActivity 
{    

	private Button upButton;
	private Button downButton;
	private EditText edQtde;
	private TextView txTotal; 
	private EditText edObs; 
	private Pedido pedido;
	private MenuItemAdapter m_adapter;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.efetuapedidomitem);
		Util.carregaTitulo(this);
		pedido = PedidoProvider.getPedidoAtual();
		TextView txDescricao = (TextView) findViewById(R.id.txDescricao);
		txDescricao.setText(pedido.getMprincipal().getDescricao());
		txTotal = (TextView) findViewById(R.id.txTotal);
		upButton = (Button) findViewById(R.id.upButton);
		downButton = (Button) findViewById(R.id.downButton);
		edQtde = (EditText) findViewById(R.id.numberEditText);
		edQtde.setText(pedido.getQtde() + "");
		edObs = (EditText) findViewById(R.id.edObs);
		edObs.setText(pedido.getObservacao());
		upButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				downButton.setBackgroundResource(R.drawable.timepicker_down_normal);
				upButton.setBackgroundResource(R.drawable.timepicker_up_pressed);
				pedido.setQtde(pedido.getQtde()+1);
				edQtde.setText(pedido.getQtde() + "");
				txTotal.setText(getString(R.string.strTotalPedido) + " " + pedido.getTotalF());
			}
		});

		downButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				downButton .setBackgroundResource(R.drawable.timepicker_down_pressed);
				upButton.setBackgroundResource(R.drawable.timepicker_up_normal);
				if (pedido.getQtde() > 1)
					pedido.setQtde(pedido.getQtde()-1);
				edQtde.setText(pedido.getQtde() + "");
				txTotal.setText(getString(R.string.strTotalPedido) + " " + pedido.getTotalF());
			}
		});
		Button btAdicionais = (Button) findViewById(R.id.btAdicionais);
		if(pedido.getItemSelecionado().getGrupoadicionais() == 0)
			btAdicionais.setVisibility(View.GONE);
		btAdicionais.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {		
				startActivityForResult(new Intent(EfetuaPedidoMItem.this, ListaAdicionais.class), 0);
			}
		});		
        Button btPedido = (Button) findViewById(R.id.btPedido);
        btPedido.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {  
        		pedido.setObservacao(edObs.getText().toString());
				pedido.setDatapedido(Data.getDataHoraAtual());
				PedidoProvider.gravaPedidoPendente();
				if(PedidoProvider.isNovo()){
					Intent i = new Intent(EfetuaPedidoMItem.this, MenuPrincipal.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivityForResult(i, 0);        	
				}else{
					setResult(RESULT_OK, new Intent());
					finish();
				}
			}
             });
        if(!pedido.getSituacao().equals("P")){
        	btPedido.setEnabled(false);
        	downButton.setEnabled(false);
        	upButton.setEnabled(false);
        	edObs.setEnabled(false);
            btAdicionais.setEnabled(false);
        }
		this.m_adapter = new MenuItemAdapter(this, R.layout.rowitem, pedido.getL_item());
		setListAdapter(this.m_adapter);
	}
	
	public void onConfigurationChanged(Configuration newConfig) {  
		super.onConfigurationChanged(newConfig);  
	}

    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        pedido.setPosItemSelecionado(position);
		startActivityForResult(new Intent(this, EfetuaPedido.class), 0);
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
				chItem.setVisibility(View.GONE);
				txDescricao.setText(o.getDescricao());
				txPreco.setVisibility(View.GONE);
				txDescricaoEstab.setText(o.getDescricaoestab());
			}
			return v;
		}
	}
    protected void onStart(){
        super.onStart();
        txTotal.setText(getString(R.string.strTotalPedido) + " " + pedido.getTotalF());
    }
} 