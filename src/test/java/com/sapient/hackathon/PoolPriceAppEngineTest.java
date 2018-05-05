package com.sapient.hackathon;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.sapient.hackathon.engine.PoolPriceAppEngine;

public class PoolPriceAppEngineTest {

//  @Test
  public void testGet() throws IOException {
    MockHttpServletResponse response = new MockHttpServletResponse();
    new PoolPriceAppEngine().doGet(null, response);
    Assert.assertEquals("text/plain", response.getContentType());
    Assert.assertEquals("UTF-8", response.getCharacterEncoding());
    Assert.assertEquals("Hello App Engine!\r\n", response.getWriterContent().toString());
  }
  
@Test
public void testPost() throws IOException {
  MockHttpServletResponse response = new MockHttpServletResponse();
  new PoolPriceAppEngine().doPost(null, response);
  Assert.assertEquals("application/json", response.getContentType());
//  Assert.assertEquals("UTF-8", response.getCharacterEncoding());
//  Assert.assertEquals("Hello App Engine!\r\n", response.getWriterContent().toString());
}
}
