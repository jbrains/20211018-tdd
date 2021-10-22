package ca.jbrains.pos.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

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
        Sale sale = new Sale(display, new InMemoryCatalog(new HashMap<String, String>()));

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

    public static class InMemoryCatalog implements Catalog {
        private Map<String, String> pricesByBarcode;

        public InMemoryCatalog(Map<String, String> pricesByBarcode) {
            this.pricesByBarcode = pricesByBarcode;
        }

        @Override
        public String findPrice(String barcode) {
            return pricesByBarcode.get(barcode);
        }
    }
}
