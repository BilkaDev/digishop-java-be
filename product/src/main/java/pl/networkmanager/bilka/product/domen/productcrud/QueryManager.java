package pl.networkmanager.bilka.product.domen.productcrud;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import pl.networkmanager.bilka.product.domen.categorycrud.CategoryCrudFacade;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class QueryManager {
    @PersistenceContext
    private final EntityManager entityManager;
    private final CategoryCrudFacade categoryCrudFacade;

    public List<Product> getProduct(String name, String category, Float price_min, Float price_max, Integer page, Integer limit, String sort, String order) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);

        if (page <= 0) page = 1;
        List<Predicate> predicates = prepareQuery(name, category, price_min, price_max, criteriaBuilder, root);

        if (!order.isEmpty() && !sort.isEmpty()) {
            String column = switch (sort) {
                case "name" -> "name";
                case "category" -> "category";
                case "data" -> "createAt";
                default -> "price";
            };
            Order orderQuery;
            if (order.equals("desc")) {
                orderQuery = criteriaBuilder.desc(root.get(column));
            } else {
                orderQuery = criteriaBuilder.asc(root.get(column));
            }
            query.orderBy(orderQuery);
        }
        query.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(query).setFirstResult((page - 1) * limit).setMaxResults(limit).getResultList();
    }

    private List<Predicate> prepareQuery(String name, String category, Float price_min, Float price_max,
                                         CriteriaBuilder criteriaBuilder, Root<Product> root) {
        List<Predicate> predicates = new ArrayList<>();
        if (name != null && !name.trim().isEmpty()) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }
        if (category != null && !category.isEmpty()) {
            // Assuming categoryCrudFacade is a dependency that is injected here or passed to the method
            categoryCrudFacade.findCategoryByShortId(category)
                    .ifPresent(value -> predicates.add(criteriaBuilder.equal(root.get("category"), value)));
        }
        if (price_min != null) {
            predicates.add(criteriaBuilder.greaterThan(root.get("price"), price_min - 0.01));
        }
        if (price_max != null) {
            predicates.add(criteriaBuilder.lessThan(root.get("price"), price_max + 0.01));
        }
        predicates.add(criteriaBuilder.isTrue(root.get("activate")));
        return predicates;
    }

    public Long countActiveProducts(String name, String category, Float price_min, Float price_max) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<Product> root = query.from(Product.class);
        List<Predicate> predicates = prepareQuery(name, category, price_min, price_max, criteriaBuilder, root);
        query.select(criteriaBuilder.count(root)).where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(query).getSingleResult();


    }
}