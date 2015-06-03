package com.ambergleam.android.paperplane.util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DistanceUtilsTest {

    @Test
    public void testDistanceUtilsForLargeNumber() {
        int testDistance = 1200300001;
        String expectedDistanceString = "1,200,300.001 km";
        String actualDistanceString = DistanceUtils.formatDistance(testDistance);
        assertThat(actualDistanceString).isEqualTo(expectedDistanceString);
    }

    @Test
    public void testDistanceUtilsForMediumNumber() {
        int testDistance = 1200050;
        String expectedDistanceString = "1,200.050 km";
        String actualDistanceString = DistanceUtils.formatDistance(testDistance);
        assertThat(actualDistanceString).isEqualTo(expectedDistanceString);
    }

    @Test
    public void testDistanceUtilsForSmallNumber() {
        int testDistance = 320;
        String expectedDistanceString = "0.320 km";
        String actualDistanceString = DistanceUtils.formatDistance(testDistance);
        assertThat(actualDistanceString).isEqualTo(expectedDistanceString);
    }

    @Test
    public void testDistanceUtilsForZero() {
        int testDistance = 0;
        String expectedDistanceString = "0.000 km";
        String actualDistanceString = DistanceUtils.formatDistance(testDistance);
        assertThat(actualDistanceString).isEqualTo(expectedDistanceString);
    }

}