package com.betacom.service;

import com.betacom.dto.ItemDTO;
import com.betacom.model.Item;
import com.betacom.model.User;
import com.betacom.repo.ItemRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {
    @Mock
    private ItemRepo itemRepo;
    @Mock
    private UserService userService;
    @InjectMocks
    private ItemService itemService;

    @Test
    void shouldReturnAllItemsForUser() {
        // given
        User user = new User();
        user.setUsername("username");

        Item item1 = new Item(UUID.randomUUID(), user, "item1");
        Item item2 = new Item(UUID.randomUUID(), user, "item2");

        given(userService.findByUsername("username")).willReturn(user);
        given(itemRepo.findAllByOwner(user)).willReturn(List.of(item1, item2));

        // when
        List<ItemDTO> result = itemService.getAllItems("username");

        // then
        assertEquals(2, result.size());
        assertEquals("item1", result.get(0).name());
        assertEquals("item2", result.get(1).name());
    }

    @Test
    void shouldAddItem_whenTitleIsValid() {
        // given
        User user = new User();
        user.setUsername("username");

        given(userService.findByUsername("username")).willReturn(user);

        // when
        itemService.addItem("New Item", "username");

        // then
        ArgumentCaptor<Item> captor = ArgumentCaptor.forClass(Item.class);
        verify(itemRepo).save(captor.capture());

        Item saved = captor.getValue();
        assertEquals("New Item", saved.getName());
        assertEquals(user, saved.getOwner());
    }

    @Test
    void shouldNotAddItem_whenTitleIsNull() {
        // when
        itemService.addItem(null, "username");

        // then
        verify(itemRepo, never()).save(any());
    }

    @Test
    void shouldNotAddItem_whenTitleIsEmpty() {
        // when
        itemService.addItem("", "username");

        // then
        verify(itemRepo, never()).save(any());
    }
}
