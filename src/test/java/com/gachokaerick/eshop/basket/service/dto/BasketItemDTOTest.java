package com.gachokaerick.eshop.basket.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gachokaerick.eshop.basket.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BasketItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BasketItemDTO.class);
        BasketItemDTO basketItemDTO1 = new BasketItemDTO();
        basketItemDTO1.setId(1L);
        BasketItemDTO basketItemDTO2 = new BasketItemDTO();
        assertThat(basketItemDTO1).isNotEqualTo(basketItemDTO2);
        basketItemDTO2.setId(basketItemDTO1.getId());
        assertThat(basketItemDTO1).isEqualTo(basketItemDTO2);
        basketItemDTO2.setId(2L);
        assertThat(basketItemDTO1).isNotEqualTo(basketItemDTO2);
        basketItemDTO1.setId(null);
        assertThat(basketItemDTO1).isNotEqualTo(basketItemDTO2);
    }
}
