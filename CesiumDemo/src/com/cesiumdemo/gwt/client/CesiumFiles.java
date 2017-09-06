package com.cesiumdemo.gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;

public interface CesiumFiles extends ClientBundle
{
	public static final CesiumFiles INSTANCE = GWT.create(CesiumFiles.class);

	@Source("public/Cesium/Widgets/Images/ImageryProviders/bingAerial.png")
		ImageResource bingMaps();
	
	@Source("public/Cesium/Widgets/widgets.css")
		@NotStrict
		CssResource cesiumCss();
	
	@Source("public/CesiumUnminified/Cesium.js")
	TextResource cesiumJs();
}
