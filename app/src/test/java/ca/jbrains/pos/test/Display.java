package ca.jbrains.pos.test;

public interface Display {
    void displayPrice(String price);

    void displayProductNotFoundMessage(String barcode);

    void displayEmptyBarcodeMessage();
}
