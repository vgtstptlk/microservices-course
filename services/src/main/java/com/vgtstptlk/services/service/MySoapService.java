package com.vgtstptlk.services.service;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService
public class MySoapService {

    @WebMethod
    @WebResult(name = "HelloMessage")
    public String hello(String name){
        return "Hello, " + (name != null ? name : "World") + "!";
    }
}
