package org.uwindsor.comp8117.cssjava.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCardListMessageBody {
    private String messageTitle;
    List<OrderCard> orderCards;
}
