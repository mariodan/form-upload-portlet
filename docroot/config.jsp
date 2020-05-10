<%--
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
--%>

<%@ include file="/init.jsp"%>

<%
	String redirect = ParamUtil.getString(renderRequest, "redirect");

	String titleXml = LocalizationUtil.getLocalizationXmlFromPreferences(preferences,renderRequest, "title");
		 
	String descriptionXml = LocalizationUtil.getLocalizationXmlFromPreferences(preferences,renderRequest, "description");
	boolean requireCaptcha = GetterUtil.getBoolean(preferences.getValue("requireCaptcha", StringPool.BLANK));
	
	String successURL = preferences.getValue("successURL",StringPool.BLANK);
	String subtitle = preferences.getValue("subtitle",StringPool.BLANK);
	String descriere = preferences.getValue("descriere",StringPool.BLANK);

	boolean sendAsEmail = GetterUtil.getBoolean(preferences.getValue("sendAsEmail", StringPool.BLANK));
	String emailFromName = FormularUploadUtil.getEmailFromName(preferences, company.getCompanyId());
	String emailFromAddress = FormularUploadUtil.getEmailFromAddress(preferences, company.getCompanyId());
	String emailAddress = preferences.getValue("emailAddress",StringPool.BLANK);
	String subject = preferences.getValue("subject", StringPool.BLANK);

	/* get default To adress from Site/Subsite */
	/* get Organization Primary Email Address*/
	String emailPrimary = FormularUploadUtil.getEmailToAddress(preferences, company.getCompanyId(), themeDisplay);

	/*
	if (Validator.isNotNull(emailPrimary)) {
		if (emailAddress.length() > 3) {

			// daca nu e deja salvata si adresa noastra atunci o adaugam

			if (emailAddress.indexOf(emailPrimary) < 0) {
				emailAddress += "," + emailPrimary;
			}
		} else {
			emailAddress = emailPrimary;
		}
	}
	*/

	/* get required fields from Portlet.Preferences portlet.xml*/
	boolean nume = GetterUtil.getBoolean(preferences.getValue("nume",StringPool.BLANK));
	boolean prenume = GetterUtil.getBoolean(preferences.getValue("prenume", StringPool.BLANK));
	boolean email = GetterUtil.getBoolean(preferences.getValue("email",StringPool.BLANK));
	boolean telefon = GetterUtil.getBoolean(preferences.getValue("telefon", StringPool.BLANK));
	boolean file = GetterUtil.getBoolean(preferences.getValue("file", StringPool.BLANK));
	
	boolean saveToDatabase = GetterUtil.getBoolean(preferences.getValue("saveToDatabase", StringPool.BLANK));

	String databaseTableName = preferences.getValue("databaseTableName", StringPool.BLANK);

%>

<liferay-portlet:actionURL portletConfiguration="true" var="configurationURL" />

<aui:form action="<%=configurationURL%>" method="post" name="fm">
	<aui:input name="<%=Constants.CMD%>" type="hidden" value="<%=Constants.UPDATE%>" />
	<aui:input name="redirect" type="hidden" value="<%=redirect%>" />


	<liferay-ui:panel-container extended="<%=Boolean.TRUE%>" id="webFormConfiguration" persistState="<%=true%>">

		<!-- Form Information -->
		<liferay-ui:panel collapsible="<%=true%>" extended="<%=true%>" id="webFormGeneral" persistState="<%=true%>" title="form-information">
			<aui:fieldset>
				<!-- title -->
				<liferay-ui:error key="titleRequired" message="please-enter-a-title" />

				<aui:field-wrapper cssClass="lfr-input-text-container" label="title">
					<liferay-ui:input-localized name="title" xml="<%=titleXml%>" />
				</aui:field-wrapper>
				<!-- end title -->

				<!-- subtitlu -->
				<liferay-ui:error key="subtitleRequired" message="please-enter-a-subtitle" />

				<aui:field-wrapper cssClass="lfr-input-text-container" label="subtitle">
					<liferay-ui:input-localized name="subtitle" xml="<%=subtitle%>" />
				</aui:field-wrapper>
				<!-- end subtitlu -->
				
				<!-- descriere -->
				<liferay-ui:error key="descriereRequired" message="please-enter-a-descriere" />

				<aui:field-wrapper cssClass="lfr-input-text-container" label="descriere">
					<liferay-ui:input-localized name="descriere" xml="<%=descriere%>" />
				</aui:field-wrapper>
				<!-- end descriere -->

				<aui:input cssClass="lfr-input-text-container"
					label="redirect-url-on-success" name="preferences--successURL--"
					value="<%=HtmlUtil.toInputSafe(successURL)%>" />
			</aui:fieldset>
		</liferay-ui:panel>


		<!-- Required Fields -->
		<liferay-ui:panel collapsible="<%=true%>" extended="<%=true%>"
			id="webFormFields" persistState="<%=true%>"
			title="required-form-fields">
			<aui:fieldset>

				<aui:fieldset cssClass="handle-data" label="required">
					<aui:input name="preferences--nume--" type="checkbox" value="<%=nume%>" />
					<aui:input name="preferences--prenume--" type="checkbox" value="<%=prenume%>" />
					<aui:input name="preferences--email--" type="checkbox" value="<%=email%>" />
					<aui:input name="preferences--telefon--" type="checkbox" value="<%=telefon%>" />
					<aui:input name="preferences--file--" type="checkbox" value="<%=file%>" />
				</aui:fieldset>

			</aui:fieldset>
		</liferay-ui:panel>
		
		
		<!-- Required Fields -->
		<liferay-ui:panel collapsible="<%= true %>" extended="<%= true %>" id="webFormDisplayItems" persistState="<%= true %>" title="items-to-display">
			<aui:fieldset>
				<aui:select helpMessage="number-of-items-to-display-help" label="number-of-items-to-display" name="preferences--delta--">
		
				<%
				int[] deltas = {1, 2, 3, 4, 5, 10, 15, 20, 25, 30, 40, 50, 60, 70, 80, 90, 100};
		
				for (int currentDelta: deltas) {
				%>
		
					<aui:option label="<%= currentDelta %>" selected="<%= (delta == currentDelta) %>" />
		
				<%
				}
				%>
		
				</aui:select>
			</aui:fieldset>
		</liferay-ui:panel>


		<!-- Form Email/Database/File settings -->

		<liferay-ui:panel collapsible="<%=true%>" extended="<%=true%>" id="webFormData" persistState="<%=true%>" title="handling-of-form-data">
			<aui:fieldset cssClass="handle-data" label="email">

				<liferay-ui:error key="emailAddressInvalid" message="please-enter-a-valid-email-address" />
				<liferay-ui:error key="emailAddressRequired" message="please-enter-an-email-address" />
				<liferay-ui:error key="fileNameInvalid" message="please-enter-a-valid-path-and-file-name" />
				<liferay-ui:error key="handlingRequired" message="please-select-an-action-for-the-handling-of-form-data" />
				<liferay-ui:error key="subjectRequired" message="please-enter-a-subject" />

				<aui:input label="send-as-email" name="preferences--sendAsEmail--" type="checkbox" value="<%=sendAsEmail%>" />

				<aui:fieldset>
					<aui:input cssClass="lfr-input-text-container" label="name-from"
						name="preferences--emailFromName--" value="<%=emailFromName%>" />

					<aui:input cssClass="lfr-input-text-container" label="address-from"
						name="preferences--emailFromAddress--"
						value="<%=emailFromAddress%>" />
				</aui:fieldset>

				<aui:input cssClass="lfr-input-text-container"
					helpMessage="add-email-addresses-separated-by-commas"
					label="addresses-to" name="preferences--emailAddress--"
					value="<%=emailAddress%>" />

				<aui:input cssClass="lfr-input-text-container"
					name="preferences--subject--" value="<%=subject%>" />

				<aui:field-wrapper cssClass="lfr-textarea-container" label="description">
					<liferay-ui:input-localized name="description" type="textarea" xml="<%=descriptionXml%>" />
				</aui:field-wrapper>

			</aui:fieldset>

			<aui:fieldset cssClass="handle-data" label="database">
				<aui:input name="preferences--saveToDatabase--" type="checkbox"
					value="<%=saveToDatabase%>" />
			</aui:fieldset>

		</liferay-ui:panel>


	</liferay-ui:panel-container>

	<aui:button-row>
		<aui:button type="submit" />
	</aui:button-row>
</aui:form>

