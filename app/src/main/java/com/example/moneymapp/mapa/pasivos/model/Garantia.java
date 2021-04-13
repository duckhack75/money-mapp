package com.example.moneymapp.mapa.pasivos.model;

public class Garantia {
    private String correo;
    private String creditoHipotecario;
    private String creditoAutomotriz;
    private String prestamoConGarantia;
    private String empeno;
    private String totalGarantia;

    public Garantia() {
    }

    public Garantia(String correo, String creditoHipotecario, String creditoAutomotriz, String prestamoConGarantia,
                    String empeno, String totalGarantia) {
        this.correo = correo;
        this.creditoHipotecario = creditoHipotecario;
        this.creditoAutomotriz = creditoAutomotriz;
        this.prestamoConGarantia = prestamoConGarantia;
        this.empeno = empeno;
        this.totalGarantia = totalGarantia;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCreditoHipotecario() {
        return creditoHipotecario;
    }

    public void setCreditoHipotecario(String creditoHipotecario) {
        this.creditoHipotecario = creditoHipotecario;
    }

    public String getCreditoAutomotriz() {
        return creditoAutomotriz;
    }

    public void setCreditoAutomotriz(String creditoAutomotriz) {
        this.creditoAutomotriz = creditoAutomotriz;
    }

    public String getPrestamoConGarantia() {
        return prestamoConGarantia;
    }

    public void setPrestamoConGarantia(String prestamoConGarantia) {
        this.prestamoConGarantia = prestamoConGarantia;
    }

    public String getEmpeno() {
        return empeno;
    }

    public void setEmpeno(String empeno) {
        this.empeno = empeno;
    }

    public String getTotalGarantia() {
        return totalGarantia;
    }

    public void setTotalGarantia(String totalGarantia) {
        this.totalGarantia = totalGarantia;
    }
}
