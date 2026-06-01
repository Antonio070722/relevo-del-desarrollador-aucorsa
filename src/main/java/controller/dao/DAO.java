package controller.dao;

import controller.connection.ConexionBBDD;

import java.sql.Connection;

public class DAO {
    protected static Connection con = ConexionBBDD.getConexion();
}
