package com.myproject.AgritradeHub.Repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.myproject.AgritradeHub.Model.AllUsers;
import com.myproject.AgritradeHub.Model.Orders;
import com.myproject.AgritradeHub.Model.Orders.OrderStatus;

public interface OrdersRepository extends JpaRepository<Orders, Long>{

	List<Orders> findAllByMerchant(AllUsers merchant);

	List<Orders> findAllByFarmer(AllUsers farmer);

	 // Monthly order counts (last 6 months for chart)
    @Query("SELECT FUNCTION('DATE_FORMAT', o.orderDate, '%b') AS month, COUNT(o) " +
           "FROM Orders o GROUP BY FUNCTION('DATE_FORMAT', o.orderDate, '%b') " +
           "ORDER BY MIN(o.orderDate)")
    List<Object[]> getMonthlyOrders();

	
    default List<String> getMonthlyOrderLabels() {
        return getMonthlyOrders().stream().map(o -> (String) o[0]).toList();
    }

    default List<Long> getMonthlyOrderCounts() {
        return getMonthlyOrders().stream().map(o -> ((Number) o[1]).longValue()).toList();
    }

	long countByOrderStatus(OrderStatus cancelled);
	
	// ✅ Monthly Order Counts (Native SQL)
    @Query(value = "SELECT MONTH(o.order_date) AS month, COUNT(*) AS total " +
                   "FROM orders o " +
                   "GROUP BY MONTH(o.order_date) " +
                   "ORDER BY month", nativeQuery = true)
    List<Object[]> getMonthlyOrderStats();

	Object countByFarmer(AllUsers farmer);

	Object countByFarmerAndOrderStatus(AllUsers farmer, OrderStatus delivered);

	List<Orders> findTop5ByFarmerOrderByOrderDateDesc(AllUsers farmer);
	
	@Query("SELECT SUM(o.pricePerUnit * o.quantity) FROM Orders o WHERE o.farmer.id = :id AND o.orderStatus = 'DELIVERED'")
	BigDecimal getTotalRevenueByFarmerId(@Param("id") Long farmerId);

	@Query("SELECT SUM(o.pricePerUnit * o.quantity) FROM Orders o WHERE o.farmer.id = :id AND o.orderStatus = 'DELIVERED' AND MONTH(o.deliveryDate) = MONTH(CURRENT_DATE) AND YEAR(o.deliveryDate) = YEAR(CURRENT_DATE)")
	BigDecimal getCurrentMonthRevenue(@Param("id") Long farmerId);

	@Query("SELECT o.productName FROM Orders o WHERE o.farmer.id = :id AND o.orderStatus = 'DELIVERED' GROUP BY o.productName ORDER BY SUM(o.quantity) DESC LIMIT 1")
	String getMostOrderedProduct(@Param("id") Long farmerId);
}
