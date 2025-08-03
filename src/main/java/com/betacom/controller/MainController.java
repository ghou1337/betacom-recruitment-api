package com.betacom.controller;

import com.betacom.dto.ItemDTO;
import com.betacom.dto.PostItem;
import com.betacom.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class MainController {
    private final ItemService itemService;

    public MainController(ItemService itemService) {
        this.itemService = itemService;
    }


    @GetMapping("/items")
    public ResponseEntity<Object> getAllItems(Principal principal) {
        List<ItemDTO> items = itemService.getAllItems(principal.getName());
        return ResponseEntity.status(HttpStatus.OK).body(items);
    }

    @PostMapping("/items")
    public ResponseEntity<String> createItem(@Valid @RequestBody PostItem item, Principal principal) {
        itemService.addItem(item.title(), principal.getName());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Item created successfully");
    }
}
