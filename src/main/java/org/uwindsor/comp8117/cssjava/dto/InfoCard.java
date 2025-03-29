package org.uwindsor.comp8117.cssjava.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
public class InfoCard {
    private String cardName;
    private List<InfoField> fields;

    public InfoCard(String cardName) {
        this.cardName = cardName;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class InfoField {
        private String label;
        private String value;
    }

    public void addField(String label, String value) {
        if (fields == null) {
            fields = new ArrayList<>();
        }
        fields.add(new InfoField(label, value));
    }
}
