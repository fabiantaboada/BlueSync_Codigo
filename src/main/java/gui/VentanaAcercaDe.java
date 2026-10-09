package gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class VentanaAcercaDe extends JDialog implements ActionListener {

    private static final long serialVersionUID = 1L;
    private JPanel panelDeContención;
    private JButton botónAceptar;
    private JButton botónProgramadores;
    private JButton botónProveedores;

    public VentanaAcercaDe() {
        setTitle("Acerca de BlueSync Workspace");
        setModal(true);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 680, 260); // Ventana más ancha para 3 botones
        setResizable(false);
        setLocationRelativeTo(null);
        
        panelDeContención = new JPanel();
        panelDeContención.setBackground(Color.WHITE);
        panelDeContención.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(panelDeContención);
        panelDeContención.setLayout(null);
        
        JLabel etiquetaLogotipo = new JLabel("");
        etiquetaLogotipo.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            ImageIcon iconoBlue = new ImageIcon(VentanaAcercaDe.class.getResource("/imágenes/logobl.png"));
            Image imgBlue = iconoBlue.getImage().getScaledInstance(140, 80, Image.SCALE_SMOOTH);
            etiquetaLogotipo.setIcon(new ImageIcon(imgBlue));
        } catch(Exception excepción) {
            etiquetaLogotipo.setText("BLUESYNC");
            etiquetaLogotipo.setFont(new Font("Segoe UI", Font.BOLD, 18));
            etiquetaLogotipo.setForeground(new Color(26, 86, 219));
        }
        etiquetaLogotipo.setBounds(20, 30, 160, 100);
        panelDeContención.add(etiquetaLogotipo);
        
        JSeparator separadorVertical = new JSeparator(SwingConstants.VERTICAL);
        separadorVertical.setBounds(190, 30, 2, 100);
        separadorVertical.setForeground(new Color(229, 231, 235));
        panelDeContención.add(separadorVertical);
        
        JLabel etiquetaVersión = new JLabel("BlueSync Workspace 1.0 (64-bit)");
        etiquetaVersión.setFont(new Font("Segoe UI", Font.BOLD, 16));
        etiquetaVersión.setForeground(new Color(31, 41, 55));
        etiquetaVersión.setBounds(210, 30, 430, 20);
        panelDeContención.add(etiquetaVersión);
        
        JLabel etiquetaDerechos = new JLabel("Copyright © 2026. Todos los derechos reservados.");
        etiquetaDerechos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        etiquetaDerechos.setForeground(new Color(75, 85, 99));
        etiquetaDerechos.setBounds(210, 55, 430, 20);
        panelDeContención.add(etiquetaDerechos);
        
        JLabel etiquetaEmpresa = new JLabel("Desarrollado para: DH Proyectos S.A.C.");
        etiquetaEmpresa.setFont(new Font("Segoe UI", Font.BOLD, 12));
        etiquetaEmpresa.setForeground(new Color(26, 86, 219)); 
        etiquetaEmpresa.setBounds(210, 75, 430, 20);
        panelDeContención.add(etiquetaEmpresa);
        
        JSeparator separadorSuperior = new JSeparator();
        separadorSuperior.setBounds(210, 110, 430, 2);
        separadorSuperior.setForeground(new Color(229, 231, 235));
        panelDeContención.add(separadorSuperior);
        
        JLabel etiquetaLicencia = new JLabel("Copia licenciada de uso corporativo y comercial.");
        etiquetaLicencia.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        etiquetaLicencia.setForeground(new Color(107, 114, 128));
        etiquetaLicencia.setBounds(210, 115, 430, 20);
        panelDeContención.add(etiquetaLicencia);
        
        JSeparator separadorInferior = new JSeparator();
        separadorInferior.setBounds(210, 145, 430, 2);
        separadorInferior.setForeground(new Color(229, 231, 235));
        panelDeContención.add(separadorInferior);
        
        JLabel etiquetaTecnología = new JLabel("Motor: Java " + System.getProperty("java.version") + " ejecutándose sobre " + System.getProperty("os.name"));
        etiquetaTecnología.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        etiquetaTecnología.setForeground(new Color(156, 163, 175));
        etiquetaTecnología.setBounds(210, 155, 430, 20);
        panelDeContención.add(etiquetaTecnología);
        
        // --- BOTONES INFERIORES ---
        botónProgramadores = new JButton("Desarrolladores");
        botónProgramadores.setFont(new Font("Segoe UI", Font.BOLD, 12));
        botónProgramadores.setBackground(new Color(26, 86, 219)); // Azul
        botónProgramadores.setForeground(Color.WHITE);
        botónProgramadores.setFocusPainted(false);
        botónProgramadores.setBorderPainted(false);
        botónProgramadores.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botónProgramadores.addActionListener(this);
        botónProgramadores.setBounds(210, 185, 140, 30);
        panelDeContención.add(botónProgramadores);
        
        botónProveedores = new JButton("Proveedores");
        botónProveedores.setFont(new Font("Segoe UI", Font.BOLD, 12));
        botónProveedores.setBackground(new Color(16, 185, 129)); // Verde
        botónProveedores.setForeground(Color.WHITE);
        botónProveedores.setFocusPainted(false);
        botónProveedores.setBorderPainted(false);
        botónProveedores.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botónProveedores.addActionListener(this);
        botónProveedores.setBounds(360, 185, 140, 30);
        panelDeContención.add(botónProveedores);
        
        botónAceptar = new JButton("Aceptar");
        botónAceptar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        botónAceptar.setBackground(new Color(107, 114, 128)); // Gris
        botónAceptar.setForeground(Color.WHITE);
        botónAceptar.setFocusPainted(false);
        botónAceptar.setBorderPainted(false);
        botónAceptar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botónAceptar.addActionListener(this);
        botónAceptar.setBounds(510, 185, 130, 30);
        panelDeContención.add(botónAceptar);
    }

    public void actionPerformed(ActionEvent evento) {
        if (evento.getSource() == botónAceptar) {
            this.dispose();
        } else if (evento.getSource() == botónProgramadores) {
            new VentanaProgramadores().setVisible(true);
        } else if (evento.getSource() == botónProveedores) {
            new VentanaProveedores().setVisible(true);
        }
    }
}