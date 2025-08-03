package com.betacom.service;

import com.betacom.dto.ItemDTO;
import com.betacom.model.Item;
import com.betacom.model.User;
import com.betacom.repo.ItemRepo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ItemService {
    private final ItemRepo itemRepo;
    private final UserService userService;

    public ItemService(ItemRepo itemRepo, UserService userService) {
        this.itemRepo = itemRepo;
        this.userService = userService;
    }


    @Transactional(readOnly = true)
    public List<ItemDTO> getAllItems(String username) {
        User user = userService.findByUsername(username);
        return itemRepo.findAllByOwner(user).stream()
                .map(item -> new ItemDTO(item.getId(), item.getName()))
                .toList();
    }

    @Transactional
    public void addItem(String title, String username) {
        User user = userService.findByUsername(username);
        if (title != null && !title.isEmpty()) {
            Item item = new Item();
            item.setName(title);
            item.setOwner(user);
            itemRepo.save(item);
        }
    }
}
