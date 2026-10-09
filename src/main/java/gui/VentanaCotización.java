package gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import lógica.GestorDeSesión;
import lógica.GestorDocumentos;
import modelo.Cliente;
import modelo.ServicioConstrucción;
import modelo.FábricaDeServicios;
import persistencia.CatálogosDAO;
import persistencia.ComprobanteDAO;

public class VentanaCotización extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private JPanel panelDeContención;
    private JTextField campoNombreCliente, campoTeléfono, campoCorreo, campoDocumento, campoLargo, campoAncho;
    private JComboBox<String> menúComprobante, menúAcabado;
    private JLabel etiquetaDocumento;
    private JButton botónGenerar, botónRegresar;
    
    private ArrayList<String> nombresDeAcabados = new ArrayList<>();
    private ArrayList<Double> preciosDeAcabados = new ArrayList<>();
    
    private boolean esAdministrador = false;
    private String nombreCajeroActivo = "", comprobanteReal = "", comprobanteCensurado = "", númeroComprobanteActual = "", nombreClienteActual = "";
    private double totalAPagarActual = 0.0;

    public VentanaCotización() {
        if (GestorDeSesión.haySesiónActiva()) { nombreCajeroActivo = GestorDeSesión.obtenerNombreCompletoActivo(); esAdministrador = GestorDeSesión.esAdministrador(); }
        setTitle("BlueSync - Cotización de Construcción");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); 
        setBounds(100, 100, 560, 550); setResizable(false); setLocationRelativeTo(null);
        
        panelDeContención = new JPanel(null); panelDeContención.setBackground(Color.WHITE); setContentPane(panelDeContención);
        JLabel etiquetaTítulo = new JLabel("Servicio de Construcción de Piscinas", SwingConstants.CENTER);
        etiquetaTítulo.setFont(new Font("Segoe UI", Font.BOLD, 20)); etiquetaTítulo.setForeground(new Color(26, 86, 219)); etiquetaTítulo.setBounds(10, 15, 520, 30); panelDeContención.add(etiquetaTítulo);
        JSeparator separador = new JSeparator(); separador.setBounds(30, 55, 480, 2); panelDeContención.add(separador);
        
        JPanel panelCliente = new JPanel(null); panelCliente.setBackground(Color.WHITE); panelCliente.setBorder(BorderFactory.createTitledBorder("Datos del Cliente")); panelCliente.setBounds(30, 70, 480, 150); panelDeContención.add(panelCliente);
        
        JLabel etiquetaComprobante = new JLabel("Recibo:"); etiquetaComprobante.setBounds(20, 25, 60, 20); panelCliente.add(etiquetaComprobante);
        menúComprobante = new JComboBox<>(new String[]{"Boleta", "Factura"}); menúComprobante.setBounds(80, 25, 100, 28); panelCliente.add(menúComprobante);
        
        etiquetaDocumento = new JLabel("DNI:"); etiquetaDocumento.setBounds(195, 25, 40, 20); panelCliente.add(etiquetaDocumento);
        campoDocumento = new JTextField(); campoDocumento.setBounds(235, 25, 225, 28); panelCliente.add(campoDocumento);
        
        JLabel etiquetaNombre = new JLabel("Nombre:"); etiquetaNombre.setBounds(20, 65, 60, 20); panelCliente.add(etiquetaNombre);
        campoNombreCliente = new JTextField(); campoNombreCliente.setBounds(80, 65, 380, 28); panelCliente.add(campoNombreCliente);
        
        JLabel etiquetaTeléfono = new JLabel("Teléfono:"); etiquetaTeléfono.setBounds(20, 105, 60, 20); panelCliente.add(etiquetaTeléfono);
        campoTeléfono = new JTextField(); campoTeléfono.setBounds(80, 105, 120, 28); panelCliente.add(campoTeléfono);
        
        JLabel etiquetaCorreo = new JLabel("Correo:"); etiquetaCorreo.setBounds(220, 105, 50, 20); panelCliente.add(etiquetaCorreo);
        campoCorreo = new JTextField(); campoCorreo.setBounds(270, 105, 190, 28); panelCliente.add(campoCorreo);
        
        menúComprobante.addActionListener(e -> { etiquetaDocumento.setText(menúComprobante.getSelectedItem().equals("Factura") ? "RUC:" : "DNI:"); campoDocumento.setText(""); campoNombreCliente.setText(""); });
        
        JPanel panelRequerimientos = new JPanel(null); panelRequerimientos.setBackground(Color.WHITE); panelRequerimientos.setBorder(BorderFactory.createTitledBorder("Requerimientos de la Obra")); panelRequerimientos.setBounds(30, 230, 480, 120); panelDeContención.add(panelRequerimientos);
        JLabel etiquetaLargo = new JLabel("Largo (m):"); etiquetaLargo.setBounds(20, 35, 70, 20); panelRequerimientos.add(etiquetaLargo);
        campoLargo = new JTextField(); campoLargo.setBounds(90, 35, 100, 28); panelRequerimientos.add(campoLargo);
        JLabel etiquetaAncho = new JLabel("Ancho (m):"); etiquetaAncho.setBounds(230, 35, 70, 20); panelRequerimientos.add(etiquetaAncho);
        campoAncho = new JTextField(); campoAncho.setBounds(310, 35, 100, 28); panelRequerimientos.add(campoAncho);
        JLabel etiquetaAcabado = new JLabel("Acabado:"); etiquetaAcabado.setBounds(20, 75, 70, 20); panelRequerimientos.add(etiquetaAcabado);
        menúAcabado = new JComboBox<>(); menúAcabado.setBounds(90, 75, 360, 28); panelRequerimientos.add(menúAcabado);
        
        cargarCatálogoDesdeSQL(); 
        
        botónGenerar = new JButton("Generar Comprobante"); botónGenerar.setBackground(new Color(26, 86, 219)); botónGenerar.setForeground(Color.WHITE); botónGenerar.setBounds(30, 380, 230, 40); botónGenerar.addActionListener(this); panelDeContención.add(botónGenerar);
        botónRegresar = new JButton("Regresar al Panel"); botónRegresar.setBackground(new Color(107, 114, 128)); botónRegresar.setForeground(Color.WHITE); botónRegresar.setBounds(280, 380, 230, 40); botónRegresar.addActionListener(this); panelDeContención.add(botónRegresar);
    }

    private void cargarCatálogoDesdeSQL() {
        List<Object[]> items = new CatálogosDAO().obtenerCatálogoConstrucción();
        nombresDeAcabados.clear(); preciosDeAcabados.clear(); menúAcabado.removeAllItems();
        for (Object[] item : items) { nombresDeAcabados.add((String)item[0]); preciosDeAcabados.add((Double)item[1]); menúAcabado.addItem(item[0] + " (S/ " + item[1] + " x m²)"); }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botónRegresar) { new MenúPrincipal().setVisible(true); this.dispose(); } 
        else if (e.getSource() == botónGenerar) prepararComprobanteYVistaPrevia();
    }
    
    private String enmascararCorreo(String c) { if (!c.contains("@")) return "***"; int a = c.indexOf("@"); return a <= 2 ? "**" + c.substring(a) : c.substring(0, 2) + "***" + c.substring(a); }
    private String enmascararTel(String t) { return t.length() < 4 ? "****" : t.substring(0, 2) + "*****" + t.substring(t.length()-2); }
    private String enmascararDoc(String d) { return d.length() < 2 ? "*" : d.charAt(0) + "****" + d.charAt(d.length()-1); }

    protected void prepararComprobanteYVistaPrevia() {
        try {
            String nom = campoNombreCliente.getText().trim().toUpperCase(), tel = campoTeléfono.getText().trim(), cor = campoCorreo.getText().trim().toUpperCase(), doc = campoDocumento.getText().trim(), tipoComprobante = menúComprobante.getSelectedItem().toString();
            if (nom.isEmpty() || tel.isEmpty() || cor.isEmpty() || doc.isEmpty()) { JOptionPane.showMessageDialog(this, "Complete todos los datos."); return; }
            if (!nom.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) { JOptionPane.showMessageDialog(this, "Nombre inválido."); return; }
            if (!tel.matches("^\\d{9}$")) { JOptionPane.showMessageDialog(this, "Teléfono inválido (9 dígitos)."); return; }
            if (!cor.matches("^[A-Z0-9+_.-]+@[A-Z0-9.-]+$")) { JOptionPane.showMessageDialog(this, "Correo inválido."); return; }
            if (tipoComprobante.equals("Boleta") && !doc.matches("^\\d{8}$")) { JOptionPane.showMessageDialog(this, "DNI debe tener 8 dígitos."); return; }
            if (tipoComprobante.equals("Factura") && !doc.matches("^(10|15|17|20)\\d{9}$")) { JOptionPane.showMessageDialog(this, "RUC inválido."); return; }
            
            String textoLargo = campoLargo.getText().trim(), textoAncho = campoAncho.getText().trim();
            if (textoLargo.isEmpty() || textoAncho.isEmpty()) { JOptionPane.showMessageDialog(this, "Falta ingresar el largo o ancho."); return; }
            
            double largoMetros, anchoMetros;
            try { largoMetros = Double.parseDouble(textoLargo); anchoMetros = Double.parseDouble(textoAncho); if(largoMetros<=0 || anchoMetros<=0) throw new Exception(); } 
            catch(Exception e) { JOptionPane.showMessageDialog(this, "Largo y Ancho deben ser numéricos."); return; }
            
            String fechaYHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss"));
            Cliente cliente = new Cliente(nom, tel, cor, tipoComprobante, doc);
            
            ServicioConstrucción serv = (ServicioConstrucción) FábricaDeServicios.crearServicio("CONSTRUCCIÓN");
            serv.establecerLargo(largoMetros); serv.establecerAncho(anchoMetros);
            int idx = menúAcabado.getSelectedIndex();
            serv.establecerCostoBasePorMetroCuadrado(preciosDeAcabados.get(idx)); 
            
            double area = serv.calcularÁrea();
            double subtotal = serv.calcularCosto(), igv = serv.calcularIGV(subtotal), total = serv.calcularTotalConIGV(subtotal);
            
            if (this.númeroComprobanteActual.isEmpty() || !this.númeroComprobanteActual.contains(tipoComprobante.equals("Factura") ? "F" : "B")) {
                this.númeroComprobanteActual = new CatálogosDAO().obtenerYGenerarSiguienteNúmero(tipoComprobante);
            }
            
            this.nombreClienteActual = cliente.obtenerNombreCompleto(); this.totalAPagarActual = total;
            
            String cabecera = "=================================================\n       DH PROYECTOS Y CONTRATISTAS S.A.C.\n    RUC: 20517614051 | Av. Las Lomas 123, Lima\n=================================================\n";
            String formato = cabecera 
                + "TIPO: " + tipoComprobante.toUpperCase() + " ELECTRÓNICA\n"
                + "NRO. COMPROBANTE: " + númeroComprobanteActual + "\n"
                + "FECHA: " + fechaYHora + "\n"
                + "ATENDIDO POR: " + nombreCajeroActivo.toUpperCase() + "\n"
                + "-------------------------------------------------\n"
                + "CLIENTE: %s\nCORREO: %s\nTELÉFONO: %s\n" + (tipoComprobante.equals("Factura")?"RUC: ":"DNI: ") + "%s\n"
                + "-------------------------------------------------\n"
                + "DETALLE: Servicio de Construcción de piscinas\n"
                + "Dimensiones: " + largoMetros + "m x " + anchoMetros + "m\n"
                + "Área: " + area + " m²\n"
                + "Acabado: " + nombresDeAcabados.get(idx) + "\n"
                + "-------------------------------------------------\n"
                + "SUBTOTAL: S/ " + String.format(Locale.US, "%.2f", subtotal) + "\n"
                + "IGV: S/ " + String.format(Locale.US, "%.2f", igv) + "\n"
                + "TOTAL A PAGAR: S/ " + String.format(Locale.US, "%.2f", total) + "\n"
                + "=================================================";

            comprobanteReal = String.format(formato, nom, cliente.obtenerCorreo(), cliente.obtenerTeléfono(), cliente.obtenerNúmeroDocumento());
            comprobanteCensurado = String.format(formato, cliente.obtenerNombreProtegido(), enmascararCorreo(cliente.obtenerCorreo()), enmascararTel(cliente.obtenerTeléfono()), enmascararDoc(cliente.obtenerNúmeroDocumento()));

            lanzarVentanaDePrevisualización();
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error de sistema.", "Error", JOptionPane.ERROR_MESSAGE); }
    }
    
    private void lanzarVentanaDePrevisualización() {
        JDialog prev = new JDialog(this, "Vista Previa del Comprobante", true);
        prev.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); 
        prev.getContentPane().setBackground(Color.WHITE); prev.setBounds(100, 100, 440, 520); prev.setLocationRelativeTo(this); prev.setLayout(null);
        
        JTextArea area = new JTextArea(esAdministrador ? comprobanteReal : comprobanteCensurado);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12)); area.setEditable(false); prev.add(new JScrollPane(area)).setBounds(15, 15, 394, 380);
        
        JButton btnMod = new JButton("Modificar"); btnMod.setBounds(20, 410, 100, 35); 
        btnMod.addActionListener(e -> { prev.dispose(); botónGenerar.setText("Actualizar Comprobante"); botónRegresar.setEnabled(false); }); 
        prev.add(btnMod);
        
        JButton btnCanc = new JButton("Cancelar"); btnCanc.setBackground(new Color(220, 38, 38)); btnCanc.setForeground(Color.WHITE); btnCanc.setBounds(130, 410, 100, 35);
        btnCanc.addActionListener(e -> { 
            prev.dispose(); 
            campoNombreCliente.setText(""); campoTeléfono.setText(""); campoCorreo.setText(""); campoDocumento.setText(""); campoLargo.setText(""); campoAncho.setText(""); númeroComprobanteActual = "";
            botónGenerar.setText("Generar Comprobante"); botónRegresar.setEnabled(true);
        });
        prev.add(btnCanc);
        
        JButton btnConf = new JButton("Confirmar"); btnConf.setBackground(new Color(16, 185, 129)); btnConf.setForeground(Color.WHITE); btnConf.setBounds(240, 410, 150, 35);
        btnConf.addActionListener(e -> {
            prev.dispose(); setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            if (new ComprobanteDAO().registrarComprobante(númeroComprobanteActual, nombreClienteActual, "SERV. CONSTRUCC", totalAPagarActual, comprobanteReal)) {
                setCursor(Cursor.getDefaultCursor());
                
                int descargar = JOptionPane.showConfirmDialog(this, "Operación Exitosa. La venta ha sido registrada en la Base de Datos.\n¿Desea descargar el comprobante en formato .txt?", "Venta Exitosa", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (descargar == JOptionPane.YES_OPTION) {
                    JFileChooser selector = new JFileChooser();
                    selector.setFileFilter(new FileNameExtensionFilter("Archivo de Texto (*.txt)", "txt"));
                    selector.setSelectedFile(new File("Comprobante_" + númeroComprobanteActual + ".txt"));
                    
                    if (selector.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                        File destino = selector.getSelectedFile();
                        if (!destino.getName().toLowerCase().endsWith(".txt")) {
                            destino = new File(destino.getAbsolutePath() + ".txt");
                        }
                        String temp = GestorDocumentos.generarComprobanteTXT(númeroComprobanteActual, comprobanteReal);
                        if (temp != null) {
                            try { 
                                Files.copy(new File(temp).toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING); 
                                new File(temp).delete(); 
                                JOptionPane.showMessageDialog(this, "Comprobante exportado correctamente."); 
                            } catch (Exception ex) { 
                                JOptionPane.showMessageDialog(this, "Error al exportar el archivo."); 
                            }
                        }
                    }
                }
                
                campoNombreCliente.setText(""); campoTeléfono.setText(""); campoCorreo.setText(""); campoDocumento.setText(""); campoLargo.setText(""); campoAncho.setText(""); númeroComprobanteActual = "";
                botónGenerar.setText("Generar Comprobante"); botónRegresar.setEnabled(true); 
            } else { 
                setCursor(Cursor.getDefaultCursor()); JOptionPane.showMessageDialog(this, "Error al registrar en BD."); 
            }
        }); prev.add(btnConf); prev.setVisible(true);
    }
}