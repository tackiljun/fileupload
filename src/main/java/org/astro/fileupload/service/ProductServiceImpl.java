package org.astro.fileupload.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.astro.fileupload.dto.PageRequestDTO;
import org.astro.fileupload.dto.PageResponseDTO;
import org.astro.fileupload.dto.ProductDTO;
import org.astro.fileupload.dto.ProductListDTO;
import org.astro.fileupload.dto.ProductRegisterDTO;
import org.astro.fileupload.mappers.ProductMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;


@Service
@Log4j2
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
/////////////////////////////////////////////////////////////////////////////
    private final ProductMapper productMapper;

/////////////////////////////////////////////////////////////////////////////    
    @Override
    public Integer register(ProductRegisterDTO dto) {

        //Integer pnoResult = null;
        List<String> fileNames = dto.getFileNames();

        int count = productMapper.insertProduct(dto);

        log.info("insert product count: " + count);

        Integer pno = dto.getPno();

        log.info("=========================" + pno);

        AtomicInteger index = new AtomicInteger();

        List<Map<String,String>> list = fileNames.stream().map(str -> {
            String uuid = str.substring(0, 36);
            String fileName = str.substring(37);

            log.info(uuid);

            return Map.of(
                "uuid", uuid,
                "fileName", fileName,
                "pno", "" + pno, 
                "ord", "" + index.getAndIncrement());

        }).collect(Collectors.toList());

        log.info(list);

        int countImages = productMapper.insertImages(list);

        log.info("=========================" + countImages);

        return pno;
    }

/////////////////////////////////////////////////////////////////////////////
    @Override
    public PageResponseDTO<ProductListDTO> list(PageRequestDTO pageRequestDTO) {
        
        List<ProductListDTO> result = productMapper.getList(pageRequestDTO);

        long total = 110L;

        return PageResponseDTO.<ProductListDTO>withAll()
                             // 중간에 있는 .은 그냥 문법.
        .list(result)
        .total(total)
        .build();
    }

/////////////////////////////////////////////////////////////////////////////
    @Override
    public ProductDTO get(Integer pno) {
        
        return productMapper.selectOne(pno);
    }

/////////////////////////////////////////////////////////////////////////////
    @Override
    public List<String> getImage(Integer pno) {
        
        return productMapper.selectImages(pno);
    }
    
/////////////////////////////////////////////////////////////////////////////
    @Override
    public void modify(ProductDTO dto) {
        
        // 상품 수정.
        productMapper.updateOne(dto);

        // 기존 이미지 삭제.
        productMapper.deleteImages(dto.getPno());

        // 신규 이미지 추가.

        List<String> fileNames = dto.getFileNames();

        Integer pno = dto.getPno();

        log.info("=========================" + pno);

        AtomicInteger index = new AtomicInteger();

        List<Map<String,String>> list = fileNames.stream().map(str -> {
            String uuid = str.substring(0, 36);
            String fileName = str.substring(37);

            log.info(uuid);

            return Map.of(
                "uuid", uuid, 
                "fileName", fileName,
                "pno", "" + pno, 
                "ord", "" + index.getAndIncrement());

        }).collect(Collectors.toList());

        log.info(list);

        int countImages = productMapper.insertImages(list);

        log.info("countImages: "+countImages);
    }
}
