package br.apolo.business.model;

import java.util.List;

import br.apolo.data.model.BaseEntity;

public class SearchResult<E extends BaseEntity> {
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
