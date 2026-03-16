package com.github.polar.catalogservice.catalog.application;

import com.github.polar.catalogservice.catalog.api.BookRequest;
import com.github.polar.catalogservice.catalog.api.BookResponse;
import com.github.polar.catalogservice.catalog.domain.Book;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BookMapper {

    Book toDomain(BookRequest request);

    BookResponse toResponse(Book book);

    List<BookResponse> toResponseList(List<Book> books);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isbn", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateBook(BookRequest request, @MappingTarget Book book);
}
