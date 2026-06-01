package controller.dao;

import models.Bus;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BusDAO extends DAO {
    /**
     * Obtiene todos los buses de la BBDD y los devuelve
     * @return La lista con todos los autobuses
     */
    public static ArrayList<Bus> getBuses() {
        String consulta = "SELECT register, type, license, imagen FROM Bus";
        ArrayList<Bus> buses = new ArrayList<>();
        if (con == null) {
            System.out.println("Error: conexión no inicializada");
            return buses;
        }
        try (PreparedStatement ps = con.prepareStatement(consulta);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Bus bus = new Bus();
                bus.setIdBus(rs.getString("register"));
                bus.setTipoBus(rs.getString("type"));
                bus.setLicenciaBus(rs.getString("license"));
                bus.setImagen(rs.getString("imagen"));
                buses.add(bus);
            }
        } catch (SQLException e) {
            System.out.println("Error con la base de datos: " + e.getMessage());
        }
        return buses;
    }

    /**
     * Introduce un nuevo Bus en la BBDD
     * @param bus El bus que se introduce
     * @return El número de registros alterados (1 si éxito, 0 si fallo)
     */
    public static int insertarBus(Bus bus) {
        String consulta = "INSERT INTO Bus (register, type, license) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(consulta)) {
            ps.setString(1, bus.getIdBus());
            ps.setString(2, bus.getTipoBus());
            ps.setString(3, bus.getLicenciaBus());
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error con la base de datos: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Edita los datos de un bus ya existente
     * @param bus El bus que se edita
     * @return El numero de registros modificados (1 si éxito, 0 si fallo)
     */
    public static int editarBus(Bus bus) {
        String consulta = "UPDATE Bus SET type = ?, license = ? WHERE register = ?";
        try (PreparedStatement ps = con.prepareStatement(consulta)) {
            ps.setString(1, bus.getTipoBus());
            ps.setString(2, bus.getLicenciaBus());
            ps.setString(3, bus.getIdBus());
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error con la base de datos: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Actualiza únicamente el campo imagen de un bus en la BBDD
     * @param bus El bus cuya imagen se actualiza
     * @return El número de registros modificados (1 si éxito, 0 si fallo)
     */
    public static int actualizarImagen(Bus bus) {
        String consulta = "UPDATE Bus SET imagen = ? WHERE register = ?";
        try (PreparedStatement ps = con.prepareStatement(consulta)) {
            ps.setString(1, bus.getImagen());
            ps.setString(2, bus.getIdBus());
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error con la base de datos: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Elimina un bus de la BBDD, junto con sus registros en la tabla intermedia BDP
     * @param bus El bus que se elimina
     * @return El número total de cambios en la BBDD (registros eliminados) o -1 si error
     */
    public static int borrarBus(Bus bus) {
        String consultaBus = "DELETE FROM Bus WHERE register = ?";
        String consultaBDP = "DELETE FROM BDP WHERE register = ?";
        int numeroCambios = 0;

        try {
            con.setAutoCommit(false);

            try (PreparedStatement psBDP = con.prepareStatement(consultaBDP)) {
                psBDP.setString(1, bus.getIdBus());
                numeroCambios += psBDP.executeUpdate();
            }

            try (PreparedStatement psBus = con.prepareStatement(consultaBus)) {
                psBus.setString(1, bus.getIdBus());
                numeroCambios += psBus.executeUpdate();
            }

            con.commit();
        } catch (SQLException e) {
            System.out.println("Error con la base de datos: " + e.getMessage());
            try {
                con.rollback();
            } catch (SQLException ex) {
                System.out.println("Error al hacer rollback: " + ex.getMessage());
            }
            return -1;
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Error al restaurar autocommit: " + e.getMessage());
            }
        }
        return numeroCambios;
    }
}
