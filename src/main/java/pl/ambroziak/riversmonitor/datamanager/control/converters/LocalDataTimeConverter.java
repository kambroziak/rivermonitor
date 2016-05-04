package pl.ambroziak.riversmonitor.datamanager.control.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by krzysztof on 30.04.16.
 */
@Converter(autoApply = true)
public class LocalDataTimeConverter implements AttributeConverter<LocalDateTime, Date> {

    @Override
    public Date convertToDatabaseColumn(LocalDateTime localDateTime) {
        if(localDateTime == null){
            return null;
        }
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Date date) {
        if(date==null){
            return null;
        }
        return LocalDateTime.ofInstant(date.toInstant(),ZoneId.systemDefault());
    }
}
