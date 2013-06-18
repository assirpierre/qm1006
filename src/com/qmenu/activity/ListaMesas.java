package com.qmenu.activity;


import java.util.ArrayList;
import java.util.Collections;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.qmenu.R;
import com.qmenu.control.AdicionaisProvider;
import com.qmenu.control.EstabProvider;
import com.qmenu.control.MenuProvider;
import com.qmenu.control.MesaProvider;
import com.qmenu.model.Mesa;
import com.qmenu.util.AsyncTaskCompleteListener;
import com.qmenu.util.Data;
import com.qmenu.util.Util;
import com.qmenu.util.WS;

public class ListaMesas extends ListActivity implements AsyncTaskCompleteListener<String>
{    
	private MenuItemAdapter m_adapter;
	private Mesa mesaSelecionada;
	private AlertDialog alert;
	private EditText edMesa;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listamesas);
		Util.carregaTitulo(this);
        Button btRefresh = (Button) findViewById(R.id.btRefresh);
        btRefresh.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		getWSListaMesa(0).execute();
        	}
        });
		atualizaAdapter();
		TextView txNomeRestaurante = (TextView)findViewById(R.id.txNomeRestaurante);
		txNomeRestaurante.setText(Util.leSessao(this, "nomeEstab"));

		edMesa = (EditText) findViewById(R.id.edMesa);
		edMesa.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode == 66 && edMesa.length() > 0) {
					mesaSelecionada = MesaProvider.getMesa(edMesa.getText().toString());
					edMesa.setText("");
					selMesa();
					return true;
				}
				return false;
			}
		});
		edMesa.setOnEditorActionListener(new EditText.OnEditorActionListener() {
		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE && edMesa.length() > 0) {
                	mesaSelecionada = MesaProvider.getMesa(edMesa.getText().toString());
                	edMesa.setText("");
                	selMesa();
                	return true;
                }
                return false;
            }
        });	
		if(!Util.leSessao(this, "estab").equals("") && MenuProvider.getMenu().size() ==0)
			getWSListaMesa(0).execute();	
		else
			carregaLabels();
	}
	
	public void onConfigurationChanged(Configuration newConfig) {  
		super.onConfigurationChanged(newConfig);  
	}

    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        mesaSelecionada = (Mesa)this.getListAdapter().getItem(position);
        selMesa();
    }

	private void selMesa() {
		if(mesaSelecionada != null){
			if(mesaSelecionada.getSituacao().equals("D")){
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
			    builder.setMessage(getString(R.string.strSelecionaMesa).replaceAll("##",Util.getStringResourceByName(this, "str" + EstabProvider.getSistemaTrabalho()) + " " + mesaSelecionada.getCodigo()));
			    final EditText input = new EditText(this);
			    input.setSingleLine(true);
			    input.setPadding(5, 0, 5, 0);
			    input.setImeOptions(EditorInfo.IME_ACTION_DONE);
			    input.setOnEditorActionListener(new EditText.OnEditorActionListener() {
				    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		                if(actionId == EditorInfo.IME_ACTION_DONE && input.length() > 0) {
		                	alert.dismiss();
		                	abreMesa(input);    		
		                	return true;
		                }
		                return false;
		            }
		        });
			    builder.setView(input);
			    builder.setPositiveButton(R.string.strBtAlertOK, new DialogInterface.OnClickListener() {
			    	public void onClick(DialogInterface dialog, int id) {
			    		abreMesa(input);
			    	}
			    });
			    builder.setNegativeButton(R.string.strBtAlertCancela, new DialogInterface.OnClickListener() {
			    	public void onClick(DialogInterface dialog, int id) {
			    	}
			    });
			    alert = builder.create();		    
		        alert.show();
	        }else{
	        	selecionaMesa();
	        }
		}else
			Util.alert(this, Util.getStringResourceByName(this, "strInvalida" + EstabProvider.getSistemaTrabalho()));
	}
    
	private void abreMesa(final EditText input) {
		mesaSelecionada.setNome("" + input.getText().toString().trim());
    	mesaSelecionada.setSituacao("O");
    	mesaSelecionada.setDataultsituacao(Data.getDataHoraAtual());
    	Collections.sort(MesaProvider.getMesa()); 
		getWSAbreMesa(0).execute();
	}
    
    private void selecionaMesa(){
    	Util.gravaSessao(this, "mesa", mesaSelecionada.getId());
    	Util.gravaSessao(this, "mesaNumero", mesaSelecionada.getCodigo());
		startActivityForResult(new Intent(this, MenuPrincipal.class), 0);
    }
    
	private WS getWSAbreMesa(int transacao) {
		WS ws = new WS("gravaMesa", this, this, transacao);
    	ws.addCampo("estab",EstabProvider.getCodigo());
    	ws.addCampo("mesa", mesaSelecionada.getId());
    	ws.addCampo("nome", mesaSelecionada.getNome());
    	return ws;
	}
	
	private WS getWSListaMesa(int transacao) {
    	WS ws = new WS("getMenuEMesas", this, this, transacao);
    	ws.addCampo("estab",Util.leSessao(this, "estab"));
    	ws.addCampo("log", Util.leArquivo(this,"log"));
	    return ws;
	}

	private class MenuItemAdapter extends ArrayAdapter<Mesa> {
		private ArrayList<Mesa> mesa;
		public MenuItemAdapter(Context context, int textViewResourceId,  ArrayList<Mesa> o) {
			super(context, textViewResourceId, o);
			this.mesa = o;
		}
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.rowlistamesas, null);
			}
			Util.formataRow(position, v);
			Mesa o = mesa.get(position);
			if (o != null) {
				TextView txCodigo = (TextView) v.findViewById(R.id.txCodigo);
				TextView txNome = (TextView) v.findViewById(R.id.txNome);
				TextView txSituacao = (TextView) v.findViewById(R.id.txSituacao);
				TextView txDataUltSituacao = (TextView) v.findViewById(R.id.txDataultsituacao);
				txCodigo.setText(Util.getStringResourceByName(ListaMesas.this, "str" + EstabProvider.getSistemaTrabalho()) + " " + o.getCodigo());
				if(!o.getNome().equals(""))
					txNome.setText(getString(R.string.strNome) + " " + o.getNome());
				else
					txNome.setText("");
				if(o.getSituacao().equals("D")){
					txSituacao.setText(getString(R.string.strSituacaoMesaD));
					txSituacao.setTextColor(Color.GREEN);
				}else{
					txSituacao.setText(getString(R.string.strSituacaoMesaO));
					txSituacao.setTextColor(Color.RED);					
				}
				txDataUltSituacao.setText(o.getHora());
			}
			return v;
		}
	}
	
    public void onTaskComplete(String metodo, int transacao, String retorno) {
    	if(retorno.equals("-2"))
    		if(metodo.equals("getMenuEMesas"))
    			Util.semConexao(this, getWSListaMesa(transacao));
    		else
    			Util.semConexao(this, getWSAbreMesa(transacao));
    	else if(retorno.indexOf("##ERRO##")==-1){
    		if(metodo.equals("getMenuEMesas")){    			
	    		int pos1 = retorno.indexOf("##SEP1##");
	    		int pos2 = retorno.indexOf("##SEP2##");
	    		int pos3 = retorno.indexOf("##SEP3##");
	    		if(pos1>0 && pos2 > 0){
	    			Util.limpaLog(this);
		    		MenuProvider.atualiza(retorno.substring(0, pos1), this);
		    		MesaProvider.atualiza(retorno.substring(pos1+8, pos2), this);
		    		atualizaAdapter();
		    		AdicionaisProvider.atualiza(retorno.substring(pos2+8, pos3), this);
		    		EstabProvider.atualiza(retorno.substring(pos3+8), this);		   
		    		carregaLabels();		    		
	    		}
    		}else{
    			if(retorno.equals("true")){
    				selecionaMesa();
    			}else{
    				Toast.makeText(this, Util.getStringResourceByName(this, "strStatusAlterado" + EstabProvider.getSistemaTrabalho()), Toast.LENGTH_LONG).show();
        			MesaProvider.atualiza(retorno, this);
        			atualizaAdapter();
    			}
    		}
	    }
    }

	private void carregaLabels() {
		Util.carregaTitulo(this);
		TextView txListaMesas = (TextView)findViewById(R.id.txListaMesas);
		txListaMesas.setText(Util.getStringResourceByName(this, "strLista" + EstabProvider.getSistemaTrabalho()));
	}

	private void atualizaAdapter() {
		m_adapter = new MenuItemAdapter(this, R.layout.rowlistamesas, MesaProvider.getMesa());
		setListAdapter(m_adapter);
	}
	
	protected void onRestart(){
		super.onRestart();
		atualizaAdapter();
	}
}
