package com.example.deckofcards.api.service.impl;

import com.example.deckofcards.api.common.exception.EntityNotFoundException;
import com.example.deckofcards.api.model.Card;
import com.example.deckofcards.api.model.Deck;
import com.example.deckofcards.api.model.Suit;
import com.example.deckofcards.api.repository.DeckRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DeckServiceImplTest {

    @Mock
    private DeckRepository deckRepository;

    private DeckServiceImpl deckService;

    @BeforeEach
    void setUp() {
        deckService = new DeckServiceImpl(deckRepository);
    }

    @Test
    void givenDeckCount_whenCreateDecks_thenReturnDeck() {
        // Arrange
        int count = 2;
        List<Card> cards = createCards(count);
        Deck expectedDeck = createDeck("deck-id", cards, true, false, cards.size());

        given(deckRepository.save(any(Deck.class))).willReturn(expectedDeck);

        // Act
        Deck actualDeck = deckService.createDecks(count);

        // Assert
        assertThat(actualDeck).isEqualTo(expectedDeck);
    }

    @Test
    void givenCardList_whenCreatePartialDeck_thenReturnDeck() {
        // Arrange
        List<Card> cards = createCards(5);
        Deck expectedDeck = createDeck("deck-id", cards, true, false, cards.size());

        given(deckRepository.save(any(Deck.class))).willReturn(expectedDeck);

        // Act
        Deck actualDeck = deckService.createPartialDeck(cards);

        // Assert
        assertThat(actualDeck).isEqualTo(expectedDeck);
    }

    @Test
    void givenNewDeck_whenShuffleDeck_thenReturnShuffledDeck() {
        // Arrange
        int count = 1;
        List<Card> cards = createCards(count);
        Deck expectedDeck = createDeck("deck-id", cards, true, true, cards.size());

        given(deckRepository.save(any(Deck.class))).willReturn(expectedDeck);

        // Act
        Deck actualDeck = deckService.shuffleDeck(Deck.NEW, count);

        // Assert
        assertThat(actualDeck).isEqualTo(expectedDeck);
        assertThat(actualDeck.isShuffled()).isTrue();
    }

    @Test
    void givenExistingDeckId_whenShuffleDeck_thenReturnShuffledDeck() {
        // Arrange
        String deckId = "existing-deck-id";
        List<Card> cards = createCards(1);
        Deck existingDeck = createDeck(deckId, cards, true, false, cards.size());
        Deck expectedDeck = createDeck(deckId, cards, true, true, cards.size());

        given(deckRepository.findById(deckId)).willReturn(Optional.of(existingDeck));
        given(deckRepository.save(any(Deck.class))).willReturn(expectedDeck);

        // Act
        Deck actualDeck = deckService.shuffleDeck(deckId, 1);

        // Assert
        assertThat(actualDeck).isEqualTo(expectedDeck);
        assertThat(actualDeck.isShuffled()).isTrue();
    }

    @Test
    void givenNonExistingDeckId_whenShuffleDeck_thenThrowEntityNotFoundException() {
        // Arrange
        String deckId = "non-existing-deck-id";

        given(deckRepository.findById(deckId)).willReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> deckService.shuffleDeck(deckId, 1))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("No such Deck with ID `%s`", deckId);
    }

    @Test
    @Disabled("Need to extract shuffler to it's own class to allow for mocking")
    void givenValidDeckIdAndCount_whenDrawCardsFromDeck_thenReturnDrawnCards() {
        // Arrange
        String deckId = "deck-id";
        int count = 2;
        List<Card> cards = createCards(5);
        Deck existingDeck = createDeck(deckId, cards, true, false, cards.size());
        Deck expectedDeck = createDeck(deckId, cards.subList(0, count), true, false, cards.size() - count);

        given(deckRepository.findById(deckId)).willReturn(Optional.of(existingDeck));
        given(deckRepository.save(any(Deck.class))).willReturn(expectedDeck);

        // Act
        Deck actualDeck = deckService.drawCardsFromDeck(deckId, count);

        // Assert
        assertThat(actualDeck).isEqualTo(expectedDeck);
        assertThat(actualDeck.getCards()).hasSize(count);
        assertThat(actualDeck.getRemaining()).isEqualTo(cards.size() - count);
    }

    @Test
    void givenNonExistingDeckId_whenDrawCardsFromDeck_thenThrowEntityNotFoundException() {
        // Arrange
        String deckId = "non-existing-deck-id";
        int count = 2;

        given(deckRepository.findById(deckId)).willReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> deckService.drawCardsFromDeck(deckId, count))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("No such Deck with ID `%s`", deckId);
    }

    @Test
    void givenInvalidCount_whenDrawCardsFromDeck_thenThrowIllegalArgumentException() {
        // Arrange
        String deckId = "deck-id";
        int count = 10;
        List<Card> cards = createCards(5);
        Deck existingDeck = createDeck(deckId, cards, true, false, cards.size());

        given(deckRepository.findById(deckId)).willReturn(Optional.of(existingDeck));

        // Act & Assert
        assertThatThrownBy(() -> deckService.drawCardsFromDeck(deckId, count))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Not enough cards in the deck to draw %d cards", count);
    }

    @Test
    void givenExistingDeckId_whenGetDeck_thenReturnDeck() {
        // Arrange
        String deckId = "existing-deck-id";
        List<Card> cards = createCards(3);
        Deck expectedDeck = createDeck(deckId, cards, true, false, cards.size());

        given(deckRepository.findById(deckId)).willReturn(Optional.of(expectedDeck));

        // Act
        Deck actualDeck = deckService.getDeck(deckId);

        // Assert
        assertThat(actualDeck).isEqualTo(expectedDeck);
    }

    @Test
    void givenNonExistingDeckId_whenGetDeck_thenThrowEntityNotFoundException() {
        // Arrange
        String deckId = "non-existing-deck-id";

        given(deckRepository.findById(deckId)).willReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> deckService.getDeck(deckId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Deck with ID `%s` not found", deckId);
    }

    private List<Card> createCards(int count) {
        List<Card> cards = new java.util.ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            cards.add(new Card(Card.VALUES[i], Suit.SPADES));
        }
        return cards;
    }

    private Deck createDeck(String deckId, List<Card> cards, boolean success, boolean shuffled, int remaining) {
        return Deck.builder()
                .deckId(deckId)
                .success(success)
                .shuffled(shuffled)
                .cards(cards)
                .remaining(remaining)
                .build();
    }
}

