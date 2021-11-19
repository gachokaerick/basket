package com.gachokaerick.eshop.basket.web.rest;

import static com.gachokaerick.eshop.basket.web.rest.TestUtil.sameInstant;
import static com.gachokaerick.eshop.basket.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gachokaerick.eshop.basket.IntegrationTest;
import com.gachokaerick.eshop.basket.domain.BasketCheckout;
import com.gachokaerick.eshop.basket.repository.BasketCheckoutRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link BasketCheckoutResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BasketCheckoutResourceIT {

    private static final String DEFAULT_STREET = "AAAAAAAAAA";
    private static final String UPDATED_STREET = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_TOWN = "AAAAAAAAAA";
    private static final String UPDATED_TOWN = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_ZIPCODE = "AAAAAAAAAA";
    private static final String UPDATED_ZIPCODE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_PAYMENT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_PAYER_COUNTRY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PAYER_COUNTRY_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_PAYER_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_PAYER_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PAYER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PAYER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PAYER_SURNAME = "AAAAAAAAAA";
    private static final String UPDATED_PAYER_SURNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PAYER_ID = "AAAAAAAAAA";
    private static final String UPDATED_PAYER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENCY = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final String DEFAULT_PAYMENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_USER_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_USER_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/basket-checkouts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BasketCheckoutRepository basketCheckoutRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBasketCheckoutMockMvc;

    private BasketCheckout basketCheckout;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BasketCheckout createEntity(EntityManager em) {
        BasketCheckout basketCheckout = new BasketCheckout()
            .street(DEFAULT_STREET)
            .city(DEFAULT_CITY)
            .town(DEFAULT_TOWN)
            .country(DEFAULT_COUNTRY)
            .zipcode(DEFAULT_ZIPCODE)
            .createTime(DEFAULT_CREATE_TIME)
            .updateTime(DEFAULT_UPDATE_TIME)
            .paymentStatus(DEFAULT_PAYMENT_STATUS)
            .payerCountryCode(DEFAULT_PAYER_COUNTRY_CODE)
            .payerEmail(DEFAULT_PAYER_EMAIL)
            .payerName(DEFAULT_PAYER_NAME)
            .payerSurname(DEFAULT_PAYER_SURNAME)
            .payerId(DEFAULT_PAYER_ID)
            .currency(DEFAULT_CURRENCY)
            .amount(DEFAULT_AMOUNT)
            .paymentId(DEFAULT_PAYMENT_ID)
            .userLogin(DEFAULT_USER_LOGIN)
            .description(DEFAULT_DESCRIPTION);
        return basketCheckout;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BasketCheckout createUpdatedEntity(EntityManager em) {
        BasketCheckout basketCheckout = new BasketCheckout()
            .street(UPDATED_STREET)
            .city(UPDATED_CITY)
            .town(UPDATED_TOWN)
            .country(UPDATED_COUNTRY)
            .zipcode(UPDATED_ZIPCODE)
            .createTime(UPDATED_CREATE_TIME)
            .updateTime(UPDATED_UPDATE_TIME)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .payerCountryCode(UPDATED_PAYER_COUNTRY_CODE)
            .payerEmail(UPDATED_PAYER_EMAIL)
            .payerName(UPDATED_PAYER_NAME)
            .payerSurname(UPDATED_PAYER_SURNAME)
            .payerId(UPDATED_PAYER_ID)
            .currency(UPDATED_CURRENCY)
            .amount(UPDATED_AMOUNT)
            .paymentId(UPDATED_PAYMENT_ID)
            .userLogin(UPDATED_USER_LOGIN)
            .description(UPDATED_DESCRIPTION);
        return basketCheckout;
    }

    @BeforeEach
    public void initTest() {
        basketCheckout = createEntity(em);
    }

    @Test
    @Transactional
    void createBasketCheckout() throws Exception {
        int databaseSizeBeforeCreate = basketCheckoutRepository.findAll().size();
        // Create the BasketCheckout
        restBasketCheckoutMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(basketCheckout))
            )
            .andExpect(status().isCreated());

        // Validate the BasketCheckout in the database
        List<BasketCheckout> basketCheckoutList = basketCheckoutRepository.findAll();
        assertThat(basketCheckoutList).hasSize(databaseSizeBeforeCreate + 1);
        BasketCheckout testBasketCheckout = basketCheckoutList.get(basketCheckoutList.size() - 1);
        assertThat(testBasketCheckout.getStreet()).isEqualTo(DEFAULT_STREET);
        assertThat(testBasketCheckout.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testBasketCheckout.getTown()).isEqualTo(DEFAULT_TOWN);
        assertThat(testBasketCheckout.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testBasketCheckout.getZipcode()).isEqualTo(DEFAULT_ZIPCODE);
        assertThat(testBasketCheckout.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testBasketCheckout.getUpdateTime()).isEqualTo(DEFAULT_UPDATE_TIME);
        assertThat(testBasketCheckout.getPaymentStatus()).isEqualTo(DEFAULT_PAYMENT_STATUS);
        assertThat(testBasketCheckout.getPayerCountryCode()).isEqualTo(DEFAULT_PAYER_COUNTRY_CODE);
        assertThat(testBasketCheckout.getPayerEmail()).isEqualTo(DEFAULT_PAYER_EMAIL);
        assertThat(testBasketCheckout.getPayerName()).isEqualTo(DEFAULT_PAYER_NAME);
        assertThat(testBasketCheckout.getPayerSurname()).isEqualTo(DEFAULT_PAYER_SURNAME);
        assertThat(testBasketCheckout.getPayerId()).isEqualTo(DEFAULT_PAYER_ID);
        assertThat(testBasketCheckout.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testBasketCheckout.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testBasketCheckout.getPaymentId()).isEqualTo(DEFAULT_PAYMENT_ID);
        assertThat(testBasketCheckout.getUserLogin()).isEqualTo(DEFAULT_USER_LOGIN);
        assertThat(testBasketCheckout.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createBasketCheckoutWithExistingId() throws Exception {
        // Create the BasketCheckout with an existing ID
        basketCheckout.setId(1L);

        int databaseSizeBeforeCreate = basketCheckoutRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBasketCheckoutMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(basketCheckout))
            )
            .andExpect(status().isBadRequest());

        // Validate the BasketCheckout in the database
        List<BasketCheckout> basketCheckoutList = basketCheckoutRepository.findAll();
        assertThat(basketCheckoutList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = basketCheckoutRepository.findAll().size();
        // set the field null
        basketCheckout.setCity(null);

        // Create the BasketCheckout, which fails.

        restBasketCheckoutMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(basketCheckout))
            )
            .andExpect(status().isBadRequest());

        List<BasketCheckout> basketCheckoutList = basketCheckoutRepository.findAll();
        assertThat(basketCheckoutList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTownIsRequired() throws Exception {
        int databaseSizeBeforeTest = basketCheckoutRepository.findAll().size();
        // set the field null
        basketCheckout.setTown(null);

        // Create the BasketCheckout, which fails.

        restBasketCheckoutMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(basketCheckout))
            )
            .andExpect(status().isBadRequest());

        List<BasketCheckout> basketCheckoutList = basketCheckoutRepository.findAll();
        assertThat(basketCheckoutList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = basketCheckoutRepository.findAll().size();
        // set the field null
        basketCheckout.setCountry(null);

        // Create the BasketCheckout, which fails.

        restBasketCheckoutMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(basketCheckout))
            )
            .andExpect(status().isBadRequest());

        List<BasketCheckout> basketCheckoutList = basketCheckoutRepository.findAll();
        assertThat(basketCheckoutList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = basketCheckoutRepository.findAll().size();
        // set the field null
        basketCheckout.setCreateTime(null);

        // Create the BasketCheckout, which fails.

        restBasketCheckoutMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(basketCheckout))
            )
            .andExpect(status().isBadRequest());

        List<BasketCheckout> basketCheckoutList = basketCheckoutRepository.findAll();
        assertThat(basketCheckoutList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = basketCheckoutRepository.findAll().size();
        // set the field null
        basketCheckout.setUpdateTime(null);

        // Create the BasketCheckout, which fails.

        restBasketCheckoutMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(basketCheckout))
            )
            .andExpect(status().isBadRequest());

        List<BasketCheckout> basketCheckoutList = basketCheckoutRepository.findAll();
        assertThat(basketCheckoutList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPayerNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = basketCheckoutRepository.findAll().size();
        // set the field null
        basketCheckout.setPayerName(null);

        // Create the BasketCheckout, which fails.

        restBasketCheckoutMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(basketCheckout))
            )
            .andExpect(status().isBadRequest());

        List<BasketCheckout> basketCheckoutList = basketCheckoutRepository.findAll();
        assertThat(basketCheckoutList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPayerIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = basketCheckoutRepository.findAll().size();
        // set the field null
        basketCheckout.setPayerId(null);

        // Create the BasketCheckout, which fails.

        restBasketCheckoutMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(basketCheckout))
            )
            .andExpect(status().isBadRequest());

        List<BasketCheckout> basketCheckoutList = basketCheckoutRepository.findAll();
        assertThat(basketCheckoutList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCurrencyIsRequired() throws Exception {
        int databaseSizeBeforeTest = basketCheckoutRepository.findAll().size();
        // set the field null
        basketCheckout.setCurrency(null);

        // Create the BasketCheckout, which fails.

        restBasketCheckoutMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(basketCheckout))
            )
            .andExpect(status().isBadRequest());

        List<BasketCheckout> basketCheckoutList = basketCheckoutRepository.findAll();
        assertThat(basketCheckoutList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = basketCheckoutRepository.findAll().size();
        // set the field null
        basketCheckout.setAmount(null);

        // Create the BasketCheckout, which fails.

        restBasketCheckoutMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(basketCheckout))
            )
            .andExpect(status().isBadRequest());

        List<BasketCheckout> basketCheckoutList = basketCheckoutRepository.findAll();
        assertThat(basketCheckoutList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUserLoginIsRequired() throws Exception {
        int databaseSizeBeforeTest = basketCheckoutRepository.findAll().size();
        // set the field null
        basketCheckout.setUserLogin(null);

        // Create the BasketCheckout, which fails.

        restBasketCheckoutMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(basketCheckout))
            )
            .andExpect(status().isBadRequest());

        List<BasketCheckout> basketCheckoutList = basketCheckoutRepository.findAll();
        assertThat(basketCheckoutList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBasketCheckouts() throws Exception {
        // Initialize the database
        basketCheckoutRepository.saveAndFlush(basketCheckout);

        // Get all the basketCheckoutList
        restBasketCheckoutMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(basketCheckout.getId().intValue())))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].town").value(hasItem(DEFAULT_TOWN)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].zipcode").value(hasItem(DEFAULT_ZIPCODE)))
            .andExpect(jsonPath("$.[*].createTime").value(hasItem(sameInstant(DEFAULT_CREATE_TIME))))
            .andExpect(jsonPath("$.[*].updateTime").value(hasItem(sameInstant(DEFAULT_UPDATE_TIME))))
            .andExpect(jsonPath("$.[*].paymentStatus").value(hasItem(DEFAULT_PAYMENT_STATUS)))
            .andExpect(jsonPath("$.[*].payerCountryCode").value(hasItem(DEFAULT_PAYER_COUNTRY_CODE)))
            .andExpect(jsonPath("$.[*].payerEmail").value(hasItem(DEFAULT_PAYER_EMAIL)))
            .andExpect(jsonPath("$.[*].payerName").value(hasItem(DEFAULT_PAYER_NAME)))
            .andExpect(jsonPath("$.[*].payerSurname").value(hasItem(DEFAULT_PAYER_SURNAME)))
            .andExpect(jsonPath("$.[*].payerId").value(hasItem(DEFAULT_PAYER_ID)))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentId").value(hasItem(DEFAULT_PAYMENT_ID)))
            .andExpect(jsonPath("$.[*].userLogin").value(hasItem(DEFAULT_USER_LOGIN)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getBasketCheckout() throws Exception {
        // Initialize the database
        basketCheckoutRepository.saveAndFlush(basketCheckout);

        // Get the basketCheckout
        restBasketCheckoutMockMvc
            .perform(get(ENTITY_API_URL_ID, basketCheckout.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(basketCheckout.getId().intValue()))
            .andExpect(jsonPath("$.street").value(DEFAULT_STREET))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.town").value(DEFAULT_TOWN))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.zipcode").value(DEFAULT_ZIPCODE))
            .andExpect(jsonPath("$.createTime").value(sameInstant(DEFAULT_CREATE_TIME)))
            .andExpect(jsonPath("$.updateTime").value(sameInstant(DEFAULT_UPDATE_TIME)))
            .andExpect(jsonPath("$.paymentStatus").value(DEFAULT_PAYMENT_STATUS))
            .andExpect(jsonPath("$.payerCountryCode").value(DEFAULT_PAYER_COUNTRY_CODE))
            .andExpect(jsonPath("$.payerEmail").value(DEFAULT_PAYER_EMAIL))
            .andExpect(jsonPath("$.payerName").value(DEFAULT_PAYER_NAME))
            .andExpect(jsonPath("$.payerSurname").value(DEFAULT_PAYER_SURNAME))
            .andExpect(jsonPath("$.payerId").value(DEFAULT_PAYER_ID))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.paymentId").value(DEFAULT_PAYMENT_ID))
            .andExpect(jsonPath("$.userLogin").value(DEFAULT_USER_LOGIN))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingBasketCheckout() throws Exception {
        // Get the basketCheckout
        restBasketCheckoutMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBasketCheckout() throws Exception {
        // Initialize the database
        basketCheckoutRepository.saveAndFlush(basketCheckout);

        int databaseSizeBeforeUpdate = basketCheckoutRepository.findAll().size();

        // Update the basketCheckout
        BasketCheckout updatedBasketCheckout = basketCheckoutRepository.findById(basketCheckout.getId()).get();
        // Disconnect from session so that the updates on updatedBasketCheckout are not directly saved in db
        em.detach(updatedBasketCheckout);
        updatedBasketCheckout
            .street(UPDATED_STREET)
            .city(UPDATED_CITY)
            .town(UPDATED_TOWN)
            .country(UPDATED_COUNTRY)
            .zipcode(UPDATED_ZIPCODE)
            .createTime(UPDATED_CREATE_TIME)
            .updateTime(UPDATED_UPDATE_TIME)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .payerCountryCode(UPDATED_PAYER_COUNTRY_CODE)
            .payerEmail(UPDATED_PAYER_EMAIL)
            .payerName(UPDATED_PAYER_NAME)
            .payerSurname(UPDATED_PAYER_SURNAME)
            .payerId(UPDATED_PAYER_ID)
            .currency(UPDATED_CURRENCY)
            .amount(UPDATED_AMOUNT)
            .paymentId(UPDATED_PAYMENT_ID)
            .userLogin(UPDATED_USER_LOGIN)
            .description(UPDATED_DESCRIPTION);

        restBasketCheckoutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBasketCheckout.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBasketCheckout))
            )
            .andExpect(status().isOk());

        // Validate the BasketCheckout in the database
        List<BasketCheckout> basketCheckoutList = basketCheckoutRepository.findAll();
        assertThat(basketCheckoutList).hasSize(databaseSizeBeforeUpdate);
        BasketCheckout testBasketCheckout = basketCheckoutList.get(basketCheckoutList.size() - 1);
        assertThat(testBasketCheckout.getStreet()).isEqualTo(UPDATED_STREET);
        assertThat(testBasketCheckout.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testBasketCheckout.getTown()).isEqualTo(UPDATED_TOWN);
        assertThat(testBasketCheckout.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testBasketCheckout.getZipcode()).isEqualTo(UPDATED_ZIPCODE);
        assertThat(testBasketCheckout.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testBasketCheckout.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
        assertThat(testBasketCheckout.getPaymentStatus()).isEqualTo(UPDATED_PAYMENT_STATUS);
        assertThat(testBasketCheckout.getPayerCountryCode()).isEqualTo(UPDATED_PAYER_COUNTRY_CODE);
        assertThat(testBasketCheckout.getPayerEmail()).isEqualTo(UPDATED_PAYER_EMAIL);
        assertThat(testBasketCheckout.getPayerName()).isEqualTo(UPDATED_PAYER_NAME);
        assertThat(testBasketCheckout.getPayerSurname()).isEqualTo(UPDATED_PAYER_SURNAME);
        assertThat(testBasketCheckout.getPayerId()).isEqualTo(UPDATED_PAYER_ID);
        assertThat(testBasketCheckout.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testBasketCheckout.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testBasketCheckout.getPaymentId()).isEqualTo(UPDATED_PAYMENT_ID);
        assertThat(testBasketCheckout.getUserLogin()).isEqualTo(UPDATED_USER_LOGIN);
        assertThat(testBasketCheckout.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingBasketCheckout() throws Exception {
        int databaseSizeBeforeUpdate = basketCheckoutRepository.findAll().size();
        basketCheckout.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBasketCheckoutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, basketCheckout.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(basketCheckout))
            )
            .andExpect(status().isBadRequest());

        // Validate the BasketCheckout in the database
        List<BasketCheckout> basketCheckoutList = basketCheckoutRepository.findAll();
        assertThat(basketCheckoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBasketCheckout() throws Exception {
        int databaseSizeBeforeUpdate = basketCheckoutRepository.findAll().size();
        basketCheckout.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBasketCheckoutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(basketCheckout))
            )
            .andExpect(status().isBadRequest());

        // Validate the BasketCheckout in the database
        List<BasketCheckout> basketCheckoutList = basketCheckoutRepository.findAll();
        assertThat(basketCheckoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBasketCheckout() throws Exception {
        int databaseSizeBeforeUpdate = basketCheckoutRepository.findAll().size();
        basketCheckout.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBasketCheckoutMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(basketCheckout))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BasketCheckout in the database
        List<BasketCheckout> basketCheckoutList = basketCheckoutRepository.findAll();
        assertThat(basketCheckoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBasketCheckoutWithPatch() throws Exception {
        // Initialize the database
        basketCheckoutRepository.saveAndFlush(basketCheckout);

        int databaseSizeBeforeUpdate = basketCheckoutRepository.findAll().size();

        // Update the basketCheckout using partial update
        BasketCheckout partialUpdatedBasketCheckout = new BasketCheckout();
        partialUpdatedBasketCheckout.setId(basketCheckout.getId());

        partialUpdatedBasketCheckout
            .street(UPDATED_STREET)
            .updateTime(UPDATED_UPDATE_TIME)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .payerName(UPDATED_PAYER_NAME)
            .payerSurname(UPDATED_PAYER_SURNAME)
            .payerId(UPDATED_PAYER_ID)
            .amount(UPDATED_AMOUNT)
            .paymentId(UPDATED_PAYMENT_ID)
            .description(UPDATED_DESCRIPTION);

        restBasketCheckoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBasketCheckout.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBasketCheckout))
            )
            .andExpect(status().isOk());

        // Validate the BasketCheckout in the database
        List<BasketCheckout> basketCheckoutList = basketCheckoutRepository.findAll();
        assertThat(basketCheckoutList).hasSize(databaseSizeBeforeUpdate);
        BasketCheckout testBasketCheckout = basketCheckoutList.get(basketCheckoutList.size() - 1);
        assertThat(testBasketCheckout.getStreet()).isEqualTo(UPDATED_STREET);
        assertThat(testBasketCheckout.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testBasketCheckout.getTown()).isEqualTo(DEFAULT_TOWN);
        assertThat(testBasketCheckout.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testBasketCheckout.getZipcode()).isEqualTo(DEFAULT_ZIPCODE);
        assertThat(testBasketCheckout.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testBasketCheckout.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
        assertThat(testBasketCheckout.getPaymentStatus()).isEqualTo(UPDATED_PAYMENT_STATUS);
        assertThat(testBasketCheckout.getPayerCountryCode()).isEqualTo(DEFAULT_PAYER_COUNTRY_CODE);
        assertThat(testBasketCheckout.getPayerEmail()).isEqualTo(DEFAULT_PAYER_EMAIL);
        assertThat(testBasketCheckout.getPayerName()).isEqualTo(UPDATED_PAYER_NAME);
        assertThat(testBasketCheckout.getPayerSurname()).isEqualTo(UPDATED_PAYER_SURNAME);
        assertThat(testBasketCheckout.getPayerId()).isEqualTo(UPDATED_PAYER_ID);
        assertThat(testBasketCheckout.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testBasketCheckout.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testBasketCheckout.getPaymentId()).isEqualTo(UPDATED_PAYMENT_ID);
        assertThat(testBasketCheckout.getUserLogin()).isEqualTo(DEFAULT_USER_LOGIN);
        assertThat(testBasketCheckout.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateBasketCheckoutWithPatch() throws Exception {
        // Initialize the database
        basketCheckoutRepository.saveAndFlush(basketCheckout);

        int databaseSizeBeforeUpdate = basketCheckoutRepository.findAll().size();

        // Update the basketCheckout using partial update
        BasketCheckout partialUpdatedBasketCheckout = new BasketCheckout();
        partialUpdatedBasketCheckout.setId(basketCheckout.getId());

        partialUpdatedBasketCheckout
            .street(UPDATED_STREET)
            .city(UPDATED_CITY)
            .town(UPDATED_TOWN)
            .country(UPDATED_COUNTRY)
            .zipcode(UPDATED_ZIPCODE)
            .createTime(UPDATED_CREATE_TIME)
            .updateTime(UPDATED_UPDATE_TIME)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .payerCountryCode(UPDATED_PAYER_COUNTRY_CODE)
            .payerEmail(UPDATED_PAYER_EMAIL)
            .payerName(UPDATED_PAYER_NAME)
            .payerSurname(UPDATED_PAYER_SURNAME)
            .payerId(UPDATED_PAYER_ID)
            .currency(UPDATED_CURRENCY)
            .amount(UPDATED_AMOUNT)
            .paymentId(UPDATED_PAYMENT_ID)
            .userLogin(UPDATED_USER_LOGIN)
            .description(UPDATED_DESCRIPTION);

        restBasketCheckoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBasketCheckout.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBasketCheckout))
            )
            .andExpect(status().isOk());

        // Validate the BasketCheckout in the database
        List<BasketCheckout> basketCheckoutList = basketCheckoutRepository.findAll();
        assertThat(basketCheckoutList).hasSize(databaseSizeBeforeUpdate);
        BasketCheckout testBasketCheckout = basketCheckoutList.get(basketCheckoutList.size() - 1);
        assertThat(testBasketCheckout.getStreet()).isEqualTo(UPDATED_STREET);
        assertThat(testBasketCheckout.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testBasketCheckout.getTown()).isEqualTo(UPDATED_TOWN);
        assertThat(testBasketCheckout.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testBasketCheckout.getZipcode()).isEqualTo(UPDATED_ZIPCODE);
        assertThat(testBasketCheckout.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testBasketCheckout.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
        assertThat(testBasketCheckout.getPaymentStatus()).isEqualTo(UPDATED_PAYMENT_STATUS);
        assertThat(testBasketCheckout.getPayerCountryCode()).isEqualTo(UPDATED_PAYER_COUNTRY_CODE);
        assertThat(testBasketCheckout.getPayerEmail()).isEqualTo(UPDATED_PAYER_EMAIL);
        assertThat(testBasketCheckout.getPayerName()).isEqualTo(UPDATED_PAYER_NAME);
        assertThat(testBasketCheckout.getPayerSurname()).isEqualTo(UPDATED_PAYER_SURNAME);
        assertThat(testBasketCheckout.getPayerId()).isEqualTo(UPDATED_PAYER_ID);
        assertThat(testBasketCheckout.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testBasketCheckout.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testBasketCheckout.getPaymentId()).isEqualTo(UPDATED_PAYMENT_ID);
        assertThat(testBasketCheckout.getUserLogin()).isEqualTo(UPDATED_USER_LOGIN);
        assertThat(testBasketCheckout.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingBasketCheckout() throws Exception {
        int databaseSizeBeforeUpdate = basketCheckoutRepository.findAll().size();
        basketCheckout.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBasketCheckoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, basketCheckout.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(basketCheckout))
            )
            .andExpect(status().isBadRequest());

        // Validate the BasketCheckout in the database
        List<BasketCheckout> basketCheckoutList = basketCheckoutRepository.findAll();
        assertThat(basketCheckoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBasketCheckout() throws Exception {
        int databaseSizeBeforeUpdate = basketCheckoutRepository.findAll().size();
        basketCheckout.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBasketCheckoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(basketCheckout))
            )
            .andExpect(status().isBadRequest());

        // Validate the BasketCheckout in the database
        List<BasketCheckout> basketCheckoutList = basketCheckoutRepository.findAll();
        assertThat(basketCheckoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBasketCheckout() throws Exception {
        int databaseSizeBeforeUpdate = basketCheckoutRepository.findAll().size();
        basketCheckout.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBasketCheckoutMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(basketCheckout))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BasketCheckout in the database
        List<BasketCheckout> basketCheckoutList = basketCheckoutRepository.findAll();
        assertThat(basketCheckoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBasketCheckout() throws Exception {
        // Initialize the database
        basketCheckoutRepository.saveAndFlush(basketCheckout);

        int databaseSizeBeforeDelete = basketCheckoutRepository.findAll().size();

        // Delete the basketCheckout
        restBasketCheckoutMockMvc
            .perform(delete(ENTITY_API_URL_ID, basketCheckout.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BasketCheckout> basketCheckoutList = basketCheckoutRepository.findAll();
        assertThat(basketCheckoutList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
