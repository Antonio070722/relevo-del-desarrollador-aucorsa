package view.bus;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BusPanel extends JPanel {

    private JTable tablaVista = new JTable();
    private DefaultTableModel modeloTabla = new DefaultTableModel();

    public BusPanel(){
        setLayout(new BorderLayout());
        modeloTabla = new DefaultTableModel(new String[] {"ID", "Tipo", "Licencia"}, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaVista = new JTable(modeloTabla);
        tablaVista.setFillsViewportHeight(true);
        tablaVista.setRowSelectionAllowed(true);
        tablaVista.setColumnSelectionAllowed(false);
        tablaVista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(new JScrollPane(tablaVista), BorderLayout.CENTER);
    }


    public JTable getTablaVista() {
        return tablaVista;
    }
    public void setTablaVista(JTable tablaVista) {
        this.tablaVista = tablaVista;
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }
    public void setModeloTabla(DefaultTableModel modeloTabla) {
        this.modeloTabla = modeloTabla;
    }
}
