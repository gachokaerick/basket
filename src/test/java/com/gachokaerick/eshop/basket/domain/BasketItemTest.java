package com.gachokaerick.eshop.basket.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gachokaerick.eshop.basket.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BasketItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BasketItem.class);
        BasketItem basketItem1 = new BasketItem();
        basketItem1.setId(1L);
        BasketItem basketItem2 = new BasketItem();
        basketItem2.setId(basketItem1.getId());
        assertThat(basketItem1).isEqualTo(basketItem2);
        basketItem2.setId(2L);
        assertThat(basketItem1).isNotEqualTo(basketItem2);
        basketItem1.setId(null);
        assertThat(basketItem1).isNotEqualTo(basketItem2);
    }
}
