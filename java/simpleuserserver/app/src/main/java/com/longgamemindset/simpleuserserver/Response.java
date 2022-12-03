package com.longgamemindset.simpleuserserver;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.HashMap;

/** 
 * Encapsulate an HTTP Response.  Mostly just wrap an output stream and
 * provide some state.
 */
public class Response  {
  private OutputStream out;
  private int statusCode;
  private String statusMessage;
  private Map<String, String> headers = new HashMap<String, String>();
  private String body;

  public Response(OutputStream out)  {
    this.out = out;
  }

  public void setResponseCode(int statusCode, String statusMessage)  {
    this.statusCode = statusCode;
    this.statusMessage = statusMessage;
  }

  public void addHeader(String headerName, String headerValue)  {
    this.headers.put(headerName, headerValue);
  }

  public void addBody(String body)  {
    headers.put("Content-Length", Integer.toString(body.length()));
    this.body = body;
  }

  public void send() throws IOException  {
    headers.put("Connection", "Close");
    out.write(("HTTP/1.1 " + statusCode + " " + statusMessage + "\r\n").getBytes());
    for (String headerName : headers.keySet())  {
      out.write((headerName + ": " + headers.get(headerName) + "\r\n").getBytes());
    }
    out.write("\r\n".getBytes());
    if (body != null)  {
      out.write(body.getBytes());
    }
  }
}
