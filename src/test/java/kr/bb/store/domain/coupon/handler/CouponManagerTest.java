package kr.bb.store.domain.coupon.handler;

import kr.bb.store.domain.coupon.entity.Coupon;
import kr.bb.store.domain.coupon.exception.InvalidCouponDurationException;
import kr.bb.store.domain.coupon.exception.InvalidCouponStartDateException;
import kr.bb.store.domain.coupon.handler.dto.CouponDto;
import kr.bb.store.domain.coupon.repository.CouponRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class CouponManagerTest {
    @Autowired
    private CouponManager couponManager;
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private EntityManager em;

    @DisplayName("요청받은 내용으로 쿠폰 정보를 수정한다")
    @Test
    public void editCoupon() {
        // given
        Coupon coupon = couponCreator();
        Coupon savedCoupon = couponRepository.save(coupon);
        CouponDto couponDto = CouponDto.builder()
                .couponName("변경된 쿠폰이름")
                .discountPrice(99_999L)
                .minPrice(999_999L)
                .limitCount(999)
                .startDate(LocalDate.of(2023,11,25))
                .endDate(LocalDate.of(2023,11,25))
                .build();

        // when
        couponManager.edit(savedCoupon,couponDto);

        em.flush();
        em.clear();

        Coupon result = couponRepository.findById(savedCoupon.getId()).get();
        assertThat(result.getCouponName()).isEqualTo("변경된 쿠폰이름");
        assertThat(result.getDiscountPrice()).isEqualTo(99_999L);
        assertThat(result.getEndDate()).isEqualTo(LocalDate.of(2023,11,25));

    }
    @DisplayName("쿠폰 종료일은 시작일보다 빠른 날짜로 수정할 수 없다")
    @Test
    public void endDateMustComesAfterStartDate() {
        // given
        Coupon coupon = couponCreator();
        Coupon savedCoupon = couponRepository.save(coupon);

        LocalDate startDate = LocalDate.of(2023,12,15);
        LocalDate endDate = LocalDate.of(2023,12,13);
        CouponDto couponDto = CouponDto.builder()
                .couponName("변경된 쿠폰이름")
                .discountPrice(99_999L)
                .minPrice(999_999L)
                .limitCount(999)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        // when
        assertThatThrownBy(() ->
                couponManager.edit(savedCoupon, couponDto))
                .isInstanceOf(InvalidCouponDurationException.class)
                .hasMessage("시작일과 종료일이 올바르지 않습니다.");
    }

    @DisplayName("현재일보다 빠른 날짜로 쿠폰을 수정할 수 없다")
    @Test
    void startDateMustComesAfterNow() {
        // given
        Coupon coupon = couponCreator();
        Coupon savedCoupon = couponRepository.save(coupon);

        LocalDate now = LocalDate.now();
        LocalDate startDate = now.minusDays(1);
        LocalDate endDate = now.plusDays(100);
        CouponDto couponDto = CouponDto.builder()
                .couponName("변경된 쿠폰이름")
                .discountPrice(99_999L)
                .minPrice(999_999L)
                .limitCount(999)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        // when
        assertThatThrownBy(() ->
                couponManager.edit(savedCoupon, couponDto))
                .isInstanceOf(InvalidCouponStartDateException.class)
                .hasMessage("시작일이 올바르지 않습니다.");
    }

    private Coupon couponCreator() {
        return Coupon.builder()
                .couponCode("쿠폰코드")
                .storeId(1L)
                .limitCount(100)
                .couponName("쿠폰이름")
                .discountPrice(10000L)
                .minPrice(100000L)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now())
                .build();
    }
}