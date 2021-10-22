package ca.jbrains.pos.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class InMemoryCatalogTest {

    @Test
    void productFound() {
        // This test refers only to the _contract_ of Catalog, but not
        // a specific implementation.
        Assertions.assertEquals(
                "EUR 7.95",
                catalogWith("12345", "EUR 7.95")
                        .findPrice("12345"));
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
