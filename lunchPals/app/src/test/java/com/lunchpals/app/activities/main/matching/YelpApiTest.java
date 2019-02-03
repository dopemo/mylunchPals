package com.lunchpals.app.activities.main.matching;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by moritz on 12/14/17.
 */
public class YelpApiTest {
    @Test
    public void hasYelpFusionApi() throws Exception {
        assertFalse(YelpApi.hasYelpFusionApi());
    }

}