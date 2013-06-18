package com.qmenu.control;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;

import com.qmenu.model.Item;
import com.qmenu.model.MPrincipal;
import com.qmenu.util.DAO;

public class MenuProvider {
	
	private static ArrayList<MPrincipal> menu = new ArrayList<MPrincipal>();
	@SuppressLint("UseSparseArrays")
	private static HashMap<Integer, MPrincipal> h_menu = new HashMap<Integer, MPrincipal>();
	
	public static void atualiza(String xml, Activity a){
		DAO rs = new DAO(xml, a);
		if(rs.next()){
			menu = new ArrayList<MPrincipal>();
			MPrincipal mprincipal = null;
			do{
				if(mprincipal == null || !mprincipal.getCodigo().equals(rs.getString("codigogrupo"))){
					mprincipal = new MPrincipal();
					mprincipal.setCodigo(rs.getString("codigogrupo"));
					mprincipal.setDescricao(rs.getString("descricaogrupo"));
					mprincipal.setTipocobranca(rs.getString("tipo_cobranca"));
					mprincipal.setQtdeitem(rs.getInt("qtdeitem"));
					menu.add(mprincipal);
					h_menu.put(rs.getInt("codigogrupo"), mprincipal);
				}
				Item item = new Item();
				item.setCodigo(rs.getInt("codigoitem"));
				item.setDescricao(rs.getString("descricaoitem"));
				item.setDescricaoestab(rs.getString("descricaoestab"));
				item.setGrupoadicionais(rs.getInt("grupo_adicionais_id"));
				item.setPreco(rs.getDouble("preco"));
				mprincipal.getItem().add(item);
				ItemProvider.addItem(rs.getInt("codigoitem"), item);
			}while(rs.next());
		}
	}

	public static ArrayList<MPrincipal> getMenu() {
		return menu;
	}
	
	public static MPrincipal getMPrincipal(Integer codigo){
		return h_menu.get(codigo);
	}

	public static void limpaMenu(){
		menu = new ArrayList<MPrincipal>();
	}
}
