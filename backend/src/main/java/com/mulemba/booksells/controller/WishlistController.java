package com.mulemba.booksells.controller;

import com.mulemba.booksells.security.SecurityUtils;
import com.mulemba.booksells.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @GetMapping
    public List<String> getWishlist() {
        return wishlistService.getWishlistBookIds(SecurityUtils.getCurrentUserId());
    }

    @PostMapping("/{bookId}/toggle")
    public List<String> toggle(@PathVariable String bookId) {
        return wishlistService.toggle(SecurityUtils.getCurrentUserId(), bookId);
    }
}
