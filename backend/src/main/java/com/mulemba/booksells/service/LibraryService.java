package com.mulemba.booksells.service;

import com.mulemba.booksells.model.LibraryItem;
import com.mulemba.booksells.model.enums.BookFormat;
import com.mulemba.booksells.repository.LibraryItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LibraryService {

    private final LibraryItemRepository libraryItemRepository;

    public List<LibraryItem> getUserLibrary(String userId) {
        return libraryItemRepository.findByUserId(userId);
    }

    @Transactional
    public void addToLibrary(String userId, String bookId, BookFormat format) {
        if (format == BookFormat.PHYSICAL) return;

        libraryItemRepository.findByUserIdAndBookId(userId, bookId).orElseGet(() -> {
            LibraryItem item = LibraryItem.builder()
                    .userId(userId)
                    .bookId(bookId)
                    .progress(0)
                    .format(format)
                    .lastRead(LocalDate.now())
                    .downloaded(false)
                    .build();
            return libraryItemRepository.save(item);
        });
    }

    @Transactional
    public LibraryItem updateProgress(String userId, String bookId, int progress) {
        LibraryItem item = libraryItemRepository.findByUserIdAndBookId(userId, bookId)
                .orElseThrow(() -> new com.mulemba.booksells.exception.ResourceNotFoundException("Item não encontrado na biblioteca"));
        item.setProgress(Math.min(100, Math.max(0, progress)));
        item.setLastRead(LocalDate.now());
        return libraryItemRepository.save(item);
    }

    @Transactional
    public LibraryItem markDownloaded(String userId, String bookId) {
        LibraryItem item = libraryItemRepository.findByUserIdAndBookId(userId, bookId)
                .orElseThrow(() -> new com.mulemba.booksells.exception.ResourceNotFoundException("Item não encontrado na biblioteca"));
        item.setDownloaded(true);
        return libraryItemRepository.save(item);
    }
}
