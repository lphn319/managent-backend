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

    @Mapping(target = "customer", source = "customer")
    CartDto toDto(Cart cart);

    @Mapping(target = "product", source = "product")
    CartItemDto toItemDto(CartItem cartItem);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cart", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "subtotal", ignore = true)
    CartItem toItemEntity(CartItemRequest cartItemRequest);

    List<CartItemDto> toItemDtoList(List<CartItem> cartItems);
}