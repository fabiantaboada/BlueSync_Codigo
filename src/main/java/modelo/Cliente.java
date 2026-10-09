package modelo;

public class Cliente {
    private String nombreCompleto;
    private String teléfono;
    private String correo;
    private String tipoComprobante; 
    private String númeroDocumento; 

    public Cliente(String nombreCompleto, String teléfono, String correo, String tipoComprobante, String númeroDocumento) {
        this.nombreCompleto = nombreCompleto;
        this.teléfono = teléfono;
        this.correo = correo;
        this.tipoComprobante = tipoComprobante;
        this.númeroDocumento = númeroDocumento;
    }

    public String obtenerNombreCompleto() { return nombreCompleto; }
    public void establecerNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String obtenerTeléfono() { return teléfono; }
    public void establecerTeléfono(String teléfono) { this.teléfono = teléfono; }

    public String obtenerCorreo() { return correo; }
    public void establecerCorreo(String correo) { this.correo = correo; }

    public String obtenerTipoComprobante() { return tipoComprobante; }
    public void establecerTipoComprobante(String tipoComprobante) { this.tipoComprobante = tipoComprobante; }

    public String obtenerNúmeroDocumento() { return númeroDocumento; }
    public void establecerNúmeroDocumento(String númeroDocumento) { this.númeroDocumento = númeroDocumento; }

    public String obtenerNombreProtegido() {
        if (nombreCompleto == null || nombreCompleto.trim().length() < 3) return "***";
        String[] partes = nombreCompleto.trim().split(" ");
        if (partes.length >= 2) {
            return partes[0].charAt(0) + ". " + partes[1].charAt(0) + "***";
        }
        return nombreCompleto.substring(0, 2) + "***";
    }
}