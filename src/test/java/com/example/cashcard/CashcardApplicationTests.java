package com.example.cashcard;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class CashcardApplicationTests {

	@Autowired
	TestRestTemplate restTemplate;

	@Test
	void shouldReturnACashCardWhenDataIsSaved() {
		ResponseEntity<String> response = restTemplate.withBasicAuth("Bhanu", "Chowdary@1507").getForEntity("/cashcards/99", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//		assertThat(response.getBody()).isBlank();

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		Number id = documentContext.read("$.id");
		assertThat(id).isEqualTo(99);

		Double amount = documentContext.read("$.amount");
		assertThat(amount).isEqualTo(123.45);

	}

	@Test
	void shouldNotReturnACashCardWithAnUnknownId() {
		ResponseEntity<String> response = restTemplate.withBasicAuth("Bhanu", "Chowdary@1507").getForEntity("/cashcards/1000", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(response.getBody()).isBlank();
	}

	@Test
	@DirtiesContext
	void shouldCreateANewCashCard() {
		CashCard newCashCard = new CashCard(null, 250.00,null);
		ResponseEntity<Void> response = restTemplate.withBasicAuth("Bhanu", "Chowdary@1507").postForEntity("/cashcards", newCashCard, Void.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		URI locationOfNewCashCard = response.getHeaders().getLocation();

		ResponseEntity<String> getResponse = restTemplate.withBasicAuth("Bhanu", "Chowdary@1507").getForEntity(locationOfNewCashCard, String.class);

		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		// Add assertions such as these
		DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
		Number id = documentContext.read("$.id");
		Double amount = documentContext.read("$.amount");
		assertThat(id).isNotNull();
		assertThat(amount).isEqualTo(250);
	}

	@Test
	void shouldReturnAllCashCardsWhenListIsRequested() {

		ResponseEntity<String> listOfCashCards = restTemplate.withBasicAuth("Bhanu", "Chowdary@1507").getForEntity("/cashcards", String.class);

		assertThat(listOfCashCards.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(listOfCashCards.getBody());

		int cashCardCount = documentContext.read("$.length()");
		assertThat(cashCardCount).isEqualTo(3);
		JSONArray ids = documentContext.read("$..id");
		assertThat(ids).containsExactlyInAnyOrder(99, 100, 101);

		JSONArray amounts = documentContext.read("$..amount");
		assertThat(amounts).containsExactlyInAnyOrder(123.45, 100.0, 150.00);
	}
	
	@Test
	void shouldReturnAPageOfCashCards() {
		ResponseEntity<String> response = restTemplate.withBasicAuth("Bhanu", "Chowdary@1507").getForEntity("/cashcards?page=0&size=1&sort=amount,desc", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		
		
		DocumentContext documentContext=JsonPath.parse(response.getBody());
		JSONArray page=documentContext.read("$[*]");
		assertThat(page.size()).isEqualTo(1);
		
		double amount=documentContext.read("$[0].amount");
		assertThat(amount).isEqualTo(123.45);
	}
	
	@Test
	void shouldReturnASortedPageOfCashCardsWithNoParametersAndUseDefaultValues() {
	    ResponseEntity<String> response = restTemplate.withBasicAuth("Bhanu", "Chowdary@1507").getForEntity("/cashcards", String.class);
	    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

	    DocumentContext documentContext = JsonPath.parse(response.getBody());
	    JSONArray page = documentContext.read("$[*]");
	    assertThat(page.size()).isEqualTo(3);

	    JSONArray amounts = documentContext.read("$..amount");
	    assertThat(amounts).containsExactly(100.00, 123.45, 150.00);
	}
	
	@Test
	void shouldNotReturnACashCardWhenUsingBadCredentials() {
		
		ResponseEntity<String> response=restTemplate.withBasicAuth("Bhanu", "WRONG_PASSWORD").getForEntity("/cashcards", String.class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}
	
	@Test
	void shouldRejectUsersWhoAreNotCardOwners() {
		
		ResponseEntity<String> response=restTemplate.withBasicAuth("Vishnu", "Vishnu@1702").getForEntity("/cashcards/99", String.class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
	}
	
	@Test
	void shouldNotAllowAccessToCashCardsTheyDoNotOwn() {
		
		ResponseEntity<String> response=restTemplate.withBasicAuth("Bhanu", "Chowdary@1507").getForEntity("/cashcards/102", String.class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		
	}
	
	@Test
	@DirtiesContext
	void shouldUpdateAnExistingCashCard() {
		CashCard cashCard = new CashCard(null, 19.99, null);
		HttpEntity<CashCard> request = new HttpEntity<>(cashCard);
		ResponseEntity<Void> response = restTemplate.withBasicAuth("Bhanu", "Chowdary@1507").exchange("/cashcards/99", HttpMethod.PUT, request, Void.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		
		
		ResponseEntity<String> updatedResponse=restTemplate.withBasicAuth("Bhanu", "Chowdary@1507").getForEntity("/cashcards/99", String.class);
		
		assertThat(updatedResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		
		DocumentContext documentContext=JsonPath.parse(updatedResponse.getBody());
		
		int id=documentContext.read("$.id");
		assertThat(id).isEqualTo(99);
		Double amount=documentContext.read("$.amount");
		assertThat(amount).isEqualTo(19.99);
		
	}
	
//	 @Test
//	    void shouldNotUpdateACashCardThatDoesNotExist() {
//	        CashCard unknownCard = new CashCard(null, 19.99, null);
//	        HttpEntity<CashCard> request = new HttpEntity<>(unknownCard);
//	        ResponseEntity<Void> response = restTemplate
//	                .withBasicAuth("sarah1", "abc123")
//	                .exchange("/cashcards/99999", HttpMethod.PUT, request, Void.class);
//	        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//	    }
//
//	    @Test
//	    void shouldNotUpdateACashCardThatIsOwnedBySomeoneElse() {
//	        CashCard kumarsCard = new CashCard(null, 333.33, null);
//	        HttpEntity<CashCard> request = new HttpEntity<>(kumarsCard);
//	        ResponseEntity<Void> response = restTemplate
//	                .withBasicAuth("sarah1", "abc123")
//	                .exchange("/cashcards/102", HttpMethod.PUT, request, Void.class);
//	        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//	    }
	
	@Test
	@DirtiesContext
	void shouldDeleteAnExistingCashCard() {
		ResponseEntity<Void> response = restTemplate.withBasicAuth("Bhanu", "Chowdary@1507").exchange("/cashcards/99",HttpMethod.DELETE, null, Void.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		
	    ResponseEntity<String> getResponse = restTemplate.withBasicAuth("Bhanu", "Chowdary@1507").getForEntity("/cashcards/99", String.class);
	    assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}
	
	@Test
	void shouldNotDeleteACashCardThatDoesNotExist() {
	    ResponseEntity<Void> deleteResponse = restTemplate.withBasicAuth("Bhanu", "Chowdary@1507").exchange("/cashcards/99999", HttpMethod.DELETE, null, Void.class);
	    assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}
	
	@Test
	void shouldNotAllowDeletionOfCashCardsTheyDoNotOwn() {
	    ResponseEntity<Void> deleteResponse = restTemplate.withBasicAuth("Bhanu", "Chowdary@1507").exchange("/cashcards/102", HttpMethod.DELETE, null, Void.class);
	    assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	    
	    ResponseEntity<String> getResponse = restTemplate.withBasicAuth("Hemanth", "Hemanth@0912").getForEntity("/cashcards/102", String.class);
	    assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
	}
	
}
