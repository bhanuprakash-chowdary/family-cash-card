package com.example.cashcard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CashCardJsonTest {

    @Autowired
    private JacksonTester<CashCard> json;
    
    @Autowired
    private JacksonTester<CashCard[]> jsonList;
    
    private CashCard[] cashCards;
    
    @BeforeEach
    void setUp() {
    	 cashCards = new CashCard[] {
    	            new CashCard(99L, 123.45,"Bhanu"),
    	            new CashCard(100L, 100.00,"Praveen"),
    	            new CashCard(101L, 150.00,"Vishnu")
    	    };
    }

    @Test
    void cashCardSerializationTest() throws IOException {
        CashCard cashCard = cashCards[0];
        assertThat(json.write(cashCard)).isStrictlyEqualToJson("single.json");
        assertThat(json.write(cashCard)).hasJsonPathNumberValue("@.id");
        assertThat(json.write(cashCard)).extractingJsonPathNumberValue("@.id")
                .isEqualTo(99);
        assertThat(json.write(cashCard)).hasJsonPathNumberValue("@.amount");
        assertThat(json.write(cashCard)).extractingJsonPathNumberValue("@.amount")
             .isEqualTo(123.45);
    }
    
    @Test
    void cashCardDeserializationTest() throws IOException {
    	  String expected = "{\n" +
                  "    \"id\":99,\n" +
                  "    \"amount\":123.45,\n" +
                  "    \"owner\":\"Bhanu\"\n" +
                  "}";
       assertThat(json.parse(expected))
               .isEqualTo(cashCards[0]);
       assertThat(json.parseObject(expected).id).isEqualTo(99L);
       assertThat(json.parseObject(expected).amount).isEqualTo(123.45);
    }
    
    @Test
    void cashCardListSerializationTest() throws IOException {
    	
    	assertThat(jsonList.write(cashCards)).isStrictlyEqualToJson("list.json");
    }
    
    
    @Test
    void cashCardListDeserializationTest() throws IOException {
    	
    	String expected = "[\n" +
    		    "    {\"id\": 99, \"amount\": 123.45, \"owner\": \"Bhanu\"},\n" +
    		    "    {\"id\": 100, \"amount\": 100.00, \"owner\": \"Praveen\"},\n" +
    		    "    {\"id\": 101, \"amount\": 150.00, \"owner\": \"Vishnu\"}\n" +
    		    "]";
    	
    	assertThat(jsonList.parse(expected)).isEqualTo(cashCards);
    }
    
}