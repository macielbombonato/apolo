package apolo.business.model;

import apolo.data.model.base.BaseEntity;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("rawtypes")
public class SearchResult<E extends BaseEntity> implements Serializable {

	private static final long serialVersionUID = 4372321043136915950L;

	private List<E> results;

	private long pageCount;

	public List<E> getResults() {
		return this.results;
	}

	public void setResults(List<E> results) {
		this.results = results;
	}

	public long getPageCount() {
		return this.pageCount;
	}

	public void setPageCount(long pageCount) {
		this.pageCount = pageCount;
	}
}
