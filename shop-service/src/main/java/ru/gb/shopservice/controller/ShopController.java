package ru.gb.shopservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.gb.shopservice.aspect.TrackUserAction;
import ru.gb.shopservice.dto.ShopStatus;
import ru.gb.shopservice.dto.storage.ReserveRequest;
import ru.gb.shopservice.service.ShopService;

@Controller
@AllArgsConstructor
@RequestMapping("/")
public class ShopController {

    private final ShopService shopService;

    @GetMapping
    @TrackUserAction
    public String showHomePage(Model model) {
        ShopStatus status = shopService.getStatus();
        model.addAttribute("amount", status.getUserAmount());
        model.addAttribute("purchases", status.getPurchases());
        model.addAttribute("inStorage", status.getInStorage());
        return "home";
    }

    @PostMapping
    public String pay(ReserveRequest request, Model model) {
        try {
            shopService.buy(request.getId(), request.getCount());
        } catch (Exception e) {}
//        return "redirect:html://localhost/shop-service";
        ShopStatus status = shopService.getStatus();
        model.addAttribute("amount", status.getUserAmount());
        model.addAttribute("purchases", status.getPurchases());
        model.addAttribute("inStorage", status.getInStorage());
        return "home";
    }




}
