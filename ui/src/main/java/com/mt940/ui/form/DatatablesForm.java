package com.mt940.ui.form;

import com.mt940.ui.domain.json.DatatablesReturnedData;
import com.mt940.ui.form.parts.ColumnFormPart;
import com.mt940.ui.form.parts.OrderFormPart;
import com.mt940.ui.form.parts.SearchFromPart;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.stream.Collectors;

public class DatatablesForm {
    // see https://www.datatables.net/manual/server-side for more details
    Integer draw;
    @NotNull
    Integer start;
    @NotNull
    Integer length;
    SearchFromPart search;
    OrderFormPart[] order;
    ColumnFormPart[] columns;

    public Integer getDraw() {
        return draw;
    }

    public void setDraw(Integer draw) {
        this.draw = draw;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public SearchFromPart getSearch() {
        return search;
    }

    public void setSearch(SearchFromPart search) {
        this.search = search;
    }

    public OrderFormPart[] getOrder() {
        return order;
    }

    public void setOrder(OrderFormPart[] order) {
        this.order = order;
    }

    public ColumnFormPart[] getColumns() {
        return columns;
    }

    public void setColumns(ColumnFormPart[] columns) {
        this.columns = columns;
    }

    public <T> DatatablesReturnedData<T> createReturnedData(Class<T> clazz) {
        DatatablesReturnedData<T> returnedData = new DatatablesReturnedData<T>();
        returnedData.setDraw(draw);

        return returnedData;
    }

    public Sort toSort() {
        if (order == null) {
            return null;
        } else {
            return new Sort(Arrays.stream(this.order)
                    .map(s -> s.toOrder(columns))
                    .collect(Collectors.toList()));
        }
    }

    @Override
    public String toString() {
        return "DatatablesForm{" +
                "draw=" + draw +
                ", start=" + start +
                ", length=" + length +
                ", search=" + search +
                ", order=" + Arrays.toString(order) +
                ", columns=" + Arrays.toString(columns) +
                '}';
    }
}
