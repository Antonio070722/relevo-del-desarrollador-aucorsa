package controller.dao;

import models.Conductor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConductorDAOTest {
    private Conductor conductor = new Conductor(1, "Admin", "Test");
    private ConductorDAO conductorDAO = new ConductorDAO();
    @Test
    public void getConductoresTest() {
        Assertions.assertNotNull(conductorDAO.getConductores());
    }

    @Test
    public void insertarConductorTest(){
        Assertions.assertEquals(conductorDAO.insertarConductor(conductor), 1);
    }

    @Test
    public void editarConductorTest(){
        Assertions.assertEquals(conductorDAO.editarConductor(conductor), 1);
    }

    @Test
    public void borrarConductorTest(){
        try {
            Assertions.assertEquals(conductorDAO.borrarConductor(conductor), 1);
        } catch (Exception e) {}
    }
}
