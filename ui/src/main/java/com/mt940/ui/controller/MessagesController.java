package com.mt940.ui.controller;

import com.mt940.dao.jpa.EARMessageDao;
import com.mt940.domain.EARMessage;
import com.mt940.ui.domain.MessageDetails;
import com.mt940.ui.form.MessageFilterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/emails")
public class MessagesController {

    @Autowired
    @Qualifier("earMessageDaoImpl")
    private EARMessageDao messageDao;

    @Autowired
    private ConversionService conversionService;

    @RequestMapping
    public String emails(Model model) {
        model.addAttribute("items", convert(messageDao.findAll()));
        model.addAttribute("form", new MessageFilterForm());
        model.addAttribute("count", messageDao.count());

        return "emails";
    }

    private List<MessageDetails> convert(List<EARMessage> source) {
        return source.stream()
                .map(s -> conversionService.convert(s, MessageDetails.class))
                .collect(Collectors.toList());
    }

    @RequestMapping("/filter")
    public String filter(MessageFilterForm form,
                              Model model) {
        List<EARMessage> list = messageDao
                .findByAllNullable(
                        form.getSentDateFrom() != null ? form.getSentDateFrom().atZone(ZoneId.systemDefault()) : null,
                        form.getSentDateTo() != null ? form.getSentDateTo().atZone(ZoneId.systemDefault()) : null,
                        form.getReceivedDateFrom() != null ? form.getReceivedDateFrom().atZone(ZoneId.systemDefault()) : null,
                        form.getReceivedDateTo() != null ? form.getReceivedDateTo().atZone(ZoneId.systemDefault()) : null,
                        form.getProcessingDateFrom() != null ? form.getProcessingDateFrom().atZone(ZoneId.systemDefault()) : null,
                        form.getProcessingDateTo() != null ? form.getProcessingDateTo().atZone(ZoneId.systemDefault()) : null
                );
        model.addAttribute("items",
                convert(list));
        model.addAttribute("form", form);
        model.addAttribute("count", list.size());

        return "emails";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    MessageDetails get(@PathVariable Long id) {
        EARMessage message = messageDao.findById(id);
        return conversionService.convert(message, MessageDetails.class); }
}
