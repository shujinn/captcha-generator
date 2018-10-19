package main;

import com.google.code.kaptcha.GimpyEngine;
import com.google.code.kaptcha.NoiseProducer;
import com.google.code.kaptcha.util.Configurable;
import com.jhlabs.image.RippleFilter;
import com.jhlabs.image.ShadowFilter;
import com.jhlabs.image.TransformFilter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class ShadowGimpy extends Configurable implements GimpyEngine {


    /**
     *
     * @param baseImage
     * @return
     */

    @Override
    public BufferedImage getDistortedImage(BufferedImage baseImage) {
        // get config for noise implement
        NoiseProducer noiseProducer = getConfig().getNoiseImpl();
        // add new noise implement
//        NoiseProducer noiseProducer1;

        BufferedImage distortedImage = new BufferedImage(baseImage.getWidth(),
                baseImage.getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D graph = (Graphics2D) distortedImage.getGraphics();

        ShadowFilter shadowFilter = new ShadowFilter();
        shadowFilter.setRadius(10);
        shadowFilter.setDistance(5);
        shadowFilter.setOpacity(1);

        Random rand = new Random();

        // wave
        RippleFilter rippleFilter = new RippleFilter();
        rippleFilter.setWaveType(RippleFilter.SINE);
        rippleFilter.setXAmplitude(7.6f);
        rippleFilter.setYAmplitude(rand.nextFloat() + 1.0f);
        rippleFilter.setXWavelength(rand.nextInt(7) + 8);
        rippleFilter.setYWavelength(rand.nextInt(3) + 2);
        rippleFilter.setEdgeAction(TransformFilter.BILINEAR);

        BufferedImage effectImage = rippleFilter.filter(baseImage, null);
        effectImage = shadowFilter.filter(effectImage, null);

        graph.drawImage(effectImage, 0, 0, null, null);
        graph.dispose();

        // draw lines over the image and/or text
        noiseProducer.makeNoise(distortedImage, .1f, .1f, .25f, .25f);
        noiseProducer.makeNoise(distortedImage, .1f, .25f, .5f, .9f);

        return distortedImage;
    }
}
