package com.qmenu.control;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;

import com.qmenu.model.Adicionais;
import com.qmenu.model.Item;
import com.qmenu.util.DAO;

@SuppressLint("UseSparseArrays")
public class AdicionaisProvider {
	
	private static HashMap<Integer, Adicionais> l_adicionais = new HashMap<Integer, Adicionais>();
	
	public static void atualiza(String xml, Activity a){
		DAO rs = new DAO(xml, a);
		int grupo = -1;
		Adicionais o = null;
		while(rs.next()){
			if(grupo != rs.getInt("grupo_adicionais_id")){
				grupo = rs.getInt("grupo_adicionais_id");
				o = new Adicionais();
				o.setGrupoadicionais(rs.getString("grupo_adicionais_id"));
				o.setDescricaoestab(rs.getString("descricaoestab"));
				l_adicionais.put(rs.getInt("grupo_adicionais_id"),o);
			}
			Item item = new Item();
			item.setCodigo(rs.getInt("codigoitem"));
			item.setDescricao(rs.getString("descricaoitem"));
			item.setDescricaoestab(rs.getString("descricaoestab"));
			item.setGrupoadicionais(rs.getInt("grupo_adicionais_id"));
			item.setPreco(rs.getDouble("preco"));
			o.getItem().add(item);
			ItemProvider.addItem(rs.getInt("codigoitem"), item);
		}
	}
	
	public static HashMap<Integer, Adicionais> getAdicionais() {
		return l_adicionais;
	}
	
	public static Adicionais getItem(Integer codigo){
		return l_adicionais.get(codigo);
	}
}
