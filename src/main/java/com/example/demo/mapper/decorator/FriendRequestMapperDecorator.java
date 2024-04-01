package com.example.demo.mapper.decorator;

import com.example.demo.dto.friendrequest.SendRequestDTO;
import com.example.demo.mapper.friend.FriendRequestMapper;
import com.example.demo.model.friend.FriendRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public abstract class FriendRequestMapperDecorator implements FriendRequestMapper {
    @Autowired
    @Qualifier(value = "delegate")
    protected FriendRequestMapper surgeryMapper;

    @Override
    public FriendRequest toEntity(SendRequestDTO sendRequestDTO) {
        FriendRequest entity = surgeryMapper.toEntity(sendRequestDTO);
        entity.setCreatedAt(new Date());
        entity.setUpdatedAt(new Date());
        return entity;
    }
}
