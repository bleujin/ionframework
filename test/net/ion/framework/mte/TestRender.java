package net.ion.framework.mte;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import net.ion.framework.util.Debug;
import net.ion.framework.util.MapUtil;
import net.ion.framework.util.StringUtil;

import junit.framework.TestCase;

public class TestRender extends TestCase {

	private Engine engine;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.engine = Engine.createDefaultEngine() ;
	}

	public void testNamedRender() throws Exception {

		engine.config().registerNamedRenderer(new NamedRenderer() {
			private DecimalFormatSymbols germany = new DecimalFormatSymbols(Locale.GERMANY);

			public RenderFormatInfo getFormatInfo() {
				return null;
			}

			public String getName() {
				return "currency";
			}

			public Class<?>[] getSupportedClasses() {
				return new Class<?>[] { BigDecimal.class };
			}

			public String render(Object o, String format) {
				if (StringUtil.isNotBlank(format) && "myform".equals(format)) {
					return "0" ; 
				} else if (o instanceof BigDecimal) {
					return new DecimalFormat(format != null ? format : "##,##0.00 €", germany).format(o);
				}
				return null;
			}

			public String render(Object o, String format, Locale locale) {
				return render(o, format);
			}
		});
		
		assertEquals("total 1.234.567,89 €", engine.transform("total ${total;currency}",  MapUtil.<String,Object>create("total", new BigDecimal(1234567.89))));
		assertEquals("total 0", engine.transform("total ${total;currency[myform]}",  MapUtil.<String,Object>create("total", new BigDecimal(1234567.89)))); // format
	}
	
	
	
	
	
}
