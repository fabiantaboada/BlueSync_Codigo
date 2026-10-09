package persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import modelo.Equipo;

public class CatálogosDAO {

    public synchronized String obtenerYGenerarSiguienteNúmero(String tipoComprobante) {
        String tipo = tipoComprobante.equalsIgnoreCase("Factura") ? "FACTURA" : "BOLETA";
        String prefijo = tipo.equals("FACTURA") ? "F001-" : "B001-";
        int nuevoNúmero = 1;

        Connection conexión = ConexiónBD.obtenerConexión();
        if (conexión == null) return prefijo + "000001";

        try {
            String consultaSelección = "SELECT ultimo_numero FROM secuencias WHERE tipo = ?";
            try (PreparedStatement sentencia = conexión.prepareStatement(consultaSelección)) {
                sentencia.setString(1, tipo);
                try (ResultSet resultados = sentencia.executeQuery()) {
                    if (resultados.next()) {
                        nuevoNúmero = resultados.getInt("ultimo_numero") + 1;
                    }
                }
            }

            String consultaActualización = "UPDATE secuencias SET ultimo_numero = ? WHERE tipo = ?";
            try (PreparedStatement sentencia = conexión.prepareStatement(consultaActualización)) {
                sentencia.setInt(1, nuevoNúmero);
                sentencia.setString(2, tipo);
                sentencia.executeUpdate();
            }
        } catch (Exception excepción) {
            excepción.printStackTrace();
        }
        return prefijo + String.format("%06d", nuevoNúmero);
    }

    public List<Object[]> obtenerCatálogoConstrucción() {
        List<Object[]> lista = new ArrayList<>();
        String consultaSQL = "SELECT nombre, precio_m2 FROM catalogo_construccion";
        try (Connection conexión = ConexiónBD.obtenerConexión();
             PreparedStatement sentencia = conexión.prepareStatement(consultaSQL);
             ResultSet resultados = sentencia.executeQuery()) {
            while (resultados.next()) {
                lista.add(new Object[]{ resultados.getString("nombre"), resultados.getDouble("precio_m2") });
            }
        } catch (Exception excepción) { excepción.printStackTrace(); }
        return lista;
    }

    public double[] obtenerTarifasMantenimiento() {
        double[] tarifas = new double[]{ 50.0, 80.0 };
        String consultaSQL = "SELECT concepto, precio FROM catalogo_mantenimiento";
        try (Connection conexión = ConexiónBD.obtenerConexión();
             PreparedStatement sentencia = conexión.prepareStatement(consultaSQL);
             ResultSet resultados = sentencia.executeQuery()) {
            while (resultados.next()) {
                String concepto = resultados.getString("concepto");
                double precio = resultados.getDouble("precio");
                if (concepto.toLowerCase().contains("hora")) tarifas[0] = precio;
                else if (concepto.toLowerCase().contains("químicos") || concepto.toLowerCase().contains("quimicos")) tarifas[1] = precio;
            }
        } catch (Exception excepción) { excepción.printStackTrace(); }
        return tarifas;
    }

    public List<Equipo> obtenerInventarioDeSuministros() {
        List<Equipo> lista = new ArrayList<>();
        String consultaSQL = "SELECT nombre, precio, stock FROM catalogo_suministros";
        try (Connection conexión = ConexiónBD.obtenerConexión();
             PreparedStatement sentencia = conexión.prepareStatement(consultaSQL);
             ResultSet resultados = sentencia.executeQuery()) {
            while (resultados.next()) {
                lista.add(new Equipo(resultados.getString("nombre"), resultados.getDouble("precio"), resultados.getInt("stock")));
            }
        } catch (Exception excepción) { excepción.printStackTrace(); }
        return lista;
    }

    public void reducirInventarioDeSuministro(String nombreProducto) {
        String consultaSQL = "UPDATE catalogo_suministros SET stock = GREATEST(0, stock - 1) WHERE nombre = ?";
        try (Connection conexión = ConexiónBD.obtenerConexión();
             PreparedStatement sentencia = conexión.prepareStatement(consultaSQL)) {
            sentencia.setString(1, nombreProducto);
            sentencia.executeUpdate();
        } catch (Exception excepción) { excepción.printStackTrace(); }
    }
    public boolean agregarÍtemConstrucción(String nombre, double precio) {
        String consultaSQL = "INSERT INTO catalogo_construccion (nombre, precio_m2) VALUES (?, ?)";
        try (Connection conexión = ConexiónBD.obtenerConexión();
             PreparedStatement sentencia = conexión.prepareStatement(consultaSQL)) {
            sentencia.setString(1, nombre);
            sentencia.setDouble(2, precio);
            return sentencia.executeUpdate() > 0;
        } catch (Exception excepción) {
            excepción.printStackTrace();
            return false;
        }
    }

    public boolean actualizarÍtemConstrucción(String nombreAntiguo, String nuevoNombre, double nuevoPrecio) {
        String consultaSQL = "UPDATE catalogo_construccion SET nombre = ?, precio_m2 = ? WHERE nombre = ?";
        try (Connection conexión = ConexiónBD.obtenerConexión();
             PreparedStatement sentencia = conexión.prepareStatement(consultaSQL)) {
            sentencia.setString(1, nuevoNombre);
            sentencia.setDouble(2, nuevoPrecio);
            sentencia.setString(3, nombreAntiguo);
            return sentencia.executeUpdate() > 0;
        } catch (Exception excepción) {
            excepción.printStackTrace();
            return false;
        }
    }

    public boolean eliminarÍtemConstrucción(String nombre) {
        String consultaSQL = "DELETE FROM catalogo_construccion WHERE nombre = ?";
        try (Connection conexión = ConexiónBD.obtenerConexión();
             PreparedStatement sentencia = conexión.prepareStatement(consultaSQL)) {
            sentencia.setString(1, nombre);
            return sentencia.executeUpdate() > 0;
        } catch (Exception excepción) {
            excepción.printStackTrace();
            return false;
        }
    }

    public boolean actualizarTarifaMantenimiento(String concepto, double nuevoPrecio) {
        String consultaSQL = "UPDATE catalogo_mantenimiento SET precio = ? WHERE concepto LIKE ?";
        try (Connection conexión = ConexiónBD.obtenerConexión();
             PreparedStatement sentencia = conexión.prepareStatement(consultaSQL)) {
            sentencia.setDouble(1, nuevoPrecio);
            sentencia.setString(2, "%" + concepto + "%");
            return sentencia.executeUpdate() > 0;
        } catch (Exception excepción) {
            excepción.printStackTrace();
            return false;
        }
    }

    public boolean agregarÍtemSuministro(String nombre, double precio, int cantidadEnAlmacén) {
        String consultaSQL = "INSERT INTO catalogo_suministros (nombre, precio, stock) VALUES (?, ?, ?)";
        try (Connection conexión = ConexiónBD.obtenerConexión();
             PreparedStatement sentencia = conexión.prepareStatement(consultaSQL)) {
            sentencia.setString(1, nombre);
            sentencia.setDouble(2, precio);
            sentencia.setInt(3, cantidadEnAlmacén);
            return sentencia.executeUpdate() > 0;
        } catch (Exception excepción) {
            excepción.printStackTrace();
            return false;
        }
    }

    public boolean actualizarÍtemSuministro(String nombreAntiguo, String nuevoNombre, double nuevoPrecio, int nuevaCantidad) {
        String consultaSQL = "UPDATE catalogo_suministros SET nombre = ?, precio = ?, stock = ? WHERE nombre = ?";
        try (Connection conexión = ConexiónBD.obtenerConexión();
             PreparedStatement sentencia = conexión.prepareStatement(consultaSQL)) {
            sentencia.setString(1, nuevoNombre);
            sentencia.setDouble(2, nuevoPrecio);
            sentencia.setInt(3, nuevaCantidad);
            sentencia.setString(4, nombreAntiguo);
            return sentencia.executeUpdate() > 0;
        } catch (Exception excepción) {
            excepción.printStackTrace();
            return false;
        }
    }

    public boolean eliminarÍtemSuministro(String nombre) {
        String consultaSQL = "DELETE FROM catalogo_suministros WHERE nombre = ?";
        try (Connection conexión = ConexiónBD.obtenerConexión();
             PreparedStatement sentencia = conexión.prepareStatement(consultaSQL)) {
            sentencia.setString(1, nombre);
            return sentencia.executeUpdate() > 0;
        } catch (Exception excepción) {
            excepción.printStackTrace();
            return false;
        }
    }
}