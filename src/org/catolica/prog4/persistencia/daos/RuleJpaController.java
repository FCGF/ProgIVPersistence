package org.catolica.prog4.persistencia.daos;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.catolica.prog4.persistencia.entities.User;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.catolica.prog4.persistencia.daos.exceptions.IllegalOrphanException;
import org.catolica.prog4.persistencia.daos.exceptions.NonexistentEntityException;
import org.catolica.prog4.persistencia.entities.Rule;

/**
 *
 * @author FCGF
 */
public class RuleJpaController implements Serializable {

    public RuleJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Rule rule) {
        if (rule.getUsers() == null) {
            rule.setUsers(new ArrayList<>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<User> attachedUsers = new ArrayList<>();
            for (User usersUserToAttach : rule.getUsers()) {
                usersUserToAttach = em.getReference(usersUserToAttach.getClass(), usersUserToAttach.getId());
                attachedUsers.add(usersUserToAttach);
            }
            rule.setUsers(attachedUsers);
            em.persist(rule);
            for (User usersUser : rule.getUsers()) {
                Rule oldRuleOfUsersUser = usersUser.getRule();
                usersUser.setRule(rule);
                usersUser = em.merge(usersUser);
                if (oldRuleOfUsersUser != null) {
                    oldRuleOfUsersUser.getUsers().remove(usersUser);
                    oldRuleOfUsersUser = em.merge(oldRuleOfUsersUser);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Rule rule) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Rule persistentRule = em.find(Rule.class, rule.getId());
            List<User> usersOld = persistentRule.getUsers();
            List<User> usersNew = rule.getUsers();
            List<String> illegalOrphanMessages = null;
            for (User usersOldUser : usersOld) {
                if (!usersNew.contains(usersOldUser)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<>();
                    }
                    illegalOrphanMessages.add("You must retain User " + usersOldUser + " since its rule field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<User> attachedUsersNew = new ArrayList<>();
            for (User usersNewUserToAttach : usersNew) {
                usersNewUserToAttach = em.getReference(usersNewUserToAttach.getClass(), usersNewUserToAttach.getId());
                attachedUsersNew.add(usersNewUserToAttach);
            }
            usersNew = attachedUsersNew;
            rule.setUsers(usersNew);
            rule = em.merge(rule);
            for (User usersNewUser : usersNew) {
                if (!usersOld.contains(usersNewUser)) {
                    Rule oldRuleOfUsersNewUser = usersNewUser.getRule();
                    usersNewUser.setRule(rule);
                    usersNewUser = em.merge(usersNewUser);
                    if (oldRuleOfUsersNewUser != null && !oldRuleOfUsersNewUser.equals(rule)) {
                        oldRuleOfUsersNewUser.getUsers().remove(usersNewUser);
                        oldRuleOfUsersNewUser = em.merge(oldRuleOfUsersNewUser);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = rule.getId();
                if (findRule(id) == null) {
                    throw new NonexistentEntityException("The rule with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Rule rule;
            try {
                rule = em.getReference(Rule.class, id);
                rule.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rule with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<User> usersOrphanCheck = rule.getUsers();
            for (User usersOrphanCheckUser : usersOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<>();
                }
                illegalOrphanMessages.add("This Rule (" + rule + ") cannot be destroyed since the User " + usersOrphanCheckUser + " in its users field has a non-nullable rule field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(rule);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Rule> findRuleEntities() {
        return findRuleEntities(true, -1, -1);
    }

    public List<Rule> findRuleEntities(int maxResults, int firstResult) {
        return findRuleEntities(false, maxResults, firstResult);
    }

    private List<Rule> findRuleEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Rule.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Rule findRule(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Rule.class, id);
        } finally {
            em.close();
        }
    }

    public int getRuleCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Rule> rt = cq.from(Rule.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
