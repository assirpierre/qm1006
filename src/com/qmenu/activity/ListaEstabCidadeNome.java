package com.qmenu.activity;


import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.qmenu.R;
import com.qmenu.model.Estabelecimento;
import com.qmenu.util.AsyncTaskCompleteListener;
import com.qmenu.util.DAO;
import com.qmenu.util.Util;
import com.qmenu.util.WS;

public class ListaEstabCidadeNome extends ListActivity implements AsyncTaskCompleteListener<String>
{    
	private MenuItemAdapter m_adapter;
	private EditText edBuscaCidade;
	private EditText edBuscaNome;
	private TextView txSelEstab;
	private boolean executando = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listaestabcidadenome);
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
		
		txSelEstab = (TextView) findViewById(R.id.txSelEstab);
		
		edBuscaCidade = (EditText) findViewById(R.id.edBuscaCidade);
		edBuscaCidade.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode == 66 && edBuscaCidade.length() > 0) {
					processa();
					return true;
				}
				return false;
			}
		});
		edBuscaCidade.setOnEditorActionListener(new EditText.OnEditorActionListener() {
		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE && edBuscaCidade.length() > 0) {
            		processa();
                	return true;
                }
                return false;
            }
        });		
		edBuscaNome = (EditText) findViewById(R.id.edBuscaNome);
        edBuscaNome.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == 66 && edBuscaNome.length() > 0) {
            		processa();
                	return true;
                }
                return false;
            }
        });        
		edBuscaNome.setOnEditorActionListener(new EditText.OnEditorActionListener() {
		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE && edBuscaNome.length() > 0) {
            		processa();
                	return true;
                }
                return false;
            }
        });
		
	}

	public void onConfigurationChanged(Configuration newConfig) {  
		super.onConfigurationChanged(newConfig);  
	}

	private void processa() {
		InputMethodManager imm = (InputMethodManager)  
		getSystemService(Context.INPUT_METHOD_SERVICE);  
		imm.hideSoftInputFromWindow(edBuscaNome.getWindowToken(), 0); 		
		if(!executando){
			txSelEstab.setVisibility(View.INVISIBLE);
			m_adapter.clear();
			getWS(0).execute();
		}
	}

    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Estabelecimento estab = (Estabelecimento)this.getListAdapter().getItem(position);
		Util.gravaSessao(this, "estab", estab.getCodigo());
		Util.gravaSessao(this, "nomeEstab", estab.getNomeFantasia());
		startActivityForResult(new Intent(this, EstabInicio.class), 0);
    }
	
	private WS getWS(int transacao){
    	WS ws = new WS("getListaEstabelecimento", this, this, transacao);
    	ws.addCampo("cidade", "" + edBuscaCidade.getText());
    	ws.addCampo("nome", "" + edBuscaNome.getText());
    	ws.addCampo("tipo", "nolocal");
    	executando = true;
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
				txNomeFantasia.setText(o.getNomeFantasia());
				txDescricao.setText(o.getDescricao());
			}
			return v;
		}
	}
	
    public void onTaskComplete(String metodo, int transacao, String retorno) {
    	if(retorno.equals("-2"))
    		Util.semConexao(this, getWS(transacao));
    	else if(retorno.indexOf("##ERRO##")==-1){
    		executando = false;
    		DAO rs = new DAO(retorno, this);
			if(rs.next()){
				txSelEstab.setVisibility(View.VISIBLE);
				do{
					Estabelecimento estab = new Estabelecimento();
					estab.setCodigo(rs.getString("codigo"));
					estab.setNomeFantasia(rs.getString("nomeFantasia"));
					estab.setDescricao(rs.getString("descricao"));
					m_adapter.add(estab);
				}while(rs.next());
			}else
				Util.semDados(this);	
    	}
    }
}
