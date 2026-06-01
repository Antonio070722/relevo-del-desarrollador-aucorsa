package app;

import controller.dao.*;
import models.*;
import utils.Utils;

import java.util.ArrayList;

// Aucorsa en Consola
public class Main {
    public static void main() {
        // Sincronizamos datos con la BBDD
        ArrayList<Bus> buses = BusDAO.getBuses();
        ConductorDAO conductorDAO = new ConductorDAO();
        ArrayList<Conductor> conductores = conductorDAO.getConductores();
        ArrayList<Lugar> lugares = LugarDAO.getLugares();
        ArrayList<BCL> relacionesTernarias = BCLDAO.getBCL();

        int opcion = 0;
        do{
            try {
                opcion = Utils.menu();
                switch (opcion) {
                    case 0 -> System.out.println("Saliendo...");
                    case 1 -> Utils.insertarBus(buses);
                    case 2 -> Utils.insertarConductor(conductores);
                    case 3 -> Utils.insertarLugar(lugares);
                    case 4 -> Utils.editarRuta(relacionesTernarias);
                    case 5 -> Utils.eliminarRuta(relacionesTernarias);
                    case 6 -> Utils.verConductor(conductores);
                    case 7 -> Utils.verDiaRuta(relacionesTernarias, lugares);
                    case 8 -> Utils.verConductoresBus(relacionesTernarias, conductores);
                    default -> System.out.println("Opción no válida");
                }
            }catch (Exception e){
                System.out.println("Error: " + e.getMessage());
            }
        } while (opcion != 0);
    }
}