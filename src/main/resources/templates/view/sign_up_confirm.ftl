<#-- @ftlvariable name="email" type="java.lang.String" -->
<#import "lib/html.ftl" as H>
<@H.html>
    <@H.head "Confirm email"/>
    <@H.body>
        <h1>Conformation email sent to <code>${email}</code></h1>
        <a href="/">Back to home page</a>
    </@H.body>
</@H.html>