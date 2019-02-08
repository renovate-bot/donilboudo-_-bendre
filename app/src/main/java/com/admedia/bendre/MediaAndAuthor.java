package com.admedia.bendre;

import com.google.gson.JsonObject;

public class MediaAndAuthor {
  private JsonObject media;
  private JsonObject author;

  public MediaAndAuthor(JsonObject media, JsonObject author) {
    this.media = media;
    this.author = author;
  }
}