package gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import lógica.GestorDocumentos;
import modelo.RegistroDeComprobante;
import persistencia.ComprobanteDAO;

public class VentanaReportes extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private JPanel panelDeContención;
    private JTextField campoFecha;
    private JComboBox<String> menúServicios;
    private JButton botónGenerar;
    private JLabel etiquetaTotalRecaudado, etiquetaOperaciones;
    private JTable tablaResultados;
    private DefaultTableModel modeloTabla;
    private JButton botónExportar, botónRegresar;
    
    private ComprobanteDAO comprobanteDAO;
    private double montoTotalActual = 0.0;
    private int cantidadOperacionesActual = 0;
    private List<RegistroDeComprobante> listaResultados = new ArrayList<>();

    public VentanaReportes() {
        this.comprobanteDAO = new ComprobanteDAO();
        setTitle("BlueSync - Tablero de Reportes Financieros");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 780, 600); setResizable(false); setLocationRelativeTo(null);
        
        panelDeContención = new JPanel(); panelDeContención.setBackground(Color.WHITE); panelDeContención.setBorder(new EmptyBorder(5, 5, 5, 5)); setContentPane(panelDeContención); panelDeContención.setLayout(null);

        JLabel etiquetaTítulo = new JLabel("Reporte Gerencial de Ventas"); etiquetaTítulo.setFont(new Font("Segoe UI", Font.BOLD, 22)); etiquetaTítulo.setForeground(new Color(26, 86, 219)); etiquetaTítulo.setHorizontalAlignment(SwingConstants.CENTER); etiquetaTítulo.setBounds(10, 15, 744, 30); panelDeContención.add(etiquetaTítulo);
        JSeparator separador = new JSeparator(); separador.setBounds(30, 55, 700, 2); separador.setForeground(new Color(229, 231, 235)); panelDeContención.add(separador);
        
        JPanel panelFiltros = new JPanel(null); panelFiltros.setBackground(Color.WHITE); panelFiltros.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(209, 213, 219)), "Filtros de Búsqueda", TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12), new Color(107, 114, 128))); panelFiltros.setBounds(30, 70, 700, 70); panelDeContención.add(panelFiltros);
        
        JLabel etiquetaFecha = new JLabel("Fecha (dd/mm/aaaa):"); etiquetaFecha.setFont(new Font("Segoe UI", Font.BOLD, 12)); etiquetaFecha.setBounds(20, 25, 140, 20); panelFiltros.add(etiquetaFecha);
        campoFecha = new JTextField(); campoFecha.setFont(new Font("Segoe UI", Font.PLAIN, 13)); campoFecha.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))); campoFecha.setBounds(150, 22, 120, 28); panelFiltros.add(campoFecha);
        JLabel etiquetaServicio = new JLabel("Servicio:"); etiquetaServicio.setFont(new Font("Segoe UI", Font.BOLD, 12)); etiquetaServicio.setBounds(300, 25, 60, 20); panelFiltros.add(etiquetaServicio);
        menúServicios = new JComboBox<>(new String[]{"Todos", "Construcción", "Mantenimiento", "Suministros"}); menúServicios.setFont(new Font("Segoe UI", Font.PLAIN, 13)); menúServicios.setBounds(360, 22, 160, 28); panelFiltros.add(menúServicios);
        botónGenerar = new JButton("Consultar"); botónGenerar.setFont(new Font("Segoe UI", Font.BOLD, 12)); botónGenerar.setBackground(new Color(26, 86, 219)); botónGenerar.setForeground(Color.WHITE); botónGenerar.setBounds(550, 22, 120, 28); botónGenerar.addActionListener(this); panelFiltros.add(botónGenerar);
        
        JPanel tarjetaIngresos = new JPanel(null); tarjetaIngresos.setBackground(new Color(239, 246, 255)); tarjetaIngresos.setBorder(BorderFactory.createLineBorder(new Color(191, 219, 254))); tarjetaIngresos.setBounds(30, 155, 340, 90); panelDeContención.add(tarjetaIngresos);
        JLabel títuloTotal = new JLabel("TOTAL RECAUDADO (S/)"); títuloTotal.setFont(new Font("Segoe UI", Font.BOLD, 13)); títuloTotal.setForeground(new Color(30, 64, 175)); títuloTotal.setBounds(20, 15, 200, 20); tarjetaIngresos.add(títuloTotal);
        etiquetaTotalRecaudado = new JLabel("S/ 0.00"); etiquetaTotalRecaudado.setFont(new Font("Segoe UI", Font.BOLD, 28)); etiquetaTotalRecaudado.setForeground(new Color(30, 58, 138)); etiquetaTotalRecaudado.setBounds(20, 40, 300, 35); tarjetaIngresos.add(etiquetaTotalRecaudado);
        
        JPanel tarjetaOperaciones = new JPanel(null); tarjetaOperaciones.setBackground(new Color(236, 253, 245)); tarjetaOperaciones.setBorder(BorderFactory.createLineBorder(new Color(167, 243, 208))); tarjetaOperaciones.setBounds(390, 155, 340, 90); panelDeContención.add(tarjetaOperaciones);
        JLabel títuloOperaciones = new JLabel("OPERACIONES REALIZADAS"); títuloOperaciones.setFont(new Font("Segoe UI", Font.BOLD, 13)); títuloOperaciones.setForeground(new Color(6, 95, 70)); títuloOperaciones.setBounds(20, 15, 200, 20); tarjetaOperaciones.add(títuloOperaciones);
        etiquetaOperaciones = new JLabel("0"); etiquetaOperaciones.setFont(new Font("Segoe UI", Font.BOLD, 28)); etiquetaOperaciones.setForeground(new Color(6, 78, 59)); etiquetaOperaciones.setBounds(20, 40, 300, 35); tarjetaOperaciones.add(etiquetaOperaciones);
        
        String[] columnas = { "N° Comprobante", "Cliente", "Tipo de Servicio", "Monto (S/)" };
        modeloTabla = new DefaultTableModel(columnas, 0) { private static final long serialVersionUID = 1L; public boolean isCellEditable(int fila, int columna) { return false; } };
        tablaResultados = new JTable(modeloTabla); tablaResultados.setRowHeight(35); tablaResultados.setFont(new Font("Segoe UI", Font.PLAIN, 13)); tablaResultados.getTableHeader().setBackground(new Color(243, 244, 246));
        JScrollPane panelDesplazamientoTabla = new JScrollPane(tablaResultados); panelDesplazamientoTabla.setBounds(30, 260, 700, 230); panelDesplazamientoTabla.getViewport().setBackground(Color.WHITE); panelDeContención.add(panelDesplazamientoTabla);
        
        botónExportar = new JButton("Exportar Resumen (.txt)"); botónExportar.setFont(new Font("Segoe UI", Font.BOLD, 14)); botónExportar.setBackground(new Color(16, 185, 129)); botónExportar.setForeground(Color.WHITE); botónExportar.setBounds(30, 505, 230, 40); botónExportar.addActionListener(this); panelDeContención.add(botónExportar);
        botónRegresar = new JButton("Regresar al Panel"); botónRegresar.setFont(new Font("Segoe UI", Font.BOLD, 14)); botónRegresar.setBackground(new Color(107, 114, 128)); botónRegresar.setForeground(Color.WHITE); botónRegresar.setBounds(500, 505, 230, 40); botónRegresar.addActionListener(this); panelDeContención.add(botónRegresar);
    }

    private void procesarConsulta() {
        String fechaBuscada = campoFecha.getText().trim();
        String servicioSeleccionado = menúServicios.getSelectedItem().toString();
        if (!fechaBuscada.matches("^\\d{2}/\\d{2}/\\d{4}$")) { JOptionPane.showMessageDialog(this, "La fecha debe tener el formato dd/mm/aaaa.", "Formato Inválido", JOptionPane.WARNING_MESSAGE); return; }

        List<RegistroDeComprobante> resultados = comprobanteDAO.generarReporte(fechaBuscada, servicioSeleccionado);
        modeloTabla.setRowCount(0); listaResultados.clear(); montoTotalActual = 0.0; cantidadOperacionesActual = 0;
        
        if (resultados.isEmpty()) { JOptionPane.showMessageDialog(this, "No se encontraron ventas.", "Sin Datos", JOptionPane.INFORMATION_MESSAGE); } 
        else {
            for (RegistroDeComprobante comprobante : resultados) {
                if (!comprobante.obtenerEstado().equals("ANULADO")) {
                    listaResultados.add(comprobante);
                    modeloTabla.addRow(new Object[]{ comprobante.obtenerNúmeroComprobante(), comprobante.obtenerCliente(), comprobante.obtenerTipoServicio(), String.format(Locale.US, "%.2f", comprobante.obtenerMontoTotal()) });
                    montoTotalActual += comprobante.obtenerMontoTotal(); cantidadOperacionesActual++;
                }
            }
        }
        etiquetaTotalRecaudado.setText("S/ " + String.format(Locale.US, "%.2f", montoTotalActual));
        etiquetaOperaciones.setText(String.valueOf(cantidadOperacionesActual));
    }

    private void exportarReporte() {
        if (listaResultados.isEmpty()) { JOptionPane.showMessageDialog(this, "No hay datos en pantalla para exportar.", "Aviso", JOptionPane.WARNING_MESSAGE); return; }
        String fechaFiltro = campoFecha.getText().trim().replace("/", "-");
        
        JFileChooser selectorDeArchivos = new JFileChooser();
        selectorDeArchivos.setDialogTitle("Exportar Reporte Gerencial a TXT");
        selectorDeArchivos.setFileFilter(new FileNameExtensionFilter("Archivo de Texto (*.txt)", "txt"));
        selectorDeArchivos.setSelectedFile(new File("Cierre_Caja_" + fechaFiltro + ".txt"));
        
        if (selectorDeArchivos.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File archivoDestino = selectorDeArchivos.getSelectedFile();
            if (!archivoDestino.getName().toLowerCase().endsWith(".txt")) { archivoDestino = new File(archivoDestino.getAbsolutePath() + ".txt"); }
            
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            String temp = GestorDocumentos.generarReporteVentasTXT(listaResultados);
            setCursor(Cursor.getDefaultCursor());
            
            if (temp != null) {
                try { 
                    Files.copy(new File(temp).toPath(), archivoDestino.toPath(), StandardCopyOption.REPLACE_EXISTING); 
                    new File(temp).delete(); 
                    JOptionPane.showMessageDialog(this, "Reporte exportado correctamente en TXT."); 
                } catch (Exception ex) { 
                    JOptionPane.showMessageDialog(this, "Error al guardar el archivo.", "Error", JOptionPane.ERROR_MESSAGE); 
                }
            }
        }
    }

    public void actionPerformed(ActionEvent evento) {
        if (evento.getSource() == botónRegresar) { new MenúPrincipal().setVisible(true); this.dispose(); } 
        else if (evento.getSource() == botónGenerar) { procesarConsulta(); } 
        else if (evento.getSource() == botónExportar) { exportarReporte(); }
    }
}