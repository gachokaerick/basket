package com.gachokaerick.eshop.basket.web.rest;

import static com.gachokaerick.eshop.basket.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gachokaerick.eshop.basket.IntegrationTest;
import com.gachokaerick.eshop.basket.domain.BasketItem;
import com.gachokaerick.eshop.basket.repository.BasketItemRepository;
import com.gachokaerick.eshop.basket.service.dto.BasketItemDTO;
import com.gachokaerick.eshop.basket.service.mapper.BasketItemMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BasketItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BasketItemResourceIT {

    private static final Long DEFAULT_PRODUCT_ID = 1L;
    private static final Long UPDATED_PRODUCT_ID = 2L;

    private static final String DEFAULT_PRODUCT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_UNIT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_UNIT_PRICE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_OLD_UNIT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_OLD_UNIT_PRICE = new BigDecimal(2);

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final String DEFAULT_PICTURE_URL = "AAAAAAAAAA";
    private static final String UPDATED_PICTURE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_USER_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_USER_LOGIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/basket-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BasketItemRepository basketItemRepository;

    @Autowired
    private BasketItemMapper basketItemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBasketItemMockMvc;

    private BasketItem basketItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BasketItem createEntity(EntityManager em) {
        BasketItem basketItem = new BasketItem()
            .productId(DEFAULT_PRODUCT_ID)
            .productName(DEFAULT_PRODUCT_NAME)
            .unitPrice(DEFAULT_UNIT_PRICE)
            .oldUnitPrice(DEFAULT_OLD_UNIT_PRICE)
            .quantity(DEFAULT_QUANTITY)
            .pictureUrl(DEFAULT_PICTURE_URL)
            .userLogin(DEFAULT_USER_LOGIN);
        return basketItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BasketItem createUpdatedEntity(EntityManager em) {
        BasketItem basketItem = new BasketItem()
            .productId(UPDATED_PRODUCT_ID)
            .productName(UPDATED_PRODUCT_NAME)
            .unitPrice(UPDATED_UNIT_PRICE)
            .oldUnitPrice(UPDATED_OLD_UNIT_PRICE)
            .quantity(UPDATED_QUANTITY)
            .pictureUrl(UPDATED_PICTURE_URL)
            .userLogin(UPDATED_USER_LOGIN);
        return basketItem;
    }

    @BeforeEach
    public void initTest() {
        basketItem = createEntity(em);
    }

    @Test
    @Transactional
    void createBasketItem() throws Exception {
        int databaseSizeBeforeCreate = basketItemRepository.findAll().size();
        // Create the BasketItem
        BasketItemDTO basketItemDTO = basketItemMapper.toDto(basketItem);
        restBasketItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(basketItemDTO))
            )
            .andExpect(status().isCreated());

        // Validate the BasketItem in the database
        List<BasketItem> basketItemList = basketItemRepository.findAll();
        assertThat(basketItemList).hasSize(databaseSizeBeforeCreate + 1);
        BasketItem testBasketItem = basketItemList.get(basketItemList.size() - 1);
        assertThat(testBasketItem.getProductId()).isEqualTo(DEFAULT_PRODUCT_ID);
        assertThat(testBasketItem.getProductName()).isEqualTo(DEFAULT_PRODUCT_NAME);
        assertThat(testBasketItem.getUnitPrice()).isEqualByComparingTo(DEFAULT_UNIT_PRICE);
        assertThat(testBasketItem.getOldUnitPrice()).isEqualByComparingTo(DEFAULT_OLD_UNIT_PRICE);
        assertThat(testBasketItem.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testBasketItem.getPictureUrl()).isEqualTo(DEFAULT_PICTURE_URL);
        assertThat(testBasketItem.getUserLogin()).isEqualTo(DEFAULT_USER_LOGIN);
    }

    @Test
    @Transactional
    void createBasketItemWithExistingId() throws Exception {
        // Create the BasketItem with an existing ID
        basketItem.setId(1L);
        BasketItemDTO basketItemDTO = basketItemMapper.toDto(basketItem);

        int databaseSizeBeforeCreate = basketItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBasketItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(basketItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BasketItem in the database
        List<BasketItem> basketItemList = basketItemRepository.findAll();
        assertThat(basketItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkProductIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = basketItemRepository.findAll().size();
        // set the field null
        basketItem.setProductId(null);

        // Create the BasketItem, which fails.
        BasketItemDTO basketItemDTO = basketItemMapper.toDto(basketItem);

        restBasketItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(basketItemDTO))
            )
            .andExpect(status().isBadRequest());

        List<BasketItem> basketItemList = basketItemRepository.findAll();
        assertThat(basketItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProductNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = basketItemRepository.findAll().size();
        // set the field null
        basketItem.setProductName(null);

        // Create the BasketItem, which fails.
        BasketItemDTO basketItemDTO = basketItemMapper.toDto(basketItem);

        restBasketItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(basketItemDTO))
            )
            .andExpect(status().isBadRequest());

        List<BasketItem> basketItemList = basketItemRepository.findAll();
        assertThat(basketItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUnitPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = basketItemRepository.findAll().size();
        // set the field null
        basketItem.setUnitPrice(null);

        // Create the BasketItem, which fails.
        BasketItemDTO basketItemDTO = basketItemMapper.toDto(basketItem);

        restBasketItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(basketItemDTO))
            )
            .andExpect(status().isBadRequest());

        List<BasketItem> basketItemList = basketItemRepository.findAll();
        assertThat(basketItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOldUnitPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = basketItemRepository.findAll().size();
        // set the field null
        basketItem.setOldUnitPrice(null);

        // Create the BasketItem, which fails.
        BasketItemDTO basketItemDTO = basketItemMapper.toDto(basketItem);

        restBasketItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(basketItemDTO))
            )
            .andExpect(status().isBadRequest());

        List<BasketItem> basketItemList = basketItemRepository.findAll();
        assertThat(basketItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = basketItemRepository.findAll().size();
        // set the field null
        basketItem.setQuantity(null);

        // Create the BasketItem, which fails.
        BasketItemDTO basketItemDTO = basketItemMapper.toDto(basketItem);

        restBasketItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(basketItemDTO))
            )
            .andExpect(status().isBadRequest());

        List<BasketItem> basketItemList = basketItemRepository.findAll();
        assertThat(basketItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPictureUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = basketItemRepository.findAll().size();
        // set the field null
        basketItem.setPictureUrl(null);

        // Create the BasketItem, which fails.
        BasketItemDTO basketItemDTO = basketItemMapper.toDto(basketItem);

        restBasketItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(basketItemDTO))
            )
            .andExpect(status().isBadRequest());

        List<BasketItem> basketItemList = basketItemRepository.findAll();
        assertThat(basketItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUserLoginIsRequired() throws Exception {
        int databaseSizeBeforeTest = basketItemRepository.findAll().size();
        // set the field null
        basketItem.setUserLogin(null);

        // Create the BasketItem, which fails.
        BasketItemDTO basketItemDTO = basketItemMapper.toDto(basketItem);

        restBasketItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(basketItemDTO))
            )
            .andExpect(status().isBadRequest());

        List<BasketItem> basketItemList = basketItemRepository.findAll();
        assertThat(basketItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBasketItems() throws Exception {
        // Initialize the database
        basketItemRepository.saveAndFlush(basketItem);

        // Get all the basketItemList
        restBasketItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(basketItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME)))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(sameNumber(DEFAULT_UNIT_PRICE))))
            .andExpect(jsonPath("$.[*].oldUnitPrice").value(hasItem(sameNumber(DEFAULT_OLD_UNIT_PRICE))))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].pictureUrl").value(hasItem(DEFAULT_PICTURE_URL)))
            .andExpect(jsonPath("$.[*].userLogin").value(hasItem(DEFAULT_USER_LOGIN)));
    }

    @Test
    @Transactional
    void getBasketItem() throws Exception {
        // Initialize the database
        basketItemRepository.saveAndFlush(basketItem);

        // Get the basketItem
        restBasketItemMockMvc
            .perform(get(ENTITY_API_URL_ID, basketItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(basketItem.getId().intValue()))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID.intValue()))
            .andExpect(jsonPath("$.productName").value(DEFAULT_PRODUCT_NAME))
            .andExpect(jsonPath("$.unitPrice").value(sameNumber(DEFAULT_UNIT_PRICE)))
            .andExpect(jsonPath("$.oldUnitPrice").value(sameNumber(DEFAULT_OLD_UNIT_PRICE)))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.pictureUrl").value(DEFAULT_PICTURE_URL))
            .andExpect(jsonPath("$.userLogin").value(DEFAULT_USER_LOGIN));
    }

    @Test
    @Transactional
    void getNonExistingBasketItem() throws Exception {
        // Get the basketItem
        restBasketItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBasketItem() throws Exception {
        // Initialize the database
        basketItemRepository.saveAndFlush(basketItem);

        int databaseSizeBeforeUpdate = basketItemRepository.findAll().size();

        // Update the basketItem
        BasketItem updatedBasketItem = basketItemRepository.findById(basketItem.getId()).get();
        // Disconnect from session so that the updates on updatedBasketItem are not directly saved in db
        em.detach(updatedBasketItem);
        updatedBasketItem
            .productId(UPDATED_PRODUCT_ID)
            .productName(UPDATED_PRODUCT_NAME)
            .unitPrice(UPDATED_UNIT_PRICE)
            .oldUnitPrice(UPDATED_OLD_UNIT_PRICE)
            .quantity(UPDATED_QUANTITY)
            .pictureUrl(UPDATED_PICTURE_URL)
            .userLogin(UPDATED_USER_LOGIN);
        BasketItemDTO basketItemDTO = basketItemMapper.toDto(updatedBasketItem);

        restBasketItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, basketItemDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(basketItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the BasketItem in the database
        List<BasketItem> basketItemList = basketItemRepository.findAll();
        assertThat(basketItemList).hasSize(databaseSizeBeforeUpdate);
        BasketItem testBasketItem = basketItemList.get(basketItemList.size() - 1);
        assertThat(testBasketItem.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testBasketItem.getProductName()).isEqualTo(UPDATED_PRODUCT_NAME);
        assertThat(testBasketItem.getUnitPrice()).isEqualTo(UPDATED_UNIT_PRICE);
        assertThat(testBasketItem.getOldUnitPrice()).isEqualTo(UPDATED_OLD_UNIT_PRICE);
        assertThat(testBasketItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testBasketItem.getPictureUrl()).isEqualTo(UPDATED_PICTURE_URL);
        assertThat(testBasketItem.getUserLogin()).isEqualTo(UPDATED_USER_LOGIN);
    }

    @Test
    @Transactional
    void putNonExistingBasketItem() throws Exception {
        int databaseSizeBeforeUpdate = basketItemRepository.findAll().size();
        basketItem.setId(count.incrementAndGet());

        // Create the BasketItem
        BasketItemDTO basketItemDTO = basketItemMapper.toDto(basketItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBasketItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, basketItemDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(basketItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BasketItem in the database
        List<BasketItem> basketItemList = basketItemRepository.findAll();
        assertThat(basketItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBasketItem() throws Exception {
        int databaseSizeBeforeUpdate = basketItemRepository.findAll().size();
        basketItem.setId(count.incrementAndGet());

        // Create the BasketItem
        BasketItemDTO basketItemDTO = basketItemMapper.toDto(basketItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBasketItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(basketItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BasketItem in the database
        List<BasketItem> basketItemList = basketItemRepository.findAll();
        assertThat(basketItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBasketItem() throws Exception {
        int databaseSizeBeforeUpdate = basketItemRepository.findAll().size();
        basketItem.setId(count.incrementAndGet());

        // Create the BasketItem
        BasketItemDTO basketItemDTO = basketItemMapper.toDto(basketItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBasketItemMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(basketItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BasketItem in the database
        List<BasketItem> basketItemList = basketItemRepository.findAll();
        assertThat(basketItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBasketItemWithPatch() throws Exception {
        // Initialize the database
        basketItemRepository.saveAndFlush(basketItem);

        int databaseSizeBeforeUpdate = basketItemRepository.findAll().size();

        // Update the basketItem using partial update
        BasketItem partialUpdatedBasketItem = new BasketItem();
        partialUpdatedBasketItem.setId(basketItem.getId());

        partialUpdatedBasketItem.productId(UPDATED_PRODUCT_ID).oldUnitPrice(UPDATED_OLD_UNIT_PRICE).pictureUrl(UPDATED_PICTURE_URL);

        restBasketItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBasketItem.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBasketItem))
            )
            .andExpect(status().isOk());

        // Validate the BasketItem in the database
        List<BasketItem> basketItemList = basketItemRepository.findAll();
        assertThat(basketItemList).hasSize(databaseSizeBeforeUpdate);
        BasketItem testBasketItem = basketItemList.get(basketItemList.size() - 1);
        assertThat(testBasketItem.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testBasketItem.getProductName()).isEqualTo(DEFAULT_PRODUCT_NAME);
        assertThat(testBasketItem.getUnitPrice()).isEqualByComparingTo(DEFAULT_UNIT_PRICE);
        assertThat(testBasketItem.getOldUnitPrice()).isEqualByComparingTo(UPDATED_OLD_UNIT_PRICE);
        assertThat(testBasketItem.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testBasketItem.getPictureUrl()).isEqualTo(UPDATED_PICTURE_URL);
        assertThat(testBasketItem.getUserLogin()).isEqualTo(DEFAULT_USER_LOGIN);
    }

    @Test
    @Transactional
    void fullUpdateBasketItemWithPatch() throws Exception {
        // Initialize the database
        basketItemRepository.saveAndFlush(basketItem);

        int databaseSizeBeforeUpdate = basketItemRepository.findAll().size();

        // Update the basketItem using partial update
        BasketItem partialUpdatedBasketItem = new BasketItem();
        partialUpdatedBasketItem.setId(basketItem.getId());

        partialUpdatedBasketItem
            .productId(UPDATED_PRODUCT_ID)
            .productName(UPDATED_PRODUCT_NAME)
            .unitPrice(UPDATED_UNIT_PRICE)
            .oldUnitPrice(UPDATED_OLD_UNIT_PRICE)
            .quantity(UPDATED_QUANTITY)
            .pictureUrl(UPDATED_PICTURE_URL)
            .userLogin(UPDATED_USER_LOGIN);

        restBasketItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBasketItem.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBasketItem))
            )
            .andExpect(status().isOk());

        // Validate the BasketItem in the database
        List<BasketItem> basketItemList = basketItemRepository.findAll();
        assertThat(basketItemList).hasSize(databaseSizeBeforeUpdate);
        BasketItem testBasketItem = basketItemList.get(basketItemList.size() - 1);
        assertThat(testBasketItem.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testBasketItem.getProductName()).isEqualTo(UPDATED_PRODUCT_NAME);
        assertThat(testBasketItem.getUnitPrice()).isEqualByComparingTo(UPDATED_UNIT_PRICE);
        assertThat(testBasketItem.getOldUnitPrice()).isEqualByComparingTo(UPDATED_OLD_UNIT_PRICE);
        assertThat(testBasketItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testBasketItem.getPictureUrl()).isEqualTo(UPDATED_PICTURE_URL);
        assertThat(testBasketItem.getUserLogin()).isEqualTo(UPDATED_USER_LOGIN);
    }

    @Test
    @Transactional
    void patchNonExistingBasketItem() throws Exception {
        int databaseSizeBeforeUpdate = basketItemRepository.findAll().size();
        basketItem.setId(count.incrementAndGet());

        // Create the BasketItem
        BasketItemDTO basketItemDTO = basketItemMapper.toDto(basketItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBasketItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, basketItemDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(basketItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BasketItem in the database
        List<BasketItem> basketItemList = basketItemRepository.findAll();
        assertThat(basketItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBasketItem() throws Exception {
        int databaseSizeBeforeUpdate = basketItemRepository.findAll().size();
        basketItem.setId(count.incrementAndGet());

        // Create the BasketItem
        BasketItemDTO basketItemDTO = basketItemMapper.toDto(basketItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBasketItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(basketItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BasketItem in the database
        List<BasketItem> basketItemList = basketItemRepository.findAll();
        assertThat(basketItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBasketItem() throws Exception {
        int databaseSizeBeforeUpdate = basketItemRepository.findAll().size();
        basketItem.setId(count.incrementAndGet());

        // Create the BasketItem
        BasketItemDTO basketItemDTO = basketItemMapper.toDto(basketItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBasketItemMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(basketItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BasketItem in the database
        List<BasketItem> basketItemList = basketItemRepository.findAll();
        assertThat(basketItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBasketItem() throws Exception {
        // Initialize the database
        basketItemRepository.saveAndFlush(basketItem);

        int databaseSizeBeforeDelete = basketItemRepository.findAll().size();

        // Delete the basketItem
        restBasketItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, basketItem.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BasketItem> basketItemList = basketItemRepository.findAll();
        assertThat(basketItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
