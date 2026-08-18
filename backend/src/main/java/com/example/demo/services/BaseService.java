package com.example.demo.services;

import com.example.demo.DTO.AddUserRequestDTO;
import com.example.demo.DTO.SimpleUserDTO;
import com.example.demo.entities.BaseUser;
import com.example.demo.repositories.BaseRepositoryHibernate;
import com.example.demo.repositories.BaseRepositoryJPA;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BaseService {


    private final BaseRepositoryHibernate baseRepositoryHibernate;
    private final BaseRepositoryJPA baseRepositoryJPA;

    public BaseService(BaseRepositoryHibernate baseRepositoryHibernate, BaseRepositoryJPA baseRepositoryJPA) {
        this.baseRepositoryHibernate = baseRepositoryHibernate;
        this.baseRepositoryJPA = baseRepositoryJPA;
    }

    public SimpleUserDTO saveUsers(AddUserRequestDTO addUserRequestDTO){
        return mapEntityToAddUsersDTO(baseRepositoryHibernate.save(mapAddUsersDTOToEntity(addUserRequestDTO)));
    }

    public List<SimpleUserDTO> usersListScope(Integer scope){
        List<BaseUser> baseUserEintites = baseRepositoryJPA.listUsersScope(scope);
        ArrayList<SimpleUserDTO> usersList = new ArrayList<>();
        if(!baseUserEintites.isEmpty()){
            baseUserEintites.forEach(u -> {
                mapEntityToUserResponseDTO(u);
                usersList.add(mapEntityToUserResponseDTO(u));
            });
        }
        return usersList;


    }




    public BaseUser mapAddUsersDTOToEntity(AddUserRequestDTO addUserRequestDTO){
        BaseUser users = new BaseUser();
        users.setFirstName(addUserRequestDTO.getFirstName());
        users.setLastName(addUserRequestDTO.getLastName());
        return users;
    }

    public SimpleUserDTO mapEntityToAddUsersDTO(BaseUser users){
        SimpleUserDTO addSimpleUserDTO = new SimpleUserDTO(
                users.getFirstName(),
                users.getLastName()
        );
        return addSimpleUserDTO;
    }

    public static SimpleUserDTO mapEntityToUserResponseDTO(BaseUser user){
        return new SimpleUserDTO(
                user.getFirstName(),
                user.getFirstName()
        );
    }

}
