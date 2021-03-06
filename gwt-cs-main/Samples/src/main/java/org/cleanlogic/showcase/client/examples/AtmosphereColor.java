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

package org.cleanlogic.showcase.client.examples;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.*;
import org.cesiumjs.cs.core.Cartesian3;
import org.cesiumjs.cs.core.HeadingPitchRoll;
import org.cesiumjs.cs.core.Math;
import org.cesiumjs.cs.scene.Camera;
import org.cesiumjs.cs.scene.options.ViewOptions;
import org.cesiumjs.cs.widgets.ViewerPanel;
import org.cleanlogic.showcase.client.basic.AbstractExample;
import org.cleanlogic.showcase.client.components.store.ShowcaseExampleStore;
import org.cleanlogic.showcase.client.examples.slider.Slider;
import org.cleanlogic.showcase.client.examples.slider.SliderEvent;
import org.cleanlogic.showcase.client.examples.slider.SliderListener;

import javax.inject.Inject;

/**
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public class AtmosphereColor extends AbstractExample {
    private ViewerPanel _csVPanel;

    private Slider _hueShiftSlider;
    private Slider _saturationShiftSlider;
    private Slider _brightnessShiftSlider;

    private TextBox _hueShiftTBox;
    private TextBox _saturationShiftTBox;
    private TextBox _brightnessShiftTBox;

    @Inject
    public AtmosphereColor(ShowcaseExampleStore store) {
        super("Atmosphere Color", "Adjust hue, saturation, and brightness of the sky/atmosphere", new String[]{"Showcase", "Cesium", "3d", "atmosphere", "fog", "lighting", "hue", "saturation", "brightness"}, store);
    }

    @Override
    public void buildPanel() {
        _csVPanel = new ViewerPanel();
        Camera camera = _csVPanel.getViewer().camera;
        ViewOptions viewOptions = new ViewOptions();
        viewOptions.destinationPos = Cartesian3.fromDegrees(-75.5847, 40.0397, 1000.0);
        viewOptions.orientation = new HeadingPitchRoll(-Math.PI_OVER_TWO(), 0.2, 0.0);
        camera.setView(viewOptions);

        HorizontalPanel hueShiftHPanel = new HorizontalPanel();
        hueShiftHPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        hueShiftHPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        hueShiftHPanel.setSpacing(10);
        _hueShiftSlider = new Slider("hueShift", -100, 100, 0);
        _hueShiftSlider.setStep(1);
        _hueShiftSlider.setWidth("150px");
        _hueShiftSlider.addListener(new MSliderListener());
        _hueShiftTBox = new TextBox();
        _hueShiftTBox.addChangeHandler(new MChangeHandler());
        _hueShiftTBox.setText("0");
        _hueShiftTBox.setSize("30px", "12px");
        hueShiftHPanel.add(_hueShiftSlider);
        hueShiftHPanel.add(_hueShiftTBox);

        HorizontalPanel saturationShiftHPanel = new HorizontalPanel();
        saturationShiftHPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        saturationShiftHPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        saturationShiftHPanel.setSpacing(10);
        _saturationShiftSlider = new Slider("saturationShift", -100, 100, 0);
        _saturationShiftSlider.setStep(1);
        _saturationShiftSlider.setWidth("150px");
        _saturationShiftSlider.addListener(new MSliderListener());
        _saturationShiftTBox = new TextBox();
        _saturationShiftTBox.addChangeHandler(new MChangeHandler());
        _saturationShiftTBox.setText("0");
        _saturationShiftTBox.setSize("30px", "12px");
        saturationShiftHPanel.add(_saturationShiftSlider);
        saturationShiftHPanel.add(_saturationShiftTBox);

        HorizontalPanel brightnessShiftHPanel = new HorizontalPanel();
        brightnessShiftHPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        brightnessShiftHPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        brightnessShiftHPanel.setSpacing(10);
        _brightnessShiftSlider = new Slider("brightnessShift", -100, 100, 0);
        _brightnessShiftSlider.setStep(1);
        _brightnessShiftSlider.setWidth("150px");
        _brightnessShiftSlider.addListener(new MSliderListener());
        _brightnessShiftTBox = new TextBox();
        _brightnessShiftTBox.addChangeHandler(new MChangeHandler());
        _brightnessShiftTBox.setText("0");
        _brightnessShiftTBox.setSize("30px", "12px");
        brightnessShiftHPanel.add(_brightnessShiftSlider);
        brightnessShiftHPanel.add(_brightnessShiftTBox);

        CheckBox lightingCBox = new CheckBox();
        lightingCBox.setWidth("100px");
        lightingCBox.setValue(true);
        lightingCBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> valueChangeEvent) {
                _csVPanel.getViewer().scene().globe.enableLighting = !_csVPanel.getViewer().scene().globe.enableLighting;
            }
        });

        CheckBox fogCBox = new CheckBox();
        fogCBox.setWidth("100px");
        fogCBox.setValue(true);
        fogCBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> valueChangeEvent) {
                _csVPanel.getViewer().scene().fog.enabled = !_csVPanel.getViewer().scene().fog.enabled;
            }
        });

        FlexTable flexTable = new FlexTable();
        flexTable.setHTML(1, 0, "<font color=\"white\">hueShift</font>");
        flexTable.setWidget(1, 1, hueShiftHPanel);
        flexTable.setHTML(2, 0, "<font color=\"white\">saturationShift</font>");
        flexTable.setWidget(2, 1, saturationShiftHPanel);
        flexTable.setHTML(3, 0, "<font color=\"white\">brightnessShift</font>");
        flexTable.setWidget(3, 1, brightnessShiftHPanel);
        flexTable.setHTML(4, 0, "<font color=\"white\">Toggle Lighting</font>");
        flexTable.setWidget(4, 1, lightingCBox);
        flexTable.setHTML(5, 0, "<font color=\"white\">Toggle Fog</font>");
        flexTable.setWidget(5, 1, fogCBox);

        AbsolutePanel aPanel = new AbsolutePanel();
        aPanel.add(_csVPanel);
        aPanel.add(flexTable, 20, 20);

        contentPanel.add(new HTML("<p>Adjust hue, saturation, and brightness of the sky/atmosphere.</p>"));
        contentPanel.add(aPanel);

        initWidget(contentPanel);
    }

    private class MSliderListener implements SliderListener {

        @Override
        public void onStart(SliderEvent e) {

        }

        @Override
        public boolean onSlide(SliderEvent e) {
            Slider source = e.getSource();
            double value = source.getValue() / 100.;
            if (source == _hueShiftSlider) {
                _csVPanel.getViewer().scene().skyAtmosphere.hueShift = (float)value;
                _hueShiftTBox.setText(value + "");
            }
            else if (source == _saturationShiftSlider) {
                _csVPanel.getViewer().scene().skyAtmosphere.saturationShift = (float)value;
                _saturationShiftTBox.setText(value + "");
            }
            else if (source == _brightnessShiftSlider) {
                _csVPanel.getViewer().scene().skyAtmosphere.brightnessShift = (float)value;
                _brightnessShiftTBox.setText(value + "");
            }
            return true;
        }

        @Override
        public void onChange(SliderEvent e) {

        }

        @Override
        public void onStop(SliderEvent e) {

        }
    }

    private class MChangeHandler implements ChangeHandler {
        @Override
        public void onChange(ChangeEvent changeEvent) {
            TextBox source = (TextBox)changeEvent.getSource();
            double value = Double.parseDouble(source.getValue());
            if (source == _hueShiftTBox) {
                _csVPanel.getViewer().scene().skyAtmosphere.hueShift = (float)value;
                _hueShiftSlider.setValue((int)(value * 100));
            }
            else if (source == _saturationShiftTBox) {
                _csVPanel.getViewer().scene().skyAtmosphere.saturationShift = (float)value;
                _saturationShiftSlider.setValue((int)(value * 100));
            }
            else if (source == _brightnessShiftTBox) {
                _csVPanel.getViewer().scene().skyAtmosphere.brightnessShift = (float)value;
                _brightnessShiftSlider.setValue((int)(value * 100));
            }
        }
    }

    @Override
    public String[] getSourceCodeURLs() {
        String[] sourceCodeURLs = new String[1];
        sourceCodeURLs[0] = GWT.getModuleBaseURL() + "examples/" + "AtmosphereColor.txt";
        return sourceCodeURLs;
    }
}
