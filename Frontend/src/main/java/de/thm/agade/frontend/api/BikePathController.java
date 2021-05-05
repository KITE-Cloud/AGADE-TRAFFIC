package de.thm.agade.frontend.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import utils.MyController;
import wsdl.Location;

import javax.servlet.ServletContext;

@RestController
public class BikePathController {

    @Autowired
    ServletContext servletContext;

    @GetMapping({"/getBikePaths"})
    public String doGet(@RequestParam double minLat,
                        @RequestParam double minLon,
                        @RequestParam double maxLat,
                        @RequestParam double maxLon) {

        Location locMin = new Location();
        locMin.setLat(minLat);
        locMin.setLon(minLon);
        Location locMax = new Location();
        locMax.setLat(maxLat);
        locMax.setLon(maxLon);

        MyController controller = MyController.getInstance();
        String bikePaths = controller.getBikePathsInBoundingBox(locMin, locMax);
        return bikePaths;
    }
}
