/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package ro.cjarges.formupload.action;

import java.util.Locale;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import ro.cjarges.formupload.util.FormularUploadUtil;

import com.liferay.compat.portal.kernel.portlet.DefaultConfigurationAction;
import com.liferay.compat.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;

/**
 * @author marian
 *
 */
public class ConfigurationActionImpl extends DefaultConfigurationAction {

	
	@Override
	public void processAction(PortletConfig portletConfig, ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {

		validateFields(actionRequest);

		if (!SessionErrors.isEmpty(actionRequest)) {
			return;
		}

		boolean updateFields = ParamUtil.getBoolean(actionRequest, "updateFields");
		String portletResource = ParamUtil.getString(actionRequest, "portletResource");
		PortletPreferences preferences = actionRequest.getPreferences();

		LocalizationUtil.setLocalizedPreferencesValues(actionRequest, preferences, "title");
		LocalizationUtil.setLocalizedPreferencesValues(actionRequest, preferences, "description");
		LocalizationUtil.setLocalizedPreferencesValues(actionRequest, preferences, "subtitle");
		LocalizationUtil.setLocalizedPreferencesValues(actionRequest, preferences, "descriere");

		if (updateFields) {

			String databaseTableName = FormularUploadUtil.getNewDatabaseTableName(portletResource);
			preferences.setValue("databaseTableName", databaseTableName);

			if (!SessionErrors.isEmpty(actionRequest)) {
				return;
			}
		}

		if (SessionErrors.isEmpty(actionRequest)) {
			preferences.store();
		}

		super.processAction(portletConfig, actionRequest, actionResponse);
	}

	@Override
	public String render(PortletConfig portletConfig, RenderRequest renderRequest,RenderResponse renderResponse) throws Exception {

		String cmd = ParamUtil.getString(renderRequest, Constants.CMD);

		if (cmd.equals(Constants.ADD)) {
			return "/config.jsp";
		} else {
			return "/config.jsp";
		}
	}

	protected void validateFields(ActionRequest actionRequest) throws Exception {

		Locale defaultLocale = LocaleUtil.getDefault();
		String defaultLanguageId = LocaleUtil.toLanguageId(defaultLocale);

		String title = ParamUtil.getString(actionRequest, "title" + StringPool.UNDERLINE + defaultLanguageId);
		String subtitle = ParamUtil.getString(actionRequest, "subtitle" + StringPool.UNDERLINE + defaultLanguageId);
		String descriere = ParamUtil.getString(actionRequest, "descriere" + StringPool.UNDERLINE + defaultLanguageId);
		
		String subject = getParameter(actionRequest, "subject");
		
		boolean sendAsEmail = GetterUtil.getBoolean(getParameter(actionRequest, "sendAsEmail"));
		boolean saveToDatabase = GetterUtil.getBoolean(getParameter(actionRequest, "saveToDatabase"));
		boolean saveToFile = GetterUtil.getBoolean(getParameter(actionRequest, "saveToFile"));

		if (Validator.isNull(title)) {
			SessionErrors.add(actionRequest, "titleRequired");
		}
		
		if (Validator.isNull(subject)) {
			SessionErrors.add(actionRequest, "subjectRequired");
		}
		
		if (Validator.isNull(descriere)) {
			SessionErrors.add(actionRequest, "descriereRequired");
		}

		if (!sendAsEmail && !saveToDatabase && !saveToFile) {
			SessionErrors.add(actionRequest, "handlingRequired");
		}

		if (saveToDatabase) {
			int i = 1;

			String languageId = LocaleUtil.toLanguageId(actionRequest.getLocale());

			String fieldLabel = ParamUtil.getString(actionRequest, "fieldLabel" + i + "_" + languageId);

			while ((i == 1) || Validator.isNotNull(fieldLabel)) {
				if (fieldLabel.length() > 75 ) {
					SessionErrors.add(actionRequest, "fieldSizeInvalid" + i);
				}

				i++;

				fieldLabel = ParamUtil.getString(actionRequest, "fieldLabel" + i + "_" + languageId);
			}
		}
	}
}
