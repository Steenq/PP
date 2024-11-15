package com.example.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

@Controller
public class GameController {

    @GetMapping("/")
    public String home() {
        return "index"; // Spring Boot и Thymeleaf автоматически найдут index.html в папке templates
    }


    @GetMapping("/games")
    public String gamesMenu() {
        return "Выберите игру: /game1, /game2 и т.д.";
    }

    @PostMapping("/game1/start")
    public String startGame1() {
        try {
            Runtime.getRuntime().exec("path/to/game1.exe");
            return "Игра 1 запущена!";
        } catch (IOException e) {
            e.printStackTrace();
            return "Ошибка запуска игры 1.";
        }
    }

    @PostMapping("/game2/start")
    public String startGame2() {
        try {
            Runtime.getRuntime().exec("path/to/game2.exe");
            return "Игра 2 запущена!";
        } catch (IOException e) {
            e.printStackTrace();
            return "Ошибка запуска игры 2.";
        }
    }
}
