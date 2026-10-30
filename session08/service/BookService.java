package com.example.session08.service;

import com.example.session08.dto.BookCreateDTO;
import com.example.session08.entity.Book;
import com.example.session08.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book createBook(BookCreateDTO dto) throws IOException {

        MultipartFile file = dto.getCoverImage();

        // Kiểm tra file
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Vui lòng chọn ảnh bìa sách!");
        }

        // Tạo đường dẫn tuyệt đối đến thư mục uploads
        String uploadDir = System.getProperty("user.dir") + "/uploads/";

        File directory = new File(uploadDir);

        // Tạo thư mục nếu chưa tồn tại
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Tạo tên file duy nhất
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        // Đường dẫn lưu file
        File destination = new File(directory, fileName);

        // Lưu ảnh
        file.transferTo(destination);

        // Tạo đối tượng Book
        Book book = new Book();

        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setStock(dto.getStock());
        book.setCoverUrl("uploads/" + fileName);

        // Lưu Database
        return bookRepository.save(book);
    }
}