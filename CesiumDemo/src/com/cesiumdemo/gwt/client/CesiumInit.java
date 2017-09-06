package com.cesiumdemo.gwt.client;

import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.dom.client.StyleInjector;

public class CesiumInit
{
	public static void initializeCesium()
	{
		ScriptInjector.fromString(CesiumFiles.INSTANCE.cesiumJs().getText()).setWindow(ScriptInjector.TOP_WINDOW)
		        .inject();
		CesiumFiles.INSTANCE.cesiumCss().ensureInjected();

		// Give the cesium stylesheet the correct url to look for the png
		// images in. Just
		// override the url for the images for the toggle layer. We will provide
		// our own icon urls.
		StyleInjector.injectStylesheet(".cesium-baseLayerPicker-selected " + "{ 	background-image: url("
		        + CesiumFiles.INSTANCE.bingMaps().getSafeUri().asString() + "); }");

	}
}
