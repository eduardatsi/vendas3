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

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_cliente_adapter, parent, false);
        }

        TextView tvNome = convertView.findViewById(R.id.tvNomeClienteAdapter);
        TextView tvSobrenome = convertView.findViewById(R.id.tvSobrenomeClienteAdapter);
        ImageView imvFoto = convertView.findViewById(R.id.imvFotoClienteAdapter);

        assert cliente != null;
        tvNome.setText(cliente.getNome());
        tvSobrenome.setText(cliente.getSobrenome());
        if(cliente.getUrl_foto() != null){

        }else{
            imvFoto.setImageResource(R.drawable.img_cliente_icon_524x524);
        }


        return convertView;
    }
}
