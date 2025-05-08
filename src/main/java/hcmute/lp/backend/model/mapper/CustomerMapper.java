package hcmute.lp.backend.model.mapper;

import hcmute.lp.backend.model.dto.customer.CustomerDto;
import hcmute.lp.backend.model.dto.customer.CustomerRequest;
import hcmute.lp.backend.model.entity.Customer;
import hcmute.lp.backend.model.entity.User;
import org.mapstruct.*;

import java.time.LocalDate;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerMapper {

    CustomerMapper INSTANCE = org.mapstruct.factory.Mappers.getMapper(CustomerMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "loyaltyPoints", constant = "0")
    @Mapping(target = "membershipLevel", constant = "BRONZE")
    Customer toEntity(CustomerRequest customerRequest, User user);

    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "status", source = "user.status")
    CustomerDto toDto(Customer customer);

    List<CustomerDto> toDtoList(List<Customer> customers);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "loyaltyPoints", ignore = true)
    @Mapping(target = "membershipLevel", ignore = true)
    void updateEntityFromRequest(CustomerRequest customerRequest, @MappingTarget Customer customer);
}