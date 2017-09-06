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

package org.cesiumjs.cs.scene.options;

import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import org.cesiumjs.cs.js.JsObject;

/**
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
public class MaterialOptions {
    /**
     * Throws errors for issues that would normally be ignored, including unused uniforms or materials.
     * Default: false
     */
    @JsProperty
    public boolean strict;
    /**
     * When true or a function that returns true, the geometry with this material is expected to appear translucent.
     * Default: true
     */
    @JsProperty
    public boolean translucent;
    /**
     * The fabric JSON used to generate the material.
     */
    @JsProperty
    public JsObject fabric;

    @JsConstructor
    public MaterialOptions() {}
}
