package helper;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class PersistenceHelper {
    private static final EntityManager entityManager;
    static {
        entityManager = Persistence.createEntityManagerFactory("Kwetter").
                createEntityManager();
    }
    public static EntityManager getEntityManager() {
        return entityManager;
    };
}
