package mn.ezpay.controller;

import mn.ezpay.entity.providers;
import mn.ezpay.service.providerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Hashtable;
import java.util.List;

@RestController
public class providerController {

    @Autowired
    providerService service;

    @RequestMapping(value = "providers/save", method = RequestMethod.POST, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public providers save(@RequestBody providers entity) {
        service.save(entity);
        return entity;
    }

    @RequestMapping(value = "providers/delete", method = RequestMethod.DELETE)
    public providers delete(@RequestParam int id) {
        providers item = service.findOne(id);
        service.delete(item);
        return item;
    }

    @RequestMapping(value = "providers/update", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json; charset=utf-8", headers = "Accept=*/*")
    public providers update(@RequestBody providers entity) {
        service.update(entity);
        return entity;
    }

    @RequestMapping(value = "providers/findAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Hashtable findAll(@RequestParam int page, @RequestParam int size, @RequestParam String order, @RequestParam String dir) {
        List list = service.findAll(page, size, order, dir);
        Hashtable pageable = new Hashtable();
        pageable.put("total", service.total());
        pageable.put("data", list);
        return pageable;
    }

}
