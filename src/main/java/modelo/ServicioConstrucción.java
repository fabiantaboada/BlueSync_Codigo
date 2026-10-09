package modelo;

public class ServicioConstrucción extends Servicio {
    private double largo;
    private double ancho;
    private double costoBasePorMetroCuadrado;

    public ServicioConstrucción() {}

    public double calcularÁrea() {
        return largo * ancho;
    }

    @Override
    public double calcularCosto() {
        return calcularÁrea() * costoBasePorMetroCuadrado;
    }

    public double obtenerLargo() { return largo; }
    public void establecerLargo(double largo) { this.largo = largo; }

    public double obtenerAncho() { return ancho; }
    public void establecerAncho(double ancho) { this.ancho = ancho; }

    public double obtenerCostoBasePorMetroCuadrado() { return costoBasePorMetroCuadrado; }
    public void establecerCostoBasePorMetroCuadrado(double costoBasePorMetroCuadrado) { this.costoBasePorMetroCuadrado = costoBasePorMetroCuadrado; }
}