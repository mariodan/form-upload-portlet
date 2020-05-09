

<%@ include file="/init.jsp" %>

<%

	String title = LocalizationUtil.getPreferencesValue(preferences, "title", themeDisplay.getLanguageId());
	String description = LocalizationUtil.getPreferencesValue(preferences, "description", themeDisplay.getLanguageId());
	boolean requireCaptcha = GetterUtil.getBoolean(preferences.getValue("requireCaptcha", StringPool.BLANK));
		 
%>


<portlet:actionURL var="uploadFormURL">
	<portlet:param name="<%= ActionRequest.ACTION_NAME %>" value="uploadForm" />
</portlet:actionURL>


<liferay-ui:error exception="<%= CaptchaMaxChallengesException.class %>" message="maximum-number-of-captcha-attempts-exceeded" />
<liferay-ui:error exception="<%= CaptchaTextException.class %>" message="text-verification-failed" />
<liferay-ui:error key="error" message="campurile-rosu-required" />

<div class="row">
	<div class="col-md-12">
	
		<h2><liferay-ui:message key="title" /></h2>
		<br>
		<br>
		
		<aui:form action="<%= uploadFormURL %>" method="post" name="fm" role="form" cssClass="form-horizontal">

			<br>
			
			<h3><liferay-ui:message key="formular-upload" /></h3>
			<p class="form_subtitle"><liferay-ui:message key="text-descriere-formular" /></p>
			<div class="row">
				<div class="col-md-12">
				
					<div class="form-group">
							<label class="col-sm-3 control-label" for="<portlet:namespace />nume"><liferay-ui:message key="nume" /></label>					
							<div class="col-sm-3 <%=ParamUtil.getString(renderRequest,"error-nume")%>">
								<input id="<portlet:namespace />nume" name="<portlet:namespace />nume" type="text" placeholder="" class="form-control" value="${nume}"  maxlength="30">
								<c:if test='<%= Validator.isNotNull(ParamUtil.getString(renderRequest,"error-nume-span")) %>'>
									<span class="<%=ParamUtil.getString(renderRequest,"error-nume-span")%>"></span>
								</c:if>
							</div>
							
							<label class="col-sm-3 control-label" for="<portlet:namespace />prenume"><liferay-ui:message key="prenume" /></label> 						
							<div class="col-sm-3 <%=ParamUtil.getString(renderRequest,"error-prenume")%>">
								<input id="<portlet:namespace />prenume" name="<portlet:namespace />prenume" type="text" placeholder="" class="form-control" value="${prenume}"  maxlength="30">
								<c:if test='<%= Validator.isNotNull(ParamUtil.getString(renderRequest,"error-prenume-span")) %>'>
									<span class="<%=ParamUtil.getString(renderRequest,"error-prenume-span")%>"></span>
								</c:if>
							</div>
					</div>
					
					<div class="form-group">
							<label class="col-sm-3 control-label" for="<portlet:namespace />email"><liferay-ui:message key="email" /></label> 						
							<div class="col-sm-3 <%=ParamUtil.getString(renderRequest,"error-email")%>">
								<liferay-ui:error key="email-invalid" message="email-invalid" />
								<input id="<portlet:namespace />email" name="<portlet:namespace />email" type="email" placeholder="" class="form-control" value="${email}"  maxlength="128">
								<c:if test='<%= Validator.isNotNull(ParamUtil.getString(renderRequest,"error-email-span")) %>'>
									<span class="<%=ParamUtil.getString(renderRequest,"error-email-span")%>"></span>
								</c:if>
							</div>
						
							<label class="col-sm-3 control-label" for="<portlet:namespace />telefon"><liferay-ui:message key="telefon" /></label> 						
							<div class="col-sm-3 <%=ParamUtil.getString(renderRequest,"error-telefon")%>">
								<liferay-ui:error key="telefon-invalid" message="telefon-invalid" />
								<input id="<portlet:namespace />telefon" name="<portlet:namespace />telefon" type="text" placeholder="" class="form-control" value="${telefon}"  maxlength="10">
								<c:if test='<%= Validator.isNotNull(ParamUtil.getString(renderRequest,"error-telefon-span"))%>'>
										<span class="<%=ParamUtil.getString(renderRequest,"error-telefon-span")%>"></span>
								</c:if>
							</div>
					</div>
					
					<div class="form-group">
						<label class="col-sm-3 control-label" for="<portlet:namespace />file"><liferay-ui:message key="file" /></label> 						
						<div class="col-sm-9 <%=ParamUtil.getString(renderRequest,"error-file")%>">
							<input id="<portlet:namespace />file" name="<portlet:namespace />file" type="text" placeholder="" class="form-control" value="${adresa}" maxlength="250">
							<c:if test='<%= Validator.isNotNull(ParamUtil.getString(renderRequest,"error-file-span")) %>'>
								<span class="<%=ParamUtil.getString(renderRequest,"error-file-span")%>"></span>
							</c:if>
						</div>
						
						
						<div class="list-item_file-upload">
                             <span class="list-item_file-type">PDF/JPG/PNG</span>
                             <label class="custom-file-upload">
                                  <input type="file" id="file-document" name="file-document" accept="image/png, image/jpeg, application/pdf"/>
                                        <i class="fa fa-cloud-upload"></i><liferay-ui:message key="incarca" />
                             </label>
                             <h3>
                             	<label id="file-document-error" class="error" for="file-document">eroare</label>
                             </h3>
                             <span class="uploaded" id="uploaded-tip"></span>
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