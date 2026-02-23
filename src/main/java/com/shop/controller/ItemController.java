package com.shop.controller;

import com.shop.dto.ItemFormDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class ItemController {

    // 상품 등록 화면
    @GetMapping(value = "/admin/item/new")
    public String itemForm(Model model) {
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "/item/itemForm";
    }

    // 상품 등록 처리 (이미지 포함 확인)
    @PostMapping(value = "/admin/item/new")
    public String itemNew(ItemFormDto itemFormDto,
                          @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList,
                          Model model) {

        // ===== 일반 입력값 확인 =====
        System.out.println("===== 상품 등록 요청 데이터 =====");
        System.out.println("상품명 : " + itemFormDto.getItemNm());
        System.out.println("가격 : " + itemFormDto.getPrice());
        System.out.println("재고 : " + itemFormDto.getStockNumber());
        System.out.println("상세 : " + itemFormDto.getItemDetail());
        System.out.println("판매상태 : " + itemFormDto.getItemSellStatus());
        System.out.println("================================");

        // ===== 이미지 확인 =====
        if (itemImgFileList != null) {
            for (int i = 0; i < itemImgFileList.size(); i++) {
                MultipartFile file = itemImgFileList.get(i);

                if (!file.isEmpty()) {
                    System.out.println("이미지 " + (i + 1) + " 파일명 : " + file.getOriginalFilename());
                    System.out.println("이미지 " + (i + 1) + " 파일크기 : " + file.getSize());
                }
            }
        }

        model.addAttribute("itemFormDto", itemFormDto);
        return "/item/itemForm";
    }
}