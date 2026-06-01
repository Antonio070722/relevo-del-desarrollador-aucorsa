package controller.conductor;

import controller.dao.ConductorDAO;
import controller.connection.ConexionBBDD;
import models.Conductor;
import view.conductor.ConductorPanel;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class ConductorController {

    ConductorDAO conductorDAO;
    ConductorPanel conductorPanel;

    // Lista local que almacena los conductores cargados desde la base de datos
    ArrayList<Conductor> conductores = new ArrayList<>();


    /**
     * Constructor de ConductorController
     * @param panel Panel de la vista donde se mostrarán los conductores
     */
    public ConductorController(ConductorPanel panel) {
        this.conductorPanel = panel;
        conductorDAO = new ConductorDAO();

        // Carga los conductores al inicializar el controlador
        cargarConductores();
    }


    /**
     * Carga todos los conductores de la base de datos y los muestra en la tabla del panel.
     */
    public void cargarConductores() {
        try {
            conductores = conductorDAO.getConductores();

            // Limpia la tabla antes de recargar los datos
            conductorPanel.getModeloTabla().setRowCount(0);

            // Añade cada conductor como una nueva fila en la tabla
            for (Conductor c : conductores) {
                conductorPanel.getModeloTabla().addRow(new Object[]{
                        c.getNumeroConductor(), c.getNombreConductor(), c.getApellidoConductor()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimina el conductor seleccionado en la tabla tras pedir confirmación al usuario.
     */
    public void eliminarConductor() {
        int filaSeleccionada = conductorPanel.getTablaVista().getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(conductorPanel,
                    "Debes seleccionar un conductor para eliminar.",
                    "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int numeroConductor = (int) conductorPanel.getModeloTabla().getValueAt(filaSeleccionada, 0);
        Conductor conductor = new Conductor();
        conductor.setNumeroConductor(numeroConductor);

        int opcion = JOptionPane.showConfirmDialog(conductorPanel,
                "¿Estás seguro de que quieres eliminar el conductor " + numeroConductor + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        int deleteSuccess = 0;
        try (Connection con = ConexionBBDD.getConexion()) {
            deleteSuccess = conductorDAO.borrarConductor(conductor);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (deleteSuccess != 0) {
            JOptionPane.showMessageDialog(conductorPanel, "Conductor eliminado correctamente.",
                    "Información", JOptionPane.INFORMATION_MESSAGE);
            cargarConductores();
        } else {
            JOptionPane.showMessageDialog(conductorPanel, "Error al eliminar el conductor.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Agrega un nuevo conductor solicitando los datos al usuario en un solo cuadro de diálogo
     */
    public void agregarConductor() {
        // Panel principal con BorderLayout
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel central con GridLayout (2 columnas, 3 filas)
        JPanel formulario = new JPanel(new GridLayout(3, 2, 10, 10));
        JTextField txtNumero = new JTextField(10);
        JTextField txtNombre = new JTextField(10);
        JTextField txtApellido = new JTextField(10);

        formulario.add(new JLabel("Número:"));
        formulario.add(txtNumero);
        formulario.add(new JLabel("Nombre:"));
        formulario.add(txtNombre);
        formulario.add(new JLabel("Apellido:"));
        formulario.add(txtApellido);

        panel.add(formulario, BorderLayout.CENTER);

        int opcion = JOptionPane.showConfirmDialog(conductorPanel, panel,
                "Nuevo Conductor", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (opcion == JOptionPane.OK_OPTION) {
            String idStr = txtNumero.getText().trim();
            String nombre = txtNombre.getText().trim();
            String apellido = txtApellido.getText().trim();

            if (idStr.isEmpty()) {
                JOptionPane.showMessageDialog(conductorPanel, "El número no puede estar vacío.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int numConductor;
            try {
                numConductor = Integer.parseInt(idStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(conductorPanel, "Número inválido.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(conductorPanel, "El nombre no puede estar vacío.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (apellido.isEmpty()) {
                JOptionPane.showMessageDialog(conductorPanel, "El apellido no puede estar vacío.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Conductor nuevo = new Conductor();
            nuevo.setNumeroConductor(numConductor);
            nuevo.setNombreConductor(nombre);
            nuevo.setApellidoConductor(apellido);

            int result = ConductorDAO.insertarConductor(nuevo);
            if (result > 0) {
                JOptionPane.showMessageDialog(conductorPanel, "Conductor añadido correctamente.",
                        "Información", JOptionPane.INFORMATION_MESSAGE);
                cargarConductores();
            } else {
                JOptionPane.showMessageDialog(conductorPanel,
                        "Error al añadir el conductor. Comprueba que el número no exista.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Modifica el conductor seleccionado en la tabla.
     * Muestra un cuadro de diálogo con los datos actuales y permite editarlos.
     * El número de conductor no se puede modificar por ser clave primaria.
     */
    public void modificarConductor() {
        int filaSeleccionada = conductorPanel.getTablaVista().getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(conductorPanel,
                    "Debes seleccionar un conductor para modificar.",
                    "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int numeroConductor = (int) conductorPanel.getModeloTabla().getValueAt(filaSeleccionada, 0);
        String nombreActual = (String) conductorPanel.getModeloTabla().getValueAt(filaSeleccionada, 1);
        String apellidoActual = (String) conductorPanel.getModeloTabla().getValueAt(filaSeleccionada, 2);

        // Panel principal con BorderLayout
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel central con GridLayout (2 columnas, 3 filas)
        JPanel formulario = new JPanel(new GridLayout(3, 2, 10, 10));
        JLabel lblNumero = new JLabel(String.valueOf(numeroConductor));
        JTextField txtNombre = new JTextField(nombreActual, 10);
        JTextField txtApellido = new JTextField(apellidoActual, 10);

        formulario.add(new JLabel("Número:"));
        formulario.add(lblNumero);
        formulario.add(new JLabel("Nombre:"));
        formulario.add(txtNombre);
        formulario.add(new JLabel("Apellido:"));
        formulario.add(txtApellido);

        panel.add(formulario, BorderLayout.CENTER);

        int opcion = JOptionPane.showConfirmDialog(conductorPanel, panel,
                "Modificar Conductor", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (opcion == JOptionPane.OK_OPTION) {
            String nuevoNombre = txtNombre.getText().trim();
            String nuevoApellido = txtApellido.getText().trim();

            if (nuevoNombre.isEmpty()) {
                JOptionPane.showMessageDialog(conductorPanel,
                        "El nombre no puede estar vacío.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (nuevoApellido.isEmpty()) {
                JOptionPane.showMessageDialog(conductorPanel,
                        "El apellido no puede estar vacío.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Conductor conductor = new Conductor();
            conductor.setNumeroConductor(numeroConductor);
            conductor.setNombreConductor(nuevoNombre);
            conductor.setApellidoConductor(nuevoApellido);

            int result = conductorDAO.editarConductor(conductor);
            if (result > 0) {
                JOptionPane.showMessageDialog(conductorPanel,
                        "Conductor modificado correctamente.",
                        "Información", JOptionPane.INFORMATION_MESSAGE);
                cargarConductores();
            } else {
                JOptionPane.showMessageDialog(conductorPanel,
                        "Error al modificar el conductor.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}