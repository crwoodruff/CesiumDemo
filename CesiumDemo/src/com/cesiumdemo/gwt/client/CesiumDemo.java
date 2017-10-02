package com.cesiumdemo.gwt.client;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.cesiumjs.cs.Cesium;
import org.cesiumjs.cs.collections.EntityCollection;
import org.cesiumjs.cs.collections.ImageryLayerCollection;
import org.cesiumjs.cs.core.Cartesian2;
import org.cesiumjs.cs.core.Cartesian3;
import org.cesiumjs.cs.core.Cartographic;
import org.cesiumjs.cs.core.Color;
import org.cesiumjs.cs.core.Event;
import org.cesiumjs.cs.core.Globe;
import org.cesiumjs.cs.core.JulianDate;
import org.cesiumjs.cs.core.PickedObject;
import org.cesiumjs.cs.core.PinBuilder;
import org.cesiumjs.cs.core.PolygonHierarchy;
import org.cesiumjs.cs.core.Rectangle;
import org.cesiumjs.cs.core.ScreenSpaceEventHandler;
import org.cesiumjs.cs.core.enums.ScreenSpaceEventType;
import org.cesiumjs.cs.core.events.MouseClickEvent;

import org.cesiumjs.cs.core.events.MouseMoveEvent;
import org.cesiumjs.cs.core.geometry.BoxGeometry;
import org.cesiumjs.cs.core.geometry.CircleGeometry;
import org.cesiumjs.cs.core.geometry.Geometry;
import org.cesiumjs.cs.core.geometry.GeometryAttribute;
import org.cesiumjs.cs.core.geometry.GeometryAttributes;
import org.cesiumjs.cs.core.geometry.GeometryInstance;
import org.cesiumjs.cs.core.geometry.RectangleGeometry;
import org.cesiumjs.cs.core.geometry.options.CircleGeometryOptions;
import org.cesiumjs.cs.core.geometry.options.RectangleGeometryOptions;
import org.cesiumjs.cs.core.options.GeometryAttributeOptions;
import org.cesiumjs.cs.core.options.GeometryInstanceOptions;
import org.cesiumjs.cs.core.providers.TerrainProvider;
import org.cesiumjs.cs.core.providers.options.CesiumTerrainProviderOptions;
import org.cesiumjs.cs.datasources.CustomDataSource;
import org.cesiumjs.cs.datasources.DataSource;
import org.cesiumjs.cs.datasources.DataSourceDisplay;
import org.cesiumjs.cs.datasources.Entity;
import org.cesiumjs.cs.datasources.EntityCluster;
import org.cesiumjs.cs.datasources.EntityClusterObject;
import org.cesiumjs.cs.datasources.KmlDataSource;
import org.cesiumjs.cs.datasources.graphics.BillboardGraphics;
import org.cesiumjs.cs.datasources.graphics.EllipseGraphics;
import org.cesiumjs.cs.datasources.graphics.LabelGraphics;
import org.cesiumjs.cs.datasources.graphics.PointGraphics;
import org.cesiumjs.cs.datasources.graphics.PolygonGraphics;
import org.cesiumjs.cs.datasources.graphics.PolylineGraphics;
import org.cesiumjs.cs.datasources.graphics.options.EllipseGraphicsOptions;
import org.cesiumjs.cs.datasources.graphics.options.LabelGraphicsOptions;
import org.cesiumjs.cs.datasources.graphics.options.PointGraphicsOptions;
import org.cesiumjs.cs.datasources.graphics.options.PolygonGraphicsOptions;
import org.cesiumjs.cs.datasources.options.DataSourceDisplayOptions;
import org.cesiumjs.cs.datasources.options.KmlDataSourceOptions;
import org.cesiumjs.cs.datasources.properties.ColorMaterialProperty;
import org.cesiumjs.cs.datasources.properties.ConstantPositionProperty;
import org.cesiumjs.cs.datasources.properties.ConstantProperty;
import org.cesiumjs.cs.datasources.properties.GridMaterialProperty;
import org.cesiumjs.cs.datasources.properties.MaterialProperty;
import org.cesiumjs.cs.datasources.properties.options.GridMaterialPropertyOptions;
import org.cesiumjs.cs.js.JsObject;
import org.cesiumjs.cs.promise.Fulfill;
import org.cesiumjs.cs.promise.Promise;
import org.cesiumjs.cs.scene.providers.ArcGisMapServerImageryProvider;
import org.cesiumjs.cs.scene.providers.ImageryProvider;
import org.cesiumjs.cs.scene.providers.UrlTemplateImageryProvider;
import org.cesiumjs.cs.scene.providers.options.ArcGisMapServerImageryProviderOptions;
import org.cesiumjs.cs.scene.providers.options.TileMapServiceImageryProviderOptions;
import org.cesiumjs.cs.scene.providers.options.UrlTemplateImageryProviderOptions;
import org.cesiumjs.cs.scene.Billboard;
import org.cesiumjs.cs.scene.ImageryLayer;
import org.cesiumjs.cs.scene.Primitive;
import org.cesiumjs.cs.scene.Scene;
import org.cesiumjs.cs.scene.apperances.EllipsoidSurfaceAppearance;
import org.cesiumjs.cs.scene.apperances.PerInstanceColorAppearance;
import org.cesiumjs.cs.scene.apperances.options.EllipsoidSurfaceAppearanceOptions;
import org.cesiumjs.cs.scene.enums.HorizontalOrigin;
import org.cesiumjs.cs.scene.enums.VerticalOrigin;
import org.cesiumjs.cs.scene.options.PrimitiveOptions;
import org.cesiumjs.cs.widgets.*;
import org.cesiumjs.cs.widgets.options.ProviderViewModelOptions;
import org.cesiumjs.cs.widgets.options.ViewerOptions;

//import javafx.scene.Scene;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class CesiumDemo implements EntryPoint {
	public CesiumDemo() {
	}
	
	//Cesium Viewer widget
	public ViewerOptions viewerOptions = new ViewerOptions();	
	private ViewerPanel myVPanel;	
	private Viewer myViewer;
	AbsolutePanel absPanel = new AbsolutePanel();
	
	//"Picked" object - object hovered over on screen
	PickedObject<Entity> pickedObject = new PickedObject<>();
	Entity pickedEntity = new Entity();
	ConstantProperty<Color> baseColor = new ConstantProperty<>();
	
	//Id of current polygon being drawn on map by user
	String currentPolyId;
	
	//Id of current polyline being drawn on map by user
	String currentLineId;
	
	//Map default data source (i.e., all items not attached to a specific data source)
	CustomDataSource defaultDataSource;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		//Globe
		Globe globe = new Globe(); 
		globe.show = true; 
		globe.baseColor = Color.GREEN();
		
		//Create imagery base layers to overwrite default existing base layers in picker
		ProviderViewModel[] providerViewModels = new ProviderViewModel[2];	
		providerViewModels = createImageryBaseLayers();	
		
		//Viewer Options
		viewerOptions.globe = globe; 
		viewerOptions.baseLayerPicker = true;
		viewerOptions.imageryProviderViewModels = providerViewModels;
		viewerOptions.fullscreenButton = false;
		
		//Create viewer panel
		myVPanel = new ViewerPanel(viewerOptions);
		myViewer = myVPanel.getViewer();
		absPanel.add(myVPanel);
		RootPanel.get().add(absPanel);
		 
		// Capability Demos	- for demo only; not meant to work seamlessly or simultaneously!	
		drawShapes();						//Draw some basic shapes (Entities) - Point, Ellipse, Polygon
		drawCustomPolygon();				//Draw a custom polygon on map as user
		//highlightOnMouseOver();			// Highlight Entity on mouseover (currently only valid for Point)	
		connectMarkers();					//Connect markers (Point Entities) with lines by clicking
		showPositionOnMouseOver();			//Show map position in upper left on map mouseover
		//renderLargeDatasetPrimitives();	//Render dataset using Primitives API (no access to individual elements)
		//renderLargeDatasetEntities();		//Render dataset using Entities and clustering (does not currently work along with highlight on mouseover demo) 
	}	
	
	//Create imagery base layers
	public ProviderViewModel[] createImageryBaseLayers() {
		ProviderViewModel[] providerViewModels = new ProviderViewModel[2];
		
		//ArcGIS (web connection required)		
		ProviderViewModelOptions.CreationFunction<ImageryProvider> arcGisCreateFunction;
		  arcGisCreateFunction = new ProviderViewModelOptions.CreationFunction<ImageryProvider>(){		  
			@Override
			public ImageryProvider function() {
				ArcGisMapServerImageryProviderOptions arcGisMapServerImageryProviderOptions = new ArcGisMapServerImageryProviderOptions();
				arcGisMapServerImageryProviderOptions.url = "https://server.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer";		
				ImageryProvider imageryProvider = new ArcGisMapServerImageryProvider(arcGisMapServerImageryProviderOptions);	
				return imageryProvider;
			}			
		  };
		  
		  ProviderViewModelOptions viewModelOptions = new ProviderViewModelOptions();	
		  viewModelOptions.creationFunction = arcGisCreateFunction;
		  viewModelOptions.name = "Esri";
		  viewModelOptions.tooltip = "Esri World Street Map Tooltip";
		  viewModelOptions.iconUrl = "cesiumdemo/cs/Cesium/Widgets/Images/ImageryProviders/esriWorldStreetMap.png";		  
		  viewModelOptions.creationFunction = arcGisCreateFunction;
		  ProviderViewModel viewModel = new ProviderViewModel(viewModelOptions);
		  providerViewModels[0] = viewModel;
		  		  
		  //Natural Earth
		  ProviderViewModelOptions.CreationFunction<ImageryProvider> natEarthCreateFunction;
		  natEarthCreateFunction = new ProviderViewModelOptions.CreationFunction<ImageryProvider>() {		  
			@Override
			public ImageryProvider function() {				
				TileMapServiceImageryProviderOptions tileMapServiceOptions = new TileMapServiceImageryProviderOptions();
				tileMapServiceOptions.url = "cesiumdemo/cs/Cesium/Assets/Textures/NaturalEarthII";
				ImageryProvider natEarthImageryProvider = Cesium.createTileMapServiceImageryProvider(tileMapServiceOptions);		
				return natEarthImageryProvider;
			}
		  };
		  ProviderViewModelOptions natEarthViewModelOptions = new ProviderViewModelOptions();	  
		  natEarthViewModelOptions.name = "Natural Earth";
		  natEarthViewModelOptions.tooltip = "Natural Earth II, darkened for contrast";
		  natEarthViewModelOptions.iconUrl = "cesiumdemo/cs/Cesium/Widgets/Images/ImageryProviders/naturalEarthII.png";
		  natEarthViewModelOptions.creationFunction = natEarthCreateFunction;
		  ProviderViewModel natEarthViewModel = new ProviderViewModel(natEarthViewModelOptions);
		  providerViewModels[1] = natEarthViewModel;	
		  
		  return providerViewModels;
	}	
	
	// Create PointGraphics Entity
	
	//Create a new Point Entity
	private Entity createNewPointEntity(Cartesian3 position, Color color, Color outlineColor, Integer pixelSize) {
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

	// Create PolygonGraphics Entity
	
	//Create a new polygon entity
	private Entity createNewPolygonEntity(Cartesian3[] polygonVerts, Color outlineColor, MaterialProperty material) {

		Entity polygonEntity = new Entity();
		// Polygon options
		PolygonGraphicsOptions polyOpt = new PolygonGraphicsOptions();
		polyOpt.outline = new ConstantProperty<>(true);
		polyOpt.outlineColor = new ConstantProperty<>(outlineColor);
		polyOpt.height = new ConstantProperty<>(0);
		polyOpt.fill = new ConstantProperty<>(false);
		polyOpt.material = material;

		// Polygon vertices
		polyOpt.hierarchy = new ConstantProperty<>(polygonVerts);

		PolygonGraphics myPolygon = new PolygonGraphics(polyOpt);
		polygonEntity.polygon = myPolygon;

		return polygonEntity;
	}

	// Draw basic user-defined shapes (Entities)
	
	//Draw basic shapes (Entities) - Pint, Ellipse, Polygon
	private void drawShapes() {

		// Position
		ConstantPositionProperty position = new ConstantPositionProperty();
		position.setValue(Cartesian3.fromDegrees(-77.0, 38.9));

		// Point Entities
		Entity pointEntity = createNewPointEntity(Cartesian3.fromDegrees(-77.0, 38.9), 
				Color.DARKRED(), Color.AQUA(), 10);
		Entity pointEntity1 = createNewPointEntity(Cartesian3.fromDegrees(-79.08, 38.38), 
				Color.PURPLE(), Color.BLUE(), 10);
		Entity pointEntity2 = createNewPointEntity(Cartesian3.fromDegrees(-79.2, 38.40),
				Color.PURPLE(), Color.BLUE(), 10);
		Entity pointEntity3 = createNewPointEntity(Cartesian3.fromDegrees(-79.15, 38.36), 
				Color.PURPLE(), Color.BLUE(), 10);

		// Point Label
		LabelGraphicsOptions lgOptions = new LabelGraphicsOptions();
		lgOptions.horizontalOrigin = new ConstantProperty<>(HorizontalOrigin.LEFT());
		lgOptions.verticalOrigin = new ConstantProperty<>(VerticalOrigin.TOP());
		lgOptions.eyeOffset = new ConstantProperty<>(new Cartesian3(5, 5, 0));
		pointEntity.label = new LabelGraphics(lgOptions);
		pointEntity.label.text = new ConstantProperty<>("Point Entity");
		
		//Ellipse Entity 
		Entity ellipseEntity = new Entity();
		  
		//Ellipse Label 
		ellipseEntity.label = new LabelGraphics();
		ellipseEntity.label.text = new ConstantProperty<>("Ellipse Entity");
		ellipseEntity.label.horizontalOrigin = new ConstantProperty<>(HorizontalOrigin.LEFT());
		ellipseEntity.label.verticalOrigin = new ConstantProperty<>(VerticalOrigin.TOP()); 
		ellipseEntity.label.pixelOffset = new ConstantProperty<>( new Cartesian2(20.0, -50.0));
		  
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
		Entity polygonEntity = createNewPolygonEntity(Cartesian3.fromDegreesArray( new double[] {-78.0,38.0, -76.0, 38.0, -77.0, 39.0}), 
				Color.AQUA(), 
				new GridMaterialProperty(new GridMaterialPropertyOptions())); 
		//Polygon Label 
		LabelGraphicsOptions  lgPolyOptions = new LabelGraphicsOptions(); 
		lgPolyOptions.text = new ConstantProperty<>("Polygon Entity"); 
		lgPolyOptions.horizontalOrigin = new ConstantProperty<>(HorizontalOrigin.LEFT()); 
		lgPolyOptions.verticalOrigin = new ConstantProperty<>(VerticalOrigin.TOP()); 
		lgPolyOptions.eyeOffset = new ConstantProperty<>(new Cartesian3(5,-50,0)); 
		polygonEntity.label = new LabelGraphics(lgPolyOptions);
		
		//Add entities to viewer
		myViewer.entities().add(ellipseEntity);
		myViewer.entities().add(polygonEntity);		 
		myViewer.entities().add(pointEntity);
		myViewer.entities().add(pointEntity1);
		myViewer.entities().add(pointEntity2);
		myViewer.entities().add(pointEntity3);
			
		//Zoom to specific entity
		//myViewer.selectedEntity = pointEntity;
		// myViewer.zoomTo(myViewer.selectedEntity);
	} 

	// Draw a custom polygon
	
	//Draw custom polygon
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

		// Action Buttons
		Button btnDrawPolygon = new Button();
		btnDrawPolygon.setText("Draw Polygon");
		btnDrawPolygon.setWidth("120px");

		Button btnPolygonDone = new Button();
		btnPolygonDone.setText("Done");
		btnPolygonDone.setWidth("60px");

		Button btnPolygonCancel = new Button();
		btnPolygonCancel.setText("Cancel");
		btnPolygonCancel.setWidth("60px");
		
		//"Draw Polygon" button
		btnDrawPolygon.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				// Add polygon click handler back to map
				myHandler.setInputAction(drawPolygonHandler, ScreenSpaceEventType.LEFT_CLICK());
				currentPolyVert.clear();

				// Add done and cancel buttons
				absPanel.add(btnPolygonDone, 132, 35);
				absPanel.add(btnPolygonCancel, 194, 35);

				// Create new Entity polygon and add to map
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
		
		//"Done" button
		btnPolygonDone.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				myHandler.removeInputAction(ScreenSpaceEventType.LEFT_CLICK());
				absPanel.remove(btnPolygonDone);
				absPanel.remove(btnPolygonCancel);
			}
		});

		//"Cancel" button
		btnPolygonCancel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				myViewer.entities().remove(myViewer.entities().getById(currentPolyId));
				myHandler.removeInputAction(ScreenSpaceEventType.LEFT_CLICK());
				absPanel.remove(btnPolygonDone);
				absPanel.remove(btnPolygonCancel);
			}
		});
	}

	
	// Draw lines between markers
	private void connectMarkers() {

		// Initialize vars for Entity Select Handlers
		PointGraphics pickedPoint = new PointGraphics();
		List<Cartesian3> polylineVerts = new ArrayList<>();
		ScreenSpaceEventHandler pickEntityHandler = new ScreenSpaceEventHandler(myVPanel.getViewer().scene().canvas());
		ScreenSpaceEventHandler dynamicLineHandler = new ScreenSpaceEventHandler(myVPanel.getViewer().scene().canvas());

		// Add Action Buttons
		Button btnConnectPoints = new Button();
		btnConnectPoints.setText("Connect Points");
		btnConnectPoints.setWidth("120px");

		Button btnConnectDone = new Button();
		btnConnectDone.setText("Done");
		btnConnectDone.setWidth("60px");

		Button btnConnectCancel = new Button();
		btnConnectCancel.setText("Cancel");
		btnConnectCancel.setWidth("60px");
		
		//"Connect Points" button
		btnConnectPoints.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				absPanel.add(btnConnectDone, 132, 60);
				absPanel.add(btnConnectCancel, 194, 60);

				// Create new Polyline Entity
				Entity lineEntity = new Entity();
				PolylineGraphics myPolyLine = new PolylineGraphics();
				lineEntity.polyline = myPolyLine;
				currentLineId = lineEntity.id;
				myViewer.entities().add(lineEntity);

				// Add handler to Select an Entity on click
				pickEntityHandler.setInputAction(new ScreenSpaceEventHandler.Listener<MouseClickEvent>() {
					@Override
					public void function(MouseClickEvent event) {
						Entity pickedEntity = new Entity();

						// Pick Entity on mouse click
						try {
							pickedObject = (PickedObject<Entity>) myVPanel.getViewer().scene().pick(event.position);
						} catch (ClassCastException e) {
							pickedObject = null;
						}

						// If Cesium found an Entity with point, add it to the polyline
						if (Cesium.defined(pickedObject)) {
							pickedEntity = pickedObject.id;
							if (Cesium.defined(pickedEntity.point)) {
								Cartesian3 pickedPosition = pickedEntity.position.getValue(JulianDate.now());
								polylineVerts.add(pickedPosition);
								myPolyLine.positions = new ConstantProperty<>(polylineVerts.toArray());
								myPolyLine.definitionChanged();
							}
							// if selected Entity does not contain a Point
							else {
								Window.alert("Please select an existing point on the map");
							}
						}
						// If no Entity selected
						else {
							Window.alert("Please select an existing point on the map");
						}
					}
				}, ScreenSpaceEventType.LEFT_CLICK());

				// Add handler to add point to Polyline on mouse move
				dynamicLineHandler.setInputAction(new ScreenSpaceEventHandler.Listener<MouseMoveEvent>() {
					@Override
					public void function(MouseMoveEvent event) {

					}
				}, ScreenSpaceEventType.MOUSE_MOVE());
			}
		});

		//"Done" button
		btnConnectDone.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				absPanel.remove(btnConnectDone);
				absPanel.remove(btnConnectCancel);
				pickEntityHandler.removeInputAction(ScreenSpaceEventType.LEFT_CLICK());
				dynamicLineHandler.removeInputAction(ScreenSpaceEventType.MOUSE_MOVE());
				polylineVerts.clear();
			}
		});
		
		//"Cancel" button
		btnConnectCancel.addClickHandler(new ClickHandler() {
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
	
	//Highlight point Entity on mouseover
	// Highlight Entity on mouseover (currently only valid for Point)	
	private void highlightOnMouseOver() {
		ScreenSpaceEventHandler myHandler = new ScreenSpaceEventHandler(myVPanel.getViewer().scene().canvas());
		myHandler.setInputAction(new ScreenSpaceEventHandler.Listener<MouseMoveEvent>() {
			@Override
			public void function(MouseMoveEvent event) {

				// Determine if a picked entity already existed (from last mouse-move), return
				// it to its original color
				//GWT.log("did we get here");
				if (Cesium.defined(pickedEntity.point)) {
					pickedEntity.point.outlineColor = baseColor;
				}

				// Pick Entity on mouse-over
				try {
					pickedObject = (PickedObject<Entity>) myVPanel.getViewer().scene().pick(event.endPosition);

				} catch (ClassCastException e) {
					pickedObject = null;
				}

				// If Cesium found an Entity, determine if it is a point and update the color
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

	// Show global position in top left corner of viewer on mouse move
	
	//Show map position on mouseover
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
					BigDecimal lat = new BigDecimal(Math.toDegrees(cartographic.latitude)).setScale(3,
							RoundingMode.HALF_EVEN);
					BigDecimal lon = new BigDecimal(Math.toDegrees(cartographic.longitude)).setScale(3,
							RoundingMode.HALF_EVEN);
					String strLat;
					String strLon;
					if (lat.compareTo(new BigDecimal(0)) == (-1)) {
						strLat = lat.abs().toString() + "S";
					} else {
						strLat = lat.toString() + "N";
					}

					if (lon.compareTo(new BigDecimal(0)) == (-1)) {
						strLon = lon.abs().toString() + "W";
					} else {
						strLon = lon.toString() + "E";
					}
					lblPosition.setText(strLat + ", " + strLon);
				} else {
					lblPosition.setText("");
				}
			}
		}, ScreenSpaceEventType.MOUSE_MOVE());
	} 

	// Get position on globe on mouse move
	
	//Get position on mouse move
	private Cartesian3 getPosition(MouseMoveEvent event) {
		Cartesian3 c3Location = myVPanel.getViewer().camera.pickEllipsoid(event.endPosition,
				myVPanel.getViewer().scene().globe.ellipsoid);
		return c3Location;
	} 

	// Get position on globe on mouse click
	
	// get position on mouse click
	private Cartesian3 getPosition(MouseClickEvent event) {
		Cartesian3 c3Location = myVPanel.getViewer().camera.pickEllipsoid(event.position,
				myVPanel.getViewer().scene().globe.ellipsoid);
		return c3Location;
	} 

	
	// Render large set of data using Geometry and Appearances via the Cesium Primitive API
	private void renderLargeDatasetPrimitives() {
		Primitive rectangle = new Primitive();
		Scene scene = myVPanel.getViewer().scene();
		PrimitiveOptions primitiveOptions = new PrimitiveOptions();
		GeometryInstanceOptions geometryInstanceOptions = new GeometryInstanceOptions();
		RectangleGeometryOptions rectangleGeometryOptions = new RectangleGeometryOptions();
		List<GeometryInstance> geometryInstances = new ArrayList<GeometryInstance>();
		//Integer i = 1;
		for (int lat = -60; lat <= 60; lat += 2) {
			for (int lon = -60; lon < 60; lon += 2) {
				rectangleGeometryOptions.rectangle = Rectangle.fromDegrees(lon, lat, lon + 1, lat + 1);
				rectangleGeometryOptions.vertexFormat = PerInstanceColorAppearance.VERTEX_FORMAT();
				geometryInstanceOptions.geometry = RectangleGeometry
						.createGeometry(new RectangleGeometry(rectangleGeometryOptions));
				geometryInstanceOptions.id = Integer.toString(lat) + Integer.toString(lon);
				geometryInstances.add(new GeometryInstance(geometryInstanceOptions));
				//i++;
			}
		}

		rectangleGeometryOptions.rectangle = Rectangle.fromDegrees(-120.0, 20.0, -60.0, 40.0);
		rectangleGeometryOptions.vertexFormat = EllipsoidSurfaceAppearance.VERTEX_FORMAT();
		geometryInstanceOptions.geometry = RectangleGeometry
				.createGeometry(new RectangleGeometry(rectangleGeometryOptions));
		primitiveOptions.geometryInstances = (GeometryInstance[]) geometryInstances.toArray();
		EllipsoidSurfaceAppearanceOptions ellipsoidSurfaceAppearanceOptions = new EllipsoidSurfaceAppearanceOptions();
		ellipsoidSurfaceAppearanceOptions.aboveGround = false;
		primitiveOptions.asynchronous = false;
		primitiveOptions.appearance = new EllipsoidSurfaceAppearance(ellipsoidSurfaceAppearanceOptions);
		rectangle = scene.primitives().add(new Primitive(primitiveOptions));
	}

	// Render large set of data using Geometry and Appearances via the Cesium Entities API
	// Add clustering capability to points
	private void renderLargeDatasetEntities() {
		buildClusterIcons();
		defaultDataSource = myViewer.dataSourceDisplay().defaultDataSource;
		for (float lat = 30; lat <= 90; lat += 1) {
			for (float lon = -90; lon <= 0; lon += 1) {

				// Point Entities
				Entity pointEntity = createNewPointEntity(Cartesian3.fromDegrees(lon, lat), Color.ANTIQUEWHITE(),
						Color.AZURE(), 5);

				// Create label
				LabelGraphicsOptions lgOptions = new LabelGraphicsOptions();
				// lgOptions.horizontalOrigin = new ConstantProperty<>(HorizontalOrigin.LEFT());
				lgOptions.verticalOrigin = new ConstantProperty<>(VerticalOrigin.CENTER());
				// lgOptions.eyeOffset = new ConstantProperty<>(new Cartesian3(5,5,0));
				pointEntity.label = new LabelGraphics(lgOptions);
				pointEntity.label.text = new ConstantProperty<>("Lat: " + lat + " Lon: " + lon);

				//PolygonEntities (no clustering capability)
				Entity polygonEntity = createNewPolygonEntity(
						Cartesian3.fromDegreesArray(
								new double[] { lon, lat, lon + 0.05, lat, lon + 0.05, lat + 0.05, lon, lat + 0.05 }),
						Color.CYAN(), new ColorMaterialProperty(Color.WHITE()));
				
				//myViewer.entities().add(polygonEntity);
				//pointEntity.billboard = createBillboardLabel("Lat: " + lat + " Lon: " + lon);
				myViewer.entities().add(pointEntity);
				
			} // for lon
		} // for lat

		myViewer.dataSourceDisplay().defaultDataSource.clustering.enabled = true;
		myViewer.dataSourceDisplay().defaultDataSource.clustering.pixelRange = 5;
		myViewer.dataSourceDisplay().defaultDataSource.clustering.minimumClusterSize = 2;
	}
	
	//Build custom cluster icons

	// Build icons for clustered markers
	@SuppressWarnings("unchecked")
	private void buildClusterIcons() {		
		String[] singleDigitIcons = new String[8];
		for (int i = 0; i < singleDigitIcons.length; ++i) {
			singleDigitIcons[i] = "Images/" + (i+2) + ".png"; 
		}

		// Create event listener
		Event.RemoveCallback removeEvent = myViewer.dataSourceDisplay().defaultDataSource.clustering.clusterEvent
				.addEventListener(new EntityCluster.newClusterCallback() {
					@Override
					public void function(Entity[] clusteredEntities, EntityClusterObject cluster) {
						cluster.label.show = false;
						cluster.billboard.show = true;
						cluster.billboard.verticalOrigin = VerticalOrigin.BOTTOM();
						if (clusteredEntities.length >= 50) {
							cluster.billboard.image = "Images/50.png";
							cluster.billboard.color = Color.RED();
							cluster.billboard.height = 30;
							cluster.billboard.width = 30;
						} else if (clusteredEntities.length >= 40) {
							cluster.billboard.image = "Images/40.png";	
							cluster.billboard.color = Color.ORANGE();
							cluster.billboard.height = 30;
							cluster.billboard.width = 30;
						} else if (clusteredEntities.length >= 30) {
							cluster.billboard.image = "Images/30.png";	
							cluster.billboard.color = Color.YELLOW();
							cluster.billboard.height = 30;
							cluster.billboard.width = 30;
						} else if (clusteredEntities.length >= 20) {
							cluster.billboard.image = "Images/20.png";		
							cluster.billboard.color = Color.GREEN();
							cluster.billboard.height = 30;
							cluster.billboard.width = 30;							
						} else if (clusteredEntities.length >= 10) {
							cluster.billboard.image = "Images/10.png";	
							cluster.billboard.color = Color.BLUE();
							cluster.billboard.height = 30;
							cluster.billboard.width = 30;
						} else {
							cluster.billboard.image = singleDigitIcons[clusteredEntities.length - 2];
							cluster.billboard.color = Color.WHITE();
							cluster.billboard.height = 30;
							cluster.billboard.width = 30;
						}
					}
				});	
	}
}
