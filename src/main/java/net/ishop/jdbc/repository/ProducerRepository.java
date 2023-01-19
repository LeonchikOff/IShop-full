package net.ishop.jdbc.repository;

import net.framework.annotations.jdbc.Select;
import net.framework.annotations.jdbc.mapping.CollectionItem;
import net.ishop.entities.Producer;

import java.util.List;

public interface ProducerRepository {

    @Select(sqlQuery = "SELECT * FROM producer ORDER BY producer.name")
    @CollectionItem(parameterizedClass = Producer.class)
    List<Producer> getAllProducersList();
}
