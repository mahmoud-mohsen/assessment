package com.assessment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import com.assessment.entity.Items;
import com.assessment.model.enums.ItemType;
import com.assessment.repository.ItemRepository;

@SpringBootApplication
public class AssessmentApplication {

	@Autowired
	ItemRepository itemRepository;

	public static void main(String[] args) {
		SpringApplication.run(AssessmentApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() {
		if (itemRepository.count() == 0) {
			itemRepository.save(generateItem("product-1", ItemType.OTHER, 10));
			itemRepository.save(generateItem("product-2", ItemType.OTHER, 15));
			itemRepository.save(generateItem("product-3", ItemType.OTHER, 20.5f));
			itemRepository.save(generateItem("product-4", ItemType.OTHER, 10));
			itemRepository.save(generateItem("product-5", ItemType.OTHER, 40));
			itemRepository.save(generateItem("grocery-1", ItemType.GROCERIES, 10.5f));
			itemRepository.save(generateItem("grocery-2", ItemType.GROCERIES, 10));
			itemRepository.save(generateItem("grocery-3", ItemType.GROCERIES, 10.5f));
			itemRepository.save(generateItem("grocery-4", ItemType.GROCERIES, 15));
			itemRepository.save(generateItem("grocery-5", ItemType.GROCERIES, 20));

		}
	}

	/**
	 * This method Used to generate Item entity
	 * 
	 * @param name
	 * @param itemType
	 * @param price
	 */
	private Items generateItem(String name, ItemType itemType, float price) {
		Items item = new Items();
		item.setItemType(itemType);
		item.setName(name);
		item.setPrice(price);
		return item;
	}

}
