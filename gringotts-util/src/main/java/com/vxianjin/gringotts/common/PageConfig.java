package com.vxianjin.gringotts.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 分页实体类
 *
 * @author gaoyuhai
 * @param <T>
 */

/**
 * 类描述：从OSS签移到api中
 * 创建人：董玉凯
 * 创建时间：2017年9月22日
 */
@SuppressWarnings("unchecked")
public class PageConfig<T> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * 当前页
     */
    private int currentPage = 1;
    /**
     * 每页数
     */
    private int pageSize = DEFAULT_PAGE_SIZE;
    /**
     * 记录总数
     */
    private int totalResultSize = 0;
    /**
     * 记录集
     */
    private List<T> items = new ArrayList<T>();

    private HashMap maps = new HashMap();

    private int totalPageNum = 1;

    private String url;

    private String totalQty;

    private String totalSett;

    private String totalProfit;

    public PageConfig(int totalResultSize) {
        this.totalResultSize = totalResultSize;
        this.pageSize = DEFAULT_PAGE_SIZE;
        this.currentPage = 1;
    }

    public PageConfig(int pageSize, int currentPage) {
        if (pageSize < 1)
            this.pageSize = DEFAULT_PAGE_SIZE;
        else
            this.pageSize = pageSize;
        this.currentPage = currentPage;
    }

    public PageConfig(int totalResultSize, int pageSize, int currentPage) {
        this.totalResultSize = totalResultSize;
        this.pageSize = pageSize;
        if (currentPage < 1)
            this.currentPage = 1;
        else if (currentPage > this.getTotalPages())
            this.currentPage = this.getTotalPages();
        else
            this.currentPage = currentPage;
        totalPageNum = getTotalPages();
    }

    public PageConfig() {
        super();
    }

    public static PageConfig top(int totalResultSize, int currentPage) {
        PageConfig pageConfig = new PageConfig(totalResultSize,
                DEFAULT_PAGE_SIZE, currentPage);
        return pageConfig;
    }

    /**
     * 设置PageConfig
     *
     * @param totalResultSize 记录总数
     * @param pageSize        每页数
     * @param currentPage     当前页
     * @return
     */
    public static PageConfig page(int totalResultSize, int pageSize,
                                  int currentPage) {
        return new PageConfig(totalResultSize, pageSize, currentPage);
    }

    public static PageConfig page(int pageSize, int currentPage) {
        return new PageConfig(pageSize, currentPage);
    }

    public String getTotalSett() {
        return totalSett;
    }

    public void setTotalSett(String totalSett) {
        this.totalSett = totalSett;
    }

    public String getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(String totalQty) {
        this.totalQty = totalQty;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        if (currentPage < 1)
            this.currentPage = 1;
        else if (currentPage > this.getTotalPages())
            this.currentPage = this.getTotalPages();
        else
            this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalResultSize() {
        return totalResultSize;
    }

    public void setTotalResultSize(int totalResultSize) {
        this.totalResultSize = totalResultSize;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public int getStartResult() {
        return (currentPage - 1) * pageSize;
    }

    public int getTotalPages() {
        if (this.totalResultSize % this.pageSize > 0)
            return this.totalResultSize / this.pageSize + 1;
        else
            return this.totalResultSize / this.pageSize;
    }

    public int getTotalPageNum() {
        return totalPageNum;
    }

    public void setTotalPageNum(int totalPageNum) {
        this.totalPageNum = totalPageNum;
    }

    /**
     * 重置记录数量总数
     *
     * @param page            每页数
     * @param totalResultSize 当前页
     * @return
     */
    public void resetTotalResultSize(PageConfig<T> pageConfig,
                                     int totalResultSize) {
        if (totalResultSize <= 0)
            return;
        pageConfig.setTotalResultSize(totalResultSize);
        int totoalNumCount = getTotalPages();
        pageConfig.setTotalPageNum(totoalNumCount);
        if (this.currentPage < 1)
            this.currentPage = 1;
        else if (this.currentPage > totoalNumCount)
            this.currentPage = totoalNumCount;
    }

    public HashMap getMaps() {
        return maps;
    }

    public void setMaps(HashMap maps) {
        this.maps = maps;
    }

    public String getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(String totalProfit) {
        this.totalProfit = totalProfit;
    }

}
