package com.gachokaerick.eshop.basket.service;

import com.gachokaerick.eshop.basket.domain.BasketCheckout;
import com.gachokaerick.eshop.basket.repository.BasketCheckoutRepository;
import com.gachokaerick.eshop.basket.service.dto.BasketCheckoutDTO;
import com.gachokaerick.eshop.basket.service.mapper.BasketCheckoutMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BasketCheckout}.
 */
@Service
@Transactional
public class BasketCheckoutService {

    private final Logger log = LoggerFactory.getLogger(BasketCheckoutService.class);

    private final BasketCheckoutRepository basketCheckoutRepository;

    private final BasketCheckoutMapper basketCheckoutMapper;

    public BasketCheckoutService(BasketCheckoutRepository basketCheckoutRepository, BasketCheckoutMapper basketCheckoutMapper) {
        this.basketCheckoutRepository = basketCheckoutRepository;
        this.basketCheckoutMapper = basketCheckoutMapper;
    }

    /**
     * Save a basketCheckout.
     *
     * @param basketCheckoutDTO the entity to save.
     * @return the persisted entity.
     */
    public BasketCheckoutDTO save(BasketCheckoutDTO basketCheckoutDTO) {
        log.debug("Request to save BasketCheckout : {}", basketCheckoutDTO);
        BasketCheckout basketCheckout = basketCheckoutMapper.toEntity(basketCheckoutDTO);
        basketCheckout = basketCheckoutRepository.save(basketCheckout);
        return basketCheckoutMapper.toDto(basketCheckout);
    }

    /**
     * Partially update a basketCheckout.
     *
     * @param basketCheckoutDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BasketCheckoutDTO> partialUpdate(BasketCheckoutDTO basketCheckoutDTO) {
        log.debug("Request to partially update BasketCheckout : {}", basketCheckoutDTO);

        return basketCheckoutRepository
            .findById(basketCheckoutDTO.getId())
            .map(existingBasketCheckout -> {
                basketCheckoutMapper.partialUpdate(existingBasketCheckout, basketCheckoutDTO);

                return existingBasketCheckout;
            })
            .map(basketCheckoutRepository::save)
            .map(basketCheckoutMapper::toDto);
    }

    /**
     * Get all the basketCheckouts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BasketCheckoutDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BasketCheckouts");
        return basketCheckoutRepository.findAll(pageable).map(basketCheckoutMapper::toDto);
    }

    /**
     * Get one basketCheckout by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BasketCheckoutDTO> findOne(Long id) {
        log.debug("Request to get BasketCheckout : {}", id);
        return basketCheckoutRepository.findById(id).map(basketCheckoutMapper::toDto);
    }

    /**
     * Delete the basketCheckout by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BasketCheckout : {}", id);
        basketCheckoutRepository.deleteById(id);
    }
}
