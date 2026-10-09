package lógica;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import modelo.RegistroDeComprobante;

public class GestorDocumentos {

    public static String generarComprobanteTXT(String numeroComprobante, String contenido) {
        String rutaArchivo = "Comprobante_" + numeroComprobante + ".txt";
        try (FileWriter escritor = new FileWriter(rutaArchivo)) {
            escritor.write(contenido);
            return rutaArchivo;
        } catch (Exception e) {
            System.err.println("Error al generar TXT: " + e.getMessage());
            return null;
        }
    }
    
    public static String generarReporteVentasTXT(List<RegistroDeComprobante> listaVentas) {
        String rutaArchivo = "Reporte_Ventas_Temp.txt";
        try (FileWriter escritor = new FileWriter(rutaArchivo)) {
            escritor.write("=================================================\n");
            escritor.write("          REGISTRO DE VENTAS - DH PROYECTOS\n");
            escritor.write("=================================================\n\n");
            
            double sumaTotal = 0;
            for (RegistroDeComprobante r : listaVentas) {
                escritor.write(r.obtenerNúmeroComprobante() + " | " + r.obtenerCliente() + " | " + r.obtenerTipoServicio() + " | S/ " + String.format("%.2f", r.obtenerMontoTotal()) + "\n");
                if (!r.obtenerEstado().equals("ANULADO")) {
                    sumaTotal += r.obtenerMontoTotal();
                }
            }
            
            escritor.write("\n=================================================\n");
            escritor.write("INGRESOS TOTALES RECAUDADOS: S/ " + String.format("%.2f", sumaTotal) + "\n");
            return rutaArchivo;
        } catch (Exception e) {
            return null;
        }
    }
}