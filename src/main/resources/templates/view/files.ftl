<#-- @ftlvariable name="files" type="java.util.List<ru.itis.some.project.dto.FileInfoDto>" -->
<#import "lib/html.ftl" as H>
<@H.html>
    <@H.head "Files"/>
    <@H.body>
        <form action="/files" method="post" enctype="multipart/form-data">
            <input type="file" name="file">
            <input type="submit" value="Upload">
        </form>
        <hr>
        <#list files>
            <ul>
                <#items as file>
                    <li><a href="/files/${file.token}">${file.name}</a></li>
                </#items>
            </ul>
        </#list>
    </@H.body>
</@H.html>