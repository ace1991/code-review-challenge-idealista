package com.idealista.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Picture {
    private Integer id;
    private String url;
    private Quality quality;
}
