package com.example.cashcard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

interface CashCardRepository extends CrudRepository<CashCard, Long>,PagingAndSortingRepository<CashCard, Long>  {
	
	CashCard findByIdAndOwner(Long id,String Owner);
	
	Page<CashCard> findByOwner(String owner,Pageable pageable);

	boolean existsByIdAndOwner(Long requestedId,String Owner);
}