package com.mt940.ui.controller;

import com.mt940.dao.jpa.EARAttachmentDao;
import com.mt940.domain.EARAttachment;
import com.mt940.domain.enums.EARAttachmentStatus;
import com.mt940.ui.domain.AttachmentDetails;
import com.mt940.ui.form.AttachmentFilterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/attachments")
public class AttachmentsController {
    @Autowired
    @Qualifier("earAttachmentDaoImpl")
    private EARAttachmentDao attachmentDao;

    @Autowired
    private ConversionService conversionService;

    @RequestMapping
    public String attachments(Model model) {
        model.addAttribute("items", convert(attachmentDao.findAll()));
        model.addAttribute("form", new AttachmentFilterForm());
        model.addAttribute("count", attachmentDao.count());

        return "attachments";
    }

    @RequestMapping("/filter")
    public String filter(AttachmentFilterForm form, Model model) {
        List<EARAttachment> list = attachmentDao.findByAllNullable(
                form.getStatus() == null ? null : EARAttachmentStatus.findByCode(form.getStatus()),
                form.getFileName(),
                form.getMessageId()
        );
        model.addAttribute("items", convert(list));
        model.addAttribute("form", form);
        model.addAttribute("count", list.size());
        return "attachments";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    AttachmentDetails get(@PathVariable Long id) {
        EARAttachment attachment = attachmentDao.findById(id);
        return conversionService.convert(attachment, AttachmentDetails.class);
    }

    private List<AttachmentDetails> convert(List<EARAttachment> source) {
        return source.stream()
                .map(s -> conversionService.convert(s, AttachmentDetails.class))
                .collect(Collectors.toList());
    }

}
