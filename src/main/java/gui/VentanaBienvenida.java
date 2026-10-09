package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class VentanaBienvenida extends JWindow {

    private static final long serialVersionUID = 1L;

    public VentanaBienvenida(String nombreUsuario, String rolUsuario) {
        // JWindow no tiene bordes nativos de Windows, es un lienzo limpio
        setBounds(100, 100, 500, 260);
        setLocationRelativeTo(null); 
        
        JPanel panelDeContención = new JPanel();
        panelDeContención.setBackground(Color.WHITE);
        // Un borde azul corporativo elegante para enmarcar la tarjeta
        panelDeContención.setBorder(BorderFactory.createLineBorder(new Color(26, 86, 219), 4));
        setContentPane(panelDeContención);
        panelDeContención.setLayout(null);
        
        JLabel etiquetaLogo = new JLabel("");
        etiquetaLogo.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            ImageIcon iconoBlue = new ImageIcon(VentanaBienvenida.class.getResource("/imágenes/logobl.png"));
            Image imgBlue = iconoBlue.getImage().getScaledInstance(180, 100, Image.SCALE_SMOOTH);
            etiquetaLogo.setIcon(new ImageIcon(imgBlue));
        } catch(Exception excepción) {
            etiquetaLogo.setText("[ LOGO BLUESYNC ]");
            etiquetaLogo.setFont(new Font("Segoe UI", Font.BOLD, 18));
            etiquetaLogo.setForeground(new Color(26, 86, 219));
        }
        etiquetaLogo.setBounds(10, 20, 480, 100);
        panelDeContención.add(etiquetaLogo);
        
        JLabel etiquetaMensaje = new JLabel("¡Bienvenido de nuevo!");
        etiquetaMensaje.setFont(new Font("Segoe UI", Font.BOLD, 22));
        etiquetaMensaje.setForeground(new Color(31, 41, 55));
        etiquetaMensaje.setHorizontalAlignment(SwingConstants.CENTER);
        etiquetaMensaje.setBounds(10, 130, 480, 30);
        panelDeContención.add(etiquetaMensaje);
        
        JLabel etiquetaNombre = new JLabel(nombreUsuario);
        etiquetaNombre.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        etiquetaNombre.setForeground(new Color(26, 86, 219));
        etiquetaNombre.setHorizontalAlignment(SwingConstants.CENTER);
        etiquetaNombre.setBounds(10, 165, 480, 25);
        panelDeContención.add(etiquetaNombre);
        
        JLabel etiquetaCargando = new JLabel("Iniciando " + (rolUsuario.equals("ADMIN") ? "panel de Administrador" : "espacio de trabajo") + "...");
        etiquetaCargando.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        etiquetaCargando.setForeground(new Color(156, 163, 175));
        etiquetaCargando.setHorizontalAlignment(SwingConstants.CENTER);
        etiquetaCargando.setBounds(10, 210, 480, 20);
        panelDeContención.add(etiquetaCargando);
        
        // Temporizador: Espera 2500 milisegundos (2.5 segundos) y ejecuta la acción
        Timer temporizador = new Timer(2500, evento -> {
            new MenúPrincipal().setVisible(true); // Abre el menú principal
            this.dispose(); // Destruye esta ventana de bienvenida
        });
        temporizador.setRepeats(false); // Para que se ejecute una sola vez
        temporizador.start();
    }
}