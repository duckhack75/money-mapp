package com.example.moneymapp.informe;

public class Informes {
    private String correo;
    private String anio;
    private String mes;
    private String dia;
    private String activos;
    private String pasivos;
    private String riquezaNeta;
    private String entradas;
    private String salidas;
    private String flujoEfectivoNeto;
    private String estadoRegistro;

    public Informes() {
    }

    public Informes(String correo, String anio, String mes, String dia, String activos,
                    String pasivos, String riquezaNeta, String entradas, String salidas,
                    String flujoEfectivoNeto, String estadoRegistro) {
        this.correo = correo;
        this.anio = anio;
        this.mes = mes;
        this.dia = dia;
        this.activos = activos;
        this.pasivos = pasivos;
        this.riquezaNeta = riquezaNeta;
        this.entradas = entradas;
        this.salidas = salidas;
        this.flujoEfectivoNeto = flujoEfectivoNeto;
        this.estadoRegistro = estadoRegistro;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getActivos() {
        return activos;
    }

    public void setActivos(String activos) {
        this.activos = activos;
    }

    public String getPasivos() {
        return pasivos;
    }

    public void setPasivos(String pasivos) {
        this.pasivos = pasivos;
    }

    public String getRiquezaNeta() {
        return riquezaNeta;
    }

    public void setRiquezaNeta(String riquezaNeta) {
        this.riquezaNeta = riquezaNeta;
    }

    public String getEntradas() {
        return entradas;
    }

    public void setEntradas(String entradas) {
        this.entradas = entradas;
    }

    public String getSalidas() {
        return salidas;
    }

    public void setSalidas(String salidas) {
        this.salidas = salidas;
    }

    public String getFlujoEfectivoNeto() {
        return flujoEfectivoNeto;
    }

    public void setFlujoEfectivoNeto(String flujoEfectivoNeto) {
        this.flujoEfectivoNeto = flujoEfectivoNeto;
    }

    public String getEstadoRegistro() {
        return estadoRegistro;
    }

    public void setEstadoRegistro(String estadoRegistro) {
        this.estadoRegistro = estadoRegistro;
    }
}
