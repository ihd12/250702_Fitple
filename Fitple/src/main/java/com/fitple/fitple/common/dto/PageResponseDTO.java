package com.fitple.fitple.common.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.IntStream;

@Getter
public class PageResponseDTO<E> {

    private int page;
    private int size;
    private int total;

    private int start;
    private int end;
    private boolean prev;
    private boolean next;

    private int last;
    private List<Integer> pageList;
    private List<E> dtoList;

    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(PageRequestDTO pageRequestDTO, List<E> dtoList, int total) {
        this.page = pageRequestDTO.getPage();
        this.size = pageRequestDTO.getSize();
        this.total = total;
        this.dtoList = dtoList;

        int tempEnd = (int)(Math.ceil(page / 10.0)) * 10;
        this.start = tempEnd - 9;
        this.end = Math.min((int)(Math.ceil(total / (double)size)), tempEnd);
        this.end = Math.max(end, 1);
        this.last = (int)(Math.ceil(total / (double)size));

        this.prev = this.start > 1;
        this.next = total > this.end * this.size;

        this.pageList = IntStream.rangeClosed(start, end).boxed().toList();
    }
}
