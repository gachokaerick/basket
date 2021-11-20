package com.gachokaerick.eshop.basket.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gachokaerick.eshop.basket.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BasketCheckoutDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BasketCheckoutDTO.class);
        BasketCheckoutDTO basketCheckoutDTO1 = new BasketCheckoutDTO();
        basketCheckoutDTO1.setId(1L);
        BasketCheckoutDTO basketCheckoutDTO2 = new BasketCheckoutDTO();
        assertThat(basketCheckoutDTO1).isNotEqualTo(basketCheckoutDTO2);
        basketCheckoutDTO2.setId(basketCheckoutDTO1.getId());
        assertThat(basketCheckoutDTO1).isEqualTo(basketCheckoutDTO2);
        basketCheckoutDTO2.setId(2L);
        assertThat(basketCheckoutDTO1).isNotEqualTo(basketCheckoutDTO2);
        basketCheckoutDTO1.setId(null);
        assertThat(basketCheckoutDTO1).isNotEqualTo(basketCheckoutDTO2);
    }
}
