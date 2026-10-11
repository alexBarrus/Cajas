package FrontEnd;

import BackEnd.Caja;
import BackEnd.ListaCajas;
import BackEnd.Nodo;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

/**
Barrutieta Nulutagua Juan Alexadner
Alcala Bejarano Jennifer Paola
 */

/**
 * Ventana del Almacén de Cajas (Lista Unidireccional).
 * Izquierda: formulario y botones. Derecha: tabla y dibujo de los enlaces.
 * La ventana solo llama a métodos públicos de ListaCajas; no toca los enlaces.
 */
public class GUICajas extends JFrame {

    private final ListaCajas lista = new ListaCajas();

    // Captura de datos del record
    private final JTextField txtCodigo = new JTextField(14);
    private final JTextField txtContenido = new JTextField(14);
    private final JTextField txtPeso = new JTextField(14);
    // Posición y búsqueda
    private final JTextField txtPosicion = new JTextField(14);
    private final JTextField txtBuscar = new JTextField(14);

    private final JLabel lblTotal = new JLabel("Cajas: 0");

    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new Object[]{"Posición", "Código", "Contenido", "Peso (kg)"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false; // la tabla es solo para ver
        }
    };
    private final JTable tabla = new JTable(modeloTabla);
    private final JTextArea txtEnlaces = new JTextArea(5, 40);

    public GUICajas() {
        super("Almacén de Cajas - Lista Unidireccional");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel izquierda = new JPanel();
        izquierda.setLayout(new BoxLayout(izquierda, BoxLayout.Y_AXIS));
        izquierda.add(crearPanelFormulario());
        izquierda.add(crearFilaBotones("Insertar",
                boton("Al principio", e -> insertar(Donde.PRINCIPIO)),
                boton("Al final", e -> insertar(Donde.FINAL)),
                boton("En posición", e -> insertar(Donde.POSICION))));
        izquierda.add(crearFilaBotones("Remover",
                boton("Al principio", e -> remover(Donde.PRINCIPIO)),
                boton("Al final", e -> remover(Donde.FINAL)),
                boton("En posición", e -> remover(Donde.POSICION))));
        izquierda.add(crearFilaBotones("Mostrar",
                boton("Al principio", e -> mostrar(Donde.PRINCIPIO)),
                boton("Al final", e -> mostrar(Donde.FINAL)),
                boton("En posición", e -> mostrar(Donde.POSICION))));
        izquierda.add(crearFilaBotones("Otros",
                boton("Buscar por código", e -> buscar()),
                boton("Vaciar lista", e -> vaciar())));
        lblTotal.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        izquierda.add(lblTotal);
        izquierda.add(Box.createVerticalGlue());

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Contenido de la lista"));

        txtEnlaces.setEditable(false);
        txtEnlaces.setLineWrap(true);
        txtEnlaces.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scrollEnlaces = new JScrollPane(txtEnlaces);
        scrollEnlaces.setBorder(BorderFactory.createTitledBorder("Enlaces (primero → siguiente → ... → null)"));

        JSplitPane derecha = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollTabla, scrollEnlaces);
        derecha.setResizeWeight(0.7);

        getContentPane().setLayout(new BorderLayout(8, 8));
        getContentPane().add(izquierda, BorderLayout.WEST);
        getContentPane().add(derecha, BorderLayout.CENTER);

        setSize(950, 560);
        setLocationRelativeTo(null);
        actualizarVista();
    }

    // ===================== Construcción de la ventana =====================

    private enum Donde { PRINCIPIO, FINAL, POSICION }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 6, 6));
        panel.setBorder(BorderFactory.createTitledBorder("Datos de la caja"));
        panel.add(new JLabel("Código:"));
        panel.add(txtCodigo);
        panel.add(new JLabel("Contenido:"));
        panel.add(txtContenido);
        panel.add(new JLabel("Peso (kg):"));
        panel.add(txtPeso);
        panel.add(new JLabel("Posición (0 = primero):"));
        panel.add(txtPosicion);
        panel.add(new JLabel("Código a buscar:"));
        panel.add(txtBuscar);
        return panel;
    }

    private JPanel crearFilaBotones(String titulo, JButton... botones) {
        JPanel panel = new JPanel(new GridLayout(1, botones.length, 6, 6));
        panel.setBorder(BorderFactory.createTitledBorder(titulo));
        for (JButton b : botones) {
            panel.add(b);
        }
        return panel;
    }

    private JButton boton(String texto, java.awt.event.ActionListener accion) {
        JButton b = new JButton(texto);
        b.addActionListener(accion);
        return b;
    }

    // ===================== Acciones de los botones =====================

    private void insertar(Donde donde) {
        Caja caja = leerCaja();
        if (caja == null) {
            return;
        }
        try {
            switch (donde) {
                case PRINCIPIO -> lista.insertarAlPrincipio(caja);
                case FINAL -> lista.insertarAlFinal(caja);
                case POSICION -> {
                    Integer pos = leerPosicion();
                    if (pos == null) {
                        return;
                    }
                    lista.insertarEnPosicion(caja, pos);
                }
            }
        } catch (IndexOutOfBoundsException ex) {
            error("Posición fuera de rango", ex.getMessage());
            return;
        }
        limpiarCampos();
        actualizarVista();
    }

    private void remover(Donde donde) {
        if (avisarSiVacia()) {
            return;
        }
        Caja removida;
        try {
            removida = switch (donde) {
                case PRINCIPIO -> lista.removerAlPrincipio();
                case FINAL -> lista.removerAlFinal();
                case POSICION -> {
                    Integer pos = leerPosicion();
                    yield (pos == null) ? null : lista.removerEnPosicion(pos);
                }
            };
        } catch (IndexOutOfBoundsException ex) {
            error("Posición fuera de rango", ex.getMessage());
            return;
        }
        if (removida == null) {
            return; // la posición no era un número; ya se avisó
        }
        actualizarVista();
        info("Caja removida", "Se removió la caja:\n\n" + removida);
    }

    private void mostrar(Donde donde) {
        if (avisarSiVacia()) {
            return;
        }
        try {
            switch (donde) {
                case PRINCIPIO -> mostrarCaja("Primera caja", lista.mostrarAlPrincipio(), 0);
                case FINAL -> mostrarCaja("Última caja", lista.mostrarAlFinal(), lista.getTamanio() - 1);
                case POSICION -> {
                    Integer pos = leerPosicion();
                    if (pos != null) {
                        mostrarCaja("Caja en posición " + pos, lista.mostrarEnPosicion(pos), pos);
                    }
                }
            }
        } catch (IndexOutOfBoundsException ex) {
            error("Posición fuera de rango", ex.getMessage());
        }
    }

    private void buscar() {
        String codigo = txtBuscar.getText().trim();
        if (codigo.isEmpty()) {
            advertencia("Campo vacío", "Escribe el código que quieres buscar.");
            txtBuscar.requestFocus();
            return;
        }
        ListaCajas.ResultadoBusqueda r = lista.buscarPorCodigo(codigo);
        if (r == null) {
            tabla.clearSelection();
            info("No encontrada", "No hay ninguna caja con el código \"" + codigo + "\".");
        } else {
            mostrarCaja("Caja encontrada en la posición " + r.posicion(), r.caja(), r.posicion());
        }
    }

    private void vaciar() {
        if (avisarSiVacia()) {
            return;
        }
        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Seguro que quieres vaciar las " + lista.getTamanio() + " cajas?",
                "Vaciar lista", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (respuesta == JOptionPane.YES_OPTION) {
            lista.vaciar();
            actualizarVista();
        }
    }

    // ===================== Validación de entradas =====================

    /** Lee y valida el formulario. Devuelve null (y avisa) si algo está mal. */
    private Caja leerCaja() {
        String codigo = txtCodigo.getText().trim();
        String contenido = txtContenido.getText().trim();
        String textoPeso = txtPeso.getText().trim();

        if (codigo.isEmpty() || contenido.isEmpty() || textoPeso.isEmpty()) {
            advertencia("Campos vacíos", "Llena todos los campos: código, contenido y peso.");
            return null;
        }

        double peso;
        try {
            peso = Double.parseDouble(textoPeso.replace(',', '.'));
        } catch (NumberFormatException e) {
            error("Peso inválido", "El peso debe ser un número (ejemplo: 12.5).\nNo se aceptan letras.");
            enfocar(txtPeso);
            return null;
        }
        if (peso <= 0) {
            advertencia("Peso inválido", "El peso debe ser mayor que 0.");
            enfocar(txtPeso);
            return null;
        }

        if (lista.buscarPorCodigo(codigo) != null) {
            advertencia("Código repetido", "Ya hay una caja con el código \"" + codigo + "\" en la lista.");
            enfocar(txtCodigo);
            return null;
        }

        return new Caja(codigo, contenido, peso);
    }

    /** Lee la posición como entero. El rango lo valida la lista. */
    private Integer leerPosicion() {
        String texto = txtPosicion.getText().trim();
        if (texto.isEmpty()) {
            advertencia("Posición vacía", "Escribe la posición (número entero, la primera es 0).");
            txtPosicion.requestFocus();
            return null;
        }
        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            error("Posición inválida", "La posición debe ser un número entero (ejemplo: 2).");
            enfocar(txtPosicion);
            return null;
        }
    }

    private boolean avisarSiVacia() {
        if (lista.estaVacia()) {
            info("Lista vacía", "No hay cajas en la lista.");
            return true;
        }
        return false;
    }

    // ===================== Refresco de la vista =====================

    /** Recorre la lista desde primero y redibuja la tabla, los enlaces y el total. */
    private void actualizarVista() {
        modeloTabla.setRowCount(0);
        StringBuilder enlaces = new StringBuilder("primero → ");

        Nodo actual = lista.getPrimero();
        int posicion = 0;
        while (actual != null) {
            Caja c = actual.getCaja();
            modeloTabla.addRow(new Object[]{posicion, c.codigo(), c.contenido(), String.format("%.2f", c.peso())});
            enlaces.append("[").append(c.codigo()).append("] → ");
            actual = actual.getSiguiente();
            posicion++;
        }
        enlaces.append("null");

        txtEnlaces.setText(enlaces.toString());
        lblTotal.setText(String.format("Cajas: %d   |   Peso total: %.2f kg",
                lista.getTamanio(), lista.pesoTotal()));
    }

    private void mostrarCaja(String titulo, Caja caja, int posicion) {
        tabla.setRowSelectionInterval(posicion, posicion);
        tabla.scrollRectToVisible(tabla.getCellRect(posicion, 0, true));
        info(titulo, caja.toString());
    }

    private void limpiarCampos() {
        txtCodigo.setText("");
        txtContenido.setText("");
        txtPeso.setText("");
        txtCodigo.requestFocus();
    }

    private void enfocar(JTextField campo) {
        campo.requestFocus();
        campo.selectAll();
    }

    private void info(String titulo, String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    private void advertencia(String titulo, String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.WARNING_MESSAGE);
    }

    private void error(String titulo, String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            // si Nimbus no está disponible se usa el aspecto por defecto
        }
        SwingUtilities.invokeLater(() -> new GUICajas().setVisible(true));
    }
}
