package com.shop.controller;

import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor  // final 필드나 @NonNull이 붙은 필드만을 포함하는 생성자를 자동으로 만들어줌. 즉, 생성자 자동 생성 + 생성자 주입 방식 DI
// @RequiredArgsConstructor과 final을 붙이면 @Autowired 없이도 자동으로 생성자 주입됨.

//  @Autowired는 1. 객체 생성 -> 아직 의존성 없음 (null 상태) -> 그 다음에 주입 이기때문에 객체가 생성되는 순간에는 아직 의존성이 없다.
//  2.불변 객체를 만들 수 없음 -> 안정성 ↓ / 3.단위 테스트할 때 직접 객체 생성이 어려움.
// @Autowired 는 생성자가 여러개 일때 사용 또는 setter 주입할 때.
public class MainController {
    private final ItemService itemService;
  @GetMapping(value = "/")
    public String main(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model){
      Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
      Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);
      model.addAttribute("items", items);
      model.addAttribute("itemSearchDto", itemSearchDto);
      model.addAttribute("maxPage", 5);
      return "main";
  }
}
