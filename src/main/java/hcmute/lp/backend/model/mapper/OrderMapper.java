package hcmute.lp.backend.model.mapper;

import hcmute.lp.backend.model.dto.order.OrderDto;
import hcmute.lp.backend.model.dto.order.OrderDetailDto;
import hcmute.lp.backend.model.dto.order.OrderRequest;
import hcmute.lp.backend.model.dto.order.OrderDetailRequest;
import hcmute.lp.backend.model.entity.Order;
import hcmute.lp.backend.model.entity.OrderDetail;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {CustomerMapper.class, DiscountMapper.class, ProductMapper.class})
public interface OrderMapper {

    OrderMapper INSTANCE = org.mapstruct.factory.Mappers.getMapper(OrderMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "discount", ignore = true)
    @Mapping(target = "orderDetails", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    Order toEntity(OrderRequest orderRequest);

    OrderDto toDto(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "subtotal", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    OrderDetail toDetailEntity(OrderDetailRequest orderDetailRequest);

    OrderDetailDto toDetailDto(OrderDetail orderDetail);

    List<OrderDto> toDtoList(List<Order> orders);
    List<OrderDetailDto> toDetailDtoList(List<OrderDetail> orderDetails);
}