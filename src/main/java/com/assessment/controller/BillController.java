package com.assessment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assessment.model.request.BillRequest;
import com.assessment.model.response.BillResponse;
import com.assessment.service.BillService;

@RestController
@RequestMapping("bill")
public class BillController {

	@Autowired
	BillService billService;

	@GetMapping("")
	public BillResponse getBill(@RequestBody BillRequest billRequest) {
		return billService.getBill(billRequest);
	}
}
