package br.edu.ifsul.vendas3.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;

import br.edu.ifsul.vendas3.R;
import br.edu.ifsul.vendas3.model.Cliente;
import br.edu.ifsul.vendas3.model.Produto;

public class ClientesAdapter extends ArrayAdapter<Cliente> {
    private final Context context;

    public ClientesAdapter(@NonNull Context context, @NonNull List<Cliente> clientes) {
        super(context, 0, clientes);
        this.context = context;
    }

    @SuppressLint("SetTextI18n")
    @SuppressWarnings("StatementWithEmptyBody")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Cliente cliente = getItem(position);

        //infla a view
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_cliente_adapter, parent, false);
        }

        //mapeia os componentes da UI para vincular os dados do objeto de modelo
        TextView tvNome = convertView.findViewById(R.id.tvNomeClienteAdapter);
        ImageView imvFoto = convertView.findViewById(R.id.imvFotoClienteAdapter);
        TextView tvDetalhes = convertView.findViewById(R.id.tvDerscricaoProduto);

        assert cliente != null;
        tvNome.setText(cliente.getNome() + " " + cliente.getSobrenome());
        if(cliente.getUrl_foto().equals("")){

        }else{
            imvFoto.setImageResource(R.drawable.img_cliente_icon_524x524);
        }


        return convertView;
    }
}
