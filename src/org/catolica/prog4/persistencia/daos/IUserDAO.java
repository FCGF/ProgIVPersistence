package org.catolica.prog4.persistencia.daos;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.catolica.prog4.persistencia.daos.exceptions.NonexistentEntityException;
import org.catolica.prog4.persistencia.entities.User;

public interface IUserDAO {

    //Padrões gerados:
    void create(User user);

    void destroy(Long id) throws NonexistentEntityException;

    void edit(User user) throws NonexistentEntityException, Exception;

    User findUser(Long id);

    List<User> findUserEntities();

    List<User> findUserEntities(int maxResults, int firstResult);

    EntityManager getEntityManager();

    int getUserCount();

    //Adicionais:
    List<User> findAll();

    User findUser(String email, String passwd) throws NoResultException;

}
