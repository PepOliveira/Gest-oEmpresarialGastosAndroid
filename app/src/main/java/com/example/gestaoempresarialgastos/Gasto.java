package com.example.gestaoempresarialgastos;

public class Gasto {
    private String nome;
    private String categoria;
    private double valor;

    public Gasto() {}

    public Gasto(String nome, String categoria, double valor) {
        this.nome = nome;
        this.categoria = categoria;
        this.valor = valor;
    }

    public String getNome() {
        return nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public double getValor() {
        return valor;
    }
}
