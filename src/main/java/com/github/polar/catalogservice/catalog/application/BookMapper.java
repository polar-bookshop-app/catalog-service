package com.github.polar.catalogservice.catalog.application;

import com.github.polar.catalogservice.catalog.api.BookRequest;
import com.github.polar.catalogservice.catalog.api.BookResponse;
import com.github.polar.catalogservice.catalog.domain.Book;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {

    Book toDomain(BookRequest request);

    BookResponse toResponse(Book book);

    List<BookResponse> toResponseList(List<Book> books);
}
