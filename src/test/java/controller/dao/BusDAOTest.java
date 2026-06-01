package controller.dao;

import models.Bus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BusDAOTest {
    private Bus bus = new Bus("1", "1", "1");
    @Test
    public void getBusesTest() {
        Assertions.assertNotNull(BusDAO.getBuses());
    }

    @Test
    public void insertarBusTest(){
        Assertions.assertEquals(BusDAO.insertarBus(bus), 1);
    }

    @Test
    public void editarBusTest(){
        Assertions.assertEquals(BusDAO.editarBus(bus), 0);
    }

    @Test
    public void borrarBusTest(){
        try {
            Assertions.assertEquals(BusDAO.borrarBus(bus), 1);
        } catch (Exception e) {}
    }
}
