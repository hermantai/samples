package com.longgamemindset.simpleuserserver;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

public class FileHandler implements Handler {
  public void handle(Request request, Response response) throws IOException {
    try {
      FileInputStream file = new FileInputStream(request.getPath().substring(1));
      response.setResponseCode(200, "OK");
      response.addHeader("Content-Type", "text/html");
      StringBuffer buf = new StringBuffer();
      // TODO this is slow
      int c;
      while ((c = file.read()) != -1) {
        buf.append((char) c);
      }
      response.addBody(buf.toString());
    } catch (FileNotFoundException e) {
      response.setResponseCode(404, "Not Found");
    }
  }
}

