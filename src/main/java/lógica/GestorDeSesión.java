package lógica;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class GestorDeSesión {
    
    private static String usuarioActual = null;
    private static String rolActual = null;
    private static String nombreCompletoActual = null;

    public static void iniciarSesión(String usuario, String rol, String nombreCompleto) {
        usuarioActual = usuario;
        rolActual = rol;
        nombreCompletoActual = nombreCompleto;
        guardarSesiónLocal(); // Guarda el archivo al iniciar
    }

    public static void cerrarSesión() {
        usuarioActual = null;
        rolActual = null;
        nombreCompletoActual = null;
        File archivoLocal = new File("sesion_guardada.txt");
        if (archivoLocal.exists()) {
            archivoLocal.delete(); // Borra el archivo al salir explícitamente
        }
    }

    private static void guardarSesiónLocal() {
        try (FileWriter escritor = new FileWriter("sesion_guardada.txt")) {
            escritor.write(usuarioActual + " : " + rolActual + " : " + nombreCompletoActual);
        } catch (Exception excepción) {}
    }

    public static void cargarSesiónLocal() {
        File archivoLocal = new File("sesion_guardada.txt");
        if (archivoLocal.exists()) {
            try (BufferedReader lector = new BufferedReader(new FileReader(archivoLocal))) {
                String líneaLeída = lector.readLine();
                if (líneaLeída != null && líneaLeída.contains(" : ")) {
                    String[] partes = líneaLeída.split(" : ");
                    if (partes.length >= 3) {
                        usuarioActual = partes[0].trim();
                        rolActual = partes[1].trim();
                        nombreCompletoActual = partes[2].trim();
                    }
                }
            } catch (Exception excepción) {}
        }
    }

    public static boolean haySesiónActiva() {
        return usuarioActual != null;
    }

    public static boolean esAdministrador() {
        return "ADMIN".equalsIgnoreCase(rolActual) || "Administrador".equalsIgnoreCase(rolActual);
    }

    public static String obtenerUsuarioActivo() { return usuarioActual; }
    public static String obtenerRolActivo() { return rolActual; }
    public static String obtenerNombreCompletoActivo() { return nombreCompletoActual; }
}