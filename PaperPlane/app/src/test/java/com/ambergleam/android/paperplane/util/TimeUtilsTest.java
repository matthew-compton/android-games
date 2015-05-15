package com.ambergleam.android.paperplane.util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeUtilsTest {

    @Test
    public void testTimeUtilsForHoursMinutesSeconds() {
        // 9 hours, 59 minutes, 59 seconds
        int testTimeMilliseconds = (10 * TimeUtils.MINUTES_PER_HOUR * TimeUtils.SECONDS_PER_MINUTE * TimeUtils.MILLISECONDS_PER_SECOND) - TimeUtils.MILLISECONDS_PER_SECOND;
        String expectedTimeString = "9:59:59.000";
        String actualTimeString = TimeUtils.formatTime(testTimeMilliseconds);
        assertThat(actualTimeString).isEqualTo(expectedTimeString);
    }

    @Test
    public void testTimeUtilsForMinutesSeconds() {
        // 35 minutes, 15 seconds
        int testTimeMilliseconds = (35 * TimeUtils.SECONDS_PER_MINUTE * TimeUtils.MILLISECONDS_PER_SECOND) + (15 * TimeUtils.MILLISECONDS_PER_SECOND);
        String expectedTimeString = "35:15.000";
        String actualTimeString = TimeUtils.formatTime(testTimeMilliseconds);
        assertThat(actualTimeString).isEqualTo(expectedTimeString);
    }

    @Test
    public void testTimeUtilsForSeconds() {
        // 22 seconds
        int testTimeMilliseconds = 22 * TimeUtils.MILLISECONDS_PER_SECOND;
        String expectedTimeString = "22.000 s";
        String actualTimeString = TimeUtils.formatTime(testTimeMilliseconds);
        assertThat(actualTimeString).isEqualTo(expectedTimeString);
    }

    @Test
    public void testTimeUtilsForMilliseconds() {
        // 220 milliseconds
        int testTimeMilliseconds = 220;
        String expectedTimeString = "0.220 s";
        String actualTimeString = TimeUtils.formatTime(testTimeMilliseconds);
        assertThat(actualTimeString).isEqualTo(expectedTimeString);
    }

    @Test
    public void testTimeUtilsForZero() {
        // 0 seconds
        int timeMilliseconds = 0;
        String expectedTimeString = "0.000 s";
        String actualTimeString = TimeUtils.formatTime(timeMilliseconds);
        assertThat(actualTimeString).isEqualTo(expectedTimeString);
    }

}