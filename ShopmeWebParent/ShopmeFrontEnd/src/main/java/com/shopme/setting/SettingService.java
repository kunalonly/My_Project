package com.shopme.setting;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;

@Service
public class SettingService {

	
private SettingRepository settingRepo;
public SettingService(SettingRepository settingRepo) {
	super();
	this.settingRepo = settingRepo;
}

public List<Setting> getGeneralSettings() {

	return settingRepo.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.CURRENCY);
}

public EmailSettingBag getEmailSettings() {
	List<Setting> settings = settingRepo.findByCategory(SettingCategory.MAIL_SERVER);
	settings.addAll(settingRepo.findByCategory(SettingCategory.MAIL_TEMPLATES));

	return new EmailSettingBag(settings);
}







}
