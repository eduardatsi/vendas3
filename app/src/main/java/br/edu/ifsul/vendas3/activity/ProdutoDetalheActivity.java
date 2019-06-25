package br.edu.ifsul.vendas3.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;

import br.edu.ifsul.vendas3.R;
import br.edu.ifsul.vendas3.model.ItemPedido;
import br.edu.ifsul.vendas3.model.Produto;
import br.edu.ifsul.vendas3.setup.AppSetup;

public class ProdutoDetalheActivity extends AppCompatActivity {

    private static final String TAG = "produtoDetalheActivity";
    private TextView tvNome, tvDescricao, tvValor, tvEstoque;
    private EditText etQuantidade;
    private ImageView imvFoto;
    private Button btVender;
    private Produto produto;
    private ProgressBar pbFotoProdutoDetalhes;
    private Bitmap fotoBitmap;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override

    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_produto_detalhe);

        tvNome = findViewById(R.id.tvNomeProduto);
        tvDescricao = findViewById(R.id.tvDerscricaoProduto);
        tvValor = findViewById(R.id.tvValorProduto);
        tvEstoque = findViewById(R.id.tvQuantidadeProduto);
        etQuantidade = findViewById(R.id.etQuantidade);
        imvFoto = findViewById(R.id.imvFoto);
        btVender = findViewById(R.id.btComprarProduto);
        tvVendedor = findViewById(R.id.btComprarProduto);
        pbFotoProdutoDetalhes = findViewById(R.id.pbFotoProdutoDetalhes);

        Integer position = getIntent().getExtras().getInt("position");
        produto = AppSetup.produtos.get(position);
        //Log.d(TAG, "" + produto.equals(AppSetup.produtos.get(position)));


        tvNome.setText(produto.getNome());
        tvDescricao.setText((produto.getDescricao()));
        tvValor.setText(NumberFormat.getCurrencyInstance().format(AppSetup.produtos.get(position).getValor()));
        tvVendedor.setText(AppSetup.user.getEmail());

        if(produto.getUrl_foto().equals("")) {
            pbFotoProdutoDetalhes.setVisibility(ProgressBar.INVISIBLE);
        } else {
            //faz o download do servidor
            if(AppSetup.cacheProdutos.get(produto.getKey()) == null){
                StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("produtos/" + produto.getCodigoDeBarras() + ".jpeg");
                final long ONE_MEGABYTE = 1024 * 1024;
                mStorageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        fotoBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        imvFoto.setImageBitmap(fotoBitmap);
                        AppSetup.cacheProdutos.put(produto.getKey(), fotoBitmap);
                        pbFotoProdutoDetalhes.setVisibility(ProgressBar.INVISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d(TAG, "Download da foto do produto falhou: " + "produtos/" + produto.getCodigoDeBarras() + ".jpeg");
                    }
                });
            }else{
                imvFoto.setImageBitmap(AppSetup.cacheProdutos.get(produto.getKey()));
                pbFotoProdutoDetalhes.setVisibility(ProgressBar.INVISIBLE);
            }
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("produtos/" + produto.getKey() + "/quantidade");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer quantidade = dataSnapshot.getValue(Integer.class);
                tvEstoque.setText(String.format("%s %s",getString(R.string.label_estoque), quantidade.toString()));
                produto.setQuantidade(quantidade);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btVender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppSetup.cliente == null){
                    startActivity(new Intent(ProdutoDetalheActivity.this, ClientesActivity.class));
                }else{
                    if(etQuantidade.getText().toString().isEmpty()){
                        Toast.makeText(ProdutoDetalheActivity.this, "Digite a quantidade a ser vendida.", Toast.LENGTH_SHORT).show();
                    }else{
                        Integer quantidade = Integer.valueOf(etQuantidade.getText().toString());
                        if(quantidade <= produto.getQuantidade()){

                            ItemPedido item = new ItemPedido();
                            item.setProduto(produto);
                            item.setQuantidade(quantidade);
                            item.setTotalItem(quantidade * produto.getValor());
                            item.setSituacao(true);
                            AppSetup.carrinho.add(item);
                            myRef.setValue(produto.getQuantidade() - quantidade);
                            Toast.makeText(ProdutoDetalheActivity.this, getString(R.string.toast_adicionado_ao_carrinho), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ProdutoDetalheActivity.this, CarrinhoActivity.class));
                            finish();
                        }else{
                            Toast.makeText(ProdutoDetalheActivity.this, "Não possuimos esta quantidade em estoque. Por favor tente novamente com uma quantidade menor", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
}
