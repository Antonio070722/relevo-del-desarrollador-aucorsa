package controller.lugar;

import controller.dao.LugarDAO;
import controller.connection.ConexionBBDD;
import models.Lugar;
import view.lugar.LugarPanel;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Clase LugarController
 * Controlador de la sección de lugares.
 * Gestiona la carga de datos desde la base de datos y las acciones
 * disponibles sobre los lugares en la vista.
 */
public class LugarController {


    LugarDAO lugarDAO;
    LugarPanel lugarPanel;

    // Lista local que almacena los lugares cargados desde la base de datos
    ArrayList<Lugar> lugares = new ArrayList<>();


    /**
     * Constructor de LugarController.
     * Inicializa el DAO, asocia el panel y carga los datos de la base de datos.
     * @param panel Panel de la vista donde se mostrarán los lugares
     */
    public LugarController(LugarPanel panel) {
        this.lugarPanel = panel;
        lugarDAO = new LugarDAO();

        // Carga los lugares al inicializar el controlador
        cargarLugares();
    }


    /**
     * Carga todos los lugares de la base de datos y los muestra en la tabla del panel.
     */
    public void cargarLugares() {
        try {
            lugares = LugarDAO.getLugares();

            // Limpia la tabla antes de recargar los datos
            lugarPanel.getModeloTabla().setRowCount(0);

            // Añade cada lugar como una nueva fila en la tabla
            for (Lugar l : lugares) {
                lugarPanel.getModeloTabla().addRow(new Object[]{
                        l.getIdLugar(), l.getCodigoPostal(), l.getCiudad(), l.getLugar()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimina el lugar seleccionado en la tabla tras pedir confirmación al usuario.
     */
    public void eliminarLugar() {
        int filaSeleccionada = lugarPanel.getTablaVista().getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(lugarPanel, "Debes seleccionar un lugar para eliminar.",
                    "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idLugar = (int) lugarPanel.getModeloTabla().getValueAt(filaSeleccionada, 0);
        Lugar lugar = new Lugar();
        lugar.setIdLugar(idLugar);

        int opcion = JOptionPane.showConfirmDialog(lugarPanel,
                "¿Estás seguro de que quieres eliminar el lugar con ID " + idLugar + "?\n" +
                "Se eliminarán también sus rutas asociadas en BDP.",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        int deleteSuccess = LugarDAO.borrarLugar(lugar);

        if (deleteSuccess > 0) {
            JOptionPane.showMessageDialog(lugarPanel, "Lugar eliminado correctamente.",
                    "Información", JOptionPane.INFORMATION_MESSAGE);
            cargarLugares();
        } else {
            JOptionPane.showMessageDialog(lugarPanel, "Error al eliminar el lugar.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Agrega un nuevo lugar solicitando los datos al usuario en un solo cuadro de diálogo.
     * Se utiliza BorderLayout y GridLayout para organizar los campos.
     */
    public void agregarLugar() {
        // Panel principal con BorderLayout
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel central con GridLayout (2 columnas, 4 filas)
        JPanel formulario = new JPanel(new GridLayout(4, 2, 10, 10));
        JTextField txtIdLugar = new JTextField(10);
        JTextField txtCodigoPostal = new JTextField(10);
        JTextField txtCiudad = new JTextField(15);
        JTextField txtLugar = new JTextField(15);

        formulario.add(new JLabel("ID Lugar:"));
        formulario.add(txtIdLugar);
        formulario.add(new JLabel("Código Postal:"));
        formulario.add(txtCodigoPostal);
        formulario.add(new JLabel("Ciudad:"));
        formulario.add(txtCiudad);
        formulario.add(new JLabel("Lugar/Sitio:"));
        formulario.add(txtLugar);

        panel.add(formulario, BorderLayout.CENTER);

        int opcion = JOptionPane.showConfirmDialog(lugarPanel, panel,
                "Nuevo Lugar", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (opcion == JOptionPane.OK_OPTION) {
            String idStr = txtIdLugar.getText().trim();
            String codigoPostal = txtCodigoPostal.getText().trim();
            String ciudad = txtCiudad.getText().trim();
            String sitio = txtLugar.getText().trim();

            if (idStr.isEmpty()) {
                JOptionPane.showMessageDialog(lugarPanel, "El ID no puede estar vacío.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int idLugar;
            try {
                idLugar = Integer.parseInt(idStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(lugarPanel, "El ID debe ser un número válido.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (codigoPostal.isEmpty()) {
                JOptionPane.showMessageDialog(lugarPanel, "El código postal no puede estar vacío.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (ciudad.isEmpty()) {
                JOptionPane.showMessageDialog(lugarPanel, "La ciudad no puede estar vacía.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (sitio.isEmpty()) {
                JOptionPane.showMessageDialog(lugarPanel, "El lugar/sitio no puede estar vacío.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Lugar nuevo = new Lugar();
            nuevo.setIdLugar(idLugar);
            nuevo.setCodigoPostal(codigoPostal);
            nuevo.setCiudad(ciudad);
            nuevo.setLugar(sitio);

            int result = LugarDAO.insertarLugar(nuevo);
            if (result > 0) {
                JOptionPane.showMessageDialog(lugarPanel, "Lugar añadido correctamente.",
                        "Información", JOptionPane.INFORMATION_MESSAGE);
                cargarLugares();
            } else {
                JOptionPane.showMessageDialog(lugarPanel,
                        "Error al añadir el lugar. Comprueba que el ID no exista ya.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Modifica el lugar seleccionado en la tabla.
     * Muestra un cuadro de diálogo con los datos actuales y permite editarlos.
     * El ID de lugar (clave primaria) no se puede modificar.
     */
    public void modificarLugar() {
        int filaSeleccionada = lugarPanel.getTablaVista().getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(lugarPanel,
                    "Debes seleccionar un lugar para modificar.",
                    "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idLugar = (int) lugarPanel.getModeloTabla().getValueAt(filaSeleccionada, 0);
        String cpActual = (String) lugarPanel.getModeloTabla().getValueAt(filaSeleccionada, 1);
        String ciudadActual = (String) lugarPanel.getModeloTabla().getValueAt(filaSeleccionada, 2);
        String sitioActual = (String) lugarPanel.getModeloTabla().getValueAt(filaSeleccionada, 3);

        // Panel principal con BorderLayout
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel central con GridLayout (2 columnas, 4 filas)
        JPanel formulario = new JPanel(new GridLayout(4, 2, 10, 10));
        JLabel lblIdLugar = new JLabel(String.valueOf(idLugar));
        JTextField txtCodigoPostal = new JTextField(cpActual, 10);
        JTextField txtCiudad = new JTextField(ciudadActual, 15);
        JTextField txtLugar = new JTextField(sitioActual, 15);

        formulario.add(new JLabel("ID Lugar:"));
        formulario.add(lblIdLugar);
        formulario.add(new JLabel("Código Postal:"));
        formulario.add(txtCodigoPostal);
        formulario.add(new JLabel("Ciudad:"));
        formulario.add(txtCiudad);
        formulario.add(new JLabel("Lugar/Sitio:"));
        formulario.add(txtLugar);

        panel.add(formulario, BorderLayout.CENTER);

        int opcion = JOptionPane.showConfirmDialog(lugarPanel, panel,
                "Modificar Lugar", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (opcion == JOptionPane.OK_OPTION) {
            String nuevoCp = txtCodigoPostal.getText().trim();
            String nuevaCiudad = txtCiudad.getText().trim();
            String nuevoSitio = txtLugar.getText().trim();

            if (nuevoCp.isEmpty()) {
                JOptionPane.showMessageDialog(lugarPanel,
                        "El código postal no puede estar vacío.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (nuevaCiudad.isEmpty()) {
                JOptionPane.showMessageDialog(lugarPanel,
                        "La ciudad no puede estar vacía.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (nuevoSitio.isEmpty()) {
                JOptionPane.showMessageDialog(lugarPanel,
                        "El lugar/sitio no puede estar vacío.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Lugar lugar = new Lugar();
            lugar.setIdLugar(idLugar);
            lugar.setCodigoPostal(nuevoCp);
            lugar.setCiudad(nuevaCiudad);
            lugar.setLugar(nuevoSitio);

            int result = LugarDAO.editarLugar(lugar);
            if (result > 0) {
                JOptionPane.showMessageDialog(lugarPanel,
                        "Lugar modificado correctamente.",
                        "Información", JOptionPane.INFORMATION_MESSAGE);
                cargarLugares();
            } else {
                JOptionPane.showMessageDialog(lugarPanel,
                        "Error al modificar el lugar.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
