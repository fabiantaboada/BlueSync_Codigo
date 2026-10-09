package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import modelo.RegistroDeComprobante;
import persistencia.ComprobanteDAO;

public class VentanaHistorialVentas extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private JPanel panelDeContención;
    private JTable tablaHistorial;
    private DefaultTableModel modeloTabla;
    private JTextField campoBusqueda;
    private JButton botónBuscar, botónMostrarTodos, botónVerRecibo, botónRegresar;
    
    private List<RegistroDeComprobante> listaOriginal;
    private List<RegistroDeComprobante> listaFiltrada;

    public VentanaHistorialVentas() {
        setTitle("BlueSync - Historial General de Ventas");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 750, 520); setResizable(false); setLocationRelativeTo(null);
        
        panelDeContención = new JPanel(null); panelDeContención.setBackground(Color.WHITE); setContentPane(panelDeContención);
        JLabel etiquetaTítulo = new JLabel("Registro Histórico de Comprobantes", SwingConstants.CENTER);
        etiquetaTítulo.setFont(new Font("Segoe UI", Font.BOLD, 22)); etiquetaTítulo.setForeground(new Color(26, 86, 219)); etiquetaTítulo.setBounds(10, 15, 714, 30); panelDeContención.add(etiquetaTítulo);
        
        JLabel lblBuscar = new JLabel("Buscar N° Comprobante:"); lblBuscar.setFont(new Font("Segoe UI", Font.BOLD, 13)); lblBuscar.setBounds(30, 65, 160, 28); panelDeContención.add(lblBuscar);
        campoBusqueda = new JTextField(); campoBusqueda.setBounds(190, 65, 200, 28); panelDeContención.add(campoBusqueda);
        
        botónBuscar = new JButton("Buscar"); botónBuscar.setBackground(new Color(26, 86, 219)); botónBuscar.setForeground(Color.WHITE); botónBuscar.setBounds(400, 65, 100, 28); botónBuscar.addActionListener(this); panelDeContención.add(botónBuscar);
        botónMostrarTodos = new JButton("Mostrar Todos"); botónMostrarTodos.setBackground(new Color(107, 114, 128)); botónMostrarTodos.setForeground(Color.WHITE); botónMostrarTodos.setBounds(510, 65, 120, 28); botónMostrarTodos.addActionListener(this); panelDeContención.add(botónMostrarTodos);
        
        String[] columnas = {"N° Comprobante", "Cliente", "Servicio", "Estado", "Monto (S/)"};
        modeloTabla = new DefaultTableModel(columnas, 0) { @Override public boolean isCellEditable(int f, int c) { return false; } };
        tablaHistorial = new JTable(modeloTabla); tablaHistorial.setRowHeight(30); tablaHistorial.getTableHeader().setBackground(new Color(243, 244, 246));
        
        JScrollPane scroll = new JScrollPane(tablaHistorial); scroll.setBounds(30, 105, 674, 260); scroll.getViewport().setBackground(Color.WHITE); panelDeContención.add(scroll);
        cargarDatosDesdeBD();
        
        botónVerRecibo = new JButton("Ver Detalle / Editar"); botónVerRecibo.setBackground(new Color(16, 185, 129)); botónVerRecibo.setForeground(Color.WHITE); botónVerRecibo.setBounds(130, 385, 200, 40); botónVerRecibo.addActionListener(this); panelDeContención.add(botónVerRecibo);
        
        botónRegresar = new JButton("Regresar al Panel"); botónRegresar.setBackground(new Color(107, 114, 128)); botónRegresar.setForeground(Color.WHITE); botónRegresar.setBounds(400, 385, 200, 40); botónRegresar.addActionListener(this); panelDeContención.add(botónRegresar);
    }
    
    public void cargarDatosDesdeBD() {
        listaOriginal = new ComprobanteDAO().obtenerÚltimasVentas(500); 
        listaFiltrada = new ArrayList<>(listaOriginal);
        actualizarTablaVisual();
    }
    
    private void actualizarTablaVisual() {
        modeloTabla.setRowCount(0); 
        for (RegistroDeComprobante r : listaFiltrada) modeloTabla.addRow(new Object[]{r.obtenerNúmeroComprobante(), r.obtenerCliente(), r.obtenerTipoServicio(), r.obtenerEstado(), String.format(Locale.US, "%.2f", r.obtenerMontoTotal())});
    }
    
    private void filtrarPorNumero() {
        String texto = campoBusqueda.getText().trim().toUpperCase();
        if (texto.isEmpty()) { JOptionPane.showMessageDialog(this, "Ingrese un N° de Comprobante."); return; }
        
        listaFiltrada.clear();
        for (RegistroDeComprobante r : listaOriginal) {
            if (r.obtenerNúmeroComprobante().toUpperCase().contains(texto)) {
                listaFiltrada.add(r);
            }
        }
        if (listaFiltrada.isEmpty()) { JOptionPane.showMessageDialog(this, "No se encontraron resultados para ese N° de Comprobante."); listaFiltrada.addAll(listaOriginal); campoBusqueda.setText(""); }
        actualizarTablaVisual();
    }

    public void actionPerformed(ActionEvent evento) {
        if (evento.getSource() == botónRegresar) { new MenúPrincipal().setVisible(true); this.dispose(); } 
        else if (evento.getSource() == botónBuscar) filtrarPorNumero();
        else if (evento.getSource() == botónMostrarTodos) { campoBusqueda.setText(""); listaFiltrada.clear(); listaFiltrada.addAll(listaOriginal); actualizarTablaVisual(); } 
        else if (evento.getSource() == botónVerRecibo) {
            int fila = tablaHistorial.getSelectedRow();
            if (fila == -1) { JOptionPane.showMessageDialog(this, "Seleccione un comprobante de la tabla."); return; }
            RegistroDeComprobante registro = listaFiltrada.get(fila);
            new VentanaVerRecibo(registro.obtenerNúmeroComprobante(), registro.obtenerContenidoRecibo(), this).setVisible(true);
        } 
    }
}