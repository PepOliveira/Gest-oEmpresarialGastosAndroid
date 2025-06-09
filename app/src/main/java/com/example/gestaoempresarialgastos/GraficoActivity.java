package com.example.gestaoempresarialgastos;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.firestore.*;

import java.util.*;

public class GraficoActivity extends AppCompatActivity {

    private PieChart pieChart;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico);

        pieChart = findViewById(R.id.pieChart);
        db = FirebaseFirestore.getInstance();

        carregarDadosGrafico();
    }

    private void carregarDadosGrafico() {
        db.collection("gastos")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Map<String, Double> mapaCategoriaValores = new HashMap<>();

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String categoria = doc.getString("categoria");
                        Double valor = doc.getDouble("valor");

                        if (categoria != null && valor != null) {
                            mapaCategoriaValores.put(categoria,
                                    mapaCategoriaValores.getOrDefault(categoria, 0.0) + valor);
                        }
                    }

                    montarGrafico(mapaCategoriaValores);
                })
                .addOnFailureListener(e -> {
                    // Trate erros, como exibir um Toast
                    e.printStackTrace();
                });
    }

    private void montarGrafico(Map<String, Double> dados) {
        List<PieEntry> entradas = new ArrayList<>();
        for (Map.Entry<String, Double> entry : dados.entrySet()) {
            entradas.add(new PieEntry(entry.getValue().floatValue(), entry.getKey()));
        }

        PieDataSet dataSet = new PieDataSet(entradas, "Gastos por Categoria");
        dataSet.setColors(Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.MAGENTA, Color.CYAN);
        dataSet.setValueTextSize(14f);
        dataSet.setValueTextColor(Color.WHITE);

        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.setUsePercentValues(true);
        pieChart.setCenterText("Categorias");
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(12f);
        pieChart.animateY(1000);
        pieChart.invalidate(); // Atualiza o gráfico
    }
}
