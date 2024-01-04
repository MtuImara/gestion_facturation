package backend.rest_api.gestion_facturation.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

public class PagingAndSortingHelper {

    public static Pageable pagination(String[] sort, int page, int size) {
        List<Order> orders = new ArrayList<>();
        if (sort[0].contains(",")) {
            for (String sortOrder : sort) {
                String[] _sort = sortOrder.split(",");
                orders.add(new Order(getSortDirection(_sort[1]), _sort[0]));
            }
        } else {
            orders.add(new Order(getSortDirection(sort[1]), sort[0]));
        }

        Pageable pagingSort = PageRequest.of(page, size, Sort.by(orders));
        return pagingSort;
    }

    private static Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }
        return Sort.Direction.ASC;
    }

    public static Map<String, Object> filteredAndSortedResult(Integer number, long totalItem, Integer totPage,
            Object data) {
        Map<String, Object> dtos = new HashMap<>();
        dtos.put("currentPage", number + 1);
        dtos.put("totalItems", totalItem);
        dtos.put("totalPages", totPage);
        dtos.put("contents", data);
        return dtos;
    }

}
