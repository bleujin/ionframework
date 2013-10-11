package net.ion.framework.mte.renderer;

import net.ion.framework.mte.NamedRenderer;
import net.ion.framework.mte.encoder.Encoder;

/**
 * <p>Marker interface to indicate that the result of a renderer implementing this
 * interface shall not be encoded by any {@link Encoder} that might be
 * configured.</p>
 * 
 * Can be set on {@link Renderer} and {@link NamedRenderer}.
 * 
 * @see Encoder
 * @see NamedRenderer
 * @see Renderer
 */
public interface RawRenderer {

}
