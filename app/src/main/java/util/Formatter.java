package util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public final class Formatter {
  private Formatter() {}

  public static String formatCurrency(BigDecimal amount) {
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
    return currencyFormatter.format(amount);
  }

  public static String formatCurrencyWithoutComma(BigDecimal amount) {
    return String.format("$%.0f", amount);
  }
}