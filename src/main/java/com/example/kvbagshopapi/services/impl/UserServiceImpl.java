package com.example.kvbagshopapi.services.impl;

import com.example.kvbagshopapi.dtos.SignUp;
import com.example.kvbagshopapi.dtos.UserDto;
import com.example.kvbagshopapi.dtos.UserPagination;
import com.example.kvbagshopapi.entities.User;
import com.example.kvbagshopapi.exceptions.NotFoundException;
import com.example.kvbagshopapi.repositories.UserRepository;
import com.example.kvbagshopapi.services.IUserService;
import com.example.kvbagshopapi.utils.MapperUtils;
import com.example.kvbagshopapi.utils.Role;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService {
    private final UserRepository userRepository;
    private ModelMapper modelMapper;

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map((User) -> modelMapper.map(User, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserPagination getAllPagingUsers(int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        // Create Pagenable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<User> users = userRepository.findAll(pageable);

        // get content for page object
        List<User> listOfusers = users.getContent();

        List<UserDto> contents = listOfusers.stream()
//                .filter(User -> !User.getIsDeleted())
                .map((User) -> modelMapper.map(User, UserDto.class))
                .collect(Collectors.toList());

        UserPagination UserPagination = new UserPagination();
        UserPagination.setContent(contents);
        UserPagination.setPageNo(users.getNumber());
        UserPagination.setPageSize(users.getSize());
        UserPagination.setTotalElements(users.getTotalElements());
        UserPagination.setTotalPages(users.getTotalPages());
        UserPagination.setLast(users.isLast());
        return UserPagination;
    }

    @Override
    public UserDto getUserById(Long UserId) {
        Optional<User> UserOp = userRepository.findById(UserId);
        if (!UserOp.isPresent())
            throw new NotFoundException("Cant find User!");
        return modelMapper.map(UserOp.get(), UserDto.class);
    }

    @Override
    public UserDto getUserByUsername(String username) {
        User UserOp = userRepository.findByUserName(username);
        if (UserOp == null)
            throw new NotFoundException("Cant find User!");
        return modelMapper.map(UserOp, UserDto.class);
    }

    @Override
    public UserDto createUser(SignUp signUp) {
        User User = modelMapper.map(signUp, User.class);
        List<Role> roles = new ArrayList<>();
        roles.add(Role.ROLE_USER);
        User.setRoles(roles);
        User.setIsActive(true);
        User savedUser = userRepository.save(User);
        UserDto saveUserDto = modelMapper.map(savedUser, UserDto.class);
        return saveUserDto;
    }


    // Cập nhật lại User (chỉ cập nhật những thuộc tính muốn thay đổi)
    @Override
    public UserDto patchUser(Long id, Map<Object, Object> AccountDto) {
        Optional<User> existingUser = userRepository.findById(id);
        if (!existingUser.isPresent()) throw new NotFoundException("Unable to update User!");

        AccountDto.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(User.class, (String) key);
            field.setAccessible(true);
            ReflectionUtils.setField(field, existingUser.get(), (Object) value);
        });
        existingUser.get().setUpdateAt(new Date(new Date().getTime()));
        User updatedUser = userRepository.save(existingUser.get());
        UserDto updatedUserDto = modelMapper.map(updatedUser, UserDto.class);

        return updatedUserDto;

    }

    // Cập nhật lại User (cập nhật lại toàn bộ các thuộc tính)
    @Override
    public UserDto updateUser(Long id, UserDto UserDto) throws NoSuchFieldException, IllegalAccessException {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser == null) throw new NotFoundException("Unable to update User!");

//        BeanUtils.copyProperties(UserDto, existingUser);

        MapperUtils.toDto(UserDto, existingUser);

        existingUser.setUpdateAt(new Date(new Date().getTime()));
        User updatedUser = userRepository.save(existingUser);
        UserDto updatedUserDto = modelMapper.map(updatedUser, UserDto.class);
        return updatedUserDto;

    }

    // Hàm deleteUser chỉ delete bằng cách set thuộc tính IsDeleted = true chứ không xoá hẳn trong database
    @Override
    public void deleteUser(Long UserId) {
        Optional<User> existingUser = userRepository.findById(UserId);
        if (!existingUser.isPresent()) throw new NotFoundException("Unable to dalete User!");

        existingUser.get().setIsActive(false);
        existingUser.get().setUpdateAt(new Date(new Date().getTime()));
        userRepository.save(existingUser.get());
    }

    @Override
    public UserDto getUserByUserNameAndPassword(String userName, String password) {
        User user = userRepository.findByUserNameAndPassword(userName, password);
        if (user == null) throw new NotFoundException("Wrong username or password !!");
        if (!user.getIsActive()) throw new NotFoundException("User has been deleted !");
        return modelMapper.map(user, UserDto.class);
    }

}
