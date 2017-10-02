package org.cesiumjs.cs.widgets.options;


import org.cesiumjs.cs.scene.providers.ImageryProvider;

import jsinterop.annotations.JsFunction;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 * @author woodruff 
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
public class ProviderViewModelOptions {
	
	/**
	 * Required.
     * A function or Command that creates one or more providers which will be added to the globe when this item is selected.
     */		
	@JsProperty
	public CreationFunction creationFunction;
		
	/**
	 * Required.
     * Gets the icon. This property is observable.
     */	
	@JsProperty
    public String iconUrl;
	
	/**
	 * Required.
     * Gets the display name. This property is observable.
     */
	@JsProperty
    public String name;
	
	/**
	 * Required.
     * Gets the tooltip. This property is observable.
     */
	@JsProperty
    public String tooltip;
	
	
	/**
     * A function which creates one or more providers.
     * @return The ImageryProvider or TerrainProvider, or array of providers, to be added to the globe.
     */	
	@JsFunction
	public interface CreationFunction<T> {
		public T function();
	}
}
