package br.edu.ifsul.vendas3.activity;

import android.content.DialogInterface;
import android.os.Bundle;
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

import br.edu.ifsul.vendas3.R;
import br.edu.ifsul.vendas3.adapter.CarrinhoAdapter;
import br.edu.ifsul.vendas3.model.ItemPedido;
import br.edu.ifsul.vendas3.setup.AppSetup;

public class CarrinhoActivity extends AppCompatActivity {
    private ListView lv_carrinho;
    private double total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);
        TextView tvTotalPedidoCarrinho = findViewById(R.id.tvTotalPedidoCarrinho);
        TextView tvClienteCarinho = findViewById(R.id.tvClienteCarrinho);

        lv_carrinho = findViewById(R.id.lv_carrinho);
        lv_carrinho.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editaItem(position);

            }
        });
        lv_carrinho.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                removeItem(position);
                return false;
            }
        });
        atualizaView();
        tvTotalPedidoCarrinho.setText(String.valueOf(total));
        Log.d("retorno", AppSetup.carrinho.toString());

        tvClienteCarinho.setText(String.valueOf(AppSetup.cliente.getNome().concat(" " + AppSetup.cliente.getSobrenome())));
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
        }
        return true;
    }

    private void editaItem(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //adiciona um título e uma mensagem
        builder.setTitle(R.string.title_confimar);
        builder.setMessage("Você tem certeza que deseja editar esse item?");
        //adiciona os botões
        builder.setPositiveButton(R.string.alertdialog_sim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

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
        //adiciona um título e uma mensagem
        builder.setTitle(R.string.title_confimar);
        builder.setMessage("Você tem certeza que deseja excluir esse item?");
        //adiciona os botões
        builder.setPositiveButton(R.string.alertdialog_sim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeItem(position);
            }
        });
        builder.setNegativeButton(R.string.alertdialog_nao, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }

    private void removeItem(int position) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("produtos/" + AppSetup.carrinho.get(position).getProduto().getKey() + "/quantidade");
        myRef.setValue(AppSetup.produtos.get(position).getQuantidade() + AppSetup.carrinho.get(position).getQuantidade());
        AppSetup.carrinho.remove(position);
        Log.d("item", "item removido");
        atualizaView();
        Toast.makeText(CarrinhoActivity.this, "Produto removido com sucesso!", Toast.LENGTH_SHORT).show();

    }

    private void confirmaCancelar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.title_confimar);
        builder.setMessage(R.string.message_confirma_cancelar);

        builder.setPositiveButton(R.string.alertdialog_sim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
        //adiciona um título e uma mensagem
        builder.setTitle(R.string.title_confimar);
        builder.setMessage(R.string.message_confirma_salvar);
        //adiciona os botões
        builder.setPositiveButton(R.string.alertdialog_sim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

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
        lv_carrinho.setAdapter(new CarrinhoAdapter(CarrinhoActivity.this, AppSetup.carrinho));
        total = 0;
        for (ItemPedido itemPedido : AppSetup.carrinho) {
            total = total + itemPedido.getTotalItem();
        }
    }
}