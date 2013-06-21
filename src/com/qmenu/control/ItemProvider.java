package com.qmenu.control;

import java.util.HashMap;

import android.annotation.SuppressLint;
import com.qmenu.model.Item;

@SuppressLint("UseSparseArrays")
public class ItemProvider {
	
	private static HashMap<Integer, Item> l_item = new HashMap<Integer, Item>();
	
	public static HashMap<Integer, Item> getItens() {
		return l_item;
	}

	public static void addItem(Integer codigo, Item item) {
		l_item.put(codigo, item);
	}
	
	public static Item getItem(Integer codigo){
		return l_item.get(codigo);
	}



}
