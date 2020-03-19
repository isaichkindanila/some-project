<#-- @ftlvariable name="_csrf" type="org.springframework.security.web.csrf.CsrfToken" -->
<#-- @ftlvariable name="error" type="java.lang.String" -->
<#import "lib/html.ftl" as H>
<@H.html>
    <@H.head "Sign up"/>
    <@H.body>
        <h1 id="error">${error!""}</h1>
        <form id="signUpForm" method="post" action="/signUp">
            <input id="email" type="email" name="email" placeholder="Email">
            <input id="p1" type="password" name="password" placeholder="Password">
            <input id="p2" type="password" placeholder="Repeat password">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
            <input id="submitButton" type="button" value="Sign up">
        </form>
    </@H.body>
    <script src="/static/js/signUp.js"></script>
</@H.html>