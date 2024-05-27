package com.example.deckofcards.api.contoller;

import com.example.deckofcards.api.model.Card;
import com.example.deckofcards.api.model.Deck;
import com.example.deckofcards.api.model.Suit;
import com.example.deckofcards.api.service.DeckService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@WebMvcTest(DeckController.class)
public class DeckControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeckService deckService;

    @BeforeEach
    public void setup() {
        this.mockMvc = standaloneSetup(new DeckController(deckService)).build();
    }

    @Test
    public void givenNewDeckAndShuffleCall_whenDefaultDeckCount_thenReturnShuffledDeck() throws Exception {
        // arrange
        Deck deck = Deck.builder().deckId("deck-id").build();
        given(deckService.shuffleDeck(Deck.NEW, 1)).willReturn(deck);

        // act
        MockHttpServletRequestBuilder request = post("/api/deck/new/shuffle");
        ResultActions result = mockMvc.perform(request);

        // assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.deck_id").value("deck-id"))
                .andExpect(jsonPath("$.cards").isEmpty());
    }

    // ... (other test methods for newDeckAndShuffle remain the same)

    @Test
    public void givenGetDeckCall_whenValidDeckId_thenReturnDeck() throws Exception {
        // arrange
        Card card = new Card("ACE", Suit.SPADES);
        Deck deck = Deck.builder().deckId("deck-id").cards(List.of(card)).build();
        given(deckService.getDeck("deck-id")).willReturn(deck);

        // act
        MockHttpServletRequestBuilder request = get("/api/deck/deck-id");
        ResultActions result = mockMvc.perform(request);

        // assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.deck_id").value("deck-id"))
                .andExpect(jsonPath("$.cards.length()").value(1))
                .andExpect(jsonPath("$.cards[0].value").value("ACE"))
                .andExpect(jsonPath("$.cards[0].suit").value("SPADES"));
    }

    @Test
    public void givenNewDeckCall_thenReturnNewDeck() throws Exception {
        // arrange
        Deck deck = Deck.builder().deckId("new-deck-id").build();
        given(deckService.createDecks(1)).willReturn(deck);

        // act
        MockHttpServletRequestBuilder request = post("/api/deck/new");
        ResultActions result = mockMvc.perform(request);

        // assert
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.deck_id").value("new-deck-id"))
                .andExpect(jsonPath("$.cards").isEmpty());
    }

    @Test
    public void givenShuffleDeckCall_whenValidDeckId_thenReturnShuffledDeck() throws Exception {
        // arrange
        Card card = new Card("KING", Suit.HEARTS);
        Deck deck = Deck.builder().deckId("shuffled-deck-id").cards(List.of(card)).build();
        given(deckService.shuffleDeck("deck-id", 1)).willReturn(deck);

        // act
        MockHttpServletRequestBuilder request = post("/api/deck/deck-id/shuffle");
        ResultActions result = mockMvc.perform(request);

        // assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.deck_id").value("shuffled-deck-id"))
                .andExpect(jsonPath("$.cards.length()").value(1))
                .andExpect(jsonPath("$.cards[0].value").value("KING"))
                .andExpect(jsonPath("$.cards[0].suit").value("HEARTS"));
    }

    @Test
    public void givenDrawCardsCall_whenValidDeckIdAndCount_thenReturnDrawnCards() throws Exception {
        // arrange
        List<Card> cards = List.of(new Card("ACE", Suit.SPADES), new Card("KING", Suit.HEARTS));
        Deck deck = Deck.builder().deckId("drawn-deck-id").cards(cards).build();
        given(deckService.drawCardsFromDeck("deck-id", 2)).willReturn(deck);

        // act
        MockHttpServletRequestBuilder request = get("/api/deck/deck-id/draw?count=2");
        ResultActions result = mockMvc.perform(request);

        // assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.deck_id").value("drawn-deck-id"))
                .andExpect(jsonPath("$.cards.length()").value(2))
                .andExpect(jsonPath("$.cards[0].value").value("ACE"))
                .andExpect(jsonPath("$.cards[0].suit").value("SPADES"))
                .andExpect(jsonPath("$.cards[1].value").value("KING"))
                .andExpect(jsonPath("$.cards[1].suit").value("HEARTS"));
    }
}
