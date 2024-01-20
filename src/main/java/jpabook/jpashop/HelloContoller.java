package jpabook.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloContoller {

    @GetMapping("hello")
    public String hello(Model model){
        model.addAttribute("data", "hello!!");
        //data라는 key로 hello를 model로 넘기겠다. 
        return "hello"; //화면 이름
    }
}
