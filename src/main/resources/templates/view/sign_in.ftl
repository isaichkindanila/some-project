<#-- @ftlvariable name="_csrf" type="org.springframework.security.web.csrf.CsrfToken" -->
<#import "lib/html.ftl" as H>
<@H.html>
    <@H.head "Sign in"/>
    <@H.body>
        <form action="/signIn" method="post">
            <input name="email" type="email" placeholder="Email">
            <input name="password" type="password" placeholder="Password">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
            <input type="submit" value="Sign in">
        </form>
    </@H.body>
</@H.html>