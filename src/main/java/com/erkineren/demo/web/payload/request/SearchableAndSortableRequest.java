package com.erkineren.demo.web.payload.request;

import com.erkineren.demo.persistence.specification.SearchCriteria;
import com.erkineren.demo.utils.AppConstants;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class SearchableAndSortableRequest {
    private Integer page = 0;
    private Integer size = 10;
    private String search;

    @Value(AppConstants.DEFAULT_PAGE_NUMBER)
    private String sorting = "id:desc";

    public List<SearchCriteria> prepareSearchCriteriaList() {
        List<SearchCriteria> params = new ArrayList<>();

        if (search != null) {
            Pattern pattern = Pattern.compile("(\\w|.+?)(:|<|>)(\\w|.+?),");
            Matcher matcher = pattern.matcher(search + ",");
            while (matcher.find()) {
                params.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
            }
        }

        return params;
    }

    public String getField() {
        if (sorting != null) {
            String[] parts = sorting.split(":");
            return parts[0];
        }
        return "id";
    }

    public Sort.Direction getDirection() {
        String direction = "ASC";
        if (sorting != null) {
            String[] parts = sorting.split(":");
            if (parts.length > 1) {
                direction = parts[1];
            }
        }
        return Sort.Direction.fromString(direction);
    }

}
