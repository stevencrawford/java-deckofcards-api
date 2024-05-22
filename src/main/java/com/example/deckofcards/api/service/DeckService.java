package com.example.deckofcards.api.service;

import com.example.deckofcards.api.model.Card;
import com.example.deckofcards.api.model.Deck;

import java.util.List;

public interface DeckService {

    Deck createDecks(final int count);

    Deck createPartialDeck(final List<Card> cards);

    Deck getDeck(final String deckId);

    Deck shuffleDeck(final String deckId, int deckCount);

    Deck drawCardsFromDeck(final String deckId, int count);

}
