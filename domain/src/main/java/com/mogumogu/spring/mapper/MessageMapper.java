package com.mogumogu.spring.mapper;


import com.mogumogu.spring.ArticleEntity;
import com.mogumogu.spring.MessageEntity;
import com.mogumogu.spring.UserEntity;
import com.mogumogu.spring.dto.MessageDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface MessageMapper {

    /**
     * Entity -> Dto
     */
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "article.id", target = "articleId")
    //@Mapping(source = "user.nickName", target = "nickName")
    @Mapping(target = "receiverId", ignore = true)
    @Mapping(target = "senderId", ignore = true)
    MessageDto.MessageResponseDto toResponseDto(MessageEntity messageEntity);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "userEntity")
    @Mapping(target = "article", source = "articleEntity")
    @Mapping(target = "content", source = "messageRequestDto.content")
    @Mapping(target = "sender", ignore = true)
    MessageEntity toRequestEntity(MessageDto.MessageRequestDto messageRequestDto, UserEntity userEntity, ArticleEntity articleEntity);


}
