package teccr.justdoitcloud.controller.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class StringToLocalTimeConverter implements Converter<String, LocalDate> {

    @Override
    public LocalDate convert(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }
        LocalDate today = LocalDate.now();

        return switch (source) {
            case "1WEEK" -> today.plusWeeks(1);
            case "1MONTH" -> today.plusMonths(1);
            default -> null;
        };
    }
}