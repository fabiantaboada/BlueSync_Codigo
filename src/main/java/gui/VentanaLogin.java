package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import lógica.ControladorDeSesión;
import lógica.GestorDeSesión;

public class VentanaLogin extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private JTextField campoUsuario;
    private JPasswordField campoContraseña;
    private JButton botónIngresar, botónRegresar;
    private int intentosFallidos = 0;
    private ControladorDeSesión controlador;
    private String rolEsperado;

    public static void main(String[] argumentos) {
        EventQueue.invokeLater(() -> {
            try {
                GestorDeSesión.cargarSesiónLocal();
                if (GestorDeSesión.haySesiónActiva()) { new MenúPrincipal().setVisible(true); return; }
                new VentanaSeleccionRol().setVisible(true);
            } catch (Exception e) { e.printStackTrace(); }
        });
    }

    public VentanaLogin(String rolSeleccionado) {
        this.rolEsperado = rolSeleccionado;
        controlador = new ControladorDeSesión();
        setTitle("Acceso al sistema - BlueSync");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 500); setResizable(false); setLocationRelativeTo(null);

        JPanel panelDeContención = new JPanel(null); panelDeContención.setBackground(Color.WHITE); setContentPane(panelDeContención);

        // --- PANEL IZQUIERDO (BANNER) ---
        JPanel panelIzquierdo = new JPanel(null); panelIzquierdo.setBackground(new Color(26, 86, 219)); panelIzquierdo.setBounds(0, 0, 350, 500); panelDeContención.add(panelIzquierdo);
        JLabel etiquetaBanner = new JLabel(""); etiquetaBanner.setBounds(0, 0, 350, 500);
        ImageIcon iconoBanner = escalarParaCubrir("/imágenes/banner.jpg", 350, 500);
        if (iconoBanner != null) etiquetaBanner.setIcon(iconoBanner);
        panelIzquierdo.add(etiquetaBanner);

        // --- PANEL DERECHO ---
        JPanel panelDerecho = new JPanel(null); panelDerecho.setBackground(Color.WHITE); panelDerecho.setBounds(350, 0, 450, 500); panelDeContención.add(panelDerecho);

        // --- LOGOS (escalado proporcional, sin estirar) ---
        JLabel logoBlueSync = new JLabel("", SwingConstants.CENTER);
        ImageIcon iconoBlueSync = escalarProporcional("/imágenes/logobl.png", 140, 70);
        if (iconoBlueSync != null) logoBlueSync.setIcon(iconoBlueSync); else logoBlueSync.setText("BLUESYNC");
        logoBlueSync.setBounds(50, 30, 150, 80); panelDerecho.add(logoBlueSync);

        JLabel logoEmpresa = new JLabel("", SwingConstants.CENTER);
        ImageIcon iconoEmpresa = escalarProporcional("/imágenes/logo.png", 140, 70);
        if (iconoEmpresa != null) logoEmpresa.setIcon(iconoEmpresa); else logoEmpresa.setText("EMPRESA");
        logoEmpresa.setBounds(250, 30, 150, 80); panelDerecho.add(logoEmpresa);

        JLabel título = new JLabel("Bienvenido de nuevo", SwingConstants.CENTER); título.setFont(new Font("Segoe UI", Font.BOLD, 22)); título.setBounds(18, 125, 414, 30); panelDerecho.add(título);
        JLabel sub = new JLabel("Ingresando como: " + rolEsperado.toUpperCase(), SwingConstants.CENTER); sub.setFont(new Font("Segoe UI", Font.BOLD, 14)); sub.setForeground(new Color(26, 86, 219)); sub.setBounds(18, 158, 414, 20); panelDerecho.add(sub);

        JLabel lblUsu = new JLabel("Usuario"); lblUsu.setBounds(75, 195, 300, 20); panelDerecho.add(lblUsu);
        campoUsuario = new JTextField(); campoUsuario.setBounds(75, 215, 300, 35); panelDerecho.add(campoUsuario);

        JLabel lblPass = new JLabel("Contraseña"); lblPass.setBounds(75, 265, 300, 20); panelDerecho.add(lblPass);
        campoContraseña = new JPasswordField(); campoContraseña.setBounds(75, 285, 300, 35); panelDerecho.add(campoContraseña);

        botónIngresar = new JButton("Ingresar al sistema"); botónIngresar.setBackground(new Color(26, 86, 219)); botónIngresar.setForeground(Color.WHITE); botónIngresar.setBounds(75, 350, 300, 40); botónIngresar.addActionListener(this); panelDerecho.add(botónIngresar);
        botónRegresar = new JButton("<< Cambiar de perfil"); botónRegresar.setBackground(Color.WHITE); botónRegresar.setBorderPainted(false); botónRegresar.setBounds(75, 400, 300, 30); botónRegresar.addActionListener(this); panelDerecho.add(botónRegresar);
    }

    // MÉTODOS PARA ESCALAR IMÁGENES SIN DEFORMARLAS
    // Carga la imagen desde los recursos; devuelve null si no existe
    private BufferedImage cargarImagen(String ruta) {
        try {
            URL url = VentanaLogin.class.getResource(ruta);
            if (url == null) return null;
            ImageIcon icono = new ImageIcon(url);
            if (icono.getIconWidth() <= 0 || icono.getIconHeight() <= 0) return null;
            BufferedImage imagen = new BufferedImage(icono.getIconWidth(), icono.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = imagen.createGraphics();
            g.drawImage(icono.getImage(), 0, 0, null);
            g.dispose();
            return imagen;
        } catch (Exception e) {
            return null;
        }
    }

    // Ajusta la imagen DENTRO de la caja máxima, respetando su proporción (para logos)
    private ImageIcon escalarProporcional(String ruta, int anchoMáximo, int altoMáximo) {
        BufferedImage original = cargarImagen(ruta);
        if (original == null) return null;

        double razón = Math.min((double) anchoMáximo / original.getWidth(), (double) altoMáximo / original.getHeight());
        int nuevoAncho = Math.max(1, (int) Math.round(original.getWidth() * razón));
        int nuevoAlto = Math.max(1, (int) Math.round(original.getHeight() * razón));

        BufferedImage escalada = new BufferedImage(nuevoAncho, nuevoAlto, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = escalada.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(original, 0, 0, nuevoAncho, nuevoAlto, null);
        g.dispose();
        return new ImageIcon(escalada);
    }

    // Llena TODA la caja sin deformar la imagen, recortando lo que sobre (para el banner)
    private ImageIcon escalarParaCubrir(String ruta, int ancho, int alto) {
        BufferedImage original = cargarImagen(ruta);
        if (original == null) return null;

        double razón = Math.max((double) ancho / original.getWidth(), (double) alto / original.getHeight());
        int anchoEscalado = (int) Math.round(original.getWidth() * razón);
        int altoEscalado = (int) Math.round(original.getHeight() * razón);

        BufferedImage lienzo = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = lienzo.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.drawImage(original, (ancho - anchoEscalado) / 2, (alto - altoEscalado) / 2, anchoEscalado, altoEscalado, null);
        g.dispose();
        return new ImageIcon(lienzo);
    }

    public void actionPerformed(ActionEvent evento) {
        if (evento.getSource() == botónIngresar) validarInicioDeSesión();
        else if (evento.getSource() == botónRegresar) { new VentanaSeleccionRol().setVisible(true); this.dispose(); }
    }

    private void validarInicioDeSesión() {
        String usuario = campoUsuario.getText().trim();
        String contraseña = new String(campoContraseña.getPassword()).trim();
        if (usuario.isEmpty() || contraseña.isEmpty()) { JOptionPane.showMessageDialog(this, "Ingrese sus credenciales."); return; }

        String[] datosIngreso = controlador.validarEIniciarSesión(usuario, contraseña);
        if (datosIngreso != null) {
            String rolEncontradoEnBD = datosIngreso[0].toUpperCase();
            boolean rolVálido = false;

            if (this.rolEsperado.equalsIgnoreCase("Administrador") && (rolEncontradoEnBD.equals("ADMIN") || rolEncontradoEnBD.equals("ADMINISTRADOR"))) {
                rolVálido = true;
            } else if (this.rolEsperado.equalsIgnoreCase("Trabajador") && (rolEncontradoEnBD.equals("TRABAJADOR") || rolEncontradoEnBD.equals("USER") || rolEncontradoEnBD.equals("VENDEDOR"))) {
                rolVálido = true;
            } else if (rolEncontradoEnBD.equalsIgnoreCase(this.rolEsperado)) {
                rolVálido = true;
            }

            if (!rolVálido) {
                GestorDeSesión.cerrarSesión();
                JOptionPane.showMessageDialog(this, "Error de acceso:\nTu cuenta es de '" + datosIngreso[0] + "', pero seleccionaste '" + this.rolEsperado + "'.", "Perfil Incorrecto", JOptionPane.ERROR_MESSAGE);
                return;
            }

            this.dispose();
            new VentanaBienvenida(datosIngreso[1], datosIngreso[0]).setVisible(true);
        } else {
            intentosFallidos++;
            if (intentosFallidos >= 3) System.exit(0);
            else JOptionPane.showMessageDialog(this, "Credenciales incorrectas. Intentos restantes: " + (3 - intentosFallidos));
        }
    }
}