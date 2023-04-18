package com.vnptt.tms.converter;

import com.vnptt.tms.dto.UserDTO;
import org.modelmapper.ModelMapper;
import com.vnptt.tms.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    @Autowired
    private ModelMapper mapper;

    /**
     *  Convert for method Post
     * @param dto
     * @return
     */
    public UserEntity toEntity(UserDTO dto) {
        UserEntity entity = new UserEntity();
        entity = mapper.map(dto, UserEntity.class);
        return entity;
    }

    /**
     *  Convert for mehtod get
     * @param entity
     * @return
     */
    public UserDTO toDTO(UserEntity entity) {
        UserDTO dto = new UserDTO();
        if (entity.getId() != null) {
            dto.setId(entity.getId());
        }
        dto = mapper.map(entity, UserDTO.class);
        if (entity.getRuleEntity() != null) {
            dto.setRuleName(entity.getRuleEntity().getName());
        }
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setModifiedDate(entity.getModifiedDate());
        dto.setModifiedBy(entity.getModifiedBy());
        return dto;
    }

    /**
     *  Convert for method put
     * @param dto
     * @param entity
     * @return
     */
    public UserEntity toEntity(UserDTO dto, UserEntity entity) {
        //entity = mapper.map(dto, UserEntity.class);
        entity.setName(dto.getName());
        if (dto.getPassword() != null ){
            entity.setPassword(dto.getPassword());
        }
        if (dto.getCompany() != null){
            entity.setCompany(dto.getCompany());
        }
        if (dto.getEmail() != null){
            entity.setMail(dto.getEmail());
        }
        entity.setContact(dto.getContact());
        return entity;
    }
}