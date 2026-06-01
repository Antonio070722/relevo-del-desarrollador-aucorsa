package utils;

import controller.dao.BCLDAO;
import controller.dao.BusDAO;
import controller.dao.ConductorDAO;
import controller.dao.LugarDAO;
import models.BCL;
import models.Bus;
import models.Conductor;
import models.Lugar;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Utils {
    /**
     * Este metodo es el encargado de mostrar el menú y devolver la opción que elige el usuario
     * @return La opción elegida por el usuario
     */

    public static int menu(){
        System.out.println("0.- Salir \n" +
                "1.- Insertar Autobus \n" +
                "2.- Instertar Conductor \n" +
                "3.- Insertar Lugar \n" +
                "4.- Actualizar dia de la semana de una ruta \n" +
                "5.- Eliminar ruta \n" +
                "6.- Ver conductor \n" +
                "7.- Dia de la ruta \n" +
                "8.- Ver conductores que conducen un autobús ");
        return pedirNumeroUsuario("Seleccione una opción: ");
    }

    /**
     * Este metodo es el encargado de pedir los datos necesarios al usuario e insertar un nuevo bus en la BD
     * @param buses La lista de buses local
     */
    public static void insertarBus(ArrayList<Bus> buses){
        String idNuevoBus = pedirTextoUsuario("Introduce ID del nuevo Bus (BXXX):", "^B\\d{3}");
        String tipoNuevoBus = pedirTextoUsuario("Introduce el tipo del nuevo Bus (Urbano, Interurbano, Turismo, Escolar): ", "^(Urbano|Interurbano|Turismo|Escolar)$");
        String licenciaNuevoBus = pedirTextoUsuario("Introduce la licencia del nuevo Bus (LICXXX): ", "LIC\\d{3}");
        Bus nuevoBus = new Bus(idNuevoBus, tipoNuevoBus, licenciaNuevoBus);
        if (BusDAO.insertarBus(nuevoBus) != 0) buses.add(nuevoBus);
    }

    /**
     * Este metodo es el encargado de pedir los datos necesarios al usuario e insertar un nuevo conductor en la BD
     * @param conductores La lista de conductores local
     */
    public static void insertarConductor(ArrayList<Conductor> conductores) {
        int numeroNuevoConductor = pedirNumeroUsuario("Introduzca el numero de licencia para el nuevo conductor: ");
        String nombreNuevoConductor = pedirTextoUsuario("Introduce el nombre del nuevo conductor: ");
        String apellidoNuevoConductor = pedirTextoUsuario("Introduce el apellido del nuevo conductor: ");
        Conductor nuevoConductor = new Conductor(numeroNuevoConductor, nombreNuevoConductor, apellidoNuevoConductor);
        if (ConductorDAO.insertarConductor(nuevoConductor) != 0) conductores.add(nuevoConductor);
    }

    /**
     * Este metodo es el encargado de pedir los datos necesarios al usuario e insertar un nuevo lugar en la BD
     * @param lugares La lista de lugares local
     */
    public static void insertarLugar(ArrayList<Lugar> lugares) {
        int idNuevoLugar = pedirNumeroUsuario("Introduce el ID del nuevo lugar: ");
        String codigoPostalNuevoLugar =  pedirTextoUsuario("Introduce el codigo postal del nuevo lugar: ");
        String ciudadNuevoLugar = pedirTextoUsuario("Introduce la ciudad del nuevo lugar: ");
        String lugarNuevo = pedirTextoUsuario("Introduce el nombre del nuevo lugar: ");
        Lugar nuevoLugar = new Lugar(idNuevoLugar, codigoPostalNuevoLugar,  ciudadNuevoLugar, lugarNuevo);
        if (LugarDAO.insertarLugar(nuevoLugar) != 0) lugares.add(nuevoLugar);
    }

    /**
     * Este metodo modifica el día de la semana de una ruta
     * @param bcls Lista con todas las relaciones ternarias
     */
    public static void editarRuta(ArrayList<BCL> bcls){
        String idBus = pedirTextoUsuario("Introduce ID del Bus que pertenece a la ruta (BXXX):", "^B\\d{3}");
        int numeroConductor = pedirNumeroUsuario("Introduzca el numero de licencia del conductor de la ruta: ");
        int idLugar = pedirNumeroUsuario("Introduce el ID del lugar de la ruta: ");
        String diaSemana = pedirTextoUsuario("Introduce el nuevo dia de la semana: ");
        String diaSemanaAntiguo = "";

        for (BCL bcl : bcls) {
            if (bcl.getIdBus().equals(idBus) && bcl.getIdLugar() == idLugar && bcl.getNumeroConductor() == numeroConductor) {
                diaSemanaAntiguo = bcl.getDiaSemana();
                bcl.setDiaSemana(diaSemana);
                if (BCLDAO.editarBCL(bcl) == 0) bcl.setDiaSemana(diaSemanaAntiguo);
            }
        }

    }

    /**
     * Este metodo es el encargado de pedir los datos necesarios al usuario y borrar una ruta de la BD
     * @param bcls La lista de claves ternarias local
     */
    public static void eliminarRuta(ArrayList<BCL> bcls) throws SQLException {
        String idBus = pedirTextoUsuario("Introduce ID del Bus que pertenece a la ruta (BXXX):", "^B\\d{3}");
        int numeroConductor = pedirNumeroUsuario("Introduzca el numero de licencia del conductor de la ruta: ");
        int idLugar = pedirNumeroUsuario("Introduce el ID del lugar de la ruta: ");
        for (BCL bcl : bcls) {
            if (bcl.getIdBus().equals(idBus) && bcl.getIdLugar() == idLugar && bcl.getNumeroConductor() == numeroConductor){
                if (BCLDAO.borrarBCL(bcl) != 0) bcls.remove(bcl);
            }
        }
    }

    /**
     * Este metodo es el encargado de pedir un ID de conductor y buscarlo para mostrar la información
     * @param conductores La lista de conductores local
     */
    public static void verConductor(ArrayList<Conductor> conductores) {
        int numeroConductor = pedirNumeroUsuario("Introduzca el numero de licencia del conductor: ");
        for (Conductor conductor : conductores) {
            if (conductor.getNumeroConductor() == numeroConductor){
                conductor.mostrarInfo();
            }
        }
    }

    /**
     * Este metodo es el encargado de pedir una ciudad y mostrar el dia que se hace su ruta
     * @param bcls Lista local de todas las relaciones ternarias
     * @param lugares Lista local de todos los lugares
     */
    public static void verDiaRuta(ArrayList<BCL> bcls, ArrayList<Lugar> lugares){
        String ciudad = pedirTextoUsuario("Introduce la ciudad: ");
        for  (Lugar lugar : lugares) {
            if (lugar.getCiudad().equals(ciudad)){
                for (BCL bcl : bcls) {
                    if (lugar.getIdLugar() == bcl.getIdLugar()){
                        System.out.println("Dia de la semana: " + bcl.getDiaSemana());
                    }
                }
            }
        }
    }

    /**
     * Este metodo es el encargado de pedir un bus y mostrar qué conductor (o conductores) lo conducen
     * @param bcls Lista local de todas las relaciones ternarias
     * @param conductores Lista local de todos los conductores
     */
    public static void verConductoresBus(ArrayList<BCL> bcls, ArrayList<Conductor> conductores) {
        String idBus = pedirTextoUsuario("Introduce ID del Bus que pertenece a la ruta (BXXX):", "^B\\d{3}");
        for (BCL bcl : bcls) {
            if (bcl.getIdBus().equals(idBus)){
                for (Conductor conductor : conductores) {
                    if (bcl.getNumeroConductor() == conductor.getNumeroConductor()) {
                        conductor.mostrarInfo();
                    }
                }
            }
        }

    }

    /**
     * Este metodo es el encargado de pedir un número al usuario
     * @param mensaje El mensaje que se muestra
     * @return El número introducido por el usuario
     */
    public static int pedirNumeroUsuario(String mensaje){
        Scanner sc = new Scanner(System.in);
        System.out.print(mensaje);
        return sc.nextInt();
    }

    /**
     * Este metodo es el encargado de pedir una cadena de texto al usuario
     * @param mensaje El mensaje que se muestra por pantalla
     * @return El texto introducido por el usuario
     */
    public static String pedirTextoUsuario(String mensaje){
        Scanner sc = new Scanner(System.in);
        System.out.print(mensaje);
        return sc.nextLine();
    }

    /**
     * Este metodo (sobrecargado) es el encargado de pedir una cadena de texto al usuario la cual debe cumplir un regex
     * Se le pedirá al usuario la cadena de nuevo hasta que cumpla con el regex establecido
     * @param mensaje El mensaje que se muestra
     * @param regex El regex que debe cumplir la cadena de texto
     * @return El texto introducido por el usuario cumpliendo el regex
     */
    public static String pedirTextoUsuario(String mensaje, String regex){
        Scanner sc = new Scanner(System.in);
        String textoUsuario;
        do{
            System.out.print(mensaje);
            textoUsuario = sc.nextLine();
        }
        while(!textoUsuario.matches(regex));
        return textoUsuario;
    }

}
