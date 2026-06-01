package controller;

import controller.bus.BusController;
import controller.bus.BusInfoController;
import controller.conductor.ConductorController;
import controller.conductor.ConductorInfoController;
import controller.ruta.RutaController;
import controller.ruta.RutaInfoController;
import controller.lugar.LugarController;
import controller.lugar.LugarInfoController;
import models.Conductor;
import models.Bus;
import models.Lugar;
import models.BCL;
import view.MainView;
import view.conductor.ConductorInfoView;
import view.bus.BusInfoView;
import view.lugar.LugarInfoView;
import view.ruta.RutaInfoView;

import javax.swing.*;
import java.awt.event.*;

public class MainController {

    // Controladores de cada sección
    private final BusController busController;
    private final ConductorController conductorController;
    private final RutaController rutaController;
    private final LugarController lugarController;
    private final MainView mainView;

    public MainController(MainView mainView) {
        this.mainView = mainView;
        busController = new BusController(mainView.getBusPanel());
        conductorController = new ConductorController(mainView.getConductorPanel());
        rutaController = new RutaController(mainView.getRutaPanel());
        lugarController = new LugarController(mainView.getLugarPanel());

        mainView.getBtnRefresh().addActionListener(e -> refrescarTabla());
        mainView.getBtnDelete().addActionListener(e -> eliminarTabla());
        mainView.getBtnAdd().addActionListener(e -> agregarTabla());
        mainView.getBtnModify().addActionListener(e -> modificarTabla());

        // Doble clic en tabla de conductores → abre ventana de información
        JTable tablaConductor = mainView.getConductorPanel().getTablaVista();
        tablaConductor.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int fila = tablaConductor.getSelectedRow();
                    Conductor conductor = new Conductor(
                            (int) tablaConductor.getModel().getValueAt(fila, 0),
                            tablaConductor.getModel().getValueAt(fila, 1).toString(),
                            tablaConductor.getModel().getValueAt(fila, 2).toString()
                    );
                    ConductorInfoView vista = new ConductorInfoView(conductor);
                    new ConductorInfoController(vista);
                }
            }
        });

        // Doble clic en tabla de buses → abre ventana de información
        JTable tablaBus = mainView.getBusPanel().getTablaVista();
        tablaBus.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int fila = tablaBus.getSelectedRow();
                    if (fila == -1) return;
                    Bus bus = new Bus();
                    bus.setIdBus(tablaBus.getModel().getValueAt(fila, 0).toString());
                    bus.setTipoBus(tablaBus.getModel().getValueAt(fila, 1).toString());
                    bus.setLicenciaBus(tablaBus.getModel().getValueAt(fila, 2).toString());
                    BusInfoView vista = new BusInfoView(bus);
                    new BusInfoController(vista);
                }
            }
        });

        // Doble clic en tabla de lugares → abre ventana de información
        JTable tablaLugar = mainView.getLugarPanel().getTablaVista();
        tablaLugar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int fila = tablaLugar.getSelectedRow();
                    if (fila == -1) return;
                    Lugar lugar = new Lugar();
                    lugar.setIdLugar((int) tablaLugar.getModel().getValueAt(fila, 0));
                    lugar.setCodigoPostal(tablaLugar.getModel().getValueAt(fila, 1).toString());
                    lugar.setCiudad(tablaLugar.getModel().getValueAt(fila, 2).toString());
                    lugar.setLugar(tablaLugar.getModel().getValueAt(fila, 3).toString());
                    LugarInfoView vista = new LugarInfoView(lugar);
                    new LugarInfoController(vista);
                }
            }
        });

        // Doble clic en tabla de rutas → abre ventana de información
        JTable tablaRuta = mainView.getRutaPanel().getTablaVista();
        tablaRuta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int fila = tablaRuta.getSelectedRow();
                    if (fila == -1) return;
                    BCL ruta = new BCL();
                    ruta.setIdBus(tablaRuta.getModel().getValueAt(fila, 0).toString());
                    ruta.setNumeroConductor((int) tablaRuta.getModel().getValueAt(fila, 1));
                    ruta.setIdLugar((int) tablaRuta.getModel().getValueAt(fila, 2));
                    ruta.setDiaSemana(tablaRuta.getModel().getValueAt(fila, 3).toString());
                    RutaInfoView vista = new RutaInfoView(ruta);
                    new RutaInfoController(vista);
                }
            }
        });

        // Confirmación al cerrar la aplicación
        mainView.addWindowListener(new WindowListener() {
            @Override public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {
                int resultado = JOptionPane.showConfirmDialog(
                        mainView,
                        "¿Estás seguro de que te marchas ya?",
                        "¿Seguro?",
                        JOptionPane.YES_NO_OPTION
                );
                if (resultado == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
            @Override public void windowClosed(WindowEvent e) {}
            @Override public void windowIconified(WindowEvent e) {}
            @Override public void windowDeiconified(WindowEvent e) {}
            @Override public void windowActivated(WindowEvent e) {}
            @Override public void windowDeactivated(WindowEvent e) {}
        });
    }

    private void agregarTabla() {
        int idx = mainView.getPanelTablas().getSelectedIndex();
        String tab = mainView.getPanelTablas().getTitleAt(idx);
        switch (tab) {
            case "Buses":      busController.agregarBus();               break;
            case "Conductores": conductorController.agregarConductor();  break;
            case "Rutas":      rutaController.agregarRuta();             break;
            case "Lugares":    lugarController.agregarLugar();           break;
        }
    }

    private void modificarTabla() {
        int idx = mainView.getPanelTablas().getSelectedIndex();
        String tab = mainView.getPanelTablas().getTitleAt(idx);
        switch (tab) {
            case "Buses":      busController.modificarBus();             break;
            case "Conductores": conductorController.modificarConductor(); break;
            case "Rutas":      rutaController.modificarRuta();           break;
            case "Lugares":    lugarController.modificarLugar();         break;
        }
    }

    private void eliminarTabla() {
        int idx = mainView.getPanelTablas().getSelectedIndex();
        String tab = mainView.getPanelTablas().getTitleAt(idx);
        switch (tab) {
            case "Buses":      busController.eliminarBus();              break;
            case "Conductores": conductorController.eliminarConductor(); break;
            case "Rutas":      rutaController.eliminarRuta();            break;
            case "Lugares":    lugarController.eliminarLugar();          break;
        }
    }

    private void refrescarTabla() {
        // Recarga los datos de la pestaña activa desde la base de datos
        int idx = mainView.getPanelTablas().getSelectedIndex();
        String tab = mainView.getPanelTablas().getTitleAt(idx);
        switch (tab) {
            case "Buses":      busController.cargarBuses();              break;
            case "Conductores": conductorController.cargarConductores(); break;
            case "Rutas":      rutaController.cargarRutas();             break;
            case "Lugares":    lugarController.cargarLugares();          break;
        }
    }
}
