<#-- @ftlvariable name="error" type="java.lang.String" -->
<#import "lib/html.ftl" as H>
<@H.html>
    <@H.head "Sign up"/>
    <@H.body>
        <h1 id="error">${error!""}</h1>
        <form id="signUpForm" method="post" action="/signUp">
            <input id="email" type="email" name="email" placeholder="Email">
            <input id="password" type="password" name="password" placeholder="Password">
            <input id="repeatPassword" type="password" placeholder="Repeat password">
            <input id="submitButton" type="button" value="Sign up">
        </form>
    </@H.body>
    <script src="/static/js/signUp.js"></script>
</@H.html>