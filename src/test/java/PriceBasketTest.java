import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PriceBasketTest {
    private static final BigDecimal ERROR = new BigDecimal(0.0001).setScale(4, RoundingMode.FLOOR);
    private static final List<String> SIMPLE_INPUT_1 = Arrays.asList("Milk", "Bread");
    private static final List<String> VALID_INPUT_1 = Arrays.asList("PriceBasket", "Milk", "Bread");
    private static final List<String> SIMPLE_INPUT_2 = Arrays.asList("Milk", "Bread", "Bread");
    private static final List<String> EMPTY_INPUT = Collections.singletonList("PriceBasket");
    private static final List<String> INVALID_INPUT_1 = Collections.singletonList("Something Else");
    private static final List<String> BASKET_INPUT_1 = Arrays.asList("Apple", "Apple", "Milk");
    private static final List<String> BASKET_INPUT_2 = Arrays.asList("Bread", "Soup", "Soup", "Milk");
    private static final List<String> BASKET_INPUT_3 = Arrays.asList("Bread", "Soup", "Soup", "Soup");
    private static final List<String> BASKET_INPUT_4 = Arrays.asList("Bread", "Bread", "Soup", "Soup");
    private static final List<String> BASKET_INPUT_5 = Arrays.asList("Bread", "Bread", "Soup", "Soup", "Soup", "Soup");
    private static final List<String> BASKET_INPUT_MIXED = Arrays.asList("Apple", "Apple", "Bread", "Soup", "Soup", "Soup");

    @Test
    public void testGetPriceForItem() {
        BigDecimal milkPrice = new BigDecimal(1.30);
        BigDecimal breadPrice = new BigDecimal(0.80);
        assertEquals(milkPrice, PriceBasket.getPriceForItem("Milk"));
        assertEquals(breadPrice, PriceBasket.getPriceForItem("Bread"));
        assertNull(PriceBasket.getPriceForItem("Non-existing"));
    }

    @Test
    public void testGeneratePrice() {
        BigDecimal expectedPrice1 = new BigDecimal(2.10);
        BigDecimal expectedPrice2 = new BigDecimal(2.90);
        BigDecimal expectedPrice3 = new BigDecimal(0.0);

        //We compare the generated price to the expected having in mind a slight precision error
        PriceBasket.countItems(SIMPLE_INPUT_1);
        assertEquals(-1, PriceBasket.generatePrice().subtract(expectedPrice1).compareTo(ERROR));
        PriceBasket.countItems(SIMPLE_INPUT_2);
        assertEquals(-1, PriceBasket.generatePrice().subtract(expectedPrice2).compareTo(ERROR));
        PriceBasket.countItems(EMPTY_INPUT);
        assertEquals(-1, PriceBasket.generatePrice().subtract(expectedPrice3).compareTo(ERROR));
    }

    @Test
    public void testCheckValidInput() {
        List<String> expected1 = Arrays.asList("Milk", "Bread");
        List<String> expected2 = Collections.emptyList();
        assertEquals(expected1, PriceBasket.checkInputAndRemovePriceBasket(VALID_INPUT_1));
        assertEquals(expected2, PriceBasket.checkInputAndRemovePriceBasket(EMPTY_INPUT));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckInvalidInputNoArgs() {
        PriceBasket.checkInputAndRemovePriceBasket(Collections.<String>emptyList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckInvalidInputNoPriceBasket() {
        PriceBasket.checkInputAndRemovePriceBasket(INVALID_INPUT_1);
    }

    @Test
    public void testCalculateAppleDiscount() {
        List<String> expected = Collections.singletonList("Apples 10% off: -0.20");
        PriceBasket.countItems(BASKET_INPUT_1);
        PriceBasket.generatePrice();
        assertEquals(expected, PriceBasket.calculateDiscounts());
    }

    @Test
    public void testCalculateBreadDiscount() {
        List<String> expected = Collections.singletonList("50% off of loaf of Bread if you buy 2 cans of Soup: -0.40");
        List<String> expected2 = Collections.singletonList("50% off of loaf of Bread if you buy 2 cans of Soup: -0.80");
        PriceBasket.countItems(BASKET_INPUT_2);
        PriceBasket.generatePrice();
        assertEquals(expected, PriceBasket.calculateDiscounts());

        //3 cans of soup - 1 loaf of bread
        PriceBasket.countItems(BASKET_INPUT_3);
        PriceBasket.generatePrice();
        assertEquals(expected, PriceBasket.calculateDiscounts());

        //2 cans of soup - 2 loaves of bread
        PriceBasket.countItems(BASKET_INPUT_4);
        PriceBasket.generatePrice();
        assertEquals(expected, PriceBasket.calculateDiscounts());

        //4 cans of soup - 2 loaves of bread
        PriceBasket.countItems(BASKET_INPUT_5);
        PriceBasket.generatePrice();
        assertEquals(expected2, PriceBasket.calculateDiscounts());
    }

    @Test
    public void testCalculateMixedDiscount() {
        List<String> expected = Arrays.asList("Apples 10% off: -0.20", "50% off of loaf of Bread if you buy 2 cans of Soup: -0.40");
        PriceBasket.countItems(BASKET_INPUT_MIXED);
        PriceBasket.generatePrice();
        assertEquals(expected, PriceBasket.calculateDiscounts());

        expected = Collections.singletonList("(No offers available)");
        PriceBasket.countItems(SIMPLE_INPUT_1);
        assertEquals(expected, PriceBasket.calculateDiscounts());
    }

}
