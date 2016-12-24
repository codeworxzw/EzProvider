package mn.ezpay.controller;

import mn.ezpay.entity.merchant;
import mn.ezpay.service.merchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Hashtable;
import java.util.List;

@RestController
public class merchantController {

    @Autowired
    merchantService service;

    @RequestMapping(value = "merchant/save", method = RequestMethod.POST, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public merchant save(@RequestBody merchant entity) {
        service.save(entity);
        return entity;
    }

    @RequestMapping(value = "merchant/delete", method = RequestMethod.DELETE)
    public merchant delete(@RequestParam int id) {
        merchant item = service.findOne(id);
        service.delete(item);
        return item;
    }

    @RequestMapping(value = "merchant/update", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json; charset=utf-8", headers = "Accept=*/*")
    public merchant update(@RequestBody merchant entity) {
        service.update(entity);
        return entity;
    }

    @RequestMapping(value = "merchant/findAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Hashtable findAll(@RequestParam int page, @RequestParam int size, @RequestParam String order, @RequestParam String dir) {
        List list = service.findAll(page, size, order, dir);
        Hashtable pageable = new Hashtable();
        pageable.put("total", service.total());
        pageable.put("data", list);
        return pageable;
    }

}
