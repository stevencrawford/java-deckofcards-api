package com.example.deckofcards.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"success", "deck_id", "shuffled", "cards", "remaining"})
@RedisHash("Deck")
public class Deck implements Serializable {

    public static final String NEW = "new";

    @Id
    @JsonProperty("deck_id")
    private String deckId;

    private boolean success;

    private boolean shuffled;

    private List<Card> cards;

    private int remaining;

    public void shuffle() {
        Collections.shuffle(cards);
        this.shuffled = true;
    }

    public static class IdGenerator {
        public static String generateID() {
            return UUID.randomUUID().toString()
                    .replace("-", "")
                    .substring(0, 11);
        }
    }

    public static <Card> List<Card> copyNTimes(List<Card> cards, int n) {
        List<Card> copies = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            copies.addAll(cards);
        }
        return copies;
    }
}
