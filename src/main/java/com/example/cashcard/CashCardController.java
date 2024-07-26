package com.example.cashcard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cashcards")
public class CashCardController {

	private final CashCardRepository cashCardRepository;

	private CashCardController(CashCardRepository cashCardRepository) {
		this.cashCardRepository = cashCardRepository;
	}

	@GetMapping("/{requestedId}")
	private ResponseEntity<CashCard> findById(@PathVariable Long requestedId,Principal principal) {
		CashCard cashCardOptional =findCashCard(requestedId,principal);
		if (cashCardOptional!=null) {
			return ResponseEntity.ok(cashCardOptional);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping
	private ResponseEntity<Void> createCashCard(@RequestBody CashCard cashCard, UriComponentsBuilder ucb,Principal principal) {

		 CashCard cashCardWithOwner = new CashCard(null, cashCard.amount, principal.getName());
		 
		CashCard savedCashCard = cashCardRepository.save(cashCardWithOwner);

		URI locationOfNewCashCard = ucb.path("/cashcards/{id}").buildAndExpand(savedCashCard.id).toUri();

		return ResponseEntity.created(locationOfNewCashCard).build();
	}

//	@GetMapping
//	private ResponseEntity<Iterable<CashCard>> findAll() {
//		return ResponseEntity.ok(cashCardRepository.findAll());
//	}
	
	@GetMapping
	private ResponseEntity<List<CashCard>> findAll(Pageable pageable,Principal principal) {
	   Page<CashCard> page = cashCardRepository.findByOwner(principal.getName(),
	           PageRequest.of(
	                   pageable.getPageNumber(),
	                   pageable.getPageSize(),
	                   pageable.getSortOr(Sort.by(Sort.Direction.ASC, "amount"))
	                   ));
	   return ResponseEntity.ok(page.getContent());
	}
	
	@PutMapping("/{requestedId}")
	private ResponseEntity<Void> putCashCard(@PathVariable Long requestedId, @RequestBody CashCard cashCardUpdate,
			Principal principal) {
		CashCard cashCard = findCashCard(requestedId,principal);
		if (cashCard != null) {

			CashCard updatedCashCard = new CashCard(cashCard.id, cashCardUpdate.amount, principal.getName());
			cashCardRepository.save(updatedCashCard);
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}
	
	private CashCard findCashCard(Long requestedId, Principal principal) {
	    return cashCardRepository.findByIdAndOwner(requestedId, principal.getName());
	}
	
	@DeleteMapping("/{requestedId}")
	private ResponseEntity<Void> deleteCashCard(@PathVariable Long requestedId,Principal principal) {
		
		boolean cashCardExists=cashCardRepository.existsByIdAndOwner(requestedId,principal.getName());
		if(cashCardExists) {
			cashCardRepository.deleteById(requestedId); 
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}

}