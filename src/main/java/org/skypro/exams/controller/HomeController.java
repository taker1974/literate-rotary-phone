// SkyPro
// Терских Константин, kostus.online.1974@yandex.ru, 2024
// Не имеет прямого отношения к курсовой работе. Просто изучение шаблонизатора Thymeleaf.

package org.skypro.exams.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Тестовый контроллер.
 *
 * @author Константин Терских, kostus.online.1974@yandex.ru, 2024
 * @version 1.1
 */
@Controller
public class HomeController {

    public HomeController() {
        // ...
    }

    @RequestMapping("/exam/home/{color}")
    public String home(@PathVariable String color, Model page) {
        page.addAttribute("username", "Kostus");
        page.addAttribute("color", color);
        return "home.html";
    }
}
