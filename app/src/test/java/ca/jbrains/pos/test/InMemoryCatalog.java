package ca.jbrains.pos.test;

import java.util.Map;

public class InMemoryCatalog implements Catalog {
    private Map<String, String> pricesByBarcode;

    public InMemoryCatalog(Map<String, String> pricesByBarcode) {
        this.pricesByBarcode = pricesByBarcode;
    }

    @Override
    public String findPrice(String barcode) {
        return pricesByBarcode.get(barcode);
    }
}
