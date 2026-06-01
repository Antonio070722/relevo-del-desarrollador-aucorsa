package controller.ruta;

import controller.dao.BCLDAO;
import controller.connection.ConexionBBDD;
import models.BCL;
import view.ruta.RutaPanel;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Clase RutaController
 * Controlador de la sección de rutas (BCL)
 * Gestiona la carga de datos desde la base de datos y las acciones
 * disponibles sobre las rutas en la vista
 */
public class RutaController {


    BCLDAO bclDAO;
    RutaPanel rutaPanel;

    // Lista local que almacena las rutas cargadas desde la base de datos
    ArrayList<BCL> rutas = new ArrayList<>();


    /**
     * Constructor de RutaController
     * Inicializa el DAO, asocia el panel y carga los datos de la base de datos
     * @param panel Panel de la vista donde se mostrarán las rutas
     */
    public RutaController(RutaPanel panel) {
        this.rutaPanel = panel;
        bclDAO = new BCLDAO();

        // Carga las rutas al inicializar el controlador
        cargarRutas();
    }


    /**
     * Carga todas las rutas de la base de datos y las muestra en la tabla del panel
     */
    public void cargarRutas() {
        try {
            rutas = BCLDAO.getBCL();

            // Limpia la tabla antes de recargar los datos
            rutaPanel.getModeloTabla().setRowCount(0);

            // Añade cada ruta como una nueva fila en la tabla
            for (BCL b : rutas) {
                rutaPanel.getModeloTabla().addRow(new Object[]{
                        b.getIdBus(), b.getNumeroConductor(), b.getIdLugar(), b.getDiaSemana()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimina la ruta seleccionada en la tabla tras pedir confirmación al usuario
     */
    public void eliminarRuta() {
        int filaSeleccionada = rutaPanel.getTablaVista().getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(rutaPanel, "Debes seleccionar una ruta para eliminar.",
                    "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idBus = (String) rutaPanel.getModeloTabla().getValueAt(filaSeleccionada, 0);
        int numeroConductor = (int) rutaPanel.getModeloTabla().getValueAt(filaSeleccionada, 1);
        int idLugar = (int) rutaPanel.getModeloTabla().getValueAt(filaSeleccionada, 2);

        BCL bcl = new BCL();
        bcl.setIdBus(idBus);
        bcl.setNumeroConductor(numeroConductor);
        bcl.setIdLugar(idLugar);

        int opcion = JOptionPane.showConfirmDialog(rutaPanel,
                "¿Estás seguro de que quieres eliminar la ruta del bus " + idBus + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        int deleteSuccess = 0;
        try (Connection con = ConexionBBDD.getConexion()) {
            deleteSuccess = BCLDAO.borrarBCL(bcl);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (deleteSuccess != 0) {
            JOptionPane.showMessageDialog(rutaPanel, "Ruta eliminada correctamente.",
                    "Información", JOptionPane.INFORMATION_MESSAGE);
            cargarRutas();
        } else {
            JOptionPane.showMessageDialog(rutaPanel, "Error al eliminar la ruta.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Agrega una nueva ruta solicitando los datos al usuario en un solo cuadro de diálogo
     * Se utiliza BorderLayout y GridLayout para organizar los campos
     */
    public void agregarRuta() {
        // Panel principal con BorderLayout
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel central con GridLayout (2 columnas, 4 filas)
        JPanel formulario = new JPanel(new GridLayout(4, 2, 10, 10));
        JTextField txtIdBus = new JTextField(10);
        JTextField txtNumeroConductor = new JTextField(10);
        JTextField txtIdLugar = new JTextField(10);
        JTextField txtDiaSemana = new JTextField(15);

        formulario.add(new JLabel("ID Bus:"));
        formulario.add(txtIdBus);
        formulario.add(new JLabel("Nº Conductor:"));
        formulario.add(txtNumeroConductor);
        formulario.add(new JLabel("ID Lugar:"));
        formulario.add(txtIdLugar);
        formulario.add(new JLabel("Día de la semana:"));
        formulario.add(txtDiaSemana);

        panel.add(formulario, BorderLayout.CENTER);

        int opcion = JOptionPane.showConfirmDialog(rutaPanel, panel,
                "Nueva Ruta", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (opcion == JOptionPane.OK_OPTION) {
            String idBus = txtIdBus.getText().trim();
            String numConStr = txtNumeroConductor.getText().trim();
            String idLugarStr = txtIdLugar.getText().trim();
            String diaSemana = txtDiaSemana.getText().trim();

            if (idBus.isEmpty()) {
                JOptionPane.showMessageDialog(rutaPanel, "El ID de bus no puede estar vacío.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int numeroConductor;
            try {
                numeroConductor = Integer.parseInt(numConStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(rutaPanel, "El número de conductor debe ser un número válido.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int idLugar;
            try {
                idLugar = Integer.parseInt(idLugarStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(rutaPanel, "El ID de lugar debe ser un número válido.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (diaSemana.isEmpty()) {
                JOptionPane.showMessageDialog(rutaPanel, "El día de la semana no puede estar vacío.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            BCL nuevo = new BCL();
            nuevo.setIdBus(idBus);
            nuevo.setNumeroConductor(numeroConductor);
            nuevo.setIdLugar(idLugar);
            nuevo.setDiaSemana(diaSemana);

            int result = BCLDAO.insertarBCL(nuevo);
            if (result > 0) {
                JOptionPane.showMessageDialog(rutaPanel, "Ruta añadida correctamente.",
                        "Información", JOptionPane.INFORMATION_MESSAGE);
                cargarRutas();
            } else {
                JOptionPane.showMessageDialog(rutaPanel,
                        "Error al añadir la ruta. Comprueba que la combinación no exista ya.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Modifica la ruta seleccionada en la tabla
     * Muestra un cuadro de diálogo con los datos actuales y permite editarlos
     * La clave primaria compuesta (idBus, numeroConductor, idLugar) no se puede modificar
     */
    public void modificarRuta() {
        int filaSeleccionada = rutaPanel.getTablaVista().getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(rutaPanel,
                    "Debes seleccionar una ruta para modificar.",
                    "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idBus = (String) rutaPanel.getModeloTabla().getValueAt(filaSeleccionada, 0);
        int numeroConductor = (int) rutaPanel.getModeloTabla().getValueAt(filaSeleccionada, 1);
        int idLugar = (int) rutaPanel.getModeloTabla().getValueAt(filaSeleccionada, 2);
        String diaActual = (String) rutaPanel.getModeloTabla().getValueAt(filaSeleccionada, 3);

        // Panel principal con BorderLayout
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel central con GridLayout (2 columnas, 4 filas)
        JPanel formulario = new JPanel(new GridLayout(4, 2, 10, 10));
        JLabel lblIdBus = new JLabel(idBus);
        JLabel lblNumeroConductor = new JLabel(String.valueOf(numeroConductor));
        JLabel lblIdLugar = new JLabel(String.valueOf(idLugar));
        JTextField txtDiaSemana = new JTextField(diaActual, 15);

        formulario.add(new JLabel("ID Bus:"));
        formulario.add(lblIdBus);
        formulario.add(new JLabel("Nº Conductor:"));
        formulario.add(lblNumeroConductor);
        formulario.add(new JLabel("ID Lugar:"));
        formulario.add(lblIdLugar);
        formulario.add(new JLabel("Día de la semana:"));
        formulario.add(txtDiaSemana);

        panel.add(formulario, BorderLayout.CENTER);

        int opcion = JOptionPane.showConfirmDialog(rutaPanel, panel,
                "Modificar Ruta", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (opcion == JOptionPane.OK_OPTION) {
            String nuevoDia = txtDiaSemana.getText().trim();

            if (nuevoDia.isEmpty()) {
                JOptionPane.showMessageDialog(rutaPanel,
                        "El día de la semana no puede estar vacío.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            BCL bcl = new BCL();
            bcl.setIdBus(idBus);
            bcl.setNumeroConductor(numeroConductor);
            bcl.setIdLugar(idLugar);
            bcl.setDiaSemana(nuevoDia);

            int result = BCLDAO.editarBCL(bcl);
            if (result > 0) {
                JOptionPane.showMessageDialog(rutaPanel,
                        "Ruta modificada correctamente.",
                        "Información", JOptionPane.INFORMATION_MESSAGE);
                cargarRutas();
            } else {
                JOptionPane.showMessageDialog(rutaPanel,
                        "Error al modificar la ruta.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
