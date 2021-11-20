package com.gachokaerick.eshop.basket.service.mapper;

import com.gachokaerick.eshop.basket.domain.BasketItem;
import com.gachokaerick.eshop.basket.service.dto.BasketItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BasketItem} and its DTO {@link BasketItemDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BasketItemMapper extends EntityMapper<BasketItemDTO, BasketItem> {}
