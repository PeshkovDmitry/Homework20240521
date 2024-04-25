package ru.gb.shopservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.gb.shopservice.dto.storage.ReserveRequest;
import ru.gb.shopservice.dto.ShopStatus;
import ru.gb.shopservice.service.ShopService;
import org.springframework.ui.Model;

@Controller
@AllArgsConstructor
@RequestMapping("/")
public class ShopController {

    private final ShopService shopService;

    @GetMapping
    public String showHomePage(Model model) {
        ShopStatus status = shopService.getStatus();
        model.addAttribute("amount", status.getUserAmount());
        model.addAttribute("purchases", status.getPurchases());
        model.addAttribute("inStorage", status.getInStorage());
        return "home";
    }

    @PostMapping
    public String pay(ReserveRequest request) {
        try {
            shopService.buy(request.getId(), request.getCount());
        } catch (Exception e) {}
        return "redirect:html://localhost/shop-service";

    }




}
