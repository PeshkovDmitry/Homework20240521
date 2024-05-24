package ru.gb.shopservice.controller;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.gb.shopservice.aspect.TrackUserAction;
import ru.gb.shopservice.dto.ShopStatus;
import ru.gb.shopservice.dto.storage.ReserveRequest;
import ru.gb.shopservice.service.rest.ShopService;

@Controller
@AllArgsConstructor
@RequestMapping("/")
public class ShopController {

    private final ShopService shopService;

    private final Counter homePageAccesses = Metrics.counter("home_page_access_count");

    private final Counter payCounter = Metrics.counter("pay_count");

    @GetMapping
    @TrackUserAction
    public String showHomePage(Model model) {
        homePageAccesses.increment();
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
            payCounter.increment();
        } catch (Exception e) {}
        ShopStatus status = shopService.getStatus();
        model.addAttribute("amount", status.getUserAmount());
        model.addAttribute("purchases", status.getPurchases());
        model.addAttribute("inStorage", status.getInStorage());
        return "home";
    }




}
