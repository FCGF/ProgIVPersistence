package org.catolica.prog4.persistencia.daos;

import java.util.List;
import javax.persistence.EntityManager;
import org.catolica.prog4.persistencia.daos.exceptions.NonexistentEntityException;
import org.catolica.prog4.persistencia.entities.Product;

/**
 *
 * @author FCGF
 */
public interface IProductDAO {

    void create(Product product);

    void destroy(Long id) throws NonexistentEntityException;

    void edit(Product product) throws NonexistentEntityException, Exception;

    Product findProduct(Long id);

    List<Product> findProductEntities();

    List<Product> findProductEntities(int maxResults, int firstResult);

    EntityManager getEntityManager();

    int getProductCount();

    List<Product> findAll();

}
