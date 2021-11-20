package com.gachokaerick.eshop.basket.web.rest;

import com.gachokaerick.eshop.basket.repository.BasketItemRepository;
import com.gachokaerick.eshop.basket.service.BasketItemService;
import com.gachokaerick.eshop.basket.service.dto.BasketItemDTO;
import com.gachokaerick.eshop.basket.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.gachokaerick.eshop.basket.domain.BasketItem}.
 */
@RestController
@RequestMapping("/api")
public class BasketItemResource {

    private final Logger log = LoggerFactory.getLogger(BasketItemResource.class);

    private static final String ENTITY_NAME = "basketBasketItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BasketItemService basketItemService;

    private final BasketItemRepository basketItemRepository;

    public BasketItemResource(BasketItemService basketItemService, BasketItemRepository basketItemRepository) {
        this.basketItemService = basketItemService;
        this.basketItemRepository = basketItemRepository;
    }

    /**
     * {@code POST  /basket-items} : Create a new basketItem.
     *
     * @param basketItemDTO the basketItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new basketItemDTO, or with status {@code 400 (Bad Request)} if the basketItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/basket-items")
    public ResponseEntity<BasketItemDTO> createBasketItem(@Valid @RequestBody BasketItemDTO basketItemDTO) throws URISyntaxException {
        log.debug("REST request to save BasketItem : {}", basketItemDTO);
        if (basketItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new basketItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BasketItemDTO result = basketItemService.save(basketItemDTO);
        return ResponseEntity
            .created(new URI("/api/basket-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /basket-items/:id} : Updates an existing basketItem.
     *
     * @param id the id of the basketItemDTO to save.
     * @param basketItemDTO the basketItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated basketItemDTO,
     * or with status {@code 400 (Bad Request)} if the basketItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the basketItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/basket-items/{id}")
    public ResponseEntity<BasketItemDTO> updateBasketItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BasketItemDTO basketItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to update BasketItem : {}, {}", id, basketItemDTO);
        if (basketItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, basketItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!basketItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BasketItemDTO result = basketItemService.save(basketItemDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, basketItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /basket-items/:id} : Partial updates given fields of an existing basketItem, field will ignore if it is null
     *
     * @param id the id of the basketItemDTO to save.
     * @param basketItemDTO the basketItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated basketItemDTO,
     * or with status {@code 400 (Bad Request)} if the basketItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the basketItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the basketItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/basket-items/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BasketItemDTO> partialUpdateBasketItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BasketItemDTO basketItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update BasketItem partially : {}, {}", id, basketItemDTO);
        if (basketItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, basketItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!basketItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BasketItemDTO> result = basketItemService.partialUpdate(basketItemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, basketItemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /basket-items} : get all the basketItems.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of basketItems in body.
     */
    @GetMapping("/basket-items")
    public ResponseEntity<List<BasketItemDTO>> getAllBasketItems(Pageable pageable) {
        log.debug("REST request to get a page of BasketItems");
        Page<BasketItemDTO> page = basketItemService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /basket-items/:id} : get the "id" basketItem.
     *
     * @param id the id of the basketItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the basketItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/basket-items/{id}")
    public ResponseEntity<BasketItemDTO> getBasketItem(@PathVariable Long id) {
        log.debug("REST request to get BasketItem : {}", id);
        Optional<BasketItemDTO> basketItemDTO = basketItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(basketItemDTO);
    }

    /**
     * {@code DELETE  /basket-items/:id} : delete the "id" basketItem.
     *
     * @param id the id of the basketItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/basket-items/{id}")
    public ResponseEntity<Void> deleteBasketItem(@PathVariable Long id) {
        log.debug("REST request to delete BasketItem : {}", id);
        basketItemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
