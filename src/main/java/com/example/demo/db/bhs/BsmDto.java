package com.example.demo.db.bhs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Customer Dto.
 *
 * @author Alexey_Krasnov
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BsmDto {

    private int id;
    private String bsm;

}

