package org.catolica.prog4.persistencia.setup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import org.catolica.prog4.persistencia.daos.RuleDAO;
import org.catolica.prog4.persistencia.daos.UserDAO;
import org.catolica.prog4.persistencia.entities.Rule;
import org.catolica.prog4.persistencia.entities.User;

public class TestarPersistencia {

    private static final String PERSISTENCE_UNIT_NAME = "PersistenciaPU";

    public static void main(String[] args) {
        //createRuletest();
        //createUserTest();
        findAllUsersTest();
        findAllRulesTest();
        User usuario = autenticar("Bill@prog4.net", "Bill#12345");
        usuario = autenticar("Bill@prog4.net", "Bill#12345.");
    }

    private static void findAllUsersTest() {
        System.out.println("\nfindAllUsersTest...");
        EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        UserDAO dao = new UserDAO(factory);
        List<User> lst = dao.findAll();
        lst.stream().forEach((o) -> {
            System.out.println(o);
        });
    }

    private static void findAllRulesTest() {
        System.out.println("\n findAllRulesTest...");
        EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        RuleDAO dao = new RuleDAO(factory);
        List<Rule> lst = dao.findRuleEntities();
        lst.stream().forEach((o) -> {
            System.out.println(o);
        });
    }

    private static void createUserTest() {
        System.out.println("\ncreateTest...");
        Map<String, String> datas = new HashMap<>(5);
        datas.put("Fabio", "Administrador");
        datas.put("João", "Comprador");
        datas.put("José", "Vendedor");
        datas.put("Galateo", "Gerente");
        datas.put("Bill", "Diretor");

        //String[] datas = {"Fabio", "João", "José", "Galateo", "Bill"};
        EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        UserDAO userDao = new UserDAO(factory);
        List<Rule> rules = new RuleDAO(factory).findAll();
        userDao.findUserEntities();
        for (Map.Entry<String, String> data : datas.entrySet()) {
            User o = new User();
            o.setNome(data.getKey());
            o.setEmail(data.getKey() + "@" + "prog4.net");
            o.setSenha(data.getKey() + "#12345");
            for (Rule rule : rules) {
                if (rule.getNome().equalsIgnoreCase(data.getValue())) {
                    o.setRule(rule);
                    break;
                }
            }
            userDao.create(o);
        }
    }

    private static void createRuletest() {
        System.out.println("\ncreateRuletest...");
        String[] datas = {"Administrador", "Comprador", "Vendedor", "Gerente", "Diretor"};
        EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        RuleDAO dao = new RuleDAO(factory);
        dao.findRuleEntities();
        for (String data : datas) {
            Rule o = new Rule();
            o.setNome(data);
            dao.create(o);
        }
    }
    
    private static void createCategorytest() {
        System.out.println("\ncreateCategorytest...");
        String[] datas = {"Convenience ", "Shopping", "Specialty", "Emergency", "Unsought"};
        EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        RuleDAO dao = new RuleDAO(factory);
        dao.findRuleEntities();
        for (String data : datas) {
            Rule o = new Rule();
            o.setNome(data);
            dao.create(o);
        }
    }

    private static User autenticar(String email, String senha) {
        System.out.println("\nautenticar...");
        EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        UserDAO dao = new UserDAO(factory);
        User user;
        try {
            user = dao.findUser(email, senha);
            System.out.println(String.format("Usuário %s autenticado com sucesso.", user.getNome()));
        } catch (NoResultException e) {
            user = null;
            System.out.println("Usuário ou senha incorretos.");
        }

        return user;
    }

}
