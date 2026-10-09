package modelo;

public class ServicioMantenimiento extends Servicio {
    private int horas;
    private double tarifaPorHora;
    private boolean requiereQuímicos;
    private double costoQuímicos;

    public ServicioMantenimiento() {}

    @Override
    public double calcularCosto() {
        double costoBase = horas * tarifaPorHora;
        if (requiereQuímicos) {
            costoBase += costoQuímicos;
        }
        return costoBase;
    }
    
    public int obtenerHoras() { return horas; }
    public void establecerHoras(int horas) { this.horas = horas; }

    public double obtenerTarifaPorHora() { return tarifaPorHora; }
    public void establecerTarifaPorHora(double tarifaPorHora) { this.tarifaPorHora = tarifaPorHora; }

    public boolean requiereLosQuímicos() { return requiereQuímicos; }
    public void establecerRequiereQuímicos(boolean requiereQuímicos) { this.requiereQuímicos = requiereQuímicos; }

    public double obtenerCostoQuímicos() { return costoQuímicos; }
    public void establecerCostoQuímicos(double costoQuímicos) { this.costoQuímicos = costoQuímicos; }
}