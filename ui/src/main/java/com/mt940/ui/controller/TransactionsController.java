package com.mt940.ui.controller;

import com.mt940.dao.jpa.MT940TransactionDao;
import com.mt940.domain.enums.Instance;
import com.mt940.domain.enums.MT940TransactionStatus;
import com.mt940.domain.mt940.MT940Transaction;
import com.mt940.domain.mt940.MT940TransactionSearchRequest;
import com.mt940.ui.domain.Transaction;
import com.mt940.ui.domain.TransactionDetails;
import com.mt940.ui.domain.json.DatatablesReturnedData;
import com.mt940.ui.domain.json.EnumItem;
import com.mt940.ui.form.DatatablesForm;
import com.mt940.ui.form.TransactionFilterForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/transactions")
public class TransactionsController {

    @Autowired
    @Qualifier("mt940TransactionDaoImpl")
    private MT940TransactionDao transactionDao;

    @Autowired
    private ConversionService conversionService;

    @Value("${bkv.ui.page_size}")
    private int PAGE_SIZE;

    Logger l = LoggerFactory.getLogger(getClass());

    @Transactional
    @RequestMapping
    public String transactions(Model model) {
        model.addAttribute("items", new ArrayList<TransactionDetails>(0));
        model.addAttribute("count", transactionDao.count());
        model.addAttribute("form", new TransactionFilterForm());
        setStaticVars(model);

        return "transactions";
    }

    @Transactional
    @RequestMapping("/filter")
    public String filter(@Valid TransactionFilterForm form, Model model) {
        Page<MT940Transaction> page = transactionDao.findByAllNullable(
                conversionService.convert(form, MT940TransactionSearchRequest.class),
                new PageRequest(0, PAGE_SIZE)
        );
        model.addAttribute("items", convert(page.getContent()));
        model.addAttribute("count", page.getTotalElements());
        model.addAttribute("form", form);
        setStaticVars(model);

        return "transactions";
    }

    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    TransactionDetails get(@PathVariable Long id) {
        MT940Transaction transaction = transactionDao.findById(id);
        return conversionService.convert(transaction, TransactionDetails.class);
    }

    @Transactional
    @RequestMapping("/update")
    @PreAuthorize("hasRole('UI_ADMIN')")
    public @ResponseBody
    Transaction update(
            @RequestParam(value = "id", required = true) Long id,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "instance", required = false) Integer instance) {

        l.info("update transaction id = {}, status = {}, instance", id, status, instance);
        MT940Transaction transaction = transactionDao.findById(id);
        if (status != null) {
            transaction.setStatus(MT940TransactionStatus.findByCode(status));
        }

        if (instance != null) {
            transaction.setInstance(Instance.findByCode(instance));
        }

        transactionDao.save(transaction);

        return conversionService
                .convert(transactionDao.findById(id), Transaction.class);
    }

    @Transactional
    @RequestMapping(value="/data", method = RequestMethod.GET)
    public @ResponseBody
    DatatablesReturnedData<Transaction> data(@Valid DatatablesForm form,
                                             TransactionFilterForm filterForm) {
        DatatablesReturnedData<Transaction> returnedData = form.createReturnedData(Transaction.class);
        MT940TransactionSearchRequest searchRequest;
        if (filterForm == null) {
            searchRequest = new MT940TransactionSearchRequest();
        } else {
            searchRequest = conversionService.convert(filterForm, MT940TransactionSearchRequest.class);
        }

        Page<MT940Transaction> page = transactionDao.findByAllNullable(
                searchRequest,
                new PageRequest(form.getStart() / form.getLength(), form.getLength(), form.toSort())
        );

        List<Transaction> transactionList = page.getContent()
                .stream()
                .map(s -> conversionService.convert(s, Transaction.class))
                .collect(Collectors.toList());

        returnedData.setData(transactionList);
        returnedData.setRecordsTotal(page.getTotalElements());
        returnedData.setRecordsFiltered(page.getTotalElements());

        return returnedData;
    }

    private void setStaticVars(Model model) {
        model.addAttribute("statuses", Arrays.stream(MT940TransactionStatus.values())
                .map(s -> conversionService.convert(s, EnumItem.class))
                .collect(Collectors.toList()));

        model.addAttribute("instances", Arrays.stream(Instance.values())
                .map(s -> conversionService.convert(s, EnumItem.class))
                .collect(Collectors.toList()));
    }

    private List<TransactionDetails> convert(List<MT940Transaction> source) {
        return source.stream()
                .map(s -> conversionService.convert(s, TransactionDetails.class))
                .collect(Collectors.toList());
    }

}
