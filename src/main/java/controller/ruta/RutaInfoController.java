package controller.ruta;

import controller.dao.BCLDAO;
import models.BCL;
import view.ruta.RutaInfoView;

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

public class RutaInfoController {

    private final RutaInfoView ventana;
    private ArrayList<BCL> listaRutas;
    private BCL rutaActual;

    public RutaInfoController(RutaInfoView ventana) {
        this.ventana = ventana;
        this.listaRutas = BCLDAO.getBCL();
        this.rutaActual = ventana.ruta;
        this.rutaActual = listaRutas.get(rutaEnLista());
        mostrarImagen();

        ventana.botonSiguiente.addActionListener(e -> irSiguiente());
        ventana.botonAnterior.addActionListener(e -> irAnterior());
        ventana.botonEditar.addActionListener(e -> abrirEdicion());
        ventana.botonCargarImg.addActionListener(e -> seleccionarImagen());
    }

    private void irSiguiente() {
        int pos = rutaEnLista();
        if (pos == listaRutas.size() - 1) {
            JOptionPane.showMessageDialog(ventana, "No hay más rutas.", "Información", JOptionPane.INFORMATION_MESSAGE);
        } else {
            actualizarRuta(listaRutas.get(pos + 1));
            mostrarImagen();
        }
    }

    private void irAnterior() {
        int pos = rutaEnLista();
        if (pos == 0) {
            JOptionPane.showMessageDialog(ventana, "No hay más rutas.", "Información", JOptionPane.INFORMATION_MESSAGE);
        } else {
            actualizarRuta(listaRutas.get(pos - 1));
            mostrarImagen();
        }
    }

    private void abrirEdicion() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel formulario = new JPanel(new GridLayout(4, 2, 10, 10));

        String[] diasSemana = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        JComboBox<String> comboDia = new JComboBox<>(diasSemana);
        comboDia.setSelectedItem(rutaActual.getDiaSemana());

        formulario.add(new JLabel("ID Bus:"));
        formulario.add(new JLabel(rutaActual.getIdBus()));
        formulario.add(new JLabel("Nº Conductor:"));
        formulario.add(new JLabel(String.valueOf(rutaActual.getNumeroConductor())));
        formulario.add(new JLabel("ID Lugar:"));
        formulario.add(new JLabel(String.valueOf(rutaActual.getIdLugar())));
        formulario.add(new JLabel("Día Semana:"));
        formulario.add(comboDia);
        panel.add(formulario, BorderLayout.CENTER);

        int opcion = JOptionPane.showConfirmDialog(ventana, panel, "Editar Ruta", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcion != JOptionPane.OK_OPTION) return;

        rutaActual.setDiaSemana((String) comboDia.getSelectedItem());
        int filas = BCLDAO.editarBCL(rutaActual);
        if (filas > 0) {
            listaRutas.set(rutaEnLista(), rutaActual);
            actualizarRuta(rutaActual);
            JOptionPane.showMessageDialog(ventana, "Ruta modificada correctamente.", "Información", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(ventana, "Error al modificar la ruta.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void seleccionarImagen() {
        JFileChooser selector = new JFileChooser();
        selector.setDialogTitle("Seleccionar imagen de la ruta");
        selector.setFileFilter(new FileNameExtensionFilter("Imágenes (JPG, PNG, GIF)", "jpg", "jpeg", "png", "gif"));

        if (selector.showOpenDialog(ventana) != JFileChooser.APPROVE_OPTION) return;

        File fichero = selector.getSelectedFile();
        URL urlDir = getClass().getResource("/images/route/");
        if (urlDir == null) {
            JOptionPane.showMessageDialog(ventana, "No se encontró el directorio de imágenes.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String ext = extraerExtension(fichero.getName());
        String nombreFichero = rutaActual.getIdBus() + "_" + rutaActual.getNumeroConductor() + "_" + rutaActual.getIdLugar() + "_custom." + ext;

        try {
            File destino = new File(new File(urlDir.toURI()), nombreFichero);
            Files.copy(fichero.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
            rutaActual.setImagen(nombreFichero);
            if (BCLDAO.actualizarImagen(rutaActual) > 0) {
                listaRutas.set(rutaEnLista(), rutaActual);
                mostrarImagen();
                JOptionPane.showMessageDialog(ventana, "Imagen actualizada correctamente.", "Información", JOptionPane.INFORMATION_MESSAGE);
            } else {
                rutaActual.setImagen(null);
                JOptionPane.showMessageDialog(ventana, "La imagen se copió pero no se guardó en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (URISyntaxException | IOException ex) {
            JOptionPane.showMessageDialog(ventana, "Error al guardar la imagen: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarImagen() {
        String rutaImg = rutaActual.getImagen();
        if (rutaImg != null && !rutaImg.isBlank()) {
            cargarDesdeRecurso("/images/route/" + rutaImg);
        } else {
            String nombre = rutaActual.getIdBus() + "_" + rutaActual.getNumeroConductor() + "_" + rutaActual.getIdLugar() + ".jpg";
            cargarDesdeRecurso("/images/route/" + nombre);
        }
    }

    private void cargarDesdeRecurso(String ruta) {
        URL url = getClass().getResource(ruta);
        if (url != null) {
            ventana.imagenRuta.setIcon(new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(220, 220, Image.SCALE_SMOOTH)));
        } else {
            usarImagenPorDefecto();
        }
    }

    private void usarImagenPorDefecto() {
        URL urlDef = getClass().getResource("/images/default.jpg");
        if (urlDef != null) {
            ventana.imagenRuta.setIcon(new ImageIcon(new ImageIcon(urlDef).getImage().getScaledInstance(220, 220, Image.SCALE_SMOOTH)));
        }
    }

    private void actualizarRuta(BCL r) {
        this.rutaActual = r;
        ventana.lblIdBus.setText(r.getIdBus());
        ventana.lblNumeroConductor.setText(String.valueOf(r.getNumeroConductor()));
        ventana.lblIdLugar.setText(String.valueOf(r.getIdLugar()));
        ventana.lblDiaSemana.setText(r.getDiaSemana());
    }

    private int rutaEnLista() {
        for (int i = 0; i < listaRutas.size(); i++) {
            BCL b = listaRutas.get(i);
            if (b.getIdBus().equals(rutaActual.getIdBus())
                    && b.getNumeroConductor() == rutaActual.getNumeroConductor()
                    && b.getIdLugar() == rutaActual.getIdLugar()) return i;
        }
        return 0;
    }

    private String extraerExtension(String nombre) {
        int punto = nombre.lastIndexOf('.');
        return (punto >= 0) ? nombre.substring(punto + 1).toLowerCase() : "jpg";
    }
}
