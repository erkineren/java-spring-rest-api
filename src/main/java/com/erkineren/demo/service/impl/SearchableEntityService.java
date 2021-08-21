package com.erkineren.demo.service.impl;

import com.erkineren.demo.persistence.specification.SearchCriteria;
import com.erkineren.demo.persistence.specification.SearchQueryCriteriaConsumer;
import com.erkineren.demo.web.payload.request.SearchableAndSortableRequest;
import org.springframework.core.GenericTypeResolver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Service
public class SearchableEntityService<T> {

    @PersistenceContext
    private EntityManager entityManager;

    private Class<T> genericType;


    public SearchableEntityService() {
        this.genericType = (Class<T>) GenericTypeResolver.resolveTypeArgument(getClass(), SearchableEntityService.class);
    }

    public Page<T> search(List<SearchCriteria> params, SearchableAndSortableRequest req) {

        final Pageable pageable = PageRequest.of(
                req.getPage(),
                req.getSize(),
                req.getDirection(),
                req.getField());

        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        final CriteriaQuery<T> query = builder.createQuery(genericType);
        final Root<T> r = query.from(genericType);

        Predicate predicate = builder.conjunction();
        SearchQueryCriteriaConsumer<T> searchConsumer = new SearchQueryCriteriaConsumer<>(predicate, builder, r);
        params.forEach(searchConsumer);
        predicate = searchConsumer.getPredicate();

        query
                .where(predicate)
                .orderBy(req.getDirection().isAscending() ? builder.asc(r.get(req.getField())) : builder.desc(r.get(req.getField())));

        TypedQuery<T> q = entityManager
                .createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize());

        countQuery
                .select(builder.count(countQuery.from(genericType)))
                .where(predicate);
        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(q.getResultList(), pageable, total);


//        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "id");
//
//        SearchSpecification<Product> spec =
//                new SearchSpecification<>(new SearchCriteria("title", ":", "Product4"));
//
//        spec.toPredicate(spec)
//        return productRepository.findAll(spec, pageable);
    }


}
