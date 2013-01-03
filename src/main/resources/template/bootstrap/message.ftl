<#if !messages.isEmpty()>
<#list allMessages as message>
    <div<#rt/>
        <#if parameters.id?if_exists != "">
            id="${parameters.id?html}"<#rt/>
        </#if>
        class="alert
            <#if message.level??>
                <#if message.level == "FATAL">alert-error</#if>
                <#if message.level == "ERROR">alert-error</#if>
                <#if message.level == "WARN"></#if>
                <#if message.level == "INFO">alert-info</#if>
            <#else>
                alert-success
            </#if>
            <#if parameters.cssClass??>${parameters.cssClass?html}</#if>
        "<#rt/>
        <#if parameters.cssStyle??>
            style="${parameters.cssStyle?html}"<#rt/>
        </#if>
    ><#rt/>
        <#if message.text?if_exists != "">
            <a class="close" data-dismiss="alert" href="#">&times;</a><#rt/>
            <#if message.level??>
                <#if message.level == "FATAL"><h4>Uh-oh...</h4></#if>
                <#if message.level == "ERROR"><h4>Oh snap!</h4></#if>
                <#if message.level == "WARN"><h4>Warning!</h4></#if>
                <#if message.level == "INFO"><h4>Heads up!</h4></#if>
            <#else>
                <h4>Success!</h4>
            </#if><#rt/>
            <#if parameters.escape>${message.text!?html}<#else>${message.text!}</#if><#rt/>
        </#if>
    </div>
</#list>
</#if>