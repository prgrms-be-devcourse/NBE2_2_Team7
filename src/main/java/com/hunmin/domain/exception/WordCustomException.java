package com.hunmin.domain.exception;

import lombok.Getter;

@Getter
public class WordCustomException extends RuntimeException {
  private final WordException wordException;

  public WordCustomException(WordException wordException){
    super(wordException.getMessage());
    this.wordException = wordException;
  }
}
