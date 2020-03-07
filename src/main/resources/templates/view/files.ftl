<#import "lib/html.ftl" as H>
<@H.html>
    <@H.head "Files"/>
    <@H.body>
        <form action="/files" method="post" enctype="multipart/form-data">
            <input type="file" name="file">
            <input type="submit" value="Upload">
        </form>
    </@H.body>
</@H.html>