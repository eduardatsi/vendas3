package br.edu.ifsul.vendas3.setup;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifsul.vendas3.model.Cliente;
import br.edu.ifsul.vendas3.model.ItemPedido;
import br.edu.ifsul.vendas3.model.Produto;

public class AppSetup {

    public static List<Produto> produtos = new ArrayList<>();
    public static List<Cliente> clientes = new ArrayList<>();
    public static Cliente cliente = null;
    public static List<ItemPedido> carrinho = new ArrayList<>();}
