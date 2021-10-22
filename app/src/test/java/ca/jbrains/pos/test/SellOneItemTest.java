package ca.jbrains.pos.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;

public class SellOneItemTest {
    @Test
    void productFound() {
        // Notice that this test is now simpler, because we can simulate the abstraction of Catalog
        // directly with a lambda expression or a stub, instead of using a lookup table as a
        // "Lightweight Implementation".
        // This lambda expression _is_ the Catalog!
        InMemoryDisplay display = new InMemoryDisplay();
        Sale sale = new Sale(display, barcode -> "EUR 7.95");

        sale.onBarcode("12345");

        Assertions.assertEquals("EUR 7.95", display.getText());
    }

    @Test
    void productFound_mockDisplay() {
        // Compare this version to the original version of the test.
        // Which do you prefer?
        // This version is more abstract, but does it seem "too strange" to you?
        // Not everyone likes this approach, but it's a good one to know about.
        Display display = Mockito.mock(Display.class);

        Sale sale = new Sale(display, barcode -> "EUR 7.95");

        sale.onBarcode("12345");

        // "Assert that the price EUR 7.95 was displayed"
        Mockito.verify(display).displayPrice("EUR 7.95");

        // Only use verify when you are checking a _desired side-effect_ in a test.
        // Don't use verify for queries; only use them for _actions_.
    }

    @Test
    void anotherProductFound() {
        InMemoryDisplay display = new InMemoryDisplay();
        Sale sale = new Sale(display, new InMemoryCatalog(new HashMap<String, String>() {{
            put("23456", "EUR 12.50");
        }}));

        sale.onBarcode("23456");

        Assertions.assertEquals("EUR 12.50", display.getText());
    }

    @Test
    void productNotFound() {
        InMemoryDisplay display = new InMemoryDisplay();
        // This now more-directly expresses the code of "product not found" by saying
        // "Pretend that the price of this barcode is null" without worrying about
        // how to "trick" the Catalog implementation into doing this. Even when the
        // implementation of Catalog changes, this test remains the same.
        Sale sale = new Sale(display, barcode -> null);

        sale.onBarcode("99999");

        Assertions.assertEquals("Product not found: 99999", display.getText());
    }

    @Test
    void emptyBarcode() {
        InMemoryDisplay display = new InMemoryDisplay();
        Sale sale = new Sale(display, null);

        sale.onBarcode("");

        Assertions.assertEquals("Scanning error: empty barcode", display.getText());
    }

    public static class Sale {
        private final Catalog catalog;
        private Display display;

        public Sale(Display display, Catalog catalog) {
            this.display = display;
            this.catalog = catalog;
        }

        public void onBarcode(String barcode) {
            if ("".equals(barcode)) {
                display.displayEmptyBarcodeMessage();
            } else {
                String price = catalog.findPrice(barcode);
                if (price != null)
                    display.displayPrice(price);
                else
                    display.displayProductNotFoundMessage(barcode);
            }
        }
    }

    public static class InMemoryDisplay implements Display {
        private String text;

        public String getText() {
            return text;
        }

        @Override
        public void displayPrice(String price) {
            this.text = price;
        }

        @Override
        public void displayProductNotFoundMessage(String barcode) {
            this.text = String.format("Product not found: %s", barcode);
        }

        @Override
        public void displayEmptyBarcodeMessage() {
            this.text = "Scanning error: empty barcode";
        }
    }
}
