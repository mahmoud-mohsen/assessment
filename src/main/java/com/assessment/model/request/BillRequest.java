package com.assessment.model.request;

import java.util.List;

import com.assessment.model.enums.BuyerType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BillRequest {

	private List<Integer> itemsIds;
	private BuyerType buyerType;
	private boolean isBuyerMoreThan2Years;
}
