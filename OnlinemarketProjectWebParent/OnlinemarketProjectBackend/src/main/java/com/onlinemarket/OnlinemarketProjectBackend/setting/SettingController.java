package com.onlinemarket.OnlinemarketProjectBackend.setting;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.onlinemarket.OnlinemarketProjectBackend.FileUploadUtil;

import com.onlinemarket.OnlinemarketProjectCommon.entity.Currency;
import com.onlinemarket.OnlinemarketProjectCommon.entity.Setting;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class SettingController {
    @Autowired
    private SettingService service;

    @Autowired
    private CurrencyRepository currencyRepo;

    @GetMapping("/settings")
    public String listAll(Model model){
        List<Setting> listSettings = service.listAllSettings();
        List<Currency> listCurrencies = currencyRepo.findAllByOrderByNameAsc();

        model.addAttribute("listCurrencies", listCurrencies);
        //add each setting individually
        for(Setting setting:listSettings){
            model.addAttribute(setting.getKey(), setting.getValue());
        }

        return "settings/settings";
    }

    @PostMapping("/settings/save_general")
    public String saveGeneralSettings(@RequestParam("fileImage") MultipartFile multipartFile, 
        HttpServletRequest request ,RedirectAttributes redirectAttributes) throws IOException{
            
            GeneralSettingBag settingBag = service.getGeneralSettings();

            saveSiteLogo(multipartFile, settingBag);
            saveCurrencySymbol(request, settingBag);
            updateSettingValuesFromForm(request, settingBag.list());

            redirectAttributes.addFlashAttribute("message", "The general settings have been saved successfully");
            return "redirect:/settings";
    }

    private void updateSettingValuesFromForm(HttpServletRequest request, List<Setting> listSettings) {
        for(Setting setting: listSettings){
            String value = request.getParameter(setting.getKey());
            if(value != null){
                setting.setValue(value);
            }
        }
        service.saveAll(listSettings);
    }

    private void saveCurrencySymbol(HttpServletRequest request, GeneralSettingBag settingBag) {
        Integer currencyId = Integer.parseInt(request.getParameter("CURRENCY_ID"));
        Optional<Currency> findByIdResult = currencyRepo.findById(currencyId);

        if(findByIdResult.isPresent()){
            Currency currency = findByIdResult.get();
            settingBag.updateCurrencySymbol(currency.getSymbol());
        }
    }

    private void saveSiteLogo(MultipartFile multipartFile, GeneralSettingBag settingBag) throws IOException {
        if(!multipartFile.isEmpty()){
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            String value = "/site-logo/"+fileName;
            settingBag.updateSiteLogo(value);
            
            String uploadDir = "./site-logo/";

            FileUploadUtil.deleteFiles(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }
    }

    @PostMapping("/settings/save_mail_server")
    public String saveMailServerSettings(HttpServletRequest request ,RedirectAttributes redirectAttributes) {
            
            List<Setting> mailServerSettings = service.getMailServerSettings();
            updateSettingValuesFromForm(request, mailServerSettings);

            redirectAttributes.addFlashAttribute("message", "The mail server settings have been saved successfully");
            return "redirect:/settings";
    }

    @PostMapping("/settings/save_mail_templates")
    public String saveMailTemplatesSettings(HttpServletRequest request ,RedirectAttributes redirectAttributes) {
            
            List<Setting> mailTemplateSettings = service.getMailTemplatesSettings();
            updateSettingValuesFromForm(request, mailTemplateSettings);

            redirectAttributes.addFlashAttribute("message", "The mail template settings have been saved successfully");
            return "redirect:/settings";
    }
}
