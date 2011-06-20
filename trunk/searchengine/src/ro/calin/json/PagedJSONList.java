package ro.calin.json;

import static ro.calin.Constants.JSON_RESULTS;
import static ro.calin.Constants.JSON_TOTAL_PAGES;

import java.util.ArrayList;

public class PagedJSONList<E extends JSONable> extends ArrayList<E> implements JSONable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6843594300166044117L;

	private int totalPages;

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	@Override
	public String toJSONString() {
		StringBuilder sb = new StringBuilder();

		sb.append('{');
		sb.append('"');
		sb.append(JSON_TOTAL_PAGES);
		sb.append('"');
		sb.append(':');
		sb.append(totalPages);

		sb.append(',');

		sb.append('"');
		sb.append(JSON_RESULTS);
		sb.append('"');
		sb.append(':');
		sb.append('[');
		for (int i = 0; i < size(); i++) {
			sb.append(get(i).toJSONString());

			if (i < size() - 1) {
				sb.append(',');
			}
		}
		sb.append(']');

		sb.append('}');

		return sb.toString();
	}
	
	@Override
	public String toString() {
		return toJSONString();
	}
}
