/*
 * Created by Sait Tuna Onder on 2018.03.29  * 
 * Copyright © 2018 Sait Tuna Onder. All rights reserved. * 
 */
package vt.trafficsimulator.jsfclasses;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author Onder
 */
@Named(value = "imagesView")
//It is request scoped, objects will be alive only for a request
@RequestScoped
public class ImagesView {

    private List<String> images;
     
    @PostConstruct
    public void init() {
        images = new ArrayList<String>();
        for (int i = 1; i <= 2; i++) {
            images.add("simulation" + i + ".gif");
        }
    }
 
    public List<String> getImages() {
        return images;
    }
}
