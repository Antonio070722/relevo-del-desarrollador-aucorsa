package controller.bus;

import controller.dao.BusDAO;
import controller.connection.ConexionBBDD;
import models.Bus;
import view.bus.BusPanel;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Clase BusController
 */
public class BusController {

    BusDAO busDAO;
    BusPanel busPanel;

    // Lista local que almacena los buses cargados desde la base de datos
    ArrayList<Bus> buses = new ArrayList<>();


    /**
     * Constructor de BusController
     * @param panel Panel de la vista donde se muestran los buses
     */
    public BusController(BusPanel panel) {
        this.busPanel = panel;
        busDAO = new BusDAO();

        // Carga los buses al inicializar el controlador
        cargarBuses();
    }


    /**
     * Carga todos los buses de la base de datos y los muestra en la tabla del panel.
     */
    public void cargarBuses() {
        try {
            buses = BusDAO.getBuses();

            // Limpia la tabla antes de recargar los datos
            busPanel.getModeloTabla().setRowCount(0);

            // Añade cada bus como una nueva fila en la tabla
            for (Bus b : buses) {
                busPanel.getModeloTabla().addRow(new Object[]{
                        b.getIdBus(), b.getTipoBus(), b.getLicenciaBus()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimina el bus seleccionado en la tabla tras pedir confirmación al usuario.
     */
    public void eliminarBus() {
        int filaSeleccionada = busPanel.getTablaVista().getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(busPanel, "Debes seleccionar un bus para eliminar.",
                    "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String matricula = (String) busPanel.getModeloTabla().getValueAt(filaSeleccionada, 0);
        Bus bus = new Bus();
        bus.setIdBus(matricula);

        int opcion = JOptionPane.showConfirmDialog(busPanel,
                "¿Estás seguro de que quieres eliminar el bus " + matricula + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        int deleteSuccess = 0;
        try (Connection con = ConexionBBDD.getConexion()) {
            deleteSuccess = BusDAO.borrarBus(bus);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (deleteSuccess != 0) {
            JOptionPane.showMessageDialog(busPanel, "Bus eliminado correctamente.",
                    "Información", JOptionPane.INFORMATION_MESSAGE);
            cargarBuses();
        } else {
            JOptionPane.showMessageDialog(busPanel, "Error al eliminar el bus.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Agrega un nuevo bus solicitando los datos al usuario en un solo cuadro de diálogo.
     * Se utiliza BorderLayout y GridLayout para organizar los campos.
     */
    public void agregarBus() {
        // Panel principal con BorderLayout
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel central con GridLayout (2 columnas, 3 filas)
        JPanel formulario = new JPanel(new GridLayout(3, 2, 10, 10));
        JTextField txtMatricula = new JTextField(10);
        JTextField txtTipo = new JTextField(15);
        JTextField txtLicencia = new JTextField(15);

        formulario.add(new JLabel("Matrícula:"));
        formulario.add(txtMatricula);
        formulario.add(new JLabel("Tipo:"));
        formulario.add(txtTipo);
        formulario.add(new JLabel("Licencia necesaria:"));
        formulario.add(txtLicencia);

        panel.add(formulario, BorderLayout.CENTER);

        int opcion = JOptionPane.showConfirmDialog(busPanel, panel,
                "Nuevo Bus", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (opcion == JOptionPane.OK_OPTION) {
            String matricula = txtMatricula.getText().trim();
            String tipo = txtTipo.getText().trim();
            String licencia = txtLicencia.getText().trim();

            if (matricula.isEmpty()) {
                JOptionPane.showMessageDialog(busPanel, "La matrícula no puede estar vacía.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (tipo.isEmpty()) {
                JOptionPane.showMessageDialog(busPanel, "El tipo no puede estar vacío.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (licencia.isEmpty()) {
                JOptionPane.showMessageDialog(busPanel, "La licencia no puede estar vacía.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Bus nuevo = new Bus();
            nuevo.setIdBus(matricula);
            nuevo.setTipoBus(tipo);
            nuevo.setLicenciaBus(licencia);

            int result = BusDAO.insertarBus(nuevo);
            if (result > 0) {
                JOptionPane.showMessageDialog(busPanel, "Bus añadido correctamente.",
                        "Información", JOptionPane.INFORMATION_MESSAGE);
                cargarBuses();
            } else {
                JOptionPane.showMessageDialog(busPanel,
                        "Error al añadir el bus. Comprueba que la matrícula no exista.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Modifica el bus seleccionado en la tabla.
     * Muestra un cuadro de diálogo con los datos actuales y permite editarlos.
     * La matrícula (clave primaria) no se puede modificar.
     */
    public void modificarBus() {
        int filaSeleccionada = busPanel.getTablaVista().getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(busPanel,
                    "Debes seleccionar un bus para modificar.",
                    "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String matricula = (String) busPanel.getModeloTabla().getValueAt(filaSeleccionada, 0);
        String tipoActual = (String) busPanel.getModeloTabla().getValueAt(filaSeleccionada, 1);
        String licenciaActual = (String) busPanel.getModeloTabla().getValueAt(filaSeleccionada, 2);

        // Panel principal con BorderLayout
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel central con GridLayout (2 columnas, 3 filas)
        JPanel formulario = new JPanel(new GridLayout(3, 2, 10, 10));
        JLabel lblMatricula = new JLabel(matricula);
        JTextField txtTipo = new JTextField(tipoActual, 15);
        JTextField txtLicencia = new JTextField(licenciaActual, 15);

        formulario.add(new JLabel("Matrícula:"));
        formulario.add(lblMatricula);
        formulario.add(new JLabel("Tipo:"));
        formulario.add(txtTipo);
        formulario.add(new JLabel("Licencia necesaria:"));
        formulario.add(txtLicencia);

        panel.add(formulario, BorderLayout.CENTER);

        int opcion = JOptionPane.showConfirmDialog(busPanel, panel,
                "Modificar Bus", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (opcion == JOptionPane.OK_OPTION) {
            String nuevoTipo = txtTipo.getText().trim();
            String nuevaLicencia = txtLicencia.getText().trim();

            if (nuevoTipo.isEmpty()) {
                JOptionPane.showMessageDialog(busPanel,
                        "El tipo no puede estar vacío.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (nuevaLicencia.isEmpty()) {
                JOptionPane.showMessageDialog(busPanel,
                        "La licencia no puede estar vacía.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Bus bus = new Bus();
            bus.setIdBus(matricula);
            bus.setTipoBus(nuevoTipo);
            bus.setLicenciaBus(nuevaLicencia);

            int result = BusDAO.editarBus(bus);
            if (result > 0) {
                JOptionPane.showMessageDialog(busPanel,
                        "Bus modificado correctamente.",
                        "Información", JOptionPane.INFORMATION_MESSAGE);
                cargarBuses();
            } else {
                JOptionPane.showMessageDialog(busPanel,
                        "Error al modificar el bus.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}