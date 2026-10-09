package persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import modelo.RegistroDeComprobante;

public class ComprobanteDAO {

    public boolean registrarComprobante(String númeroComprobante, String cliente, String tipoServicio, double montoTotal, String contenidoRecibo) {
        String consultaSQL = "INSERT INTO comprobantes (nro_comprobante, cliente, tipo_servicio, monto_total, contenido_recibo) VALUES (?, ?, ?, ?, ?)";
        Connection conexión = ConexiónBD.obtenerConexión();
        if (conexión == null) return false;

        try (PreparedStatement sentencia = conexión.prepareStatement(consultaSQL)) {
            sentencia.setString(1, númeroComprobante);
            sentencia.setString(2, cliente);
            sentencia.setString(3, tipoServicio);
            sentencia.setDouble(4, montoTotal);
            sentencia.setString(5, contenidoRecibo);
            return sentencia.executeUpdate() > 0;
        } catch (Exception excepción) {
            excepción.printStackTrace();
            return false;
        }
    }

    public List<RegistroDeComprobante> obtenerÚltimasVentas(int límite) {
        List<RegistroDeComprobante> lista = new ArrayList<>();
        String consultaSQL = "SELECT id, nro_comprobante, cliente, tipo_servicio, monto_total, estado, contenido_recibo FROM comprobantes ORDER BY id DESC LIMIT ?";
        Connection conexión = ConexiónBD.obtenerConexión();
        if (conexión == null) return lista;

        try (PreparedStatement sentencia = conexión.prepareStatement(consultaSQL)) {
            sentencia.setInt(1, límite);
            try (ResultSet resultados = sentencia.executeQuery()) {
                while (resultados.next()) {
                    lista.add(new RegistroDeComprobante(
                        resultados.getInt("id"),
                        resultados.getString("nro_comprobante"),
                        resultados.getString("cliente"),
                        resultados.getString("tipo_servicio"),
                        resultados.getDouble("monto_total"),
                        resultados.getString("estado"),
                        resultados.getString("contenido_recibo")
                    ));
                }
            }
        } catch (Exception excepción) {
            excepción.printStackTrace();
        }
        return lista;
    }

    public List<RegistroDeComprobante> buscarComprobantes(String criterio) {
        List<RegistroDeComprobante> lista = new ArrayList<>();
        String consultaSQL = "SELECT id, nro_comprobante, cliente, tipo_servicio, monto_total, estado, contenido_recibo FROM comprobantes WHERE nro_comprobante LIKE ? ORDER BY id DESC";
        Connection conexión = ConexiónBD.obtenerConexión();
        if (conexión == null) return lista;

        try (PreparedStatement sentencia = conexión.prepareStatement(consultaSQL)) {
            String búsqueda = "%" + criterio + "%";
            sentencia.setString(1, búsqueda);
            try (ResultSet resultados = sentencia.executeQuery()) {
                while (resultados.next()) {
                    lista.add(new RegistroDeComprobante(
                        resultados.getInt("id"),
                        resultados.getString("nro_comprobante"),
                        resultados.getString("cliente"),
                        resultados.getString("tipo_servicio"),
                        resultados.getDouble("monto_total"),
                        resultados.getString("estado"),
                        resultados.getString("contenido_recibo")
                    ));
                }
            }
        } catch (Exception excepción) {
            excepción.printStackTrace();
        }
        return lista;
    }

    public boolean modificarComprobante(String númeroComprobante, String nuevoCliente, String nuevoCorreo, String nuevoTeléfono, String nuevoDocumento, String contenidoOriginal) {
        String contenidoActualizado = contenidoOriginal;
        contenidoActualizado = contenidoActualizado.replaceAll("CLIENTE: .*", "CLIENTE: " + nuevoCliente);
        contenidoActualizado = contenidoActualizado.replaceAll("CORREO: .*", "CORREO: " + nuevoCorreo);
        contenidoActualizado = contenidoActualizado.replaceAll("TELÉFONO: .*", "TELÉFONO: " + nuevoTeléfono);
        contenidoActualizado = contenidoActualizado.replaceAll("DNI: .*", "DNI: " + nuevoDocumento);
        contenidoActualizado = contenidoActualizado.replaceAll("RUC: .*", "RUC: " + nuevoDocumento);

        String consultaSQL = "UPDATE comprobantes SET cliente = ?, contenido_recibo = ?, estado = 'MODIFICADO' WHERE nro_comprobante = ?";
        Connection conexión = ConexiónBD.obtenerConexión();
        if (conexión == null) return false;

        try (PreparedStatement sentencia = conexión.prepareStatement(consultaSQL)) {
            sentencia.setString(1, nuevoCliente);
            sentencia.setString(2, contenidoActualizado);
            sentencia.setString(3, númeroComprobante);
            return sentencia.executeUpdate() > 0;
        } catch (Exception excepción) {
            excepción.printStackTrace();
            return false;
        }
    }
    public List<RegistroDeComprobante> generarReporte(String fecha, String tipoServicio) {
        List<RegistroDeComprobante> lista = new ArrayList<>();
        // Filtramos asegurando el formato dd/MM/yyyy
        String consultaSQL = "SELECT id, nro_comprobante, cliente, tipo_servicio, monto_total, estado, contenido_recibo FROM comprobantes WHERE DATE(fecha_emision) = STR_TO_DATE(?, '%d/%m/%Y')";
        
        if (!tipoServicio.equals("Todos")) {
            consultaSQL += " AND tipo_servicio LIKE ?";
        }
        
        Connection conexión = ConexiónBD.obtenerConexión();
        if (conexión == null) return lista;

        try (PreparedStatement sentencia = conexión.prepareStatement(consultaSQL)) {
            sentencia.setString(1, fecha);
            
            if (!tipoServicio.equals("Todos")) {
                String filtroBusqueda = "%";
                if (tipoServicio.equals("Construcción")) filtroBusqueda = "%CONSTRUCC%";
                else if (tipoServicio.equals("Mantenimiento")) filtroBusqueda = "%MANTENIMIENTO%";
                else if (tipoServicio.equals("Suministros")) filtroBusqueda = "%SUMINISTRO%";
                sentencia.setString(2, filtroBusqueda);
            }
            
            try (ResultSet resultados = sentencia.executeQuery()) {
                while (resultados.next()) {
                    lista.add(new RegistroDeComprobante(
                        resultados.getInt("id"),
                        resultados.getString("nro_comprobante"),
                        resultados.getString("cliente"),
                        resultados.getString("tipo_servicio"),
                        resultados.getDouble("monto_total"),
                        resultados.getString("estado"),
                        resultados.getString("contenido_recibo")
                    ));
                }
            }
        } catch (Exception excepción) {
            excepción.printStackTrace();
        }
        return lista;
    }
}