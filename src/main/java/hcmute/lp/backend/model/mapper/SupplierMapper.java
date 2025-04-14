package hcmute.lp.backend.model.mapper;

import hcmute.lp.backend.model.dto.supplier.SupplierDto;
import hcmute.lp.backend.model.dto.supplier.SupplierRequest;
import hcmute.lp.backend.model.entity.Supplier;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SupplierMapper {
    SupplierMapper INSTANCE = Mappers.getMapper(SupplierMapper.class);

    // Để đơn giản, không cần chỉ định source/target cho các trường trùng tên
    SupplierDto toDto(Supplier supplier);

    List<SupplierDto> toDtoList(List<Supplier> suppliers);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "imports", ignore = true)
    Supplier toEntity(SupplierRequest supplierRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "imports", ignore = true)
    @Mapping(target = "companyName", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "address", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "phone", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "email", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "description", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(@MappingTarget Supplier supplier, SupplierRequest supplierRequest);
}