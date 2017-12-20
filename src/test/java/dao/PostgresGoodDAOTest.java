package dao;

import entities.Good;
import DbConnection.DatabaseManager;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PostgresGoodDAOTest {

    @Test
    public void testGoodDao() {
        DatabaseManager.init();
        GoodDAO goodDAO = new PostgresGoodDAO();
        final String name = "Mersedes";
        final String updatedDescription = "updated description";
        Good good = Good.testGoodForName(name);

        System.out.println("Trying to insert : " + good);
        goodDAO.addGood(good);
        Optional<Good> inserted = goodDAO.getGoodByName(name);
        System.out.println(inserted);
        assertTrue(inserted.isPresent());
        System.out.println("Extracted : " + inserted);

        // UPDATE GOOD
        good.setDescription(updatedDescription);
        System.out.println("Trying to update : " + good);
        goodDAO.updateGood(good);
        Optional<Good> updated = goodDAO.getGoodByName(name);
        assertTrue(updated.isPresent());
        assertEquals(updated.get().getDescription(), updatedDescription);
        System.out.println("Extracted : " + updated);

        // DELETE GOOD
        goodDAO.deleteGoodByName(name);
        Optional<Good> deleted = goodDAO.getGoodByName(name);
        assertFalse(deleted.isPresent());
        System.out.println("Extracted : " + deleted);


        DatabaseManager.drop();
    }
}