package br.edu.ifsul.vendas3.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;

import br.edu.ifsul.vendas3.R;
import br.edu.ifsul.vendas3.model.Produto;
import br.edu.ifsul.vendas3.setup.AppSetup;

public class ProdutoDetalheActivity extends Activity {

    private TextView tvNome, tvDescricao, tvValor, tvEstoque;
    private EditText etQuantidade;
    private ImageView imvFoto;
    private Button btVender;
    private Produto produto;

    @Override

    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_produto_detalhe);

        //mapeia os componentes da UI
        tvNome = findViewById(R.id.tvNomeProduto);
        tvDescricao = findViewById(R.id.tvDerscricaoProduto);
        tvValor = findViewById(R.id.tvValorProduto);
        tvEstoque = findViewById(R.id.tvQuantidadeProduto);
        etQuantidade = findViewById(R.id.etQuantidade);
        imvFoto = findViewById(R.id.imvFoto);
        btVender = findViewById(R.id.btComprarProduto);

        //obtém o objeto Produto anexado como metadado
        Integer position = getIntent().getExtras().getInt("position");
        produto = AppSetup.produtos.get(position);


        //bindView
        tvNome.setText(produto.getNome());
        tvDescricao.setText((produto.getDescricao()));
        tvValor.setText(NumberFormat.getCurrencyInstance().format(produto.getValor()));
        if(produto.getUrl_foto() != null) {
            //carrega a imagem aqui
        }
        tvEstoque.setText(String.format("%s %s", getString(R.string.label_estoque),produto.getQuantidade()));

    }
}
