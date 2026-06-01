package controller.dao;

import controller.connection.ConexionBBDD;
import models.Conductor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ConductorDAO {


    /** Consulta para obtener todos los conductores */
    private static final String SQL_GET_ALL       = "SELECT * FROM Driver";

    /** Consulta para buscar un conductor por su número identificador. */
    private static final String SQL_FIND_BY_ID    = "SELECT * FROM Driver WHERE numdriver = ?";

    /** Consulta para insertar un nuevo conductor. */
    private static final String SQL_INSERT        = "INSERT INTO Driver (numdriver, name, surname) VALUES (?, ?, ?)";

    /** Consulta para actualizar los datos textuales de un conductor. */
    private static final String SQL_UPDATE        = "UPDATE Driver SET name = ?, surname = ? WHERE numdriver = ?";

    /** Consulta para actualizar únicamente la imagen de un conductor. */
    private static final String SQL_UPDATE_IMAGE  = "UPDATE Driver SET imagen = ? WHERE numdriver = ?";

    /** Consulta para eliminar un conductor. */
    private static final String SQL_DELETE        = "DELETE FROM Driver WHERE numdriver = ?";

    /** Consulta para eliminar las relaciones del conductor en la tabla BDP. */
    private static final String SQL_DELETE_BDP    = "DELETE FROM BDP WHERE numdriver = ?";


    /**
     * Obtiene todos los conductores almacenados en la base de datos
     * @return Lista con todos los conductores; lista vacía si no hay ninguno o si ocurre un error de conexión
     */
    public ArrayList<Conductor> getConductores() {
        ArrayList<Conductor> conductores = new ArrayList<>();

        try (Connection con = ConexionBBDD.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_GET_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                conductores.add(mapearConductor(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener los conductores: " + e.getMessage());
        }

        return conductores;
    }

    /**
     * Busca y devuelve un conductor concreto a partir de su número identificador
     * @param idConductor Número identificador del conductor que se desea buscar.
     * @return El {@link Conductor} encontrado, o {@code null} si no existe ninguno
     * con ese identificador o si ocurre un error de conexión
     */
    public Conductor buscarConductor(int idConductor) {
        try (Connection con = ConexionBBDD.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_FIND_BY_ID)) {

            ps.setInt(1, idConductor);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearConductor(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar el conductor " + idConductor + ": " + e.getMessage());
        }

        return null;
    }

    /**
     * @param conductor El conductor del que se desea obtener la imagen
     * @return El nombre del fichero de imagen (si no tiene se carga la  imagen default)
     */
    public String obtenerImagen(Conductor conductor) {
        String consulta = "SELECT imagen FROM Driver WHERE numdriver = ?";

        try (Connection con = ConexionBBDD.getConexion();
             PreparedStatement ps = con.prepareStatement(consulta)) {

            ps.setInt(1, conductor.getNumeroConductor());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("imagen"); // puede ser null
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener la imagen del conductor "
                    + conductor.getNumeroConductor() + ": " + e.getMessage());
        }

        return null;
    }


    /**
     * @param conductor El conductor que se desea insertar
     * @return El número de registros insertados (normalmente 1), o 0 si hubo un error
     */
    public static int insertarConductor(Conductor conductor) {
        try (Connection con = ConexionBBDD.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_INSERT)) {

            ps.setInt(1, conductor.getNumeroConductor());
            ps.setString(2, conductor.getNombreConductor());
            ps.setString(3, conductor.getApellidoConductor());
            return ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al insertar el conductor: " + e.getMessage());
        }

        return 0;
    }

    /**
     * @param conductor El conductor con los nuevos datos textuales
     * @return El número de registros modificados (normalmente 1), o 0 si hubo un error
     */
    public int editarConductor(Conductor conductor) {
        try (Connection con = ConexionBBDD.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_UPDATE)) {

            ps.setString(1, conductor.getNombreConductor());
            ps.setString(2, conductor.getApellidoConductor());
            ps.setInt(3, conductor.getNumeroConductor());
            return ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al editar el conductor: " + e.getMessage());
        }

        return 0;
    }

    /**
     * Actualiza el campo {@code imagen} de un conductor en la base de datos
     * @param conductor El conductor cuya imagen se desea actualizar
     * @return El número de registros modificados (normalmente 1), o 0 si hubo un error
     */
    public int actualizarImagen(Conductor conductor) {
        try (Connection con = ConexionBBDD.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_UPDATE_IMAGE)) {

            ps.setString(1, conductor.getImagen()); // acepta null → NULL en BBDD
            ps.setInt(2, conductor.getNumeroConductor());
            return ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al actualizar la imagen del conductor "
                    + conductor.getNumeroConductor() + ": " + e.getMessage());
        }

        return 0;
    }

    /**
     * @param conductor El conductor que se desea eliminar
     * @return El número total de registros eliminados o 0 si hubo un error y se revirtió la transacción
     * @throws SQLException Si no se puede obtener conexión con la base de datos
     */
    public int borrarConductor(Conductor conductor) throws SQLException {
        int numeroCambios = 0;

        try (Connection con = ConexionBBDD.getConexion()) {
            try {
                con.setAutoCommit(false);

                // Primero se eliminan las relaciones en BDP (clave foránea)
                try (PreparedStatement ps = con.prepareStatement(SQL_DELETE_BDP)) {
                    ps.setInt(1, conductor.getNumeroConductor());
                    numeroCambios += ps.executeUpdate();
                }

                // Después se elimina el conductor
                try (PreparedStatement ps = con.prepareStatement(SQL_DELETE)) {
                    ps.setInt(1, conductor.getNumeroConductor());
                    numeroCambios += ps.executeUpdate();
                }

                con.commit();

            } catch (SQLException e) {
                System.out.println("Error al borrar el conductor, revirtiendo cambios: " + e.getMessage());
                con.rollback();
            } finally {
                con.setAutoCommit(true);
            }
        }

        return numeroCambios;
    }


    /**
     * @param rs El ResultSet posicionado en la fila que se desea mapear
     * @return Un Conductor con todos sus campos rellenos
     * @throws SQLException Si ocurre algún error al leer las columnas del ResultSet
     */
    private Conductor mapearConductor(ResultSet rs) throws SQLException {
        Conductor conductor = new Conductor();
        conductor.setNumeroConductor(rs.getInt("numdriver"));
        conductor.setNombreConductor(rs.getString("name"));
        conductor.setApellidoConductor(rs.getString("surname"));
        conductor.setImagen(rs.getString("imagen")); // null si no tiene imagen personalizada
        return conductor;
    }
}