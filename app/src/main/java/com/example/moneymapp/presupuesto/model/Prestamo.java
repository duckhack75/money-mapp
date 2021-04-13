package com.example.moneymapp.presupuesto.model;

public class Prestamo {
    private String correo;
    private String hipoteca;
    private String mensualidadAuto;
    private String tarjetaCredito;
    private String prestamo;
    private String otrosPrestamos;
    private String totalPrestamos;

    public Prestamo() {
    }

    public Prestamo(String correo, String hipoteca, String mensualidadAuto, String tarjetaCredito,
                    String prestamo, String otrosPrestamos, String totalPrestamos) {
        this.correo = correo;
        this.hipoteca = hipoteca;
        this.mensualidadAuto = mensualidadAuto;
        this.tarjetaCredito = tarjetaCredito;
        this.prestamo = prestamo;
        this.otrosPrestamos = otrosPrestamos;
        this.totalPrestamos = totalPrestamos;
    }


    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getHipoteca() {
        return hipoteca;
    }

    public void setHipoteca(String hipoteca) {
        this.hipoteca = hipoteca;
    }

    public String getMensualidadAuto() {
        return mensualidadAuto;
    }

    public void setMensualidadAuto(String mensualidadAuto) {
        this.mensualidadAuto = mensualidadAuto;
    }

    public String getTarjetaCredito() {
        return tarjetaCredito;
    }

    public void setTarjetaCredito(String tarjetaCredito) {
        this.tarjetaCredito = tarjetaCredito;
    }

    public String getPrestamo() {
        return prestamo;
    }

    public void setPrestamo(String prestamo) {
        this.prestamo = prestamo;
    }

    public String getOtrosPrestamos() {
        return otrosPrestamos;
    }

    public void setOtrosPrestamos(String otrosPrestamos) {
        this.otrosPrestamos = otrosPrestamos;
    }

    public String getTotalPrestamos() {
        return totalPrestamos;
    }

    public void setTotalPrestamos(String totalPrestamos) {
        this.totalPrestamos = totalPrestamos;
    }
}
