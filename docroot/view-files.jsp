
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

<%!
private static Log _log = LogFactoryUtil.getLog("portal-web.docroot.view-files_jsp");
%>

<%@page import="com.liferay.portal.kernel.util.ListUtil" %>
<%@page import="ro.cjarges.formupload.util.CustomComparatorUtil" %>
<%@page import="java.util.Collections" %>

<liferay-portlet:renderURL varImpl="iteratorURL">
       <portlet:param name="mvcPath" value="/view-files.jsp" />
</liferay-portlet:renderURL>

<%
	String orderByCol = ParamUtil.getString(request, "orderByCol");
	String orderByType = ParamUtil.getString(request, "orderByType");
	
	if (Validator.isNotNull(orderByCol) && Validator.isNotNull(orderByType)) {
		
		request.setAttribute("orderByCol", orderByCol);
		request.setAttribute("orderByType", orderByType);
		
	} else {
		
		orderByType = "asc";
		request.setAttribute("orderByType", orderByType);
	}
	

	if (!themeDisplay.isSignedIn()) {
%>
		
		<div class="row">
			<div class="col-md-12">
				<p class="bg-danger"><liferay-ui:message key="error" />: 
					<liferay-ui:message key="you-do-not-have-the-roles-required-to-access-this-page" />
				</p>
			</div>
		</div>
		
		<%
		
	}
%>

<%
	
	if (themeDisplay.isSignedIn()) {
		
		List<String> headerNames = new ArrayList<String>();

		headerNames.add("nume");
		headerNames.add("email");
		headerNames.add("telefon");
		headerNames.add("fisier-nume");
		headerNames.add("fisier-size");
		headerNames.add("data-adaugare");
		
%>
	

		
		<portlet:resourceURL var="exportURL">
				<portlet:param name="<%= Constants.CMD %>" value="export" />
		</portlet:resourceURL>
		
		<liferay-portlet:actionURL portletName="<%= portletResource %>" var="exportMyUrl">
							<portlet:param name="<%= ActionRequest.ACTION_NAME %>" value="exportData" />
							<portlet:param name="redirect" value="<%= currentURL %>" />
							<portlet:param name="<%= Constants.CMD %>" value="export1" />
		</liferay-portlet:actionURL>
	
		<div class="row">
			<div class="col-md-12">
				<!-- Indicates caution should be taken with this action -->
					
					
					<a href="<%=exportURL %>" >
						<button class="btn btn-warning"><liferay-ui:message key="export-formular-upload" /></button>
					</a>
					
					<%
						String taglibExport = "submitForm(document.hrefFm, '" + exportURL + "', false);";
					%>
					<!--
					<aui:button onClick="" value="export-data" />
					-->
			</div>
		</div>
		
		<div class="row">
			<br>
			<div class="col-md-12"><h2>Vizualizare formular upload</h2>
				<hr>
			</div>
			<br>
		</div>
		
		
<!-- The headerNames attribute holds the title keys for the columns. -->
<liferay-ui:search-container 
headerNames="nume, email, telefon, fileName, fileSize, createdAt"
iteratorURL="<%= iteratorURL %>"  
emptyResultsMessage="there-are-no-files"
delta="<%=delta %>"
deltaConfigurable="true"
orderByCol="<%=orderByCol %>" 
orderByType="<%=orderByType %>"
>
	
        <liferay-ui:search-container-results>
                <%

                List<WrapperFormUploadModel> uploads = FormUploadDAO.getLiferayContainerFormUploads();
                
                OrderByComparator orderByComparator = CustomComparatorUtil.getUploadOrderByComparator(orderByCol, orderByType);
                
                Collections.sort(uploads, orderByComparator);
                

                /**

                * "results" variable is initialized by the taglib. Retrieve the list of items. 
                * This may vary depending on how you obtain or create the list.
                **/

	         	results = ListUtil.subList(uploads, searchContainer.getStart(), searchContainer.getEnd());
                
                /**

                * "total" is another variable initialized by the taglib. This variable simply
                * holds the total number of items the search container will hold. Change this line
                * as necessary to get the total number of items to display.
                **/

	         	total = uploads.size();

                // Set the proper page attributes based on the above information. No need to change this. 
                pageContext.setAttribute("results", results);
                pageContext.setAttribute("total", total);
                
                %> 
        </liferay-ui:search-container-results> 
        
        <!--

        The search container row tag sets up our object. Since we have a List of Users
        one single row represents one single User object. Therefore, our "className" is         
        User. Be sure to fully qualify the class name. The escapedModel value is set to "true"
        to make sure that any values returned are properly HTML escaped. The "modelVar" 
        represents any value you want to use in the column text. 
        The variable is initialized for you. 
        -->

	<liferay-ui:search-container-row 
            className="ro.cjarges.formupload.model.WrapperFormUploadModel"  
            escapedModel="<%= true %>" 
            keyProperty="id" 
            modelVar="formUpload" > 
	
	<portlet:actionURL var="editURL">  
		<portlet:param name="jspPage" value="/edit.jsp" />
		<portlet:param name="formUploadId" value="<%=String.valueOf(formUpload.getId())%>" />
	</portlet:actionURL>

                <!--
                Create a column text tag for each column you want to display. 
                The name field is localized. Set a key that can be translated 
                from the Language.properties files. 
                -->
                <%
                	String fullNume = formUpload.getPrenume() + " " + formUpload.getNume();
                		
                	FileModel fileModel;
                	String numeFisier = "-";
                	String sizeFisier = "-";
                	if (formUpload.getFileName() != null) {
                		numeFisier = formUpload.getFileName();
                		sizeFisier = formUpload.getFileSize() + " KB";
                	}
                %>
                
			<liferay-ui:search-container-column-text
					href="<%=editURL %>" 
                    name="nume" 
                    value="<%=fullNume %>" 
                    orderable="<%= true %>"
					orderableProperty="nume"/>

			<liferay-ui:search-container-column-text
					href="<%=editURL %>" 
                    name="email" 
                    orderable="<%= true %>"
					orderableProperty="email"/>
					
			<liferay-ui:search-container-column-text
					href="<%=editURL %>" 
                    name="telefon" 
                    orderable="<%= true %>"
					orderableProperty="telefon"/>
			
			<liferay-ui:search-container-column-text 
                    name="fileName"
                    value="<%=numeFisier %>"
                    orderable="<%= false %>"/>		
			
			<liferay-ui:search-container-column-text 
                    name="fileSize"
                    value="<%=sizeFisier %>"
                    orderable="<%= false %>"/>
			
            <liferay-ui:search-container-column-text 
                    name="createdAt"
                    value="<%=FormularUploadUtil.formatDate(formUpload.getCreatedAt()) %>"
                    orderable="<%= true %>"
					orderableProperty="createdAt"/> 
            

        </liferay-ui:search-container-row> 

        <!--

        Iterator will paginate your results as necessary. The "paginate"
        paramater is true by default. Set to false to make sure all your
        items appear on one page.
        --> 

	<liferay-ui:search-iterator /> 
</liferay-ui:search-container>
		
		
		
		<div class="row">
			<div class="col-md-12">
				<a href="<portlet:renderURL><portlet:param name="jspPage" value="/view.jsp"/></portlet:renderURL>">
					<button type="button" class="btn btn-primary"><liferay-ui:message key="back" /></button>
				</a>
			</div>
		</div>
		
<%
		
	}

%>
