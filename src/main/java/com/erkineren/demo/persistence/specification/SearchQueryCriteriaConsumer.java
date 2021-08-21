package com.erkineren.demo.persistence.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.function.Consumer;

public class SearchQueryCriteriaConsumer<T> implements Consumer<SearchCriteria> {
    private Predicate predicate;
    private final CriteriaBuilder builder;
    private final Root<T> r;

    public SearchQueryCriteriaConsumer(Predicate predicate, CriteriaBuilder builder, Root<T> r) {
        super();
        this.predicate = predicate;
        this.builder = builder;
        this.r = r;
    }

    @Override
    public void accept(SearchCriteria param) {

        if (param.getOperation().equalsIgnoreCase(">")) {
            predicate = builder.and(predicate, builder.greaterThanOrEqualTo(r.get(param.getKey()), param.getValue().toString()));
        } else if (param.getOperation().equalsIgnoreCase("<")) {
            predicate = builder.and(predicate, builder.lessThanOrEqualTo(r.get(param.getKey()), param.getValue().toString()));
        } else if (param.getOperation().equalsIgnoreCase(":")) {
            if (r.get(param.getKey()).getJavaType() == String.class) {
                predicate = builder.and(predicate, builder.like(r.get(param.getKey()), "%" + param.getValue() + "%"));
            } else if (r.get(param.getKey()).getJavaType().isEnum()) {
                Class<Enum> enumType = (Class<Enum>) r.get(param.getKey()).getJavaType();
                Object enumValue = Enum.valueOf(enumType, param.getValue().toString());
                predicate = builder.and(predicate, builder.equal(r.get(param.getKey()), enumValue));
            } else {
                predicate = builder.and(predicate, builder.equal(r.get(param.getKey()), param.getValue()));
            }
        }
    }

    public Predicate getPredicate() {
        return predicate;
    }
}
