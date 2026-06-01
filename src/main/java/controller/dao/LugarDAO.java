package controller.dao;

import models.Lugar;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LugarDAO extends DAO {
    /**
     * Obtiene todos los lugares de la BBDD y los devuelve
     * @return La lista con todos los lugares
     */
    public static ArrayList<Lugar> getLugares() {
        String consulta = "SELECT idplace, cp, city, site, imagen FROM Place";
        ArrayList<Lugar> lugares = new ArrayList<>();
        if (con == null) {
            System.out.println("Error: conexión no inicializada");
            return lugares;
        }
        try (PreparedStatement ps = con.prepareStatement(consulta);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Lugar lugar = new Lugar();
                lugar.setIdLugar(rs.getInt("idplace"));
                lugar.setCodigoPostal(rs.getString("cp"));
                lugar.setCiudad(rs.getString("city"));
                lugar.setLugar(rs.getString("site"));
                lugar.setImagen(rs.getString("imagen"));
                lugares.add(lugar);
            }
        } catch (SQLException e) {
            System.out.println("Error con la base de datos: " + e.getMessage());
        }
        return lugares;
    }

    /**
     * Introduce un nuevo Lugar en la BBDD
     * @param lugar El lugar que se introduce
     * @return El número de registros alterados (1 si éxito, 0 si fallo)
     */
    public static int insertarLugar(Lugar lugar) {
        String consulta = "INSERT INTO Place (idplace, cp, city, site) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(consulta)) {
            ps.setInt(1, lugar.getIdLugar());
            ps.setString(2, lugar.getCodigoPostal());
            ps.setString(3, lugar.getCiudad());
            ps.setString(4, lugar.getLugar());
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error con la base de datos: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Edita los datos de un lugar ya existente
     * @param lugar El lugar que se edita
     * @return El número de lugares modificados (1 si éxito, 0 si fallo)
     */
    public static int editarLugar(Lugar lugar) {
        String consulta = "UPDATE Place SET cp = ?, city = ?, site = ? WHERE idplace = ?";
        try (PreparedStatement ps = con.prepareStatement(consulta)) {
            ps.setString(1, lugar.getCodigoPostal());
            ps.setString(2, lugar.getCiudad());
            ps.setString(3, lugar.getLugar());
            ps.setInt(4, lugar.getIdLugar());
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error con la base de datos: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Actualiza únicamente el campo imagen de un lugar en la BBDD
     * @param lugar El lugar cuya imagen se actualiza
     * @return El número de registros modificados (1 si éxito, 0 si fallo)
     */
    public static int actualizarImagen(Lugar lugar) {
        String consulta = "UPDATE Place SET imagen = ? WHERE idplace = ?";
        try (PreparedStatement ps = con.prepareStatement(consulta)) {
            ps.setString(1, lugar.getImagen());
            ps.setInt(2, lugar.getIdLugar());
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error con la base de datos: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Elimina un lugar de la BBDD, junto con sus registros en la tabla intermedia BDP.
     * @param lugar El lugar que se elimina
     * @return El número total de cambios en la BBDD (registros eliminados) o -1 si error
     */
    public static int borrarLugar(Lugar lugar) {
        String consultaLugar = "DELETE FROM Place WHERE idplace = ?";
        String consultaBDP = "DELETE FROM BDP WHERE idplace = ?";
        int numeroCambios = 0;

        try {
            con.setAutoCommit(false);

            try (PreparedStatement psBDP = con.prepareStatement(consultaBDP)) {
                psBDP.setInt(1, lugar.getIdLugar());
                numeroCambios += psBDP.executeUpdate();
            }

            try (PreparedStatement psLugar = con.prepareStatement(consultaLugar)) {
                psLugar.setInt(1, lugar.getIdLugar());
                numeroCambios += psLugar.executeUpdate();
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
