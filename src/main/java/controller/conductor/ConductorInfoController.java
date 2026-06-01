package controller.conductor;

import controller.dao.ConductorDAO;
import models.Conductor;
import view.conductor.ConductorInfoView;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class ConductorInfoController {

    private final ConductorInfoView ventana;
    private final ConductorDAO conductorDAO = new ConductorDAO();
    private ArrayList<Conductor> listaConductores;
    private Conductor conductorActual;

    public ConductorInfoController(ConductorInfoView ventana) {
        this.ventana = ventana;
        this.listaConductores = conductorDAO.getConductores();
        this.conductorActual = ventana.conductor;
        this.conductorActual = listaConductores.get(conductorEnLista());
        mostrarImagen();

        ventana.botonSiguiente.addActionListener(e -> irSiguiente());
        ventana.botonAnterior.addActionListener(e -> irAnterior());
        ventana.botonCargarImg.addActionListener(e -> seleccionarImagen());
        ventana.botonEditar.addActionListener(e -> abrirEdicion());
    }

    private void abrirEdicion() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel formulario = new JPanel(new GridLayout(3, 2, 10, 10));

        JTextField txtNombre = new JTextField(conductorActual.getNombreConductor(), 15);
        JTextField txtApellido = new JTextField(conductorActual.getApellidoConductor(), 15);

        formulario.add(new JLabel("Número:"));
        formulario.add(new JLabel(String.valueOf(conductorActual.getNumeroConductor())));
        formulario.add(new JLabel("Nombre:"));
        formulario.add(txtNombre);
        formulario.add(new JLabel("Apellido:"));
        formulario.add(txtApellido);
        panel.add(formulario, BorderLayout.CENTER);

        int opcion = JOptionPane.showConfirmDialog(ventana, panel, "Editar Conductor", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcion != JOptionPane.OK_OPTION) return;

        String nuevoNombre = txtNombre.getText().trim();
        String nuevoApellido = txtApellido.getText().trim();

        if (nuevoNombre.isEmpty()) {
            JOptionPane.showMessageDialog(ventana, "El nombre no puede estar vacío.", "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (nuevoApellido.isEmpty()) {
            JOptionPane.showMessageDialog(ventana, "El apellido no puede estar vacío.", "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        conductorActual.setNombreConductor(nuevoNombre);
        conductorActual.setApellidoConductor(nuevoApellido);
        int filas = conductorDAO.editarConductor(conductorActual);
        if (filas > 0) {
            actualizarConductor(conductorActual);
            listaConductores.set(conductorEnLista(), conductorActual);
            JOptionPane.showMessageDialog(ventana, "Conductor modificado correctamente.", "Información", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(ventana, "No se pudo modificar el conductor.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void seleccionarImagen() {
        JFileChooser selector = new JFileChooser();
        selector.setDialogTitle("Seleccionar imagen del conductor");
        selector.setFileFilter(new FileNameExtensionFilter("Imágenes (JPG, PNG, GIF)", "jpg", "jpeg", "png", "gif"));

        if (selector.showOpenDialog(ventana) != JFileChooser.APPROVE_OPTION) return;

        File fichero = selector.getSelectedFile();
        URL urlDir = getClass().getResource("/images/driver/");
        if (urlDir == null) {
            JOptionPane.showMessageDialog(ventana, "No se encontró el directorio de imágenes.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String ext = extraerExtension(fichero.getName());
        String nombreFichero = "driver" + conductorActual.getNumeroConductor() + "_custom." + ext;

        try {
            File destino = new File(new File(urlDir.toURI()), nombreFichero);
            Files.copy(fichero.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
            conductorActual.setImagen(nombreFichero);
            if (conductorDAO.actualizarImagen(conductorActual) > 0) {
                listaConductores.set(conductorEnLista(), conductorActual);
                mostrarImagen();
                JOptionPane.showMessageDialog(ventana, "Imagen actualizada correctamente.", "Información", JOptionPane.INFORMATION_MESSAGE);
            } else {
                conductorActual.setImagen(null);
                JOptionPane.showMessageDialog(ventana, "La imagen se copió pero no se guardó en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (URISyntaxException | IOException ex) {
            JOptionPane.showMessageDialog(ventana, "Error al guardar la imagen: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarImagen() {
        String rutaImg = conductorActual.getImagen();
        if (rutaImg != null && !rutaImg.isBlank()) {
            cargarDesdeRecurso("/images/driver/" + rutaImg);
        } else {
            cargarDesdeRecurso("/images/driver/" + conductorActual.getNumeroConductor() + ".jpg");
        }
    }

    private void cargarDesdeRecurso(String ruta) {
        URL url = getClass().getResource(ruta);
        if (url != null) {
            ventana.imagenConductor.setIcon(new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(220, 220, Image.SCALE_SMOOTH)));
        } else {
            usarImagenPorDefecto();
        }
    }

    private void usarImagenPorDefecto() {
        URL urlDef = getClass().getResource("/images/default.jpg");
        if (urlDef != null) {
            ventana.imagenConductor.setIcon(new ImageIcon(new ImageIcon(urlDef).getImage().getScaledInstance(220, 220, Image.SCALE_SMOOTH)));
        }
    }

    private void irSiguiente() {
        int pos = conductorEnLista();
        if (pos == listaConductores.size() - 1) {
            JOptionPane.showMessageDialog(ventana, "No hay más conductores.", "Información", JOptionPane.INFORMATION_MESSAGE);
        } else {
            actualizarConductor(listaConductores.get(pos + 1));
            mostrarImagen();
        }
    }

    private void irAnterior() {
        int pos = conductorEnLista();
        if (pos == 0) {
            JOptionPane.showMessageDialog(ventana, "No hay más conductores.", "Información", JOptionPane.INFORMATION_MESSAGE);
        } else {
            actualizarConductor(listaConductores.get(pos - 1));
            mostrarImagen();
        }
    }

    private void actualizarConductor(Conductor c) {
        this.conductorActual = c;
        ventana.nombre.setText(c.getNombreConductor());
        ventana.apellidos.setText(c.getApellidoConductor());
        ventana.numeroConductor.setText(String.valueOf(c.getNumeroConductor()));
    }

    private int conductorEnLista() {
        for (int i = 0; i < listaConductores.size(); i++) {
            if (listaConductores.get(i).getNumeroConductor() == conductorActual.getNumeroConductor()) return i;
        }
        return 0;
    }

    private String extraerExtension(String nombre) {
        int punto = nombre.lastIndexOf('.');
        return (punto >= 0) ? nombre.substring(punto + 1).toLowerCase() : "jpg";
    }
}
