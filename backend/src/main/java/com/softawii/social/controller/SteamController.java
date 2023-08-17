package com.softawii.social.controller;

import com.softawii.social.service.SteamService;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("steam")
// https://github.com/xPaw/SteamOpenID.php/blob/master/src/SteamOpenID.php
public class SteamController {

    private final SteamService service;

    public SteamController(SteamService service) {
        this.service = service;
    }

    @GetMapping("login")
    public ModelAndView login() {
        Map<String, String> model = new HashMap<>();
        model.put("openid_realm", "http://localhost:8099/");
        model.put("openid_return_to", "http://localhost:8099/steam/login/redirect");

        return new ModelAndView("steam", model);
    }

    @GetMapping("login/redirect")
    public ModelAndView loginRedirect(@RequestParam Map<String, String> allRequestParams) {
        // TODO: VALIDATE DATA
        MultiValueMap<String, String> openidRequest = new LinkedMultiValueMap<>();
        openidRequest.add("openid_ns", allRequestParams.get("openid.ns"));
        openidRequest.add("openid_op_endpoint", allRequestParams.get("openid.op_endpoint"));
        openidRequest.add("openid_claimed_id", allRequestParams.get("openid.claimed_id"));
        openidRequest.add("openid_identity", allRequestParams.get("openid.identity"));
        openidRequest.add("openid_return_to", allRequestParams.get("openid.return_to"));
        openidRequest.add("openid_response_nonce", allRequestParams.get("openid.response_nonce"));
        openidRequest.add("openid_assoc_handle", allRequestParams.get("openid.assoc_handle"));
        openidRequest.add("openid_signed", allRequestParams.get("openid.signed"));
        openidRequest.add("openid_sig", allRequestParams.get("openid.sig"));
        openidRequest.add("openid.mode", "check_authentication");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request      = new HttpEntity<>(openidRequest, headers);
        RestTemplate                              restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.postForEntity("https://steamcommunity.com/openid/login", request, String.class);
        if (!response.getStatusCode().isSameCodeAs(HttpStatus.OK)) {
            return new ModelAndView("redirect:/steam/failed");
        }

        Map<String, String> body = new HashMap<>();
        for (String line : response.getBody().split("\n")) {
            if (line.isEmpty()) continue;
            String[] values = line.split(":", 2);
            String   key    = values[0];
            String   value  = values[1];

            body.put(key, value);
        }

        if (!body.get("ns").equals("http://specs.openid.net/auth/2.0")) {
            return new ModelAndView("redirect:/steam/failed");
        }

        if (!body.get("is_valid").equals("true")) {
            return new ModelAndView("redirect:/steam/failed");
        }

        Pattern p = Pattern.compile("^https?://steamcommunity.com/openid/id/(7656119\\d{10})/?$");
        Matcher m = p.matcher(allRequestParams.get("openid.identity"));

        if (!m.find()) {
            return new ModelAndView("redirect:/steam/failed");
        }

        String steamUserId = m.group(1);
        try {
            Map<String, Object> userData = service.getUserData(steamUserId);
            System.out.println(userData);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ModelAndView("redirect:/steam/success");
    }

    @GetMapping("success")
    public String success() {
        return "success";
    }

    @GetMapping("failed")
    public String failed() {
        return "failed";
    }
}
