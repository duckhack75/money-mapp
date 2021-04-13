package com.example.moneymapp.deudas;

public class Deuda {
    private String correo;
    private String nombreDeuda;
    private String montoDeuda;
    private String tipoDeuda;
    private String estado;
    private String montoPagado;


    public Deuda() {
    }

    public Deuda(String correo, String nombreDeuda, String montoDeuda, String tipoDeuda,
                 String estado, String monto_pagado) {
        this.correo = correo;
        this.nombreDeuda = nombreDeuda;
        this.montoDeuda = montoDeuda;
        this.tipoDeuda = tipoDeuda;
        this.estado = estado;
        this.montoPagado = monto_pagado;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombreDeuda() {
        return nombreDeuda;
    }

    public void setNombreDeuda(String nombreDeuda) {
        this.nombreDeuda = nombreDeuda;
    }

    public String getMontoDeuda() {
        return montoDeuda;
    }

    public void setMontoDeuda(String montoDeuda) {
        this.montoDeuda = montoDeuda;
    }

    public String getTipoDeuda() {
        return tipoDeuda;
    }

    public void setTipoDeuda(String tipoDeuda) {
        this.tipoDeuda = tipoDeuda;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMonto_pagado() {
        return montoPagado;
    }

    public void setMonto_pagado(String monto_pagado) {
        this.montoPagado = monto_pagado;
    }
}
