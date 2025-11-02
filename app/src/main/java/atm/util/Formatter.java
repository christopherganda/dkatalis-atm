package atm.util;

import java.math.BigDecimal;

public final class Formatter {
  private Formatter() {}

  public static String formatCurrencyWithoutComma(BigDecimal amount) {
    return "$" + amount.setScale(0, BigDecimal.ROUND_HALF_UP).toPlainString();
  }
}