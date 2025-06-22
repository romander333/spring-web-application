package com.kozak.mybookshop.mapper;

import com.kozak.mybookshop.config.MapperConfig;
import com.kozak.mybookshop.dto.order.OrderDto;
import com.kozak.mybookshop.dto.order.OrderRequestDto;
import com.kozak.mybookshop.model.Order;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {
    OrderDto toDto(Order order);

    Order toModel(OrderRequestDto requestDto);
}
