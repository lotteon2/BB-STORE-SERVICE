package kr.bb.store.client;


import bloomingblooms.response.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url="${endpoint.user-service}")
public interface UserClient {
    @GetMapping("/client/users/{userId}/phone-number")
    CommonResponse<String> getPhoneNumber(@PathVariable(name = "userId") Long userId);

}
