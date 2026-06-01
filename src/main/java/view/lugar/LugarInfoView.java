package view.lugar;

import models.Lugar;

import javax.swing.*;
import java.awt.*;

public class LugarInfoView extends JFrame {

    public JLabel lblIdLugar, lblCodigoPostal, lblCiudad, lblLugar;
    public JLabel imagenLugar;
    public JLabel paginaActual;
    public JButton botonSiguiente, botonAnterior, botonEditar, botonCargarImg;
    public JPanel panelSur, panelBotones, panelBotonEditar, panelFoto;
    public Lugar lugar;

    public LugarInfoView() {}

    public LugarInfoView(Lugar lugar) {
        this.lugar = lugar;
        configurarVentana();
        iniciarComponentes();
        agregarTitulo();
        construirPanelCentral();
        construirPanelSur();
        setVisible(true);
    }

    private void configurarVentana() {
        setTitle("Información del Lugar");
        setSize(800, 436);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(new Color(245, 245, 245));
    }

    private void iniciarComponentes() {
        paginaActual = new JLabel("Páginas");
        botonSiguiente = new JButton("Siguiente");
        botonAnterior = new JButton("Anterior");
        botonEditar = new JButton("Editar");
        botonCargarImg = new JButton("Actualizar imagen");
        imagenLugar = new JLabel();
        imagenLugar.setPreferredSize(new Dimension(220, 220));
        imagenLugar.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
    }

    private void agregarTitulo() {
        JLabel titulo = new JLabel("INFORMACIÓN DEL LUGAR", SwingConstants.CENTER);
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        add(titulo, BorderLayout.NORTH);
    }

    private void construirPanelCentral() {
        JPanel panelCentral = new JPanel(new BorderLayout(20, 20));
        panelCentral.setBackground(Color.WHITE);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        panelFoto = new JPanel(new BorderLayout());
        panelFoto.setBackground(Color.WHITE);
        panelFoto.add(imagenLugar, BorderLayout.CENTER);
        panelFoto.add(botonCargarImg, BorderLayout.SOUTH);
        panelCentral.add(panelFoto, BorderLayout.WEST);

        JPanel panelDatos = new JPanel(new GridLayout(4, 2, 15, 15));
        panelDatos.setBackground(Color.WHITE);

        panelDatos.add(new JLabel("ID Lugar:"));
        lblIdLugar = new JLabel(String.valueOf(lugar.getIdLugar()));
        panelDatos.add(lblIdLugar);

        panelDatos.add(new JLabel("Código Postal:"));
        lblCodigoPostal = new JLabel(lugar.getCodigoPostal());
        panelDatos.add(lblCodigoPostal);

        panelDatos.add(new JLabel("Ciudad:"));
        lblCiudad = new JLabel(lugar.getCiudad());
        panelDatos.add(lblCiudad);

        panelDatos.add(new JLabel("Lugar/Sitio:"));
        lblLugar = new JLabel(lugar.getLugar());
        panelDatos.add(lblLugar);

        panelCentral.add(panelDatos, BorderLayout.CENTER);
        add(panelCentral, BorderLayout.CENTER);
    }

    private void construirPanelSur() {
        panelSur = new JPanel(new BorderLayout());
        panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(botonAnterior);
        panelBotones.add(paginaActual);
        panelBotones.add(botonSiguiente);
        panelSur.add(panelBotones, BorderLayout.CENTER);

        panelBotonEditar = new JPanel(new FlowLayout());
        panelBotonEditar.add(botonEditar);
        panelSur.add(panelBotonEditar, BorderLayout.EAST);

        add(panelSur, BorderLayout.SOUTH);
    }
}
