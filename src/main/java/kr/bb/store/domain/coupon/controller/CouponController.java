package kr.bb.store.domain.coupon.controller;

import kr.bb.store.domain.coupon.controller.request.CouponCreateRequest;
import kr.bb.store.domain.coupon.controller.request.CouponEditRequest;
import kr.bb.store.domain.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class CouponController {
    private final CouponService couponService;

    @PostMapping("/{storeId}/coupons")
    public ResponseEntity createCoupon(@PathVariable Long storeId,
                                       @RequestBody CouponCreateRequest couponCreateRequest) {
        couponService.createCoupon(storeId, couponCreateRequest);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{storeId}/coupons/{couponId}")
    public ResponseEntity editCoupon(@PathVariable Long storeId, @PathVariable Long couponId,
                                     @RequestBody CouponEditRequest couponEditRequest) {
        couponService.editCoupon(storeId, couponId, couponEditRequest);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{storeId}/coupons/{couponId}")
    public ResponseEntity deleteCoupon(@PathVariable Long storeId, @PathVariable Long couponId) {
        couponService.softDeleteCoupon(storeId, couponId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{storeId}/coupons")
    public ResponseEntity coupons(@PathVariable Long storeId) {
        return ResponseEntity.ok().body(couponService.getAllStoreCoupons(storeId));
    }

    @PostMapping("/coupons/{couponId}")
    public ResponseEntity downloadCoupon(@PathVariable Long couponId,
                                         @RequestHeader(value = "userId") Long userId) {
        couponService.downloadCoupon(userId, couponId, LocalDate.now());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{storeId}/coupons/all")
    public ResponseEntity downloadAllCoupons(@PathVariable Long storeId,
                                             @RequestHeader(value = "userId") Long userId) {
        couponService.downloadAllCoupons(userId, storeId, LocalDate.now());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{storeId}/coupons/product")
    public ResponseEntity storeCouponsForUser(@PathVariable Long storeId,
                                              @RequestHeader(value = "userId") Long userId) {

        return ResponseEntity.ok().body(couponService.getAllStoreCouponsForUser(userId, storeId));
    }

    @GetMapping("/{storeId}/coupons/my")
    public ResponseEntity couponsInPaymentStep(@PathVariable Long storeId,
                                               @RequestHeader(value = "userId") Long userId) {

        return ResponseEntity.ok().body(couponService.getAvailableCouponsInPayment(userId, storeId));
    }

    @GetMapping("/coupons/list")
    public ResponseEntity myCoupons(@RequestHeader(value = "userId") Long userId) {

        return ResponseEntity.ok().body(couponService.getMyValidCoupons(userId));
    }
}
