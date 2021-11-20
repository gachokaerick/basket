package com.gachokaerick.eshop.basket.web.rest;

import com.gachokaerick.eshop.basket.repository.BasketCheckoutRepository;
import com.gachokaerick.eshop.basket.service.BasketCheckoutService;
import com.gachokaerick.eshop.basket.service.dto.BasketCheckoutDTO;
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
 * REST controller for managing {@link com.gachokaerick.eshop.basket.domain.BasketCheckout}.
 */
@RestController
@RequestMapping("/api")
public class BasketCheckoutResource {

    private final Logger log = LoggerFactory.getLogger(BasketCheckoutResource.class);

    private static final String ENTITY_NAME = "basketBasketCheckout";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BasketCheckoutService basketCheckoutService;

    private final BasketCheckoutRepository basketCheckoutRepository;

    public BasketCheckoutResource(BasketCheckoutService basketCheckoutService, BasketCheckoutRepository basketCheckoutRepository) {
        this.basketCheckoutService = basketCheckoutService;
        this.basketCheckoutRepository = basketCheckoutRepository;
    }

    /**
     * {@code POST  /basket-checkouts} : Create a new basketCheckout.
     *
     * @param basketCheckoutDTO the basketCheckoutDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new basketCheckoutDTO, or with status {@code 400 (Bad Request)} if the basketCheckout has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/basket-checkouts")
    public ResponseEntity<BasketCheckoutDTO> createBasketCheckout(@Valid @RequestBody BasketCheckoutDTO basketCheckoutDTO)
        throws URISyntaxException {
        log.debug("REST request to save BasketCheckout : {}", basketCheckoutDTO);
        if (basketCheckoutDTO.getId() != null) {
            throw new BadRequestAlertException("A new basketCheckout cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BasketCheckoutDTO result = basketCheckoutService.save(basketCheckoutDTO);
        return ResponseEntity
            .created(new URI("/api/basket-checkouts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /basket-checkouts/:id} : Updates an existing basketCheckout.
     *
     * @param id the id of the basketCheckoutDTO to save.
     * @param basketCheckoutDTO the basketCheckoutDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated basketCheckoutDTO,
     * or with status {@code 400 (Bad Request)} if the basketCheckoutDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the basketCheckoutDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/basket-checkouts/{id}")
    public ResponseEntity<BasketCheckoutDTO> updateBasketCheckout(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BasketCheckoutDTO basketCheckoutDTO
    ) throws URISyntaxException {
        log.debug("REST request to update BasketCheckout : {}, {}", id, basketCheckoutDTO);
        if (basketCheckoutDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, basketCheckoutDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!basketCheckoutRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BasketCheckoutDTO result = basketCheckoutService.save(basketCheckoutDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, basketCheckoutDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /basket-checkouts/:id} : Partial updates given fields of an existing basketCheckout, field will ignore if it is null
     *
     * @param id the id of the basketCheckoutDTO to save.
     * @param basketCheckoutDTO the basketCheckoutDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated basketCheckoutDTO,
     * or with status {@code 400 (Bad Request)} if the basketCheckoutDTO is not valid,
     * or with status {@code 404 (Not Found)} if the basketCheckoutDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the basketCheckoutDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/basket-checkouts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BasketCheckoutDTO> partialUpdateBasketCheckout(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BasketCheckoutDTO basketCheckoutDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update BasketCheckout partially : {}, {}", id, basketCheckoutDTO);
        if (basketCheckoutDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, basketCheckoutDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!basketCheckoutRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BasketCheckoutDTO> result = basketCheckoutService.partialUpdate(basketCheckoutDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, basketCheckoutDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /basket-checkouts} : get all the basketCheckouts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of basketCheckouts in body.
     */
    @GetMapping("/basket-checkouts")
    public ResponseEntity<List<BasketCheckoutDTO>> getAllBasketCheckouts(Pageable pageable) {
        log.debug("REST request to get a page of BasketCheckouts");
        Page<BasketCheckoutDTO> page = basketCheckoutService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /basket-checkouts/:id} : get the "id" basketCheckout.
     *
     * @param id the id of the basketCheckoutDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the basketCheckoutDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/basket-checkouts/{id}")
    public ResponseEntity<BasketCheckoutDTO> getBasketCheckout(@PathVariable Long id) {
        log.debug("REST request to get BasketCheckout : {}", id);
        Optional<BasketCheckoutDTO> basketCheckoutDTO = basketCheckoutService.findOne(id);
        return ResponseUtil.wrapOrNotFound(basketCheckoutDTO);
    }

    /**
     * {@code DELETE  /basket-checkouts/:id} : delete the "id" basketCheckout.
     *
     * @param id the id of the basketCheckoutDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/basket-checkouts/{id}")
    public ResponseEntity<Void> deleteBasketCheckout(@PathVariable Long id) {
        log.debug("REST request to delete BasketCheckout : {}", id);
        basketCheckoutService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
