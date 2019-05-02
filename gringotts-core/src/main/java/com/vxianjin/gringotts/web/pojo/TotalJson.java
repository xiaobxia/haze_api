package com.vxianjin.gringotts.web.pojo;

import java.util.List;

/**
 * @author fanyinchuan
 */
public class TotalJson {
    private List<SeriesObj> seriesObj;
    private List<String> categories;

    public TotalJson() {
        super();
    }

    public List<SeriesObj> getSeriesObj() {
        return seriesObj;
    }

    public void setSeriesObj(List<SeriesObj> seriesObj) {
        this.seriesObj = seriesObj;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

}
