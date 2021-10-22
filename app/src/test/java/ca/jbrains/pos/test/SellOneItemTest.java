package ca.jbrains.pos.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class SellOneItemTest {
    @Test
    void productFound() {
        Display display = new Display();
        Sale sale = new Sale(display, new HashMap<>() {{
            put("12345", "EUR 7.95");
        }});

        sale.onBarcode("12345");

        Assertions.assertEquals("EUR 7.95", display.getText());
    }

    @Test
    void anotherProductFound() {
        Display display = new Display();
        Sale sale = new Sale(display, new HashMap<>() {{
            put("23456", "EUR 12.50");
        }});

        sale.onBarcode("23456");

        Assertions.assertEquals("EUR 12.50", display.getText());
    }

    @Test
    void productNotFound() {
        Display display = new Display();
        Sale sale = new Sale(display, new HashMap<>());

        sale.onBarcode("99999");

        Assertions.assertEquals("Product not found: 99999", display.getText());
    }

    @Test
    void emptyBarcode() {
        Display display = new Display();
        Sale sale = new Sale(display, null);

        sale.onBarcode("");

        Assertions.assertEquals("Scanning error: empty barcode", display.getText());
    }

    public static class Sale {
        private Display display;
        private Map<String, String> pricesByBarcode;

        public Sale(Display display, Map<String, String> pricesByBarcode) {
            this.display = display;
            this.pricesByBarcode = pricesByBarcode;
        }

        public void onBarcode(String barcode) {
            if ("".equals(barcode)) {
                displayEmptyBarcodeMessage();
            } else {
                String price = findPrice(barcode);
                if (price != null)
                    displayPrice(price);
                else
                    displayProductNotFoundMessage(barcode);
            }
        }

        private void findPriceThenDisplayPrice(String barcode) {
            displayPrice(findPrice(barcode));
        }

        private void displayPrice(String price) {
            display.setText(price);
        }

        private String findPrice(String barcode) {
            return pricesByBarcode.get(barcode);
        }

        private void displayProductNotFoundMessage(String barcode) {
            display.setText(String.format("Product not found: %s", barcode));
        }

        private void displayEmptyBarcodeMessage() {
            display.setText("Scanning error: empty barcode");
        }
    }

    public static class Display {
        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
