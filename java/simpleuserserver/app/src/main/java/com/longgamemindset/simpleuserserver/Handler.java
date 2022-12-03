package com.longgamemindset.simpleuserserver;

import java.io.IOException;
import java.util.Map;
import java.io.BufferedReader;
import java.io.OutputStream;

/**
 * Handlers must be thread safe.
 */
public interface Handler  {
  public void handle(Request request, Response response) throws IOException;
}

