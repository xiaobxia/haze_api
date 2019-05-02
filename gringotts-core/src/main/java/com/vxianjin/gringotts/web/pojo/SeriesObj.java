package com.vxianjin.gringotts.web.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * @author fanyinchuan
 */
public class SeriesObj implements Serializable {

    private static final long serialVersionUID = -1945755582578436051L;
    String name;
    String yAxis;
    String xAxis;
    List data;
    List category;

    public SeriesObj() {
        super();
    }

    public SeriesObj(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getyAxis() {
        return yAxis;
    }

    public void setyAxis(String yAxis) {
        this.yAxis = yAxis;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }

    public String getxAxis() {
        return xAxis;
    }

    public void setxAxis(String xAxis) {
        this.xAxis = xAxis;
    }

    public List getCategory() {
        return category;
    }

    public void setCategory(List category) {
        this.category = category;
    }
}
