package com.example.deckofcards.api.repository;

import com.example.deckofcards.api.model.Deck;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeckRepository extends CrudRepository<Deck, String> {}
