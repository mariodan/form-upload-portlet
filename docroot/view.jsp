

<%@ include file="/init.jsp" %>

<%

	String title = LocalizationUtil.getPreferencesValue(preferences, "title", themeDisplay.getLanguageId());
	String description = LocalizationUtil.getPreferencesValue(preferences, "description", themeDisplay.getLanguageId());
	
	String subtitle = LocalizationUtil.getPreferencesValue(preferences, "subtitle", themeDisplay.getLanguageId());
	String descriere = LocalizationUtil.getPreferencesValue(preferences, "descriere", themeDisplay.getLanguageId());
	
	boolean requireCaptcha = GetterUtil.getBoolean(preferences.getValue("requireCaptcha", StringPool.BLANK));
	
	/* Fetch required fields from configuration */
	Map<String, Boolean> requiredFields = new HashMap<String, Boolean>();
	
	requiredFields.put("nume", GetterUtil.getBoolean(preferences.getValue("nume", StringPool.BLANK)));
	requiredFields.put("prenume", GetterUtil.getBoolean(preferences.getValue("prenume", StringPool.BLANK)));
	requiredFields.put("email", GetterUtil.getBoolean(preferences.getValue("email", StringPool.BLANK)));
	requiredFields.put("telefon", GetterUtil.getBoolean(preferences.getValue("telefon", StringPool.BLANK)));
	requiredFields.put("file", GetterUtil.getBoolean(preferences.getValue("file", StringPool.BLANK)));
	
	
	Map<String, String> requiredValues = new HashMap<String, String>();
	for(String key: requiredFields.keySet()) {
		
		if(requiredFields.get(key)) {
			requiredValues.put(key, "required");
		} else {
			requiredValues.put(key, StringPool.BLANK);
		}
	}
%>


<portlet:actionURL var="uploadFormURL">
	<portlet:param name="<%= ActionRequest.ACTION_NAME %>" value="uploadForm" />
</portlet:actionURL>


<liferay-ui:error exception="<%= CaptchaMaxChallengesException.class %>" message="maximum-number-of-captcha-attempts-exceeded" />
<liferay-ui:error exception="<%= CaptchaTextException.class %>" message="text-verification-failed" />
<liferay-ui:error key="error" message="campurile-rosu-required" />


<div class="row">
	<div class="col-md-12">
	
		<% if(themeDisplay.isSignedIn()) {
			
			%>
				
					<!-- Indicates caution should be taken with this action -->
					<a href="<portlet:renderURL><portlet:param name="jspPage" value="/view-files.jsp" /></portlet:renderURL>">
						<button class="btn btn-warning"><liferay-ui:message key="view-uploaded-files" /></button>
					</a>
					<br>
					<br>
					
			<%
			
		} %>
	
	</div>
</div>


<div class="row">
	<div class="col-md-12">
	
		<h2><%=title %></h2>
		<br>
		<br>
		
		<aui:form action="<%= uploadFormURL %>" enctype="multipart/form-data" method="post" name="fm" role="form" cssClass="form-horizontal">

			<br>
			
			<h3><%=subtitle %></h3>
			<p class="form_subtitle"><%=descriere %></p>
			<div class="row">
				<div class="col-md-12">
					<div class="form-group">
							<label class="col-sm-3 control-label" for="<portlet:namespace />nume"><liferay-ui:message key="nume" /></label>
							<div class="col-sm-3 <%=ParamUtil.getString(renderRequest,"error-nume")%>">
								<input id="<portlet:namespace />nume" name="<portlet:namespace />nume" type="text" placeholder="" class="form-control" value="${nume}"  maxlength="30" <%=requiredValues.get("nume") %>>
								<c:if test='<%= Validator.isNotNull(ParamUtil.getString(renderRequest,"error-nume-span")) %>'>
									<span class="<%=ParamUtil.getString(renderRequest,"error-nume-span")%>"></span>
								</c:if>
							</div>
						
							<label class="col-sm-3 control-label" for="<portlet:namespace />prenume"><liferay-ui:message key="prenume" /></label> 						
							<div class="col-sm-3 <%=ParamUtil.getString(renderRequest,"error-prenume")%>">
								<input id="<portlet:namespace />prenume" name="<portlet:namespace />prenume" type="text" placeholder="" class="form-control" value="${prenume}"  maxlength="30" <%=requiredValues.get("prenume") %>>
								<c:if test='<%= Validator.isNotNull(ParamUtil.getString(renderRequest,"error-prenume-span")) %>'>
									<span class="<%=ParamUtil.getString(renderRequest,"error-prenume-span")%>"></span>
								</c:if>
							</div>
					</div>
					
					<div class="form-group">
							<label class="col-sm-3 control-label" for="<portlet:namespace />email"><liferay-ui:message key="email" /></label> 						
							<div class="col-sm-3 <%=ParamUtil.getString(renderRequest,"error-email")%>">
								<liferay-ui:error key="email-invalid" message="email-invalid" />
								<input id="<portlet:namespace />email" name="<portlet:namespace />email" type="email" placeholder="" class="form-control" value="${email}"  maxlength="128" <%=requiredValues.get("email") %>>
								<c:if test='<%= Validator.isNotNull(ParamUtil.getString(renderRequest,"error-email-span")) %>'>
									<span class="<%=ParamUtil.getString(renderRequest,"error-email-span")%>"></span>
								</c:if>
							</div>
						
							<label class="col-sm-3 control-label" for="<portlet:namespace />telefon"><liferay-ui:message key="telefon" /></label> 						
							<div class="col-sm-3 <%=ParamUtil.getString(renderRequest,"error-telefon")%>">
								<liferay-ui:error key="telefon-invalid" message="telefon-invalid" />
								<input id="<portlet:namespace />telefon" name="<portlet:namespace />telefon" type="number" inputmode="numeric" minlength="10" maxlength="10" placeholder="" class="form-control" value="${telefon}"  maxlength="10" <%=requiredValues.get("telefon") %>>
								<c:if test='<%= Validator.isNotNull(ParamUtil.getString(renderRequest,"error-telefon-span"))%>'>
										<span class="<%=ParamUtil.getString(renderRequest,"error-telefon-span")%>"></span>
								</c:if>
							</div>
					</div>
					
					<div class="form-group">
						<label class="col-sm-3 control-label" for="<portlet:namespace />file"><liferay-ui:message key="file" /></label> 						
						<div class="col-sm-9 <%=ParamUtil.getString(renderRequest, "error-file")%>">
							<liferay-ui:error key="file-invalid" message="file-invalid" />
							<liferay-ui:error key="file-invalid-extension" message="file-invalid-extension" />
							<liferay-ui:error key="file-invalid-size" message="file-invalid-size" />
							<liferay-ui:error key="file-invalid-name" message="file-invalid-name" />
							<label style="font-size: 12px;"><liferay-ui:message key="fisiere-acceptate" />: <%=allowedExtension %></label>
							<input id="<portlet:namespace />file" name="<portlet:namespace />file" type="file" name="<portlet:namespace />file" accept="image/png, image/jpeg, application/pdf"/>
                                        
							<c:if test='<%= Validator.isNotNull(ParamUtil.getString(renderRequest,"error-file-span")) %>'>
								<span class="<%=ParamUtil.getString(renderRequest,"error-file-span")%>"></span>
							</c:if>
						</div>
					</div>
					
				</div>
			</div>

			<br>
			
			<div class="row">
				<div class="form-group">
					<div class="col-sm-offset-3 col-sm-9">
						<button class="g-recaptcha btn btn-custom-form orange pull-left" onClick="" data-sitekey="6LcCSl4UAAAAAHc41o6j6r_VWh7uol55hSOgstWu" data-callback='onSubmitUpload' type="submit" value="send">
							<liferay-ui:message key="buton-trimite-upload" />
						</button>
					</div>
				</div>
			</div>
			
		</aui:form>
	</div>
</div>

<script>
function onSubmitUpload(token) {
	document.getElementById("<portlet:namespace />fm").submit();
}
</script>