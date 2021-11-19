package com.gachokaerick.eshop.basket.repository;

import com.gachokaerick.eshop.basket.domain.BasketCheckout;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BasketCheckout entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BasketCheckoutRepository extends JpaRepository<BasketCheckout, Long> {}
