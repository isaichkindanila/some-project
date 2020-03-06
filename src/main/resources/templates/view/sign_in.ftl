<#import "lib/html.ftl" as H>
<@H.html>
    <@H.head "Sign in"/>
    <@H.body>
        <form action="/signIn" method="post">
            <input name="email" type="email" placeholder="Email">
            <input name="password" type="password" placeholder="Password">
            <input type="submit" value="Sign in">
        </form>
    </@H.body>
</@H.html>