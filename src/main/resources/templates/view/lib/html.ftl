<#macro html>
    <!doctype html>
    <html lang="en">
        <#nested>
    </html>
</#macro>

<#macro head title>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>${title}</title>
        <#nested>
    </head>
</#macro>

<#macro body>
    <body>
        <#nested>
    </body>
</#macro>