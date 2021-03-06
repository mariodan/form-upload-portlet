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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://liferay.com/tld/util" prefix="liferay-util" %>

<%@ 
page import="com.liferay.compat.portal.kernel.util.ArrayUtil" %><%@
page import="com.liferay.compat.portal.kernel.util.LocalizationUtil" %><%@
page import="com.liferay.compat.portal.util.PortalUtil" %><%@
page import="com.liferay.portal.kernel.captcha.CaptchaMaxChallengesException" %><%@
page import="com.liferay.portal.kernel.captcha.CaptchaTextException" %><%@
page import="com.liferay.portal.kernel.portlet.LiferayWindowState" %><%@
page import="com.liferay.portal.kernel.servlet.SessionErrors" %><%@
page import="com.liferay.portal.kernel.util.Constants" %><%@
page import="com.liferay.portal.kernel.util.GetterUtil" %><%@
page import="com.liferay.portal.kernel.util.HtmlUtil" %><%@
page import="com.liferay.portal.kernel.util.ParamUtil" %><%@
page import="com.liferay.portal.kernel.util.PrefsParamUtil" %><%@
page import="com.liferay.portal.kernel.util.StringPool" %><%@
page import="com.liferay.portal.kernel.util.StringUtil" %><%@
page import="com.liferay.portal.kernel.util.Validator" %><%@
page import="com.liferay.portal.security.permission.ActionKeys" %><%@
page import="com.liferay.portlet.PortletPreferencesFactoryUtil" %><%@
page import="com.liferay.portlet.expando.DuplicateColumnNameException" %><%@
page import="ro.cjarges.formupload.util.FormularUploadUtil"
%>

<%@ page import="java.util.ArrayList" %><%@
page import="java.util.Calendar" %><%@
page import="java.util.Comparator" %><%@
page import="java.util.Date" %><%@
page import="java.util.Map" %><%@
page import="java.util.TreeMap" %><%@
page import="java.util.List" %><%@
page import="java.util.HashMap"%><%@
page import="java.util.Locale" %><%@
page import="java.util.TimeZone" %><%@
page import="java.util.GregorianCalendar" %>

<%@ page import="javax.portlet.ActionRequest" %>
<%@ page import="javax.portlet.PortletPreferences" %>
<%@ page import="ro.cjarges.formupload.dao.FormUploadDAO" %>
<%@ page import="ro.cjarges.formupload.model.FormUploadModel" %>
<%@ page import="ro.cjarges.formupload.model.WrapperFormUploadModel" %>

<%@ page import="ro.cjarges.formupload.model.FileModel" %>
<%@ page import="com.liferay.portal.kernel.util.OrderByComparator" %>

<%@
page import="com.liferay.portal.kernel.log.LogFactoryUtil" %><%@
page import="com.liferay.portal.kernel.log.Log" %>
<%@ page import="com.liferay.portal.kernel.util.GetterUtil" %>
<%@ page import="com.liferay.util.portlet.PortletProps" %>
<%@ page import="ro.cjarges.formupload.util.PortletPropsValues" %>
<%@ page import="com.liferay.portal.kernel.dao.search.SearchContainer" %>

<%@ page import="com.liferay.portal.model.User" %>

<portlet:defineObjects />

<liferay-theme:defineObjects />

<%

String currentURL = PortalUtil.getCurrentURL(request);
PortletPreferences preferences = renderRequest.getPreferences();
String portletResource = ParamUtil.getString(request, "portletResource");

int delta = GetterUtil.getInteger(preferences.getValue("delta", StringPool.BLANK), SearchContainer.DEFAULT_DELTA);
int maxFileSize = GetterUtil.getInteger(preferences.getValue("maxFileSize", StringPool.BLANK), 5);


String allowedExtension = PortletPropsValues.UPLOAD_FILE_EXTENSIONS;
%>