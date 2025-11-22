package com.example.easybank.util;

import com.example.easybank.domain.entity.Transaction;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class TransactionSpecifications {
    public static Specification<Transaction> hasType(String type) {
        return (root, query, criteriaBuilder) ->
                type == null ? null : criteriaBuilder.equal(root.get("type"), type);
    }

    public static Specification<Transaction> betweenDates(LocalDateTime from, LocalDateTime to) {
        return (root, query, criteriaBuilder) -> {
            if (from != null || to != null) return null;
            return criteriaBuilder.between(root.get("dateTime"), from, to);
        };
    }

    public static Specification<Transaction> hasOriginAccount(String originAccount) {
        return (root, query, criteriaBuilder) -> {
            if (originAccount == null) return null;
            return criteriaBuilder.equal(root.get("originAccount"), originAccount);
        };
    }

    public static Specification<Transaction> hasDestinationAccount(String destinationAccount) {
        return (root, query, criteriaBuilder) -> {
            if (destinationAccount == null) return null;
            return criteriaBuilder.equal(root.get("destinationAccount"), destinationAccount);
        };
    }

    public static Specification<Transaction> hasAccount(String accountId) {
        return (root, query, criteriaBuilder) -> {
            if (accountId == null) return null;
            return criteriaBuilder.or(
                    criteriaBuilder.equal(root.get("originAccount").get("id"), accountId),
                    criteriaBuilder.equal(root.get("destinationAccount").get("id"), accountId)
            );
        };
    }
}
