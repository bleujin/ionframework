package net.ion.framework.rest;

import static net.ion.framework.rest.MyConstant.NAME;
import static net.ion.framework.rest.MyConstant.NODES;
import static net.ion.framework.rest.MyConstant.PROPERTY;
import static net.ion.framework.rest.MyConstant.TYPE;

import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.util.StringUtil;

import org.apache.ecs.xml.XML;
import org.restlet.data.CharacterSet;
import org.restlet.data.Language;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;

public class XMLFormater implements ResultSetHandler, XMLHandler, IRowsRepresentationHandler, IMapListRepresentationHandler {

	private static final long serialVersionUID = -8164513968960809763L;
	public final static String XML_HEADER = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n\n";

	public XML toXML(ResultSet rs) throws SQLException {
		XML nodes = new XML(NODES);

		ResultSetMetaData meta = rs.getMetaData();
		List<String> types = new ArrayList<String>();
		List<String> names = new ArrayList<String>();

		for (int i = 0; i < meta.getColumnCount(); i++) {
			int column = i + 1;
			types.add(meta.getColumnTypeName(column));
			names.add(meta.getColumnName(column));
		}
		

		while (rs.next()) {
			XML node = new XML("node");
			for (int i = 0, last = meta.getColumnCount(); i < last; i++) {
				XML property = new XML(PROPERTY);
				property.addAttribute(TYPE, types.get(i));
				property.addAttribute(NAME, names.get(i));
				property.addElement(getCDATASection(rs.getString(i + 1)));
				node.addElement(property);
			}
			nodes.addElement(node);
		}

		return nodes;
	}

	public Representation toRepresentation(IRequest req, List<Map<String, ? extends Object>> nodeDatas, IResponse res) throws ResourceException {

		XML result = new XML("result");
		XML nodes = new XML("nodes");
		for (Map<String, ?> nodeMap : nodeDatas) {
			XML node = new XML("node");
			for (Entry<String, ?> entry : nodeMap.entrySet()) {
				XML property = new XML("property");
				property.addAttribute("name", entry.getKey());
				property.addElement(getCDATASection(entry.getValue()));
				node.addElement(property);
			}
			nodes.addElement(node);
		}

		result.addElement(req.toXML());
		result.addElement(res.toXML());
		result.addElement(nodes);

//		RopeWriter rw = new RopeWriter();
//		rw.append(XML_HEADER);
//		result.output(rw);
//
//		return  new RopeRepresentation(rw.getRope(), MediaType.APPLICATION_XML, Language.ALL, CharacterSet.UTF_8);
		
		StringWriter rw = new StringWriter(512) ;
		rw.append(XML_HEADER) ;
		result.output(rw) ;

		return new StringRepresentation(rw.getBuffer(), MediaType.APPLICATION_XML, Language.ALL, CharacterSet.UTF_8) ;
	}

	public Object handle(ResultSet rs) throws SQLException {
		return toXML(rs);
	}

	public Representation toRepresentation(IRequest req, ResultSet rs, IResponse res) {
		try {
			XML result = new XML("result");
			XML nodes = toXML(rs);
			result.addElement(req.toXML());
			result.addElement(res.toXML());
			result.addElement(nodes);

			StringWriter rw = new StringWriter(512);
			rw.append(XML_HEADER);
			result.output(rw);

			return new StringRepresentation(rw.getBuffer(), MediaType.APPLICATION_XML, Language.ALL, CharacterSet.UTF_8);
		} catch (SQLException e) {
			throw new ResourceException(e);
		}
	}

	private String getCDATASection(Object object) {
		if(object == null) {
			return "";
		} else if (object.getClass().isPrimitive() || isPrimitiveWrapperType(object)){
			return StringUtil.toString(object);
		} else {
			return "<![CDATA[" + StringUtil.toString(object) + "]]>";
		}
	}

	
	private static Set<Class<?>> DEFINED_PRIMITIVE = makeTypeList()  ;


	private static Set<Class<?>> makeTypeList() {
		 HashSet<Class<?>> ret = new HashSet<Class<?>>();

		 ret.add(Boolean.class) ;
		 ret.add(Short.class) ;
		 ret.add(Integer.class) ;
		 ret.add(Long.class) ;
		 ret.add(Float.class) ;
		 ret.add(Double.class) ;
		 return ret ;
	} ;
	
	private boolean isPrimitiveWrapperType(Object object) {
		return DEFINED_PRIMITIVE.contains(object.getClass());
	}

}
