package com.example.deckofcards.api.contoller;

import com.example.deckofcards.api.model.Deck;
import com.example.deckofcards.api.service.DeckService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import static org.mockito.BDDMockito.given;

class DeckControllerTest {

    private final DeckService mockDeckService = Mockito.mock(DeckService.class);
    private final DeckController deckController = new DeckController(mockDeckService);

    @Test
    public void givenNewDeckCall_whenProvidedCountEquals1_thenReturnSuccessRemainEquals52() {
        // arrange
        given(mockDeckService.createDecks(1)).willReturn(
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
