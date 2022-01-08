package dojo.supermarket.model;

public class Offer {
    SpecialOfferType offerType;
    private final Product product;
    double argument;

    public Offer(SpecialOfferType offerType, Product product, double argument) {
        this.offerType = offerType;
        this.argument = argument;
        this.product = product;
    }


    public Discount handel_offer_discount(Product p, double quantity, double unitPrice){
        if (this.offerType == SpecialOfferType.TenPercentDiscount){
            return new Discount(p, this.argument + "% off", -quantity * unitPrice * this.argument / 100.0);
        }
        else if (this.offerType == SpecialOfferType.ThreeForTwo){
            return Calculate_Discount(p, 3, 2, unitPrice, (int)quantity, unitPrice);
        }
        else if (this.offerType == SpecialOfferType.TwoForAmount){
            return Calculate_Discount(p, 2, this.argument, 1, (int)quantity, unitPrice);
        }
        else if (this.offerType == SpecialOfferType.FiveForAmount){
            return Calculate_Discount(p, 5, this.argument, 1, (int)quantity, unitPrice);
        }
        return null;
    }

    Discount Calculate_Discount(Product p, int bought_products, double offer_amount, double price_coefficient, int quantity, double unitPrice){
            int bought_products_ratio = quantity / bought_products;
            if (quantity >= bought_products) {
                double discountTotal = unitPrice * quantity - (offer_amount * bought_products_ratio * price_coefficient + quantity % bought_products * unitPrice);
                return new Discount(p, bought_products  + " for " + offer_amount, -discountTotal);
            }
            return null;
    }
    Product getProduct() {
        return this.product;
    }

}
