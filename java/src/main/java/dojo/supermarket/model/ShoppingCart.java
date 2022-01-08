package dojo.supermarket.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCart {

    private final List<ProductQuantity> items = new ArrayList<>();
    Map<Product, Double> productQuantities = new HashMap<>();


    List<ProductQuantity> getItems() {
        return new ArrayList<>(items);
    }

    void addItem(Product product) {
        this.addItemQuantity(product, 1.0);
    }

    Map<Product, Double> productQuantities() {
        return productQuantities;
    }


    public void addItemQuantity(Product product, double quantity) {
        items.add(new ProductQuantity(product, quantity));
        if (productQuantities.containsKey(product)) {
            productQuantities.put(product, productQuantities.get(product) + quantity);
        } else {
            productQuantities.put(product, quantity);
        }
    }

    void handleOffers(Receipt receipt, Map<Product, Offer> offers, SupermarketCatalog catalog) {
        for (Product p: productQuantities().keySet()) {
            double quantity = productQuantities.get(p);
            if (offers.containsKey(p)) {
                Offer offer = offers.get(p);
                double unitPrice = catalog.getUnitPrice(p);
                int quantityAsInt = (int) quantity;
                Discount discount = null;
                int bought_products = 1;
                int bought_products_ratio = quantityAsInt / bought_products;
                if (offer.offerType == SpecialOfferType.ThreeForTwo) {
                    bought_products  = 3;

                } else if (offer.offerType == SpecialOfferType.TwoForAmount) {
                    bought_products  = 2;
                    if (quantityAsInt >= bought_products) {
                        double total = offer.argument * (quantityAsInt / bought_products ) + quantityAsInt % bought_products * unitPrice;
                        double discountN = unitPrice * quantity - total;
                        discount = new Discount(p, bought_products + " for " + offer.argument, -discountN);
                    }

                } if (offer.offerType == SpecialOfferType.FiveForAmount) {
                    bought_products  = 5;
                }

                if (offer.offerType == SpecialOfferType.ThreeForTwo && quantityAsInt >= bought_products) {
                    double discountAmount = quantity * unitPrice - ((bought_products_ratio  * 2 * unitPrice) + quantityAsInt % bought_products * unitPrice);
                    discount = new Discount(p, bought_products + " for 2", -discountAmount);
                }
                if (offer.offerType == SpecialOfferType.TenPercentDiscount) {
                    discount = new Discount(p, offer.argument + "% off", -quantity * unitPrice * offer.argument / 100.0);
                }
                if (offer.offerType == SpecialOfferType.FiveForAmount && quantityAsInt >= bought_products) {
                    double discountTotal = unitPrice * quantity - (offer.argument * bought_products_ratio + quantityAsInt % bought_products * unitPrice);
                    discount = new Discount(p, bought_products  + " for " + offer.argument, -discountTotal);
                }
                if (discount != null)
                    receipt.addDiscount(discount);
            }

        }
    }
}