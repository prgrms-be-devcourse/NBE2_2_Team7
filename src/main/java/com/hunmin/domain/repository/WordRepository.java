package com.hunmin.domain.repository;

import com.hunmin.domain.entity.Word;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordRepository extends JpaRepository<Word, Long> {
    Optional<Word> findByTitle(String title);
    Optional<Word> findById(Long id);
    List<Word> findByLang(String lang);
}
