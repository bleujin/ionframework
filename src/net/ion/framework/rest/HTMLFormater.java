package net.ion.framework.rest;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.rope.Rope;
import net.ion.framework.rope.RopeBuilder;
import net.ion.framework.rope.RopeWriter;
import net.ion.framework.util.StringUtil;

import org.restlet.data.CharacterSet;
import org.restlet.data.Language;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;

public class HTMLFormater implements ResultSetHandler, IRowsRepresentationHandler, IMapListRepresentationHandler {

	// @Override
	public Representation toRepresentation(IRequest req, ResultSet rs, IResponse res) {
		try {
			Rope rope = RopeBuilder.build();
			rope = rope.addTo("<html><head><title>HTMLFormater</title></head><body>");
			rope = rope.addTo(req.toHTML());
			rope = rope.addTo(toHTML(rs));
			rope = rope.addTo(res.toHTML());
			rope = rope.addTo("</body></html>");

			return new RopeRepresentation(rope, MediaType.TEXT_HTML, Language.ALL, CharacterSet.UTF_8);
		} catch (SQLException e) {
			throw new ResourceException(e);
		}
	}

	// @Override
	public Representation toRepresentation(IRequest req, List<Map<String, ? extends Object>> datas, IResponse res) throws ResourceException {
		RopeWriter rw = new RopeWriter();

		for (Map<String, ?> infoMap : datas) {
			for (Entry<String, ?> entry : infoMap.entrySet()) {
				rw.append(entry.getKey(), " : ", StringUtil.toString(entry.getValue()), "<br/>");
			}
		}

		return new RopeRepresentation(rw.getRope(), MediaType.TEXT_HTML, Language.ALL, CharacterSet.UTF_8);
	}

	// @Override
	public Object handle(ResultSet rs) throws SQLException {
		Rope rope = RopeBuilder.build();
		rope = rope.addTo("<html><head><title>HTMLFormater</title></head><body>");
		rope = rope.addTo(toHTML(rs));
		rope = rope.addTo("</body></html>");

		return new RopeRepresentation(rope, MediaType.TEXT_HTML, Language.ALL, CharacterSet.UTF_8);
	}

	private Rope toHTML(ResultSet rs) throws SQLException {
		Rope rope = RopeBuilder.build();
		rope = rope.addTo("\n<table border=1 cellpadding=0 cellspacing=0 width='100%' class='body_table'>\n");

		rope = rope.addTo("\t<tr>");
		ResultSetMetaData meta = rs.getMetaData();
		for (int j = 1, last = meta.getColumnCount(); j <= last; j++) {
			rope = rope.addTo("<th>");
			rope = rope.addTo(meta.getColumnName(j));
			rope = rope.addTo("</th>");
		}
		rope = rope.addTo("</tr>\n");

		while (rs.next()) {
			rope = rope.addTo("\t<tr>");
			for (int j = 1, last = meta.getColumnCount(); j <= last; j++) {
				rope = rope.addTo("<td class='text_center'>");
				rope = rope.addTo(rs.getString(j));
				rope = rope.addTo("</td>");
			}
			rope = rope.addTo("</tr>\n");
		}
		rope = rope.addTo("</table>\n");

		return rope;
	}

}
