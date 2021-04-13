package com.example.moneymapp.presupuesto.model;

public class Seguro {
    private String correo;
    private String seguroAuto;
    private String seguroVivienda;
    private String seguroVida;
    private String seguroMedico;
    private String otrosSeguros;
    private String totalSeguros;


    public Seguro() {
    }

    public Seguro(String correo, String seguroAuto, String seguroVivienda, String seguroVida,
                  String seguroMedico, String otrosSeguros, String totalSeguros) {
        this.correo = correo;
        this.seguroAuto = seguroAuto;
        this.seguroVivienda = seguroVivienda;
        this.seguroVida = seguroVida;
        this.seguroMedico = seguroMedico;
        this.otrosSeguros = otrosSeguros;
        this.totalSeguros = totalSeguros;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getSeguroAuto() {
        return seguroAuto;
    }

    public void setSeguroAuto(String seguroAuto) {
        this.seguroAuto = seguroAuto;
    }

    public String getSeguroVivienda() {
        return seguroVivienda;
    }

    public void setSeguroVivienda(String seguroVivienda) {
        this.seguroVivienda = seguroVivienda;
    }

    public String getSeguroVida() {
        return seguroVida;
    }

    public void setSeguroVida(String seguroVida) {
        this.seguroVida = seguroVida;
    }

    public String getSeguroMedico() {
        return seguroMedico;
    }

    public void setSeguroMedico(String seguroMedico) {
        this.seguroMedico = seguroMedico;
    }

    public String getOtrosSeguros() {
        return otrosSeguros;
    }

    public void setOtrosSeguros(String otrosSeguros) {
        this.otrosSeguros = otrosSeguros;
    }

    public String getTotalSeguros() {
        return totalSeguros;
    }

    public void setTotalSeguros(String totalSeguros) {
        this.totalSeguros = totalSeguros;
    }
}
