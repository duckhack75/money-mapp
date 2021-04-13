package com.example.moneymapp.mapa.salidas.model;

public class Gasto {
    private String correo;
    private String presupuestoTotal;
    private String diaInicio;
    private String mesInicio;
    private String anioInicio;
    private String estadoPresupuesto;

    public Gasto() {
    }

    public Gasto(String correo, String presupuestoTotal, String diaInicio, String mesInicio,
                 String anioInicio, String estadoPresupuesto) {
        this.correo = correo;
        this.presupuestoTotal = presupuestoTotal;
        this.diaInicio = diaInicio;
        this.mesInicio = mesInicio;
        this.anioInicio = anioInicio;
        this.estadoPresupuesto = estadoPresupuesto;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPresupuestoTotal() {
        return presupuestoTotal;
    }

    public void setPresupuestoTotal(String presupuestoTotal) {
        this.presupuestoTotal = presupuestoTotal;
    }

    public String getDiaInicio() {
        return diaInicio;
    }

    public void setDiaInicio(String diaInicio) {
        this.diaInicio = diaInicio;
    }

    public String getMesInicio() {
        return mesInicio;
    }

    public void setMesInicio(String mesInicio) {
        this.mesInicio = mesInicio;
    }

    public String getAnioInicio() {
        return anioInicio;
    }

    public void setAnioInicio(String anioInicio) {
        this.anioInicio = anioInicio;
    }

    public String getEstadoPresupuesto() {
        return estadoPresupuesto;
    }

    public void setEstadoPresupuesto(String estadoPresupuesto) {
        this.estadoPresupuesto = estadoPresupuesto;
    }
}
