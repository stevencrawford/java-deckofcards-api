package com.example.deckofcards.api.contoller;

import com.example.deckofcards.api.model.Deck;
import com.example.deckofcards.api.service.DeckService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import static org.mockito.BDDMockito.given;

class DeckControllerTest {

    private final DeckService mockDeckServive = Mockito.mock(DeckService.class);
    private final DeckController deckController = new DeckController(mockDeckServive);

    @Test
    @DisplayName("Given New Deck call, with Count 1, then Return success")
    public void givenNewDeckCall_whenProvidedCountEquals1_thenReturnSuccessRemainEquals52() {
        // arrange
        given(mockDeckServive.createDecks(1)).willReturn(
                Deck.builder()
                        .success(true)
                        .deckId("xyz")
                        .build()
        );

        // act
        ResponseEntity<Deck> responseEntity = deckController.newDeck();
        Deck actual = responseEntity.getBody();

        // assert
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.isSuccess()).isTrue();
    }

}
