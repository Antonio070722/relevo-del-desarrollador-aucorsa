package view;

import view.bus.BusPanel;
import view.conductor.ConductorPanel;
import view.ruta.RutaPanel;
import view.lugar.LugarPanel;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame{

    JPanel panelNorte = new JPanel();
    JPanel panelBotones = new JPanel();

    private JTabbedPane panelTablas = new JTabbedPane();
    private BusPanel busPanel = new BusPanel();
    private ConductorPanel conductorPanel = new ConductorPanel();
    private RutaPanel rutaPanel = new RutaPanel();
    private LugarPanel lugarPanel = new LugarPanel();

    JButton btnAdd = new JButton("Añadir");
    JButton btnDelete = new JButton("Borrar");
    JButton btnRefresh = new JButton("Refrescar");
    JButton btnModify = new JButton("Modificar");

    public MainView(){
        establecerPredeterminado();
        añadirPestañas();
        añadirBotones();
    }

    public JTabbedPane getPanelTablas() { return panelTablas; }
    public void setPanelTablas(JTabbedPane panelTablas) { this.panelTablas = panelTablas; }

    public BusPanel getBusPanel() { return busPanel; }
    public void setBusPanel(BusPanel busPanel) { this.busPanel = busPanel; }

    public ConductorPanel getConductorPanel() { return conductorPanel; }
    public void setConductorPanel(ConductorPanel conductorPanel) { this.conductorPanel = conductorPanel; }

    public JButton getBtnAdd() { return btnAdd; }
    public void setBtnAdd(JButton btnAdd) { this.btnAdd = btnAdd; }

    public JButton getBtnDelete() { return btnDelete; }
    public void setBtnDelete(JButton btnDelete) { this.btnDelete = btnDelete; }

    public JButton getBtnRefresh() { return btnRefresh; }
    public void setBtnRefresh(JButton btnRefresh) { this.btnRefresh = btnRefresh; }

    public JButton getBtnModify() { return btnModify; }
    public void setBtnModify(JButton btnModify) { this.btnModify = btnModify; }

    public RutaPanel getRutaPanel() { return rutaPanel; }
    public void setRutaPanel(RutaPanel rutaPanel) { this.rutaPanel = rutaPanel; }

    public LugarPanel getLugarPanel() { return lugarPanel; }
    public void setLugarPanel(LugarPanel lugarPanel) { this.lugarPanel = lugarPanel; }

    public JPanel getPanelNorte() { return panelNorte; }
    public void setPanelNorte(JPanel panelNorte) { this.panelNorte = panelNorte; }

    public JPanel getPanelBotones() { return panelBotones; }
    public void setPanelBotones(JPanel botoneriaPanel) { this.panelBotones = botoneriaPanel; }


    private void establecerPredeterminado(){
        this.setTitle("Aucorsa - Ventana Principal");
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setSize(800, 600);

        setLayout(new BorderLayout());
    }

    private void añadirPestañas(){
        panelTablas.add("Buses", busPanel);
        panelTablas.add("Conductores", conductorPanel);
        panelTablas.add("Rutas", rutaPanel);
        panelTablas.add("Lugares", lugarPanel);
        add(panelTablas, BorderLayout.CENTER);
    }

    private void añadirBotones(){
        panelNorte.setLayout(new BorderLayout());
        panelBotones.setLayout(new FlowLayout());

        panelBotones.add(btnAdd);
        panelBotones.add(btnDelete);
        panelBotones.add(btnModify);
        panelBotones.add(btnRefresh);

        panelNorte.add(panelBotones, BorderLayout.NORTH);
        add(panelNorte, BorderLayout.NORTH);
    }
}
