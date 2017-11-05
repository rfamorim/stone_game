package com.rfamorim.stonegame;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/index.html")
public class IndexController {
    @RequestMapping(method = RequestMethod.GET)
    protected String gotoIndex(Model model) throws Exception {
        return "myLandingPage";
    }
}