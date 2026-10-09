package gui;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileWriter;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import persistencia.ComprobanteDAO;

public class VentanaVerRecibo extends JDialog {
    private static final long serialVersionUID = 1L;
    private JTextArea áreaRecibo;
    private String contenidoActual, númeroComp;
    private VentanaHistorialVentas padre;

    public VentanaVerRecibo(String númeroComprobante, String contenidoRecibo, VentanaHistorialVentas padre) {
        this.númeroComp = númeroComprobante;
        this.contenidoActual = contenidoRecibo;
        this.padre = padre;

        setTitle("Comprobante Emitido: " + númeroComprobante);
        setModal(true); setSize(520, 520); setLocationRelativeTo(padre); setResizable(false);
        getContentPane().setBackground(Color.WHITE); setLayout(null);

        áreaRecibo = new JTextArea(contenidoActual);
        áreaRecibo.setFont(new Font("Monospaced", Font.PLAIN, 12));
        áreaRecibo.setEditable(false);

        JScrollPane scroll = new JScrollPane(áreaRecibo);
        scroll.setBounds(20, 20, 464, 380); scroll.setBorder(BorderFactory.createLineBorder(new Color(209, 213, 219)));
        add(scroll);

        JButton btnEditar = new JButton("Editar Cliente");
        btnEditar.setBackground(new Color(243, 244, 246)); btnEditar.setForeground(new Color(55, 65, 81));
        btnEditar.setBounds(20, 420, 130, 35);
        btnEditar.addActionListener(e -> abrirDiálogoEdición());
        add(btnEditar);

        JButton btnTXT = new JButton("Descargar TXT");
        btnTXT.setBackground(new Color(16, 185, 129)); btnTXT.setForeground(Color.WHITE);
        btnTXT.setBounds(165, 420, 150, 35);
        btnTXT.addActionListener(e -> {
            JFileChooser sel = new JFileChooser(); 
            sel.setFileFilter(new FileNameExtensionFilter("Archivo de Texto (*.txt)", "txt"));
            sel.setSelectedFile(new File("Comprobante_" + númeroComp + ".txt"));
            
            if (sel.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File destino = sel.getSelectedFile();
                if (!destino.getName().toLowerCase().endsWith(".txt")) {
                    destino = new File(destino.getAbsolutePath() + ".txt");
                }
                try (FileWriter escritor = new FileWriter(destino)) {
                    escritor.write(contenidoActual);
                    JOptionPane.showMessageDialog(this, "Guardado correctamente en .txt"); 
                } catch (Exception ex) { 
                    JOptionPane.showMessageDialog(this, "Error al descargar."); 
                }
            }
        });
        add(btnTXT);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setBackground(new Color(107, 114, 128)); btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setBounds(330, 420, 150, 35);
        btnCerrar.addActionListener(e -> dispose());
        add(btnCerrar);
    }

    private void abrirDiálogoEdición() {
        final String nomAntiguo = extraerDato("CLIENTE: ");
        final String corAntiguo = extraerDato("CORREO: ");
        final String telAntiguo = extraerDato("TELÉFONO: ");
        final String docAntiguo = extraerDato("DNI: ").isEmpty() ? extraerDato("RUC: ") : extraerDato("DNI: ");

        JDialog d = new JDialog(this, "Editar Datos", true);
        d.setSize(330, 300); d.setLocationRelativeTo(this); d.setLayout(null); d.getContentPane().setBackground(Color.WHITE);

        JLabel l1 = new JLabel("Documento:"); l1.setBounds(20, 20, 80, 25); d.add(l1);
        JTextField txtDoc = new JTextField(docAntiguo); txtDoc.setBounds(100, 20, 190, 25); d.add(txtDoc);

        JLabel l2 = new JLabel("Nombre:"); l2.setBounds(20, 60, 80, 25); d.add(l2);
        JTextField txtNom = new JTextField(nomAntiguo); txtNom.setBounds(100, 60, 190, 25); d.add(txtNom);

        JLabel l3 = new JLabel("Correo:"); l3.setBounds(20, 100, 80, 25); d.add(l3);
        JTextField txtCor = new JTextField(corAntiguo); txtCor.setBounds(100, 100, 190, 25); d.add(txtCor);

        JLabel l4 = new JLabel("Teléfono:"); l4.setBounds(20, 140, 80, 25); d.add(l4);
        JTextField txtTel = new JTextField(telAntiguo); txtTel.setBounds(100, 140, 190, 25); d.add(txtTel);

        JButton btnG = new JButton("Guardar Cambios"); btnG.setBounds(65, 200, 200, 35); btnG.setBackground(new Color(26, 86, 219)); btnG.setForeground(Color.WHITE);
        btnG.addActionListener(ev -> {
            String nuevoDoc = txtDoc.getText().trim();
            String nuevoNom = txtNom.getText().trim().toUpperCase();
            String nuevoCor = txtCor.getText().trim().toUpperCase();
            String nuevoTel = txtTel.getText().trim();

            if (nuevoNom.isEmpty() || nuevoDoc.isEmpty() || nuevoCor.isEmpty() || nuevoTel.isEmpty()) { JOptionPane.showMessageDialog(d, "Complete todos los campos."); return; }
            if (!nuevoDoc.matches("^\\d{8}$") && !nuevoDoc.matches("^(10|15|17|20)\\d{9}$")) { JOptionPane.showMessageDialog(d, "Documento inválido (DNI 8 dígitos o RUC 11 dígitos)."); return; }
            if (!nuevoNom.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) { JOptionPane.showMessageDialog(d, "Nombre inválido."); return; }
            if (!nuevoTel.matches("^\\d{9}$")) { JOptionPane.showMessageDialog(d, "Teléfono inválido (9 dígitos)."); return; }
            if (!nuevoCor.matches("^[A-Z0-9+_.-]+@[A-Z0-9.-]+$")) { JOptionPane.showMessageDialog(d, "Correo inválido."); return; }

            String nuevoRecibo = contenidoActual
                .replace("CLIENTE: " + nomAntiguo, "CLIENTE: " + nuevoNom)
                .replace("CORREO: " + corAntiguo, "CORREO: " + nuevoCor)
                .replace("TELÉFONO: " + telAntiguo, "TELÉFONO: " + nuevoTel)
                .replace("DNI: " + docAntiguo, "DNI: " + nuevoDoc)
                .replace("RUC: " + docAntiguo, "RUC: " + nuevoDoc);

            boolean éxito = new ComprobanteDAO().modificarComprobante(númeroComp, nuevoNom, nuevoCor, nuevoTel, nuevoDoc, nuevoRecibo);

            if (éxito) {
                contenidoActual = nuevoRecibo;
                áreaRecibo.setText(contenidoActual);
                if (padre != null) padre.cargarDatosDesdeBD();
                JOptionPane.showMessageDialog(d, "Datos actualizados correctamente en la Base de Datos.");
                d.dispose();
            } else {
                JOptionPane.showMessageDialog(d, "Error al actualizar la base de datos.");
            }
        });
        d.add(btnG); d.setVisible(true);
    }

    private String extraerDato(String prefijo) {
        String[] líneas = contenidoActual.split("\n");
        for (String línea : líneas) {
            if (línea.startsWith(prefijo)) return línea.substring(prefijo.length()).trim();
        }
        return "";
    }
}