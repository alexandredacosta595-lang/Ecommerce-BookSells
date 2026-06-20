package com.mulemba.booksells.controller;

import com.mulemba.booksells.dto.LibraryItemResponse;
import com.mulemba.booksells.security.SecurityUtils;
import com.mulemba.booksells.service.LibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/library")
@RequiredArgsConstructor
public class LibraryController {

    private final LibraryService libraryService;

    @GetMapping
    public List<LibraryItemResponse> getLibrary() {
        return libraryService.getUserLibrary(SecurityUtils.getCurrentUserId()).stream()
                .map(LibraryItemResponse::from).toList();
    }

    @PutMapping("/{bookId}/progress")
    public LibraryItemResponse updateProgress(@PathVariable String bookId, @RequestBody Map<String, Integer> body) {
        return LibraryItemResponse.from(
                libraryService.updateProgress(SecurityUtils.getCurrentUserId(), bookId, body.get("progress"))
        );
    }

    @PutMapping("/{bookId}/download")
    public LibraryItemResponse markDownloaded(@PathVariable String bookId) {
        return LibraryItemResponse.from(
                libraryService.markDownloaded(SecurityUtils.getCurrentUserId(), bookId)
        );
    }
}
