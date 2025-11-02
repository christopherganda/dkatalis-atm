package atm.util;

import java.math.BigDecimal;

public final class Formatter {
  private Formatter() {}

  public static String formatCurrencyWithoutComma(BigDecimal amount) {
    return String.format("$%.0f", amount);
  }
}