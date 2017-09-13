package com.cesiumdemo.gwt.client;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

import org.cesiumjs.cs.Cesium;
import org.cesiumjs.cs.collections.EntityCollection;
import org.cesiumjs.cs.collections.ImageryLayerCollection;
import org.cesiumjs.cs.core.Cartesian2;
import org.cesiumjs.cs.core.Cartesian3;
import org.cesiumjs.cs.core.Cartographic;
import org.cesiumjs.cs.core.Color;
import org.cesiumjs.cs.core.Globe;
import org.cesiumjs.cs.core.JulianDate;
import org.cesiumjs.cs.core.PickedObject;
import org.cesiumjs.cs.core.PolygonHierarchy;
import org.cesiumjs.cs.core.ScreenSpaceEventHandler;
import org.cesiumjs.cs.core.enums.ScreenSpaceEventType;
import org.cesiumjs.cs.core.events.MouseClickEvent;
import org.cesiumjs.cs.core.events.MouseMoveEvent;
import org.cesiumjs.cs.datasources.DataSource;
import org.cesiumjs.cs.datasources.Entity;
import org.cesiumjs.cs.datasources.graphics.EllipseGraphics;
import org.cesiumjs.cs.datasources.graphics.LabelGraphics;
import org.cesiumjs.cs.datasources.graphics.PointGraphics;
import org.cesiumjs.cs.datasources.graphics.PolygonGraphics;
import org.cesiumjs.cs.datasources.graphics.PolylineGraphics;
import org.cesiumjs.cs.datasources.graphics.options.EllipseGraphicsOptions;
import org.cesiumjs.cs.datasources.graphics.options.LabelGraphicsOptions;
import org.cesiumjs.cs.datasources.graphics.options.PointGraphicsOptions;
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

import javafx.scene.shape.Polyline;

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
	      highlightOnMouseOver();
	      connectMarkers();
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
	  
	  //Create PointGraphics Entity
	  private Entity createNewPointEntity(Cartesian3 position, Color color, 
			  Color outlineColor, Integer pixelSize) {
		  Entity myEntity = new Entity();		  
		  PointGraphicsOptions myPointOpt = new PointGraphicsOptions();
		  		  
		  myPointOpt.color = new ConstantProperty<>(color);
		  myPointOpt.outlineColor = new ConstantProperty<>(outlineColor);
		  myPointOpt.outlineWidth = new ConstantProperty<>(2);
		  myPointOpt.pixelSize = new ConstantProperty<>(pixelSize);		  
		  myEntity.position = new ConstantPositionProperty(position);
		  
		  PointGraphics myPoint = new PointGraphics(myPointOpt);
		  myEntity.point = myPoint;
		  
		  return myEntity;
	  }
	  
	  //Draw user-defined shapes
	  private void drawShapes() {
		  
		  //Position
		  ConstantPositionProperty position = new ConstantPositionProperty();
		  position.setValue(Cartesian3.fromDegrees(-77.0, 38.9));
		  
		  //Point Entities
		  Entity pointEntity = createNewPointEntity(Cartesian3.fromDegrees(-77.0, 38.9), Color.DARKRED(), Color.AQUA(), 10);
		  Entity pointEntity1 = createNewPointEntity(Cartesian3.fromDegrees(-79.08, 38.38), Color.PURPLE(), Color.BLUE(), 10);
		  Entity pointEntity2 = createNewPointEntity(Cartesian3.fromDegrees(-79.2, 38.40), Color.PURPLE(), Color.BLUE(), 10);
		  Entity pointEntity3 = createNewPointEntity(Cartesian3.fromDegrees(-79.15, 38.36), Color.PURPLE(), Color.BLUE(), 10);		  
		  		  
		  //Point Label  
		  LabelGraphicsOptions lgOptions = new LabelGraphicsOptions();
		  lgOptions.horizontalOrigin = new ConstantProperty<>(HorizontalOrigin.LEFT());
		  lgOptions.verticalOrigin = new ConstantProperty<>(VerticalOrigin.TOP());
		  lgOptions.eyeOffset = new ConstantProperty<>(new Cartesian3(5,5,0));
		  pointEntity.label = new LabelGraphics(lgOptions);
		  pointEntity.label.text = new ConstantProperty<>("Point Entity");  
		  
		  //Ellipse Entity
		  Entity ellipseEntity = new Entity();
		  
		  //Ellipse Label
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
		  ellipseOpt.outlineWidth = new ConstantProperty<>(30);
		  
		  EllipseGraphics ellipse = new EllipseGraphics(ellipseOpt);			  
		  ellipse.height = new ConstantProperty<>(0);  
		  ellipseEntity.ellipse = ellipse;
		  		  
		  //Polygon Entity
		  Entity polygonEntity = new Entity();
		  
		  //Polygon Label		  
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
		  myViewer.entities().add(pointEntity1);
		  myViewer.entities().add(pointEntity2);
		  myViewer.entities().add(pointEntity3);
		  
		  myViewer.selectedEntity = pointEntity1;
		  //myViewer.zoomTo(myViewer.selectedEntity);
	  }	//End of method drawShapes
	  
	  //Draw a custom polygon
	  String currentPolyId;
	  private void drawCustomPolygon() {
		  ScreenSpaceEventHandler myHandler = new ScreenSpaceEventHandler(myVPanel.getViewer().scene().canvas());
		  		  
		  List<Cartesian3> currentPolyVert = new ArrayList<Cartesian3>();	  
		  Integer numPoly = 0;
		  ScreenSpaceEventHandler.Listener<MouseClickEvent> drawPolygonHandler = new ScreenSpaceEventHandler.Listener<MouseClickEvent>() {
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
		  btnDrawPolygon.setWidth("120px");
		  
		  Button btnPolygonDone = new Button();
		  btnPolygonDone.setText("Done");	
		  btnPolygonDone.setWidth("60px");	  
		  
		  Button btnPolygonCancel = new Button();
		  btnPolygonCancel.setText("Cancel");
		  btnPolygonCancel.setWidth("60px");
		  
		  btnDrawPolygon.addClickHandler( new ClickHandler() {
		      public void onClick(ClickEvent event) {
		    	  
		    	  //Add polygon click handler back to map
		    	  myHandler.setInputAction(drawPolygonHandler, ScreenSpaceEventType.LEFT_CLICK());
		    	  currentPolyVert.clear();
		    	  
		    	  //Add done and cancel buttons
		    	  absPanel.add(btnPolygonDone, 132, 35);
		    	  absPanel.add(btnPolygonCancel, 194, 35);		    	  
		   		    	  
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
		  absPanel.add(btnDrawPolygon, 10, 35);		  
		  
		  btnPolygonDone.addClickHandler( new ClickHandler() {
			  @Override
		      public void onClick(ClickEvent event) {
				  myHandler.removeInputAction(ScreenSpaceEventType.LEFT_CLICK());
				  absPanel.remove(btnPolygonDone);
				  absPanel.remove(btnPolygonCancel);
		      }
		    });
		  
		  btnPolygonCancel.addClickHandler( new ClickHandler() {
			  @Override
		      public void onClick(ClickEvent event) {
				  myViewer.entities().remove(myViewer.entities().getById(currentPolyId));
				  myHandler.removeInputAction(ScreenSpaceEventType.LEFT_CLICK());
				  absPanel.remove(btnPolygonDone);
				  absPanel.remove(btnPolygonCancel);
		      }
		    });
	  }	  
	  
	  //Draw lines between markers
	  String currentLineId;
	  private void connectMarkers() {
	      
		  //Initialize vars for Entity Select Handlers
		  PointGraphics pickedPoint = new PointGraphics();
		  List<Cartesian3> polylineVerts = new ArrayList<>();
		  ScreenSpaceEventHandler pickEntityHandler = new ScreenSpaceEventHandler(myVPanel.getViewer().scene().canvas());
		  ScreenSpaceEventHandler dynamicLineHandler = new ScreenSpaceEventHandler(myVPanel.getViewer().scene().canvas());

	      //Add Action Buttons
		  Button btnConnectPoints = new Button();
		  btnConnectPoints.setText("Connect Points");
		  btnConnectPoints.setWidth("120px");
		  
		  Button btnConnectDone = new Button();
		  btnConnectDone.setText("Done");	  
		  btnConnectDone.setWidth("60px");	  
		  
		  Button btnConnectCancel = new Button();
		  btnConnectCancel.setText("Cancel");
		  btnConnectCancel.setWidth("60px");	  
		  Entity currentLineEntity = new Entity();
		  btnConnectPoints.addClickHandler( new ClickHandler() {
		      public void onClick(ClickEvent event) {
				  absPanel.add(btnConnectDone, 132, 60);
		    	  absPanel.add(btnConnectCancel, 194, 60);
		    	  
		    	  //Create new Polyline Entity
			      Entity lineEntity = new Entity();
				  PolylineGraphics myPolyLine = new PolylineGraphics();
				  lineEntity.polyline = myPolyLine;
				  currentLineId = lineEntity.id;
				  myViewer.entities().add(lineEntity);
				  
		    	  //Add handler to Select an Entity on click
		    	  pickEntityHandler.setInputAction(new ScreenSpaceEventHandler.Listener<MouseClickEvent>() {
					  @Override
					  public void function(MouseClickEvent event) {		
						  Entity pickedEntity = new Entity();
						  
						  //Pick Entity on mouse click
						  try {
							  pickedObject = (PickedObject<Entity>) myVPanel.getViewer().scene().pick(event.position);						  	
						  }
						  catch(ClassCastException e) {
							  pickedObject = null;
						  }				  
						  
						  //If Cesium found an Entity with point, add it to the polyline 				  
						  if (Cesium.defined(pickedObject)) {
							  pickedEntity = pickedObject.id;
							  if (Cesium.defined(pickedEntity.point)) {
								  Cartesian3 pickedPosition = pickedEntity.position.getValue(JulianDate.now());
								  polylineVerts.add(pickedPosition);
								  myPolyLine.positions = new ConstantProperty<>(polylineVerts.toArray());
								  myPolyLine.definitionChanged();
							  }
							  //if selected Entity does not contain a Point
							  else {	
								  Window.alert("Please select an existing point on the map");
							  }
						  }		
						  //If no Entity selected
						  else {
							  Window.alert("Please select an existing point on the map");
						  }
					  }
				  }, ScreenSpaceEventType.LEFT_CLICK());
		    	  
		    	  //Add handler to add point to Polyline on mouse move
				  dynamicLineHandler.setInputAction(new ScreenSpaceEventHandler.Listener<MouseMoveEvent>() {
					  @Override
					  public void function(MouseMoveEvent event) {			  			  
						  	  
					  }
				  }, ScreenSpaceEventType.MOUSE_MOVE());
		      }		      
		   });
		  
		  btnConnectDone.addClickHandler( new ClickHandler() {
		      public void onClick(ClickEvent event) {
				  absPanel.remove(btnConnectDone);
		    	  absPanel.remove(btnConnectCancel);
		    	  pickEntityHandler.removeInputAction(ScreenSpaceEventType.LEFT_CLICK());
		    	  dynamicLineHandler.removeInputAction(ScreenSpaceEventType.MOUSE_MOVE());
		    	  polylineVerts.clear();
		      }		      
		   });
		  
		  btnConnectCancel.addClickHandler( new ClickHandler() {
		      public void onClick(ClickEvent event) {
				  absPanel.remove(btnConnectDone);
		    	  absPanel.remove(btnConnectCancel);
		    	  pickEntityHandler.removeInputAction(ScreenSpaceEventType.LEFT_CLICK());
		    	  dynamicLineHandler.removeInputAction(ScreenSpaceEventType.MOUSE_MOVE());
		    	  polylineVerts.clear();
		    	  myViewer.entities().remove(myViewer.entities().getById(currentLineId));
		      }		      
		   });
		  
		  absPanel.add(btnConnectPoints, 10, 60);			  
	  }	  
	  
	  //Highlight Entity on mouseover (currently only valid for Point)
	  PickedObject<Entity> pickedObject = new PickedObject<Entity>();
	  Entity pickedEntity = new Entity();
	  ConstantProperty<Color> baseColor = new ConstantProperty<>();
	  private void highlightOnMouseOver() {
		  ScreenSpaceEventHandler myHandler = new ScreenSpaceEventHandler(myVPanel.getViewer().scene().canvas());
		  myHandler.setInputAction(new ScreenSpaceEventHandler.Listener<MouseMoveEvent>() {
			  @Override
			  public void function(MouseMoveEvent event) {
				  
				  //Determine if a picked entity already existed (from last mouse-move), return it to its original color
				  if (Cesium.defined(pickedEntity.point)) {
					 pickedEntity.point.outlineColor = baseColor;
				  }
				  
				  //Pick Entity on mouse-over
				  try {
					  pickedObject = (PickedObject<Entity>) myVPanel.getViewer().scene().pick(event.endPosition);	
					  	
				  }
				  catch(ClassCastException e) {
					  pickedObject = null;
				  }				  
				  
				  //If Cesium found an Entity, determine if it is a point and update the color 
				  if (Cesium.defined(pickedObject)) {
					  pickedEntity = pickedObject.id;
					  if (Cesium.defined(pickedEntity.point)) {
						  baseColor = (ConstantProperty<Color>) pickedEntity.point.outlineColor;
						  pickedEntity.point.outlineColor = new ConstantProperty<>(Color.GOLD());
					  }
				  }		  
			  }
		  }, ScreenSpaceEventType.MOUSE_MOVE());		  
	  }
	  
	  //Show global position in top left corner of viewer on mouse move 
	  private void showPositionOnMouseOver() {
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
	  private Cartesian3 getPosition(MouseMoveEvent event) {
          Cartesian3 c3Location = myVPanel.getViewer().camera.pickEllipsoid(event.endPosition, myVPanel.getViewer().scene().globe.ellipsoid);
		  return c3Location;
	  }	//End of function getPosition(MouseMoveEvent)
	  
	  //Get position on globe on mouse click
	  private Cartesian3 getPosition(MouseClickEvent event) {
          Cartesian3 c3Location = myVPanel.getViewer().camera.pickEllipsoid(event.position, myVPanel.getViewer().scene().globe.ellipsoid);
		  return c3Location;
	  }	//End of function getPosition(MouseClickEvent)
	  
}
