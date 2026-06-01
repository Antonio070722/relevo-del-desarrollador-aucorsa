package controller.connection;

import java.sql.Connection;
import java.sql.DriverAction;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBBDD {
    private static final String url = "jdbc:mysql://127.0.0.1:3306/AUCORSA"; // URL de la base de datos
    private static final String user = "root"; // Usuario de la base de datos
    private static final String pass = "1234"; // Contraseña de la base de datos
    private static Connection conexion;

    /**
     * Crea una conexión a la BD si no existe y la devuelve
     * @return La conexion a la BD
     */
    public static Connection getConexion(){
        try {
            conexion = DriverManager.getConnection(url, user, pass);
        }catch (SQLException e){
            System.out.print("Error al conectar con la base de datos: " + e.getMessage());
        }
        return conexion;
    }

}
