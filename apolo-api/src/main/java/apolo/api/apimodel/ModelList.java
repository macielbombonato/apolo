package apolo.api.apimodel;

import apolo.api.apimodel.base.BaseAPIModel;

import java.util.List;

public class ModelList<E extends Object> extends BaseAPIModel {

    private long totalPages;

    private long totalElements;

    private List<E> list;

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public List<E> getList() {
        return list;
    }

    public void setList(List<E> list) {
        this.list = list;
    }
}
