import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Bidi;
import java.util.*;

public class PriceBasket {
    private static final String APPLE = "Apple";
    private static final String BREAD = "Bread";
    private static final String MILK = "Milk";
    private static final String SOUP = "Soup";
    private static final BigDecimal APPLE_DISCOUNT = new BigDecimal(0.10);
    private static final BigDecimal SOUP_PRICE = new BigDecimal(0.65);
    private static final BigDecimal BREAD_PRICE = new BigDecimal(0.80);
    private static final BigDecimal MILK_PRICE = new BigDecimal(1.30);
    private static final BigDecimal APPLE_PRICE = new BigDecimal(1.00);
    private static final BigDecimal ZERO = new BigDecimal(0);
    private static BigDecimal total = new BigDecimal(0.0);

    private static final Map<String, BigDecimal> itemsToPrices = new HashMap<>();
    private static final Map<String, Integer> itemCount = new HashMap<>();

    static {
        itemsToPrices.put(SOUP, SOUP_PRICE);
        itemsToPrices.put(BREAD, BREAD_PRICE);
        itemsToPrices.put(MILK, MILK_PRICE);
        itemsToPrices.put(APPLE, APPLE_PRICE);
    }

    public static void main(String[] args) {
        List<String> itemsInBasket;
        try {
            itemsInBasket = checkInputAndRemovePriceBasket(Arrays.asList(args));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }

        countItems(itemsInBasket);

        System.out.println("Subtotal: £" + generatePrice().setScale(2, RoundingMode.FLOOR));
        for (String discount : calculateDiscounts())
            System.out.println(discount);
        System.out.println("Total: £" + total.setScale(2, RoundingMode.FLOOR));
    }

    /**
     * Given a list of items updates the Map that contains all the products and their counts
     */
    public static void countItems(List<String> items) {
        int appleCount = 0, soupCount = 0, breadCount = 0, milkCount = 0;

        for (String item : items) {
            switch (item) {
                case APPLE:
                    appleCount++;
                    break;
                case SOUP:
                    soupCount++;
                    break;
                case BREAD:
                    breadCount++;
                    break;
                case MILK:
                    milkCount++;
                    break;
            }
        }

        itemCount.put(APPLE, appleCount);
        itemCount.put(SOUP, soupCount);
        itemCount.put(BREAD, breadCount);
        itemCount.put(MILK, milkCount);
    }

    /**
     * Given the list of arguments we check if the input is valid
     * Throws IllegalArgumentException if input is not of the correct format
     */
    static List<String> checkInputAndRemovePriceBasket(List<String> input) {
        if (input.size() == 0 || !input.get(0).equals("PriceBasket"))
            throw new IllegalArgumentException("Input should be of format: PriceBasket item1 item2 item3...");
        return input.subList(1, input.size());
    }

    /**
     * Using the map of items and their counts generates the subtotal before any discounts
     */
    static BigDecimal generatePrice() {
        BigDecimal subTotal = ZERO;

        for (Map.Entry<String, Integer> entry : itemCount.entrySet()) {
            subTotal = subTotal.add(getPriceForItem(entry.getKey()).multiply(new BigDecimal(entry.getValue())));
        }

        total = subTotal;
        return subTotal;
    }

    /**
     * Simple method to return price of item
     */
    static BigDecimal getPriceForItem(String item) {
        return itemsToPrices.get(item);
    }

    /**
     * Calculate the discounts for the items in the basket
     */
    static List<String> calculateDiscounts() {
        List<String> offersList = new ArrayList<>();

        //calculate apple discount
        if (itemCount.get(APPLE) != 0) {
            BigDecimal appleDiscount = APPLE_DISCOUNT.multiply(new BigDecimal(itemCount.get(APPLE)));
            offersList.add("Apples 10% off: -" + appleDiscount.setScale(2, RoundingMode.FLOOR));
            total = total.subtract(appleDiscount);
        }

        //calculate soup discount
        if (itemCount.get(SOUP) > 1 && itemCount.get(BREAD) > 0) {
            BigDecimal breadDiscount = ZERO;
            int soupCount = itemCount.get(SOUP);
            for (int i = 0; i < itemCount.get(BREAD); i++) {
                if (soupCount > 1) {
                    breadDiscount = breadDiscount.add(getPriceForItem(BREAD).divide(new BigDecimal(2)));
                    soupCount -= 2;
                }
            }
            if (breadDiscount.compareTo(ZERO) > 0) {
                offersList.add("50% off of loaf of Bread if you buy 2 cans of Soup: -" + breadDiscount.setScale(2, RoundingMode.FLOOR));
                total = total.subtract(breadDiscount);
            }
        }

        if (offersList.isEmpty())
            offersList.add("(No offers available)");

        return offersList;
    }
}
