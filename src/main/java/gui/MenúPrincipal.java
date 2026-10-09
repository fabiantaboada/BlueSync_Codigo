package gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import lógica.GestorDeSesión;
import modelo.RegistroDeComprobante;
import persistencia.ComprobanteDAO;

public class MenúPrincipal extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private JPanel panelDeContención;
    
    private JMenuBar barraDeMenú;
    private JMenu menúArchivo;
    private JMenuItem ítemAcercaDe;
    private JMenuItem ítemSalir;
    
    private JMenu menúCuenta;
    private JMenuItem ítemConfiguraciónAdministrador; 
    private JMenuItem ítemCerrarSesión; 
    
    private JButton botónConstrucción;
    private JButton botónMantenimiento;
    private JButton botónSuministro;
    private JButton botónCatálogos; 
    private JButton botónHistorial;
    private JButton botónReportes;

    public MenúPrincipal() {
        setTitle("BlueSync Workspace - Panel de Control");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 950, 650); 
        setResizable(false);
        setLocationRelativeTo(null); 
        
        barraDeMenú = new JMenuBar();
        setJMenuBar(barraDeMenú);
        
        menúArchivo = new JMenu("Archivo");
        barraDeMenú.add(menúArchivo);
        
        ítemAcercaDe = new JMenuItem("Acerca de BlueSync");
        ítemAcercaDe.addActionListener(this);
        menúArchivo.add(ítemAcercaDe);
        
        ítemSalir = new JMenuItem("Salir del sistema");
        ítemSalir.addActionListener(this);
        menúArchivo.add(ítemSalir);
        
        menúCuenta = new JMenu("Cuenta");
        barraDeMenú.add(menúCuenta);
        
        ítemConfiguraciónAdministrador = new JMenuItem("Configuración de Usuarios"); 
        ítemConfiguraciónAdministrador.addActionListener(this);
        menúCuenta.add(ítemConfiguraciónAdministrador);
        
        ítemCerrarSesión = new JMenuItem("Cerrar sesión");
        ítemCerrarSesión.addActionListener(this);
        menúCuenta.add(ítemCerrarSesión);
        
        panelDeContención = new JPanel();
        panelDeContención.setBackground(new Color(249, 250, 251)); 
        panelDeContención.setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentPane(panelDeContención);
        panelDeContención.setLayout(null);
        
        // --- SIDEBAR ---
        JPanel panelSidebar = new JPanel();
        panelSidebar.setBackground(new Color(74, 144, 226)); 
        panelSidebar.setBounds(0, 0, 260, 650);
        panelSidebar.setLayout(null);
        panelDeContención.add(panelSidebar);
        
        JLabel etiquetaLogoBlueSync = new JLabel("");
        etiquetaLogoBlueSync.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            ImageIcon iconoBlue = new ImageIcon(MenúPrincipal.class.getResource("/imágenes/logoblneg.png"));
            Image imgBlue = iconoBlue.getImage().getScaledInstance(160, 90, Image.SCALE_SMOOTH);
            etiquetaLogoBlueSync.setIcon(new ImageIcon(imgBlue));
        } catch(Exception excepción) {
            etiquetaLogoBlueSync.setText("BLUESYNC");
            etiquetaLogoBlueSync.setFont(new Font("Segoe UI", Font.BOLD, 24));
            etiquetaLogoBlueSync.setForeground(Color.WHITE);
        }
        etiquetaLogoBlueSync.setBounds(0, 20, 260, 90);
        panelSidebar.add(etiquetaLogoBlueSync);
        
        JSeparator sepSidebar = new JSeparator();
        sepSidebar.setBackground(new Color(133, 186, 245)); 
        sepSidebar.setForeground(new Color(133, 186, 245));
        sepSidebar.setBounds(30, 120, 200, 2);
        panelSidebar.add(sepSidebar);
        
        int posBotonesY = 140;
        int altoBotón = 40;
        int espaciado = 5;
        
        botónConstrucción = crearBotónSidebar("Servicio de Construcción", posBotonesY);
        panelSidebar.add(botónConstrucción); posBotonesY += altoBotón + espaciado;
        
        botónMantenimiento = crearBotónSidebar("Servicio de Mantenimiento", posBotonesY);
        panelSidebar.add(botónMantenimiento); posBotonesY += altoBotón + espaciado;
        
        botónSuministro = crearBotónSidebar("Suministro de Materiales", posBotonesY);
        panelSidebar.add(botónSuministro); posBotonesY += altoBotón + espaciado;
        
        botónHistorial = crearBotónSidebar("Historial de Comprobantes", posBotonesY);
        panelSidebar.add(botónHistorial); posBotonesY += altoBotón + espaciado;
        
        botónReportes = crearBotónSidebar("Reportes de Ventas", posBotonesY);
        panelSidebar.add(botónReportes); posBotonesY += altoBotón + espaciado;
        
        botónCatálogos = crearBotónSidebar("Gestión de Catálogos", posBotonesY);
        panelSidebar.add(botónCatálogos); posBotonesY += altoBotón + espaciado;

        // --- CABECERA SUPERIOR ---
        JPanel panelCabecera = new JPanel();
        panelCabecera.setBackground(Color.WHITE);
        panelCabecera.setBounds(260, 0, 690, 80);
        panelCabecera.setLayout(null);
        panelCabecera.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(229, 231, 235)));
        panelDeContención.add(panelCabecera);
        
        String nombreUsuarioActual = GestorDeSesión.obtenerNombreCompletoActivo();
        boolean esAdmin = GestorDeSesión.esAdministrador();
        
        JLabel etiquetaBienvenida = new JLabel("Hola, " + nombreUsuarioActual);
        etiquetaBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 20));
        etiquetaBienvenida.setForeground(new Color(31, 41, 55));
        etiquetaBienvenida.setBounds(30, 25, 400, 30);
        panelCabecera.add(etiquetaBienvenida);
        
        JLabel etiquetaRol = new JLabel("Rol: " + GestorDeSesión.obtenerRolActivo());
        etiquetaRol.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        etiquetaRol.setForeground(new Color(107, 114, 128));
        etiquetaRol.setBounds(30, 50, 400, 20);
        panelCabecera.add(etiquetaRol);
        
        JLabel etiquetaLogoEmpresa = new JLabel("");
        etiquetaLogoEmpresa.setHorizontalAlignment(SwingConstants.RIGHT);
        try {
            ImageIcon iconoEmpresa = new ImageIcon(MenúPrincipal.class.getResource("/imágenes/logo.png"));
            Image imgEmpresa = iconoEmpresa.getImage().getScaledInstance(120, 60, Image.SCALE_SMOOTH);
            etiquetaLogoEmpresa.setIcon(new ImageIcon(imgEmpresa));
        } catch(Exception excepción) {
            etiquetaLogoEmpresa.setText("[ DH PROYECTOS ]");
        }
        etiquetaLogoEmpresa.setBounds(520, 10, 130, 60);
        panelCabecera.add(etiquetaLogoEmpresa);
        
        // --- LÓGICA DE DASHBOARD HÍBRIDO ---
        ComprobanteDAO dao = new ComprobanteDAO();
        String fechaHoy = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        List<RegistroDeComprobante> ventasHoy = dao.generarReporte(fechaHoy, "Todos");
        
        double ingresosHoy = 0.0;
        int operacionesHoy = 0;
        String firmaCajero = "ATENDIDO POR: " + nombreUsuarioActual.toUpperCase();
        
        for (RegistroDeComprobante r : ventasHoy) {
            if (!r.obtenerEstado().equals("ANULADO")) {
                if (esAdmin || r.obtenerContenidoRecibo().contains(firmaCajero)) {
                    ingresosHoy += r.obtenerMontoTotal();
                    operacionesHoy++;
                }
            }
        }
        
        String tituloOperaciones = esAdmin ? "OPERACIONES (GLOBAL)" : "MIS OPERACIONES (HOY)";
        String tituloIngresos = esAdmin ? "INGRESOS (GLOBAL)" : "MIS INGRESOS (HOY)";
        
        JPanel widgetOperaciones = crearWidget(tituloOperaciones, String.valueOf(operacionesHoy), 290, 110, new Color(236, 253, 245), new Color(6, 95, 70));
        panelDeContención.add(widgetOperaciones);
        
        JPanel widgetIngresos = crearWidget(tituloIngresos, "S/ " + String.format("%.2f", ingresosHoy), 465, 110, new Color(239, 246, 255), new Color(30, 58, 138));
        panelDeContención.add(widgetIngresos);
        
        String tituloTabla = esAdmin ? "Actividad Reciente General" : "Mi Actividad Reciente";
        JLabel títuloActividad = new JLabel(tituloTabla);
        títuloActividad.setFont(new Font("Segoe UI", Font.BOLD, 14));
        títuloActividad.setForeground(new Color(75, 85, 99));
        títuloActividad.setBounds(290, 210, 300, 20);
        panelDeContención.add(títuloActividad);
        
        String[] columnas = { "Comprobante", "Servicio", "Monto" };
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0) {
            private static final long serialVersionUID = 1L;
            public boolean isCellEditable(int fila, int columna) { return false; }
        };
        
        List<RegistroDeComprobante> ultimasVentasBD = dao.obtenerÚltimasVentas(50);
        int contadorTabla = 0; //cantidad de filas
        
        for (RegistroDeComprobante r : ultimasVentasBD) {
            if (esAdmin || r.obtenerContenidoRecibo().contains(firmaCajero)) {
                modeloTabla.addRow(new Object[]{ r.obtenerNúmeroComprobante(), r.obtenerTipoServicio(), "S/ " + String.format("%.2f", r.obtenerMontoTotal()) });
                contadorTabla++;
                if (contadorTabla == 6) break;
            }
        }
        
        JTable tablaReciente = new JTable(modeloTabla);
        tablaReciente.setRowHeight(30);
        tablaReciente.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaReciente.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaReciente.getTableHeader().setBackground(new Color(243, 244, 246));
        tablaReciente.getTableHeader().setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235)));
        
        JScrollPane panelDesplazamientoTabla = new JScrollPane(tablaReciente);
        panelDesplazamientoTabla.setBounds(290, 240, 335, 270);
        panelDesplazamientoTabla.setBorder(BorderFactory.createLineBorder(new Color(209, 213, 219)));
        panelDesplazamientoTabla.getViewport().setBackground(Color.WHITE);
        panelDeContención.add(panelDesplazamientoTabla);

        // --- ZONA CENTRAL: IMAGEN VERTICAL ---
        JLabel etiquetaImagenDashboard = new JLabel("");
        etiquetaImagenDashboard.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            ImageIcon iconoDashboard = new ImageIcon(MenúPrincipal.class.getResource("/imágenes/imagen_dashboard.jpg"));
            Image imgDash = iconoDashboard.getImage().getScaledInstance(265, 400, Image.SCALE_SMOOTH);
            etiquetaImagenDashboard.setIcon(new ImageIcon(imgDash));
            etiquetaImagenDashboard.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1));
        } catch(Exception excepción) {
            etiquetaImagenDashboard.setText("[ IMAGEN DASHBOARD ]");
            etiquetaImagenDashboard.setForeground(Color.LIGHT_GRAY);
            etiquetaImagenDashboard.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        }
        etiquetaImagenDashboard.setBounds(645, 110, 265, 400);
        panelDeContención.add(etiquetaImagenDashboard);
        
        configurarPermisosPorRol();
    }

    private JPanel crearWidget(String titulo, String valor, int x, int y, Color colorFondo, Color colorTexto) {
        JPanel panel = new JPanel(null);
        panel.setBounds(x, y, 160, 80);
        panel.setBackground(colorFondo);
        panel.setBorder(BorderFactory.createLineBorder(colorFondo.darker(), 1));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblTitulo.setForeground(colorTexto);
        lblTitulo.setBounds(10, 15, 140, 15);
        panel.add(lblTitulo);

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblValor.setForeground(colorTexto);
        lblValor.setBounds(10, 35, 140, 30);
        panel.add(lblValor);

        return panel;
    }

    private JButton crearBotónSidebar(String texto, int posY) {
        JButton botón = new JButton(texto);
        botón.setBounds(0, posY, 260, 40);
        botón.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botón.setForeground(Color.WHITE);
        botón.setBackground(new Color(74, 144, 226));
        botón.setHorizontalAlignment(SwingConstants.LEFT);
        botón.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0)); 
        botón.setFocusPainted(false);
        botón.setBorderPainted(false);
        botón.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botón.addActionListener(this);
        return botón;
    }

    private void configurarPermisosPorRol() {
        if (!GestorDeSesión.esAdministrador()) {
            botónCatálogos.setVisible(false);
            botónReportes.setVisible(false); 
            ítemConfiguraciónAdministrador.setVisible(false); 
            botónHistorial.setBounds(0, 275, 260, 40);
        }
    }

    private void confirmarCierreDePrograma() {
        Object[] opciones = {"Mantener sesión", "Cerrar sesión", "Cancelar"};
        int elección = JOptionPane.showOptionDialog(this, "¿Desea cerrar sesión antes de salir?", "Confirmar salida", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        if (elección == 0) { 
            System.exit(0); 
        } else if (elección == 1) { 
            GestorDeSesión.cerrarSesión();
            System.exit(0);
        }
    }

    public void actionPerformed(ActionEvent evento) {
        if (evento.getSource() == botónConstrucción) {
            new VentanaCotización().setVisible(true);
            this.dispose();
        } else if (evento.getSource() == botónMantenimiento) {
            new VentanaMantenimiento().setVisible(true);
            this.dispose();
        } else if (evento.getSource() == botónSuministro) {
            new VentanaSuministro().setVisible(true);
            this.dispose();
        } else if (evento.getSource() == botónHistorial) {  
            new VentanaHistorialVentas().setVisible(true);
            this.dispose();  
        } else if (evento.getSource() == botónReportes) {   
            new VentanaReportes().setVisible(true); 
            this.dispose();  
        } else if (evento.getSource() == botónCatálogos) {
            new VentanaConfiguración().setVisible(true);
            this.dispose();
        } else if (evento.getSource() == ítemConfiguraciónAdministrador) {
            new VentanaConfiguraciónAdmin(this).setVisible(true); 
        } else if (evento.getSource() == ítemAcercaDe) {
            new VentanaAcercaDe().setVisible(true); 
        } else if (evento.getSource() == ítemSalir) {
            confirmarCierreDePrograma();
        } else if (evento.getSource() == ítemCerrarSesión) {
            GestorDeSesión.cerrarSesión();
            JOptionPane.showMessageDialog(this, "Se cerró la sesión de manera satisfactoria.");
            new VentanaSeleccionRol().setVisible(true); 
            this.dispose();
        }
    }
}