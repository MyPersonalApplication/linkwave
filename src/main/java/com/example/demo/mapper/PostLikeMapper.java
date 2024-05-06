package com.example.demo.mapper;

import com.example.demo.dto.postlike.CreatePostLikeDTO;
import com.example.demo.dto.postlike.PostLikeDTO;
import com.example.demo.model.interact.PostLike;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PostLikeMapper {
    PostLikeMapper INSTANCE = Mappers.getMapper(PostLikeMapper.class);

    PostLikeDTO toDto(PostLike postLike);

    @Mapping(target = "post.id", source = "postId")
    @Mapping(target = "user.id", source = "userId")
    PostLike toEntity(CreatePostLikeDTO createPostLikeDTO);
}
