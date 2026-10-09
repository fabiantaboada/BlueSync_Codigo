package gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class VentanaSeleccionRol extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private JPanel panelDeContención;
    private JComboBox<String> comboRoles;
    private JButton botónContinuar;

    public VentanaSeleccionRol() {
        setTitle("BlueSync - Selección de Perfil");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 420); 
        setResizable(false);
        setLocationRelativeTo(null);
        
        panelDeContención = new JPanel();
        panelDeContención.setBackground(Color.WHITE);
        panelDeContención.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(panelDeContención);
        panelDeContención.setLayout(null);
        
        JLabel etiquetaLogo = new JLabel("");
        etiquetaLogo.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            ImageIcon iconoBlue = new ImageIcon(VentanaSeleccionRol.class.getResource("/imágenes/logobl.png"));
            Image imgBlue = iconoBlue.getImage().getScaledInstance(160, 90, Image.SCALE_SMOOTH);
            etiquetaLogo.setIcon(new ImageIcon(imgBlue));
        } catch(Exception e) {
            etiquetaLogo.setText("BLUESYNC");
            etiquetaLogo.setFont(new Font("Segoe UI", Font.BOLD, 22));
            etiquetaLogo.setForeground(new Color(26, 86, 219));
        }
        etiquetaLogo.setBounds(145, 20, 160, 90);
        panelDeContención.add(etiquetaLogo);
        
        JLabel etiquetaTítulo = new JLabel("Bienvenido a BlueSync Workspace");
        etiquetaTítulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        etiquetaTítulo.setForeground(new Color(31, 41, 55));
        etiquetaTítulo.setHorizontalAlignment(SwingConstants.CENTER);
        etiquetaTítulo.setBounds(10, 120, 414, 30);
        panelDeContención.add(etiquetaTítulo);
        
        JLabel etiquetaSubtítulo = new JLabel("Por favor, selecciona tu perfil de acceso para continuar");
        etiquetaSubtítulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        etiquetaSubtítulo.setForeground(new Color(107, 114, 128));
        etiquetaSubtítulo.setHorizontalAlignment(SwingConstants.CENTER);
        etiquetaSubtítulo.setBounds(10, 150, 414, 20);
        panelDeContención.add(etiquetaSubtítulo);
        
        // --- COMBOBOX---
        JLabel etiquetaCombo = new JLabel("Perfil:");
        etiquetaCombo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        etiquetaCombo.setForeground(new Color(75, 85, 99));
        etiquetaCombo.setBounds(65, 200, 300, 20);
        panelDeContención.add(etiquetaCombo);
        
        comboRoles = new JComboBox<>(new String[]{"Administrador", "Trabajador"});
        comboRoles.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        comboRoles.setBackground(new Color(249, 250, 251));
        comboRoles.setBounds(65, 225, 300, 40);
        panelDeContención.add(comboRoles);
        
        // --- BOTÓN DE CONTINUAR ---
        botónContinuar = new JButton("Continuar");
        botónContinuar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botónContinuar.setBackground(new Color(26, 86, 219)); 
        botónContinuar.setForeground(Color.WHITE);
        botónContinuar.setFocusPainted(false);
        botónContinuar.setBorderPainted(false);
        botónContinuar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botónContinuar.setBounds(65, 290, 300, 45);
        botónContinuar.addActionListener(this);
        panelDeContención.add(botónContinuar);
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botónContinuar) {
            String rolSeleccionado = comboRoles.getSelectedItem().toString();
            // Lanza el login enviándole el rol que se eligió en el ComboBox
            new VentanaLogin(rolSeleccionado).setVisible(true);
            this.dispose();
        }
    }
}