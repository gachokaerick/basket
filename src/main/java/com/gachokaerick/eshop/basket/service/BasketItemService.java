package com.gachokaerick.eshop.basket.service;

import com.gachokaerick.eshop.basket.domain.BasketItem;
import com.gachokaerick.eshop.basket.repository.BasketItemRepository;
import com.gachokaerick.eshop.basket.service.dto.BasketItemDTO;
import com.gachokaerick.eshop.basket.service.mapper.BasketItemMapper;
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

    private final BasketItemMapper basketItemMapper;

    public BasketItemService(BasketItemRepository basketItemRepository, BasketItemMapper basketItemMapper) {
        this.basketItemRepository = basketItemRepository;
        this.basketItemMapper = basketItemMapper;
    }

    /**
     * Save a basketItem.
     *
     * @param basketItemDTO the entity to save.
     * @return the persisted entity.
     */
    public BasketItemDTO save(BasketItemDTO basketItemDTO) {
        log.debug("Request to save BasketItem : {}", basketItemDTO);
        BasketItem basketItem = basketItemMapper.toEntity(basketItemDTO);
        basketItem = basketItemRepository.save(basketItem);
        return basketItemMapper.toDto(basketItem);
    }

    /**
     * Partially update a basketItem.
     *
     * @param basketItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BasketItemDTO> partialUpdate(BasketItemDTO basketItemDTO) {
        log.debug("Request to partially update BasketItem : {}", basketItemDTO);

        return basketItemRepository
            .findById(basketItemDTO.getId())
            .map(existingBasketItem -> {
                basketItemMapper.partialUpdate(existingBasketItem, basketItemDTO);

                return existingBasketItem;
            })
            .map(basketItemRepository::save)
            .map(basketItemMapper::toDto);
    }

    /**
     * Get all the basketItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BasketItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BasketItems");
        return basketItemRepository.findAll(pageable).map(basketItemMapper::toDto);
    }

    /**
     * Get one basketItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BasketItemDTO> findOne(Long id) {
        log.debug("Request to get BasketItem : {}", id);
        return basketItemRepository.findById(id).map(basketItemMapper::toDto);
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
