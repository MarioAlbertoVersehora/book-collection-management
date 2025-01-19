package com.example.withoutdesignpatterns.controller;

import com.example.withoutdesignpatterns.model.Book;
import com.example.withoutdesignpatterns.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping
    public Book addBook(@RequestBody Book book) {
        if (book.getTitle() == null || book.getAuthor() == null) {
            throw new RuntimeException("Missing required fields");
        }
        return bookService.addBook(book);
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id) {
        Optional<Book> book = bookService.getBookById(id);
        if (!book.isPresent()) {
            throw new RuntimeException("Book not found");
        }
        return book.get();
    }

    @PutMapping("/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody Book bookDetails) {
        return bookService.updateBook(id, bookDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }

    @GetMapping("/sort/title/bubble-sort")
    public List<Book> sortByTitleWithBubbleSort() {
        List<Book> books = bookService.getAllBooks();
        bubbleSortByTitle(books);
        return books;
    }

    @GetMapping("/sort/pages/bubble-sort")
    public List<Book> sortByPagesWithBubbleSort() {
        List<Book> books = bookService.getAllBooks();
        bubbleSortByPages(books);
        return books;
    }

    private void bubbleSortByTitle(List<Book> books) {
        int n = books.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (books.get(j).getTitle().compareTo(books.get(j + 1).getTitle()) > 0) {
                    // Swap books[j] and books[j+1]
                    Book temp = books.get(j);
                    books.set(j, books.get(j + 1));
                    books.set(j + 1, temp);
                }
            }
        }
    }

    private void bubbleSortByPages(List<Book> books) {
        int n = books.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (books.get(j).getPages() > books.get(j + 1).getPages()) {
                    // Swap books[j] and books[j+1]
                    Book temp = books.get(j);
                    books.set(j, books.get(j + 1));
                    books.set(j + 1, temp);
                }
            }
        }
    }

    @GetMapping("/sort/title")
    public List<Book> sortByTitle() {
        List<Book> books = bookService.getAllBooks();
        return mergeSortByTitle(books);
    }

    @GetMapping("/sort/pages")
    public List<Book> sortByPages() {
        List<Book> books = bookService.getAllBooks();
        return mergeSortByPages(books);
    }

    private List<Book> mergeSortByTitle(List<Book> books) {
        if (books.size() <= 1) {
            return books;
        }

        int mid = books.size() / 2;
        List<Book> left = mergeSortByTitle(books.subList(0, mid));
        List<Book> right = mergeSortByTitle(books.subList(mid, books.size()));

        return mergeByTitle(left, right);
    }

    private List<Book> mergeByTitle(List<Book> left, List<Book> right) {
        List<Book> result = new ArrayList<>();
        int i = 0, j = 0;

        while (i < left.size() && j < right.size()) {
            if (left.get(i).getTitle().compareTo(right.get(j).getTitle()) <= 0) {
                result.add(left.get(i));
                i++;
            } else {
                result.add(right.get(j));
                j++;
            }
        }

        while (i < left.size()) {
            result.add(left.get(i));
            i++;
        }

        while (j < right.size()) {
            result.add(right.get(j));
            j++;
        }

        return result;
    }

    private List<Book> mergeSortByPages(List<Book> books) {
        if (books.size() <= 1) {
            return books;
        }

        int mid = books.size() / 2;
        List<Book> left = mergeSortByPages(books.subList(0, mid));
        List<Book> right = mergeSortByPages(books.subList(mid, books.size()));

        return mergeByPages(left, right);
    }

    private List<Book> mergeByPages(List<Book> left, List<Book> right) {
        List<Book> result = new ArrayList<>();
        int i = 0, j = 0;

        while (i < left.size() && j < right.size()) {
            if (left.get(i).getPages() <= right.get(j).getPages()) {
                result.add(left.get(i));
                i++;
            } else {
                result.add(right.get(j));
                j++;
            }
        }

        while (i < left.size()) {
            result.add(left.get(i));
            i++;
        }

        while (j < right.size()) {
            result.add(right.get(j));
            j++;
        }

        return result;
    }

}