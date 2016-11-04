package org.catolica.prog4.persistencia.setup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import org.catolica.prog4.persistencia.daos.CategoryDAO;
import org.catolica.prog4.persistencia.daos.ProductDAO;
import org.catolica.prog4.persistencia.daos.RuleDAO;
import org.catolica.prog4.persistencia.daos.UserDAO;
import org.catolica.prog4.persistencia.entities.Category;
import org.catolica.prog4.persistencia.entities.Product;
import org.catolica.prog4.persistencia.entities.Rule;
import org.catolica.prog4.persistencia.entities.User;
import org.catolica.prog4.persistencia.helpers.EntityManagerFactoryManager;

public class TestarPersistencia {

    public static void main(String[] args) {
        //createRuletest();
        //createUserTest();
        //createCategorytest();
        //createProductTest();

        findAllUsersTest();
        findAllRulesTest();
        findAllCategoriesTest();
        findAllProductsTest();

        User usuario = autenticar("Bill@prog4.net", "Bill#12345");
        usuario = autenticar("Bill@prog4.net", "Bill#12345.");
    }

    private static void findAllUsersTest() {
        System.out.println("\n findAllUsersTest...");
        EntityManagerFactory factory =  EntityManagerFactoryManager.getEntityManagerFactory();
        UserDAO dao = new UserDAO(factory);
        List<User> lst = dao.findAll();
        lst.stream().forEach((o) -> {
            System.out.println(o);
        });
    }

    private static void findAllRulesTest() {
        System.out.println("\n findAllRulesTest...");
        EntityManagerFactory factory = EntityManagerFactoryManager.getEntityManagerFactory();
        RuleDAO dao = new RuleDAO(factory);
        List<Rule> lst = dao.findRuleEntities();
        lst.stream().forEach((o) -> {
            System.out.println(o);
        });
    }

    private static void findAllCategoriesTest() {
        System.out.println("\n findAllCategoriesTest...");
        EntityManagerFactory factory = EntityManagerFactoryManager.getEntityManagerFactory();
        CategoryDAO dao = new CategoryDAO(factory);
        List<Category> lst = dao.findAll();
        lst.stream().forEach((o) -> {
            System.out.println(o);
        });
    }

    private static void findAllProductsTest() {
        System.out.println("\n findAllProductsTest...");
        EntityManagerFactory factory = EntityManagerFactoryManager.getEntityManagerFactory();
        ProductDAO dao = new ProductDAO(factory);
        List<Product> lst = dao.findAll();
        lst.stream().forEach((o) -> {
            System.out.println(o);
        });
    }

    private static void createUserTest() {
        System.out.println("\n createUserTest...");
        Map<String, String> datas = new HashMap<>(8);
        datas.put("Fabio", "Administrador");
        datas.put("João", "Comprador");
        datas.put("José", "Vendedor");
        datas.put("Galateo", "Gerente");
        datas.put("Bill", "Diretor");

        EntityManagerFactory factory = EntityManagerFactoryManager.getEntityManagerFactory();
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
        System.out.println("\n createRuletest...");
        String[] datas = {"Administrador", "Comprador", "Vendedor", "Gerente", "Diretor"};
        EntityManagerFactory factory = EntityManagerFactoryManager.getEntityManagerFactory();
        RuleDAO dao = new RuleDAO(factory);
        dao.findRuleEntities();
        for (String data : datas) {
            Rule o = new Rule();
            o.setNome(data);
            dao.create(o);
        }
    }

    private static void createCategorytest() {
        System.out.println("\n createCategorytest...");
        String[] datas = {"Convenience", "Shopping", "Specialty", "Emergency", "Unsought"};
        EntityManagerFactory factory = EntityManagerFactoryManager.getEntityManagerFactory();
        CategoryDAO dao = new CategoryDAO(factory);
        dao.findCategoryEntities();
        for (String data : datas) {
            Category o = new Category();
            o.setNome(data);
            dao.create(o);
        }
    }

    private static void createProductTest() {
        System.out.println("\n createTest...");
        Map<String, String> datas = new HashMap<>(8);
        datas.put("Chocolate", "Convenience");
        datas.put("Pair of Shoes", "Shopping");
        datas.put("BMW", "Specialty");
        datas.put("Bandages", "Emergency");
        datas.put("T-Shirt", "Unsought");

        EntityManagerFactory factory = EntityManagerFactoryManager.getEntityManagerFactory();
        ProductDAO productDao = new ProductDAO(factory);
        List<Category> categories = new CategoryDAO(factory).findAll();
        productDao.findProductEntities();
        for (Map.Entry<String, String> data : datas.entrySet()) {
            Product o = new Product();
            o.setNome(data.getKey());
            o.setDescription(data.getKey() + " Product");
            o.setPrice(data.getKey().length());
            for (Category category : categories) {
                if (category.getNome().equalsIgnoreCase(data.getValue())) {
                    o.setCategory(category);
                    break;
                }
            }
            productDao.create(o);
        }
    }

    private static User autenticar(String email, String senha) {
        System.out.println("\n autenticar...");
        EntityManagerFactory factory = EntityManagerFactoryManager.getEntityManagerFactory();
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
