package com.example.moneymapp.presupuesto.model;

public class Alimento {
    private String correo;
    private String supermercado;
    private String comidaFuera;
    private String otrosAlimentos;
    private String totalAlimentos;

    public Alimento() {
    }

    public Alimento(String correo, String supermercado, String comidaFuera, String otrosAlimentos,
                    String totalAlimentos) {
        this.correo = correo;
        this.supermercado = supermercado;
        this.comidaFuera = comidaFuera;
        this.otrosAlimentos = otrosAlimentos;
        this.totalAlimentos = totalAlimentos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getSupermercado() {
        return supermercado;
    }

    public void setSupermercado(String supermercado) {
        this.supermercado = supermercado;
    }

    public String getComidaFuera() {
        return comidaFuera;
    }

    public void setComidaFuera(String comidaFuera) {
        this.comidaFuera = comidaFuera;
    }

    public String getOtrosAlimentos() {
        return otrosAlimentos;
    }

    public void setOtrosAlimentos(String otrosAlimentos) {
        this.otrosAlimentos = otrosAlimentos;
    }

    public String getTotalAlimentos() {
        return totalAlimentos;
    }

    public void setTotalAlimentos(String totalAlimentos) {
        this.totalAlimentos = totalAlimentos;
    }
}
