/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.catolica.prog4.persistencia.daos;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.catolica.prog4.persistencia.entities.Product;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.catolica.prog4.persistencia.daos.exceptions.IllegalOrphanException;
import org.catolica.prog4.persistencia.daos.exceptions.NonexistentEntityException;
import org.catolica.prog4.persistencia.entities.Category;

/**
 *
 * @author Fernando
 */
public class CategoryJpaController implements Serializable {

    public CategoryJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Category category) {
        if (category.getProducts() == null) {
            category.setProducts(new ArrayList<Product>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Product> attachedProducts = new ArrayList<Product>();
            for (Product productsProductToAttach : category.getProducts()) {
                productsProductToAttach = em.getReference(productsProductToAttach.getClass(), productsProductToAttach.getId());
                attachedProducts.add(productsProductToAttach);
            }
            category.setProducts(attachedProducts);
            em.persist(category);
            for (Product productsProduct : category.getProducts()) {
                Category oldCategoryOfProductsProduct = productsProduct.getCategory();
                productsProduct.setCategory(category);
                productsProduct = em.merge(productsProduct);
                if (oldCategoryOfProductsProduct != null) {
                    oldCategoryOfProductsProduct.getProducts().remove(productsProduct);
                    oldCategoryOfProductsProduct = em.merge(oldCategoryOfProductsProduct);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Category category) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Category persistentCategory = em.find(Category.class, category.getId());
            List<Product> productsOld = persistentCategory.getProducts();
            List<Product> productsNew = category.getProducts();
            List<String> illegalOrphanMessages = null;
            for (Product productsOldProduct : productsOld) {
                if (!productsNew.contains(productsOldProduct)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Product " + productsOldProduct + " since its category field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Product> attachedProductsNew = new ArrayList<Product>();
            for (Product productsNewProductToAttach : productsNew) {
                productsNewProductToAttach = em.getReference(productsNewProductToAttach.getClass(), productsNewProductToAttach.getId());
                attachedProductsNew.add(productsNewProductToAttach);
            }
            productsNew = attachedProductsNew;
            category.setProducts(productsNew);
            category = em.merge(category);
            for (Product productsNewProduct : productsNew) {
                if (!productsOld.contains(productsNewProduct)) {
                    Category oldCategoryOfProductsNewProduct = productsNewProduct.getCategory();
                    productsNewProduct.setCategory(category);
                    productsNewProduct = em.merge(productsNewProduct);
                    if (oldCategoryOfProductsNewProduct != null && !oldCategoryOfProductsNewProduct.equals(category)) {
                        oldCategoryOfProductsNewProduct.getProducts().remove(productsNewProduct);
                        oldCategoryOfProductsNewProduct = em.merge(oldCategoryOfProductsNewProduct);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = category.getId();
                if (findCategory(id) == null) {
                    throw new NonexistentEntityException("The category with id " + id + " no longer exists.");
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
            Category category;
            try {
                category = em.getReference(Category.class, id);
                category.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The category with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Product> productsOrphanCheck = category.getProducts();
            for (Product productsOrphanCheckProduct : productsOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Category (" + category + ") cannot be destroyed since the Product " + productsOrphanCheckProduct + " in its products field has a non-nullable category field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(category);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Category> findCategoryEntities() {
        return findCategoryEntities(true, -1, -1);
    }

    public List<Category> findCategoryEntities(int maxResults, int firstResult) {
        return findCategoryEntities(false, maxResults, firstResult);
    }

    private List<Category> findCategoryEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Category.class));
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

    public Category findCategory(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Category.class, id);
        } finally {
            em.close();
        }
    }

    public int getCategoryCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Category> rt = cq.from(Category.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
