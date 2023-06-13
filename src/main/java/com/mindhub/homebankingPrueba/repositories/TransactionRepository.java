package com.mindhub.homebankingPrueba.repositories;

import com.mindhub.homebankingPrueba.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository <Transaction, Long> {

}
