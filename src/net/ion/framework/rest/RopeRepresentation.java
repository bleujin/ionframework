package net.ion.framework.rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.nio.charset.Charset;

import net.ion.framework.rope.Rope;
import net.ion.framework.rope.RopeBuilder;
import net.ion.framework.rope.RopeInputStream;

import org.restlet.data.CharacterSet;
import org.restlet.data.Language;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StreamRepresentation;

public class RopeRepresentation extends StreamRepresentation implements CloneableRepresentation, Serializable {

	private static final long serialVersionUID = 6504297590932758870L;
	private Rope rope;

	public RopeRepresentation(CharSequence cseq) {
		this(RopeBuilder.build(cseq));
	}

	public RopeRepresentation(CharSequence cseq, MediaType mediaType) {
		this(RopeBuilder.build(cseq), mediaType);
	}

	public RopeRepresentation(CharSequence cseq, Language language) {
		this(RopeBuilder.build(cseq), language);
	}

	public RopeRepresentation(CharSequence cseq, MediaType mediaType, Language language) {
		this(RopeBuilder.build(cseq), mediaType, language);
	}

	public RopeRepresentation(Rope text) {
		this(text, MediaType.TEXT_PLAIN);
	}

	public RopeRepresentation(Rope text, Language language) {
		this(text, MediaType.TEXT_PLAIN, language);
	}

	public RopeRepresentation(Rope text, MediaType mediaType) {
		this(text, mediaType, null);
	}

	public RopeRepresentation(Rope text, MediaType mediaType, Language language) {
		this(text, mediaType, language, CharacterSet.UTF_8);
	}

	public RopeRepresentation(Rope text, MediaType mediaType, Language language, CharacterSet characterSet) {
		super(mediaType);
		setMediaType(mediaType);
		if (language != null)
			getLanguages().add(language);
		setCharacterSet(characterSet);
		setText(text);
	}

	public InputStream getStream() throws IOException {
		if (rope != null) {
			if (getCharacterSet() != null)
				return new RopeInputStream(this.rope, getCharacterSet().getName());
			else
				return new RopeInputStream(this.rope, Charset.defaultCharset().displayName());
		} else {
			return null;
		}
	}

	public String getText() {
		return rope != null ? rope.toString() : null;
	}

	public void setText(CharSequence text) {
		setText(RopeBuilder.build(text));
	}

	public void setText(Rope rope) {
		this.rope = rope;
		setSize(-1L);
	}

	public void setText(String text) {
		setText(((CharSequence) (text)));
		setSize(-1L);
	}

	public void write(OutputStream outputStream) throws IOException {
		if (rope != null) {
			OutputStreamWriter osw = null;
			if (getCharacterSet() != null)
				osw = new OutputStreamWriter(outputStream, getCharacterSet().getName());
			else
				osw = new OutputStreamWriter(outputStream);
			rope.write(osw);
			osw.flush();
		}
	}

	public Representation cloneRepresentation() {
		final RopeRepresentation clone = new RopeRepresentation(this.rope, getMediaType());
		clone.setCharacterSet(getCharacterSet());
		clone.setLanguages(getLanguages());
		// clone.setAvailable(true) ;
		return clone;
	}

}