package modelo;

public abstract class Servicio {
    protected final double TASA_IGV = 0.18;

    public double calcularIGV(double subtotal) {
        return subtotal * TASA_IGV;
    }

    public double calcularTotalConIGV(double subtotal) {
        return subtotal + calcularIGV(subtotal);
    }

    public abstract double calcularCosto();
}