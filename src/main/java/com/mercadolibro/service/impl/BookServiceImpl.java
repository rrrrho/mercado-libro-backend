package com.mercadolibro.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibro.dto.BookReqDTO;
import com.mercadolibro.dto.BookRespDTO;
import com.mercadolibro.dto.PageDTO;
import com.mercadolibro.entity.Book;
import com.mercadolibro.exception.*;
import com.mercadolibro.repository.BookRepository;
import com.mercadolibro.repository.CategoryRepository;
import com.mercadolibro.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import java.util.List;

import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final ObjectMapper mapper;
    private final ModelMapper modelMapper;

    public static final String NOT_FOUND_ERROR_FORMAT = "Could not found %s with ID #%d.";
    public static final String BOOK_ISBN_ALREADY_EXISTS_ERROR_FORMAT = "Book with ISBN #%s already exists.";

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, CategoryRepository categoryRepository,
                           ObjectMapper mapper, ModelMapper modelMapper) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
        this.modelMapper = modelMapper;
    }

    @Override
    public PageDTO<BookRespDTO> findAll(short page) {
        Pageable pageable = PageRequest.of(page - 1, 9);
        Page<Book> res = bookRepository.findAll(pageable);

        List<BookRespDTO> content = res.getContent().stream().map(book -> mapper.convertValue(book, BookRespDTO.class))
                .collect(Collectors.toList());

        if (content.isEmpty()) {
            throw new NoBooksToShowException();
        }

        return new PageDTO<BookRespDTO>(
                content,
                res.getTotalPages(),
                res.getTotalElements(),
                res.getNumber() + 1,
                res.getSize()
        );
    }

    @Override
    public BookRespDTO save(BookReqDTO book) {
        book.getCategories().forEach(category -> {
            Long id = category.getId();
            if (!categoryRepository.existsById(id)) {
                throw new CategoryNotFoundException(String.format(NOT_FOUND_ERROR_FORMAT, "category", id));
            }
        });

        if (!bookRepository.existsByIsbn(book.getIsbn())) {
            Book saved = bookRepository.save(mapper.convertValue(book, Book.class));
            return mapper.convertValue(saved, BookRespDTO.class);
        }
        throw new BookAlreadyExistsException(String.format(BOOK_ISBN_ALREADY_EXISTS_ERROR_FORMAT, book.getIsbn()));
    }

    @Override
    public BookRespDTO update(Long id, BookReqDTO bookReqDTO){
        if (bookRepository.existsById(id)) {
            if (bookRepository.existsByIsbnAndIdNot(bookReqDTO.getIsbn(), id)) {
                throw new BookAlreadyExistsException(String.format(BOOK_ISBN_ALREADY_EXISTS_ERROR_FORMAT,
                        bookReqDTO.getIsbn()));
            }

            Book book = mapper.convertValue(bookReqDTO, Book.class);
            book.setId(id);
            Book updated = bookRepository.save(book);
            return mapper.convertValue(updated, BookRespDTO.class);
        }

        throw new BookNotFoundException(String.format(NOT_FOUND_ERROR_FORMAT, "book", id));
    }

    @Override
    public BookRespDTO patch(Long id, BookRespDTO bookRespDTO) {
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());

        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            if (bookRepository.existsByIsbnAndIdNot(bookRespDTO.getIsbn(), id)) {
                throw new BookAlreadyExistsException(String.format(BOOK_ISBN_ALREADY_EXISTS_ERROR_FORMAT, bookRespDTO.getIsbn()));
            }

            bookRespDTO.setId(id);
            modelMapper.map(bookRespDTO, optionalBook.get());
            Book book = bookRepository.save(optionalBook.get());
            return mapper.convertValue(book, BookRespDTO.class);
        }

        throw new BookNotFoundException(String.format(NOT_FOUND_ERROR_FORMAT, "book", id));
    }

    @Override
    public BookRespDTO findByID(Long id) {
        Optional<Book> searched = bookRepository.findById(id);
        if (searched.isPresent()) {
            return mapper.convertValue(searched.get(), BookRespDTO.class);
        }
        throw new BookNotFoundException(String.format(NOT_FOUND_ERROR_FORMAT, "book", id));
    }

    @Override
    public void delete(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
        } else {
            throw new BookNotFoundException(String.format(NOT_FOUND_ERROR_FORMAT, "book", id));
        }
    }
}
