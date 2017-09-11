package com.cesiumdemo.gwt.client;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.cesiumjs.cs.collections.EntityCollection;
import org.cesiumjs.cs.collections.ImageryLayerCollection;
import org.cesiumjs.cs.core.Cartesian2;
import org.cesiumjs.cs.core.Cartesian3;
import org.cesiumjs.cs.core.Cartographic;
import org.cesiumjs.cs.core.Color;
import org.cesiumjs.cs.core.Globe;
import org.cesiumjs.cs.core.PolygonHierarchy;
import org.cesiumjs.cs.core.ScreenSpaceEventHandler;
import org.cesiumjs.cs.core.enums.ScreenSpaceEventType;
import org.cesiumjs.cs.core.events.MouseClickEvent;
import org.cesiumjs.cs.core.events.MouseMoveEvent;
import org.cesiumjs.cs.datasources.Entity;
import org.cesiumjs.cs.datasources.graphics.EllipseGraphics;
import org.cesiumjs.cs.datasources.graphics.LabelGraphics;
import org.cesiumjs.cs.datasources.graphics.PointGraphics;
import org.cesiumjs.cs.datasources.graphics.PolygonGraphics;
import org.cesiumjs.cs.datasources.graphics.options.EllipseGraphicsOptions;
import org.cesiumjs.cs.datasources.graphics.options.LabelGraphicsOptions;
import org.cesiumjs.cs.datasources.graphics.options.PolygonGraphicsOptions;
import org.cesiumjs.cs.datasources.properties.ColorMaterialProperty;
import org.cesiumjs.cs.datasources.properties.ConstantPositionProperty;
import org.cesiumjs.cs.datasources.properties.ConstantProperty;
import org.cesiumjs.cs.datasources.properties.GridMaterialProperty;
import org.cesiumjs.cs.datasources.properties.options.GridMaterialPropertyOptions;
import org.cesiumjs.cs.scene.providers.ArcGisMapServerImageryProvider;
import org.cesiumjs.cs.scene.providers.ImageryProvider;
import org.cesiumjs.cs.scene.providers.UrlTemplateImageryProvider;
import org.cesiumjs.cs.scene.providers.options.ArcGisMapServerImageryProviderOptions;
import org.cesiumjs.cs.scene.providers.options.UrlTemplateImageryProviderOptions;
import org.cesiumjs.cs.scene.ImageryLayer;
import org.cesiumjs.cs.scene.Scene;
import org.cesiumjs.cs.scene.enums.HorizontalOrigin;
import org.cesiumjs.cs.scene.enums.SceneMode;
import org.cesiumjs.cs.scene.enums.VerticalOrigin;
import org.cesiumjs.cs.widgets.*;
import org.cesiumjs.cs.widgets.options.ViewerOptions;

//import javafx.scene.Scene;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class CesiumDemo implements EntryPoint  {
	public CesiumDemo() {
	  }	
	  private ViewerOptions viewerOptions = new ViewerOptions();
	  private ViewerPanel myVPanel = new ViewerPanel(viewerOptions);
	  private Viewer myViewer = myVPanel.getViewer();
	  AbsolutePanel absPanel = new AbsolutePanel();
	  //LayoutPanel absPanel = new LayoutPanel();

	  /**
		 * This is the entry point method.
		 */
	  public void onModuleLoad() {
	      Globe globe = new Globe();
	      globe.show = true; 
		  globe.baseColor = Color.GREEN();
		  viewerOptions.globe = globe;		  
		  absPanel.add(myVPanel);		  
	      RootPanel.get().add(absPanel);

		  //Capability Demos
		  //customBaseLayerPicker(viewerOptions, myVPanel, globe);
	      drawShapes();    
	      drawCustomPolygon();
	      showPositionOnMouseOver();
	  }
	  
	  //Customize the Base Layer Picker widget in the Cesium View window and add custom imagery options
	  //May need to write wrapper for BaseLayerPicker to customize available options??  Or look elsewhere
	  /*private void customBaseLayerPicker(ViewerOptions viewerOptions, ViewerPanel viewerPanel,
			  Globe globe) {
		  
		  //ProviderViewModel providerModels = new ProviderViewModel();		  
		  //ImageryLayerCollection imageryLayers = new ImageryLayerCollection();
		  	  
		  //ArcGIS
		  ArcGisMapServerImageryProviderOptions arcGisMapServerImageryProviderOptions = new ArcGisMapServerImageryProviderOptions();
	      arcGisMapServerImageryProviderOptions.url = "https://server.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer";
	      ImageryLayer arcGisImageryLayer = new ImageryLayer(new ArcGisMapServerImageryProvider(arcGisMapServerImageryProviderOptions));
	      //imageryLayers.add(arcGisImageryLayer);
	      
	      //NASA Black Marble
	      UrlTemplateImageryProviderOptions urlImageryProviderOptions = new UrlTemplateImageryProviderOptions ();
	      urlImageryProviderOptions.url = "../../resources/org.cesiumjs/public/cs/Cesium/Widgets/Images/ImageryProviders/blackmarble.png";
	      ImageryLayer nasaImageryLayer = new ImageryLayer(new UrlTemplateImageryProvider(urlImageryProviderOptions));
	      //globe.imageryLayers = imageryLayers;
	      	   
	      //ProviderViewModel providerModel = new ProviderViewModel();	      
	      //viewerOptions.imageryProviderViewModels = new ProviderViewModel[] {};
	      
	  }	//	End of method customBaseLayerPicker	 */
	  
	  //Draw user-defined shapes
	  private void drawShapes() {
		  
		  //Position
		  ConstantPositionProperty position = new ConstantPositionProperty();
		  position.setValue(Cartesian3.fromDegrees(-77.0, 38.9));
		  
		  //Point Entity
		  Entity pointEntity = new Entity();	  
		  
		  //Label  
		  LabelGraphicsOptions lgOptions = new LabelGraphicsOptions();
		  lgOptions.horizontalOrigin = new ConstantProperty<>(HorizontalOrigin.LEFT());
		  lgOptions.verticalOrigin = new ConstantProperty<>(VerticalOrigin.TOP());
		  lgOptions.eyeOffset = new ConstantProperty<>(new Cartesian3(5,5,0));
		  pointEntity.label = new LabelGraphics(lgOptions);
		  pointEntity.label.text = new ConstantProperty<>("Point Entity");
		  
		  //Point Options
		  pointEntity.position = position;		  
		  PointGraphics myPoint = new PointGraphics();
		  myPoint.color = new ConstantProperty<>(Color.DARKRED());
		  myPoint.pixelSize = new ConstantProperty<>(5);
		  myPoint.outlineColor = new ConstantProperty<>(Color.AQUA());
		  myPoint.outlineWidth = new ConstantProperty<>(2);
		  pointEntity.point = myPoint;
		  
		  //Ellipse Entity
		  Entity ellipseEntity = new Entity();
		  
		  //Label
		  ellipseEntity.label = new LabelGraphics();
		  ellipseEntity.label.text = new ConstantProperty<>("Ellipse Entity");
		  ellipseEntity.label.horizontalOrigin = new ConstantProperty<>(HorizontalOrigin.LEFT());
		  ellipseEntity.label.verticalOrigin = new ConstantProperty<>(VerticalOrigin.TOP());
		  ellipseEntity.label.pixelOffset = new ConstantProperty<>(
				    new Cartesian2(20.0, -50.0));
		  
		  //Ellipse Options
		  ellipseEntity.position = position;
		  EllipseGraphicsOptions ellipseOpt = new EllipseGraphicsOptions();
		  ellipseOpt.material = new ColorMaterialProperty(Color.RED().withAlpha((float) 0.5));
		  ellipseOpt.semiMajorAxis = new ConstantProperty<>(150000.0);
		  ellipseOpt.semiMinorAxis = new ConstantProperty<>(75000.0);	
		  ellipseOpt.outline = new ConstantProperty<>(true);
		  ellipseOpt.outlineColor = new ConstantProperty<>(Color.AQUA());
		  ellipseOpt.outlineWidth = new ConstantProperty<>(20);
		  
		  EllipseGraphics ellipse = new EllipseGraphics(ellipseOpt);			  
		  ellipse.height = new ConstantProperty<>(0);  
		  ellipseEntity.ellipse = ellipse;
		  		  
		  //Polygon Entity
		  Entity polygonEntity = new Entity();
		  
		  //Label		  
		  LabelGraphicsOptions lgPolyOptions = new LabelGraphicsOptions();
		  lgPolyOptions.text = new ConstantProperty<>("Polygon Entity");
		  lgPolyOptions.horizontalOrigin = new ConstantProperty<>(HorizontalOrigin.LEFT());
		  lgPolyOptions.verticalOrigin = new ConstantProperty<>(VerticalOrigin.TOP());		  
		  lgPolyOptions.eyeOffset = new ConstantProperty<>(new Cartesian3(5,-50,0));		  		  
		  polygonEntity.label = new LabelGraphics(lgPolyOptions);
		  
		  //Polygon options
		  PolygonGraphicsOptions polyOpt = new PolygonGraphicsOptions();
		  polyOpt.outline = new ConstantProperty<>(true);
		  polyOpt.outlineColor = new ConstantProperty<>(Color.AQUA());
		  polyOpt.outlineWidth = new ConstantProperty<>(10);
		  polyOpt.fill = new ConstantProperty<>(true);
		  GridMaterialPropertyOptions grid = new GridMaterialPropertyOptions();
		  polyOpt.material = new GridMaterialProperty(grid);
		  
		  //Polygon vertices
		  polyOpt.hierarchy = new ConstantProperty<>(Cartesian3.fromDegreesArray(
				  new double[] {-78.0, 38.0, -76.0, 38.0, -77.0, 39.0}));
		  
		  PolygonGraphics myPolygon = new PolygonGraphics(polyOpt);
		  polygonEntity.polygon = myPolygon; 
		  
		  myViewer.entities().add(ellipseEntity);
		  myViewer.entities().add(polygonEntity);
		  myViewer.entities().add(pointEntity);

		  myViewer.selectedEntity = pointEntity;
		  //myViewer.zoomTo(myViewer.selectedEntity);
	  }	//End of method drawShapes
	  
	  //Draw a custom polygon
	  String currentPolyId;
	  private void drawCustomPolygon() {
		  ScreenSpaceEventHandler myHandler = new ScreenSpaceEventHandler(myVPanel.getViewer().scene().canvas());
		  		  
		  List<Cartesian3> currentPolyVert = new ArrayList<Cartesian3>();	  
		  Integer numPoly = 0;
		  ScreenSpaceEventHandler.Listener<MouseClickEvent> myClickEvent = new ScreenSpaceEventHandler.Listener<MouseClickEvent>() {
			  @Override
			  public void function(MouseClickEvent event) {
				  Cartesian3 myPosition = getPosition(event);
				  currentPolyVert.add(myPosition);
				  PolygonGraphics myPolygon = myViewer.entities().getById(currentPolyId).polygon;
				  myPolygon.hierarchy = new ConstantProperty<>(currentPolyVert.toArray());
			  }
		  };
		  
		  //Action Buttons
		  Button btnDrawPolygon = new Button();
		  btnDrawPolygon.setText("Draw Polygon");
		  
		  Button btnDone = new Button();
		  btnDone.setText("Done");	  
		  
		  Button btnCancel = new Button();
		  btnCancel.setText("Cancel");
		  
		  btnDrawPolygon.addClickHandler( new ClickHandler() {
		      public void onClick(ClickEvent event) {
		    	  
		    	  //Add polygon click handler back to map
		    	  myHandler.setInputAction(myClickEvent, ScreenSpaceEventType.LEFT_CLICK());
		    	  currentPolyVert.clear();
		    	  
		    	  //Add done and cancel buttons
		    	  absPanel.add(btnDone, 180, 30);
		    	  absPanel.add(btnCancel, 120, 30);		    	  
		   		    	  
		    	  //Create new Entity polygon and add to map
		    	  Entity polyEntity = new Entity();		
		    	  polyEntity.id = numPoly.toString();
		    	  currentPolyId = polyEntity.id;
				  myViewer.entities().add(polyEntity);
				  PolygonGraphicsOptions myPolyOpt = new PolygonGraphicsOptions();
				  myPolyOpt.material = new ColorMaterialProperty(Color.DARKGREEN().withAlpha((float) 0.5));
				  PolygonGraphics myPolygon = new PolygonGraphics(myPolyOpt);
				  polyEntity.polygon = myPolygon;				  
		      }
		    });
		  absPanel.add(btnDrawPolygon, 10, 30);		  
		  
		  btnDone.addClickHandler( new ClickHandler() {
			  @Override
		      public void onClick(ClickEvent event) {
				  myHandler.removeInputAction(ScreenSpaceEventType.LEFT_CLICK());
				  absPanel.remove(btnDone);
				  absPanel.remove(btnCancel);
		      }
		    });
		  
		  btnCancel.addClickHandler( new ClickHandler() {
			  @Override
		      public void onClick(ClickEvent event) {
				  myViewer.entities().remove(myViewer.entities().getById(currentPolyId));
				  myHandler.removeInputAction(ScreenSpaceEventType.LEFT_CLICK());
				  absPanel.remove(btnDone);
				  absPanel.remove(btnCancel);
		      }
		    });
	  }	  
	  
	  //Show global position in top left corner of viewer on mouse move 
	  public void showPositionOnMouseOver() {
		  HorizontalPanel positionDisplay = new HorizontalPanel();
		  absPanel.add(positionDisplay, 10, 10);		  
		  Label lblPosition = new Label();
		  Label lblTitle = new Label("Map Coordinates:   ");
		  lblPosition.getElement().getStyle().setColor("WHITE");
		  lblTitle.getElement().getStyle().setColor("WHITE");
		  positionDisplay.add(lblTitle);
		  positionDisplay.add(lblPosition);		  
		  ScreenSpaceEventHandler myHandler = new ScreenSpaceEventHandler(myVPanel.getViewer().scene().canvas());
		  myHandler.setInputAction(new ScreenSpaceEventHandler.Listener<MouseMoveEvent>() {
			  @Override
			  public void function(MouseMoveEvent event) {
				  Cartesian3 myLocation = getPosition(event);
				  if (myLocation != null) {
	                  Cartographic cartographic = Cartographic.fromCartesian(myLocation);
	                  BigDecimal lat = new BigDecimal(Math.toDegrees(cartographic.latitude)).setScale(3, RoundingMode.HALF_EVEN);
	                  BigDecimal lon = new BigDecimal(Math.toDegrees(cartographic.longitude)).setScale(3, RoundingMode.HALF_EVEN);
	                  String strLat;
	                  String strLon;
	                  if (lat.compareTo(new BigDecimal(0))==(-1)) {
	                	  strLat = lat.abs().toString() + "S";
	                  }
	                  else {
	                	  strLat = lat.toString() + "N";
	                  }
	                  
	                  if (lon.compareTo(new BigDecimal(0))==(-1)) {
	                	  strLon = lon.abs().toString() + "W";
	                  }
	                  else {
	                	  strLon = lon.toString() + "E";
	                  }	                  
	                  lblPosition.setText(strLat + ", " + strLon);	                  
	              } else {
	            	  lblPosition.setText("");
	              }
			  }			  	  
		  }, ScreenSpaceEventType.MOUSE_MOVE());
	  }	//End of function showPositionOnMouseOver
	  
	  //Get position on globe on mouse move
	  public Cartesian3 getPosition(MouseMoveEvent event) {
          Cartesian3 c3Location = myVPanel.getViewer().camera.pickEllipsoid(event.endPosition, myVPanel.getViewer().scene().globe.ellipsoid);
		  return c3Location;
	  }	//End of function getPosition(MouseMoveEvent)
	  
	  //Get position on globe on mouse click
	  public Cartesian3 getPosition(MouseClickEvent event) {
          Cartesian3 c3Location = myVPanel.getViewer().camera.pickEllipsoid(event.position, myVPanel.getViewer().scene().globe.ellipsoid);
		  return c3Location;
	  }	//End of function getPosition(MouseClickEvent)
}

