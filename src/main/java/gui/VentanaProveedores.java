package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class VentanaProveedores extends JDialog implements ActionListener {

    private static final long serialVersionUID = 1L;
    private JPanel panelDeContención;
    private JButton botónCerrar;

    public VentanaProveedores() {
        setTitle("BlueSync - Tecnología Hidráulica Especializada");
        setModal(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 750, 580); 
        setResizable(false);
        setLocationRelativeTo(null);
        
        panelDeContención = new JPanel();
        panelDeContención.setBackground(Color.WHITE);
        panelDeContención.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(panelDeContención);
        panelDeContención.setLayout(new BorderLayout(0, 10));
        
        // --- CABECERA ---
        JPanel panelCabecera = new JPanel(new BorderLayout(0, 5));
        panelCabecera.setBackground(Color.WHITE);
        
        JLabel etiquetaTítulo = new JLabel("Nuestros Proveedores Oficiales", SwingConstants.CENTER);
        etiquetaTítulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        etiquetaTítulo.setForeground(new Color(26, 86, 219));
        panelCabecera.add(etiquetaTítulo, BorderLayout.CENTER);
        
        JLabel etiquetaSubtítulo = new JLabel("Tecnología Hidráulica Especializada y Equipamiento Profesional", SwingConstants.CENTER);
        etiquetaSubtítulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        etiquetaSubtítulo.setForeground(new Color(107, 114, 128));
        panelCabecera.add(etiquetaSubtítulo, BorderLayout.SOUTH);
        
        panelDeContención.add(panelCabecera, BorderLayout.NORTH);
        
        // --- CONTENEDOR DE LOGOS (GRID) ---
        JPanel panelGrid = new JPanel();
        panelGrid.setBackground(Color.WHITE);
        panelGrid.setLayout(new GridLayout(4, 3, 15, 15)); 
        panelGrid.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        // Inserción de las tarjetas. El segundo parámetro (true/false) indica si recibe "más espacio"
        panelGrid.add(crearTarjetaEscalada("/imágenes/prov1.png", false)); // Hayward
        panelGrid.add(crearTarjetaEscalada("/imágenes/prov2.png", false)); // ESPA
        panelGrid.add(crearTarjetaEscalada("/imágenes/prov3.png", false)); // Aquark
        panelGrid.add(crearTarjetaEscalada("/imágenes/prov4.png", true));  // Pentax (Más espacio)
        panelGrid.add(crearTarjetaEscalada("/imágenes/prov5.png", true));  // CTG (Más espacio)
        panelGrid.add(crearTarjetaEscalada("/imágenes/prov6.png", false)); // Pedrollo
        panelGrid.add(crearTarjetaEscalada("/imágenes/prov7.png", true));  // LEO (Más espacio)
        panelGrid.add(crearTarjetaEscalada("/imágenes/prov8.png", false)); // Pentair
        panelGrid.add(crearTarjetaEscalada("/imágenes/prov9.png", false)); // Aquant
        panelGrid.add(crearTarjetaEscalada("/imágenes/prov10.png", false));// AstralPool
        panelGrid.add(crearTarjetaEscalada("/imágenes/prov11.png", false));// Hydrazzo
        
        JPanel celdaVacia = new JPanel();
        celdaVacia.setBackground(Color.WHITE);
        panelGrid.add(celdaVacia);
        
        panelDeContención.add(panelGrid, BorderLayout.CENTER);
        
        // --- PANEL INFERIOR (BOTÓN) ---
        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(Color.WHITE);
        panelInferior.setLayout(new BorderLayout(0, 10));
        
        JSeparator separador = new JSeparator();
        separador.setForeground(new Color(229, 231, 235));
        panelInferior.add(separador, BorderLayout.NORTH);
        
        JPanel contenedorBoton = new JPanel();
        contenedorBoton.setBackground(Color.WHITE);
        
        botónCerrar = new JButton("Cerrar");
        botónCerrar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        botónCerrar.setBackground(new Color(107, 114, 128)); 
        botónCerrar.setForeground(Color.WHITE);
        botónCerrar.setFocusPainted(false);
        botónCerrar.setBorderPainted(false);
        botónCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botónCerrar.addActionListener(this);
        contenedorBoton.add(botónCerrar);
        
        panelInferior.add(contenedorBoton, BorderLayout.CENTER);
        panelDeContención.add(panelInferior, BorderLayout.SOUTH);
    }

    private JPanel crearTarjetaEscalada(String rutaImagen, boolean masEspacio) {
        JPanel panelTarjeta = new JPanel(new BorderLayout());
        panelTarjeta.setBackground(Color.WHITE);
        
        // Si pide más espacio, le damos menos padding (bordes internos) para que la imagen crezca más
        int padding = masEspacio ? 2 : 15;
        
        panelTarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235)), 
            new EmptyBorder(padding, padding, padding, padding)
        ));
        
        JLabel etiquetaLogo = new JLabel("", SwingConstants.CENTER);
        
        try {
            URL urlImagen = VentanaProveedores.class.getResource(rutaImagen);
            if (urlImagen != null) {
                ImageIcon iconoOriginal = new ImageIcon(urlImagen);
                
                int anchoReal = iconoOriginal.getIconWidth();
                int altoReal = iconoOriginal.getIconHeight();
                
                // Si la tarjeta necesita más espacio, ampliamos su caja límite de escalado
                int anchoMaximo = masEspacio ? 190 : 160;
                int altoMaximo = masEspacio ? 95 : 75; 
                
                if (anchoReal > 0 && altoReal > 0) {
                    double ratioAncho = (double) anchoMaximo / anchoReal;
                    double ratioAlto = (double) altoMaximo / altoReal;
                    double ratio = Math.min(ratioAncho, ratioAlto); 
                    
                    if (ratio > 1.0) ratio = 1.0; 
                    
                    int nuevoAncho = (int) (anchoReal * ratio);
                    int nuevoAlto = (int) (altoReal * ratio);
                    
                    Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(nuevoAncho, nuevoAlto, Image.SCALE_SMOOTH);
                    etiquetaLogo.setIcon(new ImageIcon(imagenEscalada));
                }
            } else {
                etiquetaLogo.setText("[ " + rutaImagen.replace("/imágenes/", "") + " ]");
                etiquetaLogo.setForeground(Color.LIGHT_GRAY);
            }
        } catch(Exception e) {
            etiquetaLogo.setText("Error");
            etiquetaLogo.setForeground(Color.LIGHT_GRAY);
        }
        
        panelTarjeta.add(etiquetaLogo, BorderLayout.CENTER);
        return panelTarjeta;
    }

    public void actionPerformed(ActionEvent evento) {
        if (evento.getSource() == botónCerrar) {
            this.dispose();
        }
    }
}