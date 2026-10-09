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
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import lógica.GestorDeSesión;
import lógica.GestorDocumentos;
import modelo.Cliente;
import modelo.ServicioMantenimiento;
import modelo.FábricaDeServicios;
import persistencia.CatálogosDAO;
import persistencia.ComprobanteDAO;

public class VentanaMantenimiento extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private JTextField campoNombreCliente, campoTeléfono, campoCorreo, campoDocumento;
    private JComboBox<String> menúComprobante;
    private JLabel etiquetaDocumento, etiquetaTarifaInfo;
    private JButton botónGenerar, botónRegresar;
    private JSpinner selectorHoras;
    private JCheckBox casillaQuímicos;
    
    private double tarifaPorHora = 50.0, costoKitQuímicos = 80.0;
    private boolean esAdministrador = false;
    private String nombreCajeroActivo = "", comprobanteReal = "", comprobanteCensurado = "", númeroComprobanteActual = "", nombreClienteActual = "";
    private double totalAPagarActual = 0.0;
        
    public VentanaMantenimiento() {
        if (GestorDeSesión.haySesiónActiva()) { nombreCajeroActivo = GestorDeSesión.obtenerNombreCompletoActivo(); esAdministrador = GestorDeSesión.esAdministrador(); }
        setTitle("BlueSync - Cotización de Mantenimiento"); setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 560, 490); setResizable(false); setLocationRelativeTo(null);
        
        JPanel panel = new JPanel(null); panel.setBackground(Color.WHITE); setContentPane(panel);
        JLabel titulo = new JLabel("Servicio de Mantenimiento de Piscinas", SwingConstants.CENTER); titulo.setFont(new Font("Segoe UI", Font.BOLD, 20)); titulo.setForeground(new Color(26, 86, 219)); titulo.setBounds(10, 15, 520, 30); panel.add(titulo);
        JSeparator sep = new JSeparator(); sep.setBounds(30, 55, 480, 2); panel.add(sep);
        
        JPanel pCli = new JPanel(null); pCli.setBackground(Color.WHITE); pCli.setBorder(BorderFactory.createTitledBorder("Datos del Cliente")); pCli.setBounds(30, 70, 480, 150); panel.add(pCli);
        JLabel etiquetaComprobante = new JLabel("Recibo:"); etiquetaComprobante.setBounds(20, 25, 60, 20); pCli.add(etiquetaComprobante);
        menúComprobante = new JComboBox<>(new String[]{"Boleta", "Factura"}); menúComprobante.setBounds(80, 25, 100, 28); pCli.add(menúComprobante);
        etiquetaDocumento = new JLabel("DNI:"); etiquetaDocumento.setBounds(195, 25, 40, 20); pCli.add(etiquetaDocumento);
        campoDocumento = new JTextField(); campoDocumento.setBounds(235, 25, 225, 28); pCli.add(campoDocumento);
        
        JLabel lblNom = new JLabel("Nombre:"); lblNom.setBounds(20, 65, 60, 20); pCli.add(lblNom);
        campoNombreCliente = new JTextField(); campoNombreCliente.setBounds(80, 65, 380, 28); pCli.add(campoNombreCliente);
        JLabel lblTel = new JLabel("Teléfono:"); lblTel.setBounds(20, 105, 60, 20); pCli.add(lblTel);
        campoTeléfono = new JTextField(); campoTeléfono.setBounds(80, 105, 120, 28); pCli.add(campoTeléfono);
        JLabel lblCor = new JLabel("Correo:"); lblCor.setBounds(220, 105, 50, 20); pCli.add(lblCor);
        campoCorreo = new JTextField(); campoCorreo.setBounds(270, 105, 190, 28); pCli.add(campoCorreo);
        
        menúComprobante.addActionListener(e -> { etiquetaDocumento.setText(menúComprobante.getSelectedItem().equals("Factura") ? "RUC:" : "DNI:"); campoDocumento.setText(""); campoNombreCliente.setText(""); });
        
        JPanel pServ = new JPanel(null); pServ.setBackground(Color.WHITE); pServ.setBorder(BorderFactory.createTitledBorder("Requerimientos del Servicio")); pServ.setBounds(30, 230, 480, 110); panel.add(pServ);
        JLabel lblHoras = new JLabel("Horas de trabajo:"); lblHoras.setBounds(20, 30, 110, 20); pServ.add(lblHoras);
        selectorHoras = new JSpinner(new SpinnerNumberModel(1, 1, 24, 1)); selectorHoras.setBounds(130, 27, 60, 28); pServ.add(selectorHoras);
        casillaQuímicos = new JCheckBox("Incluir Kit de Químicos"); casillaQuímicos.setBackground(Color.WHITE); casillaQuímicos.setBounds(210, 27, 250, 28); pServ.add(casillaQuímicos);
        etiquetaTarifaInfo = new JLabel("Cargando tarifas..."); etiquetaTarifaInfo.setBounds(20, 70, 440, 20); pServ.add(etiquetaTarifaInfo);
        
        cargarTarifasDesdeSQL(); 
        botónGenerar = new JButton("Generar Comprobante"); botónGenerar.setBounds(30, 370, 230, 40); botónGenerar.setBackground(new Color(26, 86, 219)); botónGenerar.setForeground(Color.WHITE); botónGenerar.addActionListener(this); panel.add(botónGenerar);
        botónRegresar = new JButton("Regresar al Panel"); botónRegresar.setBounds(280, 370, 230, 40); botónRegresar.setBackground(new Color(107, 114, 128)); botónRegresar.setForeground(Color.WHITE); botónRegresar.addActionListener(this); panel.add(botónRegresar);
    }

    private void cargarTarifasDesdeSQL() {
        double[] tarifas = new CatálogosDAO().obtenerTarifasMantenimiento();
        tarifaPorHora = tarifas[0]; costoKitQuímicos = tarifas[1];
        etiquetaTarifaInfo.setText("Tarifa actual: S/ " + String.format("%.2f", tarifaPorHora) + " x hora");
        casillaQuímicos.setText("Incluir Kit de Químicos (+ S/ " + String.format("%.2f", costoKitQuímicos) + ")");
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
            String nom = campoNombreCliente.getText().trim().toUpperCase(), tel = campoTeléfono.getText().trim(), cor = campoCorreo.getText().trim().toUpperCase(), doc = campoDocumento.getText().trim(), tipo = menúComprobante.getSelectedItem().toString();
            if (nom.isEmpty() || tel.isEmpty() || cor.isEmpty() || doc.isEmpty()) { JOptionPane.showMessageDialog(this, "Complete todos los datos."); return; }
            if (!nom.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) { JOptionPane.showMessageDialog(this, "Nombre inválido."); return; }
            if (!tel.matches("^\\d{9}$")) { JOptionPane.showMessageDialog(this, "Teléfono inválido (9 dígitos)."); return; }
            if (!cor.matches("^[A-Z0-9+_.-]+@[A-Z0-9.-]+$")) { JOptionPane.showMessageDialog(this, "Correo inválido."); return; }
            if (tipo.equals("Boleta") && !doc.matches("^\\d{8}$")) { JOptionPane.showMessageDialog(this, "DNI debe tener 8 dígitos."); return; }
            if (tipo.equals("Factura") && !doc.matches("^(10|15|17|20)\\d{9}$")) { JOptionPane.showMessageDialog(this, "RUC inválido."); return; }
            
            int horas = (Integer) selectorHoras.getValue(); boolean químicos = casillaQuímicos.isSelected();
            ServicioMantenimiento serv = (ServicioMantenimiento) FábricaDeServicios.crearServicio("MANTENIMIENTO");
            serv.establecerHoras(horas); serv.establecerTarifaPorHora(tarifaPorHora); serv.establecerRequiereQuímicos(químicos); serv.establecerCostoQuímicos(costoKitQuímicos);
            
            double subtotal = serv.calcularCosto(), igv = serv.calcularIGV(subtotal), total = serv.calcularTotalConIGV(subtotal);
            String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss"));
            Cliente cliente = new Cliente(nom, tel, cor, tipo, doc);
            
            if (this.númeroComprobanteActual.isEmpty() || !this.númeroComprobanteActual.contains(tipo.equals("Factura") ? "F" : "B")) {
                this.númeroComprobanteActual = new CatálogosDAO().obtenerYGenerarSiguienteNúmero(tipo);
            }
            this.nombreClienteActual = cliente.obtenerNombreCompleto(); this.totalAPagarActual = total;

            String cabecera = "=================================================\n       DH PROYECTOS Y CONTRATISTAS S.A.C.\n    RUC: 20517614051 | Av. Las Lomas 123, Lima\n=================================================\n";
            String formato = cabecera 
                + "TIPO: " + tipo.toUpperCase() + " ELECTRÓNICA\n"
                + "NRO. COMPROBANTE: " + númeroComprobanteActual + "\n"
                + "FECHA: " + fecha + "\n"
                + "ATENDIDO POR: " + nombreCajeroActivo.toUpperCase() + "\n"
                + "-------------------------------------------------\n"
                + "CLIENTE: %s\nCORREO: %s\nTELÉFONO: %s\n" + (tipo.equals("Factura")?"RUC: ":"DNI: ") + "%s\n"
                + "-------------------------------------------------\n"
                + "DETALLE: Servicio de Mantenimiento de piscinas\n"
                + "Horas: " + horas + " hr(s)\n"
                + "Tarifa/Hora: S/ " + tarifaPorHora + "\n"
                + "Kit de Químicos: " + (químicos ? "SÍ (S/ " + costoKitQuímicos + ")" : "NO") + "\n"
                + "-------------------------------------------------\n"
                + "SUBTOTAL: S/ " + String.format(Locale.US, "%.2f", subtotal) + "\n"
                + "IGV: S/ " + String.format(Locale.US, "%.2f", igv) + "\n"
                + "TOTAL A PAGAR: S/ " + String.format(Locale.US, "%.2f", total) + "\n"
                + "=================================================";

            comprobanteReal = String.format(formato, nom, cliente.obtenerCorreo(), cliente.obtenerTeléfono(), cliente.obtenerNúmeroDocumento());
            comprobanteCensurado = String.format(formato, cliente.obtenerNombreProtegido(), enmascararCorreo(cliente.obtenerCorreo()), enmascararTel(cliente.obtenerTeléfono()), enmascararDoc(cliente.obtenerNúmeroDocumento()));

            lanzarVentanaDePrevisualización();
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error de sistema."); }
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
            campoNombreCliente.setText(""); campoTeléfono.setText(""); campoCorreo.setText(""); campoDocumento.setText(""); númeroComprobanteActual = ""; selectorHoras.setValue(1); casillaQuímicos.setSelected(false);
            botónGenerar.setText("Generar Comprobante"); botónRegresar.setEnabled(true);
        });
        prev.add(btnCanc);
        
        JButton btnConf = new JButton("Confirmar"); btnConf.setBackground(new Color(16, 185, 129)); btnConf.setForeground(Color.WHITE); btnConf.setBounds(240, 410, 150, 35);
        btnConf.addActionListener(e -> {
            prev.dispose(); setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            if (new ComprobanteDAO().registrarComprobante(númeroComprobanteActual, nombreClienteActual, "SERV. MANTENIM", totalAPagarActual, comprobanteReal)) {
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
                
                campoNombreCliente.setText(""); campoTeléfono.setText(""); campoCorreo.setText(""); campoDocumento.setText(""); númeroComprobanteActual = ""; selectorHoras.setValue(1); casillaQuímicos.setSelected(false);
                botónGenerar.setText("Generar Comprobante"); botónRegresar.setEnabled(true); 
            } else { 
                setCursor(Cursor.getDefaultCursor()); JOptionPane.showMessageDialog(this, "Error al registrar en BD."); 
            }
        }); prev.add(btnConf); prev.setVisible(true);
    }
}