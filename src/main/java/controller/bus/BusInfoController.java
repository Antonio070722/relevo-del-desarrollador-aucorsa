package controller.bus;

import controller.dao.BusDAO;
import models.Bus;
import view.bus.BusInfoView;

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

public class BusInfoController {

    private final BusInfoView ventana;
    private ArrayList<Bus> listaBuses;
    private Bus busActual;

    public BusInfoController(BusInfoView ventana) {
        this.ventana = ventana;
        this.listaBuses = BusDAO.getBuses();
        this.busActual = ventana.bus;
        this.busActual = listaBuses.get(busEnLista());
        mostrarImagen();

        ventana.botonSiguiente.addActionListener(e -> irSiguiente());
        ventana.botonAnterior.addActionListener(e -> irAnterior());
        ventana.botonEditar.addActionListener(e -> abrirEdicion());
        ventana.botonCargarImg.addActionListener(e -> seleccionarImagen());
    }

    private void irSiguiente() {
        int pos = busEnLista();
        if (pos == listaBuses.size() - 1) {
            JOptionPane.showMessageDialog(ventana, "No hay más buses.", "Información", JOptionPane.INFORMATION_MESSAGE);
        } else {
            actualizarBus(listaBuses.get(pos + 1));
            mostrarImagen();
        }
    }

    private void irAnterior() {
        int pos = busEnLista();
        if (pos == 0) {
            JOptionPane.showMessageDialog(ventana, "No hay más buses.", "Información", JOptionPane.INFORMATION_MESSAGE);
        } else {
            actualizarBus(listaBuses.get(pos - 1));
            mostrarImagen();
        }
    }

    private void abrirEdicion() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel formulario = new JPanel(new GridLayout(3, 2, 10, 10));

        JTextField txtTipo = new JTextField(busActual.getTipoBus(), 15);
        JTextField txtLicencia = new JTextField(busActual.getLicenciaBus(), 15);

        formulario.add(new JLabel("Matrícula:"));
        formulario.add(new JLabel(busActual.getIdBus()));
        formulario.add(new JLabel("Tipo:"));
        formulario.add(txtTipo);
        formulario.add(new JLabel("Licencia:"));
        formulario.add(txtLicencia);
        panel.add(formulario, BorderLayout.CENTER);

        int opcion = JOptionPane.showConfirmDialog(ventana, panel, "Editar Bus", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcion != JOptionPane.OK_OPTION) return;

        String nuevoTipo = txtTipo.getText().trim();
        String nuevaLicencia = txtLicencia.getText().trim();
        if (nuevoTipo.isEmpty() || nuevaLicencia.isEmpty()) {
            JOptionPane.showMessageDialog(ventana, "Todos los campos son obligatorios.", "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        busActual.setTipoBus(nuevoTipo);
        busActual.setLicenciaBus(nuevaLicencia);
        int filas = BusDAO.editarBus(busActual);
        if (filas > 0) {
            listaBuses.set(busEnLista(), busActual);
            actualizarBus(busActual);
            JOptionPane.showMessageDialog(ventana, "Bus modificado correctamente.", "Información", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(ventana, "Error al modificar el bus.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void seleccionarImagen() {
        JFileChooser selector = new JFileChooser();
        selector.setDialogTitle("Seleccionar imagen del bus");
        selector.setFileFilter(new FileNameExtensionFilter("Imágenes (JPG, PNG, GIF)", "jpg", "jpeg", "png", "gif"));

        if (selector.showOpenDialog(ventana) != JFileChooser.APPROVE_OPTION) return;

        File fichero = selector.getSelectedFile();
        URL urlDir = getClass().getResource("/images/bus/");
        if (urlDir == null) {
            JOptionPane.showMessageDialog(ventana, "No se encontró el directorio de imágenes.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String ext = extraerExtension(fichero.getName());
        String nombreFichero = "bus" + busActual.getIdBus() + "_custom." + ext;

        try {
            File destino = new File(new File(urlDir.toURI()), nombreFichero);
            Files.copy(fichero.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
            busActual.setImagen(nombreFichero);
            if (BusDAO.actualizarImagen(busActual) > 0) {
                listaBuses.set(busEnLista(), busActual);
                mostrarImagen();
                JOptionPane.showMessageDialog(ventana, "Imagen actualizada correctamente.", "Información", JOptionPane.INFORMATION_MESSAGE);
            } else {
                busActual.setImagen(null);
                JOptionPane.showMessageDialog(ventana, "La imagen se copió pero no se guardó en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (URISyntaxException | IOException ex) {
            JOptionPane.showMessageDialog(ventana, "Error al guardar la imagen: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarImagen() {
        String rutaImg = busActual.getImagen();
        if (rutaImg != null && !rutaImg.isBlank()) {
            cargarDesdeRecurso("/images/bus/" + rutaImg);
        } else {
            cargarDesdeRecurso("/images/bus/" + busActual.getIdBus() + ".jpg");
        }
    }

    private void cargarDesdeRecurso(String ruta) {
        URL url = getClass().getResource(ruta);
        if (url != null) {
            ventana.imagenBus.setIcon(new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(220, 220, Image.SCALE_SMOOTH)));
        } else {
            usarImagenPorDefecto();
        }
    }

    private void usarImagenPorDefecto() {
        URL urlDef = getClass().getResource("/images/default.jpg");
        if (urlDef != null) {
            ventana.imagenBus.setIcon(new ImageIcon(new ImageIcon(urlDef).getImage().getScaledInstance(220, 220, Image.SCALE_SMOOTH)));
        }
    }

    private void actualizarBus(Bus bus) {
        this.busActual = bus;
        ventana.lblMatricula.setText(bus.getIdBus());
        ventana.lblTipo.setText(bus.getTipoBus());
        ventana.lblLicencia.setText(bus.getLicenciaBus());
    }

    private int busEnLista() {
        for (int i = 0; i < listaBuses.size(); i++) {
            if (listaBuses.get(i).getIdBus().equals(busActual.getIdBus())) return i;
        }
        return 0;
    }

    private String extraerExtension(String nombre) {
        int punto = nombre.lastIndexOf('.');
        return (punto >= 0) ? nombre.substring(punto + 1).toLowerCase() : "jpg";
    }
}
