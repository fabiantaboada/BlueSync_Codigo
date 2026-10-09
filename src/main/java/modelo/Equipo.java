package modelo;

public class Equipo {
    private String nombre;
    private double precioBase;
    private int cantidadEnAlmacén;

    public Equipo(String nombre, double precioBase, int cantidadEnAlmacén) {
        this.nombre = nombre;
        this.precioBase = precioBase;
        this.cantidadEnAlmacén = cantidadEnAlmacén;
    }

    public String obtenerNombre() { return nombre; }
    public double obtenerPrecioBase() { return precioBase; }
    public int obtenerCantidadEnAlmacén() { return cantidadEnAlmacén; }

    public void reducirInventario() {
        if (this.cantidadEnAlmacén > 0) {
            this.cantidadEnAlmacén--;
        }
    }

    @Override
    public String toString() {
        return nombre + " - S/ " + String.format("%.2f", precioBase) + " (Disponible: " + cantidadEnAlmacén + ")";
    }
}