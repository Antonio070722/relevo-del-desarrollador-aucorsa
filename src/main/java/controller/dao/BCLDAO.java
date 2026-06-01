package controller.dao;

import models.BCL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BCLDAO extends DAO {
    /**
     * Obtiene todos los BCL de la BBDD y los devuelve
     * @return La lista con todos los registros BDP
     */
    public static ArrayList<BCL> getBCL() {
        String consulta = "SELECT register, numdriver, idplace, day_of_week, imagen FROM BDP";
        ArrayList<BCL> bcls = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(consulta);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                BCL bcl = new BCL();
                bcl.setIdBus(rs.getString("register"));
                bcl.setNumeroConductor(rs.getInt("numdriver"));
                bcl.setIdLugar(rs.getInt("idplace"));
                bcl.setDiaSemana(rs.getString("day_of_week"));
                bcl.setImagen(rs.getString("imagen"));
                bcls.add(bcl);
            }
            return bcls;
        } catch (SQLException e) {
            System.out.println("Error con la base de datos: " + e.getMessage());
        }
        return null;
    }

    /**
     * Introduce un nuevo BCL en la BBDD
     * @param bcl El registro que se introduce
     * @return El número de registros alterados
     */
    public static int insertarBCL(BCL bcl) {
        String consulta = "INSERT INTO BDP (register, numdriver, idplace, day_of_week) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement ps = con.prepareStatement(consulta);
            ps.setString(1, bcl.getIdBus());
            ps.setInt(2, bcl.getNumeroConductor());
            ps.setInt(3, bcl.getIdLugar());
            ps.setString(4, bcl.getDiaSemana());
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error con la base de datos: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Edita los datos de un BCL ya existente
     * @param bcl El BCL que se edita
     * @return El número de registros modificados
     */
    public static int editarBCL(BCL bcl) {
        String consulta = "UPDATE BDP SET day_of_week = ? WHERE idplace = ? AND numdriver = ? AND register = ?";
        try {
            PreparedStatement ps = con.prepareStatement(consulta);
            ps.setString(1, bcl.getDiaSemana());
            ps.setInt(2, bcl.getIdLugar());
            ps.setInt(3, bcl.getNumeroConductor());
            ps.setString(4, bcl.getIdBus());
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error con la base de datos: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Actualiza únicamente el campo imagen de un BCL en la BBDD
     * @param bcl El registro cuya imagen se actualiza
     * @return El número de registros modificados (1 si éxito, 0 si fallo)
     */
    public static int actualizarImagen(BCL bcl) {
        String consulta = "UPDATE BDP SET imagen = ? WHERE register = ? AND numdriver = ? AND idplace = ?";
        try {
            PreparedStatement ps = con.prepareStatement(consulta);
            ps.setString(1, bcl.getImagen());
            ps.setString(2, bcl.getIdBus());
            ps.setInt(3, bcl.getNumeroConductor());
            ps.setInt(4, bcl.getIdLugar());
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error con la base de datos: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Elimina un BCL de la BBDD
     * @param bcl El BCL que se elimina
     * @return El número de cambios en la BBDD
     */
    public static int borrarBCL(BCL bcl) throws SQLException {
        String consulta = "DELETE FROM BDP WHERE idplace = ? AND numdriver = ? AND register = ?";
        int numeroCambios = 0;

        try {
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(consulta)) {
                ps.setInt(1, bcl.getIdLugar());
                ps.setInt(2, bcl.getNumeroConductor());
                ps.setString(3, bcl.getIdBus());
                numeroCambios += ps.executeUpdate();
            }

            con.commit();
        } catch (SQLException e) {
            System.out.println("Error con la base de datos: " + e.getMessage());
            con.rollback();
        } finally {
            con.setAutoCommit(true);
        }
        return numeroCambios;
    }
}
