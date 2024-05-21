package com.example.deckofcards.api.contoller;

import com.example.deckofcards.api.model.Card;
import com.example.deckofcards.api.model.Deck;
import com.example.deckofcards.api.service.DeckService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Arrays;

@RestController
@RequestMapping("/api/deck")
@RequiredArgsConstructor
public class DeckController {

    private final DeckService deckService;

    @GetMapping("{deckId}")
    public ResponseEntity<Deck> getDeck(@PathVariable("deckId") final String deckId) {
        return ResponseEntity.ok(deckService.getDeck(deckId));
    }

    @PostMapping("new")
    public ResponseEntity<Deck> newDeck() {
        Deck deck = deckService.createDecks(1);
        return ResponseEntity.created(URI.create(deck.getDeckId()))
                .body(deck);
    }

    @PostMapping("new/shuffle")
    public ResponseEntity<Deck> newDeckAndShuffle(@RequestParam(value = "deck_count", required = false, defaultValue = "1") final int deckCount,
                                                  @RequestParam(value = "remaining", required = false) final boolean remaining,
                                                  @RequestParam(value = "cards", required = false) final String cards) {
        String deckId = Deck.NEW;
        if (StringUtils.hasText(cards)) {
            Deck partialDeck = deckService.createPartialDeck(
                    Arrays.stream(cards.split(","))
                            .map(Card::parseString).toList()
            );
            deckId = partialDeck.getDeckId();
        }

        Deck deck = deckService.shuffleDeck(deckId, deckCount);
        return ResponseEntity.ok(deck);
    }

    @PostMapping("{deckId}/shuffle")
    public ResponseEntity<Deck> shuffle(@PathVariable("deckId") final String deckId,
                                        @RequestParam(value = "deck_count", required = false, defaultValue = "1") final int deckCount,
                                        @RequestParam(value = "remaining", required = false) final boolean remaining) {
        Deck deck = deckService.shuffleDeck(deckId, deckCount);
        return ResponseEntity.ok(deck);
    }

    @GetMapping("{deckId}/draw")
    public ResponseEntity<Deck> drawCardsFromDeck(@PathVariable("deckId") final String deckId,
                                                  @RequestParam(value = "count", defaultValue = "1") final int count) {
        return ResponseEntity.ok(deckService.drawCardsFromDeck(deckId, count));
    }

}
