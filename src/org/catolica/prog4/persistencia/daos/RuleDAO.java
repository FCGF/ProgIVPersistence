package org.catolica.prog4.persistencia.daos;

import java.util.List;
import javax.persistence.EntityManagerFactory;
import org.catolica.prog4.persistencia.entities.Rule;

public class RuleDAO extends RuleJpaController implements IRuleDAO {

    public RuleDAO(EntityManagerFactory emf) {
        super(emf);
    }

    @Override
    public List<Rule> findAll() {
        return super.findRuleEntities();
    }
}
