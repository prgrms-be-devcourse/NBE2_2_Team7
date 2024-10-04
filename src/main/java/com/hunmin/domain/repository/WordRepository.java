package com.hunmin.domain.repository;

import com.hunmin.domain.entity.Word;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WordRepository extends JpaRepository<Word, Long> {
    Optional<Word> findByTitle(String title);
    Optional<Word> findById(Long id);
    Page<Word> findByLang(String lang, Pageable pageable);

    Optional<Word> findByTitleAndLang(String title, String lang);
    long countByTitleAndLang(String title, String lang);

    List<Word> findByLang(String lang);
}
