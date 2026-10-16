package com.medici.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SliceResponse<T> {

    private List<T> content = Collections.emptyList();
    private int number;
    private int size;
    private boolean last;
    private boolean first;
    private String sort;

    public int getNumberOfElements() {
        return content != null ? content.size() : 0;
    }

    public boolean hasContent() {
        return content != null && !content.isEmpty();
    }

    public boolean hasNext() {
        return !last;
    }

    public boolean hasPrevious() {
        return number > 0;
    }
}
