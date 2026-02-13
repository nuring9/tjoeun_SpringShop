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
    // GET /thymeleaf/ex03 요청이 오면 아래 메서드 실행
    public String thymeleafExample03(Model model){ // 뷰로 데이터 넘기기 위해 Model 사용
        List<ItemDto> itemDtoList = new ArrayList<>(); // ItemDto들을 담을 리스트 생성

        for(int i = 1; i<=10; i++){  // 1부터 10까지 10개의 더미(예제) 데이터 만들기
            ItemDto itemDto = new ItemDto();  // DTO 객체 1개 생성
            itemDto.setItemDetail("상품 상세 설명"+i);   // 상세설명 세팅
            itemDto.setItemNm("테스트 상품"+i); // 상품명 세팅
            itemDto.setPrice(1000*i);  // 가격 세팅
            itemDto.setRegTime(LocalDateTime.now());  // 등록일시를 현재 시간으로 세팅
            itemDtoList.add(itemDto);  // 리스트에 DTO 추가
        }
        model.addAttribute("itemDtoList", itemDtoList);
        // 뷰에서 ${itemDtoList} 로 리스트 접근 가능하게 전달
        return "thymeleafEx/thymeleafEx03";
        // templates/thymeleafEx/thymeleafEx03.html 화면 반환
    }


    @GetMapping(value = "/ex04")
    public String thymeleafExample04(Model model){
        List<ItemDto> itemDtoList = new ArrayList<>();

        for(int i = 1; i<=10; i++){  // 1부터 10까지 10개의 더미(예제) 데이터 만들기
            ItemDto itemDto = new ItemDto();  // DTO 객체 1개 생성
            itemDto.setItemDetail("상품 상세 설명"+i);   // 상세설명 세팅
            itemDto.setItemNm("테스트 상품"+i); // 상품명 세팅
            itemDto.setPrice(1000*i);  // 가격 세팅
            itemDto.setRegTime(LocalDateTime.now());  // 등록일시를 현재 시간으로 세팅

            itemDtoList.add(itemDto);  // 리스트에 DTO 추가
        }
        model.addAttribute("itemDtoList", itemDtoList);
        return "thymeleafEx/thymeleafEx04";
    }

    @GetMapping(value = "/ex05")
    public String thymeleafExample05(Model model){
        return "thymeleafEx/thymeleafEx05";
    }

    @GetMapping(value = "/ex06")
    public String thymeleafExample06(String param1, String param2, Model model){
        model.addAttribute("param1", param1);
        model.addAttribute("param2", param2);
        return "thymeleafEx/thymeleafEx06";
    }

    @GetMapping(value = "/ex07")
    public String thymeleafExample07(){
        return "thymeleafEx/thymeleafEx07";
    }
}
