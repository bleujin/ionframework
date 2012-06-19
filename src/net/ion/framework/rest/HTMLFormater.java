package net.ion.framework.rest;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.util.StringUtil;

import org.restlet.data.CharacterSet;
import org.restlet.data.Language;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;

public class HTMLFormater implements ResultSetHandler, IRowsRepresentationHandler, IMapListRepresentationHandler {

	private static final long serialVersionUID = -7657937688679783171L;

	// @Override
	public Representation toRepresentation(IRequest req, ResultSet rs, IResponse res) {
		try {
			StringWriter rope = new StringWriter();
			rope.append("<html><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8' /><title>HTMLFormater</title></head><body>");
			rope.append(req.toHTML());
			toHTML(rs, rope);
			rope.append(res.toHTML());
			rope.append("</body></html>");

			return new StringRepresentation(rope.getBuffer(), MediaType.TEXT_HTML, Language.ALL, CharacterSet.UTF_8);
		} catch (SQLException e) {
			throw new ResourceException(e);
		}
	}

	// @Override
	public Representation toRepresentation(IRequest req, List<Map<String, ? extends Object>> datas, IResponse res) throws ResourceException {
		StringWriter rw = new StringWriter(512);

		rw.append("<html><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8' /><title>HTMLFormater</title></head><body>");
		rw.append("<table border=1 cellpadding=3 cellspacing=0 width='100%' class='body_table'>\n");
		rw.append("<tr><td bgColor='#eeeeee'><b>Request</b></td></tr>\n");
		rw.append("<tr><td>" + req.toHTML() + "</td></tr>\n");
		int nodeCnt = 0;
		for (Map<String, ?> infoMap : datas) {
			if (++nodeCnt == 1) {
				rw.append("<tr><td bgColor='#eeeeee'><b>Nodes</b></td></tr>\n");
			}
			rw.append("<tr><td>\n");
			for (Entry<String, ?> entry : infoMap.entrySet()) {
				rw.append(entry.getKey()).append(" : ").append(StringUtil.toString(entry.getValue())).append("<br/>\n");
			}
			rw.append("</td></tr>\n");
		}
		rw.append("<tr><td bgColor='#eeeeee'><b>Response</b></td></tr>\n");
		rw.append("<tr><td>" + res.toHTML() + "</td></tr>\n");
		rw.append("</table>\n");
		rw.append("</body></html>");

		return new StringRepresentation(rw.getBuffer(), MediaType.TEXT_HTML, Language.ALL, CharacterSet.UTF_8);
	}

	// @Override
	public Object handle(ResultSet rs) throws SQLException {
		StringWriter rope = new StringWriter();
		rope.append("<html><head><title>HTMLFormater</title></head><body>");
		toHTML(rs, rope);
		rope.append("</body></html>");

		return new StringRepresentation(rope.getBuffer(), MediaType.TEXT_HTML, Language.ALL, CharacterSet.UTF_8);
	}

	private void toHTML(ResultSet rs, Writer writer) throws SQLException {
		try {
			writer.append("\n<table border=1 cellpadding=3 cellspacing=0 width='100%' class='body_table'>\n");

			writer.append("\t<tr>");
			ResultSetMetaData meta = rs.getMetaData();
			for (int j = 1, last = meta.getColumnCount(); j <= last; j++) {
				writer.append("<th>");
				writer.append(meta.getColumnName(j));
				writer.append("</th>");
			}
			writer.append("</tr>\n");

			while (rs.next()) {
				writer.append("\t<tr>");
				for (int j = 1, last = meta.getColumnCount(); j <= last; j++) {
					writer.append("<td class='text_center'>");
					writer.append(rs.getString(j));
					writer.append("</td>");
				}
				writer.append("</tr>\n");
			}
			writer.append("</table>\n");
		} catch (IOException ex) {
			throw new SQLException(ex.getMessage());
		}

	}

}
