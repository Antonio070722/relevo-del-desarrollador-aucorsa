package controller.lugar;

import controller.dao.LugarDAO;
import models.Lugar;
import view.lugar.LugarInfoView;

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

public class LugarInfoController {

    private final LugarInfoView ventana;
    private ArrayList<Lugar> listaLugares;
    private Lugar lugarActual;

    public LugarInfoController(LugarInfoView ventana) {
        this.ventana = ventana;
        this.listaLugares = LugarDAO.getLugares();
        this.lugarActual = ventana.lugar;
        this.lugarActual = listaLugares.get(lugarEnLista());
        mostrarImagen();

        ventana.botonSiguiente.addActionListener(e -> irSiguiente());
        ventana.botonAnterior.addActionListener(e -> irAnterior());
        ventana.botonEditar.addActionListener(e -> abrirEdicion());
        ventana.botonCargarImg.addActionListener(e -> seleccionarImagen());
    }

    private void irSiguiente() {
        int pos = lugarEnLista();
        if (pos == listaLugares.size() - 1) {
            JOptionPane.showMessageDialog(ventana, "No hay más lugares.", "Información", JOptionPane.INFORMATION_MESSAGE);
        } else {
            actualizarLugar(listaLugares.get(pos + 1));
            mostrarImagen();
        }
    }

    private void irAnterior() {
        int pos = lugarEnLista();
        if (pos == 0) {
            JOptionPane.showMessageDialog(ventana, "No hay más lugares.", "Información", JOptionPane.INFORMATION_MESSAGE);
        } else {
            actualizarLugar(listaLugares.get(pos - 1));
            mostrarImagen();
        }
    }

    private void abrirEdicion() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel formulario = new JPanel(new GridLayout(4, 2, 10, 10));

        JTextField txtCP = new JTextField(lugarActual.getCodigoPostal(), 15);
        JTextField txtCiudad = new JTextField(lugarActual.getCiudad(), 15);
        JTextField txtSitio = new JTextField(lugarActual.getLugar(), 15);

        formulario.add(new JLabel("ID Lugar:"));
        formulario.add(new JLabel(String.valueOf(lugarActual.getIdLugar())));
        formulario.add(new JLabel("Código Postal:"));
        formulario.add(txtCP);
        formulario.add(new JLabel("Ciudad:"));
        formulario.add(txtCiudad);
        formulario.add(new JLabel("Lugar/Sitio:"));
        formulario.add(txtSitio);
        panel.add(formulario, BorderLayout.CENTER);

        int opcion = JOptionPane.showConfirmDialog(ventana, panel, "Editar Lugar", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcion != JOptionPane.OK_OPTION) return;

        String nuevoCP = txtCP.getText().trim();
        String nuevaCiudad = txtCiudad.getText().trim();
        String nuevoSitio = txtSitio.getText().trim();
        if (nuevoCP.isEmpty() || nuevaCiudad.isEmpty() || nuevoSitio.isEmpty()) {
            JOptionPane.showMessageDialog(ventana, "Todos los campos son obligatorios.", "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        lugarActual.setCodigoPostal(nuevoCP);
        lugarActual.setCiudad(nuevaCiudad);
        lugarActual.setLugar(nuevoSitio);
        int filas = LugarDAO.editarLugar(lugarActual);
        if (filas > 0) {
            listaLugares.set(lugarEnLista(), lugarActual);
            actualizarLugar(lugarActual);
            JOptionPane.showMessageDialog(ventana, "Lugar modificado correctamente.", "Información", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(ventana, "Error al modificar el lugar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void seleccionarImagen() {
        JFileChooser selector = new JFileChooser();
        selector.setDialogTitle("Seleccionar imagen del lugar");
        selector.setFileFilter(new FileNameExtensionFilter("Imágenes (JPG, PNG, GIF)", "jpg", "jpeg", "png", "gif"));

        if (selector.showOpenDialog(ventana) != JFileChooser.APPROVE_OPTION) return;

        File fichero = selector.getSelectedFile();
        URL urlDir = getClass().getResource("/images/place/");
        if (urlDir == null) {
            JOptionPane.showMessageDialog(ventana, "No se encontró el directorio de imágenes.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String ext = extraerExtension(fichero.getName());
        String nombreFichero = "place" + lugarActual.getIdLugar() + "_custom." + ext;

        try {
            File destino = new File(new File(urlDir.toURI()), nombreFichero);
            Files.copy(fichero.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
            lugarActual.setImagen(nombreFichero);
            if (LugarDAO.actualizarImagen(lugarActual) > 0) {
                listaLugares.set(lugarEnLista(), lugarActual);
                mostrarImagen();
                JOptionPane.showMessageDialog(ventana, "Imagen actualizada correctamente.", "Información", JOptionPane.INFORMATION_MESSAGE);
            } else {
                lugarActual.setImagen(null);
                JOptionPane.showMessageDialog(ventana, "La imagen se copió pero no se guardó en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (URISyntaxException | IOException ex) {
            JOptionPane.showMessageDialog(ventana, "Error al guardar la imagen: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarImagen() {
        String rutaImg = lugarActual.getImagen();
        if (rutaImg != null && !rutaImg.isBlank()) {
            cargarDesdeRecurso("/images/place/" + rutaImg);
        } else {
            cargarDesdeRecurso("/images/place/" + lugarActual.getIdLugar() + ".jpg");
        }
    }

    private void cargarDesdeRecurso(String ruta) {
        URL url = getClass().getResource(ruta);
        if (url != null) {
            ventana.imagenLugar.setIcon(new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(220, 220, Image.SCALE_SMOOTH)));
        } else {
            usarImagenPorDefecto();
        }
    }

    private void usarImagenPorDefecto() {
        URL urlDef = getClass().getResource("/images/default.jpg");
        if (urlDef != null) {
            ventana.imagenLugar.setIcon(new ImageIcon(new ImageIcon(urlDef).getImage().getScaledInstance(220, 220, Image.SCALE_SMOOTH)));
        }
    }

    private void actualizarLugar(Lugar l) {
        this.lugarActual = l;
        ventana.lblIdLugar.setText(String.valueOf(l.getIdLugar()));
        ventana.lblCodigoPostal.setText(l.getCodigoPostal());
        ventana.lblCiudad.setText(l.getCiudad());
        ventana.lblLugar.setText(l.getLugar());
    }

    private int lugarEnLista() {
        for (int i = 0; i < listaLugares.size(); i++) {
            if (listaLugares.get(i).getIdLugar() == lugarActual.getIdLugar()) return i;
        }
        return 0;
    }

    private String extraerExtension(String nombre) {
        int punto = nombre.lastIndexOf('.');
        return (punto >= 0) ? nombre.substring(punto + 1).toLowerCase() : "jpg";
    }
}
