package org.catolica.prog4.persistencia.daos;

import java.util.List;
import javax.persistence.EntityManager;
import org.catolica.prog4.persistencia.daos.exceptions.IllegalOrphanException;
import org.catolica.prog4.persistencia.daos.exceptions.NonexistentEntityException;
import org.catolica.prog4.persistencia.entities.Rule;

/**
 *
 * @author Fernando
 */
public interface IRuleDAO {
    
    //Padrões gerados:
    void create(Rule rule);

    void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException;

    void edit(Rule rule) throws IllegalOrphanException, NonexistentEntityException, Exception;

    Rule findRule(Long id);

    List<Rule> findRuleEntities();

    List<Rule> findRuleEntities(int maxResults, int firstResult);

    EntityManager getEntityManager();

    int getRuleCount();
    
    //Adicionais:
    List<Rule> findAll();
}
