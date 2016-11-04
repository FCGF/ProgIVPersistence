package org.catolica.prog4.persistencia.daos;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import org.catolica.prog4.persistencia.daos.exceptions.IllegalOrphanException;
import org.catolica.prog4.persistencia.daos.exceptions.NonexistentEntityException;
import org.catolica.prog4.persistencia.entities.Rule;
import org.catolica.prog4.persistencia.helpers.EntityManagerFactoryManager;
import org.junit.Test;
import static org.junit.Assert.*;

public class RuleDAOTest {

    private static final String PERSISTENCE_UNIT_NAME = "PersistenciaPU";
    private final EntityManagerFactory factory;

    public RuleDAOTest() {
        factory = EntityManagerFactoryManager.getEntityManagerFactory();
    }

    @Test
    public void test2GetUserCount() {
        System.out.println("getUserCount");
        RuleDAO instance = new RuleDAO(factory);
        assertTrue(0 < instance.getRuleCount());

        List<Rule> regra = instance.findRuleEntities();

        assertTrue(regra.size() == instance.getRuleCount());
    }

    @Test
    public void test4Create() {
        RuleDAO instance = new RuleDAO(factory);

        Rule regra = new Rule();
        String nome = "Testador";
        regra.setNome(nome);

        instance.create(regra);

        Rule result = instance.findRule(regra.getId());

        assertEquals(regra, result);
    }

    @Test
    public void test5Delete() {
        RuleDAO instance = new RuleDAO(factory);
        Rule deletar = null;

        for (Rule r : instance.findRuleEntities()) {
            if ("Testador".equals(r.getNome())) {
                deletar = r;
                break;
            }
        }

        try {
            instance.destroy(deletar.getId());
        } catch (IllegalOrphanException | NonexistentEntityException ex) {
            fail(ex.getMessage());
        } catch (NullPointerException Ex) {
            fail("Testador não inicializado.");
        }

        Rule regra = instance.findRule(deletar.getId());
        assertNull(regra);
    }

    @Test
    public void test6Update() {
        RuleDAO instance = new RuleDAO(factory);
        List<Rule> regras = instance.findRuleEntities();
        try {
            for (Rule regra : regras) {
                regra.setNome(String.format("%s.", regra.getNome()));
                instance.edit(regra);
            }
        } catch (Exception ex) {
            fail(ex.getMessage());
        }

        regras = instance.findRuleEntities();

        List<String> nomes = new ArrayList<>();

        for (Rule regra : regras) {
            try {
                String nome = regra.getNome();
                nomes.add(nome);
                regra.setNome(nome.substring(0, nome.length() - 1));
                instance.edit(regra);
            } catch (Exception ex) {
                fail(ex.getMessage());
            }
        }

        nomes.stream().forEach((nome) -> {
            assertTrue(nome.endsWith("."));
        });

    }

}
