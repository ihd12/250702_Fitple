package com.fitple.fitple.base.user.repository.dsl;

import com.fitple.fitple.base.user.domain.QUser;
import com.fitple.fitple.base.user.domain.User;
import com.fitple.fitple.base.user.dto.UserDTO;
import com.fitple.fitple.common.dto.PageRequestDTO;
import com.fitple.fitple.common.dto.PageResponseDTO;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class UserDslRepositoryImpl extends QuerydslRepositorySupport implements UserDslRepository {
    public UserDslRepositoryImpl() {super(User.class);}
    @Override
    public PageResponseDTO<UserDTO> searchUser(PageRequestDTO pageRequestDTO) {
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("id");
        QUser qUser = QUser.user;
        JPQLQuery<User> query = from(qUser);
        if(keyword!=null && !keyword.isEmpty()){
            query.where(qUser.email.contains(keyword));
        }
        int totalCount = Long.valueOf(query.fetchCount()).intValue();
        this.getQuerydsl().applyPagination(pageable,query);
        List<User> list = query.fetch();
        return PageResponseDTO.<UserDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(list.stream().map(UserDTO::toDTO).toList())
                .total(totalCount)
                .build();
    }
}
