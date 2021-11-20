package com.gachokaerick.eshop.basket.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BasketItemMapperTest {

    private BasketItemMapper basketItemMapper;

    @BeforeEach
    public void setUp() {
        basketItemMapper = new BasketItemMapperImpl();
    }
}
