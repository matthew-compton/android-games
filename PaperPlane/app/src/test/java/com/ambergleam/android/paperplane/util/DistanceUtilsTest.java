package com.ambergleam.android.paperplane.util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DistanceUtilsTest {

    @Test
    public void testDistanceUtilsForLargeNumber() {
        // 1,200,300ft
        long testDistance = 1200300;
        String expectedDistanceString = "1,200,300ft";
        String actualDistanceString = DistanceUtils.formatDistance(testDistance);
        assertThat(actualDistanceString).isEqualTo(expectedDistanceString);
    }

    @Test
    public void testDistanceUtilsForMediumNumber() {
        // 1,200ft
        long testDistance = 1200;
        String expectedDistanceString = "1,200ft";
        String actualDistanceString = DistanceUtils.formatDistance(testDistance);
        assertThat(actualDistanceString).isEqualTo(expectedDistanceString);
    }

    @Test
    public void testDistanceUtilsForSmallNumber() {
        // 320ft
        long testDistance = 320;
        String expectedDistanceString = "320ft";
        String actualDistanceString = DistanceUtils.formatDistance(testDistance);
        assertThat(actualDistanceString).isEqualTo(expectedDistanceString);
    }

    @Test
    public void testDistanceUtilsForZero() {
        // 0ft
        long testDistance = 0;
        String expectedDistanceString = "0ft";
        String actualDistanceString = DistanceUtils.formatDistance(testDistance);
        assertThat(actualDistanceString).isEqualTo(expectedDistanceString);
    }

}