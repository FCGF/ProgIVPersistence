package org.catolica.prog4.persistencia.daos;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import org.catolica.prog4.persistencia.daos.exceptions.NonexistentEntityException;
import org.catolica.prog4.persistencia.entities.User;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runners.MethodSorters;

/**
 *
 * @author FCGF
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserDAOTest {

    private static final String PERSISTENCE_UNIT_NAME = "PersistenciaPU";
    private final EntityManagerFactory factory;

    public UserDAOTest() {
        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }

    @Test
    public void test1FindAll() {
        System.out.println("findAll");
        UserDAO instance = new UserDAO(factory);
        List<User> result = instance.findAll();
        assertNotNull(result);
        assertTrue(result.size() > 0);
    }

    @Test
    public void test2GetUserCount() {
        System.out.println("getUserCount");
        UserDAO instance = new UserDAO(factory);
        List<User> result = instance.findAll();
        assertTrue(result.size() == instance.getUserCount());
    }

    @Test
    public void test3FindUser() {
        System.out.println("findUser (authenticate)");
        String email = "Fabio@prog4.net";
        String senha = "Fabio#12345";
        UserDAO instance = new UserDAO(factory);
        User result = instance.findUser(email, senha);
        assertNotNull(result);
        assertEquals(result.getNome(), "Fabio");
    }

    @Test
    public void test4Create() {
        UserDAO instance = new UserDAO(factory);

        User usuario = new User();
        String nome = "Osmar";
        usuario.setNome(nome);
        usuario.setEmail(nome + "@" + "prog4.net");
        usuario.setSenha(nome + "#12345");

        org.catolica.prog4.persistencia.entities.Rule rule = new RuleDAO(factory).findRule(1L);
        usuario.setRule(rule);

        instance.create(usuario);

        User result = instance.findUser(usuario.getId());

        assertEquals(usuario, result);
    }

    @Test(expected = NoResultException.class)
    public void test5Delete() {
        UserDAO instance = new UserDAO(factory);
        User deletar = instance.findUser("Osmar@prog4.net", "Osmar#12345");

        try {
            instance.destroy(deletar.getId());
        } catch (NonexistentEntityException ex) {
            fail(ex.getMessage());
        }

        instance.findUser("Osmar@prog4.net", "Osmar#12345");
    }

    @Test
    public void test6Update() {
        UserDAO instance = new UserDAO(factory);
        List<User> usuarios = instance.findAll();
        try {
            for (User usuario : usuarios) {
                usuario.setNome(String.format("%s.", usuario.getNome()));
                instance.edit(usuario);
            }
        } catch (Exception ex) {
            fail(ex.getMessage());
        }

        usuarios = instance.findAll();

        List<String> nomes = new ArrayList<>();

        for (User usuario : usuarios) {
            try {
                String nome = usuario.getNome();
                nomes.add(nome);
                usuario.setNome(nome.substring(0, nome.length() - 1));
                instance.edit(usuario);
            } catch (Exception ex) {
                fail(ex.getMessage());
            }

        }

        nomes.stream().forEach((nome) -> {
            assertTrue(nome.endsWith("."));
        });

    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void test7FindUserSenhaErrada() {
        thrown.expect(NoResultException.class);
        System.out.println("findUser Senha errada");
        String email = "Fabio@prog4.net";
        String senha = "Fabio#12345.";
        UserDAO instance = new UserDAO(factory);
        User result = instance.findUser(email, senha);
        fail("Deveria ter lançado exeção. Autenticação falhou.");
    }
}
