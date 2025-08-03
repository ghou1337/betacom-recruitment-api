package com.betacom.repo;

import com.betacom.model.Item;
import com.betacom.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ItemRepo extends JpaRepository<Item, UUID> {
    List<Item> findAllByOwner(User owner);
}
