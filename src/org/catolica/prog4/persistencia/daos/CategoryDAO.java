package org.catolica.prog4.persistencia.daos;

import java.util.List;
import javax.persistence.EntityManagerFactory;
import org.catolica.prog4.persistencia.entities.Category;

/**
 *
 * @author FCGF
 */
public class CategoryDAO extends CategoryJpaController implements ICategoryDAO {

    public CategoryDAO(EntityManagerFactory emf) {
        super(emf);
    }

    @Override
    public List<Category> findAll() {
        return super.findCategoryEntities();
    }

}
