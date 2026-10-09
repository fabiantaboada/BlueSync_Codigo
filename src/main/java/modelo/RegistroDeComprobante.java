package modelo;

public class RegistroDeComprobante {
    private int identificador;
    private String númeroComprobante;
    private String cliente;
    private String tipoServicio;
    private double montoTotal;
    private String estado;
    private String contenidoRecibo;

    public RegistroDeComprobante(int identificador, String númeroComprobante, String cliente, String tipoServicio, double montoTotal, String estado, String contenidoRecibo) {
        this.identificador = identificador;
        this.númeroComprobante = númeroComprobante;
        this.cliente = cliente;
        this.tipoServicio = tipoServicio;
        this.montoTotal = montoTotal;
        this.estado = estado;
        this.contenidoRecibo = contenidoRecibo;
    }

    public int obtenerIdentificador() { return identificador; }
    public String obtenerNúmeroComprobante() { return númeroComprobante; }
    public String obtenerCliente() { return cliente; }
    public String obtenerTipoServicio() { return tipoServicio; }
    public double obtenerMontoTotal() { return montoTotal; }
    public String obtenerEstado() { return estado; }
    public String obtenerContenidoRecibo() { return contenidoRecibo; }
}