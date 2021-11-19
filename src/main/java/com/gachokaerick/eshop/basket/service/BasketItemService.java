package com.gachokaerick.eshop.basket.service;

import com.gachokaerick.eshop.basket.domain.BasketItem;
import com.gachokaerick.eshop.basket.repository.BasketItemRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BasketItem}.
 */
@Service
@Transactional
public class BasketItemService {

    private final Logger log = LoggerFactory.getLogger(BasketItemService.class);

    private final BasketItemRepository basketItemRepository;

    public BasketItemService(BasketItemRepository basketItemRepository) {
        this.basketItemRepository = basketItemRepository;
    }

    /**
     * Save a basketItem.
     *
     * @param basketItem the entity to save.
     * @return the persisted entity.
     */
    public BasketItem save(BasketItem basketItem) {
        log.debug("Request to save BasketItem : {}", basketItem);
        return basketItemRepository.save(basketItem);
    }

    /**
     * Partially update a basketItem.
     *
     * @param basketItem the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BasketItem> partialUpdate(BasketItem basketItem) {
        log.debug("Request to partially update BasketItem : {}", basketItem);

        return basketItemRepository
            .findById(basketItem.getId())
            .map(existingBasketItem -> {
                if (basketItem.getProductId() != null) {
                    existingBasketItem.setProductId(basketItem.getProductId());
                }
                if (basketItem.getProductName() != null) {
                    existingBasketItem.setProductName(basketItem.getProductName());
                }
                if (basketItem.getUnitPrice() != null) {
                    existingBasketItem.setUnitPrice(basketItem.getUnitPrice());
                }
                if (basketItem.getOldUnitPrice() != null) {
                    existingBasketItem.setOldUnitPrice(basketItem.getOldUnitPrice());
                }
                if (basketItem.getQuantity() != null) {
                    existingBasketItem.setQuantity(basketItem.getQuantity());
                }
                if (basketItem.getPictureUrl() != null) {
                    existingBasketItem.setPictureUrl(basketItem.getPictureUrl());
                }
                if (basketItem.getUserLogin() != null) {
                    existingBasketItem.setUserLogin(basketItem.getUserLogin());
                }

                return existingBasketItem;
            })
            .map(basketItemRepository::save);
    }

    /**
     * Get all the basketItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BasketItem> findAll(Pageable pageable) {
        log.debug("Request to get all BasketItems");
        return basketItemRepository.findAll(pageable);
    }

    /**
     * Get one basketItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BasketItem> findOne(Long id) {
        log.debug("Request to get BasketItem : {}", id);
        return basketItemRepository.findById(id);
    }

    /**
     * Delete the basketItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BasketItem : {}", id);
        basketItemRepository.deleteById(id);
    }
}
