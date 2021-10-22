package ca.jbrains.pos.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class InMemoryCatalogTest {
    // These tests refer only to the _contract_ of Catalog, but not
    // a specific implementation.

    @Test
    void productFound() {
        Assertions.assertEquals(
                "EUR 7.95",
                catalogWith("12345", "EUR 7.95")
                        .findPrice("12345"));
    }

    @Test
    void productNotFound() {
        Assertions.assertEquals(
                null,
                catalogWithout("12345").findPrice("12345"));
    }

    // Only these "factory methods" care about the specific implementation of
    // Catalog. We can separate the factory methods from the contract tests
    // into separate classes, either using inheritance or a combination of
    // the Parameterized Test Case Pattern + Abstract Factory Pattern.
    // (The exercise is left to you to try. :) )
    private Catalog catalogWithout(String barcodeToAvoid) {
        return new InMemoryCatalog(new HashMap<>() {{
            put("not " + barcodeToAvoid, "::invalid price::");
            put("also not " + barcodeToAvoid, "::invalid price::");
            put("this is not " + barcodeToAvoid + ", you idiot", "::invalid price::");
        }});
    }

    private Catalog catalogWith(String barcode, String matchingPrice) {
        return new InMemoryCatalog(new HashMap<>() {{
            put("not " + barcode, "::invalid price::");
            put("also not " + barcode, "::invalid price::");
            put(barcode, matchingPrice);
            put("this is not " + barcode + ", you idiot", "::invalid price::");
        }});
    }
}
