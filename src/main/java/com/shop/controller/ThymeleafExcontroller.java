package com.shop.controller;

import com.shop.dto.ItemDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller  // 이 클래스 컨트롤러라고 정의
@RequestMapping(value = "/thymeleaf") // 이 컨트롤러 들어오려면 url에 이 주소로 들어가야 함.
public class ThymeleafExcontroller {
    // /thymeleaf/ex01
    @GetMapping(value = "/ex01")
    public String thymeleafExample01(Model model){ // 매개변수 Model를 넣었다는건 View를 호출할 때 데이터를 올리겠다.
        model.addAttribute("data", "타임리프 예제 입니다.");
        // UI 모델 속성 data : 타임리프 예제 입니다.
        return "thymeleafEx/thymeleafEx01";
        // HTML을 부름.
    }

    @GetMapping(value = "/ex02")
    //   /thymeleaf/ex02 로 GET 요청 오면 이 메서드 실행됨.
    //   (위에 @RequestMapping("/thymeleaf") 있어서)
    public String thymeleafExample02(Model model){
        // View(HTML)에 데이터를 넘기기 위해 Model 사용

        ItemDto itemDto = new ItemDto();
        //DTO 객체 생성, 면에 보낼 데이터 객체 생성

        itemDto.setItemDetail("상품 상세 설명");
        itemDto.setItemNm("테스트 상품1");
        itemDto.setPrice(10000);
        itemDto.setRegTime(LocalDateTime.now());
        // 값 세팅

        model.addAttribute("itemDto", itemDto);
        // View에서 ${itemDto} 로 접근 가능, 변수로 생각하면 됨. 내맘대로 정함.

        return "thymeleafEx/thymeleafEx02";
        // templates/thymeleafEx/thymeleafEx02.html 실행
    }

    @GetMapping(value = "/ex03")
    public String thymeleafExample03(Model model){
        List<ItemDto> itemDtoList = new ArrayList<>();

        for(int i = 1; i<=10; i++){
            ItemDto itemDto = new ItemDto();
            itemDto.setItemDetail("상품 상세 설명"+i);
            itemDto.setItemNm("테스트 상품"+i);
            itemDto.setPrice(1000*i);
            itemDto.setRegTime(LocalDateTime.now());
            itemDtoList.add(itemDto);
        }
        model.addAttribute("itemDtoList", itemDtoList);
        return "thymeleafEx/thymeleafEx03";
    }
}
