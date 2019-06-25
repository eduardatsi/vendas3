package br.edu.ifsul.vendas3.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.edu.ifsul.vendas3.R;
import br.edu.ifsul.vendas3.adapter.CarrinhoAdapter;
import br.edu.ifsul.vendas3.model.ItemPedido;
import br.edu.ifsul.vendas3.model.Pedido;
import br.edu.ifsul.vendas3.setup.AppSetup;

public class CarrinhoActivity extends AppCompatActivity {
    private ListView lv_carrinho;
    private double total;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setContentView(R.layout.activity_carrinho);
        TextView tvClienteCarrinho = findViewById(R.id.tvClienteCarrinho);
        tvClienteCarrinho.setText(String.valueOf(AppSetup.cliente.getNome().concat(" " + AppSetup.cliente.getSobrenome())));
        lv_carrinho = findViewById(R.id.lv_carrinho);

        atualizaView();

        lv_carrinho.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                excluiItem(position);
                return true;
            }
        });

        lv_carrinho.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editaItem(position);
            }
        });


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_carrinho, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuitem_salvar:
                confirmaSalvar();
                break;
            case R.id.menuitem_cancelar:
                confirmaCancelar();
                break;
            case R.id.home:
                finish();
                break;
        }
        return true;
    }

    private void editaItem(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.title_confimar);
        builder.setMessage(R.string.mesagem_edita);

        builder.setPositiveButton(R.string.alertdialog_sim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which){

                Intent intent = new Intent(CarrinhoActivity.this, ProdutoDetalheActivity.class);
                intent.putExtra("position", AppSetup.produtos.get(position).getIndex());
                atualizarEstoque(position);
                finish();
                startActivity(intent);
            }

        });
        builder.setNegativeButton(R.string.alertdialog_nao, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }

    private void excluiItem(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_confimar);
        builder.setMessage(R.string.mensagem_exclui);
        builder.setPositiveButton(R.string.alertdialog_sim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                atualizarEstoque(position);
            }
        });
        builder.setNegativeButton(R.string.alertdialog_nao, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }

    private void confirmaCancelar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.title_confimar);
        builder.setMessage(R.string.message_confirma_cancelar);

        builder.setPositiveButton(R.string.alertdialog_sim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("tamanho", String.valueOf(AppSetup.carrinho.size()));
                for (ItemPedido item : AppSetup.carrinho) {
                    DatabaseReference myRef = database.getReference("produtos/" + item.getProduto().getKey() + "/quantidade");
                    myRef.setValue(item.getQuantidade() + item.getProduto().getQuantidade());
                    Log.d("removido", item.toString());
                    Log.d("item", "Item Removido");
                }
                AppSetup.carrinho.clear();
                AppSetup.cliente = null;
                finish();
            }
        });
        builder.setNegativeButton(R.string.alertdialog_nao, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void confirmaSalvar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_confimar);
        builder.setMessage(R.string.message_confirma_salvar);

        builder.setPositiveButton(R.string.alertdialog_sim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (AppSetup.carrinho == null) {
                    Toast.makeText(CarrinhoActivity.this, getString(R.string.carrinho_vazio), Toast.LENGTH_SHORT).show();
                } else {
                    Date dataHoraAtual = new Date();

                    DatabaseReference myRef = database.getReference("pedidos");
                    String key = myRef.push().getKey();

                    Pedido pedido = new Pedido();
                    pedido.setCliente(AppSetup.cliente);
                    pedido.setDataCriacao(dataHoraAtual);
                    pedido.setDataModificacao(dataHoraAtual);
                    pedido.setEstado("emAberto");
                    pedido.setFormaDePagamento("aVista");
                    pedido.setItens(AppSetup.carrinho);
                    pedido.setKey(key);
                    pedido.setSituacao(true);
                    pedido.setTotalPedido(total);

                    myRef.child(key).setValue(pedido);

                    DatabaseReference myRef2 = database.getReference("clientes");
                    List<String> pedidos = new ArrayList<>();
                    pedidos.addAll(AppSetup.cliente.getPedidos());
                    pedidos.add(key);
                    AppSetup.cliente.setPedidos(pedidos);
                    myRef2.child(AppSetup.cliente.getKey()).setValue(AppSetup.cliente);

                    AppSetup.clientes = null;
                    AppSetup.carrinho.clear();
                    AppSetup.pedido = null;
                    finish();
                }
            }
        });
        builder.setNegativeButton(R.string.alertdialog_nao, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }

    public void atualizaView() {
        TextView tvTotalPedidoCarrinho = findViewById(R.id.tvTotalPedidoCarrinho);
        lv_carrinho.setAdapter(new CarrinhoAdapter(CarrinhoActivity.this, AppSetup.carrinho));
        Double total = 0.00;
        for (ItemPedido itemPedido : AppSetup.carrinho) {
            total = total + itemPedido.getTotalItem();
        }
        tvTotalPedidoCarrinho.setText(NumberFormat.getCurrencyInstance().format(total));
    }
    public void atualizarEstoque(int position) {
        DatabaseReference myRef = database.getReference("produtos/" + AppSetup.carrinho.get(position).getProduto().getKey() + "/quantidade");
        myRef.setValue(AppSetup.carrinho.get(position).getQuantidade() + AppSetup.carrinho.get(position).getProduto().getQuantidade());

        Log.d("removido", AppSetup.carrinho.get(position).toString());
        AppSetup.carrinho.remove(position);
        Log.d("item", "Item Removido!");

        atualizaView();

        Toast.makeText(CarrinhoActivity.this, "Item Removido da Venda com Sucesso", Toast.LENGTH_SHORT).show();
    }
}