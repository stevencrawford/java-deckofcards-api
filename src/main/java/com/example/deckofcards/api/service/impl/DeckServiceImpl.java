package com.example.deckofcards.api.service.impl;

import com.example.deckofcards.api.common.exception.EntityNotFoundException;
import com.example.deckofcards.api.model.Card;
import com.example.deckofcards.api.model.Deck;
import com.example.deckofcards.api.model.Suit;
import com.example.deckofcards.api.repository.DeckRepository;
import com.example.deckofcards.api.service.DeckService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.example.deckofcards.api.model.Card.VALUES;
import static java.lang.String.format;

@Component
@RequiredArgsConstructor
public class DeckServiceImpl implements DeckService {

    private final DeckRepository deckRepository;

    @Override
    public Deck createDecks(final int count) {
        List<Card> cards = Deck.copyNTimes(
                Arrays.stream(Suit.values())
                    .flatMap(suit -> Arrays.stream(VALUES)
                            .map(value -> new Card(value, suit)))
                    .toList()
                , count
        );

        Deck deck = Deck.builder()
                .success(true)
                .deckId(Deck.IdGenerator.generateID())
                .cards(cards)
                .shuffled(false)
                .remaining(cards.size())
                .build();
        return deckRepository.save(deck);
    }

    @Override
    public Deck createPartialDeck(final List<Card> cards) {
        Deck deck = Deck.builder()
                .success(true)
                .deckId(Deck.IdGenerator.generateID())
                .cards(cards)
                .shuffled(false)
                .remaining(cards.size())
                .build();
        return deckRepository.save(deck);
    }

    @Override
    public Deck shuffleDeck(final String deckId, int deckCount) {
        Optional<Deck> byDeckId = Deck.NEW.equals(deckId) ? Optional.of(createDecks(deckCount))
                : deckRepository.findById(deckId);

        if (byDeckId.isEmpty()) {
            throw new EntityNotFoundException(format("No such Deck with ID `%s`", deckId));
        }
        Deck existing = byDeckId.get();
        existing.shuffle();
        deckRepository.save(existing);
        return existing;
    }

    @Override
    public Deck drawCardsFromDeck(final String deckId, final int count) {
        Optional<Deck> byDeckId = deckRepository.findById(deckId);
        if (byDeckId.isEmpty()) {
            throw new EntityNotFoundException(format("No such Deck with ID `%s`", deckId));
        }
        Deck existing = byDeckId.get();

        if (count > existing.getRemaining()) {
            throw new IllegalArgumentException(format("Not enough cards in the deck to draw %d cards", count));
        }

        // shuffle the cards then take `count` cards from the beginning
        List<Card> randomCards = new ArrayList<>(existing.getCards());
        Collections.shuffle(randomCards);
        existing.setCards(randomCards.subList(0, count));
        existing.setRemaining(existing.getRemaining() - count);
        return existing;
    }

    @Override
    public Deck getDeck(String deckId) {
        return deckRepository.findById(deckId)
                .orElseThrow(() -> new EntityNotFoundException(format("Deck with ID `%s` not found", deckId)));
    }

}
