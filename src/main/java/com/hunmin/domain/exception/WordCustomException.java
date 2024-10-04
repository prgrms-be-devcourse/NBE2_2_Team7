package com.hunmin.domain.exception;

import lombok.Getter;

@Getter
public class WordCustomException extends RuntimeException {
  private final WordException wordException;

  public WordCustomException(WordException wordException){
    super(wordException.getMessage());
    this.wordException = wordException;
  }

  public WordCustomException(String message) {
    super(message);
    this.wordException = null; // 필요 시 null로 초기화
  }
}
