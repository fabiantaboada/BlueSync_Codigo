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
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
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
import modelo.Equipo;
import persistencia.CatálogosDAO;
import persistencia.ComprobanteDAO;

public class VentanaSuministro extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private JPanel panelDeContención;
    private JTextField campoNombreCliente, campoTeléfono, campoCorreo, campoDocumento;
    private JComboBox<String> menúComprobante;
    private JLabel etiquetaDocumento;
    private JButton botónGenerar, botónRegresar;
    private JList<Equipo> listaCatálogoVisual;
    private DefaultListModel<Equipo> modeloDeDatosLista;
    
    private boolean esAdministrador = false;
    private String nombreCajeroActivo = "", comprobanteReal = "", comprobanteCensurado = "", númeroComprobanteActual = "", nombreClienteActual = "";
    private double totalAPagarActual = 0.0;

    public VentanaSuministro() {
        if (GestorDeSesión.haySesiónActiva()) { nombreCajeroActivo = GestorDeSesión.obtenerNombreCompletoActivo(); esAdministrador = GestorDeSesión.esAdministrador(); }
        setTitle("BlueSync - Venta de Suministros"); setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 560, 590); setResizable(false); setLocationRelativeTo(null);
        
        panelDeContención = new JPanel(null); panelDeContención.setBackground(Color.WHITE); setContentPane(panelDeContención);
        JLabel etiquetaTítulo = new JLabel("Suministro de Materiales y Equipos", SwingConstants.CENTER);
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
        
        JPanel panelEquipos = new JPanel(null); panelEquipos.setBackground(Color.WHITE); panelEquipos.setBorder(BorderFactory.createTitledBorder("Seleccione los equipos")); panelEquipos.setBounds(30, 230, 480, 220); panelDeContención.add(panelEquipos);
        
        modeloDeDatosLista = new DefaultListModel<>(); listaCatálogoVisual = new JList<>(modeloDeDatosLista);
        listaCatálogoVisual.setFont(new Font("Segoe UI", Font.PLAIN, 13)); listaCatálogoVisual.setFixedCellHeight(25);
        listaCatálogoVisual.setSelectionModel(new DefaultListSelectionModel() {
            private static final long serialVersionUID = 1L;
            @Override
            public void setSelectionInterval(int índiceInicial, int índiceFinal) {
                Equipo equipoDeseado = modeloDeDatosLista.getElementAt(índiceInicial);
                if (equipoDeseado.obtenerCantidadEnAlmacén() <= 0) { JOptionPane.showMessageDialog(null, "El producto está AGOTADO."); return; }
                if (super.isSelectedIndex(índiceInicial)) super.removeSelectionInterval(índiceInicial, índiceFinal); else super.addSelectionInterval(índiceInicial, índiceFinal);
            }
        });
        panelEquipos.add(new JScrollPane(listaCatálogoVisual)).setBounds(15, 25, 450, 175);
        
        cargarInventarioDesdeSQL(); 
        botónGenerar = new JButton("Generar Comprobante"); botónGenerar.setBackground(new Color(26, 86, 219)); botónGenerar.setForeground(Color.WHITE); botónGenerar.setBounds(30, 465, 230, 40); botónGenerar.addActionListener(this); panelDeContención.add(botónGenerar);
        botónRegresar = new JButton("Regresar al Panel"); botónRegresar.setBackground(new Color(107, 114, 128)); botónRegresar.setForeground(Color.WHITE); botónRegresar.setBounds(280, 465, 230, 40); botónRegresar.addActionListener(this); panelDeContención.add(botónRegresar);
    }
    
    private void cargarInventarioDesdeSQL() { modeloDeDatosLista.clear(); for (Equipo equipo : new CatálogosDAO().obtenerInventarioDeSuministros()) modeloDeDatosLista.addElement(equipo); }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botónRegresar) { new MenúPrincipal().setVisible(true); this.dispose(); } 
        else if (e.getSource() == botónGenerar) prepararComprobanteYVistaPrevia();
    }
    
    private String enmascararCorreo(String c) { if (!c.contains("@")) return "***"; int a = c.indexOf("@"); return a <= 2 ? "**" + c.substring(a) : c.substring(0, 2) + "***" + c.substring(a); }
    private String enmascararTel(String t) { return t.length() < 4 ? "****" : t.substring(0, 2) + "*****" + t.substring(t.length() - 2); }
    private String enmascararDoc(String d) { return d.length() < 2 ? "*" : d.charAt(0) + "****" + d.charAt(d.length() - 1); }
    
    private void prepararComprobanteYVistaPrevia() {
        try {
            String nom = campoNombreCliente.getText().trim().toUpperCase(), tel = campoTeléfono.getText().trim(), cor = campoCorreo.getText().trim().toUpperCase(), doc = campoDocumento.getText().trim(), tipo = menúComprobante.getSelectedItem().toString();
            if (nom.isEmpty() || tel.isEmpty() || cor.isEmpty() || doc.isEmpty()) { JOptionPane.showMessageDialog(this, "Complete todos los datos."); return; }
            if (!nom.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) { JOptionPane.showMessageDialog(this, "Nombre inválido."); return; }
            if (!tel.matches("^\\d{9}$")) { JOptionPane.showMessageDialog(this, "Teléfono inválido (9 dígitos)."); return; }
            if (!cor.matches("^[A-Z0-9+_.-]+@[A-Z0-9.-]+$")) { JOptionPane.showMessageDialog(this, "Correo inválido."); return; }
            if (tipo.equals("Boleta") && !doc.matches("^\\d{8}$")) { JOptionPane.showMessageDialog(this, "DNI debe tener 8 dígitos."); return; }
            if (tipo.equals("Factura") && !doc.matches("^(10|15|17|20)\\d{9}$")) { JOptionPane.showMessageDialog(this, "RUC inválido."); return; }
            
            List<Equipo> sel = listaCatálogoVisual.getSelectedValuesList();
            if (sel.isEmpty()) { JOptionPane.showMessageDialog(this, "Seleccione al menos un equipo."); return; }
            
            totalAPagarActual = 0; StringBuilder det = new StringBuilder();
            for (Equipo equipo : sel) { totalAPagarActual += equipo.obtenerPrecioBase(); det.append("- ").append(equipo.obtenerNombre()).append("\n"); }
            
            double subtotal = totalAPagarActual / 1.18, igv = totalAPagarActual - subtotal;
            Cliente cliente = new Cliente(nom, tel, cor, tipo, doc);
            String fechaYHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss"));
            
            if (this.númeroComprobanteActual.isEmpty() || !this.númeroComprobanteActual.contains(tipo.equals("Factura") ? "F" : "B")) {
                this.númeroComprobanteActual = new CatálogosDAO().obtenerYGenerarSiguienteNúmero(tipo);
            }
            this.nombreClienteActual = cliente.obtenerNombreCompleto(); 

            String cabecera = "=================================================\n       DH PROYECTOS Y CONTRATISTAS S.A.C.\n    RUC: 20517614051 | Av. Las Lomas 123, Lima\n=================================================\n";
            String formato = cabecera 
                + "TIPO: " + tipo.toUpperCase() + " ELECTRÓNICA\n"
                + "NRO. COMPROBANTE: " + númeroComprobanteActual + "\n"
                + "FECHA: " + fechaYHora + "\n"
                + "ATENDIDO POR: " + nombreCajeroActivo.toUpperCase() + "\n"
                + "-------------------------------------------------\n"
                + "CLIENTE: %s\nCORREO: %s\nTELÉFONO: %s\n" + (tipo.equals("Factura") ? "RUC: " : "DNI: ") + "%s\n"
                + "-------------------------------------------------\n"
                + "DETALLE: Suministro de Materiales\n" + det.toString()
                + "-------------------------------------------------\n"
                + "SUBTOTAL: S/ " + String.format(Locale.US, "%.2f", subtotal) + "\n"
                + "IGV: S/ " + String.format(Locale.US, "%.2f", igv) + "\n"
                + "TOTAL A PAGAR: S/ " + String.format(Locale.US, "%.2f", totalAPagarActual) + "\n"
                + "=================================================";

            comprobanteReal = String.format(formato, nom, cliente.obtenerCorreo(), cliente.obtenerTeléfono(), cliente.obtenerNúmeroDocumento());
            comprobanteCensurado = String.format(formato, cliente.obtenerNombreProtegido(), enmascararCorreo(cliente.obtenerCorreo()), enmascararTel(cliente.obtenerTeléfono()), enmascararDoc(cliente.obtenerNúmeroDocumento()));

            lanzarVentanaDePrevisualización(sel);
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error de sistema.", "Error", JOptionPane.ERROR_MESSAGE); }
    }
    
    private void lanzarVentanaDePrevisualización(List<Equipo> sel) {
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
            campoNombreCliente.setText(""); campoTeléfono.setText(""); campoCorreo.setText(""); campoDocumento.setText(""); númeroComprobanteActual = ""; listaCatálogoVisual.clearSelection();
            botónGenerar.setText("Generar Comprobante"); botónRegresar.setEnabled(true);
        });
        prev.add(btnCanc);
        
        JButton btnConf = new JButton("Confirmar Venta"); btnConf.setBackground(new Color(16, 185, 129)); btnConf.setForeground(Color.WHITE); btnConf.setBounds(240, 410, 140, 35);
        btnConf.addActionListener(e -> {
            prev.dispose(); setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            CatálogosDAO cDao = new CatálogosDAO(); for(Equipo eq : sel) cDao.reducirInventarioDeSuministro(eq.obtenerNombre());
            
            if (new ComprobanteDAO().registrarComprobante(númeroComprobanteActual, nombreClienteActual, "SERV. SUMINISTROS", totalAPagarActual, comprobanteReal)) {
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
                
                cargarInventarioDesdeSQL(); campoNombreCliente.setText(""); campoTeléfono.setText(""); campoCorreo.setText(""); campoDocumento.setText(""); númeroComprobanteActual = "";
                botónGenerar.setText("Generar Comprobante"); botónRegresar.setEnabled(true); 
            } else { setCursor(Cursor.getDefaultCursor()); JOptionPane.showMessageDialog(this, "Error al registrar en BD."); }
        }); prev.add(btnConf); prev.setVisible(true);
    }
}