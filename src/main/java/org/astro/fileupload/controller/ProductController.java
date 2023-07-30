package org.astro.fileupload.controller;

import java.util.List;

import org.astro.fileupload.dto.PageRequestDTO;
import org.astro.fileupload.dto.ProductDTO;
import org.astro.fileupload.dto.ProductRegisterDTO;
import org.astro.fileupload.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;


@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
//////////////////////////////////////////////////////////////////////////////////////////
    private final ProductService productService;

//////////////////////////////////////////////////////////////////////////////////////////
    // step1 get방식으로 조회.
    @GetMapping("/modify/{pno}")
    public String modifyGet(
        @PathVariable("pno") Integer pno, PageRequestDTO pageRequestDTO, Model model) {

        ProductDTO dto = productService.get(pno);

        model.addAttribute("dto", dto);

        // 서비스에서 상품조회. Model에 담아준다.
        // todo작업을 생각해보자. -> 상품 조회 기능이 없다. -> DTO, Mapper, 서비스필요.
        return "/product/modify";
    }

    @GetMapping("/images/{pno}")
    @ResponseBody
    public List<String> getImages(@PathVariable("pno") Integer pno) {

        return productService.getImage(pno);
    }

    // // step3. post로 상품데이터 수정.
     @PostMapping("/modify/{pno}")
     public String modifyPost(@PathVariable("pno") Integer pno, ProductDTO dto) {
         // DTO를 확인 -> 등록과정과 거의 동일한 내용. 다만 pno값이 존재함.
         // todo는 뭐가 있을까? - DTO를 개발.

         dto.setPno(pno);

         productService.modify(dto);

         return "redirect:/product/read/"+pno;
     }

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model) {

        model.addAttribute("res", 
            productService.list(pageRequestDTO));
    }

    @GetMapping("/register")
    public void register() {
        log.info("get product register");
    }

    @PostMapping("/register")
    public String resisterPost(ProductRegisterDTO registerDTO, RedirectAttributes rttr) {

        log.info("====================");
        log.info(registerDTO);

        Integer pno = productService.register(registerDTO);

        log.info("NEW PNO: " + pno);

        rttr.addFlashAttribute("result", pno);
        // add 뒤 Flash가 없으면 쿼리스트링으로 따라간다.

        return "redirect:/product/list";
    }
    
}
