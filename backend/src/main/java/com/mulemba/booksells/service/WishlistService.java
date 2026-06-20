package com.mulemba.booksells.service;

import com.mulemba.booksells.repository.WishlistItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistItemRepository wishlistItemRepository;

    public List<String> getWishlistBookIds(String userId) {
        return wishlistItemRepository.findByUserId(userId).stream()
                .map(item -> item.getBookId()).toList();
    }

    @Transactional
    public List<String> toggle(String userId, String bookId) {
        var existing = wishlistItemRepository.findByUserIdAndBookId(userId, bookId);
        if (existing.isPresent()) {
            wishlistItemRepository.delete(existing.get());
        } else {
            wishlistItemRepository.save(com.mulemba.booksells.model.WishlistItem.builder()
                    .userId(userId)
                    .bookId(bookId)
                    .build());
        }
        return getWishlistBookIds(userId);
    }

    public boolean isInWishlist(String userId, String bookId) {
        return wishlistItemRepository.existsByUserIdAndBookId(userId, bookId);
    }
}
