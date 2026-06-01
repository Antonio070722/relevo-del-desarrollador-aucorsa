package controller.dao;

import models.Lugar;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LugarDAOTest {
    private Lugar lugar = new Lugar(1, "Test", "Admin", "Prueba");
    @Test
    public void getLugaresTest() {
        Assertions.assertNotNull(LugarDAO.getLugares());
    }

    @Test
    public void insertarLugarTest(){
        Assertions.assertEquals(LugarDAO.insertarLugar(lugar), 1);
    }

    @Test
    public void editarLugarTest(){
        Assertions.assertEquals(LugarDAO.editarLugar(lugar), 0);
    }

    @Test
    public void borrarLugarTest(){
        try {
            Assertions.assertEquals(LugarDAO.borrarLugar(lugar), 1);
        } catch (Exception e) {}
    }
}