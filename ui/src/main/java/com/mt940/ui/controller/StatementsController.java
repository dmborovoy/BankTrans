package com.mt940.ui.controller;

import com.mt940.dao.jpa.MT940StatementDao;
import com.mt940.domain.mt940.MT940Statement;
import com.mt940.ui.domain.StatementDetails;
import com.mt940.ui.form.StatementFilterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/statements")
public class StatementsController {

    @Autowired
    @Qualifier("mt940StatementDaoImpl")
    private MT940StatementDao statementDao;

    @Autowired
    private ConversionService conversionService;

    @RequestMapping
    @Transactional
    public String statements(Model model) {
        model.addAttribute("items", convert(statementDao.findAll()));
        model.addAttribute("form", new StatementFilterForm());
        model.addAttribute("count", statementDao.count());

        return "statements";
    }

    @Transactional
    @RequestMapping("/filter")
    public String filter(StatementFilterForm form, Model model) {
        List<MT940Statement> list = statementDao.findByAllNullable(
                form.getAccountId(),
                form.getRelatedReference(),
                form.getTransactionReference(),
                form.getMessageId(),
                form.getFileId()
        );
        model.addAttribute("items", convert(list));
        model.addAttribute("count", list.size());
        model.addAttribute("form", form);

        return "statements";
    }

    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    StatementDetails get(@PathVariable Long id) {
        MT940Statement statement = statementDao.findById(id);
        return conversionService.convert(statement, StatementDetails.class);
    }

    private List<StatementDetails> convert(List<MT940Statement> source) {
        return source.stream()
                .map(s -> conversionService.convert(s, StatementDetails.class))
                .collect(Collectors.toList());
    }
}
