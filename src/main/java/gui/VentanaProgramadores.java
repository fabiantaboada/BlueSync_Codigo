package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class VentanaProgramadores extends JDialog implements ActionListener {

    private static final long serialVersionUID = 1L;
    private JPanel panelDeContención;
    private JButton botónCerrar;

    public VentanaProgramadores() {
        setTitle("BlueSync - Equipo de Desarrollo");
        setModal(true); 
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); 
        setBounds(100, 100, 950, 460); // Ligeramente más alta para el botón
        setLocationRelativeTo(null); 
        
        panelDeContención = new JPanel();
        panelDeContención.setBackground(Color.WHITE);
        panelDeContención.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(panelDeContención);
        panelDeContención.setLayout(new BorderLayout(0, 15));
        
        // --- CABECERA ---
        JPanel panelCabecera = new JPanel(new BorderLayout(0, 10));
        panelCabecera.setBackground(Color.WHITE);
        
        JLabel etiquetaTítulo = new JLabel("Equipo de Desarrollo de BlueSync", SwingConstants.CENTER);
        etiquetaTítulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        etiquetaTítulo.setForeground(new Color(26, 86, 219)); 
        panelCabecera.add(etiquetaTítulo, BorderLayout.CENTER);
        
        JSeparator separador = new JSeparator();
        separador.setForeground(new Color(229, 231, 235));
        panelCabecera.add(separador, BorderLayout.SOUTH);
        
        panelDeContención.add(panelCabecera, BorderLayout.NORTH);
        
        // --- CONTENEDOR DE FOTOS (GRID) ---
        JPanel panelGrid = new JPanel();
        panelGrid.setBackground(Color.WHITE);
        panelGrid.setLayout(new GridLayout(1, 5, 15, 0)); 
        panelDeContención.add(panelGrid, BorderLayout.CENTER);
        
        // --- AGREGAR INTEGRANTES AL GRID CON CÓDIGOS DE ESTUDIANTE ---
        panelGrid.add(crearTarjetaIntegrante("/imágenes/foto1.jpeg", "Fabián Taboada Estrada", "N00534187"));
        panelGrid.add(crearTarjetaIntegrante("/imágenes/foto2.jpeg", "Marycielo Gonzales Rupay", "N00537700"));
        panelGrid.add(crearTarjetaIntegrante("/imágenes/foto3.jpeg", "Sean Dereck Blaz Espinoza", "N00528300"));
        panelGrid.add(crearTarjetaIntegrante("/imágenes/foto4.jpeg", "Alonso Quispe Alagon", "N00546222"));
        panelGrid.add(crearTarjetaIntegrante("/imágenes/foto5.jpeg", "Jheferson Waldir Vargas", "N00526094"));
        
        // --- PANEL INFERIOR (BOTÓN CERRAR) ---
        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(Color.WHITE);
        panelInferior.setLayout(new BorderLayout(0, 10));
        
        JSeparator separadorInferior = new JSeparator();
        separadorInferior.setForeground(new Color(229, 231, 235));
        panelInferior.add(separadorInferior, BorderLayout.NORTH);
        
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

    private JPanel crearTarjetaIntegrante(String rutaImagen, String nombreCompleto, String codigoEstudiante) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(0, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235)), 
            new EmptyBorder(10, 10, 10, 10) 
        ));
        
        JLabel etiquetaFoto = new JLabel("");
        etiquetaFoto.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            ImageIcon iconoOriginal = new ImageIcon(VentanaProgramadores.class.getResource(rutaImagen));
            
            // --- ESCALADO PROPORCIONAL ANTI-ESTIRAMIENTO ---
            int anchoOriginal = iconoOriginal.getIconWidth();
            int altoOriginal = iconoOriginal.getIconHeight();
            
            int anchoMáximo = 130;
            int altoMáximo = 150;
            
            if (anchoOriginal > 0 && altoOriginal > 0) {
                double ratioAncho = (double) anchoMáximo / anchoOriginal;
                double ratioAlto = (double) altoMáximo / altoOriginal;
                double ratioDeEscala = Math.min(ratioAncho, ratioAlto); // Toma el ratio menor para no salirse de la caja
                
                int nuevoAncho = (int) (anchoOriginal * ratioDeEscala);
                int nuevoAlto = (int) (altoOriginal * ratioDeEscala);
                
                Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(nuevoAncho, nuevoAlto, Image.SCALE_SMOOTH);
                etiquetaFoto.setIcon(new ImageIcon(imagenEscalada));
            } else {
                etiquetaFoto.setText("[ FOTO ]");
            }
        } catch(Exception e) {
            etiquetaFoto.setText("[ FOTO ]");
            etiquetaFoto.setForeground(Color.LIGHT_GRAY);
        }
        panel.add(etiquetaFoto, BorderLayout.CENTER);
        
        // Nombre arriba en negrita y código abajo en gris
        String textoHTML = "<html><center><b>" + nombreCompleto + "</b><br><span style='color:#6B7280; font-size:10px;'>" + codigoEstudiante + "</span></center></html>";
        JLabel etiquetaTexto = new JLabel(textoHTML);
        etiquetaTexto.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        etiquetaTexto.setForeground(new Color(55, 65, 81)); 
        etiquetaTexto.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(etiquetaTexto, BorderLayout.SOUTH);
        
        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent evento) {
        if (evento.getSource() == botónCerrar) {
            this.dispose();
        }
    }
}