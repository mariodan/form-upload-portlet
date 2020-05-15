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

	int id = Integer.valueOf(ParamUtil.getString(request, "formUploadId"));
	
	FormUploadModel formUpload = FormUploadDAO.getById(id);
	
	List<FileModel> files = new ArrayList<FileModel>(formUpload.getFiles());
	FileModel fileModel = new FileModel();
	if (!files.isEmpty()) {
		fileModel = files.get(0);
	}
	
	String fileUrl = PortletPropsValues.FILE_DOWNLOAD_URL;
	String link = fileUrl + fileModel.getNumeFisier();
	String numeFisier = fileModel.getNumeFisier();
%>

<div class="row">
	<div class="col-md-12 has-error has-feedback">
		<p class="title">Detalii form upload id: <%=id %></p>
		<br>
		<div class="form-group">
    		<label class="col-sm-3 control-label" for="input1">Nume:</label>
    		<div class="col-sm-9">
      			<p><%=formUpload.getNume() %></p>
    		</div>
  		</div>
  		<div class="form-group">
    		<label class="col-sm-3 control-label" for="input1">Prenume:</label>
    		<div class="col-sm-9">
      			<p><%=formUpload.getPrenume() %></p>
    		</div>
  		</div>
  		<div class="form-group">
    		<label class="col-sm-3 control-label" for="input1">Email:</label>
    		<div class="col-sm-9">
      			<p><%=formUpload.getEmail() %></p>
    		</div>
  		</div>
  		<div class="form-group">
    		<label class="col-sm-3 control-label" for="input1">Telefon:</label>
    		<div class="col-sm-9">
      			<p><%=formUpload.getTelefon() %></p>
    		</div>
  		</div>
  		<div class="form-group">
    		<label class="col-sm-3 control-label" for="input1">Nume fisier:</label>
    		<div class="col-sm-9">
      			<p><%=fileModel.getNumeFisier() %></p>
    		</div>
  		</div>
  		<div class="form-group">
    		<label class="col-sm-3 control-label" for="input1">Dimensiune fisier:</label>
    		<div class="col-sm-9">
      			<p><%=fileModel.getSize() %> KB</p>
    		</div>
  		</div>
  		
  		<div class="form-group">
    		<label class="col-sm-3 control-label" for="input1">Tip raspuns:</label>
    		<div class="col-sm-9">
      			<p><%=formUpload.getRaspuns() %></p>
    		</div>
  		</div>
  		
  		<div class="form-group">
    		<label class="col-sm-3 control-label" for="input1">Descarca fisier:</label>
    		<div class="col-sm-9">
      			<a href="<%=link %>" target="_blank" alt="Descarca fisier: <%=numeFisier %>" style="display: inline-block; color: #ffffff; background-color: #3498db; border: solid 1px #3498db; border-radius: 5px; box-sizing: border-box; cursor: pointer; text-decoration: none; font-size: 14px; font-weight: bold; margin: 0; padding: 12px 25px; text-transform: capitalize; border-color: #3498db;">Descarca fisier</a>
    		</div>
  		</div>
	</div>
</div>

<div class="row">
	<div class="col-md-12">
		<br>
		<hr>
		<a href="<portlet:renderURL><portlet:param name="jspPage" value="/view-files.jsp"/></portlet:renderURL>">
			<button type="button" class="btn btn-primary"><liferay-ui:message key="back" /></button>
		</a>
	</div>
</div>