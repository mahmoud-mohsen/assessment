package com.assessment.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assessment.entity.Items;
import com.assessment.model.enums.BuyerType;
import com.assessment.model.enums.ItemType;
import com.assessment.model.request.BillRequest;
import com.assessment.model.response.BillResponse;
import com.assessment.model.response.Message;
import com.assessment.repository.ItemRepository;

@Service
public class BillService {

	private static final int DEDUCTED_AMOUNT = 5;
	private static final int AMOUNT_TO_DEDUCT = 100;
	private static final int TWO_YEARS_DISCOUNT = 5;
	private static final int AFFILIATE_DISCOUNT = 10;
	private static final int EMPLOYEE_DISCOUNT = 30;
	@Autowired
	ItemRepository itemRepository;

	/**
	 * This method used to get Bill for base on items and customer Type. It get all
	 * needed Items from database and then get the total price for nonGroceries
	 * items and then get the groceries Items price, then start calculating
	 * percentage based discount for the nongroceries items and then calculateing
	 * the deducted amount based on the total prices
	 * 
	 * @param billRequest : contains the item id's and customer type
	 * 
	 * @return {@link BillResponse}
	 */
	public BillResponse getBill(BillRequest billRequest) {
		Iterable<Items> neededItems = itemRepository.findAllById(billRequest.getItemsIds());
		float totalPrice = 0;
		float nonGroceriesTotalPrice = getNotGroceriesTotalPrice(neededItems, billRequest.getItemsIds());
		float groceriesTotalPrice = getGroceriesTotalPrice(neededItems, billRequest.getItemsIds());

		totalPrice += groceriesTotalPrice;

		float priceAfterPercentageDiscount = getPriceAfterPercentageDiscount(billRequest, nonGroceriesTotalPrice);
		totalPrice += priceAfterPercentageDiscount;

		float deductedAmount = getDeductedAmount(totalPrice);

		totalPrice = totalPrice - deductedAmount;

		return getBillResposne(nonGroceriesTotalPrice, groceriesTotalPrice, neededItems, totalPrice,
				priceAfterPercentageDiscount, deductedAmount, billRequest.getItemsIds());
	}

	/**
	 * This method used to get the deducted amount based on each 100$ there is 5$
	 * discount
	 * 
	 * @param totalPrice
	 * @return deducted Amount
	 * 
	 */
	private float getDeductedAmount(float totalPrice) {
		float deductedAmount = 0;
		if (totalPrice > 100) {
			deductedAmount = (totalPrice % AMOUNT_TO_DEDUCT) * DEDUCTED_AMOUNT;
		}
		return deductedAmount;
	}

	/**
	 * This method used to generate the bill response
	 * 
	 * @param neededItems:                 all matched Items in the Bill
	 * @param totalPrice
	 * @param priceAfterPercentageDiscount
	 * @param deductedAmount
	 * @param nonGroceriesTotalPrice
	 * @param groceriesTotalPrice
	 * @param neededItemsIds
	 * @return {@link BillResponse}
	 */
	private BillResponse getBillResposne(float nonGroceriesTotalPrice, float groceriesTotalPrice,
			Iterable<Items> neededItems, float totalPrice, float priceAfterPercentageDiscount, float deductedAmount,
			List<Integer> neededItemsIds) {
		BillResponse billResponse = new BillResponse();
		List<Message> messages = new ArrayList<>();
		for (Items item : neededItems) {
			int frequent = Collections.frequency(neededItemsIds, item.getId());
			String paragraph = String.format("%s count: %s price is", item.getName(), frequent);
			Message message = new Message(paragraph, item.getPrice() * frequent);
			messages.add(message);

		}
		Message message = new Message("Groceries Price", groceriesTotalPrice);
		messages.add(message);
		message = new Message("Non Groceries Price", nonGroceriesTotalPrice);
		messages.add(message);
		message = new Message("Groceries Price After percentage discount", priceAfterPercentageDiscount);
		messages.add(message);
		message = new Message("deducted amount", deductedAmount);
		messages.add(message);
		message = new Message("Total Price", totalPrice);
		messages.add(message);

		billResponse.setMessages(messages);
		return billResponse;
	}

	/**
	 * This method used to get the price for nonGroceries Items after percentage
	 * discount
	 * 
	 * @param billRequest
	 * @param notGroceriesTotalPrice
	 */
	private float getPriceAfterPercentageDiscount(BillRequest billRequest, float nonGroceriesTotalPrice) {

		if (billRequest.getBuyerType().equals(BuyerType.EMPLOYEE)) {

			return getPercentageDiscount(nonGroceriesTotalPrice, EMPLOYEE_DISCOUNT);
		} else if (billRequest.getBuyerType().equals(BuyerType.AFFILIATE)) {
			return getPercentageDiscount(nonGroceriesTotalPrice, AFFILIATE_DISCOUNT);
		} else if (billRequest.isBuyerMoreThan2Years()) {
			return getPercentageDiscount(nonGroceriesTotalPrice, TWO_YEARS_DISCOUNT);
		} else {
			return nonGroceriesTotalPrice;
		}
	}

	/**
	 * This method calculate the discount result
	 * 
	 * @param price
	 * @param percentageDiscount
	 * @return discount
	 */
	private float getPercentageDiscount(float price, float percentageDiscount) {
		return price - (price * percentageDiscount / 100);
	}

	/**
	 * This method used to iterate over the items to get the not groceries item
	 * total price
	 * 
	 * @param neededItems : List of items to sum it's price
	 * @param ids
	 * @return totalPrice
	 */
	private float getNotGroceriesTotalPrice(Iterable<Items> neededItems, List<Integer> ids) {
		float totalPrice = 0;

		for (Items item : neededItems) {
			if (!item.getItemType().equals(ItemType.GROCERIES)) {
				int frequent = Collections.frequency(ids, item.getId());
				totalPrice += (item.getPrice() * frequent);

			}
		}
		return totalPrice;
	}

	/**
	 * This method used to iterate over the items to get the groceries item total
	 * price
	 * 
	 * @param neededItems : List of items to sum it's price
	 * @param ids
	 * @return totalPrice
	 */
	private float getGroceriesTotalPrice(Iterable<Items> neededItems, List<Integer> ids) {
		float groceriesTotalPrice = 0;

		for (Items item : neededItems) {
			if (item.getItemType().equals(ItemType.GROCERIES)) {
				int frequent = Collections.frequency(ids, item.getId());
				groceriesTotalPrice += (item.getPrice() * frequent);
			}
		}
		return groceriesTotalPrice;
	}
}
