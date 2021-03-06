package com.gachokaerick.eshop.basket.repository;

import com.gachokaerick.eshop.basket.domain.BasketItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BasketItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BasketItemRepository extends JpaRepository<BasketItem, Long> {}
