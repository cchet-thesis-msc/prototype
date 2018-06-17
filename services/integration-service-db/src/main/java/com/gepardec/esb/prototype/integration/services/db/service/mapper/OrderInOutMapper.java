package com.gepardec.esb.prototype.integration.services.db.service.mapper;

import com.gepardec.esb.prototype.integration.services.db.rest.model.ItemDto;
import com.gepardec.esb.prototype.integration.services.db.rest.model.OrderDto;
import com.gepardec.esb.prototype.integration.services.db.service.model.Customer;
import com.gepardec.esb.prototype.integration.services.db.service.model.Order;
import org.apache.deltaspike.data.api.mapping.SimpleQueryInOutMapperBase;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/15/18
 */
@ApplicationScoped
public class OrderInOutMapper extends SimpleQueryInOutMapperBase<Order, OrderDto> {

    @Inject
    private EntityManager em;

    private static final String ITEM_DELIMITER = "\\|";
    private static final String ITEM_DATA_DELIMITER = ",";

    @Override
    protected Object getPrimaryKey(OrderDto orderDto) {
        return (orderDto != null) ? orderDto.getCustomerId() : null;
    }

    @Override
    protected OrderDto toDto(Order order) {
        if (order != null) {
            final List<ItemDto> itemDtos = extractingItems(order.getData());
            final Map<String, Double> itemPrices = extractingItemPrices(itemDtos);
            final OrderDto dto = new OrderDto();
            dto.setId(order.getId());
            dto.setVersion(order.getVersion());
            dto.setCustomerId(order.getCustomer().getId());
            dto.setCreatedAt(Date.from(order.getCreatedAt().toInstant(ZoneOffset.UTC)));
            dto.setDeliveredAt(Date.from(order.getDeliveredAt().toInstant(ZoneOffset.UTC)));
            dto.setItems(itemDtos);
            dto.setItemPrices(itemPrices);

            return dto;
        }

        return null;
    }

    @Override
    protected Order toEntity(Order order,
                             OrderDto orderDto) {
        if (orderDto != null) {
            order = (order != null) ?order : new Order();
            order.setId((order.getId() != null) ? order.getId() : orderDto.getId());
            order.setCustomer(em.getReference(Customer.class, orderDto.getCustomerId()));
            order.setDeliveredAt(LocalDateTime.ofInstant(orderDto.getDeliveredAt().toInstant(), ZoneId.systemDefault()));
            order.setData(buildDateFromItems(orderDto.getItems()));

            return order;
        }

        throw new IllegalStateException("Why is orderDto null here ?");
    }

    private static List<ItemDto> extractingItems(final String data) {
        final List<ItemDto> itemDtos = new LinkedList<>();
        final String[] ItemParts = data.split(ITEM_DELIMITER);
        for (String itemPart : ItemParts) {
            final String[] itemDataParts = itemPart.split(ITEM_DATA_DELIMITER);
            if (itemDataParts.length > 0) {
                final ItemDto item = new ItemDto();
                item.setName(itemDataParts[0]);
                if (itemDataParts.length > 1) {
                    item.setCount(Long.valueOf(itemDataParts[1]));
                } else {
                    item.setCount(0L);
                }
                if (itemDataParts.length > 2) {
                    item.setPrice(Double.valueOf(itemDataParts[1]));
                } else {
                    item.setPrice(0.0);
                }
                itemDtos.add(item);
            }
        }

        return itemDtos;
    }

    private static Map<String, Double> extractingItemPrices(final List<ItemDto> items) {
        return items.stream()
                    .collect(Collectors.groupingBy(ItemDto::getName,
                                                   Collectors.averagingDouble(ItemDto::getPrice)));
    }

    private String buildDateFromItems(final List<ItemDto> items) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            final ItemDto item = items.get(i);
            sb.append(item.getName()).append(ITEM_DATA_DELIMITER)
              .append(item.getCount()).append(ITEM_DATA_DELIMITER)
              .append(item.getPrice());
            if (i < (items.size() - 1)) {
                sb.append(ITEM_DELIMITER);
            }
        }

        return sb.toString();
    }
}
