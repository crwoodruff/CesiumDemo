/*
 * Copyright 2017 iserge.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.cesiumjs.cs;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import jsinterop.annotations.JsFunction;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import org.cesiumjs.cs.core.Cartographic;
import org.cesiumjs.cs.core.Ellipsoid;
import org.cesiumjs.cs.core.providers.TerrainProvider;
import org.cesiumjs.cs.js.JsArray;
import org.cesiumjs.cs.js.JsImage;
import org.cesiumjs.cs.js.JsObject;
import org.cesiumjs.cs.promise.Promise;
import org.cesiumjs.cs.scene.providers.UrlTemplateImageryProvider;
import org.cesiumjs.cs.scene.providers.options.OpenStreetMapImageryProviderOptions;
import org.cesiumjs.cs.scene.providers.options.TileMapServiceImageryProviderOptions;
import org.cesiumjs.cs.widgets.Command;

/**
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public class Cesium {
    @JsProperty(namespace = JsPackage.GLOBAL, name = "Cesium")
    public static native boolean isInitialized();

    @JsProperty(namespace = "Cesium", name = "VERSION")
    public static native String version();

    @JsProperty(namespace = JsPackage.GLOBAL, name = "CesiumPath")
    public static native String path();

    public static native void log(Object object) /*-{
        console.log(object);
    }-*/;
    
    /**
     * Fix for SmartGWT
     * Thanks for Mark Erikson (https://groups.google.com/forum/#!msg/cesium-dev/ZfyW0CNRsSU/lP6KTaUpEQAJ)
     */
    public static native void fixSmartGWT() /*-{
        if ($wnd.buildInDataView !== undefined) {
            $wnd.DataView = $wnd.buildInDataView;
        } else {
            console.log("$wnd.buildInDataView === undefined");
        }
    }-*/;

    @JsMethod(namespace = "Cesium")
    public static native boolean defined(Object object);

    @JsMethod(namespace = "Cesium", name = "loadImage")
    public static native Promise<JsImage, Void> loadImage(String url);
    /**
     * Asynchronously loads the given imagery URL. Returns a promise that will resolve to an Image once loaded, or reject if the imagery failed to load
     * @param url The source of the imagery, or a promise for the URL.
     * @param allowCrossOrigin Whether to request the imagery using Cross-Origin Resource Sharing (CORS).
     *                         CORS is only actually used if the imagery URL is actually cross-origin.
     *                         Data URIs are never requested using CORS.
     * @return a promise that will resolve to the requested data when loaded.
     */
    @JsMethod(namespace = "Cesium", name = "loadImage")
    public static native Promise<JsImage, Void> loadImage(String url, boolean allowCrossOrigin);

    /**
     * Asynchronously loads the given URL as JSON. Returns a promise that will resolve to a JSON object once loaded, or
     * reject if the URL failed to load. The data is loaded using XMLHttpRequest, which means that in order to make
     * requests to another origin, the server must have Cross-Origin Resource Sharing (CORS) headers enabled.
     * This function adds 'Accept: application/json,*\/*;q=0.01' to the request headers, if not already specified.
     * @param url The URL to request, or a promise for the URL.
     * @return a promise that will resolve to the requested data when loaded.
     */
    @JsMethod(namespace = "Cesium", name = "loadJson")
    public static native Promise<JsObject, String> loadJson(Promise<String, Void> url);

    /**
     * Asynchronously loads the given URL as JSON. Returns a promise that will resolve to a JSON object once loaded, or
     * reject if the URL failed to load. The data is loaded using XMLHttpRequest, which means that in order to make
     * requests to another origin, the server must have Cross-Origin Resource Sharing (CORS) headers enabled.
     * This function adds 'Accept: application/json,*\/*;q=0.01' to the request headers, if not already specified.
     * @param url The URL to request, or a promise for the URL.
     * @return a promise that will resolve to the requested data when loaded.
     */
    @JsMethod(namespace = "Cesium", name = "loadJson")
    public static native Promise<JsObject, String> loadJson(String url);

    /**
     * Asynchronously loads the given URL as JSON. Returns a promise that will resolve to a JSON object once loaded, or
     * reject if the URL failed to load. The data is loaded using XMLHttpRequest, which means that in order to make
     * requests to another origin, the server must have Cross-Origin Resource Sharing (CORS) headers enabled.
     * This function adds 'Accept: application/json,*\/*;q=0.01' to the request headers, if not already specified.
     * @param url The URL to request, or a promise for the URL.
     * @param headers HTTP headers to send with the request. 'Accept: application/json,*\/*;q=0.01' is added to the request headers automatically if not specified.
     * @return a promise that will resolve to the requested data when loaded.
     */
    @JsMethod(namespace = "Cesium", name = "loadJson")
    public static native Promise<JsObject, String> loadJson(Promise<String, Void> url, Object headers);

    /**
     * Asynchronously loads the given URL as JSON. Returns a promise that will resolve to a JSON object once loaded, or
     * reject if the URL failed to load. The data is loaded using XMLHttpRequest, which means that in order to make
     * requests to another origin, the server must have Cross-Origin Resource Sharing (CORS) headers enabled.
     * This function adds 'Accept: application/json,*\/*;q=0.01' to the request headers, if not already specified.
     * @param url The URL to request, or a promise for the URL.
     * @param headers HTTP headers to send with the request. 'Accept: application/json,*\/*;q=0.01' is added to the request headers automatically if not specified.
     * @return a promise that will resolve to the requested data when loaded.
     */
    @JsMethod(namespace = "Cesium", name = "loadJson")
    public static native Promise<JsObject, String> loadJson(String url, Object headers);

    /**
     * Asynchronously loads the given URL as text. Returns a promise that will resolve to a String once loaded,
     * or reject if the URL failed to load. The data is loaded using XMLHttpRequest, which means that in order to
     * make requests to another origin, the server must have Cross-Origin Resource Sharing (CORS) headers enabled.
     * @param url The URL to request, or a promise for the URL.
     * @return a promise that will resolve to the requested data when loaded.
     */
    @JsMethod(namespace = "Cesium", name = "loadText")
    public static native Promise<String, String> loadText(String url);

    /**
     * Asynchronously loads the given URL as text. Returns a promise that will resolve to a String once loaded,
     * or reject if the URL failed to load. The data is loaded using XMLHttpRequest, which means that in order to
     * make requests to another origin, the server must have Cross-Origin Resource Sharing (CORS) headers enabled.
     * @param url The URL to request, or a promise for the URL.
     * @param headers HTTP headers to send with the request.
     * @return a promise that will resolve to the requested data when loaded.
     */
    @JsMethod(namespace = "Cesium", name = "loadText")
    public static native Promise<String, String> loadText(String url, JsObject headers);

    /**
     * Asynchronously loads the given URL as text. Returns a promise that will resolve to a String once loaded,
     * or reject if the URL failed to load. The data is loaded using XMLHttpRequest, which means that in order to
     * make requests to another origin, the server must have Cross-Origin Resource Sharing (CORS) headers enabled.
     * @param url The URL to request, or a promise for the URL.
     * @return a promise that will resolve to the requested data when loaded.
     */
    @JsMethod(namespace = "Cesium", name = "loadText")
    public static native Promise<String, String> loadText(Promise<String, String> url);

    /**
     * Asynchronously loads the given URL as text. Returns a promise that will resolve to a String once loaded,
     * or reject if the URL failed to load. The data is loaded using XMLHttpRequest, which means that in order to
     * make requests to another origin, the server must have Cross-Origin Resource Sharing (CORS) headers enabled.
     * @param url The URL to request, or a promise for the URL.
     * @param headers HTTP headers to send with the request.
     * @return a promise that will resolve to the requested data when loaded.
     */
    @JsMethod(namespace = "Cesium", name = "loadText")
    public static native Promise<String, String> loadText(Promise<String, String> url, JsObject headers);

    /**
     * Initiates a terrain height query for an array of {@link Cartographic} positions by requesting tiles from a terrain provider,
     * sampling, and interpolating. The interpolation matches the triangles used to render the terrain at the specified level.
     * The query happens asynchronously, so this function returns a promise that is resolved when the query completes.
     * Each point height is modified in place. If a height can not be determined because no terrain data is available
     * for the specified level at that location, or another error occurs, the height is set to undefined.
     * As is typical of the {@link Cartographic} type, the supplied height is a height above the reference
     * ellipsoid (such as {@link Ellipsoid#WGS84()}) rather than an altitude above mean sea level.
     * In other words, it will not necessarily be 0.0 if sampled in the ocean.
     * @param terrainProvider The terrain provider from which to query heights.
     * @param level The terrain level-of-detail from which to query terrain heights.
     * @param positions The positions to update with terrain heights.
     * @return A promise that resolves to the provided list of positions when terrain the query has completed.
     */
    @JsMethod(namespace = "Cesium", name = "sampleTerrain")
    public static native Promise<Cartographic[], Void> sampleTerrain(TerrainProvider terrainProvider, int level, Cartographic[] positions);

    /**
     * Creates a UrlTemplateImageryProvider instance that provides tiled imagery hosted by OpenStreetMap or
     * another provider of Slippy tiles. The default url connects to OpenStreetMap's volunteer-run servers,
     * so you must conform to their Tile Usage Policy.
     * @return The imagery provider.
     */
    @JsMethod(namespace = "Cesium", name = "createOpenStreetMapImageryProvider")
    public static native UrlTemplateImageryProvider createOpenStreetMapImageryProvider();

    /**
     * Creates a UrlTemplateImageryProvider instance that provides tiled imagery hosted by OpenStreetMap or
     * another provider of Slippy tiles. The default url connects to OpenStreetMap's volunteer-run servers,
     * so you must conform to their Tile Usage Policy.
     * @param options Options
     * @return The imagery provider.
     */
    @JsMethod(namespace = "Cesium", name = "createOpenStreetMapImageryProvider")
    public static native UrlTemplateImageryProvider createOpenStreetMapImageryProvider(OpenStreetMapImageryProviderOptions options);

    /**
     * Creates a UrlTemplateImageryProvider instance that provides tiled imagery as generated by GDDAL2Tiles etc.
     * @return The imagery provider.
     */
    @JsMethod(namespace = "Cesium", name = "createTileMapServiceImageryProvider")
    public static native UrlTemplateImageryProvider createTileMapServiceImageryProvider();

    /**
     * Creates a UrlTemplateImageryProvider instance that provides tiled imagery as generated by GDDAL2Tiles etc.
     * @param options Options
     * @return The imagery provider.
     */
    @JsMethod(namespace = "Cesium", name = "createTileMapServiceImageryProvider")
    public static native UrlTemplateImageryProvider createTileMapServiceImageryProvider(TileMapServiceImageryProviderOptions options);

    /**
     * Create a Command from a given function, for use with ViewModels. A Command is a function with an extra canExecute
     * observable property to determine whether the command can be executed. When executed, a Command function will check
     * the value of canExecute and throw if false. It also provides events for when a command has been or is about to be executed.
     * @param func The function to execute.
     * @return Command function
     */
    @JsMethod(namespace = "Cesium", name = "createCommand")
    public static native Command createCommand(Function func);

    /**
     * Create a Command from a given function, for use with ViewModels. A Command is a function with an extra canExecute
     * observable property to determine whether the command can be executed. When executed, a Command function will check
     * the value of canExecute and throw if false. It also provides events for when a command has been or is about to be executed.
     * @param func The function to execute.
     * @param canExecute A boolean indicating whether the function can currently be executed.
     * @return Command function
     */
    @JsMethod(namespace = "Cesium", name = "createCommand")
    public static native Command createCommand(Function func, boolean canExecute);

    /**
     * A browser-independent function to request a new animation frame. This is used to create an application's
     * draw loop as shown in the example below.
     * @param callback The function to call when the next frame should be drawn.
     * @return An ID that can be passed to {@link #cancelAnimationFrame} to cancel the request.
     */
    //TODO Example
    @JsMethod(namespace = "Cesium", name = "requestAnimationFrame")
    public static native Number requestAnimationFrame(RequestAnimationFrameCallback callback);

    /**
     * A browser-independent function to cancel an animation frame requested using requestAnimationFrame.
     * @param requestID The value returned by requestAnimationFrame.
     */
    @JsMethod(namespace = "Cesium", name = "cancelAnimationFrame")
    public static native void cancelAnimationFrame(Number requestID);

    /**
     * Initiates a sampleTerrain() request at the maximum available tile level for a terrain dataset.
     * @param terrainProvider The terrain provider from which to query heights.
     * @param positions The positions to update with terrain heights.
     * @return A promise that resolves to the provided list of positions when terrain the query has completed. This promise will reject if the terrain provider's `availability` property is undefined.
     * <pre>
     *     Example:
     *     {@code
     *     CesiumTerrainProviderOptions options = new CesiumTerrainProviderOptions();
     *     options.url = "https://assets.agi.com/stk-terrain/world";
     *     CesiumTerrainProvider terrainProvider = new CesiumTerrainProvider(options);
     *     Cartographic[] positions = new Cartographic[] {Cartographic.fromDegrees(86.925145, 27.988257), Cartographic.fromDegrees(87.0, 28.0)};
     *     Promise<Cartographic[], Void> promise = Cesium.sampleTerrainMostDetailed(terrainProvider, positions);
     *     promise.then(new Fulfill<Cartographic[]>() {
     *         @Override
     *         public void onFulfilled(Cartographic[] updatedPositions) {
     *             //
     *         }
     *     });
     *     }
     * </pre>
     */
    @JsMethod(namespace = "Cesium", name = "sampleTerrainMostDetailed")
    public static native Promise<Cartographic[], Void> sampleTerrainMostDetailed(TerrainProvider terrainProvider, Cartographic[] positions);

    @JsFunction
    public interface Function {
        Object function(Object ...args);
    }

    @JsFunction
    public interface RequestAnimationFrameCallback {
        /**
         * A function that will be called when the next frame should be drawn.
         * @param timestamp A timestamp for the frame, in milliseconds.
         */
        void Callback(Number timestamp);
    }
}
