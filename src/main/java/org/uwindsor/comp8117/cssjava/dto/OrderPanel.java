package org.uwindsor.comp8117.cssjava.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderPanel {
    private OrderCard orderCard;
    private List<OrderAttribute> orderAttributes;
}
