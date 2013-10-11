package net.ion.framework.mte;

import java.util.Collection;

public interface RendererRegistry {

	NamedRenderer resolveNamedRenderer(String rendererName);

	Collection<NamedRenderer> getAllNamedRenderers();

	Collection<NamedRenderer> getCompatibleRenderers(Class<?> inputType);

	RendererRegistry deregisterNamedRenderer(NamedRenderer renderer);

	RendererRegistry registerNamedRenderer(NamedRenderer renderer);

	<C> RendererRegistry registerRenderer(Class<C> clazz, Renderer<C> renderer);

	RendererRegistry deregisterRenderer(Class<?> clazz);

	<C> Renderer<C> resolveRendererForClass(Class<C> clazz);

}
