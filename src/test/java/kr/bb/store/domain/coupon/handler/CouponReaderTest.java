package kr.bb.store.domain.coupon.handler;

import kr.bb.store.domain.coupon.dto.CouponForOwnerDto;
import kr.bb.store.domain.coupon.entity.Coupon;
import kr.bb.store.domain.coupon.entity.IssuedCoupon;
import kr.bb.store.domain.coupon.entity.IssuedCouponId;
import kr.bb.store.domain.coupon.repository.CouponRepository;
import kr.bb.store.domain.coupon.repository.IssuedCouponRepository;
import kr.bb.store.domain.store.entity.Store;
import kr.bb.store.domain.store.repository.StoreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CouponReaderTest {
    @Autowired
    private CouponReader couponReader;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private IssuedCouponRepository issuedCouponRepository;



    @DisplayName("가게 사장에게 보여줄 쿠폰 정보를 조회한다")
    @Test
    public void readCouponsForOwner() {
        // given
        Store store = createStore();
        storeRepository.save(store);
        Coupon c1 = createCoupon(store);
        Coupon c2 = createCoupon(store);
        couponRepository.saveAll(List.of(c1,c2));

        // when
        List<CouponForOwnerDto> result = couponReader.readCouponsForOwner(store.getId());

        // then
        assertThat(result).hasSize(2);

    }

    @DisplayName("쿠폰이 발급되면 가게사장이 보는 쿠폰 정보에도 차감된 개수가 전달된다")
    @Test
    public void unUsedCountWillDecreaseWhenUserIssueCoupon() {
        // given
        Store store = createStore();
        storeRepository.save(store);
        Coupon c1 = createCoupon(store);
        couponRepository.save(c1);

        Long userId = 1L;
        issuedCouponRepository.save(createIssuedCoupon(c1, userId));

        // when
        List<CouponForOwnerDto> result = couponReader.readCouponsForOwner(store.getId());

        // then
        assertThat(result.get(0).getUnusedCount()).isEqualTo(99);

    }


    private IssuedCoupon createIssuedCoupon(Coupon coupon, Long userId) {
        return IssuedCoupon.builder()
                .id(createIssuedCouponId(coupon.getId(),userId))
                .coupon(coupon)
                .build();
    }

    private IssuedCouponId createIssuedCouponId(Long couponId, Long userId) {
        return IssuedCouponId.builder()
                .couponId(couponId)
                .userId(userId)
                .build();
    }

    private Store createStore() {
        return Store.builder()
                .storeManagerId(1L)
                .storeCode("가게코드")
                .storeName("가게")
                .detailInfo("가게 상세정보")
                .storeThumbnailImage("가게 썸네일")
                .phoneNumber("가게 전화번호")
                .accountNumber("가게 계좌정보")
                .bank("가게 계좌 은행정보")
                .build();
    }

    private Coupon createCoupon(Store store) {
        return Coupon.builder()
                .couponCode("쿠폰코드")
                .store(store)
                .limitCount(100)
                .couponName("쿠폰이름")
                .discountPrice(10000L)
                .minPrice(100000L)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now())
                .build();
    }

}