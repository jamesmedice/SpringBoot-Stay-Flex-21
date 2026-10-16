package com.medici.app.repository;

import com.medici.app.entity.BankAccount;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface BankAccountRepository extends PagingAndSortingRepository<BankAccount, UUID> {

}
