package br.edu.ifsul.vendas3.setup;

import android.graphics.Bitmap;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.edu.ifsul.vendas3.model.Cliente;
import br.edu.ifsul.vendas3.model.ItemPedido;
import br.edu.ifsul.vendas3.model.Pedido;
import br.edu.ifsul.vendas3.model.Produto;
import br.edu.ifsul.vendas3.model.User;

public class AppSetup {

    public static List<Produto> produtos = new ArrayList<>();
    public static List<Cliente> clientes = new ArrayList<>();
    public static List<ItemPedido> carrinho = new ArrayList<>();
    public static List<Pedido> pedidos = new ArrayList<>();
    public static Cliente cliente = null;
    public static Pedido pedido = null;
    public static User user = null;
    public static FirebaseAuth mAuth = null;
    public static Map<String, Bitmap> cacheProdutos = new HashMap<>();
    public static Map<String, Bitmap> cacheClientes = new HashMap<>();
}
