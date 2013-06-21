package com.qmenu.activity;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.qmenu.R;
import com.qmenu.control.PedidoProvider;
import com.qmenu.model.Pedido;
import com.qmenu.util.Data;
import com.qmenu.util.ImageLoader;
import com.qmenu.util.Util;

public class EfetuaPedido extends Activity 
{    

	private Button upButton;
	private Button downButton;
	private EditText edQtde;
	private TextView txTotal; 
	private EditText edObs; 
	private ImageView imgView;
	private Pedido pedido;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.efetuapedido);
		Util.carregaTitulo(this);
		pedido = PedidoProvider.getPedidoAtual();
		TextView txObs = (TextView) findViewById(R.id.txObs);
		TextView txDescricao = (TextView) findViewById(R.id.txDescricao);
		txDescricao.setText(pedido.getItem());
		TextView txDescricaoEstab = (TextView) findViewById(R.id.txDescricaoEstab);
		txDescricaoEstab.setText(pedido.getItemdescricao());
		TextView txPrecoUnitario = (TextView) findViewById(R.id.txPrecoUnitario);
		txPrecoUnitario.setText(getString(R.string.strPrecoUnitarioPedido) + " " + pedido.getItemSelecionado().getPrecoF());
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
				startActivityForResult(new Intent(EfetuaPedido.this, ListaAdicionais.class), 0);
			}
		});
        Button btPedido = (Button) findViewById(R.id.btPedido);
        btPedido.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {  
        		pedido.setObservacao(edObs.getText().toString());
				pedido.setDatapedido(Data.getDataHoraAtual());				
				PedidoProvider.gravaPedidoPendente();
	    		setResult(RESULT_OK, new Intent());
	    		finish();	            		        		
        	}
        });
        if(pedido.getMprincipal().getQtdeitem()>1){
        	btPedido.setVisibility(View.GONE);
        	btAdicionais.setVisibility(View.GONE);
        	downButton.setVisibility(View.GONE);
        	upButton.setVisibility(View.GONE);
        	edObs.setVisibility(View.GONE);
        	edQtde.setVisibility(View.GONE);
        	txObs.setVisibility(View.GONE);
        	txTotal.setVisibility(View.GONE);
        }
        if(!pedido.getSituacao().equals("P")){
        	btPedido.setEnabled(false);
        	downButton.setEnabled(false);
        	upButton.setEnabled(false);
        	edObs.setEnabled(false);
            btAdicionais.setEnabled(false);
        }
        imgView = (ImageView)findViewById(R.id.imgItem);
        ImageLoader.getInstance().load(imgView, "http://www.qmenu.com.br:8080/upload/qmenu/" + Util.leSessao(EfetuaPedido.this, "estab") + "/" + pedido.getItemSelecionado().getCodigo() + ".jpg", false);
	}

	public void onConfigurationChanged(Configuration newConfig) {  
		super.onConfigurationChanged(newConfig);  
	}
	
    protected void onStart(){
        super.onStart();
        txTotal.setText(getString(R.string.strTotalPedido) + " " + pedido.getTotalF());
    }
} 