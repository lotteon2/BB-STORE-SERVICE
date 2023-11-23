package kr.bb.store.domain.cargo.service;


import kr.bb.store.domain.cargo.dto.StockModifyDto;
import kr.bb.store.domain.cargo.entity.FlowerCargo;
import kr.bb.store.domain.cargo.entity.FlowerCargoId;
import kr.bb.store.domain.cargo.repository.FlowerCargoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CargoService {

    private final FlowerCargoRepository flowerCargoRepository;

    @Transactional
    public void modifyStock(Long storeId, List<StockModifyDto> stockModifyDtos) {
        stockModifyDtos.forEach(stockModifyDto -> {
            FlowerCargoId flowerCargoId = makeKeys(storeId, stockModifyDto.getFlowerId());
            FlowerCargo flowerCargo = flowerCargoRepository.findById(flowerCargoId).orElseThrow();
            flowerCargo.updateStock(stockModifyDto.getStock());
        });
    }



    private FlowerCargoId makeKeys(Long storeId, Long flowerId) {
        return FlowerCargoId.builder()
                .storeId(storeId)
                .flowerId(flowerId)
                .build();
    }
}