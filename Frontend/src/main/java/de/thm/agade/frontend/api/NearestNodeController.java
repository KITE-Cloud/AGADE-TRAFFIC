package de.thm.agade.frontend.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import utils.MyController;
import wsdl.Location;

import javax.servlet.ServletContext;

@RestController
public class NearestNodeController {

    @Autowired
    ServletContext servletContext;

    @GetMapping({"/getNearest"})
    public Location doGet(@RequestParam double lat,
                          @RequestParam double lon) {

        Location loc = new Location();
        loc.setLat(lat);
        loc.setLon(lon);

        MyController controller = MyController.getInstance();
        Location nearest = controller.findNearest(loc);

        return nearest;
    }
}
