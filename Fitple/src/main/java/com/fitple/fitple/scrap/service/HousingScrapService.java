package com.fitple.fitple.scrap.service;

import com.fitple.fitple.base.user.domain.User;
import com.fitple.fitple.scrap.dto.HousingScrapDTO;
import com.fitple.fitple.scrap.dto.HousingScrapRequest;
import java.util.List;

public interface HousingScrapService {

    void addScrap(Long userId, HousingScrapRequest requestDto);
    void removeScrap(Long userId, Long propertyId);
    List<Long> getScrapPropertyIds(Long userId);
    List<HousingScrapDTO> getScrapList(User user);
}