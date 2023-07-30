package org.astro.fileupload.mappers;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.astro.fileupload.dto.PageRequestDTO;
import org.astro.fileupload.dto.ProductDTO;
import org.astro.fileupload.dto.ProductListDTO;
import org.astro.fileupload.dto.ProductRegisterDTO;

public interface ProductMapper {
    
    List<ProductListDTO> getList(PageRequestDTO pageRequestDTO);

    int insertProduct(ProductRegisterDTO productRegisterDTO);

    // 람다식으로.

    int insertImages(List<Map<String,String>> imageList);

    @Select("select * from tbl_product p where p.pno = #{pno}")
    ProductDTO selectOne(Integer pno);

    @Select("select concat(uuid,'_',fileName) from tbl_product_image where pno=#{pno} order by ord ")
    List<String> selectImages(Integer pno);

    @Update("update tbl_product set pname=#{pname}, price=#{price}, status=#{status} where pno=#{pno}")
    int updateOne(ProductDTO productDTO);

    @Delete("delete from tbl_product_image where pno=#{pno}")
    int deleteImages(Integer pno);

}

