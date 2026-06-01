package controller.dao;

import models.BCL;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BCLDAOTest {
    private BCL bcl = new BCL("1", 1, 1, "1");
    @Test
    public void getBCLTest() {
        Assertions.assertNotNull(BCLDAO.getBCL());
    }

    @Test
    public void insertarBCLTest(){
        Assertions.assertEquals(BCLDAO.insertarBCL(bcl), 1);
    }

    @Test
    public void editarBCLTest(){
        Assertions.assertEquals(BCLDAO.editarBCL(bcl), 0);
    }

    @Test
    public void borrarBCLTest(){
        try {
            Assertions.assertEquals(BCLDAO.borrarBCL(bcl), 1);
        } catch (Exception e) {}
    }
}
