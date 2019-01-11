package com.gmail.htaihm.roomwordsample;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "word_table")
public class Word {

  // Tip: You can autogenerate unique keys by annotating the primary key as follows:
  //  @PrimaryKey(autoGenerate = true)
  //  private int id;

  @PrimaryKey
  @NonNull
  @ColumnInfo(name = "word")
  private String mWord;

  public Word(String word) {this.mWord = word;}

  public String getWord(){return this.mWord;}
}
