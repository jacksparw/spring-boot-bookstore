package com.assignment.bookstore.beans.dto.mapper;

import com.assignment.bookstore.beans.domain.Address;
import com.assignment.bookstore.beans.dto.AddressDTO;
import org.mapstruct.Mapper;

@Mapper
public interface AddressMapper {
    AddressDTO addressToAddressDTO(Address address);
}
