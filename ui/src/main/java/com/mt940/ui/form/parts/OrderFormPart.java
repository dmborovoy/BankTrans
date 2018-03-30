package com.mt940.ui.form.parts;

import org.springframework.data.domain.Sort;

public class OrderFormPart {
    Integer column;
    String dir;

    public OrderFormPart(int column, String dir) {
        this.column = column;
        this.dir = dir;
    }

    public OrderFormPart() {
    }

    public Integer getColumn() {
        return column;
    }

    public void setColumn(Integer column) {
        this.column = column;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public Sort.Direction toDirection() {
        Sort.Direction direction = null;
        if (dir != null) {
            if (dir.equalsIgnoreCase("asc")) {
                direction = Sort.Direction.ASC;
            } else if (dir.equalsIgnoreCase("desc")) {
                direction = Sort.Direction.DESC;
            }
        }

        return direction;
    }

    public Sort.Order toOrder(final ColumnFormPart[] columns) {
        return new Sort.Order(
                toDirection(),
                columns[column].getName()
        );
    }

    @Override
    public String toString() {
        return "OrderFormPart{" +
                "column=" + column +
                ", dir='" + dir + '\'' +
                '}';
    }
}
