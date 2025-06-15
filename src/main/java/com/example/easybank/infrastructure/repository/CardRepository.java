package com.example.easybank.infrastructure.repository;

import com.example.easybank.domain.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CardRepository extends JpaRepository<Card, UUID> {

}
