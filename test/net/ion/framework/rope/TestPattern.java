package net.ion.framework.rope;


public class TestPattern {

}
//
//class StringBuilderPattern extends AbstractPattern {
//
//	public Representation toRepresentation(ISearchResponse iresponse) throws IOException {
//
//		ISearchRequest irequest = iresponse.getRequest();
//		XML nodes = makeXML(irequest, iresponse, iresponse.getDocument());
//
//		StringBuilder rw = new StringBuilder();
//		rw.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
//		rw.append(nodes.toString());
//
//		Representation result = new StringRepresentation(rw, MediaType.APPLICATION_XML);
//		result.setCharacterSet(CharacterSet.UTF_8);
//		return result;
//	}
//}
//
//class StringWriterPattern extends AbstractPattern {
//
//	public Representation toRepresentation(ISearchResponse iresponse) throws IOException {
//
//		ISearchRequest irequest = iresponse.getRequest();
//		XML nodes = makeXML(irequest, iresponse, iresponse.getDocument());
//
//		StringWriter sw = new StringWriter();
//		sw.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
//		nodes.output(sw);
//
//		Representation result = new StringRepresentation(sw.getBuffer(), MediaType.APPLICATION_XML);
//		result.setCharacterSet(CharacterSet.UTF_8);
//		return result;
//	}
//
//}
//
//class RopePattern extends AbstractPattern {
//
//	public Representation toRepresentation(ISearchResponse iresponse) throws IOException {
//
//		ISearchRequest irequest = iresponse.getRequest();
//		XML nodes = makeXML(irequest, iresponse, iresponse.getDocument());
//
//		RopeWriter rw = new RopeWriter();
//		rw.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
//		nodes.output(rw);
//
//		Representation result = new RopeRepresentation(rw.getRope(), MediaType.APPLICATION_XML);
//		result.setCharacterSet(CharacterSet.UTF_8);
//		return result;
//	}
//
//}
//
//class AbstractPattern {
//
//	protected XML makeXML(ISearchRequest irequest, ISearchResponse iresponse, List<MyDocument> docs) {
//		XML nodes = new XML("nodes");
//
//		nodes.addElement(irequest.toXML());
//		nodes.addElement(iresponse.toXML());
//
//		for (MyDocument doc : docs) {
//			XML node = new XML("node");
//			List<Fieldable> fields = doc.getFields();
//			for (Fieldable field : fields) {
//				XML property = new XML("property");
//				property.addAttribute("name", field.name());
//				property.addAttribute("stored", field.isStored());
//				property.addElement("<![CDATA[" + field.stringValue() + "]]>");
//				node.addElement(property);
//			}
//			nodes.addElement(node);
//		}
//		return nodes;
//	}
//
//}