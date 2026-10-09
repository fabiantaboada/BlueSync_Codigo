package gui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import persistencia.UsuarioDAO;

public class VentanaConfiguraciónAdmin extends JDialog implements ActionListener {

    private static final long serialVersionUID = 1L;
    private JPanel panelPrincipal;
    private JPanel panelDerecho;
    private CardLayout diseñoPestañas;
    private JList<String> listaCategorías;

    private JTextField campoCrearNombreCompleto;
    private JTextField campoCrearUsuario;
    private JPasswordField campoCrearClave;
    private JComboBox<String> menúCrearRol;
    private JButton botónGuardarNuevaCuenta;

    private JTextField campoModificarUsuarioBuscar;
    private JButton botónBuscarUsuario;
    private JTextField campoModificarNuevoUsuario;
    private JTextField campoModificarNombreCompleto;
    private JPasswordField campoModificarNuevaClave;
    private JComboBox<String> menúModificarNuevoRol;
    private JButton botónActualizarCuenta;

    private JTextField campoEliminarUsuario;
    private JButton botónEliminarCuenta;

    private JButton botónAceptar;

    public VentanaConfiguraciónAdmin(JFrame menúPadre) {
        setTitle("BlueSync - Gestión de Cuentas de Usuario");
        setModal(true);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 680, 460); // Ventana más amplia
        setResizable(false);
        setLocationRelativeTo(menúPadre);

        panelPrincipal = new JPanel();
        panelPrincipal.setBackground(Color.WHITE);
        setContentPane(panelPrincipal);
        panelPrincipal.setLayout(null);
        
        JLabel etiquetaTítulo = new JLabel("Administración de Accesos al Sistema");
        etiquetaTítulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        etiquetaTítulo.setForeground(new Color(26, 86, 219));
        etiquetaTítulo.setHorizontalAlignment(SwingConstants.CENTER);
        etiquetaTítulo.setBounds(10, 15, 644, 30);
        panelPrincipal.add(etiquetaTítulo);
        
        JSeparator separador = new JSeparator();
        separador.setBounds(30, 55, 600, 2);
        separador.setForeground(new Color(229, 231, 235));
        panelPrincipal.add(separador);

        // --- MENÚ LATERAL IZQUIERDO ---
        String[] opcionesMenú = { "Crear cuenta", "Modificar cuenta", "Eliminar cuenta" };
        listaCategorías = new JList<>(opcionesMenú);
        listaCategorías.setFont(new Font("Segoe UI", Font.BOLD, 13));
        listaCategorías.setFixedCellHeight(35);
        listaCategorías.setSelectionBackground(new Color(219, 234, 254));
        listaCategorías.setSelectionForeground(Color.BLACK);
        listaCategorías.setSelectedIndex(0);
        listaCategorías.setBounds(30, 75, 160, 280);
        listaCategorías.setBorder(BorderFactory.createLineBorder(new Color(209, 213, 219)));
        panelPrincipal.add(listaCategorías);

        // --- PANEL DERECHO CON PESTAÑAS (CARDLAYOUT) ---
        diseñoPestañas = new CardLayout();
        panelDerecho = new JPanel(diseñoPestañas);
        panelDerecho.setBackground(Color.WHITE);
        panelDerecho.setBounds(210, 70, 420, 290);
        panelDerecho.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219)), "Formulario de Acción", TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12), new Color(107, 114, 128)
        ));
        panelPrincipal.add(panelDerecho);

        // --- 1. PESTAÑA CREAR CUENTA ---
        JPanel panelCrear = new JPanel(null);
        panelCrear.setBackground(Color.WHITE);

        JLabel etiquetaCrearNombre = new JLabel("Nombres y Apellidos:");
        etiquetaCrearNombre.setFont(new Font("Segoe UI", Font.BOLD, 12));
        etiquetaCrearNombre.setBounds(20, 30, 130, 20);
        panelCrear.add(etiquetaCrearNombre);
        campoCrearNombreCompleto = crearCampoTexto();
        campoCrearNombreCompleto.setBounds(160, 26, 230, 28);
        panelCrear.add(campoCrearNombreCompleto);

        JLabel etiquetaCrearUsuario = new JLabel("Nombre de Usuario:");
        etiquetaCrearUsuario.setFont(new Font("Segoe UI", Font.BOLD, 12));
        etiquetaCrearUsuario.setBounds(20, 70, 130, 20);
        panelCrear.add(etiquetaCrearUsuario);
        campoCrearUsuario = crearCampoTexto();
        campoCrearUsuario.setBounds(160, 66, 230, 28);
        panelCrear.add(campoCrearUsuario);

        JLabel etiquetaCrearClave = new JLabel("Contraseña:");
        etiquetaCrearClave.setFont(new Font("Segoe UI", Font.BOLD, 12));
        etiquetaCrearClave.setBounds(20, 110, 130, 20);
        panelCrear.add(etiquetaCrearClave);
        campoCrearClave = crearCampoContraseña();
        campoCrearClave.setBounds(160, 106, 230, 28);
        panelCrear.add(campoCrearClave);

        JLabel etiquetaCrearRol = new JLabel("Nivel de Acceso:");
        etiquetaCrearRol.setFont(new Font("Segoe UI", Font.BOLD, 12));
        etiquetaCrearRol.setBounds(20, 150, 130, 20);
        panelCrear.add(etiquetaCrearRol);
        menúCrearRol = new JComboBox<>(new String[] { "Administrador", "Trabajador" });
        menúCrearRol.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        menúCrearRol.setBounds(160, 146, 230, 28);
        panelCrear.add(menúCrearRol);

        botónGuardarNuevaCuenta = crearBotón("Guardar Cuenta", new Color(16, 185, 129)); // Verde
        botónGuardarNuevaCuenta.setBounds(160, 210, 230, 35);
        panelCrear.add(botónGuardarNuevaCuenta);

        // --- 2. PESTAÑA MODIFICAR CUENTA ---
        JPanel panelModificar = new JPanel(null);
        panelModificar.setBackground(Color.WHITE);

        JLabel etiquetaModificarInfo = new JLabel("Usuario a modificar:");
        etiquetaModificarInfo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        etiquetaModificarInfo.setBounds(20, 20, 130, 20);
        panelModificar.add(etiquetaModificarInfo);
        campoModificarUsuarioBuscar = crearCampoTexto();
        campoModificarUsuarioBuscar.setBounds(160, 16, 130, 28);
        panelModificar.add(campoModificarUsuarioBuscar);

        botónBuscarUsuario = crearBotón("Buscar", new Color(107, 114, 128)); // Gris
        botónBuscarUsuario.setBounds(300, 16, 90, 28);
        panelModificar.add(botónBuscarUsuario);

        JSeparator separadorModificar = new JSeparator();
        separadorModificar.setBounds(20, 55, 370, 2);
        separadorModificar.setForeground(new Color(229, 231, 235));
        panelModificar.add(separadorModificar);

        JLabel etiquetaModificarUsuario = new JLabel("Usuario:");
        etiquetaModificarUsuario.setFont(new Font("Segoe UI", Font.BOLD, 12));
        etiquetaModificarUsuario.setBounds(20, 70, 130, 20);
        panelModificar.add(etiquetaModificarUsuario);
        campoModificarNuevoUsuario = crearCampoTexto();
        campoModificarNuevoUsuario.setBounds(160, 66, 230, 28);
        campoModificarNuevoUsuario.setEnabled(false);
        panelModificar.add(campoModificarNuevoUsuario);

        JLabel etiquetaModificarNombre = new JLabel("Nombres y Apellidos:");
        etiquetaModificarNombre.setFont(new Font("Segoe UI", Font.BOLD, 12));
        etiquetaModificarNombre.setBounds(20, 110, 130, 20);
        panelModificar.add(etiquetaModificarNombre);
        campoModificarNombreCompleto = crearCampoTexto();
        campoModificarNombreCompleto.setBounds(160, 106, 230, 28);
        campoModificarNombreCompleto.setEnabled(false);
        panelModificar.add(campoModificarNombreCompleto);

        JLabel etiquetaModificarClave = new JLabel("Contraseña:");
        etiquetaModificarClave.setFont(new Font("Segoe UI", Font.BOLD, 12));
        etiquetaModificarClave.setBounds(20, 150, 130, 20);
        panelModificar.add(etiquetaModificarClave);
        campoModificarNuevaClave = crearCampoContraseña();
        campoModificarNuevaClave.setBounds(160, 146, 230, 28);
        campoModificarNuevaClave.setEnabled(false);
        panelModificar.add(campoModificarNuevaClave);

        JLabel etiquetaModificarRol = new JLabel("Nivel de Acceso:");
        etiquetaModificarRol.setFont(new Font("Segoe UI", Font.BOLD, 12));
        etiquetaModificarRol.setBounds(20, 190, 130, 20);
        panelModificar.add(etiquetaModificarRol);
        menúModificarNuevoRol = new JComboBox<>(new String[] { "Administrador", "Trabajador" });
        menúModificarNuevoRol.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        menúModificarNuevoRol.setBounds(160, 186, 230, 28);
        menúModificarNuevoRol.setEnabled(false);
        panelModificar.add(menúModificarNuevoRol);

        botónActualizarCuenta = crearBotón("Actualizar Datos", new Color(26, 86, 219)); // Azul
        botónActualizarCuenta.setBounds(160, 230, 230, 35);
        botónActualizarCuenta.setEnabled(false);
        panelModificar.add(botónActualizarCuenta);

        // --- 3. PESTAÑA ELIMINAR CUENTA ---
        JPanel panelEliminar = new JPanel(null);
        panelEliminar.setBackground(Color.WHITE);

        JLabel etiquetaEliminarTítulo = new JLabel("<html><center>Ingrese el nombre de usuario de la cuenta que desea eliminar permanentemente del sistema.</center></html>");
        etiquetaEliminarTítulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        etiquetaEliminarTítulo.setForeground(new Color(107, 114, 128));
        etiquetaEliminarTítulo.setBounds(40, 30, 340, 40);
        panelEliminar.add(etiquetaEliminarTítulo);

        JLabel etiquetaEliminarUsuario = new JLabel("Nombre de Usuario:");
        etiquetaEliminarUsuario.setFont(new Font("Segoe UI", Font.BOLD, 12));
        etiquetaEliminarUsuario.setBounds(40, 95, 130, 20);
        panelEliminar.add(etiquetaEliminarUsuario);
        campoEliminarUsuario = crearCampoTexto();
        campoEliminarUsuario.setBounds(170, 91, 210, 28);
        panelEliminar.add(campoEliminarUsuario);

        botónEliminarCuenta = crearBotón("Eliminar Cuenta", new Color(239, 68, 68)); // Rojo
        botónEliminarCuenta.setBounds(170, 140, 210, 35);
        panelEliminar.add(botónEliminarCuenta);

        // Añadir pestañas al CardLayout
        panelDerecho.add(panelCrear, "Crear cuenta");
        panelDerecho.add(panelModificar, "Modificar cuenta");
        panelDerecho.add(panelEliminar, "Eliminar cuenta");

        listaCategorías.addListSelectionListener(evento -> {
            String selección = listaCategorías.getSelectedValue();
            diseñoPestañas.show(panelDerecho, selección);
        });

        // --- BOTÓN CERRAR VENTANA ---
        botónAceptar = crearBotón("Cerrar Configuración", new Color(107, 114, 128));
        botónAceptar.setBounds(255, 380, 180, 35);
        panelPrincipal.add(botónAceptar);
    }

    // --- MÉTODOS AUXILIARES DE DISEÑO ---
    private JTextField crearCampoTexto() {
        JTextField campo = new JTextField();
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campo.setBackground(new Color(249, 250, 251));
        campo.setForeground(Color.BLACK); // Evita problemas de contraste
        campo.setDisabledTextColor(Color.BLACK); // Texto negro cuando está inactivo
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219)),
            BorderFactory.createEmptyBorder(0, 8, 0, 8)
        ));
        return campo;
    }

    private JPasswordField crearCampoContraseña() {
        JPasswordField campo = new JPasswordField();
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campo.setBackground(new Color(249, 250, 251));
        campo.setForeground(Color.BLACK);
        campo.setDisabledTextColor(Color.BLACK);
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219)),
            BorderFactory.createEmptyBorder(0, 8, 0, 8)
        ));
        return campo;
    }

    private JButton crearBotón(String texto, Color colorFondo) {
        JButton botón = new JButton(texto);
        botón.setFont(new Font("Segoe UI", Font.BOLD, 13));
        botón.setBackground(colorFondo);
        botón.setForeground(Color.WHITE);
        botón.setFocusPainted(false);
        botón.setBorderPainted(false);
        botón.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botón.addActionListener(this);
        return botón;
    }

    public void actionPerformed(ActionEvent evento) {
        if (evento.getSource() == botónAceptar) {
            this.dispose();
        } else if (evento.getSource() == botónGuardarNuevaCuenta) {
            procesarCreación();
        } else if (evento.getSource() == botónBuscarUsuario) {
            buscarUsuarioAModificar();
        } else if (evento.getSource() == botónActualizarCuenta) {
            procesarModificación();
        } else if (evento.getSource() == botónEliminarCuenta) {
            procesarEliminación();
        }
    }

    private boolean esContraseñaFuerte(String clave) {
        return clave.matches("^(?=.*[A-Z])(?=.*\\d).{8,}$");
    }

    private void procesarCreación() {
        String nombre = campoCrearNombreCompleto.getText().trim();
        String usuario = campoCrearUsuario.getText().trim();
        String clave = new String(campoCrearClave.getPassword()).trim();
        String rol = menúCrearRol.getSelectedItem().toString();

        if (nombre.isEmpty() || usuario.isEmpty() || clave.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos solicitados.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) {
            JOptionPane.showMessageDialog(this, "Los nombres y apellidos solo deben contener letras.", "Datos inválidos", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!esContraseñaFuerte(clave)) {
            JOptionPane.showMessageDialog(this, "La contraseña debe tener mínimo 8 caracteres, al menos 1 mayúscula y 1 número.", "Contraseña Débil", JOptionPane.ERROR_MESSAGE);
            return;
        }

        UsuarioDAO dao = new UsuarioDAO();
        if (dao.existeUsuario(usuario)) {
            JOptionPane.showMessageDialog(this, "El nombre de usuario ya existe en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (dao.registrarUsuario(usuario, clave, rol, nombre)) {
            JOptionPane.showMessageDialog(this, "Cuenta creada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            campoCrearNombreCompleto.setText("");
            campoCrearUsuario.setText("");
            campoCrearClave.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarUsuarioAModificar() {
        String usuarioBuscado = campoModificarUsuarioBuscar.getText().trim();
        if (usuarioBuscado.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un usuario para buscar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        UsuarioDAO dao = new UsuarioDAO();
        String[] datos = dao.obtenerDatosDeUsuario(usuarioBuscado);

        if (datos != null) {
            campoModificarNuevoUsuario.setText(datos[0]);
            campoModificarNuevaClave.setText(datos[1]);
            menúModificarNuevoRol.setSelectedItem(datos[2]);
            campoModificarNombreCompleto.setText(datos[3]);

            campoModificarNuevoUsuario.setEnabled(true);
            campoModificarNombreCompleto.setEnabled(true);
            campoModificarNuevaClave.setEnabled(true);
            menúModificarNuevoRol.setEnabled(true);
            botónActualizarCuenta.setEnabled(true);

            JOptionPane.showMessageDialog(this, "Usuario encontrado en MySQL.", "Búsqueda Exitosa", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "El usuario no existe o está mal escrito.", "No encontrado", JOptionPane.ERROR_MESSAGE);
            campoModificarNuevoUsuario.setEnabled(false);
            campoModificarNombreCompleto.setEnabled(false);
            campoModificarNuevaClave.setEnabled(false);
            menúModificarNuevoRol.setEnabled(false);
            botónActualizarCuenta.setEnabled(false);
            campoModificarNuevoUsuario.setText("");
            campoModificarNombreCompleto.setText("");
            campoModificarNuevaClave.setText("");
        }
    }

    private void procesarModificación() {
        String usuarioBuscado = campoModificarUsuarioBuscar.getText().trim();
        String nuevoUsuario = campoModificarNuevoUsuario.getText().trim();
        String nuevoNombre = campoModificarNombreCompleto.getText().trim();
        String nuevaClave = new String(campoModificarNuevaClave.getPassword()).trim();
        String nuevoRol = menúModificarNuevoRol.getSelectedItem().toString();

        if (nuevoUsuario.isEmpty() || nuevoNombre.isEmpty() || nuevaClave.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No deje campos vacíos.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!nuevoNombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) {
            JOptionPane.showMessageDialog(this, "Los nombres y apellidos solo deben contener letras.", "Datos inválidos", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!esContraseñaFuerte(nuevaClave)) {
            JOptionPane.showMessageDialog(this, "La contraseña debe tener mínimo 8 caracteres, al menos 1 mayúscula y 1 número.", "Contraseña Débil", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (usuarioBuscado.equalsIgnoreCase("admin.gerencia") && !nuevoUsuario.equalsIgnoreCase("admin.gerencia")) {
            JOptionPane.showMessageDialog(this, "Por seguridad, la cuenta 'admin.gerencia' no puede cambiar de nombre de usuario.", "Acción Bloqueada", JOptionPane.ERROR_MESSAGE);
            return;
        }

        UsuarioDAO dao = new UsuarioDAO();
        if (!nuevoUsuario.equalsIgnoreCase(usuarioBuscado) && dao.existeUsuario(nuevoUsuario)) {
            JOptionPane.showMessageDialog(this, "El nuevo nombre de usuario ya está ocupado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirmación = JOptionPane.showConfirmDialog(this, "¿Desea actualizar los datos en MySQL?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmación != JOptionPane.YES_OPTION) return;

        if (dao.actualizarUsuario(usuarioBuscado, nuevoUsuario, nuevoNombre, nuevaClave, nuevoRol)) {
            JOptionPane.showMessageDialog(this, "Datos actualizados correctamente en MySQL.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            campoModificarUsuarioBuscar.setText("");
            campoModificarNuevoUsuario.setText("");
            campoModificarNombreCompleto.setText("");
            campoModificarNuevaClave.setText("");
            campoModificarNuevoUsuario.setEnabled(false);
            campoModificarNombreCompleto.setEnabled(false);
            campoModificarNuevaClave.setEnabled(false);
            menúModificarNuevoRol.setEnabled(false);
            botónActualizarCuenta.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo actualizar el registro en MySQL.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void procesarEliminación() {
        String usuarioAEliminar = campoEliminarUsuario.getText().trim();
        if (usuarioAEliminar.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el nombre de usuario a eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (usuarioAEliminar.equalsIgnoreCase("admin.gerencia")) {
            JOptionPane.showMessageDialog(this, "La cuenta principal 'admin.gerencia' no puede ser eliminada.", "Acción Bloqueada", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirmación = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea eliminar a '" + usuarioAEliminar + "' de MySQL?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirmación != JOptionPane.YES_OPTION) return;

        UsuarioDAO dao = new UsuarioDAO();
        if (dao.eliminarUsuario(usuarioAEliminar)) {
            JOptionPane.showMessageDialog(this, "Cuenta eliminada permanentemente de MySQL.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            campoEliminarUsuario.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró el usuario en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}