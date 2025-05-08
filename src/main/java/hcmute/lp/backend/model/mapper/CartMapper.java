package hcmute.lp.backend.model.mapper;

import hcmute.lp.backend.model.dto.cart.CartDto;
import hcmute.lp.backend.model.dto.cart.CartItemDto;
import hcmute.lp.backend.model.dto.cart.CartItemRequest;
import hcmute.lp.backend.model.entity.Cart;
import hcmute.lp.backend.model.entity.CartItem;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {CustomerMapper.class, ProductMapper.class})
public interface CartMapper {

    CartMapper INSTANCE = org.mapstruct.factory.Mappers.getMapper(CartMapper.class);

    CartDto toDto(Cart cart);

    CartItemDto toItemDto(CartItem cartItem);

    CartItem toItemEntity(CartItemRequest cartItemRequest);

    List<CartItemDto> toItemDtoList(List<CartItem> cartItems);
}