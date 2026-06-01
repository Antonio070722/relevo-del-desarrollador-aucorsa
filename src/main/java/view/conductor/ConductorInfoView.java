package view.conductor;

import models.Conductor;

import javax.swing.*;
import java.awt.*;

public class ConductorInfoView extends JFrame {

    public JLabel nombre;
    public JLabel apellidos;
    public JLabel numeroConductor;
    public JLabel paginaActual;
    public JLabel imagenConductor;
    public JLabel lblNombre;
    public JLabel lblApellidos;
    public JLabel lblNumero;
    public Conductor conductor;

    public JButton botonSiguiente;
    public JButton botonAnterior;
    public JButton botonEditar;
    public JButton botonCargarImg;

    public JPanel panelFoto;
    public JPanel panelSur;
    public JPanel panelPrincipal;
    public JPanel panelDatos;
    public JPanel panelBotones;
    public JPanel panelBotonEditar;

    public ConductorInfoView() {}

    public ConductorInfoView(Conductor conductor) {
        this.conductor = conductor;
        configurarVentana();
        iniciarComponentes();
        agregarTitulo();
        construirPanelCentral();
        construirPanelSur();
        setVisible(true);
    }

    private void configurarVentana() {
        setTitle("Información del Conductor");
        setSize(800, 436);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(new Color(245, 245, 245));
    }

    private void iniciarComponentes() {
        paginaActual = new JLabel("Páginas");
        imagenConductor = new JLabel();
        imagenConductor.setPreferredSize(new Dimension(220, 220));
        botonSiguiente = new JButton("Siguiente");
        botonAnterior = new JButton("Anterior");
        botonEditar = new JButton("Editar");
        botonCargarImg = new JButton("Actualizar imagen");
    }

    private void agregarTitulo() {
        JLabel titulo = new JLabel("INFORMACIÓN DEL CONDUCTOR", SwingConstants.CENTER);
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        add(titulo, BorderLayout.NORTH);
    }

    private void construirPanelCentral() {
        panelPrincipal = new JPanel(new BorderLayout(20, 20));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        panelPrincipal.setBackground(Color.WHITE);

        imagenConductor.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));

        panelFoto = new JPanel(new BorderLayout());
        panelFoto.setBackground(Color.WHITE);
        panelFoto.add(imagenConductor, BorderLayout.CENTER);
        panelFoto.add(botonCargarImg, BorderLayout.SOUTH);
        panelPrincipal.add(panelFoto, BorderLayout.WEST);

        panelDatos = new JPanel(new GridLayout(3, 2, 15, 15));
        panelDatos.setBackground(Color.WHITE);

        lblNombre = new JLabel("Nombre:");
        nombre = new JLabel(conductor.getNombreConductor());
        lblApellidos = new JLabel("Apellidos:");
        apellidos = new JLabel(conductor.getApellidoConductor());
        lblNumero = new JLabel("Número:");
        numeroConductor = new JLabel(String.valueOf(conductor.getNumeroConductor()));

        panelDatos.add(lblNombre);
        panelDatos.add(nombre);
        panelDatos.add(lblApellidos);
        panelDatos.add(apellidos);
        panelDatos.add(lblNumero);
        panelDatos.add(numeroConductor);

        panelPrincipal.add(panelDatos, BorderLayout.CENTER);
        add(panelPrincipal, BorderLayout.CENTER);
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
