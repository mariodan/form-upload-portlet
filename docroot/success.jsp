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

<%@ include file="/init.jsp" %>

<%

	String title = LocalizationUtil.getPreferencesValue(preferences, "title", themeDisplay.getLanguageId());
	String description = LocalizationUtil.getPreferencesValue(preferences, "description", themeDisplay.getLanguageId());
		 
%>

<div class="row">
	<div class="col-md-12">
		<liferay-ui:error key="success" message="Formularul a fost trimis cu succes!" />
	</div>
</div>