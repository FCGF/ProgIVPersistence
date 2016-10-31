package org.catolica.prog4.persistencia.daos;

import java.util.List;
import javax.persistence.EntityManager;
import org.catolica.prog4.persistencia.daos.exceptions.IllegalOrphanException;
import org.catolica.prog4.persistencia.daos.exceptions.NonexistentEntityException;
import org.catolica.prog4.persistencia.entities.Category;

/**
 *
 * @author FCGF
 */
public interface ICategoryDAO {

    //Padrões gerados:
    void create(Category category);

    void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException;

    void edit(Category category) throws IllegalOrphanException, NonexistentEntityException, Exception;

    Category findCategory(Long id);

    List<Category> findCategoryEntities();

    List<Category> findCategoryEntities(int maxResults, int firstResult);

    EntityManager getEntityManager();

    int getCategoryCount();

    //Adicionais:
    List<Category> findAll();
}
