package dojo.supermarket.model;

import java.util.HashMap;
import java.util.Map;

public class  SupermarketCatalog {
    private Map<String, Double> prices = new HashMap<>();

    public void addProductPrice(Product product, double price) {
        this.prices.put(product.getName(), price);
    }

    public double getProductPrice(Product p) {
        return this.prices.get(p.getName());
    }
}