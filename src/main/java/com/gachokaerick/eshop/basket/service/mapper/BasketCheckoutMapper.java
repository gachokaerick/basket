package com.gachokaerick.eshop.basket.service.mapper;

import com.gachokaerick.eshop.basket.domain.BasketCheckout;
import com.gachokaerick.eshop.basket.service.dto.BasketCheckoutDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BasketCheckout} and its DTO {@link BasketCheckoutDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BasketCheckoutMapper extends EntityMapper<BasketCheckoutDTO, BasketCheckout> {}
