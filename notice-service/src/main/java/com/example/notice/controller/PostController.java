package com.example.notice.controller;

import com.example.notice.entity.Post;
import com.example.notice.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping
    public ResponseEntity<Page<Post>> findAll(@RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(postService.findAll(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> findById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Post> save(@RequestBody Post post) {
        return ResponseEntity.ok(postService.save(post));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> update(@PathVariable Long id, @RequestBody Post post) {
        return ResponseEntity.ok(postService.update(id, post));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.ok().build();
    }
}