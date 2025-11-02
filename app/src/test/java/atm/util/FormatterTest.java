package atm.util;

import org.junit.jupiter.api.Test;

public class FormatterTest {
  
  @Test
  void testFormatCurrencyWithoutComma() {
    String formatted = Formatter.formatCurrencyWithoutComma(new java.math.BigDecimal("1234567.89"));
    assert(formatted.equals("$1234568"));
  }
}
