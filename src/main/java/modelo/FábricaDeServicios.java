package modelo;

public class FábricaDeServicios {
    
    public static Servicio crearServicio(String tipoServicio) {
        if (tipoServicio == null) {
            return null;
        }
        
        switch (tipoServicio.toUpperCase()) {
            case "CONSTRUCCIÓN":
                return new ServicioConstrucción();
            case "MANTENIMIENTO":
                return new ServicioMantenimiento();
            case "SUMINISTRO":
                return new ServicioSuministro();
            default:
                throw new IllegalArgumentException("Tipo de servicio desconocido: " + tipoServicio);
        }
    }
}