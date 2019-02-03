package com.lunchpals.app.utils;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by moritz on 12/14/17.
 */
public class ImageUploadTest {

    String url = "www.example.com";
    String name = "name";
    ImageUpload imageUpload;

    @Before
    public void setUp(){
        imageUpload = new ImageUpload(name, url);
    }

    @Test
    public void getName() throws Exception {
        assertEquals(name, imageUpload.getName());
    }

    @Test
    public void getUrl() throws Exception {
        assertEquals(url, imageUpload.getUrl());
    }

}