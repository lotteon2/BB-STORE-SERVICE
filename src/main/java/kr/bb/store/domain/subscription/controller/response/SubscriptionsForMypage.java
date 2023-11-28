package kr.bb.store.domain.subscription.controller.response;

import kr.bb.store.domain.subscription.dto.SubscriptionForUserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionsForMypage {
    private List<SubscriptionForUserDto> data;
}