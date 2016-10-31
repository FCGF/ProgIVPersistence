package org.catolica.prog4.persistencia.daos;

import java.util.List;
import javax.persistence.EntityManagerFactory;
import org.catolica.prog4.persistencia.entities.Product;

/**
 *
 * @author FCGF
 */
public class ProductDAO extends ProductJpaController implements IProductDAO {

    public ProductDAO(EntityManagerFactory emf) {
        super(emf);
    }

    @Override
    public List<Product> findAll() {
        return super.findProductEntities();
    }

}
