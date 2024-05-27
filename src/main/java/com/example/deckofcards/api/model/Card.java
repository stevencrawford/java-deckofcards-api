package com.example.deckofcards.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static java.lang.String.format;

@Builder
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"code", "image", "images", "value", "suit"})
public class Card {

    private static final String VALUES_AS_STRING = "A,2,3,4,5,6,7,8,9,0,J,Q,K";
    public static final String[] VALUES = VALUES_AS_STRING.split(",");

    private String value;
    private Suit suit;

    public Card(final String value, final Suit suit) {
        this.value = value;
        this.suit = suit;
    }

    @JsonProperty
    String code() {
        return value + suit.symbol;
    }

    @JsonProperty
    String image() {
        return images().png();
    }

    @JsonProperty
    Images images() {
        return new Images(
                format("http://localhost:8080/img/%s.svg", code()),
                format("http://localhost:8080/img/%s.png", code())
        );
    }

    public static Card parseString(final String valueSuit) {
        String value = valueSuit.substring(0, 1);
        if (!VALUES_AS_STRING.contains(value)) {
            throw new IllegalArgumentException("`%s` is not a valid Card");
        }

        Suit suit = Suit.fromSymbol(valueSuit.charAt(1));
        return new Card(value, suit);
    }

    public record Images(String svg, String png) {}

}
