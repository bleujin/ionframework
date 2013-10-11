package net.ion.framework.mte;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import junit.framework.TestCase;

import net.ion.framework.mte.model.Article;
import net.ion.framework.mte.model.Customer;
import net.ion.framework.mte.model.Item;
import net.ion.framework.mte.model.Order;
import net.ion.framework.mte.util.Util;

public class TestLive extends TestCase {
	public static String template = Util.resourceToString("net/ion/framework/mte/template/email.jmte", "UTF-8");
	public static String expected = Util.resourceToString("net/ion/framework/mte/template/expected-output.txt", "UTF-8");

	public void testShop() throws Exception {
		Engine engine = Engine.createDefaultEngine();
		engine.config().registerRenderer(Date.class, new DateRenderer());
		engine.config().registerNamedRenderer(new CurrencyRenderer());

		String output = engine.transform(template, createModel());
		assertEquals(Util.unifyNewlines(expected), Util.unifyNewlines(output));
	}

	private Map<String, Object> createModel() {
		Map<String, Object> model = new HashMap<String, Object>();
		{
			Calendar instance = GregorianCalendar.getInstance(Locale.GERMAN);
			instance.set(2011, Calendar.JANUARY, 28);
			Date orderDate = instance.getTime();

			Customer customer = new Customer("Oliver", "Zeigermann", "Gaussstrasse 180\n" + "22765 Hamburg\n" + "GERMANY");
			Order order = new Order(customer, orderDate);

			Article article1 = new Article("How to become famous", new BigDecimal("17.80"));
			order.getItems().add(new Item(1, article1));

			Article article2 = new Article("Cool stuff", new BigDecimal("1.00"));
			order.getItems().add(new Item(2, article2));

			model.put("order", order);
			model.put("separator", "----------------");

		}
		return model;
	}

}

class CurrencyRenderer implements NamedRenderer {
	private static final DecimalFormatSymbols GERMANY = new DecimalFormatSymbols(Locale.GERMANY);
	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("##,##0.00", GERMANY);
	private static final String EURO_CHARACTER = "\u20AC";

	public RenderFormatInfo getFormatInfo() {
		return null;
	}

	public String getName() {
		return "currency";
	}

	public Class<?>[] getSupportedClasses() {
		return new Class<?>[] { BigDecimal.class };
	}

	public String render(Object o, String format, Locale locale) {
		if (o instanceof BigDecimal) {
			final NumberFormat numberFormat;
			if (format == null) {
				numberFormat = DECIMAL_FORMAT;
			} else {
				numberFormat = new DecimalFormat(format, GERMANY);
			}
			String formatted = numberFormat.format(o) + " " + EURO_CHARACTER;
			return formatted;
		}
		return null;
	}
}

class DateRenderer implements Renderer<Date> {
	DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.GERMAN);

	// DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM, ''yy", Locale.GERMAN);

	public String render(Date date, Locale locale) {
		String rendered = dateFormat.format(date);
		return rendered;
	}

}