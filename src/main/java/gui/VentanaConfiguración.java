package gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import modelo.Equipo;
import persistencia.CatálogosDAO;

public class VentanaConfiguración extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private JPanel panelDeContención;
    
    private JLabel etiquetaMódulo;
    private JComboBox<String> menúMódulo;
    
    private JList<String> listaCatálogo;
    private DefaultListModel<String> modeloLista;
    private JScrollPane panelDesplazamiento;
    
    private JLabel etiquetaNombreNuevo;
    private JTextField campoNombre;
    private JLabel etiquetaPrecioNuevo;
    private JTextField campoPrecio;
    private JLabel etiquetaStockNuevo;
    private JTextField campoStock;
    
    private JButton botónAgregar;
    private JButton botónActualizar;
    private JButton botónEliminar;
    private JButton botónRegresar;
    
    private CatálogosDAO catálogosDAO;

    public VentanaConfiguración() {
        catálogosDAO = new CatálogosDAO();
        
        setTitle("BlueSync - Configuración de Catálogos");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 750, 560); 
        setResizable(false);
        setLocationRelativeTo(null);
        
        panelDeContención = new JPanel();
        panelDeContención.setBackground(Color.WHITE);
        panelDeContención.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(panelDeContención);
        panelDeContención.setLayout(null);
        
        JLabel etiquetaTítulo = new JLabel("Gestor Dinámico de Precios y Servicios");
        etiquetaTítulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        etiquetaTítulo.setForeground(new Color(26, 86, 219));
        etiquetaTítulo.setHorizontalAlignment(SwingConstants.CENTER);
        etiquetaTítulo.setBounds(10, 15, 714, 30);
        panelDeContención.add(etiquetaTítulo);
        
        JSeparator separador = new JSeparator();
        separador.setBounds(30, 55, 680, 2);
        separador.setForeground(new Color(229, 231, 235));
        panelDeContención.add(separador);
        
        // --- COLUMNA IZQUIERDA: SELECCIÓN Y LISTA ---
        etiquetaMódulo = new JLabel("Seleccione el Módulo a configurar:");
        etiquetaMódulo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        etiquetaMódulo.setForeground(Color.BLACK); // Máximo contraste
        etiquetaMódulo.setBounds(30, 70, 250, 20);
        panelDeContención.add(etiquetaMódulo);
        
        menúMódulo = new JComboBox<>();
        menúMódulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        menúMódulo.setForeground(Color.BLACK);
        menúMódulo.addItem("1. Construcción de piscinas");
        menúMódulo.addItem("2. Mantenimiento de piscinas");
        menúMódulo.addItem("3. Suministro de materiales");
        menúMódulo.setBounds(30, 95, 290, 30);
        panelDeContención.add(menúMódulo);
        
        menúMódulo.addActionListener(evento -> {
            actualizarEtiquetasVisuales();
            cargarDatosDesdeMySQL();
        });
        
        modeloLista = new DefaultListModel<>();
        listaCatálogo = new JList<>(modeloLista);
        listaCatálogo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        listaCatálogo.setForeground(Color.BLACK); // Máximo contraste para la lista
        listaCatálogo.setFixedCellHeight(25);
        listaCatálogo.setSelectionBackground(new Color(219, 234, 254));
        listaCatálogo.setSelectionForeground(Color.BLACK);
        
        panelDesplazamiento = new JScrollPane(listaCatálogo);
        panelDesplazamiento.setBounds(30, 135, 290, 360);
        panelDesplazamiento.setBorder(BorderFactory.createLineBorder(new Color(209, 213, 219)));
        panelDeContención.add(panelDesplazamiento);
        
        listaCatálogo.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent evento) {
                if (!evento.getValueIsAdjusting() && listaCatálogo.getSelectedIndex() != -1) {
                    String seleccionado = listaCatálogo.getSelectedValue();
                    String[] partes = seleccionado.split(" : ");
                    if (partes.length >= 2) {
                        campoNombre.setText(partes[0].trim());
                        campoPrecio.setText(partes[1].trim());
                    }
                    if (partes.length == 3) {
                        campoStock.setText(partes[2].trim());
                    } else {
                        campoStock.setText("");
                    }
                }
            }
        });
        
        // --- COLUMNA DERECHA: FORMULARIO DE EDICIÓN ---
        JPanel panelFormulario = new JPanel(null);
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219)), "Formulario de Datos", TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12), Color.BLACK
        ));
        panelFormulario.setBounds(350, 70, 350, 425);
        panelDeContención.add(panelFormulario);
        
        etiquetaNombreNuevo = new JLabel("Nombre del Acabado:");
        etiquetaNombreNuevo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        etiquetaNombreNuevo.setForeground(Color.BLACK);
        etiquetaNombreNuevo.setBounds(20, 30, 300, 20);
        panelFormulario.add(etiquetaNombreNuevo);
        
        campoNombre = crearCampoTexto();
        campoNombre.setBounds(20, 55, 310, 30);
        panelFormulario.add(campoNombre);
        
        etiquetaPrecioNuevo = new JLabel("Precio (S/):");
        etiquetaPrecioNuevo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        etiquetaPrecioNuevo.setForeground(Color.BLACK);
        etiquetaPrecioNuevo.setBounds(20, 95, 140, 20);
        panelFormulario.add(etiquetaPrecioNuevo);
        
        campoPrecio = crearCampoTexto();
        campoPrecio.setBounds(20, 120, 140, 30);
        panelFormulario.add(campoPrecio);
        
        etiquetaStockNuevo = new JLabel("Stock:");
        etiquetaStockNuevo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        etiquetaStockNuevo.setForeground(Color.BLACK);
        etiquetaStockNuevo.setBounds(180, 95, 140, 20);
        panelFormulario.add(etiquetaStockNuevo);
        
        campoStock = crearCampoTexto();
        campoStock.setBounds(180, 120, 150, 30);
        panelFormulario.add(campoStock);
        
        botónAgregar = crearBotón("Agregar como NUEVO", new Color(16, 185, 129)); 
        botónAgregar.setBounds(20, 180, 310, 40);
        panelFormulario.add(botónAgregar);
        
        botónActualizar = crearBotón("Actualizar SELECCIONADO", new Color(26, 86, 219)); 
        botónActualizar.setBounds(20, 230, 310, 40);
        panelFormulario.add(botónActualizar);
        
        botónEliminar = crearBotón("Eliminar de la lista", new Color(239, 68, 68)); 
        botónEliminar.setBounds(20, 280, 310, 40);
        panelFormulario.add(botónEliminar);
        
        JSeparator sepBotones = new JSeparator();
        sepBotones.setBounds(20, 335, 310, 2);
        sepBotones.setForeground(new Color(229, 231, 235));
        panelFormulario.add(sepBotones);
        
        botónRegresar = crearBotón("Regresar al Panel", new Color(107, 114, 128)); 
        botónRegresar.setBounds(20, 355, 310, 40);
        panelFormulario.add(botónRegresar);
        
        actualizarEtiquetasVisuales();
        cargarDatosDesdeMySQL();
    }
    
    // Método auxiliar con protección anti-texto invisible
    private JTextField crearCampoTexto() {
        JTextField campo = new JTextField();
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campo.setBackground(new Color(249, 250, 251));
        campo.setForeground(Color.BLACK); // Letra negra siempre
        campo.setDisabledTextColor(Color.BLACK); // Evita que se ponga gris claro al bloquearse
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
        if (evento.getSource() == botónAgregar) {
            procesarAgregar();
        } else if (evento.getSource() == botónActualizar) {
            procesarActualizar();
        } else if (evento.getSource() == botónEliminar) {
            procesarEliminar();
        } else if (evento.getSource() == botónRegresar) {
            new MenúPrincipal().setVisible(true);
            this.dispose();
        }
    }
    
    private void actualizarEtiquetasVisuales() {
        int índice = menúMódulo.getSelectedIndex();
        campoNombre.setText("");
        campoPrecio.setText("");
        campoStock.setText("");
        listaCatálogo.clearSelection();
        
        if (índice == 0) { 
            etiquetaNombreNuevo.setText("Nombre del Acabado:");
            etiquetaPrecioNuevo.setText("Precio x m² (S/):");
            campoNombre.setEnabled(true);
            botónAgregar.setEnabled(true);
            botónEliminar.setEnabled(true);
            etiquetaStockNuevo.setVisible(false);
            campoStock.setVisible(false);
        } else if (índice == 1) { 
            etiquetaNombreNuevo.setText("Ítem Fijo (Solo actualizar precio):");
            etiquetaPrecioNuevo.setText("Precio actual (S/):");
            campoNombre.setEnabled(false); // Aquí era donde se perdía el texto, pero el setDisabledTextColor(Color.BLACK) ya lo solucionó.
            botónAgregar.setEnabled(false);
            botónEliminar.setEnabled(false);
            etiquetaStockNuevo.setVisible(false);
            campoStock.setVisible(false);
        } else if (índice == 2) { 
            etiquetaNombreNuevo.setText("Nombre del Material/Equipo:");
            etiquetaPrecioNuevo.setText("Precio Unit (S/):");
            campoNombre.setEnabled(true);
            botónAgregar.setEnabled(true);
            botónEliminar.setEnabled(true);
            etiquetaStockNuevo.setVisible(true);
            campoStock.setVisible(true);
        }
    }
    
    private void cargarDatosDesdeMySQL() {
        modeloLista.clear();
        int índice = menúMódulo.getSelectedIndex();
        
        if (índice == 0) {
            List<Object[]> datos = catálogosDAO.obtenerCatálogoConstrucción();
            for (Object[] fila : datos) {
                modeloLista.addElement(fila[0] + " : " + fila[1]);
            }
        } else if (índice == 1) {
            double[] tarifas = catálogosDAO.obtenerTarifasMantenimiento();
            modeloLista.addElement("Tarifa por Hora : " + tarifas[0]);
            modeloLista.addElement("Kit de Químicos : " + tarifas[1]);
        } else if (índice == 2) {
            List<Equipo> inventario = catálogosDAO.obtenerInventarioDeSuministros();
            for (Equipo equipo : inventario) {
                modeloLista.addElement(equipo.obtenerNombre() + " : " + equipo.obtenerPrecioBase() + " : " + equipo.obtenerCantidadEnAlmacén());
            }
        }
    }
    
    private void procesarAgregar() {
        String nombre = campoNombre.getText().trim();
        String precioTexto = campoPrecio.getText().trim();
        String stockTexto = campoStock.getText().trim();
        int índiceMódulo = menúMódulo.getSelectedIndex();
        
        if (nombre.isEmpty() || precioTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar el nombre y el precio.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            double precio = Double.parseDouble(precioTexto);
            if (precio <= 0) throw new NumberFormatException();
            
            boolean éxito = false;
            if (índiceMódulo == 0) {
                éxito = catálogosDAO.agregarÍtemConstrucción(nombre, precio);
            } else if (índiceMódulo == 2) {
                if (stockTexto.isEmpty()) throw new NumberFormatException();
                int stock = Integer.parseInt(stockTexto);
                if (stock < 0) throw new NumberFormatException();
                éxito = catálogosDAO.agregarÍtemSuministro(nombre, precio, stock);
            }
            
            if (éxito) {
                JOptionPane.showMessageDialog(this, "Registro guardado exitosamente.");
                cargarDatosDesdeMySQL();
                campoNombre.setText(""); campoPrecio.setText(""); campoStock.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Error: El nombre ya existe en la base de datos.", "Error de SQL", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException excepción) {
            JOptionPane.showMessageDialog(this, "Verifique que el precio y el stock sean números válidos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void procesarActualizar() {
        int selección = listaCatálogo.getSelectedIndex();
        if (selección == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un ítem de la lista para actualizar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String nombreOriginal = modeloLista.getElementAt(selección).split(" : ")[0].trim();
        String nombreNuevo = campoNombre.getText().trim();
        String precioTexto = campoPrecio.getText().trim();
        String stockTexto = campoStock.getText().trim();
        int índiceMódulo = menúMódulo.getSelectedIndex();
        
        if (precioTexto.isEmpty() || (índiceMódulo != 1 && nombreNuevo.isEmpty())) {
            JOptionPane.showMessageDialog(this, "Faltan datos por llenar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            double precio = Double.parseDouble(precioTexto);
            if (precio <= 0) throw new NumberFormatException();
            
            boolean éxito = false;
            if (índiceMódulo == 0) {
                éxito = catálogosDAO.actualizarÍtemConstrucción(nombreOriginal, nombreNuevo, precio);
            } else if (índiceMódulo == 1) {
                String conceptoBúsqueda = nombreOriginal.contains("Hora") ? "Hora" : "Químicos";
                éxito = catálogosDAO.actualizarTarifaMantenimiento(conceptoBúsqueda, precio);
            } else if (índiceMódulo == 2) {
                int stock = Integer.parseInt(stockTexto);
                if (stock < 0) throw new NumberFormatException();
                éxito = catálogosDAO.actualizarÍtemSuministro(nombreOriginal, nombreNuevo, precio, stock);
            }
            
            if (éxito) {
                JOptionPane.showMessageDialog(this, "Registro actualizado correctamente.");
                cargarDatosDesdeMySQL();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar el registro.", "Error de SQL", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException excepción) {
            JOptionPane.showMessageDialog(this, "Valores numéricos inválidos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void procesarEliminar() {
        int selección = listaCatálogo.getSelectedIndex();
        if (selección == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un ítem de la lista para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String nombreOriginal = modeloLista.getElementAt(selección).split(" : ")[0].trim();
        int confirmación = JOptionPane.showConfirmDialog(this, "¿Eliminar '" + nombreOriginal + "' de la base de datos?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        
        if (confirmación == JOptionPane.YES_OPTION) {
            int índiceMódulo = menúMódulo.getSelectedIndex();
            boolean éxito = false;
            
            if (índiceMódulo == 0) {
                éxito = catálogosDAO.eliminarÍtemConstrucción(nombreOriginal);
            } else if (índiceMódulo == 2) {
                éxito = catálogosDAO.eliminarÍtemSuministro(nombreOriginal);
            }
            
            if (éxito) {
                JOptionPane.showMessageDialog(this, "Registro eliminado de la base de datos.");
                cargarDatosDesdeMySQL();
                campoNombre.setText(""); campoPrecio.setText(""); campoStock.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar. Verifique conexiones.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}