package lego.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import lego.pojo.Order;


public interface OrderMapper {

	public List<Order> findAll(String userId);

	public void deleteOrder(String[] orderIds);

	public void updateState(@Param("orderIds")String[] orderIds, @Param("paystate")int paystate);

	public void saveOrder(Order order);

}
