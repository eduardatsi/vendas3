package br.edu.ifsul.vendas3.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifsul.vendas3.R;
import br.edu.ifsul.vendas3.adapter.ClientesAdapter;
import br.edu.ifsul.vendas3.model.Cliente;
import br.edu.ifsul.vendas3.model.Produto;

public class ClientesActivity extends AppCompatActivity {
    private static final String TAG = "clientesactivity";
    private ListView lvClientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        lvClientes = findViewById(R.id.lv_clientes);

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("clientes");

        // Read from the database
        myRef.orderByChild("nome").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //imprime os dados originais no LogCat (veja que eles chegam na ordem de criação dos nós)
                Log.d(TAG, "Value is: " + dataSnapshot.getValue());

                List<Cliente> clientes = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Cliente cliente = ds.getValue(Cliente.class);
                    cliente.setKey(ds.getKey()); //armazena a UUID gerada pelo banco
                    clientes.add(cliente);
                }

                //carrega os dados na View
                lvClientes.setAdapter(new ClientesAdapter(ClientesActivity.this, clientes));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}
