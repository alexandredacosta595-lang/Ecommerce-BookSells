package com.mulemba.booksells.dto;

import com.mulemba.booksells.model.LibraryItem;

public record LibraryItemResponse(
        String bookId,
        int progress,
        String format,
        String lastRead,
        boolean downloaded
) {
    public static LibraryItemResponse from(LibraryItem item) {
        return new LibraryItemResponse(
                item.getBookId(),
                item.getProgress(),
                item.getFormat().name().toLowerCase(),
                item.getLastRead().toString(),
                item.isDownloaded()
        );
    }
}
