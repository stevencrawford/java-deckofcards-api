package com.example.deckofcards.api.routes;

import com.example.deckofcards.api.model.Deck;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

import static org.apache.camel.model.rest.RestParamType.path;

@Component§
public class DeckApiRouter extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        restConfiguration()
                .component("servlet")
                .port(8080)
                .dataFormatProperty("prettyPrint", "true")
                .bindingMode(RestBindingMode.json)
                // turn on openapi api-doc
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "Deck API")
                .apiProperty("api.version", "1.0.0")
        ;

        rest("/deck")
                .description("Deck API")
                .consumes("application/json")
                .produces("application/json")

                .get("/{id}").description("Get a deck by ID")
                    .outType(Deck.class)
                    .param().name("id").type(path).description("The ID of the deck").dataType("string").endParam()
                    .responseMessage().code(200).message("Deck successfully returned").endResponseMessage()
                    .to("bean:deckService?method=getDeck(${header.id})")
        ;
    }
}
