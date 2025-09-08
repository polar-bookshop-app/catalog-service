package com.github.polar.catalogservice.domain;

import java.util.Optional;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface BookRepository extends CrudRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);

    @Transactional(propagation = Propagation.MANDATORY)
    @Modifying
    @Query("delete from Book where isbn = :isbn")
    boolean deleteByIsbn(String isbn);
}
