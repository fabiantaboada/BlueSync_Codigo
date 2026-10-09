package modelo;

import java.util.List;

public class ServicioSuministro extends Servicio {
    private List<Equipo> equiposSeleccionados;

    public ServicioSuministro() {}

    public void establecerEquipos(List<Equipo> equiposSeleccionados) {
        this.equiposSeleccionados = equiposSeleccionados;
    }

    @Override
    public double calcularCosto() {
        if (equiposSeleccionados == null) return 0.0;
        return equiposSeleccionados.stream()
            .filter(equipo -> equipo.obtenerCantidadEnAlmacén() > 0)
            .map(Equipo::obtenerPrecioBase)
            .reduce(0.0, Double::sum);
    }

    public double calcularPresupuestoFinal(List<Equipo> lista) {
        return lista.stream()
            .filter(equipo -> equipo.obtenerCantidadEnAlmacén() > 0)
            .map(equipo -> equipo.obtenerPrecioBase() * (1 + TASA_IGV))
            .reduce(0.0, Double::sum);
    }
}