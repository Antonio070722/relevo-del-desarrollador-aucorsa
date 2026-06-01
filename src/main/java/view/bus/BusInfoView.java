package view.bus;

import models.Bus;

import javax.swing.*;
import java.awt.*;

public class BusInfoView extends JFrame {

    public JLabel lblMatricula, lblTipo, lblLicencia;
    public JLabel imagenBus;
    public JLabel paginaActual;
    public JButton botonSiguiente, botonAnterior, botonEditar, botonCargarImg;
    public JPanel panelSur, panelBotones, panelBotonEditar, panelFoto;
    public Bus bus;

    public BusInfoView() {}

    public BusInfoView(Bus bus) {
        this.bus = bus;
        configurarVentana();
        iniciarComponentes();
        agregarTitulo();
        construirPanelCentral();
        construirPanelSur();
        setVisible(true);
    }

    private void configurarVentana() {
        setTitle("Información del Bus");
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
        imagenBus = new JLabel();
        imagenBus.setPreferredSize(new Dimension(220, 220));
        imagenBus.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
    }

    private void agregarTitulo() {
        JLabel titulo = new JLabel("INFORMACIÓN DEL BUS", SwingConstants.CENTER);
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        add(titulo, BorderLayout.NORTH);
    }

    private void construirPanelCentral() {
        JPanel panelCentral = new JPanel(new BorderLayout(20, 20));
        panelCentral.setBackground(Color.WHITE);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        panelFoto = new JPanel(new BorderLayout());
        panelFoto.setBackground(Color.WHITE);
        panelFoto.add(imagenBus, BorderLayout.CENTER);
        panelFoto.add(botonCargarImg, BorderLayout.SOUTH);
        panelCentral.add(panelFoto, BorderLayout.WEST);

        JPanel panelDatos = new JPanel();
        panelDatos.setLayout(new GridLayout(3, 2, 15, 15));
        panelDatos.setBackground(Color.WHITE);

        panelDatos.add(new JLabel("Matrícula:"));
        lblMatricula = new JLabel(bus.getIdBus());
        panelDatos.add(lblMatricula);

        panelDatos.add(new JLabel("Tipo:"));
        lblTipo = new JLabel(bus.getTipoBus());
        panelDatos.add(lblTipo);

        panelDatos.add(new JLabel("Licencia:"));
        lblLicencia = new JLabel(bus.getLicenciaBus());
        panelDatos.add(lblLicencia);

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
