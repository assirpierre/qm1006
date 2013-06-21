package com.qmenu.control;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import android.app.Activity;

import com.qmenu.model.Pedido;
import com.qmenu.util.DAO;
import com.qmenu.util.Numero;

public class PedidoProvider {
	
	private static Pedido pedidoAtual;
	private static boolean novo;
	private static ArrayList<Pedido> pedido;
	private static ArrayList<Pedido> pedidoPendente = new ArrayList<Pedido>();
	private static double total;
	
	public static void atualiza(String xml, Activity a){
		total = 0;
		pedido = new ArrayList<Pedido>();
		DAO rs = new DAO(xml, a);
		while(rs.next()){
			Pedido o = new Pedido();
			o.setCodigo(rs.getString("sequencia"));
			o.setLinha(rs.getString("linha"));
			o.setDatapedido(rs.getString("data_pedido"));
			o.setObservacao(rs.getString("observacao"));
			o.setSituacao(rs.getString("situacao"));
			o.setUsuario(rs.getString("usuario"));
			o.setMprincipal(MenuProvider.getMPrincipal(rs.getInt("menu_principal_id")));
			o.setQtde((int)rs.getDouble("qtde"));
			o.setPrecoadicionais(rs.getDouble("preco_adicionais"));
			o.setTotal(rs.getDouble("total"));
			total += rs.getDouble("total");
            o.setItem(rs.getString("item"));
            o.setItemdescricao(rs.getString("item_descricao"));
            o.setItemadd(rs.getString("item_adicional"));
            o.setItemadddescricao(rs.getString("item_adicional_descricao"));
			pedido.add(o);
			if(o.getSituacao().equals("P"))
				pedidoPendente.add(o);
		}
	}

	public static ArrayList<Pedido> getPedido() {
		return pedido;
	}

	public static void setPedido(ArrayList<Pedido> pedido) {
		PedidoProvider.pedido = pedido;
	}

	public static ArrayList<Pedido> getPedidoPendente() {
		return pedidoPendente;
	}
	
	public static void setPedidoPendente(ArrayList<Pedido> pedidoPendente) {
		PedidoProvider.pedidoPendente = pedidoPendente;
	}

	public static void gravaPedidoPendente() {
		if(novo)
			pedidoPendente.add(pedidoAtual);
	}	

	public static String getXMLPedidoPendente() {
		String xml = "";
		try{
			if(pedidoPendente.size() > 0){
				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				Document document = null;			
				document = builder.newDocument();
				document.getXmlEncoding();
				Element e = document.createElement("cadastro");
				e.setAttribute("treg", "" + pedidoPendente.size());
				e.setAttribute("tcol", "32");
				for(Pedido p: pedidoPendente){
					Element el = document.createElement("registro");
					el.appendChild(getE(document, "datapedido", p.getDatapedido(), "d"));
					el.appendChild(getE(document, "observacao", p.getObservacao(), "t"));
					el.appendChild(getE(document, "grupoitem", "" + p.getMprincipal().getCodigo(), "t"));
					el.appendChild(getE(document, "preco", "" + p.getPrecounitario(), "t"));
					el.appendChild(getE(document, "precoadicionais", "" + p.getPrecoadicionais(), "t"));
					el.appendChild(getE(document, "qtde", "" + p.getQtde(), "t"));
					el.appendChild(getE(document, "total", p.getTotalF(), "t"));
                    String item = "", itemdescricao = "", itemadd = "", itemadddescricao = "";
					for(int i = 0; i <p.getL_item().size();i++){
                        item += p.getL_item().get(i).getCodigo() + (i==0?"":" - ");
                        itemdescricao += p.getL_item().get(i).getDescricaoestab() + (i==0?"":" - ");
                    }
					for(int i = 0; i <p.getL_itemadd().size();i++){
                        itemadd += p.getL_itemadd().get(i).getCodigo() + (i==0?"":" - ");
                        itemadddescricao += p.getL_itemadd().get(i).getDescricaoestab() + (i==0?"":" - ");
                    }
                    el.appendChild(getE(document, "item", item, "t"));
                    el.appendChild(getE(document, "itemdescricao", itemdescricao, "t"));
                    el.appendChild(getE(document, "itemadd", itemadd, "t"));
                    el.appendChild(getE(document, "itemadddescricao", itemadddescricao, "t"));
    				e.appendChild(el);
    			}
    			document.appendChild(e);
				ByteArrayOutputStream os = new ByteArrayOutputStream();   
				TransformerFactory tf = TransformerFactory.newInstance();   
				Transformer trans = tf.newTransformer();   
				trans.setOutputProperty("encoding", "ISO-8859-1");
				trans.transform(new DOMSource(document), new StreamResult(os));   
				xml = os.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			xml = "##ERRO##";
		}				
		return xml;
	}
	
	private static Element getE(Document document, String nome, String valor, String tipo){
		Element e = document.createElement(nome);
		e.setAttribute("tipo", tipo);
		e.setTextContent(valor);
		return e;
	}
	
	public static void limpaPendente(){
		pedidoPendente = new ArrayList<Pedido>();
	}

	public static Pedido getPedidoAtual() {
		return pedidoAtual;
	}

	public static void setPedidoAtual(Pedido pedidoAtual, boolean novo) {
		PedidoProvider.pedidoAtual = pedidoAtual;
		PedidoProvider.novo = novo;
	}

	public static boolean isNovo() {
		return novo;
	}
	
	public static String getTotal(){
		return Numero.formata(total, 2);
	}

	public static String getTotalPendente(){
		double total = 0;
		for(Pedido p: pedidoPendente)
			total += p.getTotal();
		return Numero.formata(total, 2);
	}
}
