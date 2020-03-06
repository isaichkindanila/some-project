<#import "lib/html.ftl" as H>
<@H.html>
    <@H.head "Home page"/>
    <@H.body>
        <#if auth??>
            <a href="/files">Files</a>
            <a href="/signOut">Sign out</a>
        <#else>
            <a href="/signIn">Sign in</a>
            <a href="/signUp">Sign up</a>
        </#if>
    </@H.body>
</@H.html>