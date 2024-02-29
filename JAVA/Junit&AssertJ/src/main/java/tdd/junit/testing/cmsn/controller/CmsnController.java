package tdd.junit.testing.cmsn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tdd.junit.testing.cmsn.service.CmsnService;

@Controller
@RequestMapping("/cmsn")
public class CmsnController {
    private CmsnService cmsnService = new CmsnService();
    @GetMapping("/main")
    @ResponseBody
    public String cmsnMain() {
        int cmsnResult = cmsnService.calcCmsn(2,3);
        return "Commission Main, calcCmsn result is " + String.valueOf(cmsnResult);
    }
}
