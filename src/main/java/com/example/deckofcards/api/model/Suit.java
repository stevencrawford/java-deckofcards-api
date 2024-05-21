package com.example.deckofcards.api.model;

import lombok.Getter;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum Suit {
    HEARTS('H'), DIAMONDS('D'), CLUBS('C'), SPADES('S');

    private static final Map<Character, Suit> SYMBOL_TO_SUIT_MAP = Stream.of(values())
            .collect(Collectors.toUnmodifiableMap(Suit::getSymbol, Function.identity()));

    final char symbol;

    Suit(char s) {
        this.symbol = s;
    }

    public static Suit fromSymbol(char symbol) {
        return SYMBOL_TO_SUIT_MAP.get(symbol);
    }

}
