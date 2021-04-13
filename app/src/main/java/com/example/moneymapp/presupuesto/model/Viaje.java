package com.example.moneymapp.presupuesto.model;

public class Viaje {

    private String correo;
    private String hotel;
    private String transporte;
    private String comidas;
    private String entretenimiento;
    private String otrosGastosViajes;
    private String totalViajes;


    public Viaje() {
    }

    public Viaje(String correo, String hotel, String transporte, String comidas, String entretenimiento,
                 String otrosGastosViajes, String totalViajes) {
        this.correo = correo;
        this.hotel = hotel;
        this.transporte = transporte;
        this.comidas = comidas;
        this.entretenimiento = entretenimiento;
        this.otrosGastosViajes = otrosGastosViajes;
        this.totalViajes = totalViajes;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public String getTransporte() {
        return transporte;
    }

    public void setTransporte(String transporte) {
        this.transporte = transporte;
    }

    public String getComidas() {
        return comidas;
    }

    public void setComidas(String comidas) {
        this.comidas = comidas;
    }

    public String getEntretenimiento() {
        return entretenimiento;
    }

    public void setEntretenimiento(String entretenimiento) {
        this.entretenimiento = entretenimiento;
    }

    public String getOtrosGastosViajes() {
        return otrosGastosViajes;
    }

    public void setOtrosGastosViajes(String otrosGastosViajes) {
        this.otrosGastosViajes = otrosGastosViajes;
    }

    public String getTotalViajes() {
        return totalViajes;
    }

    public void setTotalViajes(String totalViajes) {
        this.totalViajes = totalViajes;
    }
}

