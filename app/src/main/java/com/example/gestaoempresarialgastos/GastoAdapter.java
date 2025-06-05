package com.example.gestaoempresarialgastos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GastoAdapter extends RecyclerView.Adapter<GastoAdapter.ViewHolder> {
    private List<Gasto> lista;
    private Context context;

    public GastoAdapter(List<Gasto> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public GastoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_gasto, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GastoAdapter.ViewHolder holder, int position) {
        Gasto gasto = lista.get(position);
        holder.txtNome.setText(gasto.getNome());
        holder.txtCategoria.setText("Categoria: " + gasto.getCategoria());
        holder.txtValor.setText("R$ " + String.format("%.2f", gasto.getValor()));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNome, txtCategoria, txtValor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNome = itemView.findViewById(R.id.txtNome);
            txtCategoria = itemView.findViewById(R.id.txtCategoria);
            txtValor = itemView.findViewById(R.id.txtValor);
        }
    }
}
