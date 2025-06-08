package com.example.gestaoempresarialgastos;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private GastoAdapter adapter;
    private List<Gasto> listaGastos = new ArrayList<>();
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private Button btnLogout, btnAddGasto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recyclerView);
        btnLogout = findViewById(R.id.btnLogout);
        btnAddGasto = findViewById(R.id.btnAddGasto);

        adapter = new GastoAdapter(listaGastos, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        buscarGastos();

        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        btnAddGasto.setOnClickListener(v -> mostrarDialogAdicionarGasto());
    }

    private void buscarGastos() {
        db.collection("gastos")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    listaGastos.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Gasto gasto = doc.toObject(Gasto.class);
                        listaGastos.add(gasto);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Erro ao carregar dados", Toast.LENGTH_SHORT).show()
                );
    }

    private void mostrarDialogAdicionarGasto() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Adicionar Gasto");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        EditText inputNome = new EditText(this);
        inputNome.setHint("Nome");
        layout.addView(inputNome);

        EditText inputCategoria = new EditText(this);
        inputCategoria.setHint("Categoria");
        layout.addView(inputCategoria);

        EditText inputValor = new EditText(this);
        inputValor.setHint("Valor");
        inputValor.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        layout.addView(inputValor);

        builder.setView(layout);

        builder.setPositiveButton("Salvar", (dialog, which) -> {
            String nome = inputNome.getText().toString().trim();
            String categoria = inputCategoria.getText().toString().trim();
            String valorStr = inputValor.getText().toString().trim();

            if (nome.isEmpty() || categoria.isEmpty() || valorStr.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            double valor = Double.parseDouble(valorStr);

            Gasto novoGasto = new Gasto(nome, categoria, valor);

            db.collection("gastos")
                    .add(novoGasto)
                    .addOnSuccessListener(documentReference -> {
                        listaGastos.add(novoGasto);
                        adapter.notifyItemInserted(listaGastos.size() - 1);
                        Toast.makeText(this, "Gasto adicionado", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Erro ao salvar gasto", Toast.LENGTH_SHORT).show()
                    );
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}
