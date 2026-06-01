package view.ruta;

import models.BCL;

import javax.swing.*;
import java.awt.*;

public class RutaInfoView extends JFrame {

    public JLabel lblIdBus, lblNumeroConductor, lblIdLugar, lblDiaSemana;
    public JLabel imagenRuta;
    public JLabel paginaActual;
    public JButton botonSiguiente, botonAnterior, botonEditar, botonCargarImg;
    public JPanel panelSur, panelBotones, panelBotonEditar, panelFoto;
    public BCL ruta;

    public RutaInfoView() {}

    public RutaInfoView(BCL ruta) {
        this.ruta = ruta;
        configurarVentana();
        iniciarComponentes();
        agregarTitulo();
        construirPanelCentral();
        construirPanelSur();
        setVisible(true);
    }

    private void configurarVentana() {
        setTitle("Información de la Ruta");
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
        imagenRuta = new JLabel();
        imagenRuta.setPreferredSize(new Dimension(220, 220));
        imagenRuta.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
    }

    private void agregarTitulo() {
        JLabel titulo = new JLabel("INFORMACIÓN DE LA RUTA", SwingConstants.CENTER);
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        add(titulo, BorderLayout.NORTH);
    }

    private void construirPanelCentral() {
        JPanel panelCentral = new JPanel(new BorderLayout(20, 20));
        panelCentral.setBackground(Color.WHITE);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        panelFoto = new JPanel(new BorderLayout());
        panelFoto.setBackground(Color.WHITE);
        panelFoto.add(imagenRuta, BorderLayout.CENTER);
        panelFoto.add(botonCargarImg, BorderLayout.SOUTH);
        panelCentral.add(panelFoto, BorderLayout.WEST);

        JPanel panelDatos = new JPanel(new GridLayout(4, 2, 15, 15));
        panelDatos.setBackground(Color.WHITE);

        panelDatos.add(new JLabel("ID Bus:"));
        lblIdBus = new JLabel(ruta.getIdBus());
        panelDatos.add(lblIdBus);

        panelDatos.add(new JLabel("Nº Conductor:"));
        lblNumeroConductor = new JLabel(String.valueOf(ruta.getNumeroConductor()));
        panelDatos.add(lblNumeroConductor);

        panelDatos.add(new JLabel("ID Lugar:"));
        lblIdLugar = new JLabel(String.valueOf(ruta.getIdLugar()));
        panelDatos.add(lblIdLugar);

        panelDatos.add(new JLabel("Día Semana:"));
        lblDiaSemana = new JLabel(ruta.getDiaSemana());
        panelDatos.add(lblDiaSemana);

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
